package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class AlchemyDocService(
  val name: String,
  val status: String,
  val isActive: Boolean,
  val description: String,
  val endpointExample: String,
  val curlSnippet: String
)

data class ByteUnitRow(
  val step: Int,
  val prefixSi: String,
  val symbolSi: String,
  val powerSi: String,
  val sizeSi: String,
  val prefixBin: String,
  val symbolBin: String,
  val powerBin: String,
  val isCenter: Boolean = false
)

@Composable
fun DocumentationScreen(
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val clipboard = LocalClipboardManager.current
  var selectedTab by remember { mutableStateOf(0) }
  val tabs = listOf("User Repos & RPCs", "Alchemy 14 APIs", "SI/Binäre Byte-Matrix", "Trinität & Bonding Curve", "Souveräne Parität")

  val alchemyServices = remember {
    listOf(
      AlchemyDocService(
        name = "Node API",
        status = "Active / Connected",
        isActive = true,
        description = "Core JSON-RPC 2.0 Engine für Ethereum Mainnet Node Abfragen (eth_blockNumber, eth_getBalance, eth_sendRawTransaction).",
        endpointExample = "https://eth-mainnet.g.alchemy.com/v2/\$ALCHEMY_API_KEY",
        curlSnippet = "curl -X POST https://eth-mainnet.g.alchemy.com/v2/\$KEY -H 'Content-Type: application/json' -d '{\"id\":1,\"jsonrpc\":\"2.0\",\"method\":\"eth_blockNumber\"}'"
      ),
      AlchemyDocService(
        name = "NFT API",
        status = "Available",
        isActive = false,
        description = "Multi-Chain API zum Launchen, Verifizieren, Analysieren und Handeln von NFTs & Notariatsurkunden (getNFTsForOwner, getNFTMetadata).",
        endpointExample = "https://eth-mainnet.g.alchemy.com/nft/v3/\$ALCHEMY_API_KEY",
        curlSnippet = "curl https://eth-mainnet.g.alchemy.com/nft/v3/\$KEY/getNFTsForOwner?owner=0x892a...RFOF"
      ),
      AlchemyDocService(
        name = "Token API",
        status = "Available",
        isActive = false,
        description = "Präzise Abfrage von ERC-20 / ZON Token-Guthaben, Metadaten und Devisenwerten über alle Adressen.",
        endpointExample = "alchemy_getTokenBalances, alchemy_getTokenMetadata",
        curlSnippet = "curl -X POST https://eth-mainnet.g.alchemy.com/v2/\$KEY -d '{\"id\":1,\"method\":\"alchemy_getTokenBalances\",\"params\":[\"0x892a...\"]}'"
      ),
      AlchemyDocService(
        name = "Prices API",
        status = "Available",
        isActive = false,
        description = "Echtzeit- und historische Devisenpreise für Krypto, ZON Bonding Pools und Währungsmärkte.",
        endpointExample = "https://api.g.alchemy.com/prices/v1/\$KEY/tokens/by-symbol?symbols=ETH,BTC",
        curlSnippet = "curl https://api.g.alchemy.com/prices/v1/\$KEY/tokens/by-symbol?symbols=ETH,BTC"
      ),
      AlchemyDocService(
        name = "Transfers API",
        status = "Available",
        isActive = false,
        description = "Historische Transaktionen, interne Swaps und Tokenbewegungen nach Blockbereich oder Adressfilter.",
        endpointExample = "alchemy_getAssetTransfers",
        curlSnippet = "curl -X POST https://eth-mainnet.g.alchemy.com/v2/\$KEY -d '{\"id\":1,\"method\":\"alchemy_getAssetTransfers\",\"params\":[{\"category\":[\"external\",\"erc20\"]}]}'"
      ),
      AlchemyDocService(
        name = "Bundler API",
        status = "Available",
        isActive = false,
        description = "ERC-4337 Account Abstraction RPC Endpoint für UserOperations und autonome Smart Accounts.",
        endpointExample = "eth_sendUserOperation, eth_estimateUserOperationGas",
        curlSnippet = "curl -X POST https://eth-mainnet.g.alchemy.com/v2/\$KEY -d '{\"method\":\"eth_sendUserOperation\",\"params\":[userOp, entryPoint]}'"
      ),
      AlchemyDocService(
        name = "Debug API",
        status = "Available",
        isActive = false,
        description = "Tiefgehende Einblicke in Ausführungs-Traces und On-Chain-Aktivitäten (debug_traceTransaction).",
        endpointExample = "debug_traceTransaction, debug_traceBlockByNumber",
        curlSnippet = "curl -X POST https://eth-mainnet.g.alchemy.com/v2/\$KEY -d '{\"method\":\"debug_traceTransaction\",\"params\":[\"0xtxhash\"]}'"
      ),
      AlchemyDocService(
        name = "Block Timestamp API",
        status = "Available",
        isActive = false,
        description = "Schnelle Ermittlung von Blöcken anhand präziser Zeitstempel für Notariats- und Beweissicherungszeitpunkte.",
        endpointExample = "https://api.g.alchemy.com/v2/\$KEY/blocks/by-timestamp?timestamp=1774310400",
        curlSnippet = "curl https://api.g.alchemy.com/v2/\$KEY/blocks/by-timestamp?timestamp=1774310400"
      ),
      AlchemyDocService(
        name = "Gas Manager",
        status = "Available",
        isActive = false,
        description = "Paymaster-Sponsoring: Beseitigt Gasgebühren für Endnutzer durch automatische Abdeckung im Hintergrund.",
        endpointExample = "pm_sponsorUserOperation",
        curlSnippet = "curl -X POST https://eth-mainnet.g.alchemy.com/v2/\$KEY -d '{\"method\":\"pm_sponsorUserOperation\",\"params\":[userOp]}'"
      ),
      AlchemyDocService(
        name = "Trace API",
        status = "Available",
        isActive = false,
        description = "Ermöglicht Opcode-Level Analysen von Smart Contracts und automatisierten ZON Bonding Transaktionen.",
        endpointExample = "trace_call, trace_rawTransaction",
        curlSnippet = "curl -X POST https://eth-mainnet.g.alchemy.com/v2/\$KEY -d '{\"method\":\"trace_call\",\"params\":[callObject, [\"trace\"]]}'"
      ),
      AlchemyDocService(
        name = "Transaction Receipts API",
        status = "Available",
        isActive = false,
        description = "Batch-Abruf aller Quittungen und Events eines Blocks in einem einzigen optimierten Aufruf.",
        endpointExample = "alchemy_getTransactionReceipts",
        curlSnippet = "curl -X POST https://eth-mainnet.g.alchemy.com/v2/\$KEY -d '{\"method\":\"alchemy_getTransactionReceipts\",\"params\":[{\"blockNumber\":\"latest\"}]}'"
      ),
      AlchemyDocService(
        name = "userOp Simulation API",
        status = "Available",
        isActive = false,
        description = "Simuliert User Operations vor der On-Chain Ausführung und gibt genaue Asset-Änderungen zurück.",
        endpointExample = "alchemy_simulateUserOperationAssetChanges",
        curlSnippet = "curl -X POST https://eth-mainnet.g.alchemy.com/v2/\$KEY -d '{\"method\":\"alchemy_simulateUserOperationAssetChanges\",\"params\":[userOp]}'"
      ),
      AlchemyDocService(
        name = "Websockets",
        status = "Available",
        isActive = false,
        description = "Bi-direktionales Echtzeitprotokoll für kontinuierliche Block-, Pending-Tx und Event-Subscriptions (eth_subscribe).",
        endpointExample = "wss://eth-mainnet.g.alchemy.com/v2/\$ALCHEMY_API_KEY",
        curlSnippet = "wscat -c wss://eth-mainnet.g.alchemy.com/v2/\$KEY -x '{\"method\":\"eth_subscribe\",\"params\":[\"newHeads\"]}'"
      ),
      AlchemyDocService(
        name = "Webhooks",
        status = "Available",
        isActive = false,
        description = "Automatisierte Push-Benachrichtigungen an Server/Endpunkte bei Smart Contract Adressaktivitäten.",
        endpointExample = "https://dashboard.alchemy.com/webhooks",
        curlSnippet = "# Webhooks werden im Alchemy Dashboard konfiguriert für GraphQL / REST Push"
      )
    )
  }

  val byteTable = remember {
    listOf(
      ByteUnitRow(1, "Quecto", "qB", "10⁻³⁰", "0,000000000000000000000000000001 B", "Quectibyte", "QiB", "2⁰ = 1 B"),
      ByteUnitRow(2, "Ronto", "rB", "10⁻²⁷", "0,000000000000000000000000001 B", "Rontibyte", "RiB", "2¹⁰ = 1.024 B"),
      ByteUnitRow(3, "Yocto", "yB", "10⁻²⁴", "0,000000000000000000000001 B", "Yoctibyte", "YiB", "2²⁰ = 1.048.576 B"),
      ByteUnitRow(4, "Zepto", "zB", "10⁻²¹", "0,000000000000000000001 B", "Zeptibyte", "ZiB", "2³⁰ = 1.073.741.824 B"),
      ByteUnitRow(5, "Atto", "aB", "10⁻¹⁸", "0,000000000000000001 B", "Attibyte", "AiB", "2⁴⁰ ≈ 1,099 × 10¹² B"),
      ByteUnitRow(6, "Femto", "fB", "10⁻¹⁵", "0,000000000000001 B", "Femtoibyte", "FiB", "2⁵⁰ ≈ 1,125 × 10¹⁵ B"),
      ByteUnitRow(7, "Pico", "pB", "10⁻¹²", "0,000000000001 B", "Picoibyte", "PiB", "2⁶⁰ ≈ 1,152 × 10¹⁸ B"),
      ByteUnitRow(8, "Nano", "nB", "10⁻⁹", "0,000000001 B", "Nanoibyte", "NiB", "2⁷⁰ ≈ 1,180 × 10²¹ B"),
      ByteUnitRow(9, "Mikro", "µB", "10⁻⁶", "0,000001 B", "Microibyte", "µiB", "2⁸⁰ ≈ 1,208 × 10²⁴ B"),
      ByteUnitRow(10, "Milli", "mB", "10⁻³", "0,001 B", "Millibyte", "miB", "2⁹⁰ ≈ 1,237 × 10²⁷ B"),
      ByteUnitRow(11, "Zenti", "cB", "10⁻²", "0,01 B", "Centibyte", "ciB", "2¹⁰⁰ ≈ 1,267 × 10³⁰ B"),
      ByteUnitRow(12, "Dezi", "dB", "10⁻¹", "0,1 B", "Decibyte", "diB", "2¹¹⁰ ≈ 1,298 × 10³³ B"),
      ByteUnitRow(13, "Byte", "B", "10⁰", "1 B (MITTELPUNKT)", "Byte", "B", "2¹²⁰ ≈ 1,330 × 10³⁶ B", isCenter = true),
      ByteUnitRow(14, "Deka", "daB", "10¹", "10 B", "Dekibyte", "daiB", "2¹³⁰ ≈ 1,363 × 10³⁹ B"),
      ByteUnitRow(15, "Hekto", "hB", "10²", "100 B", "Hektibyte", "hiB", "2¹⁴⁰ ≈ 1,398 × 10⁴² B"),
      ByteUnitRow(16, "Kilo", "kB", "10³", "1.000 B", "Kilobibyte", "KiB", "2¹⁵⁰ ≈ 1,433 × 10⁴⁵ B"),
      ByteUnitRow(17, "Mega", "MB", "10⁶", "1.000.000 B", "Megabibyte", "MiB", "2¹⁶⁰ ≈ 1,470 × 10⁴⁸ B"),
      ByteUnitRow(18, "Giga", "GB", "10⁹", "1.000.000.000 B", "Gigabibyte", "GiB", "2¹⁷⁰ ≈ 1,508 × 10⁵¹ B"),
      ByteUnitRow(19, "Tera", "TB", "10¹²", "1.000.000.000.000 B", "Terabibyte", "TiB", "2¹⁸⁰ ≈ 1,547 × 10⁵⁴ B"),
      ByteUnitRow(20, "Peta", "PB", "10¹⁵", "10¹⁵ B", "Petabibyte", "PiBiB", "2¹⁹⁰ ≈ 1,587 × 10⁵⁷ B"),
      ByteUnitRow(21, "Exa", "EB", "10¹⁸", "10¹⁸ B", "Exbibibyte", "EiBiB", "2²⁰⁰ ≈ 1,628 × 10⁶⁰ B"),
      ByteUnitRow(22, "Zetta", "ZB", "10²¹", "10²¹ B", "Zebbibyte", "ZiBiB", "2²¹⁰ ≈ 1,670 × 10⁶³ B"),
      ByteUnitRow(23, "Yotta", "YB", "10²⁴", "10²⁴ B", "Yobbibyte", "YiBiB", "2²²⁰ ≈ 1,713 × 10⁶⁶ B"),
      ByteUnitRow(24, "Quetta", "QB", "10³⁰", "10³⁰ B", "Quettibyte", "QiBiB", "2²³⁰ ≈ 1,757 × 10⁶⁹ B")
    )
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(BackgroundLight)
      .testTag("documentation_screen"),
    contentPadding = PaddingValues(bottom = 70.dp)
  ) {
    // 1. Cyberpunk Pixel Welcome Banner
    item {
      CyberpunkPixelWelcomeBanner()
    }

    // 2. Navigation Tabs
    item {
      ScrollableTabRow(
        selectedTabIndex = selectedTab,
        containerColor = Color.White,
        contentColor = BlueprintNavy,
        edgePadding = 16.dp
      ) {
        tabs.forEachIndexed { idx, title ->
          Tab(
            selected = selectedTab == idx,
            onClick = { selectedTab = idx },
            text = {
              Text(
                text = title,
                fontWeight = if (selectedTab == idx) FontWeight.Bold else FontWeight.Normal,
                fontSize = 12.sp,
                maxLines = 1
              )
            }
          )
        }
      }
    }

    // Tab Contents
    when (selectedTab) {
      0 -> {
        // Tab 0: Benutzer Repositories, eigene RPCs & Multi-Chain (ETH, BTC, TON, ZON, EVM)
        item {
          UserInfrastructureDocSection(
            onCopy = { snippet, label ->
              clipboard.setText(AnnotatedString(snippet))
              Toast.makeText(context, "$label kopiert!", Toast.LENGTH_SHORT).show()
            }
          )
        }
      }

      1 -> {
        // Alchemy 14 Services
        item {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "14 ALCHEMY MULTI-CHAIN MICROSERVICES",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Black,
              color = BlueprintNavy
            )
            Text(
              text = "Offizielle Schnittstellen-Dokumentation aller 14 Alchemy Services für das RFOF-NETWORK.",
              style = MaterialTheme.typography.bodySmall,
              color = TextMuted,
              modifier = Modifier.padding(bottom = 12.dp)
            )
          }
        }

        items(alchemyServices) { service ->
          AlchemyServiceDocCard(
            service = service,
            onCopy = {
              clipboard.setText(AnnotatedString(service.curlSnippet))
              Toast.makeText(context, "cURL-Snippet kopiert!", Toast.LENGTH_SHORT).show()
            }
          )
        }
      }

      2 -> {
        // Symmetrical SI & Binary Byte Table
        item {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "SYMMETRISCHE 24-STUFEN BYTE-MATRIX",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Black,
              color = BlueprintNavy
            )
            Text(
              text = "Universales thermodynamisches Datenmaßsystem mit exaktem 1-Byte-Mittelpunkt (Stufe 13: Quecto bis Quetta).",
              style = MaterialTheme.typography.bodySmall,
              color = TextMuted,
              modifier = Modifier.padding(bottom = 12.dp)
            )

            Card(
              modifier = Modifier.fillMaxWidth(),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              border = BorderStroke(1.dp, BlueprintBorder),
              shape = RoundedCornerShape(10.dp)
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .background(BlueprintNavy.copy(alpha = 0.08f), RoundedCornerShape(6.dp))
                    .padding(8.dp),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text("#", fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.width(22.dp))
                  Text("SI-Präfix", fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.width(60.dp))
                  Text("Symbol", fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.width(44.dp))
                  Text("Potenz", fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.width(48.dp))
                  Text("Binär (Basis 2)", fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.weight(1f))
                }

                Divider(modifier = Modifier.padding(vertical = 4.dp), color = BlueprintBorder)

                byteTable.forEach { row ->
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .background(
                        if (row.isCenter) UrkundeGoldBg else Color.Transparent,
                        RoundedCornerShape(4.dp)
                      )
                      .padding(horizontal = 8.dp, vertical = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(
                      text = "${row.step}",
                      fontSize = 10.sp,
                      fontWeight = if (row.isCenter) FontWeight.Bold else FontWeight.Normal,
                      color = if (row.isCenter) UrkundeGoldDark else BlueprintNavy,
                      modifier = Modifier.width(22.dp)
                    )
                    Text(
                      text = row.prefixSi,
                      fontSize = 10.sp,
                      fontWeight = if (row.isCenter) FontWeight.Bold else FontWeight.Normal,
                      color = if (row.isCenter) UrkundeGoldDark else BlueprintNavy,
                      modifier = Modifier.width(60.dp)
                    )
                    Text(
                      text = row.symbolSi,
                      fontSize = 10.sp,
                      fontWeight = FontWeight.SemiBold,
                      color = SignalBlue,
                      modifier = Modifier.width(44.dp)
                    )
                    Text(
                      text = row.powerSi,
                      fontSize = 10.sp,
                      fontFamily = FontFamily.Monospace,
                      color = TextMuted,
                      modifier = Modifier.width(48.dp)
                    )
                    Text(
                      text = "${row.symbolBin} (${row.powerBin})",
                      fontSize = 9.sp,
                      fontFamily = FontFamily.Monospace,
                      color = if (row.isCenter) UrkundeGoldDark else Color(0xFF475569),
                      modifier = Modifier.weight(1f)
                    )
                  }
                }
              }
            }
          }
        }
      }

      3 -> {
        // Trinität & Bonding Curve
        item {
          TrinitaryAndBondingDocSection()
        }
      }

      4 -> {
        // Souveräne Parität
        item {
          SovereignParityDocSection()
        }
      }
    }
  }
}

