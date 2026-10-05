package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.window.Dialog
import com.example.auth.AuthManager
import com.example.data.WalletRepository
import com.example.model.CryptoAsset
import com.example.model.CryptoChain
import com.example.model.SovereignLicenseData
import com.example.network.AlchemyMultiChainService
import com.example.network.GenesisSwapReceipt
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradingOverlayDialog(
  initialTab: Int = 0, // 0 = Swap, 1 = Genesis Swap, 2 = Einzahlen, 3 = Auszahlen
  onDismiss: () -> Unit
) {
  val context = LocalContext.current
  val assets by WalletRepository.assets.collectAsState()
  val currentUser by AuthManager.currentUser.collectAsState()
  val isAdmin = AuthManager.isAdmin
  val chainStatus by AlchemyMultiChainService.chainStatus.collectAsState()
  val isSyncingChains by AlchemyMultiChainService.isSyncing.collectAsState()

  val tabTitles = if (isAdmin) {
    listOf("Swap / Tausch", "Genesis Swap", "Einzahlen", "Auszahlen")
  } else {
    listOf("Swap / Tausch", "Einzahlen", "Auszahlen")
  }

  var activeTab by remember { mutableStateOf(if (initialTab < tabTitles.size) initialTab else 0) }

  // Default Swap selections
  var fromAssetIndex by remember { mutableStateOf(0) }
  var toAssetIndex by remember { mutableStateOf(if (assets.size > 1) 1 else 0) }
  var fromAmountText by remember { mutableStateOf("0.5") }
  var slippageTolerance by remember { mutableStateOf("0.5%") }

  // Genesis Swap selections (MTK <-> Multi-Chain)
  var genesisFromIndex by remember { mutableStateOf(0) }
  var genesisToIndex by remember { mutableStateOf(if (assets.size > 1) 1 else 0) }
  var genesisAmountText by remember { mutableStateOf("100.0") }
  var lastGenesisReceipt by remember { mutableStateOf<GenesisSwapReceipt?>(null) }

  // Deposit / Withdraw selections
  var targetAssetIndex by remember { mutableStateOf(0) }
  var transferAmountText by remember { mutableStateOf("100") }
  var recipientAddressText by remember { mutableStateOf("") }

  val fromAsset = assets.getOrNull(fromAssetIndex) ?: assets.firstOrNull()
  val toAsset = assets.getOrNull(toAssetIndex) ?: assets.getOrNull(1) ?: fromAsset
  val targetAsset = assets.getOrNull(targetAssetIndex) ?: assets.firstOrNull()

  val genesisFromAsset = assets.getOrNull(genesisFromIndex) ?: assets.firstOrNull()
  val genesisToAsset = assets.getOrNull(genesisToIndex) ?: assets.getOrNull(1) ?: genesisFromAsset

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = Color.White,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 16.dp)
        .testTag("trading_overlay_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .heightIn(max = 660.dp)
          .padding(16.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
              modifier = Modifier
                .size(34.dp)
                .background(BlueprintNavy, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = UrkundeGold, modifier = Modifier.size(20.dp))
            }
            Column {
              Text(
                text = "TRADING & ASSET HUB",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = BlueprintNavy
              )
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(6.dp)
                    .background(if (chainStatus.alchemyEthConnected) SignalGreen else Color(0xFFEAB308), CircleShape)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = if (isAdmin) "Admin Modus • Alchemy ETH #${chainStatus.ethBlockNumber}" else "Nutzer Modus • Multi-Chain Live",
                  style = MaterialTheme.typography.labelSmall,
                  color = if (isAdmin) UrkundeGoldDark else TextMuted,
                  fontSize = 10.sp
                )
              }
            }
          }

          IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
            Icon(Icons.Default.Close, contentDescription = "Schließen")
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tab Selector
        ScrollableTabRow(
          selectedTabIndex = activeTab,
          containerColor = Slate100,
          contentColor = BlueprintNavy,
          edgePadding = 0.dp,
          modifier = Modifier.background(Slate100, RoundedCornerShape(8.dp))
        ) {
          tabTitles.forEachIndexed { idx, title ->
            Tab(
              selected = activeTab == idx,
              onClick = { activeTab = idx },
              text = {
                Text(
                  text = title,
                  fontSize = 11.sp,
                  fontWeight = if (activeTab == idx) FontWeight.Bold else FontWeight.Normal,
                  maxLines = 1,
                  softWrap = false
                )
              }
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (fromAsset == null || toAsset == null || targetAsset == null) {
          Text("Keine Assets verfügbar", color = TextMuted)
          return@Column
        }

        val tabLabel = tabTitles.getOrElse(activeTab) { "Swap / Tausch" }

        when (tabLabel) {
          "Swap / Tausch" -> {
            // SWAP SECTION
            val fromRate = fromAsset.usdRate
            val toRate = toAsset.usdRate
            val fromVal = fromAmountText.toDoubleOrNull() ?: 0.0
            val estimatedTo = if (toRate > 0) (fromVal * fromRate) / toRate else 0.0

            Card(
              colors = CardDefaults.cardColors(containerColor = Slate50),
              border = BorderStroke(1.dp, BlueprintBorder),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text("Sie zahlen:", fontSize = 11.sp, color = TextMuted)
                  Text("Guthaben: ${String.format("%.4f", fromAsset.balance)} ${fromAsset.chain.symbol}", fontSize = 10.sp, color = BlueprintNavy)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  OutlinedTextField(
                    value = fromAmountText,
                    onValueChange = { fromAmountText = it },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                  )
                  Spacer(modifier = Modifier.width(8.dp))

                  Surface(
                    color = fromAsset.chain.color.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.clickable {
                      fromAssetIndex = (fromAssetIndex + 1) % assets.size
                    }
                  ) {
                    Text(
                      text = "${fromAsset.chain.symbol} ▾",
                      color = fromAsset.chain.color,
                      fontWeight = FontWeight.Bold,
                      fontSize = 12.sp,
                      modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp)
                    )
                  }
                }
              }
            }

            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
              contentAlignment = Alignment.Center
            ) {
              IconButton(
                onClick = {
                  val temp = fromAssetIndex
                  fromAssetIndex = toAssetIndex
                  toAssetIndex = temp
                },
                modifier = Modifier
                  .size(32.dp)
                  .background(Color.White, CircleShape)
                  .border(1.dp, BlueprintBorder, CircleShape)
              ) {
                Icon(Icons.Default.SwapVert, contentDescription = "Umkehren", tint = SignalBlue, modifier = Modifier.size(18.dp))
              }
            }

            Card(
              colors = CardDefaults.cardColors(containerColor = Slate50),
              border = BorderStroke(1.dp, BlueprintBorder),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text("Sie erhalten (geschätzt):", fontSize = 11.sp, color = TextMuted)
                  Text("Kurs: 1 ${fromAsset.chain.symbol} ≈ ${String.format("%.4f", if (toRate > 0) fromRate / toRate else 0.0)} ${toAsset.chain.symbol}", fontSize = 10.sp, color = TextMuted)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = String.format("%.6f", estimatedTo),
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = BlueprintNavy
                  )

                  Surface(
                    color = toAsset.chain.color.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.clickable {
                      toAssetIndex = (toAssetIndex + 1) % assets.size
                    }
                  ) {
                    Text(
                      text = "${toAsset.chain.symbol} ▾",
                      color = toAsset.chain.color,
                      fontWeight = FontWeight.Bold,
                      fontSize = 12.sp,
                      modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp)
                    )
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text("Slippage-Toleranz:", fontSize = 10.sp, color = TextMuted)
              Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                listOf("0.1%", "0.5%", "1.0%").forEach { slip ->
                  FilterChip(
                    selected = slippageTolerance == slip,
                    onClick = { slippageTolerance = slip },
                    label = { Text(slip, fontSize = 9.sp) }
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
              onClick = {
                val result = WalletRepository.swap(fromAsset.chain, toAsset.chain, fromVal)
                if (result != null) {
                  Toast.makeText(context, "Swap ausgeführt: $fromVal ${fromAsset.chain.symbol} ➔ ${String.format("%.4f", result)} ${toAsset.chain.symbol}", Toast.LENGTH_LONG).show()
                  onDismiss()
                } else {
                  Toast.makeText(context, "Swap fehlgeschlagen! Bitte Guthaben prüfen.", Toast.LENGTH_SHORT).show()
                }
              },
              colors = ButtonDefaults.buttonColors(containerColor = SignalBlue),
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(8.dp)
            ) {
              Text("Sofort-Tausch bestätigen")
            }
          }

          "Genesis Swap" -> {
            // GENESIS SWAP SECTION (NO PROCESS BYPASSED)
            if (genesisFromAsset == null || genesisToAsset == null) return@Column

            val fromVal = genesisAmountText.toDoubleOrNull() ?: 0.0
            val fromRate = genesisFromAsset.usdRate
            val toRate = genesisToAsset.usdRate
            val estimatedTo = if (toRate > 0) (fromVal * fromRate) / toRate else 0.0

            Card(
              colors = CardDefaults.cardColors(containerColor = UrkundeGoldBg),
              border = BorderStroke(1.dp, UrkundeGold),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.Verified, contentDescription = null, tint = UrkundeGoldDark, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "GENESIS SWAP (ALCHEMY ON-CHAIN GEPRÜFT)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = BlueprintNavy
                  )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "Prozess-Souveränität: Kein Schritt wird umgangen. Strikte Prüfung über Alchemy Ethereum Mainnet (Block #${chainStatus.ethBlockNumber}, Gas ${String.format("%.1f", chainStatus.ethGasPriceGwei)} Gwei), Notariats-Siegel nach § 36 BeurkG und XJustiz-Dual-Parität.",
                  fontSize = 10.sp,
                  color = TextSecondary,
                  lineHeight = 14.sp
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Multi-Chain Node Live Stats Bar
            Card(
              colors = CardDefaults.cardColors(containerColor = Slate50),
              border = BorderStroke(1.dp, BlueprintBorder),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(10.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text("Alchemy ETH Node:", fontSize = 10.sp, color = TextMuted)
                  Text("Block #${chainStatus.ethBlockNumber} (${String.format("%.1f", chainStatus.ethGasPriceGwei)} Gwei)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BlueprintNavy)
                }
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text("Parallel BTC / TON:", fontSize = 10.sp, color = TextMuted)
                  Text("BTC #${chainStatus.btcBlockHeight} • TON #${chainStatus.tonMasterSeqno}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BlueprintNavy)
                }
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text("MTK Ledger & Lizenz:", fontSize = 10.sp, color = TextMuted)
                  Text("Ledger #${chainStatus.mtkLedgerHeight} • Hash: ${SovereignLicenseData.LICENSE_HASH.take(10)}...", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = UrkundeGoldDark)
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Input fields
            OutlinedTextField(
              value = genesisAmountText,
              onValueChange = { genesisAmountText = it },
              label = { Text("Genesis Betrag (${genesisFromAsset.chain.symbol})") },
              modifier = Modifier.fillMaxWidth(),
              singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text("Tauschpaar:", fontSize = 11.sp, color = TextMuted)
              Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Surface(
                  color = genesisFromAsset.chain.color.copy(alpha = 0.15f),
                  shape = RoundedCornerShape(6.dp),
                  modifier = Modifier.clickable {
                    genesisFromIndex = (genesisFromIndex + 1) % assets.size
                  }
                ) {
                  Text("${genesisFromAsset.chain.symbol} ▾", modifier = Modifier.padding(6.dp), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = genesisFromAsset.chain.color)
                }
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp).align(Alignment.CenterVertically))
                Surface(
                  color = genesisToAsset.chain.color.copy(alpha = 0.15f),
                  shape = RoundedCornerShape(6.dp),
                  modifier = Modifier.clickable {
                    genesisToIndex = (genesisToIndex + 1) % assets.size
                  }
                ) {
                  Text("${genesisToAsset.chain.symbol} ▾", modifier = Modifier.padding(6.dp), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = genesisToAsset.chain.color)
                }
              }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = "Ergebnis: ${String.format("%.4f", estimatedTo)} ${genesisToAsset.chain.symbol} (Guthaben: ${String.format("%.2f", genesisFromAsset.balance)})",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = BlueprintNavy
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
              onClick = {
                val receipt = WalletRepository.executeGenesisSwap(genesisFromAsset.chain, genesisToAsset.chain, fromVal)
                if (receipt != null) {
                  lastGenesisReceipt = receipt
                  Toast.makeText(context, "Genesis Swap #${receipt.swapId} validiert & ausgeführt!", Toast.LENGTH_LONG).show()
                } else {
                  Toast.makeText(context, "Fehlgeschlagen: Unzureichende Reserven.", Toast.LENGTH_SHORT).show()
                }
              },
              colors = ButtonDefaults.buttonColors(containerColor = UrkundeGoldDark),
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(8.dp)
            ) {
              Text("Genesis Swap protokollieren & ausführen", fontWeight = FontWeight.Bold)
            }

            // Receipt display if present
            lastGenesisReceipt?.let { rec ->
              Spacer(modifier = Modifier.height(10.dp))
              Card(
                colors = CardDefaults.cardColors(containerColor = Slate100),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(10.dp)) {
                  Text("GENESIS PROTOKOLL-QUITTUNG:", fontSize = 10.sp, fontWeight = FontWeight.Black, color = SignalGreen)
                  Text("ID: ${rec.swapId} | Alchemy ETH Block #${rec.ethBlockHeight}", fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                  Text("TX: ${rec.txHashSha256.take(30)}...", fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                  Text("Notarielles Siegel: ${rec.dualParityNotarialSeal}", fontSize = 9.sp, fontFamily = FontFamily.Monospace, color = UrkundeWax)
                  Text("Status: ${rec.executionStatus}", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = SignalGreen)
                }
              }
            }
          }

          "Einzahlen" -> {
            // DEPOSIT SECTION
            Text("Asset zur Einzahlung auswählen:", fontSize = 11.sp, color = TextMuted)
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              assets.forEachIndexed { idx, asset ->
                FilterChip(
                  selected = targetAssetIndex == idx,
                  onClick = { targetAssetIndex = idx },
                  label = { Text(asset.chain.symbol, fontSize = 10.sp) }
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
              value = transferAmountText,
              onValueChange = { transferAmountText = it },
              label = { Text("Betrag in ${targetAsset.chain.symbol}") },
              modifier = Modifier.fillMaxWidth(),
              singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
              color = Slate50,
              shape = RoundedCornerShape(6.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(10.dp)) {
                Text("Einzahlungsadresse (${targetAsset.chain.fullName}):", fontSize = 10.sp, color = TextMuted)
                Text(currentUser.walletAddress, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BlueprintNavy)
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
              onClick = {
                val amt = transferAmountText.toDoubleOrNull() ?: 0.0
                WalletRepository.deposit(targetAsset.chain, amt)
                Toast.makeText(context, "$amt ${targetAsset.chain.symbol} erfolgreich eingezahlt!", Toast.LENGTH_SHORT).show()
                onDismiss()
              },
              colors = ButtonDefaults.buttonColors(containerColor = SignalGreen),
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(8.dp)
            ) {
              Text("Einzahlung bestätigen")
            }
          }

          "Auszahlen" -> {
            // WITHDRAW SECTION
            Text("Asset zur Auszahlung auswählen:", fontSize = 11.sp, color = TextMuted)
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              assets.forEachIndexed { idx, asset ->
                FilterChip(
                  selected = targetAssetIndex == idx,
                  onClick = { targetAssetIndex = idx },
                  label = { Text(asset.chain.symbol, fontSize = 10.sp) }
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
              value = transferAmountText,
              onValueChange = { transferAmountText = it },
              label = { Text("Auszahlungsbetrag (${targetAsset.chain.symbol})") },
              modifier = Modifier.fillMaxWidth(),
              singleLine = true
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
              value = recipientAddressText,
              onValueChange = { recipientAddressText = it },
              label = { Text("Zieladresse (${targetAsset.chain.symbol})") },
              modifier = Modifier.fillMaxWidth(),
              singleLine = true
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
              onClick = {
                val amt = transferAmountText.toDoubleOrNull() ?: 0.0
                val targetAddr = if (recipientAddressText.isNotBlank()) recipientAddressText else "0xRecv...984"
                val success = WalletRepository.withdraw(targetAsset.chain, targetAddr, amt)
                if (success) {
                  Toast.makeText(context, "$amt ${targetAsset.chain.symbol} an $targetAddr ausgezahlt!", Toast.LENGTH_SHORT).show()
                  onDismiss()
                } else {
                  Toast.makeText(context, "Unzureichendes Guthaben!", Toast.LENGTH_SHORT).show()
                }
              },
              colors = ButtonDefaults.buttonColors(containerColor = UrkundeWax),
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(8.dp)
            ) {
              Text("Auszahlung beauftragen")
            }
          }
        }
      }
    }
  }
}
