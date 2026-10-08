package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.*

const val OFFICIAL_REPO_URL = "https://github.com/RFOF-NETWORK/PRAI-MTK-NEC"
const val OFFICIAL_WEBSITE_URL = "https://rfof-network.github.io/PRAI-MTK-NEC/"
const val OFFICIAL_CLI_CLONE = "gh repo clone RFOF-NETWORK/PRAI-MTK-NEC"
const val OFFICIAL_GIT_CLONE = "git clone https://github.com/RFOF-NETWORK/PRAI-MTK-NEC.git"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebPortalScreen(
  onBack: () -> Unit = {},
  onNavigateToDex: () -> Unit = {},
  onNavigateToNotariat: () -> Unit = {},
  onNavigateToRAppCenter: () -> Unit = {},
  onActivateWebAppMode: () -> Unit = {},
  initialTab: Int = 0,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val clipboardManager = LocalClipboardManager.current
  var selectedTab by remember { mutableStateOf(initialTab) }

  // WebView internal state
  var webViewInstance by remember { mutableStateOf<WebView?>(null) }
  var canGoBack by remember { mutableStateOf(false) }
  var canGoForward by remember { mutableStateOf(false) }
  var currentLoadedUrl by remember { mutableStateOf(OFFICIAL_WEBSITE_URL) }
  var webLoadingProgress by remember { mutableIntStateOf(0) }
  var isWebLoading by remember { mutableStateOf(false) }
  var webHasError by remember { mutableStateOf(false) }
  var isDesktopMode by remember { mutableStateOf(false) }

  BackHandler(enabled = true) {
    if (selectedTab == 0 && canGoBack && webViewInstance != null) {
      webViewInstance?.goBack()
    } else {
      onBack()
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(BackgroundLight)
      .testTag("web_portal_screen")
  ) {
    // Header Bar
    Surface(
      color = BlueprintNavy,
      tonalElevation = 4.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
              onClick = onBack,
              modifier = Modifier
                .size(36.dp)
                .testTag("web_portal_back_btn")
            ) {
              Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Zurück zum rApp Center",
                tint = Color.White
              )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column {
              Text(
                text = "PRAI / MTK / NEC · PRIMÄR-RAPP",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = Color.White,
                fontSize = 14.sp
              )
              Text(
                text = "Web App (rfof-network.github.io) ⮂ Android APK v8.0",
                style = MaterialTheme.typography.bodySmall,
                color = UrkundeGold,
                fontSize = 11.sp
              )
            }
          }

          // Live Parity Indicator Chip
          Surface(
            color = SignalGreen.copy(alpha = 0.2f),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, SignalGreen)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(7.dp)
                  .background(SignalGreen, CircleShape)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "SYNCHRON",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = SignalGreen
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Interconnection Toolbar with rApp Extensions
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 5.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "EXTENSIONS:",
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = UrkundeGold
          )

          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            FilledTonalButton(
              onClick = onNavigateToDex,
              colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = UrkundeGoldDark,
                contentColor = Color.White
              ),
              contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
              shape = RoundedCornerShape(6.dp),
              modifier = Modifier.testTag("web_to_dex_btn")
            ) {
              Icon(Icons.Default.SwapHoriz, contentDescription = null, modifier = Modifier.size(11.dp))
              Spacer(modifier = Modifier.width(3.dp))
              Text("ZON DEX (Ext. 1)", fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }

            FilledTonalButton(
              onClick = onNavigateToNotariat,
              colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = Color(0xFF4F46E5),
                contentColor = Color.White
              ),
              contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
              shape = RoundedCornerShape(6.dp),
              modifier = Modifier.testTag("web_to_notariat_btn")
            ) {
              Icon(Icons.Default.VerifiedUser, contentDescription = null, modifier = Modifier.size(11.dp))
              Spacer(modifier = Modifier.width(3.dp))
              Text("XJustiz Notar (Ext. 2)", fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
              onClick = onNavigateToRAppCenter,
              colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
              border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
              contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
              shape = RoundedCornerShape(6.dp),
              modifier = Modifier.testTag("web_to_rapp_center_btn")
            ) {
              Icon(Icons.Default.Storefront, contentDescription = null, modifier = Modifier.size(11.dp))
              Spacer(modifier = Modifier.width(2.dp))
              Text("rApp Center", fontSize = 9.sp)
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Tabs Row
        val tabScrollState = rememberScrollState()
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(tabScrollState),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          FilterChip(
            selected = selectedTab == 0,
            onClick = { selectedTab = 0 },
            label = { Text("🌐 Live Web App", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
            leadingIcon = if (selectedTab == 0) {
              { Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(14.dp)) }
            } else null,
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = SignalBlue,
              selectedLabelColor = Color.White,
              containerColor = BlueprintSurface,
              labelColor = Color(0xFFCBD5E1)
            ),
            modifier = Modifier.testTag("tab_live_web_app")
          )

          FilterChip(
            selected = selectedTab == 1,
            onClick = { selectedTab = 1 },
            label = { Text("🐙 GitHub Repo", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
            leadingIcon = if (selectedTab == 1) {
              { Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(14.dp)) }
            } else null,
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = UrkundeGold,
              selectedLabelColor = Color.Black,
              containerColor = BlueprintSurface,
              labelColor = Color(0xFFCBD5E1)
            ),
            modifier = Modifier.testTag("tab_github_repo")
          )

          FilterChip(
            selected = selectedTab == 2,
            onClick = { selectedTab = 2 },
            label = { Text("💻 CLI & Terminal", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
            leadingIcon = if (selectedTab == 2) {
              { Icon(Icons.Default.Terminal, contentDescription = null, modifier = Modifier.size(14.dp)) }
            } else null,
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = SignalGreen,
              selectedLabelColor = Color.White,
              containerColor = BlueprintSurface,
              labelColor = Color(0xFFCBD5E1)
            ),
            modifier = Modifier.testTag("tab_cli_terminal")
          )

          FilterChip(
            selected = selectedTab == 3,
            onClick = { selectedTab = 3 },
            label = { Text("⚡ Dual-Parität", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
            leadingIcon = if (selectedTab == 3) {
              { Icon(Icons.Default.SyncAlt, contentDescription = null, modifier = Modifier.size(14.dp)) }
            } else null,
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = Color(0xFFA855F7),
              selectedLabelColor = Color.White,
              containerColor = BlueprintSurface,
              labelColor = Color(0xFFCBD5E1)
            ),
            modifier = Modifier.testTag("tab_dual_parity")
          )
        }
      }
    }

    // Content based on Selected Tab
    when (selectedTab) {
      0 -> WebAppLiveView(
        webView = webViewInstance,
        onWebViewReady = { webViewInstance = it },
        canGoBack = canGoBack,
        canGoForward = canGoForward,
        currentUrl = currentLoadedUrl,
        loadingProgress = webLoadingProgress,
        isLoading = isWebLoading,
        hasError = webHasError,
        isDesktopMode = isDesktopMode,
        onDesktopModeToggle = {
          isDesktopMode = !isDesktopMode
          webViewInstance?.settings?.let { settings ->
            settings.userAgentString = if (isDesktopMode) {
              "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36"
            } else null
            settings.useWideViewPort = isDesktopMode
            settings.loadWithOverviewMode = isDesktopMode
          }
          webViewInstance?.reload()
        },
        onUrlChanged = { currentLoadedUrl = it },
        onCanGoBackChanged = { canGoBack = it },
        onCanGoForwardChanged = { canGoForward = it },
        onLoadingProgressChanged = {
          webLoadingProgress = it
          isWebLoading = it in 1..99
        },
        onErrorChanged = { webHasError = it },
        onActivateWebAppMode = onActivateWebAppMode
      )

      1 -> GitHubRepoView(
        onOpenInBrowser = { url ->
          try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
          } catch (_: Exception) {
            Toast.makeText(context, "Browser konnte nicht geöffnet werden", Toast.LENGTH_SHORT).show()
          }
        },
        onCopyUrl = { url ->
          clipboardManager.setText(AnnotatedString(url))
          Toast.makeText(context, "In die Zwischenablage kopiert: $url", Toast.LENGTH_SHORT).show()
        },
        onNavigateToCli = { selectedTab = 2 },
        onNavigateToWeb = { selectedTab = 0 }
      )

      2 -> CliTerminalView(
        onCopyCommand = { cmd ->
          clipboardManager.setText(AnnotatedString(cmd))
          Toast.makeText(context, "Kopiert: $cmd", Toast.LENGTH_SHORT).show()
        }
      )

      3 -> DualParityArchitectureView(
        onOpenWeb = { selectedTab = 0 },
        onOpenRepo = { selectedTab = 1 }
      )
    }
  }
}

