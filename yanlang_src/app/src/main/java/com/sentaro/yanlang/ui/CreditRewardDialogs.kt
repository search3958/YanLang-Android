package com.sentaro.yanlang.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sentaro.yanlang.R

@Composable
internal fun CreditRewardDialogs(
    showCreditEmptyDialog: Boolean,
    rewardBusy: Boolean,
    rewardPhase: CreditRewardPhase?,
    rewardResult: String?,
    rewardSuccess: Boolean,
    onDismissEmpty: () -> Unit,
    onStartReward: () -> Unit,
    onCloseEmpty: () -> Unit,
    onDismissResult: () -> Unit,
) {
    if (showCreditEmptyDialog) {
        AlertDialog(
            onDismissRequest = onDismissEmpty,
            title = { Text(stringResource(R.string.credit_empty_title)) },
            text = { Text(stringResource(R.string.credit_empty_message)) },
            confirmButton = {
                TextButton(
                    enabled = !rewardBusy,
                    onClick = onStartReward,
                ) {
                    Text(
                        stringResource(R.string.credit_reward_ad),
                        maxLines = 1,
                        softWrap = false,
                    )
                }
            },
            dismissButton = {
                TextButton(
                    enabled = !rewardBusy,
                    onClick = onCloseEmpty,
                ) {
                    Text(stringResource(R.string.credit_empty_close))
                }
            },
        )
    }

    when (rewardPhase) {
        CreditRewardPhase.LOADING_AD -> {
            AlertDialog(
                onDismissRequest = { println("[YanLangApp] Ad-loading dialog dismissal ignored while reward flow is active") },
                title = { Text(stringResource(R.string.credit_reward_ad_loading_title)) },
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(28.dp), strokeWidth = 3.dp)
                        Text(stringResource(R.string.credit_reward_ad_loading))
                    }
                },
                confirmButton = {},
            )
        }
        CreditRewardPhase.RESETTING_CREDITS -> {
            AlertDialog(
                onDismissRequest = { println("[YanLangApp] Credit-reset dialog dismissal ignored while server verification is active") },
                title = { Text(stringResource(R.string.credit_reward_reset_loading_title)) },
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(28.dp), strokeWidth = 3.dp)
                        Text(stringResource(R.string.credit_reward_reset_loading))
                    }
                },
                confirmButton = {},
            )
        }
        null -> Unit
    }

    rewardResult?.let { message ->
        AlertDialog(
            onDismissRequest = onDismissResult,
            title = {
                Text(
                    if (rewardSuccess) stringResource(R.string.credit_reward_success_title)
                    else stringResource(R.string.credit_reward_error_title),
                )
            },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = onDismissResult) {
                    Text(stringResource(R.string.credit_empty_close))
                }
            },
        )
    }
}
