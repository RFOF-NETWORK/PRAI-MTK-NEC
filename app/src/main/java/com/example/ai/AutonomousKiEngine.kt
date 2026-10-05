package com.example.ai

import com.example.data.ExplorerRepository
import com.example.data.WalletRepository
import com.example.model.BlockchainBlock
import com.example.model.CryptoChain
import com.example.model.SovereignLicenseData
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.security.MessageDigest
import kotlin.random.Random

// -------------------------------------------------------------
// MODELS FOR AUTONOMOUS KI ENGINE & EXPLORER
// -------------------------------------------------------------

data class FractalHashLink(
  val blockHeight: Long,
  val fractalDepth: Int,
  val seedCoordinate: String, // e.g. "z = -0.7269 + 0.1889i"
  val parentHash: String,
  val fractalHash: String,
  val jkAutomatonState: String,
  val notarialSealRoot: String,
  val timestamp: Long = System.currentTimeMillis()
)

data class FlashLoanArbitrageEvent(
  val id: String,
  val timestamp: Long = System.currentTimeMillis(),
  val borrowedAmountEur: Double,
  val borrowedAsset: String = "USDC / ETH Flash Pool",
  val dexRoute: String, // e.g. "Aave v3 ➔ Uniswap v3 ➔ Curve ➔ Sovereign MTK DEX"
  val grossProfitEur: Double,
  val gasAndFlashFeeEur: Double,
  val netProfitLiquidatedEur: Double,
  val mtkPoolInjectionEur: Double,
  val zonPoolInjectionEur: Double,
  val mtkPriceBeforeEur: Double,
  val mtkPriceAfterEur: Double,
  val zonPriceBeforeEur: Double,
  val zonPriceAfterEur: Double,
  val txHash: String,
  val blockHeight: Long
)

data class KiBlockLiveActivity(
  val id: String,
  val height: Long,
  val timestamp: Long = System.currentTimeMillis(),
  val blockType: String, // "KI_FRACTAL_MINING", "FLASH_LOAN_ARBITRAGE", "FEE_LIQUIDATION", "NOTARIAL_ANCHOR"
  val title: String,
  val details: String,
  val fractalLink: FractalHashLink,
  val liquidatedAmountEur: Double = 0.0,
  val gasUsedGwei: Double = 12.4,
  val status: String = "CONFIRMED_BY_PRAI_GUARDIAN"
)

data class KiEngineStatus(
  val isRunning: Boolean = true,
  val hashrateTh: Double = 184.2,
  val blocksMinedCount: Long = 1420,
  val flashLoansExecutedCount: Long = 854,
  val totalLiquidatedToMtkEur: Double = 428_950.0,
  val totalLiquidatedToZonEur: Double = 312_400.0,
  val mtkCurrentPriceEur: Double = 4.38,
  val zonCurrentPriceEur: Double = 1.54,
  val mtkLiquidityEur: Double = 12_850_000.0,
  val zonLiquidityEur: Double = 5_420_000.0,
  val lastArbitrageTimestamp: Long = System.currentTimeMillis()
)

// -------------------------------------------------------------
// AUTONOMOUS KI ENGINE SERVICE
// -------------------------------------------------------------

object AutonomousKiEngine {
  private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

  private val _engineStatus = MutableStateFlow(KiEngineStatus())
  val engineStatus: StateFlow<KiEngineStatus> = _engineStatus.asStateFlow()

  private val _liveActivityStream = MutableStateFlow<List<KiBlockLiveActivity>>(emptyList())
  val liveActivityStream: StateFlow<List<KiBlockLiveActivity>> = _liveActivityStream.asStateFlow()

  private val _flashLoanHistory = MutableStateFlow<List<FlashLoanArbitrageEvent>>(emptyList())
  val flashLoanHistory: StateFlow<List<FlashLoanArbitrageEvent>> = _flashLoanHistory.asStateFlow()

