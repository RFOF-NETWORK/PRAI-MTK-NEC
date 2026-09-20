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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.auth.AuthManager
import com.example.auth.UserRole
import com.example.crypto.AESEncryption
import com.example.data.WalletRepository
import com.example.model.*
import com.example.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
  onNavigateToExplorer: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val clipboardManager = LocalClipboardManager.current
  val currentUser by AuthManager.currentUser.collectAsState()
  val assets by WalletRepository.assets.collectAsState()
  val escrowState by WalletRepository.escrowState.collectAsState()
  val stakingStates by WalletRepository.stakingStates.collectAsState()
  val miningState by WalletRepository.miningState.collectAsState()
  val transactions by WalletRepository.transactions.collectAsState()

  var selectedTab by remember { mutableStateOf(0) }
  val tabs = listOf("Vermögen & Assets", "Treuhand & Escrow", "Mining & Staking", "AES-Schlüssel")

  // Modal dialog states
  var showDepositDialog by remember { mutableStateOf(false) }
  var showWithdrawDialog by remember { mutableStateOf(false) }
  var showSwapDialog by remember { mutableStateOf(false) }
  var activeChainForAction by remember { mutableStateOf(CryptoChain.ETH) }

  val totalUsdValue = remember(assets) {
    assets.sumOf { it.usdValue }
  }

  val currencyFormat = remember {
    NumberFormat.getCurrencyInstance(Locale.GERMANY)
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(BackgroundLight)
      .testTag("wallet_screen")
  ) {
    // Top Wallet Hero Card
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
          Column {
            Text(
              text = if (currentUser.role == UserRole.ADMIN) "ESCROW & MASTER TREUHAND WALLET" else "TREUGEBER MULTI-CHAIN WALLET",
              style = MaterialTheme.typography.labelSmall,
              color = if (currentUser.role == UserRole.ADMIN) UrkundeGold else Color(0xFF94A3B8),
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp
            )
            Text(
              text = currentUser.username,
              style = MaterialTheme.typography.titleMedium,
              color = Color.White,
              fontWeight = FontWeight.Black
            )
          }

          Surface(
            color = if (currentUser.role == UserRole.ADMIN) UrkundeGoldBg else SignalBlueLight,
            shape = RoundedCornerShape(4.dp)
          ) {
            Text(
              text = if (currentUser.role == UserRole.ADMIN) "ADMIN: RFOF-NETWORK" else "NUTZER (USER)",
              color = if (currentUser.role == UserRole.ADMIN) UrkundeGoldDark else SignalBlue,
              fontWeight = FontWeight.Bold,
              fontSize = 10.sp,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = currencyFormat.format(totalUsdValue),
          style = MaterialTheme.typography.headlineMedium,
          color = Color.White,
          fontWeight = FontWeight.Black
        )

        Text(
          text = "Gesamtportfolio • W3Connect & Multi-Chain (MTK, BTC, ETH, TON)",
          style = MaterialTheme.typography.bodySmall,
          color = Color(0xFFCBD5E1),
          fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Quick Action Buttons (Deposit, Withdraw, Swap, Explorer)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Button(
            onClick = {
              activeChainForAction = CryptoChain.ETH
              showDepositDialog = true
            },
            modifier = Modifier
              .weight(1f)
              .testTag("wallet_deposit_button"),
            colors = ButtonDefaults.buttonColors(containerColor = SignalBlue),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
          ) {
            Icon(Icons.Default.ArrowDownward, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Einzahlen", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }

          Button(
            onClick = {
              activeChainForAction = CryptoChain.ETH
              showWithdrawDialog = true
            },
            modifier = Modifier
              .weight(1f)
              .testTag("wallet_withdraw_button"),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
          ) {
            Icon(Icons.Default.ArrowUpward, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Auszahlen", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }

          Button(
            onClick = { showSwapDialog = true },
            modifier = Modifier
              .weight(1f)
              .testTag("wallet_swap_button"),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF475569)),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
          ) {
            Icon(Icons.Default.SwapHoriz, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Tauschen", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }

          IconButton(
            onClick = onNavigateToExplorer,
            modifier = Modifier
              .background(Color(0xFF1E293B), RoundedCornerShape(8.dp))
              .size(36.dp)
              .testTag("wallet_go_explorer_button")
          ) {
            Icon(Icons.Default.Explore, contentDescription = "Explorer", tint = UrkundeGold, modifier = Modifier.size(20.dp))
          }
        }
      }
    }

    // Tab Navigation Bar
    ScrollableTabRow(
      selectedTabIndex = selectedTab,
      containerColor = Color.White,
      contentColor = BlueprintNavy,
      edgePadding = 12.dp,
      modifier = Modifier.fillMaxWidth()
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

    // Main Tab Content
    when (selectedTab) {
      0 -> AssetsTabContent(
        assets = assets,
        isAdmin = currentUser.role == UserRole.ADMIN,
        onSelectAsset = { asset ->
          activeChainForAction = asset.chain
          showDepositDialog = true
        },
        transactions = transactions
      )
      1 -> EscrowTabContent(
        escrow = escrowState,
        isAdmin = currentUser.role == UserRole.ADMIN,
        onLockMore = {
          Toast.makeText(context, "Treuhand-Hinterlegung notariell verifiziert (§ 36 BeurkG)", Toast.LENGTH_SHORT).show()
        }
      )
      2 -> MiningStakingTabContent(
        miningState = miningState,
        stakingStates = stakingStates,
        onToggleMining = { WalletRepository.toggleMining() },
        onStake = { chain, amount ->
          if (WalletRepository.stake(chain, amount)) {
            Toast.makeText(context, "$amount ${chain.symbol} erfolgreich gestaked!", Toast.LENGTH_SHORT).show()
          } else {
            Toast.makeText(context, "Ungenügendes Guthaben!", Toast.LENGTH_SHORT).show()
          }
        }
      )
      3 -> KeyManagementTabContent(
        seedPhrase = WalletRepository.getSeedPhrase(),
        onCopy = {
          clipboardManager.setText(AnnotatedString(it))
          Toast.makeText(context, "Seed-Phrase kopiert", Toast.LENGTH_SHORT).show()
        }
      )
    }
  }

  // --- MODALS ---

  // Deposit Dialog
  if (showDepositDialog) {
    DepositDialog(
      initialChain = activeChainForAction,
      onDismiss = { showDepositDialog = false },
      onDepositSimulate = { chain, amount ->
        WalletRepository.deposit(chain, amount, "Deposit über Wallet UI")
        showDepositDialog = false
        Toast.makeText(context, "$amount ${chain.symbol} eingezahlt!", Toast.LENGTH_SHORT).show()
      }
    )
  }

  // Withdraw Dialog
  if (showWithdrawDialog) {
    WithdrawDialog(
      initialChain = activeChainForAction,
      assets = assets,
      onDismiss = { showWithdrawDialog = false },
      onWithdraw = { chain, addr, amount ->
        val success = WalletRepository.withdraw(chain, addr, amount)
        showWithdrawDialog = false
        if (success) {
          Toast.makeText(context, "$amount ${chain.symbol} ausgezahlt an $addr", Toast.LENGTH_SHORT).show()
        } else {
          Toast.makeText(context, "Guthaben unzureichend!", Toast.LENGTH_SHORT).show()
        }
      }
    )
  }

  // Swap Dialog
  if (showSwapDialog) {
    SwapDialog(
      assets = assets,
      onDismiss = { showSwapDialog = false },
      onSwap = { from, to, amount ->
        val result = WalletRepository.swap(from, to, amount)
        showSwapDialog = false
        if (result != null) {
          Toast.makeText(context, "Erfolgreich getauscht: $amount ${from.symbol} -> $result ${to.symbol}", Toast.LENGTH_SHORT).show()
        } else {
          Toast.makeText(context, "Tausch fehlgeschlagen (Guthaben prüfen)!", Toast.LENGTH_SHORT).show()
        }
      }
    )
  }
}

// -------------------------------------------------------------
// ASSETS TAB
// -------------------------------------------------------------
@Composable
private fun AssetsTabContent(
  assets: List<CryptoAsset>,
  isAdmin: Boolean,
  onSelectAsset: (CryptoAsset) -> Unit,
  transactions: List<WalletTransaction>
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "UNTERSTÜTZTE KRYPTOWÄHRUNGEN",
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Bold,
          color = BlueprintNavy
        )
        Text(
          text = if (isAdmin) "Vollständige Sovereign-Sicht" else "Nutzer-Sicht (BTC, ETH, TON)",
          style = MaterialTheme.typography.labelSmall,
          color = TextMuted,
          fontSize = 10.sp
        )
      }
    }

    items(assets) { asset ->
      AssetCard(asset = asset, onClick = { onSelectAsset(asset) })
    }

    if (!isAdmin) {
      item {
        Card(
          colors = CardDefaults.cardColors(containerColor = Slate50),
          border = BorderStroke(1.dp, BlueprintBorder),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Icon(Icons.Default.Info, contentDescription = null, tint = UrkundeGold)
            Text(
              text = "Hinweis zur Rechtskonformität: Native MTK wird ausschließlich vom Admin (RFOF-NETWORK) im Treuhand-Speicher gehalten. Nutzer halten BTC, ETH, TON und beglaubigte NEC-Urkunden. Dadurch bleibt das Modell paragrafengetreu abgesichert.",
              style = MaterialTheme.typography.bodySmall,
              color = TextSecondary,
              fontSize = 11.sp,
              lineHeight = 15.sp
            )
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(10.dp))
      Text(
        text = "LETZTE TRANSAKTIONEN (LEDGER)",
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = BlueprintNavy
      )
    }

    items(transactions.take(5)) { tx ->
      TransactionRow(tx = tx)
    }
  }
}

