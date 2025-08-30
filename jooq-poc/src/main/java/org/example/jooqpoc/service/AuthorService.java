package org.example.jooqpoc.service;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.jooqpoc.model.Author;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.*;

@Service
public class AuthorService {

    @Autowired
    private DSLContext dsl;

    /**
     * Find all authors
     */
    public List<Author> findAllAuthors() {
        return dsl.select()
                .from(table("authors"))
                .orderBy(field("last_name"), field("first_name"))
                .fetch(this::mapAuthor);
    }

    /**
     * Find author by ID
     */
    public Optional<Author> findAuthorById(Long id) {
        Record record = dsl.select()
                .from(table("authors"))
                .where(field("id").eq(id))
                .fetchOne();

        return record != null ? Optional.of(mapAuthor(record)) : Optional.empty();
    }

    /**
     * Find authors by nationality (demonstrates WHERE with grouping)
     */
    public List<Author> findAuthorsByNationality(String nationality) {
        return dsl.select()
                .from(table("authors"))
                .where(field("nationality").eq(nationality))
                .orderBy(field("birth_date"))
                .fetch(this::mapAuthor);
    }

    /**
     * Find authors born in a specific year range (demonstrates date operations)
     */
    public List<Author> findAuthorsByBirthYearRange(int startYear, int endYear) {
        return dsl.select()
                .from(table("authors"))
                .where(year(field("birth_date")).between(startYear, endYear))
                .orderBy(field("birth_date"))
                .fetch(this::mapAuthor);
    }

    /**
     * Get author statistics with book counts (demonstrates complex aggregation with LEFT JOIN)
     */
    public List<Record> getAuthorStatistics() {
        return dsl.select(
                field("authors.id"),
                field("authors.first_name"),
                field("authors.last_name"),
                field("authors.nationality"),
                field("authors.birth_date"),
                count(field("books.id")).as("total_books"),
                coalesce(sum(field("books.pages", Integer.class)), 0).as("total_pages"),
                coalesce(avg(field("books.price", BigDecimal.class)), 0).as("average_book_price"),
                coalesce(min(field("books.publication_year", Integer.class)), 0).as("first_publication_year"),
                coalesce(max(field("books.publication_year", Integer.class)), 0).as("last_publication_year")
        )
        .from(table("authors"))
        .leftJoin(table("books"))
        .on(field("authors.id").eq(field("books.author_id")))
        .groupBy(
                field("authors.id"),
                field("authors.first_name"),
                field("authors.last_name"),
                field("authors.nationality"),
                field("authors.birth_date")
        )
        .orderBy(count(field("books.id")).desc(), field("authors.last_name"))
        .fetch();
    }

    /**
     * Find most prolific authors (demonstrates HAVING clause)
     */
    public List<Record> findMostProlificAuthors(int minBookCount) {
        return dsl.select(
                field("authors.first_name"),
                field("authors.last_name"),
                count(field("books.id")).as("book_count")
        )
        .from(table("authors"))
        .innerJoin(table("books"))
        .on(field("authors.id").eq(field("books.author_id")))
        .groupBy(field("authors.id"), field("authors.first_name"), field("authors.last_name"))
        .having(count(field("books.id")).greaterOrEqual(minBookCount))
        .orderBy(count(field("books.id")).desc())
        .fetch();
    }

    /**
     * Create a new author
     */
    public Author createAuthor(Author author) {
        Record record = dsl.insertInto(table("authors"))
                .columns(
                        field("first_name"),
                        field("last_name"),
                        field("birth_date"),
                        field("nationality")
                )
                .values(
                        author.getFirstName(),
                        author.getLastName(),
                        author.getBirthDate(),
                        author.getNationality()
                )
                .returningResult(field("id"))
                .fetchOne();

        if (record != null) {
            author.setId(record.getValue(field("id", Long.class)));
        }
        return author;
    }

    /**
     * Update an author
     */
    public boolean updateAuthor(Long id, Author author) {
        int updatedRows = dsl.update(table("authors"))
                .set(field("first_name"), author.getFirstName())
                .set(field("last_name"), author.getLastName())
                .set(field("birth_date"), author.getBirthDate())
                .set(field("nationality"), author.getNationality())
                .set(field("updated_at"), LocalDateTime.now())
                .where(field("id").eq(id))
                .execute();

        return updatedRows > 0;
    }

    /**
     * Delete an author (this will cascade delete their books due to FK constraint)
     */
    public boolean deleteAuthor(Long id) {
        int deletedRows = dsl.deleteFrom(table("authors"))
                .where(field("id").eq(id))
                .execute();

        return deletedRows > 0;
    }

    // Helper method for mapping records to Author objects
    private Author mapAuthor(Record record) {
        Author author = new Author();
        author.setId(record.getValue(field("id", Long.class)));
        author.setFirstName(record.getValue(field("first_name", String.class)));
        author.setLastName(record.getValue(field("last_name", String.class)));
        author.setBirthDate(record.getValue(field("birth_date", LocalDate.class)));
        author.setNationality(record.getValue(field("nationality", String.class)));
        author.setCreatedAt(record.getValue(field("created_at", LocalDateTime.class)));
        author.setUpdatedAt(record.getValue(field("updated_at", LocalDateTime.class)));
        return author;
    }
}