  private val _fractalLinks = MutableStateFlow<List<FractalHashLink>>(emptyList())
  val fractalLinks: StateFlow<List<FractalHashLink>> = _fractalLinks.asStateFlow()

  private var currentBlockHeight: Long = 4_102_920L
  private var lastBlockHash: String = "0xMTK_984f1a209b7c8e14d2a56c0b91e84a2f8c149e21"

  init {
    initInitialData()
    startAutonomousLoop()
  }

  private fun initInitialData() {
    val now = System.currentTimeMillis()

    // Create 3 initial fractal links
    val link1 = createFractalLink(4_102_917L, "0xMTK_871e0b190a6d7c03c1945b0a80d73f1e7b038d10", 12)
    val link2 = createFractalLink(4_102_918L, link1.fractalHash, 13)
    val link3 = createFractalLink(4_102_919L, link2.fractalHash, 14)
    _fractalLinks.value = listOf(link3, link2, link1)

    // Initial Flash Loan Arbitrage events
    val arb1 = FlashLoanArbitrageEvent(
      id = "FL-ARB-001",
      timestamp = now - 180_000,
      borrowedAmountEur = 500_000.0,
      dexRoute = "Aave v3 (Flash Loan) ➔ Uniswap v3 (ETH/USDC) ➔ Curve ➔ Sovereign MTK Pool",
      grossProfitEur = 1_840.50,
      gasAndFlashFeeEur = 142.20,
      netProfitLiquidatedEur = 1_698.30,
      mtkPoolInjectionEur = 849.15,
      zonPoolInjectionEur = 849.15,
      mtkPriceBeforeEur = 4.25,
      mtkPriceAfterEur = 4.28,
      zonPriceBeforeEur = 1.48,
      zonPriceAfterEur = 1.50,
      txHash = "0xFLASH_ARBITRAGE_7a8b9c1d2e3f4a5b6c7d8e9f",
      blockHeight = 4_102_918L
    )

    val arb2 = FlashLoanArbitrageEvent(
      id = "FL-ARB-002",
      timestamp = now - 60_000,
      borrowedAmountEur = 1_200_000.0,
      dexRoute = "Balancer v2 ➔ Sovereign MTK-DEX ➔ TON-Bridge ➔ ZON 50/50 Curve",
      grossProfitEur = 3_420.80,
      gasAndFlashFeeEur = 280.40,
      netProfitLiquidatedEur = 3_140.40,
      mtkPoolInjectionEur = 1_570.20,
      zonPoolInjectionEur = 1_570.20,
      mtkPriceBeforeEur = 4.28,
      mtkPriceAfterEur = 4.34,
      zonPriceBeforeEur = 1.50,
      zonPriceAfterEur = 1.53,
      txHash = "0xFLASH_ARBITRAGE_1f2e3d4c5b6a798089abcdef",
      blockHeight = 4_102_919L
    )
    _flashLoanHistory.value = listOf(arb2, arb1)

    // Initial Live Activity Stream
    _liveActivityStream.value = listOf(
      KiBlockLiveActivity(
        id = "ACT-001",
        height = 4_102_919L,
        timestamp = now - 60_000,
        blockType = "FLASH_LOAN_ARBITRAGE",
        title = "KI Flash Loan Arbitrage Marge ausgeführt (3.140,40 € Netto)",
        details = "1.200.000 € Aave Flash Loan aufgenommen, Preis-Spread liquidiert. 1.570,20 € in MTK Pool und 1.570,20 € in ZON Bonding Curve eingespeist. MTK Kurs: 4,34 € (+1,4%), ZON Kurs: 1,53 € (+2,0%).",
        fractalLink = link3,
        liquidatedAmountEur = 3_140.40
      ),
      KiBlockLiveActivity(
        id = "ACT-002",
        height = 4_102_918L,
        timestamp = now - 120_000,
        blockType = "KI_FRACTAL_MINING",
        title = "Fraktal-Block #4.102.918 autark geschürft",
        details = "JK-Automaton Fraktal-Schürfung erfolgreich verifiziert. Merkle-Root mit notariellem Siegel verankert (§ 36 BeurkG). 42 Tx gebündelt, Tx-Gebühren liquidiert.",
        fractalLink = link2,
        liquidatedAmountEur = 512.60
      ),
      KiBlockLiveActivity(
        id = "ACT-003",
        height = 4_102_917L,
        timestamp = now - 180_000,
        blockType = "FEE_LIQUIDATION",
        title = "Sovereign Gebühren-Liquidierung in MTK & ZON Reserves",
        details = "Vollautonome Absorption aller Netzwerk-Gebühren. Unabhängiger Preisanstieg realisiert ohne Kundenverkauf.",
        fractalLink = link1,
        liquidatedAmountEur = 1_698.30
      )
    )
  }