@Composable
private fun AssetCard(
  asset: CryptoAsset,
  onClick: () -> Unit
) {
  val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.GERMANY) }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .testTag("asset_card_${asset.chain.symbol}"),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, BlueprintBorder),
    shape = RoundedCornerShape(10.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Box(
          modifier = Modifier
            .size(40.dp)
            .background(asset.chain.color.copy(alpha = 0.15f), CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = asset.chain.symbol.take(3),
            color = asset.chain.color,
            fontWeight = FontWeight.Black,
            fontSize = 12.sp
          )
        }

        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = asset.chain.fullName,
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = BlueprintNavy
            )
            if (asset.isRestrictedToAdmin) {
              Spacer(modifier = Modifier.width(6.dp))
              Surface(
                color = UrkundeGoldBg,
                shape = RoundedCornerShape(4.dp)
              ) {
                Text(
                  text = "ADMIN-RESERVE",
                  color = UrkundeGoldDark,
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                )
              }
            }
          }
          Text(
            text = "${currencyFormat.format(asset.usdRate)} / ${asset.chain.symbol}",
            style = MaterialTheme.typography.bodySmall,
            color = TextMuted,
            fontSize = 11.sp
          )
        }
      }

      Column(horizontalAlignment = Alignment.End) {
        Text(
          text = "${String.format(Locale.US, "%.4f", asset.balance)} ${asset.chain.symbol}",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Black,
          color = BlueprintNavy
        )
        Text(
          text = currencyFormat.format(asset.usdValue),
          style = MaterialTheme.typography.bodySmall,
          color = TextSecondary,
          fontSize = 11.sp
        )
      }
    }
  }
}

