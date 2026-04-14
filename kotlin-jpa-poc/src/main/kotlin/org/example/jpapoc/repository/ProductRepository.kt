package org.example.jpapoc.repository

import org.example.jpapoc.entity.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long> {
    fun findByCategory(category: String): List<Product>
}
