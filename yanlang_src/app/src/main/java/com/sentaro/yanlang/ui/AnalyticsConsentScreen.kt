package com.sentaro.yanlang.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sentaro.yanlang.R

@Composable
fun AnalyticsConsentScreen(
    onConsent: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var agreed by remember { mutableStateOf(false) }

    Column(
        modifier
            .fillMaxSize()
            .padding(32.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.weight(1f, true))
        Text(
            stringResource(R.string.analytics_consent_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(20.dp))
        Text(
            stringResource(R.string.analytics_consent_message),
            color = androidx.compose.ui.graphics.Color(0xFF666666),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = {
                agreed = true
                onConsent()
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = SmoothCornerShape(999.dp),
            enabled = !agreed,
        ) {
            Text(stringResource(R.string.analytics_consent_accept), fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.weight(1f, true))
    }
}
