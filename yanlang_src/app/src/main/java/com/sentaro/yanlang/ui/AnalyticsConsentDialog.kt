package com.sentaro.yanlang.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sentaro.yanlang.R

@Composable
internal fun AnalyticsConsentDialog(onConsent: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    AlertDialog(
        onDismissRequest = {},
        text = {
            Column(
                modifier = Modifier.verticalScroll(ScrollState(0)),
            ) {
                Text(stringResource(R.string.analytics_consent_message))
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
