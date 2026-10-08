package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.TrendingUp
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
import com.example.data.WalletRepository
import com.example.model.CryptoAsset
import com.example.ui.theme.*

data class LaunchedToken(
  val name: String,
  val symbol: String,
  val supply: String,
  val curveType: String,
  val currentPrice: String,
  val volume24h: String,
  val change24h: String,
  val isPositive: Boolean
)

data class LiquidityPool(
  val pair: String,
  val tvl: String,
  val apy: String,
  val volume24h: String,
  val userShare: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZonDexScreen(
  onBack: () -> Unit = {},
  onNavigateToWebPortal: () -> Unit = {},
  onNavigateToNotariat: () -> Unit = {},
  onNavigateToRAppCenter: () -> Unit = {},
  executionMode: com.example.model.ExecutionMode = com.example.model.ExecutionMode.MAIN_REAL,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val assets by WalletRepository.assets.collectAsState()

  BackHandler(enabled = true) {
    onBack()
  }

  var selectedTab by remember { mutableIntStateOf(0) }
  val tabTitles = listOf("⚡ Bonding-Swap", "🚀 Launchpad", "💧 Pools", "⛏️ Staking", "🔗 Verzahnung")

  // Swap State
  var fromAssetSymbol by remember { mutableStateOf("MTK") }
  var toAssetSymbol by remember { mutableStateOf("ZON") }
  var swapAmountInput by remember { mutableStateOf("50.0") }
  var swapReceiptMessage by remember { mutableStateOf<String?>(null) }

  // Launchpad State
  var launchTokenName by remember { mutableStateOf("") }
  var launchTokenSymbol by remember { mutableStateOf("") }
  var launchTokenSupply by remember { mutableStateOf("1000000") }
  var launchCurveSlope by remember { mutableStateOf("Linear (y = kx)") }
  var launchTokensList by remember {
    mutableStateOf(
      listOf(
        LaunchedToken("ZON Native Token", "ZON", "10,000,000", "Polynomisch", "0.42 MTK", "$48,920", "+14.8%", true),
        LaunchedToken("Community Notariat Coin", "CNC", "5,000,000", "Linear", "0.18 MTK", "$12,400", "+5.2%", true),
        LaunchedToken("Kreis Autonomie Token", "KAT", "2,500,000", "Exponentiell", "1.05 MTK", "$9,150", "-2.1%", false),
        LaunchedToken("ÖkoEnergie ZON", "OEZ", "8,000,000", "Bonding Curve", "0.08 MTK", "$3,200", "+8.4%", true)
      )
    )
  }

  // Liquidity Pools
  val pools = remember {
    listOf(
      LiquidityPool("ZON / MTK", "$1,480,200", "24.8% APY", "$184,000", "1,240 ZON-LP"),
      LiquidityPool("MTK / USDC", "$3,920,000", "11.2% APY", "$492,000", "450 MTK-LP"),
      LiquidityPool("NEC / ETH", "$2,100,500", "18.5% APY", "$230,000", "80 NEC-LP"),
      LiquidityPool("ZON / USDT", "$890,000", "15.0% APY", "$95,000", "0 LP")
    )
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(BackgroundLight)
      .testTag("zon_dex_screen")
  ) {
    // -----------------------------------------------------------------
    // TOP HEADER
    // -----------------------------------------------------------------
    Surface(
      color = BlueprintNavy,
      tonalElevation = 4.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
              onClick = onBack,
              modifier = Modifier
                .size(36.dp)
                .testTag("zon_dex_back_btn")
            ) {
              Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Zurück",
                tint = Color.White
              )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "ZON UNIVERSAL DEX",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Black,
                  color = Color.White,
                  fontSize = 14.sp
                )
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
              }
              Text(
                text = "Bonding-Curve Börse & Launchpad · com.rfof.zon.dex",
                style = MaterialTheme.typography.bodySmall,
                color = UrkundeGold,
                fontSize = 11.sp
              )
            }
          }

          Surface(
            color = if (executionMode == com.example.model.ExecutionMode.TEST_DEMO) SignalGreen.copy(alpha = 0.2f) else UrkundeGold.copy(alpha = 0.2f),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, if (executionMode == com.example.model.ExecutionMode.TEST_DEMO) SignalGreen else UrkundeGold)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(7.dp)
                  .background(if (executionMode == com.example.model.ExecutionMode.TEST_DEMO) SignalGreen else UrkundeGold, CircleShape)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = if (executionMode == com.example.model.ExecutionMode.TEST_DEMO) "TEST (DEMO)" else "MAIN (REAL)",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = if (executionMode == com.example.model.ExecutionMode.TEST_DEMO) SignalGreen else UrkundeGold
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Interconnection Toolbar (Verzahnungs-Buttons)
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 5.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "VERZAHNUNG:",
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = UrkundeGold
          )

          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            FilledTonalButton(
              onClick = onNavigateToWebPortal,
              colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = SignalBlue,
                contentColor = Color.White
              ),
              contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
              shape = RoundedCornerShape(6.dp),
              modifier = Modifier.testTag("zon_dex_to_primary_rapp_btn")
            ) {
              Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(11.dp))
              Spacer(modifier = Modifier.width(3.dp))
              Text("Primär-rApp (Web App)", fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }

            FilledTonalButton(
              onClick = onNavigateToNotariat,
              colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = Color(0xFF4F46E5),
                contentColor = Color.White
              ),
              contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
              shape = RoundedCornerShape(6.dp),
              modifier = Modifier.testTag("zon_dex_to_notariat_btn")
            ) {
              Icon(Icons.Default.VerifiedUser, contentDescription = null, modifier = Modifier.size(11.dp))
              Spacer(modifier = Modifier.width(3.dp))
              Text("XJustiz Notariat", fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
              onClick = onNavigateToRAppCenter,
              colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
              border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
              contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
              shape = RoundedCornerShape(6.dp),
              modifier = Modifier.testTag("zon_dex_to_rapp_center_btn")
            ) {
              Icon(Icons.Default.Storefront, contentDescription = null, modifier = Modifier.size(11.dp))
              Spacer(modifier = Modifier.width(2.dp))
              Text("Center", fontSize = 9.sp)
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Navigation Tabs
        val tabScrollState = rememberScrollState()
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(tabScrollState),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          tabTitles.forEachIndexed { index, title ->
            val isSelected = selectedTab == index
            FilterChip(
              selected = isSelected,
              onClick = { selectedTab = index },
              label = { Text(title, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = if (index == 0) UrkundeGold else SignalBlue,
                selectedLabelColor = if (index == 0) Color.Black else Color.White,
                containerColor = BlueprintSurface,
                labelColor = Color(0xFFCBD5E1)
              ),
              modifier = Modifier.testTag("zon_dex_tab_$index")
            )
          }
        }
      }
    }

    // -----------------------------------------------------------------
    // TAB CONTENTS
    // -----------------------------------------------------------------
    when (selectedTab) {
      0 -> ZonBondingSwapView(
        fromAsset = fromAssetSymbol,
        toAsset = toAssetSymbol,
        amountInput = swapAmountInput,
        onAmountChange = { swapAmountInput = it },
        onFromChange = { fromAssetSymbol = it },
        onToChange = { toAssetSymbol = it },
        onSwapExecute = {
          val amt = swapAmountInput.toDoubleOrNull() ?: 1.0
          val outAmt = amt * (if (fromAssetSymbol == "MTK") 2.38 else 0.42)
          swapReceiptMessage = "Tausch erfolgreich! $amt $fromAssetSymbol ➔ ${String.format("%.3f", outAmt)} $toAssetSymbol über Bonding-Curve ausgeführt."
          Toast.makeText(context, swapReceiptMessage, Toast.LENGTH_LONG).show()
        },
        receiptMessage = swapReceiptMessage
      )

      1 -> ZonLaunchpadView(
        tokenName = launchTokenName,
        tokenSymbol = launchTokenSymbol,
        tokenSupply = launchTokenSupply,
        curveSlope = launchCurveSlope,
        onNameChange = { launchTokenName = it },
        onSymbolChange = { launchTokenSymbol = it },
        onSupplyChange = { launchTokenSupply = it },
        onSlopeChange = { launchCurveSlope = it },
        tokens = launchTokensList,
        onDeployToken = {
          if (launchTokenName.isNotBlank() && launchTokenSymbol.isNotBlank()) {
            val newToken = LaunchedToken(
              name = launchTokenName,
              symbol = launchTokenSymbol.uppercase(),
              supply = launchTokenSupply,
              curveType = launchCurveSlope,
              currentPrice = "0.10 MTK",
              volume24h = "$0",
              change24h = "+0.0%",
              isPositive = true
            )
            launchTokensList = listOf(newToken) + launchTokensList
            launchTokenName = ""
            launchTokenSymbol = ""
            Toast.makeText(context, "Neuer Token '${newToken.symbol}' erfolgreich auf Bonding-Curve gemintet!", Toast.LENGTH_LONG).show()
          } else {
            Toast.makeText(context, "Bitte Name und Symbol eingeben!", Toast.LENGTH_SHORT).show()
          }
        }
      )

      2 -> ZonPoolsView(
        pools = pools,
        onAddLiquidity = { pair ->
          Toast.makeText(context, "Liquidität für $pair erfolgreich bereitgestellt!", Toast.LENGTH_SHORT).show()
        }
      )

      3 -> ZonStakingView(
        onStake = { poolName, amount ->
          Toast.makeText(context, "$amount in $poolName erfolgreich gestaked!", Toast.LENGTH_SHORT).show()
        }
      )

      4 -> ZonInterconnectionParityView(
        onNavigateToWeb = onNavigateToWebPortal,
        onNavigateToNotariat = onNavigateToNotariat,
        onNavigateToRAppCenter = onNavigateToRAppCenter
      )
    }
  }
}

