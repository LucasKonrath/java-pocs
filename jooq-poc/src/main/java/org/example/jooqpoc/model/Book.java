package org.example.jooqpoc.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Book {
    private Long id;
    private String title;
    private String isbn;
    private Integer publicationYear;
    private Integer pages;
    private BigDecimal price;
    private Long authorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // For joined queries
    private Author author;

    // Constructors
    public Book() {}

    public Book(String title, String isbn, Integer publicationYear, Integer pages, BigDecimal price, Long authorId) {
        this.title = title;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.pages = pages;
        this.price = price;
        this.authorId = authorId;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Integer getPublicationYear() { return publicationYear; }
    public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }

    public Integer getPages() { return pages; }
    public void setPages(Integer pages) { this.pages = pages; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }
}