@Composable
fun CyberpunkPixelWelcomeBanner() {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            Color(0xFF0F172A), // Dark Midnight
            Color(0xFF2E1065), // Deep Cyber Violet
            Color(0xFF1E1B4B)  // Blueprint Dark
          )
        )
      )
      .padding(horizontal = 16.dp, vertical = 20.dp)
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
      // Pixel Logo Frame
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0xFF000000).copy(alpha = 0.6f))
          .border(2.dp, Brush.horizontalGradient(listOf(Color(0xFFC084FC), Color(0xFF38BDF8), Color(0xFFA855F7))), RoundedCornerShape(8.dp))
          .padding(horizontal = 16.dp, vertical = 10.dp)
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = "██████╗ ██████╗  █████╗ ██╗",
            fontFamily = FontFamily.Monospace,
            fontSize = 9.sp,
            color = Color(0xFF38BDF8), // Cyan
            fontWeight = FontWeight.Black
          )
          Text(
            text = "██╔══██╗██╔══██╗██╔══██╗██║",
            fontFamily = FontFamily.Monospace,
            fontSize = 9.sp,
            color = Color(0xFFA855F7), // Violet
            fontWeight = FontWeight.Black
          )
          Text(
            text = "██████╔╝██████╔╝███████║██║",
            fontFamily = FontFamily.Monospace,
            fontSize = 9.sp,
            color = Color(0xFFE879F9), // Magenta
            fontWeight = FontWeight.Black
          )
          Text(
            text = "██╔═══╝ ██╔══██╗██╔══██║██║",
            fontFamily = FontFamily.Monospace,
            fontSize = 9.sp,
            color = Color(0xFF38BDF8),
            fontWeight = FontWeight.Black
          )
          Text(
            text = "██║     ██║  ██║██║  ██║██║",
            fontFamily = FontFamily.Monospace,
            fontSize = 9.sp,
            color = Color(0xFFC084FC),
            fontWeight = FontWeight.Black
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).background(Color(0xFF38BDF8), CircleShape))
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "PRAI · MTK · NEC DOKUMENTATION & ARCHITEKTUR",
          color = Color.White,
          fontWeight = FontWeight.Black,
          fontSize = 12.sp,
          letterSpacing = 0.5.sp
        )
      }

      Text(
        text = "RFOF-NETWORK · Dual-Parität (§ 36 BeurkG) · ZON Universal Engine",
        color = Color(0xFFC084FC),
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold
      )
    }
  }
}

