package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.auth.AuthManager
import com.example.auth.UserRole
import com.example.data.WalletRepository
import com.example.ui.theme.*

@Composable
fun AuthDialog(
  onDismiss: () -> Unit
) {
  val currentUser by AuthManager.currentUser.collectAsState()
  var customInput by remember { mutableStateOf("") }
  var showSuccessMsg by remember { mutableStateOf<String?>(null) }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.95f)
        .padding(16.dp)
        .testTag("auth_dialog"),
      shape = RoundedCornerShape(16.dp),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 8.dp
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        // Dialog Header
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
                text = "AUTH & ROLLEN-STATUS",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = BlueprintNavy
              )
              Text(
                text = "PRAI / MTK / NEC Identitätssystem",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                fontSize = 11.sp
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

        Spacer(modifier = Modifier.height(16.dp))

        // Current Active Identity Card
        val isAdmin = currentUser.role == UserRole.ADMIN
        val badgeColor = if (isAdmin) UrkundeGold else SignalBlue
        val badgeBg = if (isAdmin) UrkundeGoldBg else SignalBlueLight

        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = Slate50),
          border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BlueprintBorder))
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "AKTIVE SITZUNG",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = TextMuted
              )
              Surface(
                color = badgeBg,
                shape = RoundedCornerShape(4.dp)
              ) {
                Text(
                  text = currentUser.role.badge,
                  color = badgeColor,
                  fontWeight = FontWeight.Black,
                  fontSize = 10.sp,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = currentUser.username,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Black,
              color = BlueprintNavy
            )

            Text(
              text = "Provider: ${currentUser.authProvider}",
              style = MaterialTheme.typography.bodySmall,
              color = TextSecondary,
              fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Role Explanation
            Surface(
              color = Color.White,
              shape = RoundedCornerShape(6.dp),
              modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BlueprintBorder, RoundedCornerShape(6.dp))
            ) {
              Text(
                text = if (isAdmin) {
                  "👑 ADMIN-HOHEIT (RFOF-NETWORK): Vollzugriff auf Escrow-Treuhand, MTK-Sovereign-Reserve, BTC, ETH, TON und alle 8 NEC-Stammurkunden."
                } else {
                  "👤 NUTZER-STATUS: Berechtigt zur Nutzung als Erfinder/Partner/Kunde, Treugeber-Wallet mit BTC, ETH, TON & erlangten NEC-Zertifikaten (Kein ungedecktes MTK)."
                },
                style = MaterialTheme.typography.bodySmall,
                color = if (isAdmin) UrkundeGoldDark else TextSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp,
                modifier = Modifier.padding(10.dp)
              )
            }
          }
        }

        if (showSuccessMsg != null) {
          Spacer(modifier = Modifier.height(10.dp))
          Surface(
            color = SignalGreenLight,
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = showSuccessMsg ?: "",
              color = SignalGreen,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(8.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = "IDENTITÄT WECHSELN / ANMELDEN",
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Bold,
          color = BlueprintNavy
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 1. RFOF-NETWORK GitHub OAuth (Admin)
        Button(
          onClick = {
            AuthManager.loginAsRfofNetwork()
            WalletRepository.refreshAssetsForCurrentRole()
            showSuccessMsg = "Erfolgreich als Admin (RFOF-NETWORK) autorisiert!"
          },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("auth_rfof_network_button"),
          colors = ButtonDefaults.buttonColors(containerColor = BlueprintNavy),
          shape = RoundedCornerShape(8.dp)
        ) {
          Icon(Icons.Default.Security, contentDescription = null, tint = UrkundeGold, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("RFOF-NETWORK (GitHub Auth / Admin)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 2. Google OAuth (User)
        OutlinedButton(
          onClick = {
            AuthManager.loginWithGoogle("erfinder.partner@gmail.com", "Google Erfinder-Partner")
            WalletRepository.refreshAssetsForCurrentRole()
            showSuccessMsg = "Als Google-Nutzer angemeldet (Rolle: User)"
          },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("auth_google_button"),
          shape = RoundedCornerShape(8.dp)
        ) {
          Icon(Icons.Default.AccountCircle, contentDescription = null, tint = SignalBlue, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Mit Google Account anmelden (User)", color = BlueprintNavy, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 3. W3Connect / Wallet Connect (User)
        OutlinedButton(
          onClick = {
            AuthManager.loginWithWeb3("0x71C2B04E5F931aC2388C89284De15f458B43a890", "EVM / W3Connect")
            WalletRepository.refreshAssetsForCurrentRole()
            showSuccessMsg = "Web3 Wallet verbunden (Rolle: User)"
          },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("auth_web3_button"),
          shape = RoundedCornerShape(8.dp)
        ) {
          Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = Color(0xFF627EEA), modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("W3Connect / Wallet verbinden (User)", color = BlueprintNavy, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 4. Logout / Gast
        TextButton(
          onClick = {
            AuthManager.logout()
            WalletRepository.refreshAssetsForCurrentRole()
            showSuccessMsg = "Abgemeldet. Gast-Modus aktiv."
          },
          modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .testTag("auth_logout_button")
        ) {
          Text("Abmelden (Gast-Modus)", color = TextMuted, fontSize = 11.sp)
        }
      }
    }
  }
}
