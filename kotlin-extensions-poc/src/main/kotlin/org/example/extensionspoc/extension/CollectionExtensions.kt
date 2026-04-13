package org.example.extensionspoc.extension

fun List<Int>.median(): Double {
    if (this.isEmpty()) throw IllegalArgumentException("List must not be empty")
    val sorted = this.sorted()
    val mid = sorted.size / 2
    return if (sorted.size % 2 == 0) (sorted[mid - 1] + sorted[mid]) / 2.0
    else sorted[mid].toDouble()
}

fun <T> List<T>.second(): T {
    if (this.size < 2) throw IllegalArgumentException("List must have at least 2 elements")
    return this[1]
}

fun <K, V> Map<K, V?>.filterValuesNotNull(): Map<K, V> =
    this.entries
        .filter { it.value != null }
        .associate { it.key to it.value!! }