@Composable
private fun TransactionRow(tx: WalletTransaction) {
  val typeColor = when (tx.type) {
    TxType.DEPOSIT -> SignalGreen
    TxType.WITHDRAW -> FeedbackRed
    TxType.SWAP -> SignalBlue
    TxType.STAKE -> Color(0xFF8B5CF6)
    TxType.ESCROW_LOCK -> UrkundeGold
    TxType.CERTIFICATE_MINT -> UrkundeWax
  }

  Surface(
    color = Color.White,
    shape = RoundedCornerShape(8.dp),
    border = BorderStroke(0.5.dp, BlueprintBorder),
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.weight(1f)
      ) {
        Box(
          modifier = Modifier
            .size(30.dp)
            .background(typeColor.copy(alpha = 0.12f), CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = when (tx.type) {
              TxType.DEPOSIT -> Icons.Default.ArrowDownward
              TxType.WITHDRAW -> Icons.Default.ArrowUpward
              TxType.SWAP -> Icons.Default.SwapHoriz
              TxType.STAKE -> Icons.Default.AccountBalance
              TxType.ESCROW_LOCK -> Icons.Default.Lock
              TxType.CERTIFICATE_MINT -> Icons.Default.WorkspacePremium
            },
            contentDescription = null,
            tint = typeColor,
            modifier = Modifier.size(16.dp)
          )
        }

        Column {
          Text(
            text = tx.type.label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = BlueprintNavy
          )
          Text(
            text = tx.note.ifBlank { tx.txHash.take(16) + "..." },
            style = MaterialTheme.typography.bodySmall,
            color = TextMuted,
            fontSize = 10.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
      }

      Column(horizontalAlignment = Alignment.End) {
        Text(
          text = "${if (tx.type == TxType.DEPOSIT) "+" else "-"}${tx.amount} ${tx.chain.symbol}",
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Bold,
          color = typeColor
        )
        Text(
          text = "Status: Bestätigt",
          style = MaterialTheme.typography.labelSmall,
          color = SignalGreen,
          fontSize = 9.sp
        )
      }
    }
  }
}

