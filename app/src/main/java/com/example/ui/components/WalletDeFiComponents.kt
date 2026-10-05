package com.example.ui.components

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.DeFiRepository
import com.example.model.*
import com.example.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@Composable
fun LaunchpadTabContent(
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val tokens by DeFiRepository.tokens.collectAsState()
  var showLaunchDialog by remember { mutableStateOf(false) }

  if (showLaunchDialog) {
    LaunchTokenDialog(
      onDismiss = { showLaunchDialog = false },
      onTokenCreated = { token ->
        showLaunchDialog = false
        Toast.makeText(context, "${token.name} (${token.symbol}) erfolgreich als ZON-Extension gelauncht!", Toast.LENGTH_LONG).show()
      }
    )
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BlueprintNavy)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "TOKEN LAUNCHPAD & DEX",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = Color.White
              )
              Text(
                text = "ZON Extension Engine · Autonome 50/50 Bonding Curve",
                style = MaterialTheme.typography.bodySmall,
                color = UrkundeGold,
                fontSize = 11.sp
              )
            }

            Button(
              onClick = { showLaunchDialog = true },
              colors = ButtonDefaults.buttonColors(containerColor = SignalGreen),
              shape = RoundedCornerShape(8.dp),
              contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Icon(Icons.Default.RocketLaunch, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Token Launchen", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "Jeder gelaunchte Token basiert auf ZON. Keine externe Börse nötig – die dezentrale Börse ist direkt integriert. Staking- und Mining-Pools werden ab 20.000 € Liquidität freigeschaltet. Ersteller erhalten 10% permanentes Gebühreneinkommen.",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFCBD5E1),
            fontSize = 11.sp
          )
        }
      }
    }

    item {
      Text(
        text = "Aktive Community-Währungen (${tokens.size})",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = BlueprintNavy
      )
    }

    items(tokens, key = { it.id }) { token ->
      TokenCardItem(token = token)
    }

    item {
      Spacer(modifier = Modifier.height(40.dp))
    }
  }
}

@Composable
fun TokenCardItem(token: LaunchedToken) {
  val context = LocalContext.current
  val currencyFmt = remember { NumberFormat.getCurrencyInstance(Locale.GERMANY) }

  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, BlueprintBorder),
    shape = RoundedCornerShape(12.dp)
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
              .size(40.dp)
              .background(SignalBlueLight, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Text(token.iconEmoji, fontSize = 20.sp)
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(token.name, fontWeight = FontWeight.Bold, color = BlueprintNavy, fontSize = 14.sp)
              Spacer(modifier = Modifier.width(6.dp))
              Surface(color = SignalBlueLight, shape = RoundedCornerShape(4.dp)) {
                Text(
                  token.symbol,
                  color = SignalBlue,
                  fontWeight = FontWeight.Black,
                  fontSize = 9.sp,
                  modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
              }
            }
            Text(
              text = "Extension von ${token.extensionOf} • Erstellt: ${token.creationDate}",
              fontSize = 10.sp,
              color = TextMuted
            )
          }
        }

        Column(horizontalAlignment = Alignment.End) {
          Text(
            text = currencyFmt.format(token.priceEur),
            fontWeight = FontWeight.Black,
            fontSize = 14.sp,
            color = SignalGreen
          )
          Text(
            text = "Liquidität: ${currencyFmt.format(token.liquidityEur)}",
            fontSize = 10.sp,
            color = if (token.liquidityEur >= 20_000.0) SignalGreen else TextMuted
          )
        }
      }

      Divider(modifier = Modifier.padding(vertical = 10.dp), color = BlueprintBorder)

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          if (token.isStakingSupported) {
            Surface(color = Color(0xFFEFF6FF), shape = RoundedCornerShape(4.dp)) {
              Text("Staking Aktiv", color = SignalBlue, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp))
            }
          }
          if (token.isMiningSupported) {
            Surface(color = Color(0xFFF0FDF4), shape = RoundedCornerShape(4.dp)) {
              Text("Mining Aktiv", color = SignalGreen, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp))
            }
          }
        }

        OutlinedButton(
          onClick = {
            val msg = DeFiRepository.wrapTokenPreLaunch(token.symbol, CryptoChain.ETH, 100.0)
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
          },
          contentPadding = PaddingValues(horizontal = 8.dp, vertical = 3.dp),
          shape = RoundedCornerShape(6.dp)
        ) {
          Icon(Icons.Default.Link, contentDescription = null, modifier = Modifier.size(12.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Pre-Launch Wrap", fontSize = 10.sp)
        }
      }
    }
  }
}