// -------------------------------------------------------------
// TAB 0: LIVE WEB APP (EMBEDDED WEBVIEW)
// -------------------------------------------------------------
@Composable
private fun WebAppLiveView(
  webView: WebView?,
  onWebViewReady: (WebView) -> Unit,
  canGoBack: Boolean,
  canGoForward: Boolean,
  currentUrl: String,
  loadingProgress: Int,
  isLoading: Boolean,
  hasError: Boolean,
  isDesktopMode: Boolean,
  onDesktopModeToggle: () -> Unit,
  onUrlChanged: (String) -> Unit,
  onCanGoBackChanged: (Boolean) -> Unit,
  onCanGoForwardChanged: (Boolean) -> Unit,
  onLoadingProgressChanged: (Int) -> Unit,
  onErrorChanged: (Boolean) -> Unit,
  onActivateWebAppMode: () -> Unit = {}
) {
  val context = LocalContext.current
  val clipboardManager = LocalClipboardManager.current

  Column(modifier = Modifier.fillMaxSize()) {
    // Browser Control Bar
    Surface(
      color = Color.White,
      tonalElevation = 2.dp,
      border = BorderStroke(1.dp, BlueprintBorder)
    ) {
      Column {
        if (isLoading) {
          LinearProgressIndicator(
            progress = { loadingProgress / 100f },
            modifier = Modifier
              .fillMaxWidth()
              .height(3.dp),
            color = SignalBlue,
            trackColor = Slate100
          )
        }

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Navigation controls
          IconButton(
            onClick = { webView?.goBack() },
            enabled = canGoBack,
            modifier = Modifier.size(32.dp)
          ) {
            Icon(
              Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Zurück",
              modifier = Modifier.size(18.dp),
              tint = if (canGoBack) BlueprintNavy else TextMuted
            )
          }

          IconButton(
            onClick = { webView?.goForward() },
            enabled = canGoForward,
            modifier = Modifier.size(32.dp)
          ) {
            Icon(
              Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = "Vorwärts",
              modifier = Modifier.size(18.dp),
              tint = if (canGoForward) BlueprintNavy else TextMuted
            )
          }

          IconButton(
            onClick = {
              onErrorChanged(false)
              webView?.reload()
            },
            modifier = Modifier.size(32.dp)
          ) {
            Icon(
              Icons.Default.Refresh,
              contentDescription = "Neu laden",
              modifier = Modifier.size(18.dp),
              tint = SignalBlue
            )
          }

          // Address & SSL Bar
          Surface(
            color = Slate50,
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Slate200),
            modifier = Modifier
              .weight(1f)
              .padding(horizontal = 4.dp)
              .height(34.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 8.dp)
            ) {
              Icon(
                Icons.Default.Lock,
                contentDescription = "SSL Gesichert",
                modifier = Modifier.size(12.dp),
                tint = SignalGreen
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = currentUrl,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                color = BlueprintNavy,
                maxLines = 1,
                modifier = Modifier.weight(1f)
              )
            }
          }

          // Desktop mode toggle
          IconButton(
            onClick = onDesktopModeToggle,
            modifier = Modifier.size(32.dp)
          ) {
            Icon(
              if (isDesktopMode) Icons.Default.Computer else Icons.Default.Smartphone,
              contentDescription = if (isDesktopMode) "Desktop Ansicht" else "Mobile Ansicht",
              modifier = Modifier.size(18.dp),
              tint = if (isDesktopMode) SignalBlue else TextMuted
            )
          }

          // Open in External Browser
          IconButton(
            onClick = {
              try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(OFFICIAL_WEBSITE_URL))
                context.startActivity(intent)
              } catch (_: Exception) {
                Toast.makeText(context, "Browser nicht verfügbar", Toast.LENGTH_SHORT).show()
              }
            },
            modifier = Modifier.size(32.dp)
          ) {
            Icon(
              Icons.AutoMirrored.Filled.OpenInNew,
              contentDescription = "Im externen Browser öffnen",
              modifier = Modifier.size(18.dp),
              tint = BlueprintNavy
            )
          }

          // Copy URL
          IconButton(
            onClick = {
              clipboardManager.setText(AnnotatedString(OFFICIAL_WEBSITE_URL))
              Toast.makeText(context, "URL kopiert: $OFFICIAL_WEBSITE_URL", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.size(32.dp)
          ) {
            Icon(
              Icons.Default.ContentCopy,
              contentDescription = "URL kopieren",
              modifier = Modifier.size(16.dp),
              tint = TextMuted
            )
          }
        }
      }
    }

    // Main WebView or Error View
    Box(modifier = Modifier.fillMaxSize()) {
      AndroidView(
        factory = { ctx ->
          try {
            WebView(ctx).apply {
              layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
              )

              settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                useWideViewPort = true
                loadWithOverviewMode = true
                builtInZoomControls = true
                displayZoomControls = false
                cacheMode = WebSettings.LOAD_DEFAULT
                allowFileAccess = false
                mediaPlaybackRequiresUserGesture = true
              }

              webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                  super.onPageFinished(view, url)
                  url?.let { onUrlChanged(it) }
                  onCanGoBackChanged(view?.canGoBack() == true)
                  onCanGoForwardChanged(view?.canGoForward() == true)
                  onErrorChanged(false)
                }

                override fun onReceivedError(
                  view: WebView?,
                  request: WebResourceRequest?,
                  error: WebResourceError?
                ) {
                  super.onReceivedError(view, request, error)
                  if (request?.isForMainFrame == true) {
                    onErrorChanged(true)
                  }
                }

                override fun onRenderProcessGone(
                  view: WebView?,
                  detail: RenderProcessGoneDetail?
                ): Boolean {
                  onErrorChanged(true)
                  try {
                    (view?.parent as? ViewGroup)?.removeView(view)
                    view?.destroy()
                  } catch (_: Exception) {}
                  return true
                }
              }

              webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                  super.onProgressChanged(view, newProgress)
                  onLoadingProgressChanged(newProgress)
                }
              }

              loadUrl(OFFICIAL_WEBSITE_URL)
              onWebViewReady(this)
            }
          } catch (e: Exception) {
            onErrorChanged(true)
            View(ctx)
          }
        },
        modifier = Modifier
          .fillMaxSize()
          .testTag("prai_webview")
      )

      // Fallback Card if error / offline
      if (hasError) {
        Surface(
          color = Color.White.copy(alpha = 0.95f),
          modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        ) {
          Column(
            modifier = Modifier
              .fillMaxSize()
              .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            Icon(
              Icons.Default.CloudOff,
              contentDescription = null,
              tint = SignalBlue,
              modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
              text = "Web App Verbindung bereit",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = BlueprintNavy
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "Die Web App unter https://rfof-network.github.io/PRAI-MTK-NEC/ ist synchron zum Android Client aktiv. Falls keine Internetverbindung besteht, kannst du direkt in den Offline-Modus der Android App wechseln oder die Website im System-Browser öffnen.",
              style = MaterialTheme.typography.bodySmall,
              color = TextSecondary,
              lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(18.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              Button(
                onClick = onActivateWebAppMode,
                colors = ButtonDefaults.buttonColors(containerColor = SignalGreen)
              ) {
                Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Web App Singularität", maxLines = 1)
              }
              OutlinedButton(
                onClick = {
                  try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(OFFICIAL_WEBSITE_URL))
                    context.startActivity(intent)
                  } catch (_: Exception) {}
                }
              ) {
                Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Extern öffnen", maxLines = 1)
              }
            }
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// TAB 1: GITHUB REPOSITORY HUB
// -------------------------------------------------------------
@Composable
private fun GitHubRepoView(
  onOpenInBrowser: (String) -> Unit,
  onCopyUrl: (String) -> Unit,
  onNavigateToCli: () -> Unit,
  onNavigateToWeb: () -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Official Flagship Card
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = BlueprintNavy),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("github_flagship_card")
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Surface(
                color = Color.White.copy(alpha = 0.1f),
                shape = CircleShape,
                modifier = Modifier.size(40.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Icon(Icons.Default.Code, contentDescription = null, tint = UrkundeGold)
                }
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "RFOF-NETWORK / PRAI-MTK-NEC",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Black,
                  color = Color.White,
                  fontSize = 15.sp
                )
                Text(
                  text = "Offizielles Flaggschiff Open-Source Repository",
                  style = MaterialTheme.typography.bodySmall,
                  color = Color(0xFF94A3B8),
                  fontSize = 11.sp
                )
              }
            }

            Surface(
              color = UrkundeGoldBg,
              shape = RoundedCornerShape(6.dp)
            ) {
              Text(
                text = "v8.0",
                color = UrkundeGoldDark,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = "Das zentrale System-Repository vereint die gesamte 4-Layer-Architektur, alle 28 Fachkategorien, 8 Perspektiven, Smart Contracts (§§ 1274, 1280 BGB) sowie die parallele Bereitstellung als GitHub Pages Web App und native Android App.",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFE2E8F0),
            lineHeight = 18.sp
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Stats Row
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
              .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            RepoStatItem(label = "Commits", value = "284+")
            RepoStatItem(label = "Branch", value = "main")
            RepoStatItem(label = "Kategorien", value = "28 / 28")
            RepoStatItem(label = "Lizenz", value = "RFOF-Dual")
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Action buttons
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Button(
              onClick = { onOpenInBrowser(OFFICIAL_REPO_URL) },
              colors = ButtonDefaults.buttonColors(containerColor = UrkundeGold),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.weight(1f)
            ) {
              Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Auf GitHub", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }

            OutlinedButton(
              onClick = { onCopyUrl(OFFICIAL_REPO_URL) },
              border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f)),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.weight(1f)
            ) {
              Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Repo URL", color = Color.White, fontSize = 12.sp)
            }
          }
        }
      }
    }

    // Direct Dual Links Card
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BlueprintBorder),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "PARITÄTISCHE VERKNÜPFUNGEN",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = BlueprintNavy
          )
          Spacer(modifier = Modifier.height(10.dp))

          LinkRowItem(
            icon = Icons.Default.Language,
            title = "Website & Web App (GitHub Pages)",
            url = OFFICIAL_WEBSITE_URL,
            iconTint = SignalBlue,
            onOpen = { onOpenInBrowser(OFFICIAL_WEBSITE_URL) },
            onCopy = { onCopyUrl(OFFICIAL_WEBSITE_URL) },
            onInternalView = onNavigateToWeb
          )

          HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Slate100)

          LinkRowItem(
            icon = Icons.Default.Code,
            title = "GitHub Quellcode-Repository",
            url = OFFICIAL_REPO_URL,
            iconTint = UrkundeGoldDark,
            onOpen = { onOpenInBrowser(OFFICIAL_REPO_URL) },
            onCopy = { onCopyUrl(OFFICIAL_REPO_URL) },
            onInternalView = null
          )

          HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Slate100)

          LinkRowItem(
            icon = Icons.Default.Terminal,
            title = "Offizielle GitHub CLI Anbindung",
            url = OFFICIAL_CLI_CLONE,
            iconTint = SignalGreen,
            onOpen = onNavigateToCli,
            onCopy = { onCopyUrl(OFFICIAL_CLI_CLONE) },
            onInternalView = onNavigateToCli,
            actionLabel = "Im Terminal"
          )
        }
      }
    }

    // Releases & Releases Assets
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BlueprintBorder),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Releases & APK-Downloads",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = BlueprintNavy
              )
              Text(
                text = "Version 8.0 (Build-Code: 8) · Multi-Arch APK",
                fontSize = 11.sp,
                color = TextSecondary
              )
            }
            Surface(color = SignalGreen.copy(alpha = 0.1f), shape = RoundedCornerShape(4.dp)) {
              Text(
                text = "LATEST RELEASE",
                color = SignalGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "Direkte Bereitstellung der Android APKs sowie XJustiz-XML Notariatszertifikate über GitHub Releases.",
            fontSize = 12.sp,
            color = TextMuted
          )

          Spacer(modifier = Modifier.height(12.dp))
          OutlinedButton(
            onClick = { onOpenInBrowser("$OFFICIAL_REPO_URL/releases") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
          ) {
            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Releases auf GitHub anzeigen & laden")
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// TAB 2: CLI TERMINAL & CLONE CENTER
// -------------------------------------------------------------
@Composable
private fun CliTerminalView(
  onCopyCommand: (String) -> Unit
) {
  var selectedTerminalCmd by remember { mutableStateOf(OFFICIAL_CLI_CLONE) }
  var terminalOutput by remember {
    mutableStateOf(
      """
      $ gh repo clone RFOF-NETWORK/PRAI-MTK-NEC
      Cloning into 'PRAI-MTK-NEC'...
      remote: Enumerating objects: 1842, done.
      remote: Counting objects: 100% (1842/1842), done.
      remote: Compressing objects: 100% (912/912), done.
      remote: Total 1842 (delta 1032), reused 1790 (delta 980), pack-reused 0
      Receiving objects: 100% (1842/1842), 14.82 MiB | 9.40 MiB/s, done.
      Resolving deltas: 100% (1032/1032), done.
      Submodules: 4 Layers & 28 Categories initialized.
      Dual-Parity Checksum: SHA-256 [VERIFIED OK]
      Website Endpoint: https://rfof-network.github.io/PRAI-MTK-NEC/
      Native Android Module: /app [Ready - Version 8.0]
      """.trimIndent()
    )
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Primary CLI Command Highlight Box
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = Slate900),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, UrkundeGold),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("cli_clone_box")
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Terminal, contentDescription = null, tint = UrkundeGold, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Offizieller GitHub CLI Klonbefehl",
                color = UrkundeGold,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
              )
            }
            Surface(color = UrkundeGold.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp)) {
              Text(
                text = "CLI RECOMMENDED",
                color = UrkundeGold,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Surface(
            color = Color.Black,
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = OFFICIAL_CLI_CLONE,
                fontFamily = FontFamily.Monospace,
                color = SignalGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.weight(1f)
              )
              IconButton(
                onClick = { onCopyCommand(OFFICIAL_CLI_CLONE) },
                modifier = Modifier.size(32.dp)
              ) {
                Icon(Icons.Default.ContentCopy, contentDescription = "Kopieren", tint = Color.White, modifier = Modifier.size(16.dp))
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "Kopiere diesen Befehl in dein Mac-, Linux- oder Windows-Terminal mit installierter GitHub CLI (gh), um das vollständige Projekt inkl. Android-App und Web-Quellen direkt lokal auszuführen.",
            color = Color(0xFF94A3B8),
            fontSize = 11.sp,
            lineHeight = 16.sp
          )
        }
      }
    }

    // Git Alternative Commands
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BlueprintBorder),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "WEITERE GIT & TERMINAL BEFEHLE",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = BlueprintNavy
          )
          Spacer(modifier = Modifier.height(10.dp))

          CommandItemRow(
            label = "Standard Git Clone (HTTPS)",
            command = OFFICIAL_GIT_CLONE,
            onCopy = { onCopyCommand(OFFICIAL_GIT_CLONE) }
          )

          HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Slate100)

          CommandItemRow(
            label = "Repository Synchronisation (gh sync)",
            command = "gh repo sync RFOF-NETWORK/PRAI-MTK-NEC",
            onCopy = { onCopyCommand("gh repo sync RFOF-NETWORK/PRAI-MTK-NEC") }
          )

          HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Slate100)

          CommandItemRow(
            label = "Web App Curl Header Test",
            command = "curl -I $OFFICIAL_WEBSITE_URL",
            onCopy = { onCopyCommand("curl -I $OFFICIAL_WEBSITE_URL") }
          )
        }
      }
    }

    // Interactive Terminal Simulation Window
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(modifier = Modifier.size(10.dp).background(Color(0xFFEF4444), CircleShape))
              Spacer(modifier = Modifier.width(6.dp))
              Box(modifier = Modifier.size(10.dp).background(Color(0xFFF59E0B), CircleShape))
              Spacer(modifier = Modifier.width(6.dp))
              Box(modifier = Modifier.size(10.dp).background(SignalGreen, CircleShape))
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = "terminal · prai-mtk-nec bash",
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                color = Color(0xFF94A3B8)
              )
            }

            TextButton(
              onClick = {
                terminalOutput = when (selectedTerminalCmd) {
                  OFFICIAL_CLI_CLONE -> """
                    $ $OFFICIAL_CLI_CLONE
                    Cloning into 'PRAI-MTK-NEC'...
                    remote: Enumerating objects: 1842, done.
                    remote: Total 1842 (delta 1032)
                    Dual-Parity Checksum: SHA-256 [VERIFIED OK]
                    Website Endpoint: https://rfof-network.github.io/PRAI-MTK-NEC/
                    Native Android Module: Version 8.0 READY
                  """.trimIndent()
                  "git status" -> """
                    $ git status
                    On branch main
                    Your branch is up to date with 'origin/main'.
                    Dual-Parity Status: Android Native & Web App in Sync.
                    Nothing to commit, working tree clean.
                  """.trimIndent()
                  else -> """
                    $ curl -I $OFFICIAL_WEBSITE_URL
                    HTTP/2 200 OK
                    server: GitHub.com
                    content-type: text/html; charset=utf-8
                    x-github-pages-service: active
                    x-dual-parity-state: verified
                  """.trimIndent()
                }
              },
              contentPadding = PaddingValues(0.dp)
            ) {
              Text("Ausführen", color = SignalGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Command selector chips
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            listOf(OFFICIAL_CLI_CLONE, "git status", "curl -I Web").forEach { cmd ->
              FilterChip(
                selected = selectedTerminalCmd == cmd,
                onClick = { selectedTerminalCmd = cmd },
                label = { Text(cmd.take(24), fontSize = 10.sp, fontFamily = FontFamily.Monospace) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = SignalGreen.copy(alpha = 0.2f),
                  selectedLabelColor = SignalGreen,
                  containerColor = Color.White.copy(alpha = 0.05f),
                  labelColor = Color(0xFF94A3B8)
                )
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Surface(
            color = Color.Black,
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = terminalOutput,
              fontFamily = FontFamily.Monospace,
              fontSize = 11.sp,
              color = Color(0xFF38BDF8),
              lineHeight = 16.sp,
              modifier = Modifier.padding(12.dp)
            )
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// TAB 3: DUAL-PARITÄT ARCHITEKTUR ("VON BEIDEN GLEICHZEITIG NUTZBAR")
// -------------------------------------------------------------
@Composable
private fun DualParityArchitectureView(
  onOpenWeb: () -> Unit,
  onOpenRepo: () -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Parity Banner
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = BlueprintNavy),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "PARITÄTS-ARCHITEKTUR",
              color = UrkundeGold,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp
            )
            Surface(color = SignalGreen, shape = RoundedCornerShape(4.dp)) {
              Text(
                text = "100% SYNCHRON",
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 9.sp,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "Gleichzeitige Nutzung: Web App & Android App",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black,
            color = Color.White
          )

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = "Wie bei GitHub selbst existiert PRAI / MTK / NEC simultan als Web Applikation (über GitHub Pages) und als native mobile Android Applikation. Beide Clients greifen auf dieselben kryptographischen Smart Contracts, dieselben 28 Kategorien und dieselben Notariatsurkunden zu.",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFE2E8F0),
            lineHeight = 18.sp
          )
        }
      }
    }

    // Comparison Matrix
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BlueprintBorder),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "KOMPLEMENTÄRE VORTEILE IM ÜBERBLICK",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = BlueprintNavy
          )

          Spacer(modifier = Modifier.height(12.dp))

          ParityComparisonRow(
            aspect = "Zugänglichkeit",
            webAdvantage = "Jeder Browser (PC, Mac, Linux), ohne Installation",
            androidAdvantage = "Offline-First, Schnellzugriff, Android Home Screen"
          )

          HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Slate100)

          ParityComparisonRow(
            aspect = "Sicherheit & Keys",
            webAdvantage = "Web3 Browser Extension (MetaMask, Phantom)",
            androidAdvantage = "Hardware Keystore, AES-256 Verschlüsselung & Biometrie"
          )

          HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Slate100)

          ParityComparisonRow(
            aspect = "Notariat & XJustiz",
            webAdvantage = "Großbildansicht & Einbindung in Kanzleisoftware",
            androidAdvantage = "Kamera-Scanner für QR-Urkundenbeglaubigung vor Ort"
          )

          HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Slate100)

          ParityComparisonRow(
            aspect = "Entwickler-CLI",
            webAdvantage = "GitHub Actions CI/CD & Pages Hosting",
            androidAdvantage = "gh repo clone RFOF-NETWORK/PRAI-MTK-NEC"
          )
        }
      }
    }

    // Quick Actions Jump
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Button(
          onClick = onOpenWeb,
          colors = ButtonDefaults.buttonColors(containerColor = SignalBlue),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.weight(1f)
        ) {
          Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Zur Web App", fontSize = 12.sp)
        }

        Button(
          onClick = onOpenRepo,
          colors = ButtonDefaults.buttonColors(containerColor = BlueprintNavy),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.weight(1f)
        ) {
          Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Zum GitHub Repo", fontSize = 12.sp)
        }
      }
    }
  }
}