@Composable
fun AlchemyServiceDocCard(
  service: AlchemyDocService,
  onCopy: () -> Unit
) {
  var expanded by remember { mutableStateOf(false) }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 5.dp)
      .clickable { expanded = !expanded },
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, if (service.isActive) SignalGreen else BlueprintBorder),
    shape = RoundedCornerShape(10.dp)
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
              .size(10.dp)
              .background(if (service.isActive) SignalGreen else Color(0xFF94A3B8), CircleShape)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = service.name,
            fontWeight = FontWeight.Bold,
            color = BlueprintNavy,
            fontSize = 14.sp
          )
        }

        Surface(
          color = if (service.isActive) SignalGreenLight else Color(0xFFF1F5F9),
          shape = RoundedCornerShape(6.dp)
        ) {
          Text(
            text = service.status,
            color = if (service.isActive) SignalGreen else Color(0xFF64748B),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = service.description,
        style = MaterialTheme.typography.bodySmall,
        color = Color(0xFF334155),
        fontSize = 11.sp
      )

      AnimatedVisibility(visible = expanded) {
        Column(modifier = Modifier.padding(top = 10.dp)) {
          Text(
            text = "Endpoint / Methode:",
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            color = BlueprintNavy
          )
          Surface(
            color = Color(0xFF0F172A),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = service.endpointExample,
              color = Color(0xFF38BDF8),
              fontFamily = FontFamily.Monospace,
              fontSize = 10.sp,
              modifier = Modifier.padding(8.dp)
            )
          }

          Spacer(modifier = Modifier.height(8.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "cURL Snippet:",
              fontWeight = FontWeight.Bold,
              fontSize = 10.sp,
              color = BlueprintNavy
            )
            TextButton(
              onClick = onCopy,
              contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(12.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Kopieren", fontSize = 10.sp)
            }
          }

          Surface(
            color = Color(0xFF0F172A),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = service.curlSnippet,
              color = Color(0xFFA7F3D0),
              fontFamily = FontFamily.Monospace,
              fontSize = 9.sp,
              modifier = Modifier.padding(8.dp)
            )
          }
        }
      }
    }
  }
}

