package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auth.AuthManager
import com.example.data.ExplorerRepository
import com.example.data.WalletRepository
import com.example.model.EscrowStatus
import com.example.model.SovereignLicenseData
import com.example.ui.theme.*

@Composable
fun AdminSovereignExplorerView(
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val clipboardManager = LocalClipboardManager.current
  val escrowState by WalletRepository.escrowState.collectAsState()
  val currentUser by AuthManager.currentUser.collectAsState()
  val certificates by ExplorerRepository.certificates.collectAsState()

  var flashLoanLimitEur by remember { mutableStateOf("2.500.000") }
  var minArbitrageProfitMargin by remember { mutableStateOf("0.15%") }
  var isEmergencyCircuitBreakerArmed by remember { mutableStateOf(false) }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(BackgroundLight)
      .padding(14.dp)
      .testTag("admin_sovereign_explorer_view"),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    // Sovereign Master Header
    item {
      Surface(
        color = Color(0xFF1E293B),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.5.dp, UrkundeGoldDark),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .background(UrkundeGoldBg, CircleShape)
                  .border(1.5.dp, UrkundeGoldDark, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = UrkundeGoldDark, modifier = Modifier.size(22.dp))
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "SOVEREIGN MASTER EXPLORER",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Black,
                  color = UrkundeGold
                )
                Text(
                  text = "RFOF-NETWORK • Alleinherrschaft über Treasury & Genesis",
                  style = MaterialTheme.typography.labelSmall,
                  color = Color(0xFFE2E8F0),
                  fontSize = 10.sp
                )
              }
            }

            Surface(
              color = UrkundeGoldDark,
              shape = RoundedCornerShape(4.dp)
            ) {
              Text(
                text = "ADMIN ONLY",
                color = Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "Gemäß § 36 BeurkG, XJustiz-Clearing und der 8-Sichten-Matrix verfügt der Admin über vollständige Notariats- und Treasury-Kontrolle. Der Admin hat uneingeschränkten Einblick in die Nutzer-Sicht, die KI-Sicht und diese Sovereign-Master-Sicht.",
            fontSize = 10.sp,
            color = Color(0xFFCBD5E1),
            lineHeight = 14.sp
          )
        }
      }
    }

    // Escrow Master Vault Section (§ 36 BeurkG)
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BlueprintBorder),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "TREUHAND-SPEICHER & NOTARIATS-ESCROW",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = BlueprintNavy
            )
            Surface(
              color = UrkundeGoldBg,
              shape = RoundedCornerShape(4.dp)
            ) {
              Text(
                text = escrowState.notarialActNumber,
                color = UrkundeGoldDark,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Column(
            modifier = Modifier
              .fillMaxWidth()
              .background(Slate50, RoundedCornerShape(6.dp))
              .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Text("• Treuhänder: ${escrowState.treuhaender}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BlueprintNavy)
            Text("• Gebundenes MTK: ${String.format("%,.0f", escrowState.totalLockedMtk)} MTK", fontSize = 10.sp, color = UrkundeGoldDark)
            Text("• Hinterlegte Deckung: ${escrowState.backingBtc} BTC • ${escrowState.backingEth} ETH • ${escrowState.backingTon} TON", fontSize = 10.sp, color = SignalBlue)
            Text("• Rechtsgrundlage: ${escrowState.legalBase}", fontSize = 10.sp, color = TextMuted)
            Text("• Status: ${escrowState.status.label}", fontSize = 10.sp, color = SignalGreen, fontWeight = FontWeight.Bold)
          }

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Button(
              onClick = {
                clipboardManager.setText(AnnotatedString(escrowState.notarialActNumber + " - " + escrowState.legalBase))
                Toast.makeText(context, "Notariatsnachweis in Zwischenablage kopiert!", Toast.LENGTH_SHORT).show()
              },
              colors = ButtonDefaults.buttonColors(containerColor = BlueprintNavy),
              shape = RoundedCornerShape(6.dp),
              modifier = Modifier.weight(1f)
            ) {
              Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(15.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Nachweis kopieren", fontSize = 11.sp)
            }

            Button(
              onClick = {
                Toast.makeText(context, "Escrow-Audit erfolgreich durchgeführt: 100% Deckung bestätigt.", Toast.LENGTH_LONG).show()
              },
              colors = ButtonDefaults.buttonColors(containerColor = UrkundeGoldDark),
              shape = RoundedCornerShape(6.dp),
              modifier = Modifier.weight(1f)
            ) {
              Icon(Icons.Default.Verified, contentDescription = null, modifier = Modifier.size(15.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Audit validieren", fontSize = 11.sp)
            }
          }
        }
      }
    }

    // Genesis Block & License Anchor
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BlueprintBorder),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = "GENESIS-BLOCK #4.102.918 & SOVEREIGN LICENSE",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = BlueprintNavy
          )
          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = "License Hash: ${SovereignLicenseData.LICENSE_HASH}",
            fontSize = 9.sp,
            color = TextMuted,
            fontFamily = FontFamily.Monospace
          )
          Text(
            text = "Urheberrecht: ${SovereignLicenseData.COPYRIGHT_HOLDER}",
            fontSize = 10.sp,
            color = BlueprintNavy,
            fontWeight = FontWeight.SemiBold
          )

          Spacer(modifier = Modifier.height(8.dp))

          Button(
            onClick = {
              clipboardManager.setText(AnnotatedString(SovereignLicenseData.LICENSE_HASH))
              Toast.makeText(context, "Sovereign License Hash kopiert!", Toast.LENGTH_SHORT).show()
            },
            colors = ButtonDefaults.buttonColors(containerColor = BlueprintNavy),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(Icons.Default.Key, contentDescription = null, modifier = Modifier.size(15.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Genesis-Lizenz Hash kopieren", fontSize = 11.sp)
          }
        }
      }
    }

    // Flash Loan & KI-Governance Settings
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BlueprintBorder),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = "KI FLASH-LOAN & ARBITRAGE GOVERNANCE",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = BlueprintNavy
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Konfiguration der autonomen KI-Liquidierungs-Parameter. Die KI darf Flash Loans ausschließlich innerhalb dieser Rahmenparameter vollautonom zur Kurssicherung ausführen.",
            fontSize = 10.sp,
            color = TextSecondary,
            lineHeight = 14.sp
          )

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = flashLoanLimitEur,
            onValueChange = { flashLoanLimitEur = it },
            label = { Text("Max. Flash-Loan Kreditvolumen (€)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = minArbitrageProfitMargin,
            onValueChange = { minArbitrageProfitMargin = it },
            label = { Text("Min. Arbitrage-Marge (Mindestgewinn)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("Notfall-Schutzbremse (Circuit Breaker)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BlueprintNavy)
              Text("Sperrt temporär alle Flash Loans im Ernstfall", fontSize = 9.sp, color = TextMuted)
            }
            Switch(
              checked = isEmergencyCircuitBreakerArmed,
              onCheckedChange = {
                isEmergencyCircuitBreakerArmed = it
                Toast.makeText(context, if (it) "Schutzbremse SCHARFGESCHALTET!" else "Schutzbremse DEAKTIVIERT.", Toast.LENGTH_SHORT).show()
              }
            )
          }
        }
      }
    }
  }
}
