package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.RAppRepository
import com.example.model.ExecutionMode
import com.example.model.ReleaseChannel
import com.example.model.SovereignApp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RAppCenterScreen(
  onExecuteApp: (SovereignApp, ExecutionMode) -> Unit = { _, _ -> },
  isWebAppMode: Boolean = false,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val apps by RAppRepository.apps.collectAsState()
  var searchQuery by remember { mutableStateOf("") }
  var selectedChannel by remember { mutableStateOf<ReleaseChannel?>(null) }
  var showDeployDialog by remember { mutableStateOf(false) }
  var inspectingApp by remember { mutableStateOf<SovereignApp?>(null) }

  val filteredApps = remember(apps, searchQuery, selectedChannel) {
    apps.filter { app ->
      (searchQuery.isBlank() || app.name.contains(searchQuery, ignoreCase = true) || app.description.contains(searchQuery, ignoreCase = true) || app.category.contains(searchQuery, ignoreCase = true)) &&
      app.matchesChannel(selectedChannel)
    }
  }

  if (showDeployDialog) {
    DeployRAppDialog(
      onDismiss = { showDeployDialog = false },
      onDeploySuccess = {
        showDeployDialog = false
        Toast.makeText(context, "rApp erfolgreich dezentral im PlayStore bereitgestellt!", Toast.LENGTH_LONG).show()
      }
    )
  }

  if (inspectingApp != null) {
    AppDetailsDialog(
      app = inspectingApp!!,
      currentChannel = selectedChannel,
      isWebAppMode = isWebAppMode,
      onDismiss = { inspectingApp = null },
      onExecuteTestDemo = { onExecuteApp(inspectingApp!!, ExecutionMode.TEST_DEMO) },
      onExecuteMainReal = { onExecuteApp(inspectingApp!!, ExecutionMode.MAIN_REAL) }
    )
  }

  Scaffold(
    modifier = modifier
      .fillMaxSize()
      .testTag("rapp_center_screen"),
    floatingActionButton = {
      ExtendedFloatingActionButton(
        onClick = { showDeployDialog = true },
        containerColor = SignalBlue,
        contentColor = Color.White,
        icon = { Icon(Icons.Default.CloudUpload, contentDescription = "Deploy") },
        text = { Text("rApp Deployen", fontWeight = FontWeight.Bold) },
        modifier = Modifier.testTag("deploy_rapp_fab")
      )
    }
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues),
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      // 1. Hero Banner
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = BlueprintNavy),
          elevation = CardDefaults.cardElevation(4.dp)
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(42.dp)
                    .background(UrkundeGoldBg, CircleShape)
                    .border(2.dp, UrkundeGold, CircleShape),
                  contentAlignment = Alignment.Center
                ) {
                  Text("🏛️", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(
                    text = if (isWebAppMode) "rApp CENTER · WEB APP EDITION" else "rApp CENTER · PLAYSTORE",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                  )
                  Text(
                    text = if (isWebAppMode) "RFOF-NETWORK Autonome Versionskontrolle · GitHub Parität" else "RFOF-NETWORK Autonome Versionskontrolle · APK Edition",
                    style = MaterialTheme.typography.bodySmall,
                    color = UrkundeGold,
                    fontSize = 11.sp
                  )
                }
              }

              Surface(
                color = if (isWebAppMode) SignalGreenLight else SignalBlueLight,
                shape = RoundedCornerShape(6.dp)
              ) {
                Text(
                  text = if (isWebAppMode) "WEB APP AKTIV" else "APK AKTIV",
                  color = if (isWebAppMode) SignalGreen else SignalBlue,
                  fontWeight = FontWeight.Bold,
                  fontSize = 9.sp,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
              text = if (isWebAppMode) {
                "Singuläre Parität: Identische UI & Daten wie in der APK App. Klicke auf 'rApp Ausführen (APK App)', um nahtlos in die native APK App zurückzukehren."
              } else {
                "Der dezentrale App Store für souveräne Anwendungen. Verbinde dein Git Repository, teste auf dem autonomen Testnet oder veröffentliche auf dem Mainnet mit garantierter Versionsautonomie."
              },
              style = MaterialTheme.typography.bodySmall,
              color = Color(0xFFCBD5E1),
              fontSize = 12.sp
            )
          }
        }
      }

      // 2. Search & Filter
      item {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("rapp_search_input"),
          placeholder = { Text("rApp, Paket oder Kategorie suchen...") },
          leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Suche") },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(onClick = { searchQuery = "" }) {
                Icon(Icons.Default.Clear, contentDescription = "Löschen")
              }
            }
          },
          singleLine = true,
          shape = RoundedCornerShape(10.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SignalBlue,
            unfocusedBorderColor = BlueprintBorder
          )
        )
      }

      // 3. Channel Filter Chips
      item {
        val channelScrollState = rememberScrollState()
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .horizontalScroll(channelScrollState),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            FilterChip(
              selected = selectedChannel == null,
              onClick = { selectedChannel = null },
              label = { Text("Alle Kanäle", fontSize = 11.sp, fontWeight = if (selectedChannel == null) FontWeight.Bold else FontWeight.Normal) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = UrkundeGold,
                selectedLabelColor = Color.Black
              )
            )
            ReleaseChannel.values().forEach { channel ->
              val isSelected = selectedChannel == channel
              FilterChip(
                selected = isSelected,
                onClick = { selectedChannel = if (isSelected) null else channel },
                label = { Text(channel.label, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = Color(channel.badgeColor),
                  selectedLabelColor = Color.White
                )
              )
            }
          }

          // Status-Banner für aktuellen Kanalmodus
          Surface(
            color = when {
              selectedChannel == null -> UrkundeGoldBg
              selectedChannel?.isDual == true -> Color(0xFFFEF3C7)
              selectedChannel?.isTestnet == true -> SignalGreenLight
              selectedChannel?.isMainnet == true -> Color(0xFFEEF2FF)
              else -> Slate100
            },
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(
              1.dp,
              when {
                selectedChannel == null -> UrkundeGold
                selectedChannel?.isDual == true -> UrkundeGoldDark
                selectedChannel?.isTestnet == true -> SignalGreen
                selectedChannel?.isMainnet == true -> Color(0xFF6366F1)
                else -> BlueprintBorder
              }
            ),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                when {
                  selectedChannel == null -> "⚖️"
                  selectedChannel?.isDual == true -> "⚡"
                  selectedChannel?.isTestnet == true -> "🧪"
                  selectedChannel?.isMainnet == true -> "🌐"
                  else -> "📡"
                },
                fontSize = 13.sp
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = when {
                  selectedChannel == null -> "Anfang · Duale Parität: Alle Apps mit Test (Demo) & Main (Real)"
                  selectedChannel?.isDual == true -> "Ende · Dual-Parität: Beide Modi (Demo & Real) für alle Apps aktiv"
                  selectedChannel?.isTestnet == true -> "${selectedChannel?.label}: Nur Test (Demo) Modus aktiv · Reale Transaktionen ausgeblendet"
                  selectedChannel?.isMainnet == true -> "${selectedChannel?.label}: Nur Main (Real) Modus aktiv · Demo ausgeblendet"
                  else -> ""
                },
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = BlueprintNavy
              )
            }
          }
        }
      }

      // 4. Interconnection Banner (Verzahnung von Primär-rApp und 2 Extensions)
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("rapp_interconnection_banner"),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = BorderStroke(1.5.dp, UrkundeGold)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🔗", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "VERZAHNUNG: 1 PRIMÄR-RAPP & 2 EXTENSIONS",
                  fontWeight = FontWeight.Black,
                  fontSize = 11.sp,
                  color = BlueprintNavy
                )
              }
              Surface(color = SignalGreenLight, shape = RoundedCornerShape(4.dp)) {
                Text(
                  text = "3 APPS SYNCHRON",
                  color = SignalGreen,
                  fontSize = 8.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = "• 5 Release-Kanäle: Sovereign Testnet ➔ Sovereign Mainnet ➔ Ethereum Testnet ➔ Ethereum Mainnet ➔ Dual-Hybrid\n" +
                "• Dualer Ausführungsmodus: Jede rApp kann bei 'Alle Kanäle' direkt als 'Test (Demo)' oder 'Main (Real)' ausgeführt werden.\n" +
                "• Primär-rApp: 'PRAI / MTK / NEC' besitzt volle Singularität. In der APK App führt 'rApp Ausführen' zur Web App, in der Web App führt 'rApp Ausführen' zur APK App.\n" +
                "• Extension 1: 'ZON Universal DEX & Launchpad' ist exklusiv für Bonding-Curve Trading & Tokenerstellung verzahnt.\n" +
                "• Extension 2: 'XJustiz Notar & Archiv' ist exklusiv für Urkundenbeweissicherung & XJustiz-XML Export verzahnt.",
              fontSize = 11.sp,
              color = TextSecondary,
              lineHeight = 16.sp
            )
          }
        }
      }

      // 5. Header count
      item {
        Text(
          text = "${filteredApps.size} von ${apps.size} rApps registriert",
          style = MaterialTheme.typography.labelSmall,
          color = TextMuted
        )
      }

      // 6. Apps list
      items(filteredApps, key = { it.id }) { app ->
        RAppCardItem(
          app = app,
          currentChannel = selectedChannel,
          isWebAppMode = isWebAppMode,
          onClick = { inspectingApp = app },
          onCheckUpdate = {
            val msg = RAppRepository.checkForUpdates(app.id)
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
          },
          onExecuteTestDemo = { onExecuteApp(app, ExecutionMode.TEST_DEMO) },
          onExecuteMainReal = { onExecuteApp(app, ExecutionMode.MAIN_REAL) }
        )
      }

      item {
        Spacer(modifier = Modifier.height(70.dp))
      }
    }
  }
}

