package com.example.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class LiveMarketData(
  val symbol: String,
  val priceUsd: Double,
  val priceEur: Double,
  val change24hPercent: Double,
  val lastUpdated: Long = System.currentTimeMillis()
)

object CryptoPriceService {
  private const val TAG = "CryptoPriceService"

  private val client = OkHttpClient.Builder()
    .connectTimeout(10, TimeUnit.SECONDS)
    .readTimeout(10, TimeUnit.SECONDS)
    .build()

  private val _isOnline = MutableStateFlow(false)
  val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

  private val _marketData = MutableStateFlow<Map<String, LiveMarketData>>(
    mapOf(
      "BTC" to LiveMarketData("BTC", 64200.0, 59100.0, +1.4),
      "ETH" to LiveMarketData("ETH", 3480.0, 3205.0, -0.8),
      "TON" to LiveMarketData("TON", 5.65, 5.21, +3.2),
      "MTK" to LiveMarketData("MTK", 4.25, 3.91, +5.8)
    )
  )
  val marketData: StateFlow<Map<String, LiveMarketData>> = _marketData.asStateFlow()

  private val _isRefreshing = MutableStateFlow(false)
  val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

  fun initialize(context: Context) {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    if (connectivityManager != null) {
      val activeNetwork = connectivityManager.activeNetwork
      val caps = connectivityManager.getNetworkCapabilities(activeNetwork)
      _isOnline.value = caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

      val request = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()

      connectivityManager.registerNetworkCallback(request, object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
          _isOnline.value = true
          fetchLivePrices()
        }

        override fun onLost(network: Network) {
          _isOnline.value = false
        }
      })
    }

    // Initial fetch
    fetchLivePrices()
  }

  fun fetchLivePrices() {
    CoroutineScope(Dispatchers.IO).launch {
      _isRefreshing.value = true
      try {
        // Fetch from CoinGecko free public API
        val url = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin,ethereum,the-open-network&vs_currencies=usd,eur&include_24hr_change=true"
        val request = Request.Builder()
          .url(url)
          .header("Accept", "application/json")
          .header("User-Agent", "PRAI-MTK-NEC-Android/1.0")
          .build()

        client.newCall(request).execute().use { response ->
          if (response.isSuccessful) {
            val bodyString = response.body?.string()
            if (!bodyString.isNullOrBlank()) {
              val json = JSONObject(bodyString)
              val updated = _marketData.value.toMutableMap()

              // Bitcoin
              if (json.has("bitcoin")) {
                val btc = json.getJSONObject("bitcoin")
                val usd = btc.optDouble("usd", 64200.0)
                val eur = btc.optDouble("eur", 59100.0)
                val change = btc.optDouble("usd_24h_change", 1.4)
                updated["BTC"] = LiveMarketData("BTC", usd, eur, change)
              }

              // Ethereum
              if (json.has("ethereum")) {
                val eth = json.getJSONObject("ethereum")
                val usd = eth.optDouble("usd", 3480.0)
                val eur = eth.optDouble("eur", 3205.0)
                val change = eth.optDouble("usd_24h_change", -0.8)
                updated["ETH"] = LiveMarketData("ETH", usd, eur, change)
              }

              // TON
              if (json.has("the-open-network")) {
                val ton = json.getJSONObject("the-open-network")
                val usd = ton.optDouble("usd", 5.65)
                val eur = ton.optDouble("eur", 5.21)
                val change = ton.optDouble("usd_24h_change", 3.2)
                updated["TON"] = LiveMarketData("TON", usd, eur, change)
              }

              _marketData.value = updated
              _isOnline.value = true
              Log.d(TAG, "Live market prices updated successfully from CoinGecko")
            }
          }
        }
      } catch (e: Exception) {
        Log.w(TAG, "Could not fetch live market prices (using cached rates): ${e.message}")
      } finally {
        _isRefreshing.value = false
      }
    }
  }
}
