package org.example.cachepoc.service

import org.example.cachepoc.model.Country
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class CountryService {

    private val countries = mutableMapOf(
        "BR" to Country("BR", "Brazil", 214000000),
        "US" to Country("US", "United States", 331000000),
        "JP" to Country("JP", "Japan", 125000000)
    )

    @Cacheable("countries", key = "#code")
    fun getByCode(code: String): Country? {
        Thread.sleep(3000)
        return countries[code]
    }

    @CacheEvict("countries", key = "#code")
    fun evict(code: String) {
    }

    @CachePut("countries", key = "#code")
    fun update(code: String, country: Country): Country {
        countries[code] = country
        return country
    }
}
