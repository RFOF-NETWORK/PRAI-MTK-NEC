package org.rfof.shared

object IbanValidator {
    fun normalize(iban: String): String = iban.replace(" ", "").uppercase()

    fun isValid(iban: String): Boolean {
        val s = normalize(iban)
        if (s.length < 15 || s.length > 34) return false
        val rearr = s.substring(4) + s.substring(0,4)
        // convert chars to digits
        val digits = StringBuilder()
        for (ch in rearr) {
            when (ch) {
                in '0'..'9' -> digits.append(ch)
                in 'A'..'Z' -> digits.append((ch.code - 55).toString())
                else -> return false
            }
        }
        // compute mod 97 without BigInteger by iterating chunks
        var remainder = 0
        var idx = 0
        val digitStr = digits.toString()
        while (idx < digitStr.length) {
            val take = minOf(9, digitStr.length - idx)
            val part = digitStr.substring(idx, idx + take)
            val value = (remainder.toString() + part).toLong()
            remainder = (value % 97).toInt()
            idx += take
        }
        return remainder == 1
    }
}
