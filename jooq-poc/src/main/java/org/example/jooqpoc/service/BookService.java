package org.example.jooqpoc.service;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.jooqpoc.model.Book;
import org.example.jooqpoc.model.Author;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.*;

@Service
public class BookService {

    @Autowired
    private DSLContext dsl;

    /**
     * Find all books with author information (demonstrates INNER JOIN)
     */
    public List<Book> findAllBooksWithAuthors() {
        return dsl.select(
                field("books.id").as("book_id"),
                field("books.title"),
                field("books.isbn"),
                field("books.publication_year"),
                field("books.pages"),
                field("books.price"),
                field("books.author_id"),
                field("books.created_at").as("book_created_at"),
                field("books.updated_at").as("book_updated_at"),
                field("authors.id").as("author_id"),
                field("authors.first_name"),
                field("authors.last_name"),
                field("authors.birth_date"),
                field("authors.nationality"),
                field("authors.created_at").as("author_created_at"),
                field("authors.updated_at").as("author_updated_at")
        )
        .from(table("books"))
        .innerJoin(table("authors"))
        .on(field("books.author_id").eq(field("authors.id")))
        .orderBy(field("books.title"))
        .fetch(this::mapBookWithAuthor);
    }

    /**
     * Find books by author nationality (demonstrates WHERE clause with JOIN)
     */
    public List<Book> findBooksByAuthorNationality(String nationality) {
        return dsl.select(
                field("books.id"),
                field("books.title"),
                field("books.isbn"),
                field("books.publication_year"),
                field("books.pages"),
                field("books.price"),
                field("books.author_id"),
                field("books.created_at"),
                field("books.updated_at")
        )
        .from(table("books"))
        .innerJoin(table("authors"))
        .on(field("books.author_id").eq(field("authors.id")))
        .where(field("authors.nationality").eq(nationality))
        .orderBy(field("books.publication_year").desc())
        .fetch(this::mapBook);
    }

