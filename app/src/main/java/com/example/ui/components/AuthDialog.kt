package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.auth.*
import com.example.data.WalletRepository
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun AuthDialog(
  onDismiss: () -> Unit
) {
  val coroutineScope = rememberCoroutineScope()
  val currentUser by AuthManager.currentUser.collectAsState()
  val currentMode by AuthManager.authExecutionMode.collectAsState()
  val parallelProviders by AuthManager.parallelProviders.collectAsState()

  var showSuccessMsg by remember { mutableStateOf<String?>(null) }
  var showErrorMsg by remember { mutableStateOf<String?>(null) }
  var activeTab by remember { mutableIntStateOf(0) } // 0: Login & Registrieren, 1: 5 Parallele Provider, 2: Schnell-Anmeldung
  var isRegisterMode by remember { mutableStateOf(false) }
  var usernameInput by remember { mutableStateOf("") }
  var passwordInput by remember { mutableStateOf("") }
  var userTypeInput by remember { mutableStateOf(UserType.ERFINDER) }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.96f)
        .fillMaxHeight(0.92f)
        .padding(8.dp)
        .testTag("auth_dialog"),
      shape = RoundedCornerShape(16.dp),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 8.dp
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(18.dp)
      ) {
        // 1. Dialog Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .background(BlueprintNavy, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                tint = UrkundeGold,
                modifier = Modifier.size(20.dp)
              )
            }
            Column {
              Text(
                text = "MULTI-PARALLEL AUTH HUB",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = BlueprintNavy,
                fontSize = 14.sp
              )
              Text(
                text = "Entropy Double Proxy Validator · Serverless Level",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                fontSize = 10.sp
              )
            }
          }
          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("close_auth_dialog_button")
          ) {
            Icon(Icons.Default.Close, contentDescription = "Schließen", tint = TextMuted)
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 2. Deterministic Mode Selector (Test/Demo vs Main/Real)
        Surface(
          color = Slate50,
          shape = RoundedCornerShape(10.dp),
          border = BorderStroke(1.dp, BlueprintBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(10.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "DETERMINISTISCHER MODUS:",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = BlueprintNavy
              )

              Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                FilterChip(
                  selected = currentMode == AuthExecutionMode.TEST_DEMO,
                  onClick = {
                    AuthManager.setExecutionMode(AuthExecutionMode.TEST_DEMO)
                    showSuccessMsg = "Modus auf TEST (DEMO) umgeschaltet. Nur Sandbox-Tokens aktiv."
                    showErrorMsg = null
                  },
                  label = { Text("🧪 Test (Demo)", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                  colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = SignalGreenLight,
                    selectedLabelColor = SignalGreen
                  )
                )

                FilterChip(
                  selected = currentMode == AuthExecutionMode.MAIN_REAL,
                  onClick = {
                    AuthManager.setExecutionMode(AuthExecutionMode.MAIN_REAL)
                    showSuccessMsg = "Modus auf MAIN (REAL) umgeschaltet. Keine Demo-Zulassung."
                    showErrorMsg = null
                  },
                  label = { Text("🛡️ Main (Real)", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                  colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFEEF2FF),
                    selectedLabelColor = Color(0xFF4F46E5)
                  )
                )
              }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = if (currentMode == AuthExecutionMode.TEST_DEMO) {
                "⚡ Im Test-Modus arbeitet nur die Test-Authentifizierung deterministisch isoliert. Reale Transaktions-Keys sind gesperrt."
              } else {
                "🔒 Im Main-Modus sind Demo/Test-Tokens strikt deaktiviert. Nur echte kryptographische Provider-Zertifikate werden akzeptiert."
              },
              fontSize = 9.sp,
              color = if (currentMode == AuthExecutionMode.TEST_DEMO) SignalGreen else Color(0xFF4F46E5),
              fontWeight = FontWeight.Medium
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Feedback Messages
        if (showSuccessMsg != null) {
          Surface(
            color = SignalGreenLight,
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SignalGreen, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(text = showSuccessMsg ?: "", color = SignalGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
          }
          Spacer(modifier = Modifier.height(6.dp))
        }

        if (showErrorMsg != null) {
          Surface(
            color = Color(0xFFFEE2E2),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(text = showErrorMsg ?: "", color = Color(0xFFDC2626), fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
          }
          Spacer(modifier = Modifier.height(6.dp))
        }

        // Active Session Badge
        val isAdmin = currentUser.role == UserRole.ADMIN
        Surface(
          color = BlueprintNavy,
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "PRIMÄRE IDENTITÄT: ${currentUser.username}",
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Provider: ${currentUser.authProvider} · Rolle: ${currentUser.role.displayName}",
                color = if (isAdmin) UrkundeGold else Color(0xFF94A3B8),
                fontSize = 9.sp
              )
            }
            Surface(
              color = if (isAdmin) UrkundeGold else SignalBlue,
              shape = RoundedCornerShape(4.dp)
            ) {
              Text(
                text = currentUser.role.badge,
                color = if (isAdmin) Color.Black else Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Tab Row
        TabRow(
          selectedTabIndex = activeTab,
          containerColor = Color.Transparent,
          contentColor = BlueprintNavy,
          modifier = Modifier.fillMaxWidth()
        ) {
          Tab(
            selected = activeTab == 0,
            onClick = { activeTab = 0 },
            text = { Text("🔑 Login / Registrieren", fontSize = 10.sp, fontWeight = FontWeight.Bold) }
          )
          Tab(
            selected = activeTab == 1,
            onClick = { activeTab = 1 },
            text = { Text("🛡️ 5 Parallel Provider", fontSize = 10.sp, fontWeight = FontWeight.Bold) }
          )
          Tab(
            selected = activeTab == 2,
            onClick = { activeTab = 2 },
            text = { Text("⚡ Schnell-Auswahl", fontSize = 10.sp, fontWeight = FontWeight.Bold) }
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Tab Content
        if (activeTab == 0) {
          // Tab 0: Credentials Login & Account Creation
          LazyColumn(
            modifier = Modifier
              .weight(1f)
              .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            item {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                FilterChip(
                  selected = !isRegisterMode,
                  onClick = { isRegisterMode = false },
                  label = { Text("Einloggen", fontWeight = FontWeight.Bold) },
                  colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = BlueprintNavy,
                    selectedLabelColor = Color.White
                  ),
                  modifier = Modifier.weight(1f)
                )
                FilterChip(
                  selected = isRegisterMode,
                  onClick = { isRegisterMode = true },
                  label = { Text("Neuen Account erstellen", fontWeight = FontWeight.Bold) },
                  colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = UrkundeGold,
                    selectedLabelColor = BlueprintNavy
                  ),
                  modifier = Modifier.weight(1f)
                )
              }
            }

            if (!isRegisterMode) {
              // LOGIN MODE
              item {
                Card(
                  colors = CardDefaults.cardColors(containerColor = Slate50),
                  border = BorderStroke(1.dp, BlueprintBorder),
                  shape = RoundedCornerShape(10.dp)
                ) {
                  Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Anmelden mit Account oder Master-Phrasen", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = BlueprintNavy)
                    
                    OutlinedTextField(
                      value = usernameInput,
                      onValueChange = { usernameInput = it },
                      label = { Text("Benutzername (z.B. Satoramy oder RFOF-NETWORK)") },
                      singleLine = true,
                      modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                      value = passwordInput,
                      onValueChange = { passwordInput = it },
                      label = { Text("Passwort oder Master Seed-Phrase") },
                      singleLine = true,
                      modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                      modifier = Modifier.fillMaxWidth(),
                      horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                      Button(
                        onClick = {
                          val res = AuthManager.loginWithCredentials(usernameInput, passwordInput)
                          if (res.success) {
                            WalletRepository.refreshAssetsForCurrentRole()
                            showSuccessMsg = res.message
                            showErrorMsg = null
                          } else {
                            showErrorMsg = res.message
                          }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BlueprintNavy),
                        modifier = Modifier.weight(1f)
                      ) {
                        Text("Anmelden", color = Color.White, fontWeight = FontWeight.Bold)
                      }
                    }

                    // Dual Account Explanation
                    Surface(
                      color = Color(0xFFFEF3C7),
                      shape = RoundedCornerShape(6.dp),
                      modifier = Modifier.fillMaxWidth()
                    ) {
                      Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                          text = "⭐ Dual-Account Hinweis (Satoramy & RFOF-NETWORK):",
                          fontWeight = FontWeight.Bold,
                          fontSize = 10.sp,
                          color = Color(0xFF92400E)
                        )
                        Text(
                          text = "Der Creator besitzt zwei verbundene Accounts. Das manuelle Passwort von Satoramy öffnet auch RFOF-NETWORK. Satoramy und RFOF-NETWORK haben beide vollen Admin-Zugriff und Phrasen-Einsicht.",
                          fontSize = 9.sp,
                          color = Color(0xFF78350F)
                        )
                      }
                    }
                  }
                }
              }
            } else {
              // REGISTER MODE
              item {
                Card(
                  colors = CardDefaults.cardColors(containerColor = Slate50),
                  border = BorderStroke(1.dp, BlueprintBorder),
                  shape = RoundedCornerShape(10.dp)
                ) {
                  Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Neuen Account registrieren (Eindeutiger Name)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = BlueprintNavy)
                    
                    OutlinedTextField(
                      value = usernameInput,
                      onValueChange = { usernameInput = it },
                      label = { Text("Gewünschter Benutzername (z.B. Satoramy)") },
                      singleLine = true,
                      modifier = Modifier.fillMaxWidth()
                    )

                    if (usernameInput.trim().equals("satoramy", ignoreCase = true) || usernameInput.trim().equals("sartoramy", ignoreCase = true)) {
                      Surface(
                        color = Color(0xFFFEF9C3),
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, UrkundeGold),
                        modifier = Modifier.fillMaxWidth()
                      ) {
                        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                          Icon(Icons.Default.Star, contentDescription = null, tint = UrkundeGoldDark, modifier = Modifier.size(16.dp))
                          Spacer(modifier = Modifier.width(6.dp))
                          Text(
                            text = "Dual-Identity Creator-Account erkannt! Wird automatisch mit RFOF-NETWORK verknüpft (Admin & Nutzer, geteiltes Passwort, Phrasen-Zugang).",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = UrkundeGoldDark
                          )
                        }
                      }
                    }

                    OutlinedTextField(
                      value = passwordInput,
                      onValueChange = { passwordInput = it },
                      label = { Text("Passwort festlegen (min. 4 Zeichen)") },
                      singleLine = true,
                      modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                      onClick = {
                        val res = AuthManager.registerAccount(usernameInput, passwordInput, userTypeInput)
                        if (res.success) {
                          WalletRepository.refreshAssetsForCurrentRole()
                          showSuccessMsg = res.message
                          showErrorMsg = null
                        } else {
                          showErrorMsg = res.message
                        }
                      },
                      colors = ButtonDefaults.buttonColors(containerColor = UrkundeGold),
                      modifier = Modifier.fillMaxWidth()
                    ) {
                      Text("Account erstellen & Einloggen", color = BlueprintNavy, fontWeight = FontWeight.Black)
                    }
                  }
                }
              }
            }

            // Master Phrases Info Box for Creator
            if (currentUser.role == UserRole.ADMIN || currentUser.username == "Satoramy") {
              item {
                Surface(
                  color = Slate100,
                  shape = RoundedCornerShape(8.dp),
                  border = BorderStroke(1.dp, BlueprintBorder),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Column(modifier = Modifier.padding(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Icon(Icons.Default.Key, contentDescription = null, tint = UrkundeGoldDark, modifier = Modifier.size(14.dp))
                      Spacer(modifier = Modifier.width(4.dp))
                      Text("Master-Phrasen Einsicht (Nur Creator: RFOF & Satoramy):", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = BlueprintNavy)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                      text = AuthManager.getMasterCreatorPhraseIfAuthorized(currentUser) ?: "••••••••••••••••••••••••••••••••••••••••••••••••",
                      fontFamily = FontFamily.Monospace,
                      fontSize = 9.sp,
                      color = TextSecondary,
                      modifier = Modifier
                        .background(Color.White, RoundedCornerShape(4.dp))
                        .padding(6.dp)
                    )
                  }
                }
              }
            }
          }
        } else if (activeTab == 1) {
          // 5 Parallel Providers List
          LazyColumn(
            modifier = Modifier
              .weight(1f)
              .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            AuthProviderType.values().forEach { providerType ->
              item(key = providerType.name) {
                val state = parallelProviders[providerType] ?: ParallelProviderState(provider = providerType)
                ProviderItemCard(
                  state = state,
                  currentMode = currentMode,
                  onToggle = { enable ->
                    if (enable) {
                      coroutineScope.launch {
                        val defaultId = when (providerType) {
                          AuthProviderType.FIREBASE -> "gen-lang-client-0256777474"
                          AuthProviderType.GITHUB -> "RFOF-NETWORK"
                          AuthProviderType.GOOGLE -> "Google OAuth Client (Privat)"
                          AuthProviderType.MICROSOFT -> "Microsoft Azure AD Client (Privat)"
                          AuthProviderType.W3CONNECT -> "0xRFOF9842A7b2F366c8B01C5D19E77F32e2A8321"
                        }
                        val res = AuthManager.authenticateProvider(providerType, defaultId, currentMode)
                        if (res.success) {
                          showSuccessMsg = "${providerType.displayName} verbunden (Mode: ${currentMode.label})"
                          showErrorMsg = null
                        } else {
                          showErrorMsg = res.message
                        }
                      }
                    } else {
                      AuthManager.disconnectProvider(providerType)
                      showSuccessMsg = "${providerType.displayName} getrennt (OFF)"
                      showErrorMsg = null
                    }
                  },
                  onMakePrimary = {
                    showSuccessMsg = "${providerType.displayName} ist als Provider aktiviert. Authentifizierung erfolgt über Passwort-Login."
                    showErrorMsg = null
                  }
                )
              }
            }
          }
        } else {
          // Tab 2: Schnell-Auswahl (SICHER: Kein Auto-Login, leitet zur Passworteingabe weiter)
          Column(
            modifier = Modifier
              .weight(1f)
              .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Surface(
              color = Color(0xFFFEF3C7),
              shape = RoundedCornerShape(8.dp),
              border = BorderStroke(1.dp, Color(0xFFFCD34D)),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF92400E), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Kein Auto-Login: Die Schnellauswahl überträgt die gewünschte Identität in das Anmeldeformular. Die Sitzung wird erst nach korrekter Passworteingabe erzeugt.",
                  fontSize = 10.sp,
                  color = Color(0xFF78350F),
                  fontWeight = FontWeight.Medium
                )
              }
            }

            // 1. RFOF-NETWORK GitHub OAuth (Master Admin)
            Button(
              onClick = {
                usernameInput = "RFOF-NETWORK"
                passwordInput = ""
                activeTab = 0
                isRegisterMode = false
                showSuccessMsg = "Identität 'RFOF-NETWORK' ausgewählt. Bitte Passwort oder Master-Phrasen eingeben."
                showErrorMsg = null
              },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("auth_rfof_network_button"),
              colors = ButtonDefaults.buttonColors(containerColor = BlueprintNavy),
              shape = RoundedCornerShape(8.dp)
            ) {
              Icon(Icons.Default.Security, contentDescription = null, tint = UrkundeGold, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("RFOF-NETWORK (Master-Admin / GitHub OAuth)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }

            // 2. SATORAMY (Dual-Admin / Creator & User)
            Button(
              onClick = {
                usernameInput = "Satoramy"
                passwordInput = ""
                activeTab = 0
                isRegisterMode = false
                showSuccessMsg = "Identität 'Satoramy' ausgewählt. Bitte Passwort eingeben."
                showErrorMsg = null
              },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("auth_satoramy_quick_button"),
              colors = ButtonDefaults.buttonColors(containerColor = UrkundeGold),
              shape = RoundedCornerShape(8.dp)
            ) {
              Icon(Icons.Default.Star, contentDescription = null, tint = BlueprintNavy, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Satoramy (Dual-Admin & Nutzer / Creator)", color = BlueprintNavy, fontWeight = FontWeight.Black, fontSize = 12.sp)
            }

            // 3. Google OAuth (User)
            OutlinedButton(
              onClick = {
                usernameInput = "Google-Erfinder"
                passwordInput = ""
                activeTab = 0
                isRegisterMode = false
                showSuccessMsg = "Identität 'Google-Erfinder' ausgewählt. Bitte Passwort eingeben."
                showErrorMsg = null
              },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("auth_google_button"),
              shape = RoundedCornerShape(8.dp)
            ) {
              Icon(Icons.Default.AccountCircle, contentDescription = null, tint = SignalBlue, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Mit Google Account anmelden (Passwort-Pflicht)", color = BlueprintNavy, fontSize = 12.sp)
            }

            // 4. Microsoft Azure AD
            OutlinedButton(
              onClick = {
                usernameInput = "Microsoft-Partner"
                passwordInput = ""
                activeTab = 0
                isRegisterMode = false
                showSuccessMsg = "Identität 'Microsoft-Partner' ausgewählt. Bitte Passwort eingeben."
                showErrorMsg = null
              },
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(8.dp)
            ) {
              Icon(Icons.Default.Window, contentDescription = null, tint = Color(0xFF00A4EF), modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Mit Microsoft Azure AD anmelden (Passwort-Pflicht)", color = BlueprintNavy, fontSize = 12.sp)
            }

            // 5. W3Connect / Wallet Connect (User)
            OutlinedButton(
              onClick = {
                usernameInput = "W3-Connect-User"
                passwordInput = ""
                activeTab = 0
                isRegisterMode = false
                showSuccessMsg = "Identität 'W3-Connect-User' ausgewählt. Bitte Passwort eingeben."
                showErrorMsg = null
              },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("auth_web3_button"),
              shape = RoundedCornerShape(8.dp)
            ) {
              Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = Color(0xFF627EEA), modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("W3Connect / Wallet verbinden (Passwort-Pflicht)", color = BlueprintNavy, fontSize = 12.sp)
            }

            // 6. Firebase Serverless Identity
            OutlinedButton(
              onClick = {
                usernameInput = "Firebase-Client"
                passwordInput = ""
                activeTab = 0
                isRegisterMode = false
                showSuccessMsg = "Identität 'Firebase-Client' ausgewählt. Bitte Passwort eingeben."
                showErrorMsg = null
              },
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(8.dp)
            ) {
              Icon(Icons.Default.Whatshot, contentDescription = null, tint = Color(0xFFFF9100), modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Firebase Cloud Identity verbinden (Passwort-Pflicht)", color = BlueprintNavy, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.weight(1f))

            // Logout / Gast
            TextButton(
              onClick = {
                AuthManager.logout()
                WalletRepository.refreshAssetsForCurrentRole()
                showSuccessMsg = "Abgemeldet. Gast-Modus aktiv."
                showErrorMsg = null
              },
              modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .testTag("auth_logout_button")
            ) {
              Icon(Icons.Default.ExitToApp, contentDescription = null, tint = TextMuted, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Abmelden (Gast-Modus)", color = TextMuted, fontSize = 11.sp)
            }
          }
        }
      }
    }
  }
}

@Composable
private fun ProviderItemCard(
  state: ParallelProviderState,
  currentMode: AuthExecutionMode,
  onToggle: (Boolean) -> Unit,
  onMakePrimary: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(10.dp),
    colors = CardDefaults.cardColors(containerColor = if (state.isEnabled) Color.White else Slate50),
    border = BorderStroke(
      1.dp,
      if (state.isEnabled) state.provider.brandColor.copy(alpha = 0.5f) else BlueprintBorder
    ),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(10.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(state.provider.iconEmoji, fontSize = 18.sp)
          Column {
            Text(
              text = state.provider.displayName,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = BlueprintNavy
            )
            Text(
              text = state.provider.protocol,
              fontSize = 9.sp,
              color = TextMuted
            )
          }
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Surface(
            color = if (state.isEnabled) SignalGreen.copy(alpha = 0.15f) else Slate200,
            shape = RoundedCornerShape(4.dp)
          ) {
            Text(
              text = if (state.isEnabled) "AKTIV (ON)" else "INAKTIV (OFF)",
              color = if (state.isEnabled) SignalGreen else TextMuted,
              fontWeight = FontWeight.Bold,
              fontSize = 9.sp,
              modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
            )
          }

          Switch(
            checked = state.isEnabled,
            onCheckedChange = { onToggle(it) },
            modifier = Modifier.height(28.dp)
          )
        }
      }

      if (state.isEnabled) {
        Spacer(modifier = Modifier.height(6.dp))

        Surface(
          color = Slate50,
          shape = RoundedCornerShape(6.dp),
          border = BorderStroke(1.dp, Slate200),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(6.dp)) {
            Text(
              text = "Account: ${state.accountIdentifier}",
              fontSize = 10.sp,
              fontWeight = FontWeight.SemiBold,
              color = BlueprintNavy
            )
            state.activeToken?.let { tok ->
              Text(
                text = "Token: ${tok.tokenValue.take(30)}...",
                fontFamily = FontFamily.Monospace,
                fontSize = 9.sp,
                color = TextSecondary
              )
              Text(
                text = "⚡ Double-Proxy: Ingress ✓ | Egress (${if (tok.egressServerlessBound) "Firestore Cloud" else "Local Parity"}) ✓",
                fontSize = 8.sp,
                color = SignalGreen,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          TextButton(
            onClick = onMakePrimary,
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
          ) {
            Text("Als Primär setzen", fontSize = 10.sp, color = SignalBlue, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
