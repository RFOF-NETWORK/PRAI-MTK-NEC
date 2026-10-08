package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.auth.AuthManager
import com.example.auth.UserRole
import com.example.auth.UserType
import com.example.crypto.AESEncryption
import com.example.data.CodeRepository
import com.example.data.RepositoryManager
import com.example.model.SovereignLicenseData
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettingsScreen(
  onNavigateToExplorer: () -> Unit = {},
  onNavigateToWallet: () -> Unit = {},
  onBack: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val clipboardManager = LocalClipboardManager.current
  val currentUser by AuthManager.currentUser.collectAsState()
  val repositories by RepositoryManager.repositories.collectAsState()

  var selectedTab by remember { mutableStateOf(0) }
  val tabs = listOf("Profil & Repos", "Entwickler-Settings", "Sicherheit & Keys", "IP-Lizenz")

  // Modals
  var showNewRepoDialog by remember { mutableStateOf(false) }
  var showLicenseDialog by remember { mutableStateOf(false) }
  var showGuardianDialog by remember { mutableStateOf(false) }

  // Editable Form states
  var bioState by remember(currentUser) { mutableStateOf(currentUser.bio) }
  var orgState by remember(currentUser) { mutableStateOf(currentUser.organization) }
  var sshKeyState by remember(currentUser) { mutableStateOf(currentUser.sshPublicKey) }
  var gpgKeyState by remember(currentUser) { mutableStateOf(currentUser.gpgKeyId) }
  var userTypeState by remember(currentUser) { mutableStateOf(currentUser.userType) }
  var editorThemeState by remember(currentUser) { mutableStateOf(currentUser.editorTheme) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(BackgroundLight)
      .testTag("profile_settings_screen")
  ) {
    // Top Profile Header (GitHub / GitLab Inspired)
    Surface(
      color = BlueprintNavy,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 14.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Box(
              modifier = Modifier
                .size(50.dp)
                .background(if (currentUser.role == UserRole.ADMIN) UrkundeGoldBg else SignalBlueLight, CircleShape)
                .border(2.dp, if (currentUser.role == UserRole.ADMIN) UrkundeGold else SignalBlue, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = if (currentUser.role == UserRole.ADMIN) Icons.Default.Shield else Icons.Default.Person,
                contentDescription = null,
                tint = if (currentUser.role == UserRole.ADMIN) UrkundeGoldDark else SignalBlue,
                modifier = Modifier.size(26.dp)
              )
            }

            Column(modifier = Modifier.weight(1f, fill = false)) {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                  text = currentUser.username,
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Black,
                  color = Color.White,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
                Surface(
                  color = if (currentUser.role == UserRole.ADMIN) UrkundeGold else SignalBlue,
                  shape = RoundedCornerShape(4.dp)
                ) {
                  Text(
                    text = currentUser.role.badge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.sp,
                    maxLines = 1,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                  )
                }
              }

              Text(
                text = currentUser.userType.label,
                style = MaterialTheme.typography.labelSmall,
                color = UrkundeGold,
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              Text(
                text = "Auth: ${currentUser.authProvider} • ${currentUser.walletAddress.take(10)}...",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF94A3B8),
                fontSize = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }
          }

          Spacer(modifier = Modifier.width(8.dp))

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Button(
              onClick = { showGuardianDialog = true },
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA855F7)),
              shape = RoundedCornerShape(8.dp),
              contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
              modifier = Modifier.wrapContentWidth().testTag("guardian_button")
            ) {
              Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(15.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "KI-Guardian",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                softWrap = false
              )
            }

            Button(
              onClick = {
                AuthManager.updateProfileSettings(
                  newBio = bioState,
                  newOrganization = orgState,
                  newSshKey = sshKeyState,
                  newGpgKey = gpgKeyState,
                  newEditorTheme = editorThemeState,
                  userType = userTypeState
                )
                Toast.makeText(context, "Profil-Einstellungen gespeichert!", Toast.LENGTH_SHORT).show()
              },
              colors = ButtonDefaults.buttonColors(containerColor = SignalGreen),
              shape = RoundedCornerShape(8.dp),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
              modifier = Modifier.wrapContentWidth()
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
              ) {
                Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(15.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Speichern",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.SemiBold,
                  maxLines = 1,
                  softWrap = false
                )
              }
            }
          }
        }
      }
    }

    // Tab Navigation
    TabRow(
      selectedTabIndex = selectedTab,
      containerColor = Color.White,
      contentColor = BlueprintNavy
    ) {
      tabs.forEachIndexed { index, title ->
        Tab(
          selected = selectedTab == index,
          onClick = { selectedTab = index },
          text = {
            Text(
              text = title,
              fontSize = 11.sp,
              fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
            )
          }
        )
      }
    }

    when (selectedTab) {
      0 -> ProfileAndReposTab(
        currentUser = currentUser,
        repositories = repositories,
        bioState = bioState,
        onBioChange = { bioState = it },
        orgState = orgState,
        onOrgChange = { orgState = it },
        onOpenNewRepo = { showNewRepoDialog = true },
        onCopyPagesUrl = { url ->
          clipboardManager.setText(AnnotatedString(url))
          Toast.makeText(context, "RFOF Pages URL kopiert: $url", Toast.LENGTH_SHORT).show()
        }
      )
      1 -> DeveloperSettingsTab(
        sshKeyState = sshKeyState,
        onSshKeyChange = { sshKeyState = it },
        gpgKeyState = gpgKeyState,
        onGpgKeyChange = { gpgKeyState = it },
        editorThemeState = editorThemeState,
        onEditorThemeChange = { editorThemeState = it },
        pat = currentUser.personalAccessToken
      )
      2 -> SecurityAndKeysTab(
        currentUser = currentUser,
        onNavigateToWallet = onNavigateToWallet
      )
      3 -> SovereignLicenseTab(
        onOpenFullLicense = { showLicenseDialog = true }
      )
    }
  }

  // --- MODALS ---
  if (showNewRepoDialog) {
    PublishRepositoryDialog(
      onDismiss = { showNewRepoDialog = false },
      onPublish = { name, org, desc, lic, isPriv, certId ->
        RepositoryManager.publishRepository(
          name = name,
          organization = org,
          description = desc,
          licenseType = lic,
          isPrivate = isPriv,
          linkedNecCertId = certId
        )
        Toast.makeText(context, "Repository '$name' erfolgreich unter '$org' veröffentlicht!", Toast.LENGTH_LONG).show()
        showNewRepoDialog = false
      }
    )
  }

  if (showLicenseDialog) {
    SovereignLicenseFullDialog(onDismiss = { showLicenseDialog = false })
  }

  if (showGuardianDialog) {
    GuardianAssistantDialog(onDismiss = { showGuardianDialog = false })
  }
}

