package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

object DeFiRepository {

  private val initialTokens = listOf(
    LaunchedToken(
      id = "tok-zon",
      name = "ZON Universal Currency",
      symbol = "ZON",
      creatorAddress = "0x892a...RFOF_SYSTEM",
      totalSupply = 1_000_000.0,
      circulatingSupply = 425_000.0,
      priceEur = 1.48,
      liquidityEur = 629_000.0,
      isMiningSupported = true,
      isStakingSupported = true,
      iconEmoji = "🟢",
      creationDate = "23.09.2026",
      extensionOf = "NEC_GENESIS"
    ),
    LaunchedToken(
      id = "tok-sol",
      name = "Solaris Clean Energy Grid",
      symbol = "SOLAR",
      creatorAddress = "0x44f1...COMMUNITY",
      totalSupply = 500_000.0,
      circulatingSupply = 120_000.0,
      priceEur = 0.85,
      liquidityEur = 102_000.0,
      isMiningSupported = true,
      isStakingSupported = true,
      iconEmoji = "☀️",
      creationDate = "22.09.2026",
      extensionOf = "ZON"
    ),
    LaunchedToken(
      id = "tok-bio",
      name = "Biotech Regenerative Yield",
      symbol = "BIOX",
      creatorAddress = "0x91c0...RESEARCH",
      totalSupply = 250_000.0,
      circulatingSupply = 85_000.0,
      priceEur = 2.30,
      liquidityEur = 195_500.0,
      isMiningSupported = false,
      isStakingSupported = true,
      iconEmoji = "🧬",
      creationDate = "20.09.2026",
      extensionOf = "ZON"
    )
  )

  private val initialStakingPools = listOf(
    CommunityStakingPool(
      poolId = "pool-zon-alpha",
      tokenSymbol = "ZON",
      tokenName = "ZON Genesis High-Yield Pool",
      totalStaked = 210_000.0,
      apyPercent = 14.8,
      minStake = 10.0,
      lockPeriodDays = 30,
      participantsCount = 142,
      liquidityBackedEur = 310_800.0
    ),
    CommunityStakingPool(
      poolId = "pool-solar-green",
      tokenSymbol = "SOLAR",
      tokenName = "Solaris Photovoltaik Stake",
      totalStaked = 75_000.0,
      apyPercent = 11.5,
      minStake = 50.0,
      lockPeriodDays = 60,
      participantsCount = 68,
      liquidityBackedEur = 63_750.0
    ),
    CommunityStakingPool(
      poolId = "pool-biox-yield",
      tokenSymbol = "BIOX",
      tokenName = "Biotech Cell-Harvest Pool",
      totalStaked = 40_000.0,
      apyPercent = 18.2,
      minStake = 25.0,
      lockPeriodDays = 90,
      participantsCount = 49,
      liquidityBackedEur = 92_000.0
    )
  )

  private val initialMiningPools = listOf(
    CommunityMiningPool(
      poolId = "mine-zon-jk",
      tokenSymbol = "ZON",
      algorithm = "JK Flip-Flop-Flap / SHA-256",
      hashrateGh = 14820.5,
      activeMiners = 312,
      blockReward = 12.5,
      difficulty = 4.82,
      liquidityBackedEur = 629_000.0
    ),
    CommunityMiningPool(
      poolId = "mine-solar-grid",
      tokenSymbol = "SOLAR",
      algorithm = "Proof of Useful Work (PoUW)",
      hashrateGh = 4210.0,
      activeMiners = 84,
      blockReward = 5.0,
      difficulty = 2.10,
      liquidityBackedEur = 102_000.0
    )
  )