@Composable
fun TrinitaryAndBondingDocSection() {
  Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      border = BorderStroke(1.dp, BlueprintBorder),
      shape = RoundedCornerShape(12.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Balance, contentDescription = null, tint = UrkundeGold, modifier = Modifier.size(22.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "DIE SOUVERÄNE TRINITÄT (DREI-EINIGKEIT)",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Black,
            color = BlueprintNavy
          )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Das RFOF-NETWORK basiert auf der untrennbaren Dreiteilung der Rollen:\n\n" +
            "1. ⚙️ **SYSTEMSEITIGE AUTONOMIE:** Fest kodierte Genesis-Blöcke, Notariats-Parität, automatische Validatoren, JK Flip-Flop-Flap Automaten und autonome Wallets.\n\n" +
            "2. 🛡️ **ADMIN (MANUELLE AUTONOMIE):** Erfinder & Stammvater, Supply-Rechte für MTK & ZON, Notarielle Freigaben (§ 36 BeurkG), Notfall-Bremsen und strategische Genesis-Gewichtung.\n\n" +
            "3. 👥 **COMMUNITY NUTZER:** Dezentrale Teilhabe, Launch von eigenen Tokens (ZON-Extensions), Eröffnung von Staking/Mining Pools ab 20.000 € Liquidität, Governance in den 28 Fachkategorien.",
          fontSize = 11.sp,
          color = BlueprintNavy,
          lineHeight = 16.sp
        )
      }
    }

    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      border = BorderStroke(1.dp, SignalGreen),
      shape = RoundedCornerShape(12.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.TrendingUp, contentDescription = null, tint = SignalGreen, modifier = Modifier.size(22.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "50/50 EXPONENTIAL BONDING CURVE",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Black,
            color = BlueprintNavy
          )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "• **Initialzustand:** Supply = 0, Liquidität = 0, Preis = 0.\n" +
            "• **Gebührenbindung:** 100% aller Transaktionsgebühren fließen dauerhaft und unantastbar in den Liquiditätspool.\n" +
            "• **50/50 Dual-Wachstum:** Mit jedem Liquiditätszufluss steigt der mathematische Kurs um 50%, während zeitgleich 50% neue Tokens autonom gemintet werden.\n" +
            "• **Keine ungedeckte Inflation:** Jedes existierende ZON-Token ist zu jedem Zeitpunkt durch reale Liquidität gedeckt.\n" +
            "• **JK Flip-Flop Brücke:** Synchronisiert mit Bitcoin (PoW), Ethereum (EVM) und TON (The Open Network).",
          fontSize = 11.sp,
          color = BlueprintNavy,
          lineHeight = 16.sp
        )
      }
    }
  }
}