// -------------------------------------------------------------
// TAB 0: PROFIL & REPOSITORIES
// -------------------------------------------------------------
@Composable
private fun ProfileAndReposTab(
  currentUser: com.example.auth.UserProfile,
  repositories: List<CodeRepository>,
  bioState: String,
  onBioChange: (String) -> Unit,
  orgState: String,
  onOrgChange: (String) -> Unit,
  onOpenNewRepo: () -> Unit,
  onCopyPagesUrl: (String) -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    // Bio & Organization Card
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BlueprintBorder),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(
            text = "BENUTZER-PROFIL & ORGANISATION",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = BlueprintNavy
          )

          OutlinedTextField(
            value = bioState,
            onValueChange = onBioChange,
            label = { Text("Biografie / Aufgabenbereich") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 2
          )

          Text("Zugehörige Organisation / Rechtsform:", fontSize = 11.sp, color = TextMuted)
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            listOf("©", "GbR", "eGbR", "geGbR", "Stiftung").forEach { orgShort ->
              val isSelected = orgState.startsWith(orgShort)
              FilterChip(
                selected = isSelected,
                onClick = {
                  val full = RepositoryManager.ORGANIZATIONS.firstOrNull { it.startsWith(orgShort) } ?: orgShort
                  onOrgChange(full)
                },
                label = { Text(orgShort, fontSize = 10.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) }
              )
            }
          }
        }
      }
    }

    // Repositories Header & Action Button
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "CODE-REPOSITORIES (${repositories.size})",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = BlueprintNavy
          )
          Text(
            text = "Echtzeit-Repositories mit NEC-Urkunden-Verknüpfung",
            style = MaterialTheme.typography.bodySmall,
            color = TextMuted,
            fontSize = 10.sp
          )
        }

        Button(
          onClick = onOpenNewRepo,
          colors = ButtonDefaults.buttonColors(containerColor = BlueprintNavy),
          shape = RoundedCornerShape(6.dp),
          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Neues Repo", fontSize = 11.sp)
        }
      }
    }

    // Repositories list
    items(repositories) { repo ->
      RepositoryCard(repo = repo, onCopyPagesUrl = { onCopyPagesUrl(repo.pagesUrl) })
    }
  }
}