  private val initialNfts = listOf(
    NftCertificate(
      tokenId = "NEC-NFT #001",
      title = "Montalkanio Notarial Stamm-Urkunde",
      urkundenNummer = "URK-NR. 1892/2026",
      notarialSealDate = "15.01.2026",
      ownerAddress = "0x892a...RFOF_ADMIN",
      priceZon = 2500.0,
      ipfsCid = "ipfs://QmXoypizjW3WknFiJnKLwHCnL72vedxjQkDDP1mXWo6uco",
      categoryId = 1,
      rechtsform = "©"
    ),
    NftCertificate(
      tokenId = "NEC-NFT #002",
      title = "Souveräne 8-Sichten Matrix Patenturkunde",
      urkundenNummer = "URK-NR. 1893/2026",
      notarialSealDate = "02.02.2026",
      ownerAddress = "0x892a...RFOF_ADMIN",
      priceZon = 1800.0,
      ipfsCid = "ipfs://QmZtmD2qtNmR4ndx2Lp1c2sFvFpGqHwT4xY1m9kLk7yP9A",
      categoryId = 2,
      rechtsform = "GbR"
    ),
    NftCertificate(
      tokenId = "NEC-NFT #003",
      title = "XJustiz Grundbuch-Treuhand Zertifikat",
      urkundenNummer = "URK-NR. 1894/2026",
      notarialSealDate = "10.03.2026",
      ownerAddress = "0x892a...RFOF_ADMIN",
      priceZon = 3200.0,
      ipfsCid = "ipfs://QmYwAPJzv5CZsnA625s3Xf2nemtYgPpHdWEz79ojWnPbdG",
      categoryId = 3,
      rechtsform = "eGbR"
    )
  )

  private val _tokens = MutableStateFlow<List<LaunchedToken>>(initialTokens)
  val tokens: StateFlow<List<LaunchedToken>> = _tokens.asStateFlow()

  private val _stakingPools = MutableStateFlow<List<CommunityStakingPool>>(initialStakingPools)
  val stakingPools: StateFlow<List<CommunityStakingPool>> = _stakingPools.asStateFlow()

  private val _miningPools = MutableStateFlow<List<CommunityMiningPool>>(initialMiningPools)
  val miningPools: StateFlow<List<CommunityMiningPool>> = _miningPools.asStateFlow()

  private val _nfts = MutableStateFlow<List<NftCertificate>>(initialNfts)
  val nfts: StateFlow<List<NftCertificate>> = _nfts.asStateFlow()

  // Mathematical 50/50 Exponential Bonding Curve calculation
  fun calculateBondingPriceAndSupply(currentLiquidity: Double, incomingFeeEur: Double): Pair<Double, Double> {
    val newLiquidity = currentLiquidity + incomingFeeEur
    // 50% price growth component, 50% supply minting component
    val priceEur = if (newLiquidity <= 0) 0.0 else Math.sqrt(newLiquidity) * 0.05
    val mintedSupply = if (newLiquidity <= 0) 0.0 else (newLiquidity / (priceEur + 0.001)) * 0.5
    return Pair(priceEur, mintedSupply)
  }

  fun launchNewToken(
    name: String,
    symbol: String,
    creatorAddress: String,
    initialLiquidityDeposit: Double,
    isMining: Boolean,
    isStaking: Boolean,
    iconEmoji: String
  ): LaunchedToken {
    val dateStr = SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN).format(Date())
    val (calcPrice, calcSupply) = calculateBondingPriceAndSupply(0.0, initialLiquidityDeposit)
    val token = LaunchedToken(
      id = "tok-${symbol.lowercase()}-${System.currentTimeMillis()}",
      name = name,
      symbol = symbol.uppercase(),
      creatorAddress = creatorAddress,
      totalSupply = Math.max(calcSupply, 1000.0),
      circulatingSupply = Math.max(calcSupply * 0.3, 300.0),
      priceEur = Math.max(calcPrice, 0.10),
      liquidityEur = initialLiquidityDeposit,
      isMiningSupported = isMining,
      isStakingSupported = isStaking,
      iconEmoji = iconEmoji.ifBlank { "🪙" },
      creationDate = dateStr,
      extensionOf = "ZON"
    )
    _tokens.value = listOf(token) + _tokens.value