@Composable
fun SovereignParityDocSection() {
  Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      border = BorderStroke(1.dp, UrkundeGold),
      shape = RoundedCornerShape(12.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Gavel, contentDescription = null, tint = UrkundeGoldDark, modifier = Modifier.size(22.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "NOTARIELLE DUAL-PARITÄT (§ 36 BeurkG)",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Black,
            color = BlueprintNavy
          )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Alle 28 Fachkategorien und digitalen Urkunden besitzen rechtsverbindliche notarielle Beweiskraft.\n\n" +
            "• **XJustiz Standard:** Automatisierte Übermittlung an Gerichts- und Grundbuchregister.\n" +
            "• **Dual-Architektur:** Jeder Smart-Contract Event ist an eine notarielle Urkundennummer gekoppelt (Stammurkunde 1892/2026).\n" +
            "• **Souveränes Escrow:** Treuhand-Gelder sind insolvenzfest und segregiert gebunden.",
          fontSize = 11.sp,
          color = BlueprintNavy,
          lineHeight = 16.sp
        )
      }
    }
  }
}

// =========================================================================
// TAB 0: BENUTZER DOKUMENTATION - EIGENE INFRASTRUKTUR, REPOS & RPCS
// (Rein aus der Nutzer-Perspektive: Keine Admin-Interna, keine KI-Interna)
// =========================================================================

