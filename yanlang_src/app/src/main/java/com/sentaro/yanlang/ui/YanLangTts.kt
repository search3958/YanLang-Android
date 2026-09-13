package com.sentaro.yanlang.ui

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.io.File
import java.util.Locale
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

private const val YANLANG_TTS_TAG = "YanLangTts"
private const val MAX_CACHED_UTTERANCES = 96

internal class YanLangTts(
    context: Context,
    private val languageCode: String,
    private val lowVolumeMessage: String,
) {
    private data class PendingAudio(
        val text: String,
        val file: File,
        val fromPreload: Boolean,
        val playAfterLoad: Boolean,
    )

    private val appContext = context.applicationContext
    private val audioManager = appContext.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
    private val soundPool = SoundPool.Builder()
        .setMaxStreams(2)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build(),
        )
        .build()

    private val soundCache = ConcurrentHashMap<String, Int>()
    private val pendingSoundIds = ConcurrentHashMap<Int, PendingAudio>()
    private val pendingRequests = ConcurrentHashMap<String, PendingAudio>()
    private val loadingTexts = ConcurrentHashMap.newKeySet<String>()
    private val preloadQueue = ConcurrentLinkedQueue<String>()
    private val requestedPreloadTexts = ConcurrentLinkedQueue<String>()
    private val pendingSpeakText = java.util.concurrent.atomic.AtomicReference<String?>(null)
    private val preloadRunning = AtomicBoolean(false)
    private val cacheFiles = ConcurrentHashMap.newKeySet<File>()

    private var tts: TextToSpeech? = null
    private var initialized = false
    private var languageUsable = false
    private var released = false
    private var triedFallbackEngine = false
    private val lowVolumeWarningShown = AtomicBoolean(false)

    init {
        soundPool.setOnLoadCompleteListener { _, soundId, status ->
            val request = pendingSoundIds.remove(soundId) ?: return@setOnLoadCompleteListener
            loadingTexts.remove(request.text)

            if (status != 0 || released) {
                Log.e(YANLANG_TTS_TAG, "SoundPool load failed: status=$status textLength=${request.text.length}")
                soundPool.unload(soundId)
                deleteCacheFile(request.file)
                finishPreload(request)
                return@setOnLoadCompleteListener
            }

            soundCache[request.text] = soundId
            Log.d(YANLANG_TTS_TAG, "RAM cache ready: textLength=${request.text.length} soundId=$soundId")

            if (request.playAfterLoad) {
                playCached(request.text, soundId)
            }
            deleteCacheFile(request.file)
            finishPreload(request)
        }

        initializeTts()
    }

    private fun initializeTts(enginePackage: String? = null) {
        if (released) return
        Log.d(YANLANG_TTS_TAG, "Initializing Android TextToSpeech: languageCode=$languageCode engine=${enginePackage ?: "default"}")
        tts = if (enginePackage.isNullOrBlank()) {
            TextToSpeech(appContext, ::onTtsInitialized)
        } else {
            TextToSpeech(appContext, ::onTtsInitialized, enginePackage)
        }
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) = Unit

            override fun onDone(utteranceId: String?) {
                if (released || utteranceId.isNullOrBlank()) return
                val request = pendingRequests.remove(utteranceId) ?: return
                loadSynthesizedAudio(request)
            }

            override fun onError(utteranceId: String?) {
                val request = utteranceId?.let(pendingRequests::remove)
                if (request == null) {
                    Log.e(YANLANG_TTS_TAG, "TTS utterance error: utteranceId=$utteranceId")
                    return
                }
                loadingTexts.remove(request.text)
                deleteCacheFile(request.file)
                Log.e(YANLANG_TTS_TAG, "TTS synthesis error: textLength=${request.text.length} preload=${request.fromPreload}")
                finishPreload(request)
            }
        })
    }

    private fun onTtsInitialized(status: Int) {
        if (released) return
        initialized = status == TextToSpeech.SUCCESS
        Log.d(YANLANG_TTS_TAG, "TTS initialized: success=$initialized status=$status")

        if (!initialized) {
            tryFallbackEngine()
            return
        }

        configureLanguage()
        if (!languageUsable) {
            tryFallbackEngine()
            return
        }

        // TTS初期化完了前に表示が発生する可能性がある。そのリクエストを絶対に失わないようにする。
        pendingSpeakText.getAndSet(null)?.let { text ->
            Log.d(YANLANG_TTS_TAG, "Playing queued reveal after TTS initialization: textLength=${text.length}")
            speak(text)
        }
        preload(requestedPreloadTexts.toList())
    }

    private fun tryFallbackEngine() {
        if (triedFallbackEngine || released) return
        triedFallbackEngine = true
        val candidates = runCatching { tts?.engines ?: emptyList() }.getOrDefault(emptyList())
        Log.d(YANLANG_TTS_TAG, "Installed TTS engines=${candidates.map { it.name }}")
        val fallback = candidates.firstOrNull { it.name != tts?.defaultEngine }
        if (fallback == null) {
            Log.e(YANLANG_TTS_TAG, "No fallback TTS engine is available on this device")
            return
        }
        tts?.shutdown()
        tts = null
        initialized = false
        languageUsable = false
        initializeTts(fallback.name)
    }

    private fun configureLanguage() {
        val engine = tts ?: return
        val locale = localeForLanguageCode(languageCode)
        val result = engine.setLanguage(locale)
        languageUsable = result == TextToSpeech.LANG_AVAILABLE ||
            result == TextToSpeech.LANG_COUNTRY_AVAILABLE ||
            result == TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE
        Log.d(YANLANG_TTS_TAG, "TTS locale configured: locale=$locale result=$result usable=$languageUsable")
        if (!languageUsable) {
            Log.e(YANLANG_TTS_TAG, "TTS language data is unavailable: locale=$locale")
        }
    }

    fun preload(texts: Collection<String>) {
        if (released) return
        val ordered = texts.asSequence()
            .map(String::trim)
            .filter(String::isNotBlank)
            .distinct()
            .take(MAX_CACHED_UTTERANCES)
            .toList()

        preloadQueue.clear()
        requestedPreloadTexts.clear()
        ordered.forEach { text ->
            requestedPreloadTexts.offer(text)
            if (!soundCache.containsKey(text) && !loadingTexts.contains(text)) {
                preloadQueue.offer(text)
            }
        }
        Log.d(YANLANG_TTS_TAG, "Preload requested: count=${ordered.size} initialized=$initialized usable=$languageUsable")
        preloadNext()
    }

    private fun preloadNext() {
        if (released || !initialized || !languageUsable) return
        if (!preloadRunning.compareAndSet(false, true)) return

        while (true) {
            val text = preloadQueue.poll()
            if (text == null) {
                preloadRunning.set(false)
                Log.d(YANLANG_TTS_TAG, "Preload queue completed")
                return
            }
            if (soundCache.containsKey(text) || loadingTexts.contains(text)) continue
            if (synthesizeToCache(text, fromPreload = true, playAfterLoad = false)) return
        }
    }

    private fun synthesizeToCache(text: String, fromPreload: Boolean, playAfterLoad: Boolean): Boolean {
        val engine = tts
        if (released || !initialized || !languageUsable || engine == null) {
            if (fromPreload) preloadRunning.set(false)
            Log.e(YANLANG_TTS_TAG, "Cannot synthesize: initialized=$initialized usable=$languageUsable engine=${engine != null}")
            return false
        }
        if (!loadingTexts.add(text)) return true

        val file = File(appContext.cacheDir, "yanlang_tts_${UUID.randomUUID()}.wav")
        cacheFiles.add(file)
        val utteranceId = "yanlang_cache_${UUID.randomUUID()}"
        pendingRequests[utteranceId] = PendingAudio(text, file, fromPreload, playAfterLoad)

        val result = engine.synthesizeToFile(text, android.os.Bundle(), file, utteranceId)
        Log.d(YANLANG_TTS_TAG, "synthesizeToFile requested: textLength=${text.length} result=$result preload=$fromPreload")
        if (result == TextToSpeech.ERROR) {
            pendingRequests.remove(utteranceId)
            loadingTexts.remove(text)
            deleteCacheFile(file)
            finishPreload(PendingAudio(text, file, fromPreload, playAfterLoad))
            return false
        }
        return true
    }

    private fun loadSynthesizedAudio(request: PendingAudio) {
        if (released) {
            deleteCacheFile(request.file)
            finishPreload(request)
            return
        }
        val soundId = soundPool.load(request.file.absolutePath, 1)
        if (soundId == 0) {
            loadingTexts.remove(request.text)
            deleteCacheFile(request.file)
            Log.e(YANLANG_TTS_TAG, "SoundPool.load returned 0: textLength=${request.text.length}")
            finishPreload(request)
            return
        }
        pendingSoundIds[soundId] = request
        Log.d(YANLANG_TTS_TAG, "Synthesis completed; SoundPool RAM load queued: textLength=${request.text.length}")
    }

    private fun finishPreload(request: PendingAudio) {
        if (request.fromPreload) {
            preloadRunning.set(false)
            preloadNext()
        }
    }

    fun speak(text: String) {
        val normalized = text.trim()
        if (released || normalized.isBlank()) {
            Log.d(YANLANG_TTS_TAG, "Speak skipped: released=$released blank=${normalized.isBlank()}")
            return
        }

        val cachedId = soundCache[normalized]
        if (cachedId != null) {
            playCached(normalized, cachedId)
            return
        }

        if (!initialized || !languageUsable || tts == null) {
            pendingSpeakText.set(normalized)
            Log.d(YANLANG_TTS_TAG, "Speak queued until TTS initialization: textLength=${normalized.length}")
            return
        }

        if (loadingTexts.contains(normalized)) {
            Log.d(YANLANG_TTS_TAG, "Speak requested while cache is loading; direct TTS playback will start now")
            speakDirect(normalized)
            return
        }

        speakDirect(normalized)
        synthesizeToCache(normalized, fromPreload = false, playAfterLoad = false)
    }

    private fun speakDirect(text: String) {
        val engine = tts
        if (released || !initialized || !languageUsable || engine == null) {
            pendingSpeakText.set(text)
            Log.e(YANLANG_TTS_TAG, "Direct TTS unavailable; queued: initialized=$initialized usable=$languageUsable")
            return
        }
        val utteranceId = "yanlang_speak_${UUID.randomUUID()}"
        val result = engine.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
        if (result == TextToSpeech.ERROR) {
            Log.e(YANLANG_TTS_TAG, "TextToSpeech.speak returned ERROR: textLength=${text.length}")
        } else {
            Log.d(YANLANG_TTS_TAG, "Direct TTS playback requested: textLength=${text.length}")
        }
    }

    private fun playCached(text: String, soundId: Int) {
        val streamId = soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
        if (streamId == 0) {
            Log.e(YANLANG_TTS_TAG, "SoundPool playback returned 0; falling back to Android TTS: textLength=${text.length}")
            speakDirect(text)
        } else {
            Log.d(YANLANG_TTS_TAG, "RAM cached playback started: textLength=${text.length} streamId=$streamId")
        }
    }

    fun warnLowVolumeOnce(): Boolean {
        if (released || lowVolumeWarningShown.get()) return false
        if (!isMusicVolumeTooLow()) return false
        if (!lowVolumeWarningShown.compareAndSet(false, true)) return false
        Toast.makeText(appContext, lowVolumeMessage, Toast.LENGTH_SHORT).show()
        Log.d(YANLANG_TTS_TAG, "Low-volume warning displayed once for this word screen")
        return true
    }

    private fun isMusicVolumeTooLow(): Boolean {
        val manager = audioManager ?: run {
            Log.e(YANLANG_TTS_TAG, "AudioManager unavailable")
            return false
        }
        val max = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val current = manager.getStreamVolume(AudioManager.STREAM_MUSIC)
        if (max <= 0) return false
        val percent = current * 100f / max
        Log.d(YANLANG_TTS_TAG, "Music volume=$current/$max percent=${String.format(Locale.US, "%.1f", percent)}")
        return current * 10 <= max
    }

    fun shutdown() {
        if (released) return
        released = true
        initialized = false
        languageUsable = false
        tts?.stop()
        tts?.shutdown()
        tts = null
        soundPool.release()
        pendingRequests.values.forEach { deleteCacheFile(it.file) }
        pendingSoundIds.values.forEach { deleteCacheFile(it.file) }
        cacheFiles.forEach(::deleteCacheFile)
        pendingRequests.clear()
        pendingSoundIds.clear()
        preloadQueue.clear()
        requestedPreloadTexts.clear()
        loadingTexts.clear()
        soundCache.clear()
        Log.d(YANLANG_TTS_TAG, "TTS manager shutdown")
    }

    private fun deleteCacheFile(file: File) {
        cacheFiles.remove(file)
        if (file.exists() && !file.delete()) {
            Log.w(YANLANG_TTS_TAG, "Temporary TTS file could not be deleted: ${file.name}")
        }
    }
}