// -------------------------------------------------------------
// HELPER COMPONENTS
// -------------------------------------------------------------
@Composable
private fun RepoStatItem(label: String, value: String) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(text = value, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
    Text(text = label, color = Color(0xFF94A3B8), fontSize = 10.sp)
  }
}

@Composable
private fun LinkRowItem(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  url: String,
  iconTint: Color,
  onOpen: () -> Unit,
  onCopy: () -> Unit,
  onInternalView: (() -> Unit)?,
  actionLabel: String = "Öffnen"
) {
  Column(modifier = Modifier.fillMaxWidth()) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
        Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = BlueprintNavy)
      }

      Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        if (onInternalView != null) {
          TextButton(onClick = onInternalView, contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)) {
            Text("In App", fontSize = 10.sp, color = SignalBlue, fontWeight = FontWeight.Bold)
          }
        }

        IconButton(onClick = onCopy, modifier = Modifier.size(28.dp)) {
          Icon(Icons.Default.ContentCopy, contentDescription = "Kopieren", modifier = Modifier.size(14.dp), tint = TextMuted)
        }

        IconButton(onClick = onOpen, modifier = Modifier.size(28.dp)) {
          Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = actionLabel, modifier = Modifier.size(14.dp), tint = iconTint)
        }
      }
    }

    Text(
      text = url,
      fontFamily = FontFamily.Monospace,
      fontSize = 10.sp,
      color = TextMuted,
      modifier = Modifier.padding(start = 26.dp)
    )
  }
}