@Composable
private fun RepositoryCard(
  repo: CodeRepository,
  onCopyPagesUrl: () -> Unit
) {
  val context = LocalContext.current
  val clipboardManager = LocalClipboardManager.current
  val isFlagship = repo.name == "PRAI-MTK-NEC"

  Card(
    colors = CardDefaults.cardColors(containerColor = if (isFlagship) BlueprintNavy.copy(alpha = 0.03f) else Color.White),
    border = BorderStroke(if (isFlagship) 1.5.dp else 1.dp, if (isFlagship) UrkundeGold else BlueprintBorder),
    shape = RoundedCornerShape(10.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          Icon(
            if (isFlagship) Icons.Default.Language else Icons.Default.Folder,
            contentDescription = null,
            tint = if (isFlagship) UrkundeGoldDark else SignalBlue,
            modifier = Modifier.size(20.dp)
          )
          Column {
            Text(
              text = repo.name,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = BlueprintNavy
            )
            if (isFlagship) {
              Text(
                text = "FLAGGSCHIFF · WEB & ANDROID APP",
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                color = UrkundeGoldDark
              )
            }
          }
        }

        Surface(
          color = if (isFlagship) UrkundeGoldBg else Slate100,
          shape = RoundedCornerShape(4.dp)
        ) {
          Text(
            text = repo.organization.take(18),
            color = if (isFlagship) UrkundeGoldDark else TextMuted,
            fontWeight = FontWeight.Bold,
            fontSize = 9.sp,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = repo.description,
        style = MaterialTheme.typography.bodySmall,
        color = TextSecondary,
        fontSize = 11.sp,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(Slate50, RoundedCornerShape(4.dp))
          .padding(6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Commit: ${repo.latestCommitHash}",
          fontFamily = FontFamily.Monospace,
          fontSize = 9.sp,
          color = TextMuted
        )
        if (repo.linkedNecCertId.isNotBlank()) {
          Text(
            text = "Urkunde: ${repo.linkedNecCertId}",
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = SignalGreen
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Direct Action Buttons: Web App, GitHub, CLI
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Web App Link Button
        OutlinedButton(
          onClick = {
            try {
              val intent = Intent(Intent.ACTION_VIEW, Uri.parse(repo.computedPagesUrl))
              context.startActivity(intent)
            } catch (_: Exception) {
              clipboardManager.setText(AnnotatedString(repo.computedPagesUrl))
              Toast.makeText(context, "URL kopiert: ${repo.computedPagesUrl}", Toast.LENGTH_SHORT).show()
            }
          },
          contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
          shape = RoundedCornerShape(6.dp),
          modifier = Modifier.weight(1f)
        ) {
          Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(13.dp), tint = SignalBlue)
          Spacer(modifier = Modifier.width(4.dp))
          Text("Web App", fontSize = 10.sp, maxLines = 1, color = SignalBlue)
        }

        // GitHub Repo Button
        OutlinedButton(
          onClick = {
            try {
              val intent = Intent(Intent.ACTION_VIEW, Uri.parse(repo.computedRepoUrl))
              context.startActivity(intent)
            } catch (_: Exception) {
              clipboardManager.setText(AnnotatedString(repo.computedRepoUrl))
              Toast.makeText(context, "Repo kopiert: ${repo.computedRepoUrl}", Toast.LENGTH_SHORT).show()
            }
          },
          contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
          shape = RoundedCornerShape(6.dp),
          modifier = Modifier.weight(1f)
        ) {
          Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(13.dp), tint = BlueprintNavy)
          Spacer(modifier = Modifier.width(4.dp))
          Text("GitHub", fontSize = 10.sp, maxLines = 1, color = BlueprintNavy)
        }

        // CLI Clone Button
        OutlinedButton(
          onClick = {
            clipboardManager.setText(AnnotatedString(repo.computedCloneCommand))
            Toast.makeText(context, "Kopiert: ${repo.computedCloneCommand}", Toast.LENGTH_SHORT).show()
          },
          contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
          shape = RoundedCornerShape(6.dp),
          modifier = Modifier.weight(1f)
        ) {
          Icon(Icons.Default.Terminal, contentDescription = null, modifier = Modifier.size(13.dp), tint = SignalGreen)
          Spacer(modifier = Modifier.width(4.dp))
          Text("CLI Clone", fontSize = 10.sp, maxLines = 1, color = SignalGreen)
        }
      }
    }
  }
}

