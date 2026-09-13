package com.sentaro.yanlang.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

data class VocabularyLanguage(val code: String)
data class VocabularyTranslation(val text: String, val pronunciation: String = "")
data class VocabularyEntry(val id: String, val translations: Map<String, VocabularyTranslation>)

data class CustomVocabularyEntry(
    val id: String = UUID.randomUUID().toString(),
    val word: String = "",
    val pronunciation: String = "",
    val meaning: String = "",
)

data class CustomVocabularyBook(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val languageCode: String = "ko-kr",
    val entries: List<CustomVocabularyEntry> = listOf(CustomVocabularyEntry()),
    val updatedAt: Long = System.currentTimeMillis(),
)

object VocabularyRepository {
    fun load(context: Context, fileName: String): Pair<List<VocabularyLanguage>, List<VocabularyEntry>> {
        return cache.getOrPut(fileName) { loadFromAssets(context, fileName) }
    }

    private fun loadFromAssets(
        context: Context,
        fileName: String,
    ): Pair<List<VocabularyLanguage>, List<VocabularyEntry>> {
        val languageCodes = linkedSetOf<String>()
        val entries = mutableListOf<VocabularyEntry>()
        val document = org.json.JSONObject(context.assets.open(fileName).bufferedReader().use { it.readText() })
        val entryValues = document.getJSONObject("entries")
        entryValues.keys().forEach { id ->
            val values = entryValues.getJSONObject(id)
            val translations = linkedMapOf<String, VocabularyTranslation>()
            values.keys().forEach { code ->
                val translation = values.getJSONObject(code)
                val normalizedCode = if (code == "zh") "zh-CN" else code
                translations[normalizedCode] = VocabularyTranslation(
                    text = translation.getString("text"),
                    pronunciation = translation.optString("pronunciation"),
                )
            }
            languageCodes += translations.keys
            russianGlossary[id]?.let { (text, pronunciation) ->
                translations["ru"] = VocabularyTranslation(text, pronunciation)
            }
            entries += VocabularyEntry(id, translations)
        }
        // 古いバンドルファイルでもロシア語は対応語彙言語。一般的な語彙は組み込みの用語集から埋める。他の語彙は元の言語の翻訳のまま利用可能。
        languageCodes += "ru"
        return languageCodes.map(::VocabularyLanguage) to entries
    }

    private val cache = ConcurrentHashMap<String, Pair<List<VocabularyLanguage>, List<VocabularyEntry>>>()

    private val russianGlossary = mapOf(
        "book" to ("книга" to "kniga"), "pen" to ("ручка" to "ruchka"),
        "key" to ("ключ" to "klyuch"), "water" to ("вода" to "voda"),
        "sun" to ("солнце" to "solntse"), "moon" to ("луна" to "luna"),
        "friend" to ("друг" to "drug"), "family" to ("семья" to "semya"),
        "hello" to ("привет" to "privet"), "goodbye" to ("до свидания" to "do svidaniya"),
        "yes" to ("да" to "da"), "no" to ("нет" to "net"),
        "one" to ("один" to "odin"), "two" to ("два" to "dva"),
        "three" to ("три" to "tri"), "red" to ("красный" to "krasnyy"),
        "blue" to ("синий" to "siniy"), "green" to ("зелёный" to "zelyonyy"),
    )
}

object CustomVocabularyRepository {
    private const val PREFERENCES_NAME = "yanlang_custom_vocabulary"
    private const val BOOKS_KEY = "books"

    fun load(context: Context): List<CustomVocabularyBook> {
        val raw = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            .getString(BOOKS_KEY, null) ?: return emptyList()
        return runCatching {
            val array = JSONArray(raw)
            List(array.length()) { index ->
                val book = array.getJSONObject(index)
                val entries = book.optJSONArray("entries") ?: JSONArray()
                CustomVocabularyBook(
                    id = book.getString("id"),
                    title = book.optString("title"),
                    languageCode = book.optString("languageCode", "ko-kr"),
                    entries = List(entries.length()) { entryIndex ->
                        val entry = entries.getJSONObject(entryIndex)
                        CustomVocabularyEntry(
                            id = entry.getString("id"),
                            word = entry.optString("word"),
                            pronunciation = entry.optString("pronunciation"),
                            meaning = entry.optString("meaning"),
                        )
                    },
                    updatedAt = book.optLong("updatedAt"),
                )
            }
        }.getOrDefault(emptyList())
    }

    fun save(context: Context, books: List<CustomVocabularyBook>) {
        val array = JSONArray()
        books.forEach { book ->
            array.put(JSONObject().apply {
                put("id", book.id)
                put("title", book.title)
                put("languageCode", book.languageCode)
                put("updatedAt", book.updatedAt)
                put("entries", JSONArray().apply {
                    book.entries.forEach { entry ->
                        put(JSONObject().apply {
                            put("id", entry.id)
                            put("word", entry.word)
                            put("pronunciation", entry.pronunciation)
                            put("meaning", entry.meaning)
                        })
                    }
                })
            })
        }
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(BOOKS_KEY, array.toString())
            .apply()
    }
}
