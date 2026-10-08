package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auth.AuthManager
import com.example.auth.UserRole
import com.example.data.RepositoryManager
import com.example.model.ExecutionMode
import com.example.ui.components.AuthDialog
import com.example.ui.components.GlobalSearchDialog
import com.example.ui.components.TradingOverlayDialog
import com.example.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    RepositoryManager.initializeDatabase(applicationContext)
    com.example.data.ExplorerRepository.initializeDatabase(applicationContext)
    com.example.network.CryptoPriceService.initialize(applicationContext)
    com.example.network.AlchemyMultiChainService.initialize(applicationContext)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        MainAppContent()
      }
    }
  }
}

enum class Screen {
  DASHBOARD,
  CATEGORIES,
  CATEGORY_DETAIL,
  LAYERS,
  ARENAS,
  WALLET,
  EXPLORER,
  SYSTEM_HUB,
  RAPP_CENTER,
  DOCUMENTATION,
  TITLES,
  BLUEPRINT,
  WOHNZENTREN,
  GLOSSAR,
  HEALTH,
  PROFILE,
  WEB_PORTAL,
  ZON_DEX,
  XJUSTIZ_NOTAR
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContent() {
  val context = LocalContext.current
  val currentUser by AuthManager.currentUser.collectAsState()
  val isOnline by com.example.network.CryptoPriceService.isOnline.collectAsState()
  var currentScreen by remember { mutableStateOf(Screen.DASHBOARD) }
  var selectedCategoryId by remember { mutableStateOf(1) }
  var showGlobalSearch by remember { mutableStateOf(false) }
  var showAuthDialog by remember { mutableStateOf(false) }
  var showTradingOverlay by remember { mutableStateOf(false) }
  var rappExecutionMode by remember { mutableStateOf(ExecutionMode.MAIN_REAL) }
  var isWebAppMode by remember { mutableStateOf(false) }
  val screenStack = remember { mutableStateListOf(Screen.DASHBOARD) }

  fun navigateTo(screen: Screen) {
    if (screenStack.lastOrNull() != screen) {
      screenStack.add(screen)
    }
    currentScreen = screen
  }

  fun navigateBack() {
    if (screenStack.size > 1) {
      screenStack.removeAt(screenStack.size - 1)
      currentScreen = screenStack.last()
    } else {
      currentScreen = Screen.DASHBOARD
    }
  }

  BackHandler(enabled = screenStack.size > 1) {
    navigateBack()
  }

  if (showGlobalSearch) {
    GlobalSearchDialog(
      onDismiss = { showGlobalSearch = false },
      onSelectCategory = { catId ->
        selectedCategoryId = catId
        navigateTo(Screen.CATEGORY_DETAIL)
      }
    )
  }

  if (showAuthDialog) {
    AuthDialog(
      onDismiss = { showAuthDialog = false }
    )
  }

  if (showTradingOverlay) {
    TradingOverlayDialog(
      onDismiss = { showTradingOverlay = false }
    )
  }

  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .testTag("main_app_scaffold"),
    topBar = {
      Column {
        if (isWebAppMode) {
          Surface(
            color = Color(0xFF0F172A),
            border = BorderStroke(1.dp, SignalGreen.copy(alpha = 0.5f)),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("web_app_browser_bar")
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 5.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f, fill = false)
              ) {
                Surface(
                  color = Color.Black.copy(alpha = 0.5f),
                  shape = RoundedCornerShape(16.dp),
                  border = BorderStroke(1.dp, SignalGreen.copy(alpha = 0.4f))
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Icon(
                      Icons.Default.Lock,
                      contentDescription = "SSL Gesichert",
                      tint = SignalGreen,
                      modifier = Modifier.size(11.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = "https://rfof-network.github.io/PRAI-MTK-NEC/",
                      fontSize = 10.sp,
                      fontFamily = FontFamily.Monospace,
                      color = Color.White,
                      maxLines = 1
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.width(6.dp))

              Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                  color = SignalGreen.copy(alpha = 0.2f),
                  shape = RoundedCornerShape(4.dp),
                  border = BorderStroke(1.dp, SignalGreen)
                ) {
                  Text(
                    text = "WEB APP PARITÄT",
                    color = SignalGreen,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                  )
                }
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(
                  onClick = {
                    isWebAppMode = false
                    Toast.makeText(context, "Zurück zur nativen APK App gewechselt", Toast.LENGTH_SHORT).show()
                  },
                  modifier = Modifier
                    .size(28.dp)
                    .testTag("exit_web_app_btn")
                ) {
                  Icon(
                    Icons.Default.Android,
                    contentDescription = "Zurück zur APK App",
                    tint = SignalGreen,
                    modifier = Modifier.size(16.dp)
                  )
                }
              }
            }
          }
        }

        TopAppBar(
          title = {
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "PRAI / MTK / NEC",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Black,
                  color = Color.White
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                  modifier = Modifier
                    .size(7.dp)
                    .background(if (isOnline) SignalGreen else Color(0xFFEF4444), CircleShape)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                  text = if (isOnline) "LIVE" else "OFFLINE",
                  fontSize = 8.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (isOnline) SignalGreen else Color(0xFFEF4444)
                )
              }
              Text(
                text = if (isWebAppMode) "Web App Version · 100% Parität" else if (currentUser.role == UserRole.ADMIN) "Admin: RFOF-NETWORK" else "Nutzer: ${currentUser.username}",
                style = MaterialTheme.typography.labelSmall,
                color = if (isWebAppMode) SignalGreen else if (currentUser.role == UserRole.ADMIN) UrkundeGold else Color(0xFF94A3B8),
                maxLines = 1
              )
            }
          },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = BlueprintNavy,
          titleContentColor = Color.White,
          actionIconContentColor = Color.White
        ),
        actions = {
          // Role & Auth status chip
          Surface(
            color = if (currentUser.role == UserRole.ADMIN) UrkundeGoldBg else SignalBlueLight,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .clickable { showAuthDialog = true }
              .testTag("top_auth_role_badge")
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
            ) {
              Icon(
                imageVector = if (currentUser.role == UserRole.ADMIN) Icons.Default.Shield else Icons.Default.Person,
                contentDescription = null,
                tint = if (currentUser.role == UserRole.ADMIN) UrkundeGoldDark else SignalBlue,
                modifier = Modifier.size(13.dp)
              )
              Spacer(modifier = Modifier.width(3.dp))
              Text(
                text = if (currentUser.role == UserRole.ADMIN) "ADMIN" else "USER",
                color = if (currentUser.role == UserRole.ADMIN) UrkundeGoldDark else SignalBlue,
                fontWeight = FontWeight.Black,
                fontSize = 9.sp
              )
            }
          }

          IconButton(
            onClick = { showTradingOverlay = true },
            modifier = Modifier.testTag("top_trading_button")
          ) {
            Icon(Icons.Default.SwapHoriz, contentDescription = "Trading & Swap", tint = UrkundeGold)
          }

          IconButton(
            onClick = { navigateTo(Screen.RAPP_CENTER) },
            modifier = Modifier.testTag("top_rapp_center_button")
          ) {
            Icon(Icons.Default.Storefront, contentDescription = "rApp Center & PlayStore", tint = UrkundeGold)
          }

          IconButton(
            onClick = { navigateTo(Screen.PROFILE) },
            modifier = Modifier.testTag("top_profile_button")
          ) {
            Icon(Icons.Default.ManageAccounts, contentDescription = "Profil & Repos", tint = Color.White)
          }

          IconButton(
            onClick = { showGlobalSearch = true },
            modifier = Modifier.testTag("top_global_search_button")
          ) {
            Icon(Icons.Default.Search, contentDescription = "Globale Suche", tint = Color.White)
          }

          IconButton(
            onClick = { navigateTo(Screen.HEALTH) },
            modifier = Modifier.testTag("top_health_button")
          ) {
            Icon(Icons.Default.CheckCircle, contentDescription = "Health Check", tint = SignalGreen)
          }
        }
      )
    }
  },
  bottomBar = {
      val navScrollState = rememberScrollState()
      Surface(
        color = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("main_bottom_nav")
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(navScrollState)
            .padding(horizontal = 4.dp, vertical = 2.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          NavigationBarItem(
            selected = currentScreen == Screen.DASHBOARD,
            onClick = { navigateTo(Screen.DASHBOARD) },
            icon = { Icon(Icons.Default.Dashboard, contentDescription = "Übersicht") },
            label = { Text("Übersicht", fontSize = 9.sp, maxLines = 1, softWrap = false) },
            alwaysShowLabel = true,
            modifier = Modifier.width(68.dp)
          )
          NavigationBarItem(
            selected = currentScreen == Screen.CATEGORIES || currentScreen == Screen.CATEGORY_DETAIL,
            onClick = { navigateTo(Screen.CATEGORIES) },
            icon = { Icon(Icons.Default.Category, contentDescription = "Kategorien") },
            label = { Text("Kategorien", fontSize = 9.sp, maxLines = 1, softWrap = false) },
            alwaysShowLabel = true,
            modifier = Modifier.width(68.dp)
          )
          NavigationBarItem(
            selected = currentScreen == Screen.ARENAS,
            onClick = { navigateTo(Screen.ARENAS) },
            icon = { Icon(Icons.Default.SportsScore, contentDescription = "Arenen") },
            label = { Text("Arenen", fontSize = 9.sp, maxLines = 1, softWrap = false) },
            alwaysShowLabel = true,
            modifier = Modifier.width(68.dp)
          )
          NavigationBarItem(
            selected = currentScreen == Screen.WALLET,
            onClick = { navigateTo(Screen.WALLET) },
            icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Wallet") },
            label = { Text("Wallet", fontSize = 9.sp, maxLines = 1, softWrap = false) },
            alwaysShowLabel = true,
            modifier = Modifier.width(68.dp)
          )
          NavigationBarItem(
            selected = currentScreen == Screen.EXPLORER,
            onClick = { navigateTo(Screen.EXPLORER) },
            icon = { Icon(Icons.Default.Explore, contentDescription = "Explorer") },
            label = { Text("Explorer", fontSize = 9.sp, maxLines = 1, softWrap = false) },
            alwaysShowLabel = true,
            modifier = Modifier.width(68.dp)
          )
          NavigationBarItem(
            selected = currentScreen in listOf(Screen.SYSTEM_HUB, Screen.LAYERS, Screen.TITLES, Screen.BLUEPRINT, Screen.WOHNZENTREN, Screen.GLOSSAR, Screen.HEALTH),
            onClick = { navigateTo(Screen.SYSTEM_HUB) },
            icon = { Icon(Icons.Default.Apps, contentDescription = "System") },
            label = { Text("System", fontSize = 9.sp, maxLines = 1, softWrap = false) },
            alwaysShowLabel = true,
            modifier = Modifier.width(68.dp)
          )
          // 7th Button: rApp Center (PlayStore)
          NavigationBarItem(
            selected = currentScreen in listOf(Screen.RAPP_CENTER, Screen.WEB_PORTAL, Screen.ZON_DEX, Screen.XJUSTIZ_NOTAR),
            onClick = { navigateTo(Screen.RAPP_CENTER) },
            icon = { Icon(Icons.Default.Storefront, contentDescription = "rApp Center") },
            label = { Text("rApp Center", fontSize = 9.sp, maxLines = 1, softWrap = false, color = if (currentScreen in listOf(Screen.RAPP_CENTER, Screen.WEB_PORTAL, Screen.ZON_DEX, Screen.XJUSTIZ_NOTAR)) SignalBlue else Color.Unspecified) },
            alwaysShowLabel = true,
            modifier = Modifier.width(76.dp)
          )
          // 8th Button: Dokumentation & Guardian
          NavigationBarItem(
            selected = currentScreen == Screen.DOCUMENTATION,
            onClick = { navigateTo(Screen.DOCUMENTATION) },
            icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "Dokumentation") },
            label = { Text("Dokumentation", fontSize = 9.sp, maxLines = 1, softWrap = false, color = if (currentScreen == Screen.DOCUMENTATION) Color(0xFFA855F7) else Color.Unspecified) },
            alwaysShowLabel = true,
            modifier = Modifier.width(82.dp)
          )
        }
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(BackgroundLight)
    ) {
      when (currentScreen) {
        Screen.DASHBOARD -> DashboardScreen(
          onNavigateToLayers = { navigateTo(Screen.LAYERS) },
          onNavigateToCategories = { navigateTo(Screen.CATEGORIES) },
          onNavigateToCategoryDetail = { id ->
            selectedCategoryId = id
            navigateTo(Screen.CATEGORY_DETAIL)
          },
          onNavigateToArenas = { navigateTo(Screen.ARENAS) },
          onNavigateToTitles = { navigateTo(Screen.TITLES) },
          onNavigateToBlueprint = { navigateTo(Screen.BLUEPRINT) },
          onNavigateToWohnzentren = { navigateTo(Screen.WOHNZENTREN) },
          onOpenSearch = { showGlobalSearch = true },
          onNavigateToWallet = { navigateTo(Screen.WALLET) },
          onNavigateToExplorer = { navigateTo(Screen.EXPLORER) },
          onNavigateToProfile = { navigateTo(Screen.PROFILE) },
          onNavigateToWebPortal = { navigateTo(Screen.WEB_PORTAL) },
          onNavigateToRAppCenter = { navigateTo(Screen.RAPP_CENTER) },
          onOpenTrading = { showTradingOverlay = true },
          onOpenAuth = { showAuthDialog = true }
        )

        Screen.CATEGORIES -> CategoriesScreen(
          onSelectCategory = { id ->
            selectedCategoryId = id
            navigateTo(Screen.CATEGORY_DETAIL)
          }
        )

        Screen.CATEGORY_DETAIL -> CategoryDetailScreen(
          categoryId = selectedCategoryId,
          onBack = { navigateBack() }
        )

        Screen.LAYERS -> LayerExplorerScreen(
          onBack = { navigateBack() }
        )

        Screen.ARENAS -> ArenaMatrixScreen(
          onSelectCategory = { id ->
            selectedCategoryId = id
            navigateTo(Screen.CATEGORY_DETAIL)
          }
        )

        Screen.WALLET -> WalletScreen(
          onNavigateToExplorer = { navigateTo(Screen.EXPLORER) }
        )

        Screen.EXPLORER -> BlockchainExplorerScreen(
          onBack = { navigateBack() }
        )

        Screen.SYSTEM_HUB -> SystemHubScreen(
          onNavigateToTitles = { navigateTo(Screen.TITLES) },
          onNavigateToBlueprint = { navigateTo(Screen.BLUEPRINT) },
          onNavigateToWohnzentren = { navigateTo(Screen.WOHNZENTREN) },
          onNavigateToGlossar = { navigateTo(Screen.GLOSSAR) },
          onNavigateToHealth = { navigateTo(Screen.HEALTH) },
          onNavigateToWallet = { navigateTo(Screen.WALLET) },
          onNavigateToExplorer = { navigateTo(Screen.EXPLORER) },
          onNavigateToProfile = { navigateTo(Screen.PROFILE) },
          onNavigateToWebPortal = { navigateTo(Screen.WEB_PORTAL) },
          onOpenAuth = { showAuthDialog = true }
        )

        Screen.RAPP_CENTER -> RAppCenterScreen(
          isWebAppMode = isWebAppMode,
          onExecuteApp = { app, mode ->
            rappExecutionMode = mode
            val modeLabel = if (mode == ExecutionMode.TEST_DEMO) "Test (Demo)" else "Main (Real)"
            if (app.id != "rapp-001") {
              Toast.makeText(context, "Starte ${app.name} im $modeLabel Modus...", Toast.LENGTH_SHORT).show()
            }
            when (app.id) {
              "rapp-001" -> {
                // Primäre rApp PRAI / MTK / NEC: Öffnet die Web App (Website des Repositories)
                Toast.makeText(context, "Öffne Web App von PRAI / MTK / NEC...", Toast.LENGTH_SHORT).show()
                navigateTo(Screen.WEB_PORTAL)
              }
              "rapp-002" -> {
                // Extension 1: ZON Universal DEX & Launchpad
                navigateTo(Screen.ZON_DEX)
              }
              "rapp-003" -> {
                // Extension 2: XJustiz Notar & Grundbuch Archiv
                navigateTo(Screen.XJUSTIZ_NOTAR)
              }
              else -> {
                navigateTo(Screen.WEB_PORTAL)
              }
            }
          }
        )

        Screen.DOCUMENTATION -> DocumentationScreen()

        Screen.TITLES -> TitlesScreen()

        Screen.BLUEPRINT -> SystemBlueprintScreen()

        Screen.WOHNZENTREN -> WohnzentrenScreen()

        Screen.GLOSSAR -> GlossarFaqScreen()

        Screen.HEALTH -> HealthCheckScreen()

        Screen.PROFILE -> ProfileSettingsScreen(
          onNavigateToExplorer = { navigateTo(Screen.EXPLORER) },
          onNavigateToWallet = { navigateTo(Screen.WALLET) },
          onBack = { navigateBack() }
        )

        Screen.WEB_PORTAL -> WebPortalScreen(
          onBack = { navigateTo(Screen.RAPP_CENTER) },
          onNavigateToDex = { navigateTo(Screen.ZON_DEX) },
          onNavigateToNotariat = { navigateTo(Screen.XJUSTIZ_NOTAR) },
          onNavigateToRAppCenter = { navigateTo(Screen.RAPP_CENTER) },
          onActivateWebAppMode = {
            isWebAppMode = true
            Toast.makeText(context, "Web App von PRAI / MTK / NEC betreten (Singularität)", Toast.LENGTH_SHORT).show()
            navigateTo(Screen.DASHBOARD)
          }
        )

        Screen.ZON_DEX -> ZonDexScreen(
          executionMode = rappExecutionMode,
          onBack = { navigateTo(Screen.RAPP_CENTER) },
          onNavigateToWebPortal = {
            isWebAppMode = true
            Toast.makeText(context, "Web App von PRAI / MTK / NEC betreten (Singularität)", Toast.LENGTH_SHORT).show()
            navigateTo(Screen.DASHBOARD)
          },
          onNavigateToNotariat = { navigateTo(Screen.XJUSTIZ_NOTAR) },
          onNavigateToRAppCenter = { navigateTo(Screen.RAPP_CENTER) }
        )

        Screen.XJUSTIZ_NOTAR -> XJustizArchiveScreen(
          executionMode = rappExecutionMode,
          onBack = { navigateTo(Screen.RAPP_CENTER) },
          onNavigateToWebPortal = {
            isWebAppMode = true
            Toast.makeText(context, "Web App von PRAI / MTK / NEC betreten (Singularität)", Toast.LENGTH_SHORT).show()
            navigateTo(Screen.DASHBOARD)
          },
          onNavigateToDex = { navigateTo(Screen.ZON_DEX) },
          onNavigateToRAppCenter = { navigateTo(Screen.RAPP_CENTER) }
        )
      }
    }
  }
}
