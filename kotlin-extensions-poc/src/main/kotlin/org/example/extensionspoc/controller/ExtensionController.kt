package org.example.extensionspoc.controller

import org.example.extensionspoc.extension.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class ExtensionController {

    @GetMapping("/slug")
    fun slug(@RequestParam text: String): Map<String, Any> =
        mapOf("original" to text, "slug" to text.toSlug())

    @GetMapping("/mask-email")
    fun maskEmail(@RequestParam email: String): Map<String, Any> =
        mapOf("original" to email, "masked" to email.maskEmail())

    @GetMapping("/palindrome")
    fun palindrome(@RequestParam text: String): Map<String, Any> =
        mapOf("original" to text, "isPalindrome" to text.isPalindrome(), "wordCount" to text.wordCount())

    @GetMapping("/median")
    fun median(@RequestBody numbers: List<Int>): Map<String, Any> =
        mapOf("numbers" to numbers, "median" to numbers.median(), "second" to numbers.second())
}