    // If liquidity reaches 20,000 EUR threshold, automatically unlock pools!
    if (initialLiquidityDeposit >= 20_000.0) {
      if (isStaking) {
        val pool = CommunityStakingPool(
          poolId = "pool-${token.symbol.lowercase()}",
          tokenSymbol = token.symbol,
          tokenName = "${token.name} High-Yield Pool",
          totalStaked = token.circulatingSupply * 0.4,
          apyPercent = 12.0,
          minStake = 10.0,
          lockPeriodDays = 30,
          participantsCount = 1,
          liquidityBackedEur = initialLiquidityDeposit
        )
        _stakingPools.value = listOf(pool) + _stakingPools.value
      }
      if (isMining) {
        val minePool = CommunityMiningPool(
          poolId = "mine-${token.symbol.lowercase()}",
          tokenSymbol = token.symbol,
          algorithm = "JK Flip-Flop-Flap / SHA-256",
          hashrateGh = 1250.0,
          activeMiners = 5,
          blockReward = 10.0,
          difficulty = 1.8,
          liquidityBackedEur = initialLiquidityDeposit
        )
        _miningPools.value = listOf(minePool) + _miningPools.value
      }
    }
    return token
  }

  fun createPoolIfLiquidityEligible(tokenSymbol: String, isStaking: Boolean): Pair<Boolean, String> {
    val token = _tokens.value.find { it.symbol.equals(tokenSymbol, ignoreCase = true) }
      ?: return Pair(false, "Token $tokenSymbol nicht gefunden.")

    if (token.liquidityEur < 20_000.0) {
      return Pair(
        false,
        "Liquiditätsschwelle nicht erreicht: Aktuell ${String.format(Locale.GERMANY, "%,.2f €", token.liquidityEur)} von erforderlichen 20.000,00 €!"
      )
    }

    if (isStaking) {
      val existing = _stakingPools.value.find { it.tokenSymbol == token.symbol }
      if (existing != null) return Pair(false, "Staking-Pool für ${token.symbol} existiert bereits.")
      val pool = CommunityStakingPool(
        poolId = "pool-${token.symbol.lowercase()}",
        tokenSymbol = token.symbol,
        tokenName = "${token.name} Staking Pool",
        totalStaked = 5000.0,
        apyPercent = 10.5,
        minStake = 10.0,
        lockPeriodDays = 30,
        participantsCount = 1,
        liquidityBackedEur = token.liquidityEur
      )
      _stakingPools.value = listOf(pool) + _stakingPools.value
      return Pair(true, "Staking-Pool für ${token.symbol} erfolgreich eröffnet!")
    } else {
      val existing = _miningPools.value.find { it.tokenSymbol == token.symbol }
      if (existing != null) return Pair(false, "Mining-Pool für ${token.symbol} existiert bereits.")
      val pool = CommunityMiningPool(
        poolId = "mine-${token.symbol.lowercase()}",
        tokenSymbol = token.symbol,
        algorithm = "JK Flip-Flop-Flap / SHA-256",
        hashrateGh = 2500.0,
        activeMiners = 10,
        blockReward = 8.0,
        difficulty = 2.5,
        liquidityBackedEur = token.liquidityEur
      )
      _miningPools.value = listOf(pool) + _miningPools.value
      return Pair(true, "Mining-Pool für ${token.symbol} erfolgreich eröffnet!")
    }
  }

  fun wrapTokenPreLaunch(tokenSymbol: String, targetChain: CryptoChain, amount: Double): String {
    return "Wrapping erfolgreich: $amount $tokenSymbol wurden über das ZON-Bridge Protokoll auf ${targetChain.fullName} gewrappt (TxHash: 0x${UUID.randomUUID().toString().replace("-", "").take(16)}...)."
  }
}