// -------------------------------------------------------------
// TREUHAND & ESCROW TAB
// -------------------------------------------------------------
@Composable
private fun EscrowTabContent(
  escrow: EscrowContractState,
  isAdmin: Boolean,
  onLockMore: () -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = Slate50),
        border = BorderStroke(1.dp, BlueprintBorder),
        shape = RoundedCornerShape(10.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "UNIFIZIERTE TREUHAND-ARCHITEKTUR",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = BlueprintNavy
            )
            Surface(
              color = UrkundeGoldBg,
              shape = RoundedCornerShape(4.dp)
            ) {
              Text(
                text = escrow.status.label,
                color = UrkundeGoldDark,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "Notar-Vertrag: ${escrow.contractId} (${escrow.notarialActNumber})",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = BlueprintNavy
          )
          Text(
            text = "Rechtsgrundlage: ${escrow.legalBase}",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            fontSize = 11.sp
          )
          Text(
            text = "Treuhänder: ${escrow.treuhaender}",
            style = MaterialTheme.typography.bodySmall,
            color = SignalBlue,
            fontSize = 11.sp
          )
        }
      }
    }

    item {
      Text(
        text = "HINTERLEGTE DECKUNGS-RESERVEN (100% COLLATERAL)",
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = BlueprintNavy
      )
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        ReserveCard(title = "Gesperrte MTK", value = "10.000.000 MTK", subtitle = "Sovereign Vault", modifier = Modifier.weight(1f))
        ReserveCard(title = "Gedecktes BTC", value = "${escrow.backingBtc} BTC", subtitle = "Cold Storage", modifier = Modifier.weight(1f))
      }
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        ReserveCard(title = "Gedecktes ETH", value = "${escrow.backingEth} ETH", subtitle = "Staking Escrow", modifier = Modifier.weight(1f))
        ReserveCard(title = "Gedecktes TON", value = "${escrow.backingTon} TON", subtitle = "Nominator Pool", modifier = Modifier.weight(1f))
      }
    }

    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BlueprintBorder),
        shape = RoundedCornerShape(10.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = "Treuhand vs. Treugeber Rechtsverhältnis",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = BlueprintNavy
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Die Wallet integriert Escrow- und Treugeber-Funktionen in einer einzigen kohärenten Architektur: Der Admin (RFOF-NETWORK) fungiert als Treuhänder der Gesamtreserven, während alle übrigen Konten als Treugeber agieren. Alle Auszahlungen und Zertifikats-Ausgaben werden kryptographisch und notariell gegengezeichnet.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            fontSize = 11.sp,
            lineHeight = 16.sp
          )

          if (isAdmin) {
            Spacer(modifier = Modifier.height(12.dp))
            Button(
              onClick = onLockMore,
              colors = ButtonDefaults.buttonColors(containerColor = BlueprintNavy),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Icon(Icons.Default.Lock, contentDescription = null, tint = UrkundeGold, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Weitere Deckung im Notariats-Tresor hinterlegen", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}

@Composable
private fun ReserveCard(
  title: String,
  value: String,
  subtitle: String,
  modifier: Modifier = Modifier
) {
  Card(
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, BlueprintBorder),
    shape = RoundedCornerShape(8.dp),
    modifier = modifier
  ) {
    Column(modifier = Modifier.padding(10.dp)) {
      Text(text = title, fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Medium)
      Spacer(modifier = Modifier.height(4.dp))
      Text(text = value, fontSize = 13.sp, color = BlueprintNavy, fontWeight = FontWeight.Black)
      Text(text = subtitle, fontSize = 9.sp, color = SignalBlue)
    }
  }
}

// -------------------------------------------------------------
// MINING & STAKING TAB
// -------------------------------------------------------------
@Composable
private fun MiningStakingTabContent(
  miningState: MiningState,
  stakingStates: Map<CryptoChain, StakingState>,
  onToggleMining: () -> Unit,
  onStake: (CryptoChain, Double) -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      // BTC Mining Section
      Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BlueprintBorder),
        shape = RoundedCornerShape(10.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
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
                  .size(32.dp)
                  .background(CryptoChain.BTC.color.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.Bolt, contentDescription = null, tint = CryptoChain.BTC.color, modifier = Modifier.size(18.dp))
              }
              Column {
                Text(
                  text = "BITCOIN MINING CLUSTER",
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold,
                  color = BlueprintNavy
                )
                Text(
                  text = miningState.poolName,
                  style = MaterialTheme.typography.bodySmall,
                  color = TextMuted,
                  fontSize = 10.sp
                )
              }
            }

            Switch(
              checked = miningState.isActive,
              onCheckedChange = { onToggleMining() }
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text("Hashrate", fontSize = 10.sp, color = TextMuted)
              Text("${miningState.hashrateTh} TH/s", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BlueprintNavy)
            }
            Column {
              Text("Aktive Worker", fontSize = 10.sp, color = TextMuted)
              Text("${miningState.workersCount} Einheiten", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BlueprintNavy)
            }
            Column(horizontalAlignment = Alignment.End) {
              Text("Täglicher Ertrag", fontSize = 10.sp, color = TextMuted)
              Text("${miningState.dailyRewardBtc} BTC", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SignalGreen)
            }
          }
        }
      }
    }

    item {
      Text(
        text = "PROOF-OF-STAKE VALIDATOR POOLS (ETH & TON)",
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = BlueprintNavy
      )
    }

    // ETH Staking Card
    stakingStates[CryptoChain.ETH]?.let { ethStaking ->
      item {
        StakingCard(
          staking = ethStaking,
          onAddStake = { onStake(CryptoChain.ETH, 0.5) }
        )
      }
    }

    // TON Staking Card
    stakingStates[CryptoChain.TON]?.let { tonStaking ->
      item {
        StakingCard(
          staking = tonStaking,
          onAddStake = { onStake(CryptoChain.TON, 100.0) }
        )
      }
    }
  }
}