@Composable
fun LaunchTokenDialog(
  onDismiss: () -> Unit,
  onTokenCreated: (LaunchedToken) -> Unit
) {
  var name by remember { mutableStateOf("") }
  var symbol by remember { mutableStateOf("") }
  var initialDeposit by remember { mutableStateOf("2500") }
  var isMining by remember { mutableStateOf(true) }
  var isStaking by remember { mutableStateOf(true) }
  var iconEmoji by remember { mutableStateOf("⚡") }

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
        Text("Währung Launchen (ZON-Extension)", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = BlueprintNavy)
        Text(
          text = "Der Token startet bei 0 Supply & 0 Preis und mintet sich über die 50/50 Exponential Bonding Curve autonom aus der Liquidität.",
          style = MaterialTheme.typography.bodySmall,
          color = TextMuted
        )

        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Token Name (z.B. Solaris Grid)") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = symbol,
            onValueChange = { symbol = it },
            label = { Text("Kürzel (z.B. SOL)") },
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

        OutlinedTextField(
          value = initialDeposit,
          onValueChange = { initialDeposit = it },
          label = { Text("Start-Liquidität in EUR (Pool-Freischaltung ab 20.000 €)") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Staking-Funktion aktivieren:", fontSize = 12.sp)
          Switch(checked = isStaking, onCheckedChange = { isStaking = it })
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("JK-Mining aktivieren:", fontSize = 12.sp)
          Switch(checked = isMining, onCheckedChange = { isMining = it })
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          TextButton(onClick = onDismiss) { Text("Abbrechen") }
          Spacer(modifier = Modifier.width(8.dp))
          Button(
            onClick = {
              val depositVal = initialDeposit.toDoubleOrNull() ?: 1000.0
              val tok = DeFiRepository.launchNewToken(
                name = name.ifBlank { "Community Token" },
                symbol = symbol.ifBlank { "COM" },
                creatorAddress = "0xUSER_WALLET",
                initialLiquidityDeposit = depositVal,
                isMining = isMining,
                isStaking = isStaking,
                iconEmoji = iconEmoji
              )
              onTokenCreated(tok)
            },
            colors = ButtonDefaults.buttonColors(containerColor = SignalGreen),
            enabled = name.isNotBlank() && symbol.isNotBlank()
          ) {
            Text("Jetzt Launchen")
          }
        }
      }
    }
  }
}

