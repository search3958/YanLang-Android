package com.sentaro.yanlang.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sentaro.yanlang.R
import com.sentaro.yanlang.data.NetworkClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.withContext

@Composable
internal fun AnalyticsConsentDialog(onConsent: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    var termsContent by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            termsContent = fetchTerms("https://search3958.github.io/policies/yanlang/policies.md")
            isLoading = false
        }
    }

    AlertDialog(
        onDismissRequest = {},
        text = {
            Column(
                modifier = Modifier.verticalScroll(ScrollState(0)),
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                }
                val displayText = termsContent ?: stringResource(R.string.analytics_consent_loading_message)
                Text(text = displayText)
            }
        },
        confirmButton = {
            TextButton(onClick = onConsent) {
                Text(stringResource(R.string.analytics_consent_accept))
            }
        },
        dismissButton = {
            TextButton(onClick = { uriHandler.openUri("https://search3958.github.io/policies/yanlang/") }) {
                Text(stringResource(R.string.show_latest))
            }
        },
    )
}

private suspend fun fetchTerms(url: String): String {
    return try {
        NetworkClient.http.get(url).body()
    } catch (exception: Exception) {
        "エラー: ${exception.message ?: "不明なエラー"}"
    }
}