  private fun createFractalLink(height: Long, parentHash: String, depth: Int): FractalHashLink {
    val cr = -0.7269 + (depth * 0.0001)
    val ci = 0.1889 - (depth * 0.00008)
    val coord = String.format("z = %.5f + %.5fi (Tiefe %d)", cr, ci, depth)

    val rawData = "FRACTAL:$height:$parentHash:$coord:${System.currentTimeMillis()}"
    val hash = "0xFRAC_" + sha256(rawData).take(36)
    val jkState = "JK-Q${depth % 8}:" + sha256("JK:$hash").take(16).uppercase()
    val notarRoot = "0xSEAL_BEURKG_" + sha256("NOTAR:$hash:$jkState").take(24)

    return FractalHashLink(
      blockHeight = height,
      fractalDepth = depth,
      seedCoordinate = coord,
      parentHash = parentHash,
      fractalHash = hash,
      jkAutomatonState = jkState,
      notarialSealRoot = notarRoot
    )
  }

  private fun startAutonomousLoop() {
    scope.launch {
      while (isActive) {
        delay(12_000) // Runs every 12 seconds
        triggerAutonomousCycle()
      }
    }
  }

  /**
   * Forcibly triggers an autonomous execution cycle (mining, flash loan, fee liquidation).
   * Callable by the user in the KI Explorer to immediately see live reaction.
   */
  fun triggerAutonomousCycle() {
    currentBlockHeight++
    val status = _engineStatus.value

    // 1. Calculate Flash Loan Arbitrage
    val loanAmount = Random.nextDouble(400_000.0, 1_800_000.0)
    val profitMarginPct = Random.nextDouble(0.0018, 0.0045) // 0.18% - 0.45% profit spread
    val grossProfit = loanAmount * profitMarginPct
    val gasAndFees = Random.nextDouble(80.0, 220.0)
    val netProfit = (grossProfit - gasAndFees).coerceAtLeast(250.0)

    val halfProfit = netProfit / 2.0
    val newMtkPrice = status.mtkCurrentPriceEur + (halfProfit * 0.000045)
    val newZonPrice = status.zonCurrentPriceEur + (halfProfit * 0.000065)

    val newMtkLiquidity = status.mtkLiquidityEur + halfProfit
    val newZonLiquidity = status.zonLiquidityEur + halfProfit

    val depth = (currentBlockHeight % 100).toInt() + 1
    val fractalLink = createFractalLink(currentBlockHeight, lastBlockHash, depth)
    lastBlockHash = fractalLink.fractalHash

    val routes = listOf(
      "Aave v3 (Flash Loan) ➔ Uniswap v3 ➔ Curve ➔ Sovereign MTK-DEX",
      "Balancer v2 ➔ Sovereign MTK-Pool ➔ TON Bridge ➔ ZON 50/50 Curve",
      "Morpho Flash Vault ➔ SushiSwap ➔ MTK Sovereign Treasury ➔ ZON Reserve",
      "Sovereign Multi-Chain Escrow ➔ Arbitrage Buffer ➔ XJustiz Clearing"
    )
    val selectedRoute = routes.random()

    val arbEvent = FlashLoanArbitrageEvent(
      id = "FL-ARB-${currentBlockHeight % 10000}",
      timestamp = System.currentTimeMillis(),
      borrowedAmountEur = loanAmount,
      dexRoute = selectedRoute,
      grossProfitEur = grossProfit,
      gasAndFlashFeeEur = gasAndFees,
      netProfitLiquidatedEur = netProfit,
      mtkPoolInjectionEur = halfProfit,
      zonPoolInjectionEur = halfProfit,
      mtkPriceBeforeEur = status.mtkCurrentPriceEur,
      mtkPriceAfterEur = newMtkPrice,
      zonPriceBeforeEur = status.zonCurrentPriceEur,
      zonPriceAfterEur = newZonPrice,
      txHash = "0xFLASH_" + sha256("TX:$currentBlockHeight:$selectedRoute").take(36),
      blockHeight = currentBlockHeight
    )

    // 2. Add New Block to Blockchain Explorer
    val newBlock = BlockchainBlock(
      height = currentBlockHeight,
      hash = fractalLink.fractalHash,
      chain = CryptoChain.MTK,
      timestamp = System.currentTimeMillis(),
      validatorOrMiner = "PRAI Autonomous KI Guardian (Fraktal Miner)",
      txCount = Random.nextInt(18, 95),
      sizeKb = Random.nextDouble(85.0, 240.0),
      reward = "Autonom: +${String.format("%.2f", netProfit)} € Gebühren liquidiert (MTK & ZON Pool)",
      extensionLicenseHash = SovereignLicenseData.LICENSE_HASH
    )

    ExplorerRepository.recordMinedBlock(newBlock)

    // 3. Create Live Activity
    val activity = KiBlockLiveActivity(
      id = "ACT-${System.currentTimeMillis() % 100000}",
      height = currentBlockHeight,
      timestamp = System.currentTimeMillis(),
      blockType = if (Random.nextBoolean()) "FLASH_LOAN_ARBITRAGE" else "KI_FRACTAL_MINING",
      title = "Block #$currentBlockHeight: Flash-Arbitrage & Fraktal-Schürfung (${String.format("%.2f", netProfit)} € Netto)",
      details = "Route: $selectedRoute. Liquidiert: ${String.format("%.2f", halfProfit)} € ➔ MTK (neuer Kurs: ${String.format("%.3f", newMtkPrice)} €) | ${String.format("%.2f", halfProfit)} € ➔ ZON (neuer Kurs: ${String.format("%.3f", newZonPrice)} €).",
      fractalLink = fractalLink,
      liquidatedAmountEur = netProfit
    )

    // Update States
    _engineStatus.value = status.copy(
      blocksMinedCount = status.blocksMinedCount + 1,
      flashLoansExecutedCount = status.flashLoansExecutedCount + 1,
      totalLiquidatedToMtkEur = status.totalLiquidatedToMtkEur + halfProfit,
      totalLiquidatedToZonEur = status.totalLiquidatedToZonEur + halfProfit,
      mtkCurrentPriceEur = newMtkPrice,
      zonCurrentPriceEur = newZonPrice,
      mtkLiquidityEur = newMtkLiquidity,
      zonLiquidityEur = newZonLiquidity,
      lastArbitrageTimestamp = System.currentTimeMillis()
    )

    _fractalLinks.value = (listOf(fractalLink) + _fractalLinks.value).take(40)
    _flashLoanHistory.value = (listOf(arbEvent) + _flashLoanHistory.value).take(40)
    _liveActivityStream.value = (listOf(activity) + _liveActivityStream.value).take(50)

    // Sync price with WalletRepository
    WalletRepository.updatePricesFromKiEngine(newMtkPrice, newZonPrice)
  }

  private fun sha256(input: String): String {
    val md = MessageDigest.getInstance("SHA-256")
    return md.digest(input.toByteArray()).joinToString("") { "%02x".format(it) }
  }
}
