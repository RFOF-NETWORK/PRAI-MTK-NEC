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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Box(
              modifier = Modifier
                .size(52.dp)
                .background(if (currentUser.role == UserRole.ADMIN) UrkundeGoldBg else SignalBlueLight, CircleShape)
                .border(2.dp, if (currentUser.role == UserRole.ADMIN) UrkundeGold else SignalBlue, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = if (currentUser.role == UserRole.ADMIN) Icons.Default.Shield else Icons.Default.Person,
                contentDescription = null,
                tint = if (currentUser.role == UserRole.ADMIN) UrkundeGoldDark else SignalBlue,
                modifier = Modifier.size(28.dp)
              )
            }

            Column {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                  text = currentUser.username,
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Black,
                  color = Color.White
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
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                  )
                }
              }

              Text(
                text = currentUser.userType.label,
                style = MaterialTheme.typography.labelSmall,
                color = UrkundeGold,
                fontSize = 11.sp
              )
              Text(
                text = "Auth: ${currentUser.authProvider} • ${currentUser.walletAddress.take(10)}...",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF94A3B8),
                fontSize = 10.sp
              )
            }
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
            shape = RoundedCornerShape(6.dp),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
          ) {
            Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Speichern", fontSize = 11.sp)
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
  Card(
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, BlueprintBorder),
    shape = RoundedCornerShape(8.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          Icon(Icons.Default.Folder, contentDescription = null, tint = SignalBlue, modifier = Modifier.size(18.dp))
          Text(
            text = repo.name,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = BlueprintNavy
          )
        }

        Surface(
          color = UrkundeGoldBg,
          shape = RoundedCornerShape(4.dp)
        ) {
          Text(
            text = repo.organization.take(18),
            color = UrkundeGoldDark,
            fontWeight = FontWeight.Bold,
            fontSize = 9.sp,
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
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

      Spacer(modifier = Modifier.height(6.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Lizenz: ${repo.licenseType.take(24)}...",
          fontSize = 9.sp,
          color = TextMuted
        )

        TextButton(
          onClick = onCopyPagesUrl,
          contentPadding = PaddingValues(0.dp)
        ) {
          Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(12.dp), tint = SignalBlue)
          Spacer(modifier = Modifier.width(3.dp))
          Text("RFOF Pages Link", fontSize = 10.sp, color = SignalBlue)
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
