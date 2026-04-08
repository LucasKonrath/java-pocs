package org.example.cachepoc.controller

import org.example.cachepoc.model.Country
import org.example.cachepoc.service.CountryService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/countries")
class CountryController(private val countryService: CountryService) {

    @GetMapping("/{code}")
    fun getByCode(@PathVariable code: String): Country? {
        return countryService.getByCode(code)
    }

    @DeleteMapping("/{code}/cache")
    fun evict(@PathVariable code: String) {
        countryService.evict(code)
    }

    @PutMapping("/{code}")
    fun update(@PathVariable code: String, @RequestBody country: Country): Country {
        return countryService.update(code, country)
    }
}
