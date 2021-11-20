package com.dixitgarg.investmentapplication.utils

import android.content.Context
import java.io.InputStream
import java.math.BigDecimal
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Utility {
    companion object {
        const val SERVER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"

        private val suffixes: NavigableMap<Double, String> =
            TreeMap()
        val UNIT_K = "K"
        val UNIT_M = "M"
        val UNIT_G = "G"
        val UNIT_T = "T"
        val UNIT_P = "P"
        val UNIT_E = "E"

        init
        {
            suffixes[1000.0] = UNIT_K
            suffixes[1_000_000.0] = UNIT_M
            suffixes[1_000_000_000.0] = UNIT_G
            suffixes[1_000_000_000_000.0] = UNIT_T
            suffixes[1_000_000_000_000_000.0] = UNIT_P
            suffixes[1_000_000_000_000_000_000.0] = UNIT_E
        }

        fun convertStringToGivenDateFormat(
            dateStr: String,
            sourceFormat: String,
            requiredFormat: String
        ): String {
            try {
                val sdf = SimpleDateFormat(sourceFormat, Locale.getDefault())
                val date = try {
                    sdf.parse(dateStr)
                } catch (pe: ParseException) {
                    null
                }
                val requiredSDF = SimpleDateFormat(requiredFormat, Locale.getDefault())
                return try {
                    requiredSDF.format(date!!)
                } catch (pe: ParseException) {
                    requiredSDF.format(Date())
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                return ""
            }

        }

        fun readJSONFromAsset(context: Context): String {
            val json: String
            try {
                val inputStream: InputStream = context.assets.open("Payments.json")
                json = inputStream.bufferedReader().use {
                    it.readText()
                }
            } catch (ex: Exception) {
                ex.localizedMessage
                return ""
            }
            return json
        }

        fun formatDoubleValues(value: Double, upToDecimalPlaces: Int = 0): String? {
            if (value < 0) return "-" + formatDoubleValues(-value, upToDecimalPlaces)
            if (value < 1000) return convertToDecimalFormat(
                value.toString(),
                upToDecimalPlaces
            ) //Double.toString(value); //deal with easy case
            val e = suffixes.floorEntry(value)
            val divideBy = e.key
            var suffix = e.value
            val truncated =
                value / (divideBy / 10) //the number part of the output times 10
            var doubleTruncatedValue = truncated / 10.0
            if (doubleTruncatedValue < 100) { //if value like 1m then change this 1m to 1000k
                doubleTruncatedValue *= 1000
                suffix = getNewUpdatedUnit(suffix)
            }
            return localeNumberFormatting(
                doubleTruncatedValue.toString() + "",
                upToDecimalPlaces
            )
                .toString() + "" + suffix
        }

        private fun convertToDecimalFormat(value: String, upToDecimal: Int): String? {
            if (canConvertToNumber(value))
                return String.format("%." + upToDecimal + "f", BigDecimal(value))
            return value
        }

        private fun canConvertToNumber(value: String?): Boolean {
            if (value == null)
                return false
            return try {
                value.toDouble()
                true
            } catch (exception: NumberFormatException) {
                false
            }
            return false
        }

        private fun getNewUpdatedUnit(suffix: String): String {
            return when (suffix) {
                UNIT_K -> ""
                UNIT_M -> UNIT_K
                UNIT_G -> UNIT_M
                UNIT_T -> UNIT_G
                UNIT_P -> UNIT_T
                UNIT_E -> UNIT_P
                else -> UNIT_E
            }
        }

        fun localeNumberFormatting(number: String, decimalPlaces: Int = 0): String? {
            return localeNumberFormatting(number.toDouble(), decimalPlaces)
        }

        private fun localeNumberFormatting(
            number: Double?,
            decimalPlaces: Int
        ): String? {
            var formattedNumber = "0.00"
            try {
                val numberFormat =
                    NumberFormat.getInstance(Locale("en", "US"))
                numberFormat.minimumFractionDigits = decimalPlaces
                numberFormat.maximumFractionDigits = decimalPlaces
                formattedNumber = numberFormat.format(number)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return formattedNumber
        }

    }
}