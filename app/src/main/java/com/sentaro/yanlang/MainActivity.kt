package com.sentaro.yanlang

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sentaro.yanlang.data.LearningRepository
import com.sentaro.yanlang.ui.YanLangApp
import com.sentaro.yanlang.ui.theme.YanLangTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = LearningRepository(applicationContext)
        setContent {
            YanLangTheme {
                YanLangApp(repository = repository)
            }
        }
    }
}