@Composable
fun RAppCardItem(
  app: SovereignApp,
  currentChannel: ReleaseChannel? = null,
  isWebAppMode: Boolean = false,
  onClick: () -> Unit,
  onCheckUpdate: () -> Unit,
  onExecuteTestDemo: () -> Unit = {},
  onExecuteMainReal: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .testTag("rapp_card_${app.id}"),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(
      if (app.isFlagship) 2.dp else 1.dp,
      if (app.isFlagship) UrkundeGold else BlueprintBorder
    ),
    shape = RoundedCornerShape(12.dp),
    elevation = CardDefaults.cardElevation(2.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
          Box(
            modifier = Modifier
              .size(46.dp)
              .background(if (app.isFlagship) UrkundeGoldBg else SignalBlueLight, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
          ) {
            Text(app.iconEmoji, fontSize = 24.sp)
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = app.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = BlueprintNavy
              )
              if (app.isFlagship) {
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                  color = UrkundeGold,
                  shape = RoundedCornerShape(4.dp)
                ) {
                  Text(
                    text = "PRIMÄR-RAPP · FLAGSHIP",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 8.sp,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                  )
                }
              } else if (app.id == "rapp-002") {
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                  color = UrkundeGoldDark,
                  shape = RoundedCornerShape(4.dp)
                ) {
                  Text(
                    text = "EXTENSION 1",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 8.sp,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                  )
                }
              } else if (app.id == "rapp-003") {
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                  color = Color(0xFF4F46E5),
                  shape = RoundedCornerShape(4.dp)
                ) {
                  Text(
                    text = "EXTENSION 2",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 8.sp,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                  )
                }
              }
            }
            Text(
              text = "${app.category} • v${app.versionName}",
              style = MaterialTheme.typography.bodySmall,
              color = TextMuted,
              fontSize = 11.sp
            )
            Text(
              text = app.packageName,
              style = MaterialTheme.typography.bodySmall,
              color = SignalBlue,
              fontSize = 10.sp
            )
            // Verzahnungs-Info
            Text(
              text = when (app.id) {
                "rapp-001" -> if (isWebAppMode) {
                  "🔗 Singularität: Web App ⮂ APK App Parität (rApp Ausführen wechselt zur APK App)"
                } else {
                  "🔗 Singularität: APK App ⮂ Web App Parität (rApp Ausführen wechselt zur Web App)"
                }
                "rapp-002" -> "🔗 Verzahnt mit: Primär-rApp & XJustiz Notariat (Bonding-Curve Treuhand)"
                "rapp-003" -> "🔗 Verzahnt mit: Primär-rApp & ZON DEX (Notariatsnachweis § 36 BeurkG)"
                else -> "🔗 Autonome rApp Parität"
              },
              fontSize = 9.sp,
              color = UrkundeGoldDark,
              fontWeight = FontWeight.SemiBold
            )
          }
        }

        Surface(
          color = Color(app.releaseChannel.badgeColor).copy(alpha = 0.15f),
          shape = RoundedCornerShape(6.dp)
        ) {
          Text(
            text = app.releaseChannel.label.take(16),
            color = Color(app.releaseChannel.badgeColor),
            fontWeight = FontWeight.Bold,
            fontSize = 9.sp,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = app.description,
        style = MaterialTheme.typography.bodySmall,
        color = BlueprintNavy,
        fontSize = 12.sp,
        maxLines = 2
      )

      Spacer(modifier = Modifier.height(10.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Verified, contentDescription = null, tint = UrkundeGold, modifier = Modifier.size(13.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = app.dualParityStatus,
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold,
            color = UrkundeGoldDark
          )
        }

        val isFlagship = app.isFlagship
        val showDemoButton = !isFlagship && when {
          currentChannel == null -> true // Alle Kanäle (Anfang) -> immer Demo und Real
          currentChannel.isDual -> true // Dual-Parität (Ende) -> immer Demo und Real
          currentChannel.isTestnet -> true // Testkanal -> Demo Button einblenden
          currentChannel.isMainnet -> false // Mainnet -> Demo Button ausblenden
          else -> true
        }
        val showRealButton = when {
          isFlagship -> true // Flagship hat immer den Web/APK App Button
          currentChannel == null -> true // Alle Kanäle (Anfang) -> immer Demo und Real
          currentChannel.isDual -> true // Dual-Parität (Ende) -> immer Demo und Real
          currentChannel.isTestnet -> false // Testkanal -> Realen Button ausblenden
          currentChannel.isMainnet -> true // Mainnet -> Realen Button einblenden
          else -> true
        }

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
          if (app.isFlagship) {
            // PRAI / MTK / NEC ist die Primär-rApp:
            // In APK App: wechselt in die Web App
            // In Web App: wechselt in die APK App
            Button(
              onClick = onExecuteMainReal,
              colors = ButtonDefaults.buttonColors(
                containerColor = if (isWebAppMode) Color(0xFF059669) else SignalBlue
              ),
              contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
              shape = RoundedCornerShape(6.dp),
              modifier = Modifier.testTag("rapp_btn_main_${app.id}")
            ) {
              Icon(
                if (isWebAppMode) Icons.Default.Android else Icons.Default.Language,
                contentDescription = null,
                modifier = Modifier.size(12.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                if (isWebAppMode) "rApp Ausführen (APK App)" else "rApp Ausführen (Web App)",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
            }
          } else {
            // Extension- & Community-rApps: Testnet / Mainnet / Dual-Parität Logik
            if (showDemoButton) {
              FilledTonalButton(
                onClick = onExecuteTestDemo,
                colors = ButtonDefaults.filledTonalButtonColors(
                  containerColor = SignalGreenLight,
                  contentColor = SignalGreen
                ),
                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.testTag("rapp_btn_test_${app.id}")
              ) {
                Icon(Icons.Default.Science, contentDescription = null, modifier = Modifier.size(11.dp))
                Spacer(modifier = Modifier.width(3.dp))
                Text("Test (Demo)", fontSize = 9.sp, fontWeight = FontWeight.Bold)
              }
            }

            if (showRealButton) {
              Button(
                onClick = onExecuteMainReal,
                colors = ButtonDefaults.buttonColors(
                  containerColor = if (app.id == "rapp-002") UrkundeGoldDark else Color(0xFF4F46E5)
                ),
                contentPadding = PaddingValues(horizontal = 7.dp, vertical = 4.dp),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.testTag("rapp_btn_main_${app.id}")
              ) {
                Icon(
                  when (app.id) {
                    "rapp-002" -> Icons.Default.SwapHoriz
                    else -> Icons.Default.VerifiedUser
                  },
                  contentDescription = null,
                  modifier = Modifier.size(11.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text("Main (Real)", fontSize = 9.sp, fontWeight = FontWeight.Bold)
              }
            }
          }

          OutlinedButton(
            onClick = onCheckUpdate,
            contentPadding = PaddingValues(horizontal = 5.dp, vertical = 4.dp),
            shape = RoundedCornerShape(6.dp)
          ) {
            Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(11.dp))
          }

          OutlinedButton(
            onClick = onClick,
            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
            shape = RoundedCornerShape(6.dp)
          ) {
            Text("Details", fontSize = 9.sp)
            Spacer(modifier = Modifier.width(2.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(10.dp))
          }
        }
      }
    }
  }
}

@Composable
fun DeployRAppDialog(
  onDismiss: () -> Unit,
  onDeploySuccess: () -> Unit
) {
  var name by remember { mutableStateOf("") }
  var packageName by remember { mutableStateOf("") }
  var category by remember { mutableStateOf("Sovereign DApp") }
  var repoUrl by remember { mutableStateOf("https://github.com/rfof-network/") }
  var versionName by remember { mutableStateOf("1.0.0") }
  var channel by remember { mutableStateOf(ReleaseChannel.SOVEREIGN_TESTNET) }
  var description by remember { mutableStateOf("") }
  var iconEmoji by remember { mutableStateOf("🚀") }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(14.dp),
      color = Color.White,
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Text(
          text = "Neue rApp Deployen",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          color = BlueprintNavy
        )
        Text(
          text = "Deploye aus deinem Git Repository direkt in das rApp Center mit autonomer Update-Garantie.",
          style = MaterialTheme.typography.bodySmall,
          color = TextMuted
        )

        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("App Name") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        OutlinedTextField(
          value = packageName,
          onValueChange = { packageName = it },
          label = { Text("Package Name (z.B. com.rfof.app)") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        OutlinedTextField(
          value = repoUrl,
          onValueChange = { repoUrl = it },
          label = { Text("Git Repository URL") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = versionName,
            onValueChange = { versionName = it },
            label = { Text("Version") },
            modifier = Modifier.weight(1f),
            singleLine = true
          )
          OutlinedTextField(
            value = iconEmoji,
            onValueChange = { iconEmoji = it },
            label = { Text("Emoji") },
            modifier = Modifier.width(80.dp),
            singleLine = true
          )
        }

        Text("Release-Kanal:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
        val deployChannelScrollState = rememberScrollState()
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(deployChannelScrollState),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          ReleaseChannel.values().forEach { ch ->
            FilterChip(
              selected = channel == ch,
              onClick = { channel = ch },
              label = { Text(ch.label, fontSize = 10.sp) }
            )
          }
        }

        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text("Beschreibung der rApp") },
          modifier = Modifier.fillMaxWidth(),
          maxLines = 3
        )

        Spacer(modifier = Modifier.height(8.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End,
          verticalAlignment = Alignment.CenterVertically
        ) {
          TextButton(onClick = onDismiss) {
            Text("Abbrechen")
          }
          Spacer(modifier = Modifier.width(8.dp))
          Button(
            onClick = {
              if (name.isNotBlank()) {
                RAppRepository.deployAppFromRepository(
                  name = name,
                  packageName = packageName,
                  category = category,
                  repositoryUrl = repoUrl,
                  versionName = versionName,
                  channel = channel,
                  description = description,
                  iconEmoji = iconEmoji
                )
                onDeploySuccess()
              }
            },
            colors = ButtonDefaults.buttonColors(containerColor = SignalGreen),
            enabled = name.isNotBlank()
          ) {
            Text("Im PlayStore Deployen")
          }
        }
      }
    }
  }
}

