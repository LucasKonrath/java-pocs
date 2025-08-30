package org.example.jooqpoc.controller;

import org.example.jooqpoc.model.Book;
import org.example.jooqpoc.service.BookService;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    /**
     * GET /api/books - Get all books with author information
     */
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.findAllBooksWithAuthors();
        return ResponseEntity.ok(books);
    }

    /**
     * GET /api/books/{id} - Get book by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookService.findBookById(id);
        return book.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/books/search?title={searchTerm} - Search books by title
     */
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String title) {
        List<Book> books = bookService.searchBooksByTitle(title);
        return ResponseEntity.ok(books);
    }

    /**
     * GET /api/books/nationality/{nationality} - Find books by author nationality
     */
    @GetMapping("/nationality/{nationality}")
    public ResponseEntity<List<Book>> getBooksByNationality(@PathVariable String nationality) {
        List<Book> books = bookService.findBooksByAuthorNationality(nationality);
        return ResponseEntity.ok(books);
    }

    /**
     * GET /api/books/price-range?min={minPrice}&max={maxPrice} - Find books by price range
     */
    @GetMapping("/price-range")
    public ResponseEntity<List<Book>> getBooksByPriceRange(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {
        List<Book> books = bookService.findBooksByPriceRange(min, max);
        return ResponseEntity.ok(books);
    }

    /**
     * GET /api/books/recent?year={year}&limit={limit}&offset={offset} - Get recent books with pagination
     */
    @GetMapping("/recent")
    public ResponseEntity<List<Book>> getRecentBooks(
            @RequestParam int year,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        List<Book> books = bookService.findRecentBooks(year, limit, offset);
        return ResponseEntity.ok(books);
    }

    /**
     * POST /api/books - Create a new book
     */
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book createdBook = bookService.createBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    /**
     * PUT /api/books/{id} - Update a book
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBook(@PathVariable Long id, @RequestBody Book book) {
        boolean updated = bookService.updateBook(id, book);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    /**
     * DELETE /api/books/{id} - Delete a book
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        boolean deleted = bookService.deleteBook(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * GET /api/books/stats/by-author - Get book count statistics by author
     */
    @GetMapping("/stats/by-author")
    public ResponseEntity<List<Record>> getBookCountByAuthor() {
        List<Record> stats = bookService.getBooksCountByAuthor();
        return ResponseEntity.ok(stats);
    }

    /**
     * GET /api/books/stats/by-decade - Get average price statistics by decade
     */
    @GetMapping("/stats/by-decade")
    public ResponseEntity<List<Record>> getAveragePriceByDecade() {
        List<Record> stats = bookService.getAveragePriceByDecade();
        return ResponseEntity.ok(stats);
    }
}