@Composable
internal fun rememberYanLangTts(
    languageCode: String,
    lowVolumeMessage: String,
): YanLangTts {
    val context = LocalContext.current
    val tts = remember(languageCode) {
        YanLangTts(context, languageCode, lowVolumeMessage)
    }
    DisposableEffect(tts) {
        onDispose { tts.shutdown() }
    }
    return tts
}

@Composable
internal fun YanLangTtsPreload(
    tts: YanLangTts,
    texts: Collection<String>,
) {
    val stableTexts = remember(texts) { texts.toList() }
    LaunchedEffect(tts, stableTexts) {
        tts.preload(stableTexts)
    }
}

private fun localeForLanguageCode(languageCode: String): Locale = when (languageCode.lowercase(Locale.ROOT)) {
    "ja", "ja-jp" -> Locale.JAPAN
    "ko", "ko-kr", "ko-kp" -> Locale.KOREA
    "zh", "zh-cn" -> Locale.SIMPLIFIED_CHINESE
    "zh-tw" -> Locale.TRADITIONAL_CHINESE
    "en", "en-us" -> Locale.US
    "ru", "ru-ru" -> Locale("ru", "RU")
    "es", "es-es" -> Locale("es", "ES")
    else -> Locale.forLanguageTag(languageCode)
}
