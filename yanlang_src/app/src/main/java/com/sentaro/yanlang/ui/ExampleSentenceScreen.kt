package com.sentaro.yanlang.ui

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.sentaro.yanlang.R

internal data class ExampleSentence(
    val title: String,
    val text: String,
)

internal data class ExampleSentenceGroup(
    val code: String,
    val label: String,
    val sentences: List<ExampleSentence>,
)
internal object ExampleSentenceLibrary {
    val groups = listOf(
        ExampleSentenceGroup(
            code = "ja",
            label = "日本語",
            sentences = listOf(
                ExampleSentence("日常", "朝、窓を開けると涼しい風が入ってきました。今日は少し遠回りして学校へ行きました。"),
                ExampleSentence("旅行", "秋になったら山の町へ旅行したいです。温泉に入って、静かな景色を楽しみたいと思っています。"),
                ExampleSentence("自己紹介", "私は新しいことを試すのが好きです。失敗しても、その原因を考えて次に活かすようにしています。"),
            ),
        ),

        ExampleSentenceGroup(
            code = "en",
            label = "English",
            sentences = listOf(
                ExampleSentence("A rainy day", "It started raining after school. I waited under a roof and watched people hurry home with their umbrellas."),
                ExampleSentence("Photography", "I recently became interested in photography. Now I look for interesting light and small details wherever I go."),
                ExampleSentence("Learning", "I enjoy solving difficult problems. Understanding why my first answer was wrong often teaches me more than getting it right."),
            ),
        ),

        ExampleSentenceGroup(
            code = "ko-kr",
            label = "한국어 (한국)",
            sentences = listOf(
                ExampleSentence("카페", "주말에 친구와 새로 생긴 카페에 갔어요. 창가에 앉아서 한참 이야기를 나눴어요."),
                ExampleSentence("취미", "요즘 사진 찍는 것이 재미있어요. 평범한 풍경도 다른 각도에서 보면 새롭게 보이는 것 같아요."),
                ExampleSentence("공부", "어려운 문제를 만나면 바로 답을 보지 않으려고 해요. 스스로 생각한 뒤 틀린 이유를 찾아봅니다."),
            ),
        ),

        ExampleSentenceGroup(
            code = "ko-kp",
            label = "조선말 (조선및 연변)",
            sentences = listOf(
                ExampleSentence("과학", "한 연구기관에서 식물의 생장을 관찰하고 있습니다. 알맞은 재배조건을 찾기 위한 사업입니다."),
                ExampleSentence("날씨", "최근 아침과 저녁의 기온차가 크게 나타나고 있습니다. 야외활동을 할 때 건강관리에 주의를 돌려야 합니다."),
                ExampleSentence("생활", "주민들이 집 가까이에 작은 텃밭을 꾸리고 있습니다. 여러가지 남새를 알뜰히 가꾸고 있습니다."),
            ),
        ),

        ExampleSentenceGroup(
            code = "zh-CN",
            label = "中文（简体）",
            sentences = listOf(
                ExampleSentence("城市", "最近，城市里增加了不少小型公园。居民晚上可以在那里散步和休息。"),
                ExampleSentence("学习", "学习语言不能只背单词。多听真实对话，才能慢慢掌握自然的表达方式。"),
                ExampleSentence("自然", "下过雨以后，山里的空气特别清新。树叶上的水珠在阳光下闪闪发亮。"),
            ),
        ),

        ExampleSentenceGroup(
            code = "zh-TW",
            label = "中文（繁體）",
            sentences = listOf(
                ExampleSentence("城市", "最近，城市裡多了幾個小公園。晚上常有人帶著孩子到那裡散步。"),
                ExampleSentence("學習", "學習語言不能只記單字。多聽自然的對話，才能慢慢掌握真正的語氣。"),
                ExampleSentence("自然", "雨後的山林空氣非常清新。陽光照在葉片上的水珠上，看起來閃閃發亮。"),
            ),
        ),

        ExampleSentenceGroup(
            code = "ru",
            label = "Русский",
            sentences = listOf(
                ExampleSentence("Утро", "Сегодня утром я вышел из дома раньше обычного. На улице было тихо и прохладно."),
                ExampleSentence("Кулинария", "Недавно я начал готовить простые блюда. Каждая новая попытка получается немного лучше предыдущей."),
                ExampleSentence("Природа", "После дождя лес выглядит совершенно иначе. Особенно хорошо слышен шум деревьев и воды."),
            ),
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ExampleSentenceScreen(
    onBack: () -> Unit,
    onSelect: (targetLanguage: String, title: String, text: String) -> Unit,
) {
    BackHandler(onBack = onBack)

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.example_sentence_title),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = hapticAction(onBack),
                        modifier = Modifier.padding(start = 10.dp),
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.ui_042),
                            tint = Color.Black,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = HeaderBackground,
                ),
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .windowInsetsPadding(androidx.compose.foundation.layout.WindowInsets.navigationBars),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp),
        ) {
            item {
                Text(
                    stringResource(R.string.example_sentence_select),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black.copy(alpha = 0.58f),
                    modifier = Modifier.padding(horizontal = 4.dp),
                )
            }

            items(ExampleSentenceLibrary.groups, key = { it.code }) { group ->
                Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
                    Text(
                        group.label,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp),
                    )
                    group.sentences.forEachIndexed { index, sentence ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    Log.d(
                                        "ExampleSentenceScreen",
                                        "Example selected: language=${group.code}, index=$index, title=${sentence.title}",
                                    )
                                    onSelect(group.code, sentence.title, sentence.text)
                                },
                            shape = SmoothCornerShape(22.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFF6F6F6),
                            ),
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 15.dp),
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        sentence.title,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = AccentGreen,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }
                                Spacer(Modifier.height(5.dp))
                                Text(
                                    sentence.text,
                                    style = MaterialTheme.typography.bodyLarge,
                                    lineHeight = 25.sp,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
