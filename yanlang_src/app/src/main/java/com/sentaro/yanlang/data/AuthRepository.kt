package com.sentaro.yanlang.data

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import com.sentaro.yanlang.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class CreditInfo(
    val uuid: String,
    val dailyLimit: Int,
    val tokenCredits: Int,
    val remainingTokens: Int,
    val usagePercent: Double,
    val lastUse: String,
)

object SupabaseAuthClient {
    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_ANON_KEY,
    ) {
        install(Auth)
    }
}

class AuthRepository {
    private val auth = SupabaseAuthClient.client.auth

    val currentUserId: String?
        get() = auth.currentSessionOrNull()?.user?.id

    val isSignedIn: Boolean
        get() = auth.currentSessionOrNull() != null

    suspend fun signInAnonymously(): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            Log.d("AuthRepository", "Starting anonymous sign-in...")
            auth.signInAnonymously()
            val userId = auth.currentSessionOrNull()?.user?.id
                ?: throw IllegalStateException("匿名サインインに失敗しました")
            Log.d("AuthRepository", "Anonymous sign-in success: $userId")
            userId
        }.onFailure { e ->
            Log.e("AuthRepository", "Anonymous sign-in failed: ${e.message}", e)
        }
    }

    suspend fun fetchCredits(): Result<CreditInfo> = withContext(Dispatchers.IO) {
        runCatching {
            val token = auth.currentSessionOrNull()?.accessToken
                ?: throw IllegalStateException("Not authenticated")
            val url = "${BuildConfig.SUPABASE_URL}/functions/v1/yanlang-credits-checker"
            val connection = (URL(url).openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                connectTimeout = 10_000
                readTimeout = 10_000
                doOutput = true
                setRequestProperty("Authorization", "Bearer $token")
                setRequestProperty("Content-Type", "application/json; charset=utf-8")
                setRequestProperty("Accept", "application/json")
            }
            try {
                connection.outputStream.bufferedWriter(Charsets.UTF_8).use { it.write("{}") }
                val status = connection.responseCode
                val responseText = (
                    if (status in 200..299) connection.inputStream else connection.errorStream
                    )?.bufferedReader(Charsets.UTF_8)?.use { it.readText() }.orEmpty()
                if (status !in 200..299) {
                    throw Exception("Credit check failed ($status): $responseText")
                }
                val json = JSONObject(responseText)
                CreditInfo(
                    uuid = json.optString("uuid"),
                    dailyLimit = json.optInt("daily_limit"),
                    tokenCredits = json.optInt("token_credits"),
                    remainingTokens = json.optInt("remaining_tokens"),
                    usagePercent = json.optDouble("usage_percent"),
                    lastUse = json.optString("last_use"),
                )
            } finally {
                connection.disconnect()
            }
        }.onFailure { e ->
            Log.e("AuthRepository", "Fetch credits failed: ${e.message}", e)
        }
    }
}
