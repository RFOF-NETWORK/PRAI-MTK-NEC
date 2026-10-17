package org.rfof.shared

import kotlin.math.*

data class GenesisBlock(
    val genesisId: String,
    val e8Points: Int,
    val matrixSize: Pair<Int, Int>,
    val rounds: List<Int>,
    val signature: String
)

object QppsGenesis {
    private val initA = -1
    private val initB = -28

    // Dual shifting sequence
    private val sequence = listOf(10, 8, 7, 5)

    // E8 base points
    private const val E8_POINTS = 248

    // 120x120 matrix
    private val MATRIX = Pair(120, 120)

    fun computeGenesis(): GenesisBlock {
        // deterministic string derivation using the fixed constants and sequence
        val payload = StringBuilder()
        payload.append("init:").append(initA).append(",")
        payload.append(initB).append(";seq:")
        sequence.forEach { payload.append(it).append(',') }
        payload.append(";e8:").append(E8_POINTS).append(";mat:").append(MATRIX.first).append('x').append(MATRIX.second)

        val gid = simpleDeterministicHash(payload.toString())
        val signature = deriveSignature(gid)

        return GenesisBlock(
            genesisId = gid,
            e8Points = E8_POINTS,
            matrixSize = MATRIX,
            rounds = sequence,
            signature = signature
        )
    }

    private fun simpleDeterministicHash(input: String): String {
        // 64-bit rolling polynomial hash, deterministic across JS/JVM
        var h = 1469598103934665603UL
        val prime = 1099511628211UL
        input.encodeToByteArray().forEach { b ->
            h = (h xor (b.toULong())) * prime
        }
        return h.toString(16)
    }

    private fun deriveSignature(gid: String): String {
        // deterministic "signature" based on gid and constants (not a crypto signature)
        val combined = gid + ":" + initA + ":" + initB
        return simpleDeterministicHash(combined)
    }
}