@Composable
private fun StakingCard(
  staking: StakingState,
  onAddStake: () -> Unit
) {
  Card(
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, BlueprintBorder),
    shape = RoundedCornerShape(10.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
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
              .size(32.dp)
              .background(staking.chain.color.copy(alpha = 0.15f), CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = staking.chain.symbol,
              color = staking.chain.color,
              fontWeight = FontWeight.Black,
              fontSize = 10.sp
            )
          }
          Column {
            Text(
              text = "${staking.chain.fullName} Staking",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = BlueprintNavy
            )
            Text(
              text = staking.validatorPool,
              style = MaterialTheme.typography.bodySmall,
              color = TextMuted,
              fontSize = 10.sp
            )
          }
        }

        Surface(
          color = SignalGreenLight,
          shape = RoundedCornerShape(4.dp)
        ) {
          Text(
            text = "${staking.aprPercent}% APR",
            color = SignalGreen,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text("Gestaked", fontSize = 10.sp, color = TextMuted)
          Text("${staking.stakedAmount} ${staking.chain.symbol}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BlueprintNavy)
        }
        Column {
          Text("Erträge erwirtschaftet", fontSize = 10.sp, color = TextMuted)
          Text("+${staking.rewardsAccumulated} ${staking.chain.symbol}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SignalGreen)
        }
        OutlinedButton(
          onClick = onAddStake,
          shape = RoundedCornerShape(6.dp),
          contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text("+ Stake", fontSize = 11.sp)
        }
      }
    }
  }
}