data class UserRpcTemplate(
  val chainName: String,
  val symbol: String,
  val defaultRpc: String,
  val chainIdHex: String,
  val explorerUrl: String,
  val curlExample: String,
  val desc: String
)

@Composable
fun UserInfrastructureDocSection(
  onCopy: (String, String) -> Unit
) {
  val userRpcTemplates = remember {
    listOf(
      UserRpcTemplate(
        chainName = "Ethereum (Mainnet / L2s)",
        symbol = "ETH / EVM",
        defaultRpc = "https://eth-mainnet.g.alchemy.com/v2/YOUR_API_KEY",
        chainIdHex = "0x1 (1)",
        explorerUrl = "https://etherscan.io",
        curlExample = "curl https://eth-mainnet.g.alchemy.com/v2/YOUR_API_KEY \\\n  -X POST \\\n  -H \"Content-Type: application/json\" \\\n  -d '{\"jsonrpc\":\"2.0\",\"method\":\"eth_blockNumber\",\"params\":[],\"id\":1}'",
        desc = "Vollständige EVM-Kompatibilität für Smart Contracts, ERC-20 Tokens und dezentrale dApps. Jederzeit mit eigenen privaten Knotenpunkten überschreibbar."
      ),
      UserRpcTemplate(
        chainName = "Bitcoin (PoW & Ordinals)",
        symbol = "BTC",
        defaultRpc = "https://btc.rfof-network.org/rpc/v1",
        chainIdHex = "Mainnet (PoW)",
        explorerUrl = "https://mempool.space",
        curlExample = "curl --user user:password --data-binary '{\"jsonrpc\":\"1.0\",\"id\":\"curltext\",\"method\":\"getblockchaininfo\",\"params\":[]}' \\\n  -H 'content-type:text/plain;' https://btc.rfof-network.org/rpc/v1",
        desc = "UTXO-Schnittstelle und Bitcoind JSON-RPC zur Verknüpfung eigener Wallets, Transaktionssignierung und Beglaubigung gegen den PoW-Mutteranker."
      ),
      UserRpcTemplate(
        chainName = "TON (The Open Network)",
        symbol = "TON",
        defaultRpc = "https://toncenter.com/api/v2/jsonRPC",
        chainIdHex = "TON Mainnet",
        explorerUrl = "https://tonscan.org",
        curlExample = "curl -X POST https://toncenter.com/api/v2/jsonRPC \\\n  -H \"Content-Type: application/json\" \\\n  -d '{\"id\":\"1\",\"jsonrpc\":\"2.0\",\"method\":\"getMasterchainInfo\",\"params\":{}}'",
        desc = "Hochskalierende Multi-Shard Blockchain. Direkte Bindung für Telegram- und Web3-Micropayments, Jettons und dezentrale DNS-Registrierungen."
      ),
      UserRpcTemplate(
        chainName = "ZON Sovereign Chain",
        symbol = "ZON / MTK",
        defaultRpc = "https://rpc.zon-chain.network/v1",
        chainIdHex = "0x7A0N (31245)",
        explorerUrl = "https://explorer.rfof-network.org",
        curlExample = "curl https://rpc.zon-chain.network/v1 \\\n  -X POST \\\n  -H \"Content-Type: application/json\" \\\n  -d '{\"jsonrpc\":\"2.0\",\"method\":\"zon_getBondingStatus\",\"params\":[\"MTK\"],\"id\":1}'",
        desc = "Das native Herzstück mit 50/50 Exponential Bonding Curve. Alle Transaktionsgebühren fließen autonom in den Liquiditätspool."
      ),
      UserRpcTemplate(
        chainName = "Benutzerdefinierte EVM (Arbitrum, Optimism, Polygon, BNB)",
        symbol = "Custom EVM",
        defaultRpc = "https://your-custom-rpc.provider.com",
        chainIdHex = "Custom (z.B. 42161, 10, 137)",
        explorerUrl = "https://your-explorer.io",
        curlExample = "curl https://your-custom-rpc.provider.com \\\n  -X POST \\\n  -H \"Content-Type: application/json\" \\\n  -d '{\"jsonrpc\":\"2.0\",\"method\":\"net_version\",\"params\":[],\"id\":1}'",
        desc = "Verbinde beliebige EVM-kompatible L2-Rollups oder Sidechains direkt mit deinen eigenen Code-Repositories und Smart-Contract-Deployments."
      )
    )
  }

  Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
    // 1. Hero Card: User Empowering Guide
    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(containerColor = BlueprintNavy),
      shape = RoundedCornerShape(14.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Terminal, contentDescription = null, tint = UrkundeGold, modifier = Modifier.size(24.dp))
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "ENTWICKLER- & NUTZER-LEITFADEN",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black,
            color = Color.White
          )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Willkommen in deiner souveränen Entwickler-Zentrale. Hier erfährst du schrittweise, wie du deine eigenen Code-Repositories mit Alchemy, Ethereum, Bitcoin, TON und ZON verknüpfst, eigene RPC-Endpunkte hinterlegst und vollwertige EVM-Smart-Contracts startest.",
          fontSize = 11.sp,
          color = Color(0xFFCBD5E1),
          lineHeight = 16.sp
        )
      }
    }

    // 2. Step 1: Eigene Repositories anlegen und mit Chains verknüpfen
    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      border = BorderStroke(1.dp, BlueprintBorder),
      shape = RoundedCornerShape(12.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            shape = CircleShape,
            color = SignalBlueLight,
            modifier = Modifier.size(26.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Text("1", fontWeight = FontWeight.Black, color = SignalBlue, fontSize = 12.sp)
            }
          }
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "CODE-REPOSITORIES NATIV VERKNÜPFEN",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = BlueprintNavy
          )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Du kannst im Tab 'Profil & Repos' jederzeit neue Quellcode-Repositories veröffentlichen. Jedes Repository erhält:\n\n" +
            "• Eindeutige Commit-Hashes: Kryptographisch signiert mit SHA-256 / AES-256.\n" +
            "• RFOF Pages URL: Automatische Bereitstellung deines Codes unter https://rfof-network.github.io/<repo-name>.\n" +
            "• Rechtsform-Wahl: Wähle zwischen Urheberrecht (©), GbR, eGbR, gemeinnütziger geGbR oder autarker Treuhandstiftung.\n" +
            "• NEC-Urkunden-Bindung: Kopple deine Code-Releases direkt mit einer notariellen Urkunde für rechtssicheren IP-Schutz.",
          fontSize = 11.sp,
          color = BlueprintNavy,
          lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(10.dp))
        Surface(
          color = Slate50,
          shape = RoundedCornerShape(8.dp),
          border = BorderStroke(1.dp, Slate200),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(10.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text("Git Remote hinzufügen (Terminal):", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = BlueprintNavy)
              TextButton(
                onClick = {
                  onCopy(
                    "git remote add rfof https://git.rfof-network.org/users/YOUR_HANDLE/my-smart-contract.git\ngit push rfof main",
                    "Git Remote Befehl"
                  )
                },
                contentPadding = PaddingValues(0.dp)
              ) {
                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(12.dp))
                Spacer(modifier = Modifier.width(3.dp))
                Text("Kopieren", fontSize = 10.sp)
              }
            }
            Text(
              text = "git remote add rfof https://git.rfof-network.org/users/YOUR_HANDLE/my-smart-contract.git\ngit push rfof main",
              fontFamily = FontFamily.Monospace,
              fontSize = 9.sp,
              color = SignalBlue
            )
          }
        }
      }
    }

    // 3. Step 2: Alchemy Multi-Chain Key Integration
    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      border = BorderStroke(1.dp, BlueprintBorder),
      shape = RoundedCornerShape(12.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            shape = CircleShape,
            color = SignalGreenLight,
            modifier = Modifier.size(26.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Text("2", fontWeight = FontWeight.Black, color = SignalGreen, fontSize = 12.sp)
            }
          }
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "ALCHEMY MULTI-CHAIN SERVICES VERBINDEN",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = BlueprintNavy
          )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Alchemy bietet 14 professionelle Microservices für Entwickler. Um deinen eigenen Alchemy API-Key zu nutzen:\n\n" +
            "1. Registriere dich kostenfrei auf alchemy.com und erstelle eine App für deine gewünschte Chain (ETH, Polygon, Arbitrum, Base).\n" +
            "2. Kopiere deinen persönlichen API-Key oder die HTTPS-RPC-URL.\n" +
            "3. Hinterlege den Key in deinen Projektdateien oder direkt in den Entwickler-Settings als Environment-Variable ALCHEMY_API_KEY.\n" +
            "4. Nutze die vorgefertigten Endpunkte für Transfers, Token Balances, NFT Metadata und Smart-Contract-Simulationen.",
          fontSize = 11.sp,
          color = BlueprintNavy,
          lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(10.dp))
        Surface(
          color = Slate50,
          shape = RoundedCornerShape(8.dp),
          border = BorderStroke(1.dp, Slate200),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(10.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text("Alchemy SDK Initialisierung:", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = BlueprintNavy)
              TextButton(
                onClick = {
                  val snippet = "import { Alchemy, Network } from \"alchemy-sdk\";\n\nconst config = {\n  apiKey: \"YOUR_ALCHEMY_API_KEY\",\n  network: Network.ETH_MAINNET,\n};\nconst alchemy = new Alchemy(config);\nconst latestBlock = await alchemy.core.getBlockNumber();"
                  onCopy(snippet, "Alchemy SDK Code")
                },
                contentPadding = PaddingValues(0.dp)
              ) {
                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(12.dp))
                Spacer(modifier = Modifier.width(3.dp))
                Text("Kopieren", fontSize = 10.sp)
              }
            }
            Text(
              text = "const alchemy = new Alchemy({ apiKey: \"YOUR_API_KEY\", network: Network.ETH_MAINNET });\nconst blockNumber = await alchemy.core.getBlockNumber();",
              fontFamily = FontFamily.Monospace,
              fontSize = 9.sp,
              color = Color(0xFF0F766E)
            )
          }
        }
      }
    }

    // 4. Step 3: Multi-Chain RPC-Verbindung & EVMs (ETH, BTC, TON, ZON)
    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      border = BorderStroke(1.dp, SignalBlue),
      shape = RoundedCornerShape(12.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            shape = CircleShape,
            color = SignalBlueLight,
            modifier = Modifier.size(26.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Text("3", fontWeight = FontWeight.Black, color = SignalBlue, fontSize = 12.sp)
            }
          }
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "MULTI-CHAIN RPCS & EVM NETZWERKE",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = BlueprintNavy
          )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Du kannst beliebige RPC-Knotenpunkte für alle 4 Kern-Netzwerke sowie eigene EVM-Chains hinterlegen. Klicke auf ein Netzwerk, um cURL-Snippets oder Endpunkte zu kopieren:",
          fontSize = 11.sp,
          color = BlueprintNavy,
          lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // RPC Cards
        userRpcTemplates.forEach { tpl ->
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Slate50),
            border = BorderStroke(1.dp, Slate200),
            shape = RoundedCornerShape(8.dp)
          ) {
            Column(modifier = Modifier.padding(10.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                  Box(modifier = Modifier.size(8.dp).background(SignalGreen, CircleShape))
                  Text(tpl.chainName, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = BlueprintNavy)
                }
                Surface(color = Slate200, shape = RoundedCornerShape(4.dp)) {
                  Text(tpl.symbol, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp), color = BlueprintNavy)
                }
              }

              Spacer(modifier = Modifier.height(4.dp))
              Text(tpl.desc, fontSize = 10.sp, color = TextSecondary, lineHeight = 14.sp)
              Spacer(modifier = Modifier.height(6.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "RPC: ${tpl.defaultRpc}",
                  fontFamily = FontFamily.Monospace,
                  fontSize = 9.sp,
                  color = SignalBlue,
                  maxLines = 1,
                  modifier = Modifier.weight(1f)
                )
                TextButton(
                  onClick = { onCopy(tpl.curlExample, "${tpl.chainName} RPC cURL") },
                  contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                ) {
                  Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(12.dp))
                  Spacer(modifier = Modifier.width(3.dp))
                  Text("cURL", fontSize = 10.sp)
                }
              }
            }
          }
        }
      }
    }

    // 5. Step 4: Eigene Token-Launches & Staking/Mining Pools
    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      border = BorderStroke(1.dp, UrkundeGold),
      shape = RoundedCornerShape(12.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            shape = CircleShape,
            color = UrkundeGoldBg,
            modifier = Modifier.size(26.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Text("4", fontWeight = FontWeight.Black, color = UrkundeGoldDark, fontSize = 12.sp)
            }
          }
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "EIGENE TOKENS & LIQUIDITÄTSPOOLS LAUNCHEN",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = BlueprintNavy
          )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Jeder Nutzer kann eigene Token-Erweiterungen (ZON-Subtokens oder EVM-Contracts) starten:\n\n" +
            "• Mining/Staking Pools ab 20.000 €: Sobald eine gesicherte Liquidität von mindestens 20.000 € hinterlegt ist, kann ein eigener dezentraler Staking-Pool mit automatisierter Gebührenverteilung eröffnet werden.\n" +
            "• Transaktionsgebühren-Arbitrage: Im System fließen Transaktionsgebühren unumkehrbar in die Liquidität, wodurch der innere Wert mathematisch kontinuierlich wächst.\n" +
            "• Multi-Chain Brücken (Bridge): Verknüpfe deine ZON-Guthaben direkt mit ERC-20 (Ethereum), Ordinals (Bitcoin) und Jettons (TON).\n" +
            "• Echtzeit-Explorer: Überprüfe jederzeit deine eigenen Transaktionen im Explorer-Tab unter 'Meine Aktivitäten'.",
          fontSize = 11.sp,
          color = BlueprintNavy,
          lineHeight = 16.sp
        )
      }
    }
  }
}
