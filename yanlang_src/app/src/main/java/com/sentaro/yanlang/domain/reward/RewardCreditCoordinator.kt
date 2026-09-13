package com.sentaro.yanlang.domain.reward

import android.content.Context
import android.util.Log
import com.sentaro.yanlang.data.AuthRepository
import com.sentaro.yanlang.data.RewardClaimStatus
import com.sentaro.yanlang.data.RewardedCreditAdManager
import kotlinx.coroutines.delay

/** 報酬クレジットワークフローを所有する。Composeはフェーズ/結果のみをレンダリングする。 */
class RewardCreditCoordinator(
    private val authRepository: AuthRepository,
    private val adManager: RewardedCreditAdManager = RewardedCreditAdManager.shared,
) {
    suspend fun run(
        context: Context,
        userId: String,
        onLoadingAd: () -> Unit,
        onVerifying: () -> Unit,
    ): Result<RewardClaimStatus> {
        Log.i(TAG, "REWARD_FLOW_START userIdPresent=${userId.isNotBlank()}")
        val preparation = authRepository.prepareRewardClaim().getOrElse { throw it }
        Log.i(TAG, "REWARD_CLAIM_PREPARED claimIdPresent=${preparation.claimId.isNotBlank()}")

        if (!preparation.customData.contains(userId)) {
            Log.e(TAG, "REWARD_CLAIM_INVALID_USER_BINDING")
            throw IllegalStateException("広告認証情報にユーザーUUIDが含まれていません")
        }

        if (!adManager.isAdAvailable()) {
            onLoadingAd()
            adManager.ensurePreloaded()
        }

        adManager.show(
            context = context,
            customData = preparation.customData,
            userId = userId,
        ).getOrElse { throw it }
        Log.i(TAG, "REWARDED_AD_FINISHED")

        onVerifying()
        delay(POLL_INTERVAL_MS)
        Log.i(TAG, "REWARD_STATUS_POLL_START")

        var finalStatus: RewardClaimStatus? = null
        var attempt = 0
        var pollingFinished = false

        while (attempt < MAX_POLL_ATTEMPTS && !pollingFinished) {
            val statusResult = authRepository.getRewardClaimStatus(preparation.claimId)
            if (statusResult.isSuccess) {
                val status = statusResult.getOrNull()
                if (status == null) {
                    Log.w(TAG, "REWARD_STATUS_EMPTY attempt=${attempt + 1}")
                    if (attempt >= MAX_POLL_ATTEMPTS - 1) break
                    attempt += 1
                    delay(POLL_INTERVAL_MS)
                } else {
                    finalStatus = status
                    Log.i(TAG, "REWARD_STATUS_POLL attempt=${attempt + 1} status=${status.status}")
                    pollingFinished = status.status == VERIFIED_STATUS ||
                        status.status == FAILED_STATUS ||
                        status.status == EXPIRED_STATUS
                    if (!pollingFinished) {
                        attempt += 1
                        delay(POLL_INTERVAL_MS)
                    }
                }
            } else {
                Log.w(TAG, "REWARD_STATUS_REQUEST_FAILED attempt=${attempt + 1}")
                if (attempt >= MAX_POLL_ATTEMPTS - 1) break
                attempt += 1
                delay(POLL_INTERVAL_MS)
            }
        }

        val status = finalStatus
        if (status == null) {
            Log.e(TAG, "REWARD_STATUS_POLL_EXHAUSTED attempts=$MAX_POLL_ATTEMPTS")
            return Result.failure(VerificationPendingException())
        }
        Log.i(TAG, "REWARD_FLOW_FINISHED status=${status.status}")
        return Result.success(status)
    }

    companion object {
        private const val TAG = "RewardCreditCoordinator"
        const val VERIFIED_STATUS = "verified"
        const val FAILED_STATUS = "failed"
        const val EXPIRED_STATUS = "expired"
        private const val POLL_INTERVAL_MS = 1_000L
        private const val MAX_POLL_ATTEMPTS = 60
    }
}

class VerificationPendingException : IllegalStateException()
