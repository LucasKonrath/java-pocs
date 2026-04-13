package org.example.extensionspoc.extension

fun String.toSlug(): String =
    this.trim()
        .lowercase()
        .replace(Regex("[^a-z0-9\\s-]"), "")
        .replace(Regex("\\s+"), "-")
        .replace(Regex("-+"), "-")
        .trim('-')

fun String.maskEmail(): String {
    val parts = this.split("@")
    if (parts.size != 2) return this
    val local = parts[0]
    val masked = if (local.length <= 2) "*".repeat(local.length)
    else local.first() + "*".repeat(local.length - 2) + local.last()
    return "$masked@${parts[1]}"
}

fun String.wordCount(): Int =
    this.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }.size

fun String.isPalindrome(): Boolean {
    val cleaned = this.lowercase().filter { it.isLetterOrDigit() }
    return cleaned == cleaned.reversed()
}
