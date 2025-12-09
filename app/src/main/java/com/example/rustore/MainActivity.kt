@file:OptIn(ExperimentalMaterial3Api::class)        // убирает жёлтые предупреждения Material3

package com.example.rustore
import androidx.navigation.compose.*                 // добавляет все navigation-функции
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

data class App(
    val id: Int,
    val name: String,
    val developer: String,
    val shortDescription: String,
    val fullDescription: String,
    val category: String,
    val rating: String = "0+",
    val iconId: Int
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("onboarding", MODE_PRIVATE)
        val showedOnboardingShown = prefs.getBoolean("shown", false)

        val apps = listOf(
            App(1, "ВКонтакте", "VK", "Социальная сеть №1", "Самая популярная социальная сеть в России", "Социальные сети", "12+", R.drawable.vklogo),
            App(2, "Госуслуги", "Минцифры России", "Государственные услуги", "Получайте услуги онлайн", "Государственные", "0+", R.drawable.goslogo),
            App(3, "СБПэй", "НСПК", "Оплата по QR", "Быстрые платежи", "Финансы", "0+", R.drawable.sbplogo),
            App(4, "Яндекс Go", "Яндекс", "Такси и доставка", "Всё в одном приложении", "Транспорт", "0+", R.drawable.gologo),
            App(5, "2ГИС", "2GIS", "Карты офлайн", "Навигатор без интернета", "Инструменты", "0+", R.drawable.gislogo),
            App(6, "Тинькофф", "Тинькофф Банк", "Мобильный банк", "Лучший банк России", "Финансы", "6+", R.drawable.tbanklogo),
            App(7, "Brawl Stars", "Supercell", "Сражения 3 на 3", "Динамичный экшен", "Игры", "6+", R.drawable.brawllogo),
            App(8, "Мой МТС", "МТС", "Управление тарифом", "Контроль расходов", "Инструменты", "0+", R.drawable.mtslogo)
        )

        setContent {
            MaterialTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = if (showedOnboardingShown) "catalog" else "onboarding"
                ) {
                    composable("onboarding") {
                        OnboardingScreen {
                            prefs.edit().putBoolean("shown", true).apply()
                            navController.navigate("catalog") {
                                popUpTo("onboarding") { inclusive = true }
                            }
                        }
                    }
                    composable("catalog") { CatalogScreen(apps, navController) }
                    composable("app/{appId}") { entry ->
                        val id = entry.arguments?.getString("appId")?.toIntOrNull() ?: 1
                        val app = apps.find { it.id == id }
                        if (app != null) AppDetailScreen(app, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun CatalogScreen(apps: List<App>, navController: NavHostController) {
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
                AppCard(app) { navController.navigate("app/${app.id}") }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppCard(app: App, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp, 8.dp).clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(app.iconId), null, Modifier.size(72.dp))
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(app.name, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                Text(app.developer, color = Color.Gray, fontSize = 14.sp)
                Text(app.shortDescription, fontSize = 14.sp, maxLines = 2)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(app.category, color = Color(0xFF0066FF), fontSize = 13.sp)
                Text(app.rating, color = Color.Gray, fontSize = 13.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDetailScreen(app: App, navController: NavHostController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(app.name) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Назад", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0066FF),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(padding).padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(app.iconId), null, Modifier.size(100.dp))
                Spacer(Modifier.width(20.dp))
                Column {
                    Text(app.name, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Text(app.developer, fontSize = 18.sp, color = Color.Gray)
                    Row {
                        Text(app.category, color = Color(0xFF0066FF))
                        Spacer(Modifier.width(16.dp))
                        Text(app.rating, color = Color.Gray)
                    }
                }
            }
            Spacer(Modifier.height(32.dp))
            Text("О приложении", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Text(app.fullDescription, fontSize = 16.sp)

            Spacer(Modifier.height(40.dp))
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0066FF))
            ) {
                Text("Установить", color = Color.White, fontSize = 18.sp)
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
            Image(painter = painterResource(R.drawable.ic_launcher_foreground), null, Modifier.size(128.dp))
            Text("Добро пожаловать\nв RuStore", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text("Российский магазин приложений", color = Color.White.copy(0.9f), fontSize = 18.sp, textAlign = TextAlign.Center)
            Button(onClick = onFinish, modifier = Modifier.fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(Color.White)) {
                Text("Перейти к приложениям", color = Color(0xFF0066FF))
            }
            Spacer(Modifier.height(40.dp))
        }
    }
}