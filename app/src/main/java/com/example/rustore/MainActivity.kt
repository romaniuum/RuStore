@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.rustore

import android.net.Uri
import androidx.navigation.compose.*
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.rotate
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

data class App(
    val id: Int,
    val name: String,
    val developer: String,
    val shortDescription: String,
    val fullDescription: String,
    val category: String,
    val rating: String = "0+",
    val screenshots: List<Int> = emptyList(),
    val iconId: Int
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("onboarding", MODE_PRIVATE)
        val showedOnboardingShown = prefs.getBoolean("shown", false)

        val apps = listOf(
            App(
                id = 1,
                name = "ВКонтакте",
                developer = "VK",
                shortDescription = "Социальная сеть №1",
                fullDescription = "ВКонтакте — это платформа для общения, публикации фото и видео, создания сообществ и прослушивания музыки. В приложении доступны чаты, трансляции, история и персональные рекомендации. Безопасность и приватность профиля настраиваются в несколько касаний.",
                category = "Социальные сети",
                rating = "12+",
                screenshots = listOf(R.drawable.vk_s1, R.drawable.vk_s2, R.drawable.vk_s3),
                iconId = R.drawable.vklogo
            ),
            App(
                id = 2,
                name = "Госуслуги",
                developer = "Минцифры России",
                shortDescription = "Государственные услуги",
                fullDescription = "Госуслуги позволяет получать государственные услуги онлайн: запись к врачу, получение справок, налоги, автоуслуги и многое другое. Приложение интегрируется с учетной записью и поддерживает безопасную авторизацию через подтвержденной учетной записи.",
                category = "Госуслуги",
                rating = "0+",
                screenshots = listOf(R.drawable.gosuslugi_s1, R.drawable.gosuslugi_s2, R.drawable.gosuslugi_s3),
                iconId = R.drawable.goslogo
            ),
            App(3, "СБПэй", "НСПК", "Оплата по QR", "Быстрые и безопасные платежи по QR-коду, перевод по номеру телефона и история операций. Поддерживает карты большинства банков и интеграцию со смартфоном.", "Финансы", "0+", listOf(R.drawable.sbpay_s1, R.drawable.sbpay_s2, R.drawable.sbpay_s3), R.drawable.sbplogo),
            App(4, "Яндекс Go", "Яндекс", "Такси и доставка", "Яндекс Go объединяет такси, доставку еды, каршеринг и навигацию в одном приложении. Удобный интерфейс, отслеживание заказа, опции для бизнеса и скидки постоянным пользователям.", "Транспорт", "0+", listOf(R.drawable.yandexgo_s1, R.drawable.yandexgo_s2, R.drawable.yandexgo_s3), R.drawable.gologo),
            App(5, "2ГИС", "2GIS", "Карты офлайн", "Подробные офлайн-карты, поиск организаций, маршруты и отзывы. 2ГИС показывает планировку зданий, телефоны и время работы, а также работает без интернета.", "Инструменты", "0+", listOf(R.drawable.gis_s1, R.drawable.gis_s2, R.drawable.gis_s3), R.drawable.gislogo),
            App(6, "Тинькофф", "Тинькофф Банк", "Мобильный банк", "Тинькофф — мобильный банк для управления счетами, платежей, инвестиций и карт. Удобные переводы, кэшбэк и круглосуточная поддержка.", "Финансы", "6+", listOf(R.drawable.tbank_s1, R.drawable.tbank_s2, R.drawable.tbank_s3), R.drawable.tbanklogo),
            App(7, "Brawl Stars", "Supercell", "Сражения 3 на 3", "Brawl Stars — динамичная многопользовательская игра с короткими матчами, уникальными бойцами и турнирами. Командная игра, прокачка бойцов и события каждый день.", "Игры", "6+", listOf(R.drawable.brawl_s1, R.drawable.brawl_s2, R.drawable.brawl_s3), R.drawable.brawllogo),
            App(8, "Мой МТС", "МТС", "Управление тарифом", "Приложение Мой МТС позволяет управлять мобильным тарифом, мониторить расход трафика, пополнять баланс и подключать сервисы. Также доступны бонусы и поддержка.", "Инструменты", "0+", listOf(R.drawable.mts_s1, R.drawable.mts_s2, R.drawable.mts_s3), R.drawable.mtslogo)
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
                    // Main catalog (главный экран с приложениями). В AppBar добавлена кнопка "Категории".
                    composable("catalog") { CatalogScreen(apps, navController, category = null) }

                    // Categories screen — список категорий с кнопкой назад
                    composable("categories") { CategoriesScreen(apps, navController) }

                    // Catalog filtered by category — показывает стрелку назад, чтобы вернуться в категории
                    composable("catalog/category/{category}") { entry ->
                        val category = entry.arguments?.getString("category")?.let { Uri.decode(it) }
                        CatalogScreen(apps, navController, category)
                    }

                    composable("app/{appId}") { entry ->
                        val id = entry.arguments?.getString("appId")?.toIntOrNull() ?: 1
                        val app = apps.find { it.id == id }
                        if (app != null) AppDetailScreen(app, navController)
                    }
                    composable("app/{appId}/screenshot/{index}") { entry ->
                        val id = entry.arguments?.getString("appId")?.toIntOrNull() ?: 1
                        val index = entry.arguments?.getString("index")?.toIntOrNull() ?: 0
                        val app = apps.find { it.id == id }
                        if (app != null && index in app.screenshots.indices) {
                            FullscreenScreenshotScreen(app, index, navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoriesScreen(apps: List<App>, navController: NavHostController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Категории", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0066FF),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        val groups = apps.groupBy { it.category }
        val items = groups.map { (name, list) -> name to list.size }

        LazyColumn(contentPadding = padding) {
            items(items) { (name, count) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 8.dp)
                        .clickable {
                            // Навигация к отфильтрованной витрине; оставляем категорию в backstack (чтобы можно было вернуться)
                            navController.navigate("catalog/category/${Uri.encode(name)}")
                        },
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(name, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                            Text("$count приложений", color = Color.Gray, fontSize = 14.sp)
                        }
                        // Простая стрелочка как индикатор
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color(0xFF0066FF), modifier = Modifier.rotate(180f))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(apps: List<App>, navController: NavHostController, category: String? = null) {
    val title = category ?: "RuStore"

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(title, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    if (category != null) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.Default.ArrowBack, "Назад", tint = Color.White)
                        }
                    }
                },
                actions = {
                    if (category == null) {
                        TextButton(onClick = { navController.navigate("categories") }) {
                            Text("Категории", color = Color.White)
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0066FF),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        val displayed = if (category == null) apps else apps.filter { it.category == category }

        LazyColumn(contentPadding = padding) {
            items(displayed) { app ->
                AppCard(app) { navController.navigate("app/${app.id}") }
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
            .padding(16.dp, 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(app.iconId), contentDescription = null, modifier = Modifier.size(72.dp))
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
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(padding).padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(app.iconId), contentDescription = null, modifier = Modifier.size(100.dp))
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

            Spacer(Modifier.height(24.dp))

            if (app.screenshots.isNotEmpty()) {
                Text("Скриншоты", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                Spacer(Modifier.height(8.dp))
                LazyRow {
                    itemsIndexed(app.screenshots) { index, resId ->
                        Image(
                            painter = painterResource(resId),
                            contentDescription = "Скрин ${index + 1}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(width = 220.dp, height = 400.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    navController.navigate("app/${app.id}/screenshot/$index")
                                }
                        )
                    }
                }
                Spacer(Modifier.height(24.dp))
            }

            Text("О приложении", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Text(app.fullDescription, fontSize = 16.sp)

            Spacer(Modifier.height(40.dp))
            Button(
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Установка не реализована в демо-режиме")
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0066FF))
            ) {
                Text("Установить", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullscreenScreenshotScreen(app: App, index: Int, navController: NavHostController) {
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
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color.Black
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding).background(Color.Black), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(app.screenshots[index]),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
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
            Image(painter = painterResource(R.drawable.rustorelogo), contentDescription = null, modifier = Modifier.size(128.dp))
            Text("Добро пожаловать\nв RuStore", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text("Российский магазин приложений", color = Color.White.copy(0.9f), fontSize = 18.sp, textAlign = TextAlign.Center)
            Button(onClick = onFinish, modifier = Modifier.fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(Color.White)) {
                Text("Перейти к приложениям", color = Color(0xFF0066FF))
            }
            Spacer(Modifier.height(40.dp))
        }
    }
}