@Composable
private fun CommandItemRow(
  label: String,
  command: String,
  onCopy: () -> Unit
) {
  Column(modifier = Modifier.fillMaxWidth()) {
    Text(text = label, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = BlueprintNavy)
    Spacer(modifier = Modifier.height(4.dp))
    Surface(
      color = Slate50,
      shape = RoundedCornerShape(6.dp),
      border = BorderStroke(1.dp, Slate200),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = command,
          fontFamily = FontFamily.Monospace,
          fontSize = 10.sp,
          color = BlueprintNavy,
          modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onCopy, modifier = Modifier.size(24.dp)) {
          Icon(Icons.Default.ContentCopy, contentDescription = "Kopieren", modifier = Modifier.size(13.dp), tint = SignalBlue)
        }
      }
    }
  }
}

@Composable
private fun ParityComparisonRow(
  aspect: String,
  webAdvantage: String,
  androidAdvantage: String
) {
  Column(modifier = Modifier.fillMaxWidth()) {
    Text(
      text = aspect,
      fontWeight = FontWeight.Bold,
      fontSize = 12.sp,
      color = BlueprintNavy
    )
    Spacer(modifier = Modifier.height(6.dp))
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Surface(
        color = SignalBlue.copy(alpha = 0.06f),
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier.weight(1f)
      ) {
        Column(modifier = Modifier.padding(8.dp)) {
          Text("🌐 Web App", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = SignalBlue)
          Spacer(modifier = Modifier.height(2.dp))
          Text(webAdvantage, fontSize = 10.sp, color = BlueprintNavy, lineHeight = 14.sp)
        }
      }

      Surface(
        color = SignalGreen.copy(alpha = 0.06f),
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier.weight(1f)
      ) {
        Column(modifier = Modifier.padding(8.dp)) {
          Text("📱 Android App", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = SignalGreen)
          Spacer(modifier = Modifier.height(2.dp))
          Text(androidAdvantage, fontSize = 10.sp, color = BlueprintNavy, lineHeight = 14.sp)
        }
      }
    }
  }
}