// -------------------------------------------------------------
// TAB 0: BONDING SWAP VIEW
// -------------------------------------------------------------
@Composable
private fun ZonBondingSwapView(
  fromAsset: String,
  toAsset: String,
  amountInput: String,
  onAmountChange: (String) -> Unit,
  onFromChange: (String) -> Unit,
  onToChange: (String) -> Unit,
  onSwapExecute: () -> Unit,
  receiptMessage: String?
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    // Bonding Curve Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, UrkundeGold)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Bonding-Curve Swap ($fromAsset ⮂ $toAsset)",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = BlueprintNavy
              )
              Text(
                text = "Preisformel: P(S) = S² · k · Mathematische Slippage-Garantie",
                fontSize = 10.sp,
                color = TextSecondary
              )
            }
            Surface(color = SignalGreenLight, shape = RoundedCornerShape(6.dp)) {
              Text(
                text = "AUTOMATISCH",
                color = SignalGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // From Field
          OutlinedTextField(
            value = amountInput,
            onValueChange = onAmountChange,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("zon_swap_input"),
            label = { Text("Verkaufen ($fromAsset)") },
            trailingIcon = {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 8.dp)
              ) {
                Text(fromAsset, fontWeight = FontWeight.Bold, color = SignalBlue)
              }
            },
            singleLine = true,
            shape = RoundedCornerShape(10.dp)
          )

          Spacer(modifier = Modifier.height(8.dp))

          // Swap direction toggle
          Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            FilledIconButton(
              onClick = {
                val temp = fromAsset
                onFromChange(toAsset)
                onToChange(temp)
              },
              colors = IconButtonDefaults.filledIconButtonColors(containerColor = SignalBlue),
              modifier = Modifier.size(32.dp)
            ) {
              Icon(Icons.Default.SwapVert, contentDescription = "Tauschrichtung umkehren", tint = Color.White)
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          // To Field estimated
          val parsed = amountInput.toDoubleOrNull() ?: 0.0
          val estimatedOut = parsed * (if (fromAsset == "MTK") 2.38 else 0.42)
          OutlinedTextField(
            value = String.format("%.4f", estimatedOut),
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Erhalten ($toAsset, geschätzt)") },
            trailingIcon = {
              Text(toAsset, fontWeight = FontWeight.Bold, color = UrkundeGoldDark, modifier = Modifier.padding(end = 12.dp))
            },
            singleLine = true,
            shape = RoundedCornerShape(10.dp)
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Execution Button
          Button(
            onClick = onSwapExecute,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("zon_execute_swap_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = UrkundeGoldDark),
            shape = RoundedCornerShape(10.dp)
          ) {
            Icon(Icons.Default.Bolt, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Bonding-Swap sofort ausführen", fontWeight = FontWeight.Bold, color = Color.White)
          }

          if (receiptMessage != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
              color = SignalGreenLight,
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = receiptMessage,
                color = SignalGreen,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(10.dp)
              )
            }
          }
        }
      }
    }

    // Live Orderbook Strip
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BlueprintBorder)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = "LIVE ORDERBOOK & TIEFE",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = BlueprintNavy
          )
          Spacer(modifier = Modifier.height(8.dp))

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("KAUF-ORDERS (BIDS)", fontSize = 10.sp, color = SignalGreen, fontWeight = FontWeight.Bold)
            Text("VERKAUF-ORDERS (ASKS)", fontSize = 10.sp, color = Color(0xFFEF4444), fontWeight = FontWeight.Bold)
          }

          HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = BlueprintBorder)

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
              Text("0.422 MTK  •  12,500 ZON", fontSize = 11.sp, fontFamily = FontFamily.Monospace)
              Text("0.420 MTK  •  28,000 ZON", fontSize = 11.sp, fontFamily = FontFamily.Monospace)
              Text("0.418 MTK  •   8,200 ZON", fontSize = 11.sp, fontFamily = FontFamily.Monospace)
            }
            Column(horizontalAlignment = Alignment.End) {
              Text("0.425 MTK  •   5,100 ZON", fontSize = 11.sp, fontFamily = FontFamily.Monospace)
              Text("0.428 MTK  •  19,400 ZON", fontSize = 11.sp, fontFamily = FontFamily.Monospace)
              Text("0.432 MTK  •  42,000 ZON", fontSize = 11.sp, fontFamily = FontFamily.Monospace)
            }
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// TAB 1: LAUNCHPAD VIEW
// -------------------------------------------------------------
@Composable
private fun ZonLaunchpadView(
  tokenName: String,
  tokenSymbol: String,
  tokenSupply: String,
  curveSlope: String,
  onNameChange: (String) -> Unit,
  onSymbolChange: (String) -> Unit,
  onSupplyChange: (String) -> Unit,
  onSlopeChange: (String) -> Unit,
  tokens: List<LaunchedToken>,
  onDeployToken: () -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    // Launch Form
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, SignalBlue)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = "Neuen Token auf Bonding-Curve starten",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = BlueprintNavy
          )
          Text(
            text = "Kein zentraler Liquidity-Pool nötig. Die Bonding-Curve garantiert instanten Handel & Preisfindung.",
            fontSize = 11.sp,
            color = TextSecondary
          )

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = tokenName,
            onValueChange = onNameChange,
            label = { Text("Token Name (z.B. Sovereign Gold)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(8.dp)
          )

          Spacer(modifier = Modifier.height(8.dp))

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
              value = tokenSymbol,
              onValueChange = onSymbolChange,
              label = { Text("Symbol (z.B. SVG)") },
              modifier = Modifier.weight(1f),
              singleLine = true,
              shape = RoundedCornerShape(8.dp)
            )

            OutlinedTextField(
              value = tokenSupply,
              onValueChange = onSupplyChange,
              label = { Text("Initial Supply") },
              modifier = Modifier.weight(1f),
              singleLine = true,
              shape = RoundedCornerShape(8.dp)
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Button(
            onClick = onDeployToken,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("zon_deploy_token_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = SignalBlue),
            shape = RoundedCornerShape(8.dp)
          ) {
            Icon(Icons.Default.RocketLaunch, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Token auf ZON Launchpad deployen", fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    // List of active Tokens
    item {
      Text(
        text = "AKTIVE TOKENS AUF DER BONDING-CURVE",
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        color = TextMuted
      )
    }

    items(tokens) { token ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BlueprintBorder)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(38.dp)
                .background(SignalBlueLight, RoundedCornerShape(8.dp)),
              contentAlignment = Alignment.Center
            ) {
              Text(token.symbol.take(2), fontWeight = FontWeight.Black, color = SignalBlue)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(token.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = BlueprintNavy)
              Text("${token.supply} • ${token.curveType}", fontSize = 10.sp, color = TextMuted)
            }
          }

          Column(horizontalAlignment = Alignment.End) {
            Text(token.currentPrice, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = BlueprintNavy)
            Text(
              token.change24h,
              fontSize = 11.sp,
              color = if (token.isPositive) SignalGreen else Color(0xFFEF4444),
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// TAB 2: POOLS VIEW
// -------------------------------------------------------------
@Composable
private fun ZonPoolsView(
  pools: List<LiquidityPool>,
  onAddLiquidity: (String) -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    items(pools) { pool ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BlueprintBorder)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.WaterDrop, contentDescription = null, tint = SignalBlue, modifier = Modifier.size(20.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text(pool.pair, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = BlueprintNavy)
            }

            Surface(
              color = SignalGreenLight,
              shape = RoundedCornerShape(6.dp)
            ) {
              Text(
                pool.apy,
                color = SignalGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
              Text("Total Value Locked (TVL)", fontSize = 10.sp, color = TextMuted)
              Text(pool.tvl, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Column {
              Text("24h Volumen", fontSize = 10.sp, color = TextMuted)
              Text(pool.volume24h, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Column {
              Text("Dein Anteil", fontSize = 10.sp, color = TextMuted)
              Text(pool.userShare, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SignalBlue)
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Button(
            onClick = { onAddLiquidity(pool.pair) },
            colors = ButtonDefaults.buttonColors(containerColor = SignalBlue),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text("+ Liquidität hinzufügen")
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// TAB 3: STAKING VIEW
// -------------------------------------------------------------
@Composable
private fun ZonStakingView(
  onStake: (String, String) -> Unit
) {
  var stakeAmount by remember { mutableStateOf("100") }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BlueprintNavy)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text("ZON Protocol Staking", fontWeight = FontWeight.Black, color = Color.White, fontSize = 16.sp)
          Text(
            "Stake ZON oder MTK, um an den dezentralen Netzwerkgebühren aller 28 Fachkategorien zu partizipieren.",
            color = Color(0xFFCBD5E1),
            fontSize = 11.sp
          )

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = stakeAmount,
            onValueChange = { stakeAmount = it },
            label = { Text("Menge zum Staken", color = Color.White) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White,
              focusedBorderColor = UrkundeGold,
              unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
            )
          )

          Spacer(modifier = Modifier.height(12.dp))

          Button(
            onClick = { onStake("ZON Vault", stakeAmount) },
            colors = ButtonDefaults.buttonColors(containerColor = UrkundeGold),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text("Jetzt Staken & Belohnungen erhalten", color = Color.Black, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// TAB 4: VERZAHNUNG & PARITÄT
// -------------------------------------------------------------
@Composable
private fun ZonInterconnectionParityView(
  onNavigateToWeb: () -> Unit,
  onNavigateToNotariat: () -> Unit,
  onNavigateToRAppCenter: () -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.5.dp, UrkundeGold)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "🔗 SOUVERÄNE VERZAHNUNG DER 3 APPS",
            fontWeight = FontWeight.Black,
            fontSize = 14.sp,
            color = BlueprintNavy
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "ZON Universal DEX & Launchpad ist als rApp Extension 1 eng verzahnt mit der Primär-rApp 'PRAI / MTK / NEC' und der Extension 2 'XJustiz Notar & Grundbuch Archiv'.",
            fontSize = 11.sp,
            color = TextSecondary,
            lineHeight = 16.sp
          )

          Spacer(modifier = Modifier.height(14.dp))

          Button(
            onClick = onNavigateToWeb,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = SignalBlue),
            shape = RoundedCornerShape(8.dp)
          ) {
            Icon(Icons.Default.Language, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Zur Primär-rApp wechseln (Web App Browser)")
          }

          Spacer(modifier = Modifier.height(8.dp))

          Button(
            onClick = onNavigateToNotariat,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
            shape = RoundedCornerShape(8.dp)
          ) {
            Icon(Icons.Default.VerifiedUser, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Zur Extension 2 wechseln (XJustiz Notariat)")
          }

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedButton(
            onClick = onNavigateToRAppCenter,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
          ) {
            Icon(Icons.Default.Storefront, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Zurück zum rApp Center (PlayStore)")
          }
        }
      }
    }
  }
}
