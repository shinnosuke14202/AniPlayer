package com.example.aniplayer.utils.parsers

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.math.absoluteValue

fun Number.format(
    decimals: Int = 0,
    decPoint: Char = '.',
    thousandsSep: Char? = ' '
): String {
    val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
    val symbols = formatter.decimalFormatSymbols
    if (thousandsSep != null) {
        symbols.groupingSeparator = thousandsSep
        formatter.isGroupingUsed = true
    } else {
        formatter.isGroupingUsed = false
    }
    symbols.decimalSeparator = decPoint
    formatter.decimalFormatSymbols = symbols
    formatter.minimumFractionDigits = decimals
    formatter.maximumFractionDigits = decimals
    return when (this) {
        is Float,
        is Double,
            -> formatter.format(this.toDouble())

        else -> formatter.format(this.toLong())
    }
}

fun Float.toIntUp(): Int {
    val intValue = toInt()
    return if ((this - intValue.toFloat()).absoluteValue <= 0.00001) {
        intValue
    } else {
        intValue + 1
    }
}

infix fun Int.upBy(step: Int): Int {
    val mod = this % step
    return if (mod == 0) {
        this
    } else {
        this - mod + step
    }
}

fun Number.formatSimple(): String {
    val raw = toString()
    return if (raw.endsWith(".0") || raw.endsWith(",0")) {
        raw.dropLast(2)
    } else {
        raw
    }
}

@OptIn(ExperimentalContracts::class)
inline fun Int.ifZero(defaultValue: () -> Int): Int {
    contract {
        callsInPlace(defaultValue, InvocationKind.AT_MOST_ONCE)
    }
    return if (this == 0) {
        defaultValue()
    } else {
        this
    }
}

@OptIn(ExperimentalContracts::class)
inline fun Long.ifZero(defaultValue: () -> Long): Long {
    contract {
        callsInPlace(defaultValue, InvocationKind.AT_MOST_ONCE)
    }
    return if (this == 0L) {
        defaultValue()
    } else {
        this
    }
}

fun longOf(a: Int, b: Int): Long {
    return a.toLong() shl 32 or (b.toLong() and 0xffffffffL)
}
