package com.sentaro.yanlang

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.sentaro.yanlang.data.LearningRepository
import com.sentaro.yanlang.data.RewardedCreditAdManager
import com.sentaro.yanlang.ui.YanLangApp
import com.sentaro.yanlang.ui.theme.YanLangTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Log.i("YanLangReward", "REWARDED_BUILD_MARKER=20260912-SPEC3")
        RewardedCreditAdManager.shared.initialize(this)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        val repository = LearningRepository(applicationContext)
        setContent {
            YanLangTheme {
                YanLangApp(repository = repository)
            }
        }
        Log.d(TAG, "MainActivity content initialized")
    }

    private companion object {
        const val TAG = "MainActivity"
    }
}
