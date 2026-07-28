package com.sentaro.yanlang.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.sentaro.yanlang.R

@Composable
internal fun AnalyticsConsentDialog(onConsent: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        text = { Text(stringResource(R.string.analytics_consent_message)) },
        confirmButton = {
            TextButton(onClick = onConsent) {
                Text(stringResource(R.string.analytics_consent_accept))
            }
        },
    )
}