@Composable
fun AppDetailsDialog(
  app: SovereignApp,
  currentChannel: ReleaseChannel? = null,
  isWebAppMode: Boolean = false,
  onDismiss: () -> Unit,
  onExecuteTestDemo: () -> Unit = {},
  onExecuteMainReal: () -> Unit = {}
) {
  val context = LocalContext.current
  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(14.dp),
      color = Color.White,
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(50.dp)
              .background(SignalBlueLight, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
          ) {
            Text(app.iconEmoji, fontSize = 26.sp)
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(text = app.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = BlueprintNavy)
            Text(text = app.packageName, style = MaterialTheme.typography.bodySmall, color = TextMuted)
          }
        }

        HorizontalDivider(color = BlueprintBorder)

        Text(text = "Beschreibung:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Text(text = app.description, style = MaterialTheme.typography.bodySmall, color = BlueprintNavy)

        Text(text = "Spezifikationen:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Text(text = "• Version: ${app.versionName} (Build #${app.versionCode})", fontSize = 11.sp, color = TextMuted)
        Text(text = "• Kanal: ${app.releaseChannel.label}", fontSize = 11.sp, color = TextMuted)
        Text(text = "• Notariats-Zertifikat: ${app.notarialCertificate}", fontSize = 11.sp, color = UrkundeGoldDark, fontWeight = FontWeight.SemiBold)
        Text(text = "• Git Repository: ${app.repositoryUrl}", fontSize = 11.sp, color = SignalBlue)

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Autonome Updates:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
          Switch(
            checked = app.autoUpdateEnabled,
            onCheckedChange = { RAppRepository.toggleAutoUpdate(app.id) }
          )
        }

        Spacer(modifier = Modifier.height(10.dp))
        val isFlagship = app.isFlagship
        val showDemoButton = !isFlagship && when {
          currentChannel == null -> true // Alle Kanäle (Anfang) -> immer Demo und Real
          currentChannel.isDual -> true // Dual-Parität (Ende) -> immer Demo und Real
          currentChannel.isTestnet -> true // Testkanal -> Demo Button einblenden
          currentChannel.isMainnet -> false // Mainnet -> Demo Button ausblenden
          else -> true
        }
        val showRealButton = when {
          isFlagship -> true // Flagship hat immer den Web/APK App Button
          currentChannel == null -> true // Alle Kanäle (Anfang) -> immer Demo und Real
          currentChannel.isDual -> true // Dual-Parität (Ende) -> immer Demo und Real
          currentChannel.isTestnet -> false // Testkanal -> Realen Button ausblenden
          currentChannel.isMainnet -> true // Mainnet -> Realen Button einblenden
          else -> true
        }

        if (isFlagship) {
          // Primäre rApp PRAI / MTK / NEC:
          // In APK App: "rApp Ausführen (In Web App starten)"
          // In Web App: "rApp Ausführen (In APK App starten)"
          Button(
            onClick = {
              onDismiss()
              onExecuteMainReal()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
              containerColor = if (isWebAppMode) Color(0xFF059669) else SignalBlue
            )
          ) {
            Icon(
              if (isWebAppMode) Icons.Default.Android else Icons.Default.Language,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              if (isWebAppMode) "rApp Ausführen (APK App)" else "rApp Ausführen (Web App)",
              fontWeight = FontWeight.Bold
            )
          }
        } else {
          // Extensions / Community rApps: Kanalgesteuerter Modus
          if (showDemoButton && showRealButton) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              FilledTonalButton(
                onClick = {
                  onDismiss()
                  onExecuteTestDemo()
                },
                colors = ButtonDefaults.filledTonalButtonColors(
                  containerColor = SignalGreenLight,
                  contentColor = SignalGreen
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
              ) {
                Icon(Icons.Default.Science, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Test (Demo)", fontWeight = FontWeight.Bold, fontSize = 11.sp)
              }

              Button(
                onClick = {
                  onDismiss()
                  onExecuteMainReal()
                },
                colors = ButtonDefaults.buttonColors(
                  containerColor = if (app.id == "rapp-002") UrkundeGoldDark else Color(0xFF4F46E5)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
              ) {
                Icon(
                  when (app.id) {
                    "rapp-002" -> Icons.Default.SwapHoriz
                    else -> Icons.Default.VerifiedUser
                  },
                  contentDescription = null,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Main (Real)", fontWeight = FontWeight.Bold, fontSize = 11.sp)
              }
            }
          } else if (showDemoButton) {
            FilledTonalButton(
              onClick = {
                onDismiss()
                onExecuteTestDemo()
              },
              colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = SignalGreenLight,
                contentColor = SignalGreen
              ),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Icon(Icons.Default.Science, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("rApp Ausführen · Test (Demo)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
          } else if (showRealButton) {
            Button(
              onClick = {
                onDismiss()
                onExecuteMainReal()
              },
              colors = ButtonDefaults.buttonColors(
                containerColor = if (app.id == "rapp-002") UrkundeGoldDark else Color(0xFF4F46E5)
              ),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Icon(
                when (app.id) {
                  "rapp-002" -> Icons.Default.SwapHoriz
                  else -> Icons.Default.VerifiedUser
                },
                contentDescription = null,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text("rApp Ausführen · Main (Real)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
          }
        }
      }
    }
  }
}
