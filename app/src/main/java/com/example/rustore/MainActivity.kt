package com.example.rustore

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class App(
    val id: Int,
    val name: String,
    val developer: String,
    val shortDescription: String,
    val category: String,
    val rating: String = "0+",
    val iconId: Int = R.drawable.ic_launcher_foreground
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("onboarding", Context.MODE_PRIVATE)
        val showed = prefs.getBoolean("showed", false)

        val appsList = listOf(
            App(1, "ВКонтакте", "VK", "Социальная сеть", "Социальные сети", "12+", R.drawable.vklogo),
            App(2, "Госуслуги", "Минцифры России", "Государственные услуги", "Государственные", "0+", R.drawable.goslogo),
            App(3, "СБПэй", "НСПК", "Оплата по QR", "Финансы", "0+",R.drawable.sbplogo),
            App(4, "Яндекс Go", "Яндекс", "Такси и доставка", "Транспорт", "0+",R.drawable.gologo),
            App(5, "2ГИС", "2GIS", "Карты офлайн", "Инструменты", "0+",R.drawable.gislogo),
            App(6, "Тинькофф", "Тинькофф Банк", "Мобильный банк", "Финансы", "6+",R.drawable.tbanklogo),
            App(7, "Brawl Stars", "Supercell", "Сражения 3 на 3", "Игры", "6+",R.drawable.brawllogo),
            App(8, "Мой МТС", "МТС", "Управление тарифом", "Инструменты", "0+",R.drawable.mtslogo)
        )

        setContent {
            MaterialTheme {
                if (!showed) {
                    OnboardingScreen {
                        prefs.edit().putBoolean("showed", true).apply()
                        recreate()
                    }
                } else {
                    AppsCatalogScreen(appsList)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppsCatalogScreen(apps: List<App>) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("RuStore", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0066FF),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(apps) { app ->
                AppCard(app) {}
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppCard(app: App, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = app.iconId),
                contentDescription = null,
                modifier = Modifier.size(72.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(app.name, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                Text(app.developer, color = Color.Gray, fontSize = 14.sp)
                Spacer(Modifier.height(4.dp))
                Text(app.shortDescription, fontSize = 14.sp, maxLines = 2)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(app.category, color = Color(0xFF0066FF), fontSize = 13.sp)
                Spacer(Modifier.height(4.dp))
                Text(app.rating, color = Color.Gray, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    Surface(color = Color(0xFF0066FF), modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier.height(80.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier.size(128.dp)
            )
            Text(
                "Добро пожаловать\nв RuStore",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "Российский магазин приложений для Android",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
            Button(
                onClick = onFinish,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Перейти к приложениям", color = Color(0xFF0066FF), fontSize = 18.sp)
            }
            Spacer(Modifier.height(40.dp))
        }
    }
}