    /**
     * Find books by price range (demonstrates BETWEEN)
     */
    public List<Book> findBooksByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return dsl.select()
        .from(table("books"))
        .where(field("price").between(minPrice, maxPrice))
        .orderBy(field("price"))
        .fetch(this::mapBook);
    }

    /**
     * Find books published after a certain year with pagination (demonstrates LIMIT and OFFSET)
     */
    public List<Book> findRecentBooks(int year, int limit, int offset) {
        return dsl.select()
        .from(table("books"))
        .where(field("publication_year").greaterThan(year))
        .orderBy(field("publication_year").desc(), field("title"))
        .limit(limit)
        .offset(offset)
        .fetch(this::mapBook);
    }

    /**
     * Search books by title (demonstrates LIKE and ILIKE)
     */
    public List<Book> searchBooksByTitle(String searchTerm) {
        return dsl.select()
        .from(table("books"))
        .where(upper(field("title", String.class)).like("%" + searchTerm.toUpperCase() + "%"))
        .orderBy(field("title"))
        .fetch(this::mapBook);
    }

    /**
     * Find a book by ID (demonstrates single record fetching)
     */
    public Optional<Book> findBookById(Long id) {
        Record record = dsl.select()
        .from(table("books"))
        .where(field("id").eq(id))
        .fetchOne();

        return record != null ? Optional.of(mapBook(record)) : Optional.empty();
    }

    /**
     * Create a new book (demonstrates INSERT)
     */
    public Book createBook(Book book) {
        Record record = dsl.insertInto(table("books"))
        .columns(
            field("title"),
            field("isbn"),
            field("publication_year"),
            field("pages"),
            field("price"),
            field("author_id")
        )
        .values(
            book.getTitle(),
            book.getIsbn(),
            book.getPublicationYear(),
            book.getPages(),
            book.getPrice(),
            book.getAuthorId()
        )
        .returningResult(field("id"))
        .fetchOne();

        if (record != null) {
            book.setId(record.getValue(field("id", Long.class)));
        }
        return book;
    }

    /**
     * Update a book (demonstrates UPDATE)
     */
    public boolean updateBook(Long id, Book book) {
        int updatedRows = dsl.update(table("books"))
        .set(field("title"), book.getTitle())
        .set(field("isbn"), book.getIsbn())
        .set(field("publication_year"), book.getPublicationYear())
        .set(field("pages"), book.getPages())
        .set(field("price"), book.getPrice())
        .set(field("updated_at"), LocalDateTime.now())
        .where(field("id").eq(id))
        .execute();

        return updatedRows > 0;
    }

    /**
     * Delete a book (demonstrates DELETE)
     */
    public boolean deleteBook(Long id) {
        int deletedRows = dsl.deleteFrom(table("books"))
        .where(field("id").eq(id))
        .execute();

        return deletedRows > 0;
    }

    /**
     * Get books count by author (demonstrates GROUP BY and COUNT)
     */
    public Result<Record> getBooksCountByAuthor() {
        return dsl.select(
            field("authors.first_name"),
            field("authors.last_name"),
            count().as("book_count")
        )
        .from(table("authors"))
        .leftJoin(table("books"))
        .on(field("authors.id").eq(field("books.author_id")))
        .groupBy(field("authors.id"), field("authors.first_name"), field("authors.last_name"))
        .orderBy(count().desc())
        .fetch();
    }

    /**
     * Get average book price by publication decade (demonstrates complex aggregation)
     */
    public Result<Record> getAveragePriceByDecade() {
        return dsl.select(
            field("publication_year", Integer.class).div(10).mul(10).as("decade"),
            avg(field("price", BigDecimal.class)).as("average_price"),
            count().as("book_count")
        )
        .from(table("books"))
        .where(field("price").isNotNull())
        .groupBy(field("publication_year", Integer.class).div(10).mul(10))
        .orderBy(field("decade"))
        .fetch();
    }

    // Helper methods for mapping records to objects
    private Book mapBook(Record record) {
        Book book = new Book();
        book.setId(record.getValue(field("id", Long.class)));
        book.setTitle(record.getValue(field("title", String.class)));
        book.setIsbn(record.getValue(field("isbn", String.class)));
        book.setPublicationYear(record.getValue(field("publication_year", Integer.class)));
        book.setPages(record.getValue(field("pages", Integer.class)));
        book.setPrice(record.getValue(field("price", BigDecimal.class)));
        book.setAuthorId(record.getValue(field("author_id", Long.class)));
        book.setCreatedAt(record.getValue(field("created_at", LocalDateTime.class)));
        book.setUpdatedAt(record.getValue(field("updated_at", LocalDateTime.class)));
        return book;
    }

    private Book mapBookWithAuthor(Record record) {
        Book book = new Book();
        book.setId(record.getValue(field("book_id", Long.class)));
        book.setTitle(record.getValue(field("title", String.class)));
        book.setIsbn(record.getValue(field("isbn", String.class)));
        book.setPublicationYear(record.getValue(field("publication_year", Integer.class)));
        book.setPages(record.getValue(field("pages", Integer.class)));
        book.setPrice(record.getValue(field("price", BigDecimal.class)));
        book.setAuthorId(record.getValue(field("author_id", Long.class)));
        book.setCreatedAt(record.getValue(field("book_created_at", LocalDateTime.class)));
        book.setUpdatedAt(record.getValue(field("book_updated_at", LocalDateTime.class)));

        Author author = new Author();
        author.setId(record.getValue(field("author_id", Long.class)));
        author.setFirstName(record.getValue(field("first_name", String.class)));
        author.setLastName(record.getValue(field("last_name", String.class)));
        author.setBirthDate(record.getValue(field("birth_date", LocalDate.class)));
        author.setNationality(record.getValue(field("nationality", String.class)));
        author.setCreatedAt(record.getValue(field("author_created_at", LocalDateTime.class)));
        author.setUpdatedAt(record.getValue(field("author_updated_at", LocalDateTime.class)));

        book.setAuthor(author);
        return book;
    }
}