@Composable
fun StakingMiningPoolsTabContent(
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val stakingPools by DeFiRepository.stakingPools.collectAsState()
  val miningPools by DeFiRepository.miningPools.collectAsState()
  var activeSubTab by remember { mutableStateOf(0) }
  val subTabs = listOf("Staking Pools", "Mining Pools")

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp)
  ) {
    TabRow(
      selectedTabIndex = activeSubTab,
      containerColor = Color.White,
      contentColor = BlueprintNavy
    ) {
      subTabs.forEachIndexed { i, title ->
        Tab(
          selected = activeSubTab == i,
          onClick = { activeSubTab = i },
          text = { Text(title, fontWeight = if (activeSubTab == i) FontWeight.Bold else FontWeight.Normal, fontSize = 12.sp) }
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Notice regarding 20,000 EUR threshold
    Surface(
      color = UrkundeGoldBg,
      shape = RoundedCornerShape(8.dp),
      border = BorderStroke(1.dp, UrkundeGold.copy(alpha = 0.5f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Info, contentDescription = null, tint = UrkundeGoldDark, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Pool-Kriterium: Neue Pools werden erst ab 20.000,00 € Liquidität in der jeweiligen Währung freigeschaltet.",
          fontSize = 11.sp,
          color = UrkundeGoldDark,
          fontWeight = FontWeight.Medium
        )
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    LazyColumn(
      verticalArrangement = Arrangement.spacedBy(10.dp),
      contentPadding = PaddingValues(bottom = 60.dp)
    ) {
      if (activeSubTab == 0) {
        items(stakingPools) { pool ->
          Card(
            modifier = Modifier.fillMaxWidth(),
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
                Column {
                  Text(pool.tokenName, fontWeight = FontWeight.Bold, color = BlueprintNavy, fontSize = 14.sp)
                  Text("Mindest-Stake: ${pool.minStake} ${pool.tokenSymbol} • Sperrfrist: ${pool.lockPeriodDays} Tage", fontSize = 11.sp, color = TextMuted)
                }
                Surface(color = SignalGreenLight, shape = RoundedCornerShape(6.dp)) {
                  Text(
                    text = "${pool.apyPercent}% APY",
                    color = SignalGreen,
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(8.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "Pool-Größe: ${pool.totalStaked} ${pool.tokenSymbol} (${pool.participantsCount} Teilnehmer)",
                  fontSize = 11.sp,
                  color = BlueprintNavy
                )
                Button(
                  onClick = {
                    Toast.makeText(context, "Erfolgreich in ${pool.tokenName} gestaked!", Toast.LENGTH_SHORT).show()
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = BlueprintNavy),
                  shape = RoundedCornerShape(6.dp),
                  contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                  Text("Staken", fontSize = 11.sp)
                }
              }
            }
          }
        }
      } else {
        items(miningPools) { mine ->
          Card(
            modifier = Modifier.fillMaxWidth(),
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
                Column {
                  Text(mine.tokenSymbol + " Mining Cluster", fontWeight = FontWeight.Bold, color = BlueprintNavy, fontSize = 14.sp)
                  Text("Algorithmus: ${mine.algorithm}", fontSize = 11.sp, color = SignalBlue)
                }
                Surface(color = SignalBlueLight, shape = RoundedCornerShape(6.dp)) {
                  Text(
                    text = "${mine.hashrateGh} GH/s",
                    color = SignalBlue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(8.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "Block-Reward: ${mine.blockReward} ${mine.tokenSymbol} (${mine.activeMiners} Miner aktiv)",
                  fontSize = 11.sp,
                  color = BlueprintNavy
                )
                Button(
                  onClick = {
                    Toast.makeText(context, "Miner mit Cluster ${mine.tokenSymbol} verbunden!", Toast.LENGTH_SHORT).show()
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = SignalGreen),
                  shape = RoundedCornerShape(6.dp),
                  contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                  Text("Minen", fontSize = 11.sp)
                }
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun NftMarketplaceTabContent(
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val nfts by DeFiRepository.nfts.collectAsState()

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp),
    contentPadding = PaddingValues(bottom = 60.dp)
  ) {
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = BlueprintNavy),
        shape = RoundedCornerShape(12.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "NEC NOTARIAL NFT MARKTPLATZ",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black,
            color = Color.White
          )
          Text(
            text = "Handelbare Notariatsurkunden, Patentzertifikate & 8-Sichten Urkunden mit 100% notarieller Beweiskraft (§ 36 BeurkG).",
            style = MaterialTheme.typography.bodySmall,
            color = UrkundeGold,
            fontSize = 11.sp
          )
        }
      }
    }

    items(nfts) { nft ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, UrkundeGold),
        shape = RoundedCornerShape(10.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(nft.title, fontWeight = FontWeight.Bold, color = BlueprintNavy, fontSize = 14.sp)
              Text("Urkunde: ${nft.urkundenNummer} • Notariat: ${nft.notarialSealDate}", fontSize = 11.sp, color = UrkundeGoldDark, fontWeight = FontWeight.SemiBold)
              Text("IPFS: ${nft.ipfsCid.take(28)}...", fontSize = 10.sp, color = SignalBlue, fontFamily = FontFamily.Monospace)
            }
            Surface(color = UrkundeGoldBg, shape = RoundedCornerShape(6.dp)) {
              Text(
                text = "${nft.priceZon} ZON",
                color = UrkundeGoldDark,
                fontWeight = FontWeight.Black,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Verified, contentDescription = null, tint = SignalGreen, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Beglaubigt (§ 36 BeurkG)", fontSize = 10.sp, color = SignalGreen, fontWeight = FontWeight.Bold)
            }

            Button(
              onClick = {
                Toast.makeText(context, "${nft.title} im Treuhand-Depot hinterlegt!", Toast.LENGTH_SHORT).show()
              },
              colors = ButtonDefaults.buttonColors(containerColor = UrkundeGoldDark),
              shape = RoundedCornerShape(6.dp),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
              Text("Zertifikat Handeln", fontSize = 11.sp, color = Color.White)
            }
          }
        }
      }
    }
  }
}

@Composable
fun MultiChainExplorerTabContent(
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val chainStatus by com.example.network.AlchemyMultiChainService.chainStatus.collectAsState()

  fun openExternalUrl(url: String) {
    try {
      val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
      context.startActivity(intent)
    } catch (e: Exception) {
      Toast.makeText(context, "Explorer Link: $url", Toast.LENGTH_SHORT).show()
    }
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp),
    contentPadding = PaddingValues(bottom = 60.dp)
  ) {
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = BlueprintNavy),
        shape = RoundedCornerShape(12.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "MULTI-CHAIN BLOCKCHAIN EXPLORER",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black,
            color = Color.White
          )
          Text(
            text = "Parallele analoge Synchronisation zwischen dem souveränen Ledger und globalen Explorern (Mempool, Etherscan, Tonscan).",
            style = MaterialTheme.typography.bodySmall,
            color = UrkundeGold,
            fontSize = 11.sp
          )
        }
      }
    }

    // 1. Sovereign Ledger
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(2.dp, UrkundeGold),
        shape = RoundedCornerShape(10.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(modifier = Modifier.size(10.dp).background(UrkundeGold, CircleShape))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Souveräner Notariats-Ledger (L1)", fontWeight = FontWeight.Bold, color = BlueprintNavy)
            }
            Surface(color = UrkundeGoldBg, shape = RoundedCornerShape(4.dp)) {
              Text("AUTONOM", color = UrkundeGoldDark, fontWeight = FontWeight.Black, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
            }
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text("Blockhöhe: #18,924 • Siegel: § 36 BeurkG • Letzter Hash: 0x892a7f10...c4e9", fontSize = 11.sp, color = TextMuted, fontFamily = FontFamily.Monospace)
          Text("Status: Dual-Parität mit Grundbuch & Urkundenarchiv aktiv", fontSize = 10.sp, color = SignalGreen, fontWeight = FontWeight.SemiBold)
        }
      }
    }

    // 2. Ethereum (Etherscan & Alchemy)
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFF627EEA)),
        shape = RoundedCornerShape(10.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(modifier = Modifier.size(10.dp).background(Color(0xFF627EEA), CircleShape))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Ethereum Mainnet (Alchemy RPC)", fontWeight = FontWeight.Bold, color = BlueprintNavy)
            }
            IconButton(
              onClick = { openExternalUrl("https://etherscan.io") },
              modifier = Modifier.size(28.dp)
            ) {
              Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = "Etherscan", tint = SignalBlue, modifier = Modifier.size(16.dp))
            }
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text("Letzter Block: ${chainStatus.ethBlockNumber} • Gas: ${chainStatus.ethGasPriceGwei} Gwei", fontSize = 11.sp, color = TextMuted, fontFamily = FontFamily.Monospace)
          Text("Verbindung: eth-mainnet.g.alchemy.com/v2/", fontSize = 10.sp, color = SignalBlue)
        }
      }
    }

    // 3. Bitcoin Mempool
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF7931A)),
        shape = RoundedCornerShape(10.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(modifier = Modifier.size(10.dp).background(Color(0xFFF7931A), CircleShape))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Bitcoin Mempool (mempool.space)", fontWeight = FontWeight.Bold, color = BlueprintNavy)
            }
            IconButton(
              onClick = { openExternalUrl("https://mempool.space") },
              modifier = Modifier.size(28.dp)
            ) {
              Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = "Mempool", tint = Color(0xFFF7931A), modifier = Modifier.size(16.dp))
            }
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text("Blockhöhe: #842,910 • Median Gebühr: 14 sat/vB", fontSize = 11.sp, color = TextMuted, fontFamily = FontFamily.Monospace)
          Text("Synchronisation: JK-Automaton PoW Parität", fontSize = 10.sp, color = Color(0xFFF7931A))
        }
      }
    }

    // 4. The Open Network (TON)
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFF0098EA)),
        shape = RoundedCornerShape(10.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(modifier = Modifier.size(10.dp).background(Color(0xFF0098EA), CircleShape))
              Spacer(modifier = Modifier.width(8.dp))
              Text("The Open Network (Tonscan)", fontWeight = FontWeight.Bold, color = BlueprintNavy)
            }
            IconButton(
              onClick = { openExternalUrl("https://tonscan.org") },
              modifier = Modifier.size(28.dp)
            ) {
              Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = "Tonscan", tint = Color(0xFF0098EA), modifier = Modifier.size(16.dp))
            }
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text("Masterchain Seqno: #38,109,240 • TPS: 14,200", fontSize = 11.sp, color = TextMuted, fontFamily = FontFamily.Monospace)
          Text("Bridge: ZON Wrapped Token Contract auf TON aktiv", fontSize = 10.sp, color = Color(0xFF0098EA))
        }
      }
    }
  }
}
