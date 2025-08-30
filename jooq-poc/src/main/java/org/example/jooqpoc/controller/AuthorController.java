package org.example.jooqpoc.controller;

import org.example.jooqpoc.model.Author;
import org.example.jooqpoc.service.AuthorService;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    /**
     * GET /api/authors - Get all authors
     */
    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        List<Author> authors = authorService.findAllAuthors();
        return ResponseEntity.ok(authors);
    }

    /**
     * GET /api/authors/{id} - Get author by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        Optional<Author> author = authorService.findAuthorById(id);
        return author.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/authors/nationality/{nationality} - Find authors by nationality
     */
    @GetMapping("/nationality/{nationality}")
    public ResponseEntity<List<Author>> getAuthorsByNationality(@PathVariable String nationality) {
        List<Author> authors = authorService.findAuthorsByNationality(nationality);
        return ResponseEntity.ok(authors);
    }

    /**
     * GET /api/authors/birth-year?start={startYear}&end={endYear} - Find authors by birth year range
     */
    @GetMapping("/birth-year")
    public ResponseEntity<List<Author>> getAuthorsByBirthYear(
            @RequestParam int start,
            @RequestParam int end) {
        List<Author> authors = authorService.findAuthorsByBirthYearRange(start, end);
        return ResponseEntity.ok(authors);
    }

    /**
     * GET /api/authors/stats - Get detailed author statistics with book counts
     */
    @GetMapping("/stats")
    public ResponseEntity<List<Record>> getAuthorStatistics() {
        List<Record> stats = authorService.getAuthorStatistics();
        return ResponseEntity.ok(stats);
    }

    /**
     * GET /api/authors/prolific?minBooks={minBookCount} - Find most prolific authors
     */
    @GetMapping("/prolific")
    public ResponseEntity<List<Record>> getMostProlificAuthors(@RequestParam int minBooks) {
        List<Record> authors = authorService.findMostProlificAuthors(minBooks);
        return ResponseEntity.ok(authors);
    }

    /**
     * POST /api/authors - Create a new author
     */
    @PostMapping
    public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
        Author createdAuthor = authorService.createAuthor(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAuthor);
    }

    /**
     * PUT /api/authors/{id} - Update an author
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAuthor(@PathVariable Long id, @RequestBody Author author) {
        boolean updated = authorService.updateAuthor(id, author);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    /**
     * DELETE /api/authors/{id} - Delete an author
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        boolean deleted = authorService.deleteAuthor(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
