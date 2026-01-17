package com.moisespellegrin.agrocalculo.utils

fun String.toDoubleBR(): Double? {
    return this
        .trim()
        .replace(" ", "")
        .replace(",", ".")
        .toDoubleOrNull()
}