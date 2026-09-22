package com.example.data

import com.example.auth.AuthManager
import com.example.crypto.AESEncryption
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object WalletRepository {
  // Seed phrase for demo / local keystore
  private var currentSeedPhrase = listOf(
    "quantum", "shield", "matrix", "patent", "notary", "escrow",
    "sovereign", "montalkanio", "anchor", "circuit", "beacon", "liberty"
  )

  private val _assets = MutableStateFlow<List<CryptoAsset>>(emptyList())
  val assets: StateFlow<List<CryptoAsset>> = _assets.asStateFlow()

  private val _escrowState = MutableStateFlow(EscrowContractState())
  val escrowState: StateFlow<EscrowContractState> = _escrowState.asStateFlow()

  private val _stakingStates = MutableStateFlow<Map<CryptoChain, StakingState>>(
    mapOf(
      CryptoChain.ETH to StakingState(
        chain = CryptoChain.ETH,
        stakedAmount = 14.5,
        aprPercent = 3.8,
        rewardsAccumulated = 0.482,
        validatorPool = "Lido / PRAI Validator Cluster #3"
      ),
      CryptoChain.TON to StakingState(
        chain = CryptoChain.TON,
        stakedAmount = 2500.0,
        aprPercent = 4.6,
        rewardsAccumulated = 78.4,
        validatorPool = "TonWhales / RFOF Nominator Pool"
      )
    )
  )
  val stakingStates: StateFlow<Map<CryptoChain, StakingState>> = _stakingStates.asStateFlow()

  private val _miningState = MutableStateFlow(MiningState())
  val miningState: StateFlow<MiningState> = _miningState.asStateFlow()

  private val _transactions = MutableStateFlow<List<WalletTransaction>>(emptyList())
  val transactions: StateFlow<List<WalletTransaction>> = _transactions.asStateFlow()

  init {
    refreshAssetsForCurrentRole()
    initInitialTransactions()

    // Listen to real live price updates from network
    kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
      com.example.network.CryptoPriceService.marketData.collect { liveData ->
        refreshAssetsWithMarketData(liveData)
      }
    }
  }

  fun refreshAssetsForCurrentRole() {
    refreshAssetsWithMarketData(com.example.network.CryptoPriceService.marketData.value)
  }

  private fun refreshAssetsWithMarketData(liveMarket: Map<String, com.example.network.LiveMarketData>) {
    val isAdmin = AuthManager.isAdmin
    val derived = AESEncryption.deriveAddresses(currentSeedPhrase)

    val btcInfo = liveMarket["BTC"]
    val ethInfo = liveMarket["ETH"]
    val tonInfo = liveMarket["TON"]
    val mtkInfo = liveMarket["MTK"]

    val list = mutableListOf<CryptoAsset>()

    // MTK: ONLY the Admin (RFOF-NETWORK) holds native sovereign MTK.
    // Users hold BTC, ETH, TON and certificates so that the model remains paragraphically correct.
    if (isAdmin) {
      list.add(
        CryptoAsset(
          chain = CryptoChain.MTK,
          balance = 1_250_000.0,
          usdRate = mtkInfo?.priceUsd ?: 4.25,
          address = derived["MTK"] ?: "0xMTK_ADMIN",
          change24h = mtkInfo?.change24hPercent ?: +5.8,
          isRestrictedToAdmin = true
        )
      )
    }

    // BTC
    list.add(
      CryptoAsset(
        chain = CryptoChain.BTC,
        balance = if (isAdmin) 12.85 else 0.65,
        usdRate = btcInfo?.priceUsd ?: 64_200.0,
        address = derived["BTC"] ?: "bc1q_demo",
        change24h = btcInfo?.change24hPercent ?: +1.4
      )
    )

    // ETH
    list.add(
      CryptoAsset(
        chain = CryptoChain.ETH,
        balance = if (isAdmin) 84.2 else 4.15,
        usdRate = ethInfo?.priceUsd ?: 3_480.0,
        address = derived["ETH"] ?: "0x_demo",
        change24h = ethInfo?.change24hPercent ?: -0.8
      )
    )

    // TON
    list.add(
      CryptoAsset(
        chain = CryptoChain.TON,
        balance = if (isAdmin) 15_800.0 else 940.0,
        usdRate = tonInfo?.priceUsd ?: 5.60,
        address = derived["TON"] ?: "EQD_demo",
        change24h = tonInfo?.change24hPercent ?: +8.2
      )
    )

    _assets.value = list
  }

  private fun initInitialTransactions() {
    val now = System.currentTimeMillis()
    _transactions.value = listOf(
      WalletTransaction(
        id = "TX-001",
        txHash = "0x89f2c7a6e1248039d9b734892c5e7b218491a0c4",
        chain = CryptoChain.MTK,
        type = TxType.ESCROW_LOCK,
        amount = 50_000.0,
        fromAddress = "0xRFOF9842...321",
        toAddress = "ESCROW_TREUHAND_NOTARIAT",
        timestamp = now - 3600_000 * 2,
        note = "Notarielle Deckung nach § 36 BeurkG hinterlegt"
      ),
      WalletTransaction(
        id = "TX-002",
        txHash = "0x4b71a9320e8b7c4192084c7a529e81b6728c8914",
        chain = CryptoChain.ETH,
        type = TxType.DEPOSIT,
        amount = 2.5,
        fromAddress = "0xExtern...992",
        toAddress = "0xETH_WALLET",
        timestamp = now - 3600_000 * 14,
        note = "W3Connect EVM Deposit"
      ),
      WalletTransaction(
        id = "TX-003",
        txHash = "bc1q783b92418a09b2e7c4190823b7a1590483c1",
        chain = CryptoChain.BTC,
        type = TxType.WITHDRAW,
        amount = 0.12,
        fromAddress = "bc1q_WALLET",
        toAddress = "bc1qExternalColdVault",
        timestamp = now - 3600_000 * 28,
        note = "Auszahlung an Hardware-Wallet"
      ),
      WalletTransaction(
        id = "TX-004",
        txHash = "EQD4a918237b0192847c190823b471a9823b1092",
        chain = CryptoChain.TON,
        type = TxType.STAKE,
        amount = 500.0,
        fromAddress = "EQD_WALLET",
        toAddress = "TON_NOMINATOR_POOL",
        timestamp = now - 3600_000 * 52,
        note = "Nominator Pool Delegation (4.6% APY)"
      )
    )
  }

  fun deposit(chain: CryptoChain, amount: Double, note: String = "Deposit") {
    val tx = WalletTransaction(
      id = "TX-${System.currentTimeMillis() % 10000}",
      txHash = "0x" + AESEncryption.sha256("DEP:$chain:$amount:${System.currentTimeMillis()}").take(40),
      chain = chain,
      type = TxType.DEPOSIT,
      amount = amount,
      fromAddress = "External Source",
      toAddress = getAddressForChain(chain),
      timestamp = System.currentTimeMillis(),
      note = note
    )
    _transactions.value = listOf(tx) + _transactions.value

    _assets.value = _assets.value.map {
      if (it.chain == chain) it.copy(balance = it.balance + amount) else it
    }
  }

  fun withdraw(chain: CryptoChain, toAddress: String, amount: Double): Boolean {
    val current = _assets.value.find { it.chain == chain } ?: return false
    if (current.balance < amount) return false

    val tx = WalletTransaction(
      id = "TX-${System.currentTimeMillis() % 10000}",
      txHash = "0x" + AESEncryption.sha256("WTH:$chain:$amount:$toAddress").take(40),
      chain = chain,
      type = TxType.WITHDRAW,
      amount = amount,
      fromAddress = getAddressForChain(chain),
      toAddress = toAddress,
      timestamp = System.currentTimeMillis(),
      note = "Auszahlung autorisiert"
    )
    _transactions.value = listOf(tx) + _transactions.value

    _assets.value = _assets.value.map {
      if (it.chain == chain) it.copy(balance = it.balance - amount) else it
    }
    return true
  }

  fun swap(fromChain: CryptoChain, toChain: CryptoChain, amountFrom: Double): Double? {
    val fromAsset = _assets.value.find { it.chain == fromChain } ?: return null
    if (fromAsset.balance < amountFrom) return null

    val toAsset = _assets.value.find { it.chain == toChain } ?: return null

    val valueUsd = amountFrom * fromAsset.usdRate
    val amountTo = valueUsd / toAsset.usdRate

    val tx = WalletTransaction(
      id = "TX-${System.currentTimeMillis() % 10000}",
      txHash = "0x" + AESEncryption.sha256("SWAP:$fromChain:$toChain:$amountFrom").take(40),
      chain = fromChain,
      type = TxType.SWAP,
      amount = amountFrom,
      fromAddress = getAddressForChain(fromChain),
      toAddress = getAddressForChain(toChain),
      timestamp = System.currentTimeMillis(),
      note = "Tausch von ${fromChain.symbol} zu ${toChain.symbol} ($amountTo ${toChain.symbol})"
    )
    _transactions.value = listOf(tx) + _transactions.value

    _assets.value = _assets.value.map {
      when (it.chain) {
        fromChain -> it.copy(balance = it.balance - amountFrom)
        toChain -> it.copy(balance = it.balance + amountTo)
        else -> it
      }
    }
    return amountTo
  }

  fun toggleMining() {
    val current = _miningState.value
    _miningState.value = current.copy(isActive = !current.isActive)
  }

  fun stake(chain: CryptoChain, amount: Double): Boolean {
    val asset = _assets.value.find { it.chain == chain } ?: return false
    if (asset.balance < amount) return false

    val currentStaking = _stakingStates.value[chain] ?: return false
    _stakingStates.value = _stakingStates.value + (chain to currentStaking.copy(
      stakedAmount = currentStaking.stakedAmount + amount
    ))

    _assets.value = _assets.value.map {
      if (it.chain == chain) it.copy(balance = it.balance - amount) else it
    }
    return true
  }

  fun getSeedPhrase(): List<String> = currentSeedPhrase

  fun importSeedPhrase(words: List<String>) {
    if (words.size == 12) {
      currentSeedPhrase = words
      refreshAssetsForCurrentRole()
    }
  }

  private fun getAddressForChain(chain: CryptoChain): String {
    val derived = AESEncryption.deriveAddresses(currentSeedPhrase)
    return derived[chain.symbol] ?: "0x_demo"
  }
}