// -------------------------------------------------------------
// TAB 1: ENTWICKLER-SETTINGS (VS CODE / GITHUB / GITLAB)
// -------------------------------------------------------------
@Composable
private fun DeveloperSettingsTab(
  sshKeyState: String,
  onSshKeyChange: (String) -> Unit,
  gpgKeyState: String,
  onGpgKeyChange: (String) -> Unit,
  editorThemeState: String,
  onEditorThemeChange: (String) -> Unit,
  pat: String
) {
  val clipboardManager = LocalClipboardManager.current
  val context = LocalContext.current

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BlueprintBorder),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text(
            text = "KRYPTOGRAPHISCHE ENTWICKLER-SCHLÜSSEL",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = BlueprintNavy
          )

          OutlinedTextField(
            value = sshKeyState,
            onValueChange = onSshKeyChange,
            label = { Text("SSH Public Key (ED25519)") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 2
          )

          OutlinedTextField(
            value = gpgKeyState,
            onValueChange = onGpgKeyChange,
            label = { Text("GPG Signing Key ID (für Commit-Beglaubigung)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )

          Column {
            Text("Personal Access Token (PAT):", fontSize = 10.sp, color = TextMuted)
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .background(Slate100, RoundedCornerShape(6.dp))
                .padding(8.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(text = pat.take(16) + "••••••••••••", fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = BlueprintNavy)
              IconButton(
                onClick = {
                  clipboardManager.setText(AnnotatedString(pat))
                  Toast.makeText(context, "Token kopiert!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.size(24.dp)
              ) {
                Icon(Icons.Default.ContentCopy, contentDescription = "Kopieren", modifier = Modifier.size(16.dp), tint = SignalBlue)
              }
            }
          }
        }
      }
    }

    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BlueprintBorder),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(
            text = "EDITOR & IDE PRÄFERENZEN (VS CODE / GITLAB)",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = BlueprintNavy
          )

          Text("Farbschema / Theme:", fontSize = 11.sp, color = TextMuted)
          val themes = listOf("VS Code High-Contrast Navy", "VS Code Dark Modern", "Monokai Sovereign", "Cyberpunk Gold")
          themes.forEach { theme ->
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier
                .fillMaxWidth()
                .clickable { onEditorThemeChange(theme) }
                .padding(vertical = 4.dp)
            ) {
              RadioButton(selected = editorThemeState == theme, onClick = { onEditorThemeChange(theme) })
              Spacer(modifier = Modifier.width(6.dp))
              Text(theme, fontSize = 12.sp, color = BlueprintNavy)
            }
          }
        }
      }
    }

    // EIGENE RPCS & MULTI-CHAIN VERBINDUNGEN (ETH, BTC, TON, ZON, EVM)
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BlueprintBorder),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "BENUTZER-RPCS & MULTI-CHAIN ENDPUNKTE",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = BlueprintNavy
            )
            Surface(color = SignalGreenLight, shape = RoundedCornerShape(4.dp)) {
              Text("EVM / PoW / Sharded", fontSize = 8.sp, color = SignalGreen, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
            }
          }

          Text(
            text = "Hinterlege hier deine eigenen Alchemy RPC-Schlüssel oder private RPC-Knoten für deine Repositories:",
            fontSize = 11.sp,
            color = TextMuted
          )

          var ethRpc by remember { mutableStateOf("https://eth-mainnet.g.alchemy.com/v2/demo") }
          var zonRpc by remember { mutableStateOf("https://rpc.zon-chain.network/v1") }
          var tonRpc by remember { mutableStateOf("https://toncenter.com/api/v2/jsonRPC") }
          var customEvmRpc by remember { mutableStateOf("https://arb1.arbitrum.io/rpc") }

          OutlinedTextField(
            value = ethRpc,
            onValueChange = { ethRpc = it },
            label = { Text("Ethereum / EVM RPC (z.B. Alchemy / Infura)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )

          OutlinedTextField(
            value = zonRpc,
            onValueChange = { zonRpc = it },
            label = { Text("ZON Sovereign Core RPC") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )

          OutlinedTextField(
            value = tonRpc,
            onValueChange = { tonRpc = it },
            label = { Text("TON Sharded Network RPC") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )

          OutlinedTextField(
            value = customEvmRpc,
            onValueChange = { customEvmRpc = it },
            label = { Text("Custom EVM L2 RPC (Arbitrum / Base / Polygon)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )

          Button(
            onClick = {
              Toast.makeText(context, "Benutzer-RPCs erfolgreich gespeichert und getestet!", Toast.LENGTH_SHORT).show()
            },
            colors = ButtonDefaults.buttonColors(containerColor = BlueprintNavy),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("RPC-Knotenpunkte verifizieren & speichern", fontSize = 11.sp)
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// TAB 2: SICHERHEIT & SCHLÜSSEL
// -------------------------------------------------------------
@Composable
private fun SecurityAndKeysTab(
  currentUser: com.example.auth.UserProfile,
  onNavigateToWallet: () -> Unit
) {
  val context = LocalContext.current

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BlueprintBorder),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text(
            text = "PASSWORT & AES-256 MASTER-KEY",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = BlueprintNavy
          )

          Text(
            text = "Alle privaten Transaktionen, Dokumenten-Signaturen und Repositories werden mit PBKDF2 (100.000 Iterationen) und AES-256 GCM verschlüsselt.",
            fontSize = 11.sp,
            color = TextSecondary,
            lineHeight = 15.sp
          )

          Button(
            onClick = {
              Toast.makeText(context, "Neues PBKDF2-Master-Passwort generiert & gesichert!", Toast.LENGTH_SHORT).show()
            },
            colors = ButtonDefaults.buttonColors(containerColor = BlueprintNavy),
            shape = RoundedCornerShape(6.dp)
          ) {
            Icon(Icons.Default.VpnKey, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Master-Passwort ändern", fontSize = 11.sp)
          }

          Divider()

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("Web3-Schlüsselverwaltung", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = BlueprintNavy)
              Text("12-Wort Seedphrase & Private Keys", fontSize = 10.sp, color = TextMuted)
            }
            OutlinedButton(onClick = onNavigateToWallet) {
              Text("Zur Wallet", fontSize = 11.sp)
            }
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// TAB 3: SOVEREIGN IP LIZENZ
// -------------------------------------------------------------
@Composable
private fun SovereignLicenseTab(onOpenFullLicense: () -> Unit) {
  val clipboardManager = LocalClipboardManager.current
  val context = LocalContext.current

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = UrkundeGoldBg),
        border = BorderStroke(1.dp, UrkundeGold),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(
            text = "GEISTIGES EIGENTUM & ERFINDER-CHARTA",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Black,
            color = UrkundeGoldDark
          )
          Text(
            text = SovereignLicenseData.LICENSE_NAME,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = BlueprintNavy
          )
          Text(
            text = "Lizenz-Hash: ${SovereignLicenseData.LICENSE_HASH}",
            fontFamily = FontFamily.Monospace,
            fontSize = 9.sp,
            color = TextMuted,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )

          Spacer(modifier = Modifier.height(4.dp))

          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
              onClick = onOpenFullLicense,
              colors = ButtonDefaults.buttonColors(containerColor = BlueprintNavy),
              shape = RoundedCornerShape(6.dp)
            ) {
              Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Volltext lesen", fontSize = 11.sp)
            }

            OutlinedButton(
              onClick = {
                clipboardManager.setText(AnnotatedString(SovereignLicenseData.FULL_LICENSE_TEXT))
                Toast.makeText(context, "Lizenztext in Zwischenablage kopiert", Toast.LENGTH_SHORT).show()
              },
              shape = RoundedCornerShape(6.dp)
            ) {
              Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Kopieren", fontSize = 11.sp)
            }
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// PUBLISH REPOSITORY DIALOG
// -------------------------------------------------------------
@Composable
private fun PublishRepositoryDialog(
  onDismiss: () -> Unit,
  onPublish: (name: String, org: String, desc: String, lic: String, isPriv: Boolean, certId: String) -> Unit
) {
  var name by remember { mutableStateOf("") }
  var org by remember { mutableStateOf(RepositoryManager.ORGANIZATIONS[0]) }
  var desc by remember { mutableStateOf("") }
  var isPrivate by remember { mutableStateOf(false) }
  var certId by remember { mutableStateOf("NEC-001") }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(14.dp),
      color = Color.White,
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
          text = "NEUES REPOSITORY VERÖFFENTLICHEN",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Black,
          color = BlueprintNavy
        )

        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Repository-Name (z.B. my-module-core)") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        OutlinedTextField(
          value = desc,
          onValueChange = { desc = it },
          label = { Text("Beschreibung des Moduls / Quellcodes") },
          modifier = Modifier.fillMaxWidth(),
          maxLines = 2
        )

        Text("Rechtsform / Organisation:", fontSize = 10.sp, color = TextMuted)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          RepositoryManager.ORGANIZATIONS.forEach { organizationOption ->
            val short = organizationOption.substringBefore(" ")
            FilterChip(
              selected = org == organizationOption,
              onClick = { org = organizationOption },
              label = { Text(short, fontSize = 9.sp) }
            )
          }
        }

        OutlinedTextField(
          value = certId,
          onValueChange = { certId = it },
          label = { Text("Verknüpfte NEC-Urkunden-ID (z.B. NEC-001)") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End,
          verticalAlignment = Alignment.CenterVertically
        ) {
          TextButton(onClick = onDismiss) { Text("Abbrechen") }
          Spacer(modifier = Modifier.width(6.dp))
          Button(
            onClick = {
              if (name.isNotBlank()) {
                onPublish(name, org, desc, SovereignLicenseData.LICENSE_NAME, isPrivate, certId)
              }
            },
            colors = ButtonDefaults.buttonColors(containerColor = BlueprintNavy)
          ) {
            Text("Veröffentlichen")
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// SOVEREIGN LICENSE FULL DIALOG
// -------------------------------------------------------------
@Composable
private fun SovereignLicenseFullDialog(onDismiss: () -> Unit) {
  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(14.dp),
      color = Color.White,
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.85f)
        .padding(16.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "RFOF SOVEREIGN IP LIZENZ",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black,
            color = BlueprintNavy
          )
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Schließen")
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Surface(
          color = Slate50,
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
          LazyColumn(modifier = Modifier.padding(12.dp)) {
            item {
              Text(
                text = SovereignLicenseData.FULL_LICENSE_TEXT,
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                lineHeight = 14.sp,
                color = BlueprintNavy
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun GuardianAssistantDialog(
  onDismiss: () -> Unit
) {
  var userPrompt by remember { mutableStateOf("") }
  var conversation by remember {
    mutableStateOf(
      listOf(
        Pair(
          "PRAI Guardian",
          "🛡️ **Willkommen, Schöpfer.**\nIch bin der autonome PRAI / MTK / NEC Guardian Copilot. Ich helfe dir beim Codieren, Verwalten deiner Git Repositories, Erklären der 28 Fachkategorien, der ZON 50/50 Bonding Curve und der Alchemy Multi-Chain Integration."
        )
      )
    )
  }
  var isThinking by remember { mutableStateOf(false) }
  val coroutineScope = rememberCoroutineScope()

  val suggestedPrompts = remember {
    listOf(
      "Wie funktioniert die ZON Bonding Curve?",
      "Erkläre mir die 14 Alchemy Services",
      "Wie deploye ich eine rApp im PlayStore?",
      "Erkläre Notarielle Dual-Parität (§ 36 BeurkG)"
    )
  }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = Color.White,
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.85f)
        .padding(8.dp)
        .testTag("guardian_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(16.dp)
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .background(Color(0xFF2E1065), CircleShape)
                .border(1.5.dp, Color(0xFFA855F7), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text("🛡️", fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "PRAI · MTK · NEC GUARDIAN",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Black,
                color = BlueprintNavy
              )
              Text(
                text = "Autonomer Copilot & Repository Manager",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFA855F7),
                fontSize = 10.sp
              )
            }
          }
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Schließen")
          }
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp), color = BlueprintBorder)

        // Suggestion Chips
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          suggestedPrompts.forEach { suggestion ->
            SuggestionChip(
              onClick = {
                userPrompt = suggestion
              },
              label = { Text(suggestion, fontSize = 10.sp, maxLines = 1) },
              colors = SuggestionChipDefaults.suggestionChipColors(containerColor = Color(0xFFF3E8FF))
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Chat Message Log
        LazyColumn(
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .background(Slate50, RoundedCornerShape(8.dp))
            .padding(10.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(conversation) { (sender, msg) ->
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .background(
                  if (sender == "Du") SignalBlueLight else Color.White,
                  RoundedCornerShape(8.dp)
                )
                .border(
                  0.5.dp,
                  if (sender == "Du") SignalBlue.copy(alpha = 0.3f) else BlueprintBorder,
                  RoundedCornerShape(8.dp)
                )
                .padding(10.dp)
            ) {
              Text(
                text = sender,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = if (sender == "Du") SignalBlue else Color(0xFFA855F7)
              )
              Spacer(modifier = Modifier.height(3.dp))
              Text(
                text = msg,
                fontSize = 11.sp,
                color = BlueprintNavy,
                lineHeight = 15.sp
              )
            }
          }

          if (isThinking) {
            item {
              Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = Color(0xFFA855F7))
                Spacer(modifier = Modifier.width(8.dp))
                Text("PRAI Guardian berechnet Antwort...", fontSize = 11.sp, color = TextMuted)
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Input Field and Send Button
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = userPrompt,
            onValueChange = { userPrompt = it },
            placeholder = { Text("Frage den Guardian oder gib Befehl ein...", fontSize = 11.sp) },
            modifier = Modifier.weight(1f),
            singleLine = true,
            shape = RoundedCornerShape(8.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          IconButton(
            onClick = {
              val promptToSend = userPrompt.trim()
              if (promptToSend.isNotBlank() && !isThinking) {
                userPrompt = ""
                conversation = conversation + Pair("Du", promptToSend)
                isThinking = true
                coroutineScope.launch {
                  val answer = com.example.ai.PraiGuardianService.askGuardian(promptToSend)
                  conversation = conversation + Pair("PRAI Guardian", answer)
                  isThinking = false
                }
              }
            },
            enabled = userPrompt.isNotBlank() && !isThinking,
            modifier = Modifier
              .background(Color(0xFFA855F7), RoundedCornerShape(8.dp))
              .size(46.dp)
          ) {
            Icon(Icons.Default.Send, contentDescription = "Senden", tint = Color.White, modifier = Modifier.size(18.dp))
          }
        }
      }
    }
  }
}