// -------------------------------------------------------------
// AES KEY MANAGEMENT TAB
// -------------------------------------------------------------
@Composable
private fun KeyManagementTabContent(
  seedPhrase: List<String>,
  onCopy: (String) -> Unit
) {
  var isMasked by remember { mutableStateOf(true) }
  var passphraseInput by remember { mutableStateOf("") }
  var encryptedBackupString by remember { mutableStateOf<String?>(null) }

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
        shape = RoundedCornerShape(10.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "AES-256 KRYPTOGRAPHISCHE KEYSTORE-SICHERUNG",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = BlueprintNavy
            )
            IconButton(onClick = { isMasked = !isMasked }) {
              Icon(
                imageVector = if (isMasked) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                contentDescription = "Maskieren"
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Seed Phrase Grid (12 words)
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .background(Slate50, RoundedCornerShape(8.dp))
              .border(1.dp, BlueprintBorder, RoundedCornerShape(8.dp))
              .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            for (i in 0 until 4) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                for (j in 0 until 3) {
                  val index = i * 3 + j
                  val word = if (isMasked) "••••••" else seedPhrase.getOrElse(index) { "" }
                  Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(0.5.dp, BlueprintBorder),
                    modifier = Modifier.weight(1f)
                  ) {
                    Text(
                      text = "${index + 1}. $word",
                      fontSize = 11.sp,
                      color = BlueprintNavy,
                      fontWeight = FontWeight.Medium,
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                    )
                  }
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Button(
            onClick = { onCopy(seedPhrase.joinToString(" ")) },
            colors = ButtonDefaults.buttonColors(containerColor = BlueprintNavy),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Seed-Phrase in Zwischenablage kopieren", fontSize = 11.sp)
          }
        }
      }
    }

    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BlueprintBorder),
        shape = RoundedCornerShape(10.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = "AES-GCM Verschlüsseltes Backup generieren",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = BlueprintNavy
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Erstellt ein mit AES-256 geschütztes Backup des Keystores. Nur mit dem vergebenen Passwort wiederherstellbar.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            fontSize = 11.sp
          )

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = passphraseInput,
            onValueChange = { passphraseInput = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("AES Passwort vergeben...") },
            singleLine = true
          )

          Spacer(modifier = Modifier.height(10.dp))

          Button(
            onClick = {
              if (passphraseInput.isNotBlank()) {
                val encrypted = AESEncryption.encrypt(seedPhrase.joinToString(" "), passphraseInput)
                encryptedBackupString = encrypted.cipherTextBase64.take(48) + "... (AES-GCM Authenticated)"
              }
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SignalBlue),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text("Verschlüsseltes Backup erzeugen", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }

          if (encryptedBackupString != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
              color = SignalGreenLight,
              shape = RoundedCornerShape(6.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = "Backup bereit: $encryptedBackupString",
                color = SignalGreen,
                fontSize = 11.sp,
                modifier = Modifier.padding(8.dp)
              )
            }
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// DEPOSIT DIALOG
// -------------------------------------------------------------
@Composable
private fun DepositDialog(
  initialChain: CryptoChain,
  onDismiss: () -> Unit,
  onDepositSimulate: (CryptoChain, Double) -> Unit
) {
  var selectedChain by remember { mutableStateOf(initialChain) }
  var depositAmount by remember { mutableStateOf("0.5") }
  val clipboardManager = LocalClipboardManager.current
  val dummyAddress = "0x71C2B04E5F931aC2388C89284De15f458B43a890"

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(14.dp),
      color = Color.White,
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Text(
          text = "EINZAHLUNG / DEPOSIT",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Black,
          color = BlueprintNavy
        )
        Text(
          text = "Krypto über externe Wallet oder W3Connect senden",
          style = MaterialTheme.typography.bodySmall,
          color = TextMuted,
          fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Chain selector chips
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          CryptoChain.entries.forEach { chain ->
            FilterChip(
              selected = selectedChain == chain,
              onClick = { selectedChain = chain },
              label = { Text(chain.symbol, fontSize = 11.sp) }
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Address Card
        Surface(
          color = Slate50,
          shape = RoundedCornerShape(8.dp),
          border = BorderStroke(1.dp, BlueprintBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(10.dp)) {
            Text("Empfangsadresse (${selectedChain.symbol}):", fontSize = 10.sp, color = TextMuted)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = dummyAddress,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = BlueprintNavy,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            TextButton(
              onClick = { clipboardManager.setText(AnnotatedString(dummyAddress)) },
              contentPadding = PaddingValues(0.dp)
            ) {
              Text("Adresse kopieren", fontSize = 10.sp, color = SignalBlue)
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = depositAmount,
          onValueChange = { depositAmount = it },
          label = { Text("Betrag (${selectedChain.symbol})") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

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
              val amt = depositAmount.toDoubleOrNull() ?: 0.5
              onDepositSimulate(selectedChain, amt)
            },
            colors = ButtonDefaults.buttonColors(containerColor = SignalBlue)
          ) {
            Text("Einzahlung bestätigen")
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// WITHDRAW DIALOG
// -------------------------------------------------------------
@Composable
private fun WithdrawDialog(
  initialChain: CryptoChain,
  assets: List<CryptoAsset>,
  onDismiss: () -> Unit,
  onWithdraw: (CryptoChain, String, Double) -> Unit
) {
  var selectedChain by remember { mutableStateOf(initialChain) }
  var targetAddress by remember { mutableStateOf("") }
  var withdrawAmount by remember { mutableStateOf("0.1") }

  val currentAsset = assets.find { it.chain == selectedChain }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(14.dp),
      color = Color.White,
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Text(
          text = "AUSZAHLUNG / WITHDRAW",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Black,
          color = BlueprintNavy
        )
        Text(
          text = "An externe Blockchain-Adresse überweisen",
          style = MaterialTheme.typography.bodySmall,
          color = TextMuted,
          fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Chain selector
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          CryptoChain.entries.forEach { chain ->
            FilterChip(
              selected = selectedChain == chain,
              onClick = { selectedChain = chain },
              label = { Text(chain.symbol, fontSize = 11.sp) }
            )
          }
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Verfügbar: ${currentAsset?.balance ?: 0.0} ${selectedChain.symbol}",
          fontSize = 11.sp,
          color = TextSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = targetAddress,
          onValueChange = { targetAddress = it },
          label = { Text("Ziel-Adresse (z.B. 0x... oder bc1q...)") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = withdrawAmount,
          onValueChange = { withdrawAmount = it },
          label = { Text("Betrag (${selectedChain.symbol})") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

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
              val amt = withdrawAmount.toDoubleOrNull() ?: 0.1
              onWithdraw(selectedChain, targetAddress.ifBlank { "0xExternalVault" }, amt)
            },
            colors = ButtonDefaults.buttonColors(containerColor = BlueprintNavy)
          ) {
            Text("Auszahlen")
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// SWAP DIALOG
// -------------------------------------------------------------
@Composable
private fun SwapDialog(
  assets: List<CryptoAsset>,
  onDismiss: () -> Unit,
  onSwap: (CryptoChain, CryptoChain, Double) -> Unit
) {
  var fromChain by remember { mutableStateOf(CryptoChain.ETH) }
  var toChain by remember { mutableStateOf(CryptoChain.BTC) }
  var swapAmount by remember { mutableStateOf("1.0") }

  val fromAsset = assets.find { it.chain == fromChain }
  val toAsset = assets.find { it.chain == toChain }

  val estimatedOutput = remember(swapAmount, fromAsset, toAsset) {
    val amt = swapAmount.toDoubleOrNull() ?: 0.0
    val fromUsd = amt * (fromAsset?.usdRate ?: 1.0)
    val toRate = toAsset?.usdRate ?: 1.0
    fromUsd / toRate
  }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(14.dp),
      color = Color.White,
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Text(
          text = "KRYPTO TAUSCHEN (SWAP)",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Black,
          color = BlueprintNavy
        )
        Text(
          text = "Direkter dezentraler Tausch mit Echtzeit-Kursen",
          style = MaterialTheme.typography.bodySmall,
          color = TextMuted,
          fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text("Von Krypto:", fontSize = 11.sp, color = TextMuted)
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          CryptoChain.entries.forEach { chain ->
            FilterChip(
              selected = fromChain == chain,
              onClick = { fromChain = chain },
              label = { Text(chain.symbol, fontSize = 11.sp) }
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = swapAmount,
          onValueChange = { swapAmount = it },
          label = { Text("Einsatz (${fromChain.symbol})") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text("Zu Krypto:", fontSize = 11.sp, color = TextMuted)
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          CryptoChain.entries.forEach { chain ->
            FilterChip(
              selected = toChain == chain,
              onClick = { toChain = chain },
              label = { Text(chain.symbol, fontSize = 11.sp) }
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Surface(
          color = Slate50,
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(10.dp)) {
            Text("Geschätzter Erhalt:", fontSize = 10.sp, color = TextMuted)
            Text(
              text = "≈ ${String.format(Locale.US, "%.5f", estimatedOutput)} ${toChain.symbol}",
              fontSize = 14.sp,
              fontWeight = FontWeight.Black,
              color = SignalGreen
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

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
              val amt = swapAmount.toDoubleOrNull() ?: 1.0
              onSwap(fromChain, toChain, amt)
            },
            colors = ButtonDefaults.buttonColors(containerColor = SignalBlue)
          ) {
            Text("Jetzt Tauschen")
          }
        }
      }
    }
  }
}
