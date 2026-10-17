package org.rfof.shared

import kotlin.math.*

class SupplyController(private var baseSupply: Double = 0.0, private var basePrice: Double = 0.0) {

    init {
        if (baseSupply < 0.0) throw IllegalArgumentException("Supply must be >= 0")
        if (basePrice < 0.0) throw IllegalArgumentException("Price must be >= 0")
    }

    private var currentSupply = baseSupply
    private var currentPrice = basePrice

    // Roles allowed to mint
    private val allowedMinterIds = setOf("RFOF-NETWORK", "SATORAMY")

    fun getSupply(): Double = currentSupply
    fun getPrice(): Double = currentPrice

    // Apply a number of roundTrips; each round trip applies the 50%:50% exponential increase
    fun applyRoundTrips(rounds: Int) {
        if (rounds <= 0) return
        repeat(rounds) {
            // supply and price increase by 50% each round (exponential)
            currentSupply = currentSupply * 1.5
            currentPrice = currentPrice * 1.5
            // Guard: supply and price cannot be negative or NaN
            if (currentSupply.isNaN() || currentPrice.isNaN()) throw IllegalStateException("Numeric overflow")
        }
    }

    fun mint(callerId: String, amount: Double) {
        if (!allowedMinterIds.contains(callerId)) throw SecurityException("Unauthorized minter: $callerId")
        if (amount <= 0.0) throw IllegalArgumentException("Amount must be positive")
        // The system treats every admin issuance as backed by treuhand coverage; we don't model real fiat here.
        currentSupply += amount
    }

    fun burn(callerId: String, amount: Double) {
        if (!allowedMinterIds.contains(callerId)) throw SecurityException("Unauthorized burner: $callerId")
        if (amount <= 0.0) throw IllegalArgumentException("Amount must be positive")
        currentSupply = (currentSupply - amount).coerceAtLeast(0.0)
    }
}
