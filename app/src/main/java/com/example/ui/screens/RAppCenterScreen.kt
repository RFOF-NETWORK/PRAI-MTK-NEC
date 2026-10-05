package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.model.ReleaseChannel
import com.example.model.SovereignApp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RAppCenterScreen(
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
      (selectedChannel == null || app.releaseChannel == selectedChannel)
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
      onDismiss = { inspectingApp = null }
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
                    text = "rApp CENTER · PLAYSTORE",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                  )
                  Text(
                    text = "RFOF-NETWORK Autonome Versionskontrolle",
                    style = MaterialTheme.typography.bodySmall,
                    color = UrkundeGold,
                    fontSize = 11.sp
                  )
                }
              }

              Surface(
                color = SignalGreenLight,
                shape = RoundedCornerShape(6.dp)
              ) {
                Text(
                  text = "AUTONOM",
                  color = SignalGreen,
                  fontWeight = FontWeight.Bold,
                  fontSize = 9.sp,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
              text = "Der dezentrale App Store für souveräne Anwendungen. Verbinde dein Git Repository, teste auf dem autonomen Testnet oder veröffentliche auf dem Mainnet mit garantierter Versionsautonomie.",
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
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          FilterChip(
            selected = selectedChannel == null,
            onClick = { selectedChannel = null },
            label = { Text("Alle Kanäle", fontSize = 11.sp) }
          )
          ReleaseChannel.values().forEach { channel ->
            val isSelected = selectedChannel == channel
            FilterChip(
              selected = isSelected,
              onClick = { selectedChannel = if (isSelected) null else channel },
              label = { Text(channel.label.take(18) + "...", fontSize = 11.sp) }
            )
          }
        }
      }

      // 4. Header count
      item {
        Text(
          text = "${filteredApps.size} von ${apps.size} rApps registriert",
          style = MaterialTheme.typography.labelSmall,
          color = TextMuted
        )
      }

      // 5. Apps list
      items(filteredApps, key = { it.id }) { app ->
        RAppCardItem(
          app = app,
          onClick = { inspectingApp = app },
          onCheckUpdate = {
            val msg = RAppRepository.checkForUpdates(app.id)
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
          }
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
  onClick: () -> Unit,
  onCheckUpdate: () -> Unit,
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
                    text = "FLAGSHIP",
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

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          OutlinedButton(
            onClick = onCheckUpdate,
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            shape = RoundedCornerShape(6.dp)
          ) {
            Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(12.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Update", fontSize = 10.sp)
          }

          Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = BlueprintNavy),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
            shape = RoundedCornerShape(6.dp)
          ) {
            Text("Details", fontSize = 10.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(12.dp))
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
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          ReleaseChannel.values().forEach { ch ->
            FilterChip(
              selected = channel == ch,
              onClick = { channel = ch },
              label = { Text(ch.label.take(12), fontSize = 10.sp) }
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
  onDismiss: () -> Unit
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

        Divider(color = BlueprintBorder)

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
        Button(
          onClick = {
            Toast.makeText(context, "${app.name} wird im autonomen Container ausgeführt!", Toast.LENGTH_SHORT).show()
            onDismiss()
          },
          modifier = Modifier.fillMaxWidth(),
          colors = ButtonDefaults.buttonColors(containerColor = BlueprintNavy)
        ) {
          Text("rApp Ausführen")
        }
      }
    }
  }
}
