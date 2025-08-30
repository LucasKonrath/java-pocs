# Spring Boot JOOQ POC

This is a comprehensive Proof of Concept (POC) demonstrating the integration and capabilities of JOOQ with Spring Boot. The project showcases various JOOQ features including type-safe SQL building, complex queries, joins, aggregations, and CRUD operations.

## Overview

JOOQ (Java Object Oriented Querying) is a library that provides a fluent API for building type-safe SQL queries in Java. This POC demonstrates:

- **Type-safe SQL queries** without code generation
- **Complex joins** and aggregations
- **Dynamic query building**
- **CRUD operations** with JOOQ
- **Advanced SQL features** like window functions, subqueries, and CTEs
- **Spring Boot integration** with JOOQ DSLContext

## Tech Stack

- **Java 24**
- **Spring Boot 3.5.5**
- **JOOQ 3.19.8**
- **H2 Database** (in-memory for development)
- **Maven** for dependency management

## Database Schema

The POC uses a book management system with the following entities:

### Tables
- **authors**: Author information (id, first_name, last_name, birth_date, nationality)
- **books**: Book details (id, title, isbn, publication_year, pages, price, author_id)
- **categories**: Book categories (id, name, description)
- **book_categories**: Many-to-many relationship between books and categories
- **reviews**: Book reviews (id, book_id, reviewer_name, rating, comment, review_date)

## Key JOOQ Features Demonstrated

### 1. Basic CRUD Operations
- **Create**: Insert new authors and books
- **Read**: Fetch records with various filters
- **Update**: Modify existing records
- **Delete**: Remove records with cascade handling

### 2. Advanced Query Features
- **JOINs**: Inner joins, left joins with multiple tables
- **Aggregations**: COUNT, SUM, AVG, MIN, MAX
- **GROUP BY & HAVING**: Complex grouping with conditions
- **Subqueries**: Nested queries for advanced filtering
- **Pagination**: LIMIT and OFFSET for result pagination
- **Dynamic Filters**: Conditional WHERE clauses

### 3. Type Safety
- **Field references**: Type-safe field access
- **Parameter binding**: Safe parameter substitution
- **Result mapping**: Automatic type conversion

## API Endpoints

### Book Endpoints (`/api/books`)

| Method | Endpoint | Description | JOOQ Features |
|--------|----------|-------------|---------------|
| GET | `/api/books` | Get all books with authors | INNER JOIN, field aliasing |
| GET | `/api/books/{id}` | Get book by ID | Single record fetch, Optional handling |
| GET | `/api/books/search?title={term}` | Search books by title | LIKE queries, UPPER function |
| GET | `/api/books/nationality/{nationality}` | Books by author nationality | JOIN with WHERE clause |
| GET | `/api/books/price-range?min={min}&max={max}` | Books by price range | BETWEEN operator |
| GET | `/api/books/recent?year={year}&limit={limit}&offset={offset}` | Recent books with pagination | GREATER THAN, LIMIT, OFFSET |
| POST | `/api/books` | Create new book | INSERT with RETURNING |
| PUT | `/api/books/{id}` | Update book | UPDATE with WHERE |
| DELETE | `/api/books/{id}` | Delete book | DELETE with WHERE |
| GET | `/api/books/stats/by-author` | Books count by author | LEFT JOIN, GROUP BY, COUNT |
| GET | `/api/books/stats/by-decade` | Average price by decade | Complex aggregation, arithmetic |

### Author Endpoints (`/api/authors`)

| Method | Endpoint | Description | JOOQ Features |
|--------|----------|-------------|---------------|
| GET | `/api/authors` | Get all authors | Basic SELECT with ORDER BY |
| GET | `/api/authors/{id}` | Get author by ID | Single record fetch |
| GET | `/api/authors/nationality/{nationality}` | Authors by nationality | WHERE clause |
| GET | `/api/authors/birth-year?start={start}&end={end}` | Authors by birth year range | Date functions, BETWEEN |
| GET | `/api/authors/stats` | Author statistics with book counts | Complex aggregation with multiple functions |
| GET | `/api/authors/prolific?minBooks={count}` | Most prolific authors | GROUP BY with HAVING |
| POST | `/api/authors` | Create new author | INSERT with RETURNING |
| PUT | `/api/authors/{id}` | Update author | UPDATE with timestamp |
| DELETE | `/api/authors/{id}` | Delete author | DELETE with cascade |

## Project Structure

```
src/main/java/org/example/jooqpoc/
├── JooqPocApplication.java          # Main Spring Boot application
├── controller/
│   ├── AuthorController.java        # REST endpoints for authors
│   └── BookController.java          # REST endpoints for books
├── model/
│   ├── Author.java                  # Author entity
│   ├── Book.java                    # Book entity
│   └── Category.java                # Category entity
└── service/
    ├── AuthorService.java           # Author business logic with JOOQ
    └── BookService.java             # Book business logic with JOOQ

src/main/resources/
├── application.properties           # Application configuration
├── schema.sql                       # Database schema creation
└── data.sql                        # Sample data insertion
```

## Running the Application

### Prerequisites
- Java 24 or later
- Maven 3.6+

### Steps
1. **Clone and navigate to the project directory**
2. **Compile the project**:
   ```bash
   mvn clean compile
   ```

3. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**:
   - Application runs on: `http://localhost:8081`
   - H2 Console: `http://localhost:8081/h2-console`
     - JDBC URL: `jdbc:h2:mem:testdb`
     - User: `sa`
     - Password: (empty)

## Sample API Calls

### Get all books with authors
```bash
curl http://localhost:8081/api/books
```

### Search books by title
```bash
curl "http://localhost:8081/api/books/search?title=1984"
```

### Get books by author nationality
```bash
curl http://localhost:8081/api/books/nationality/British
```

### Get author statistics
```bash
curl http://localhost:8081/api/authors/stats
```

### Create a new author
```bash
curl -X POST http://localhost:8081/api/authors \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "J.K.",
    "lastName": "Rowling",
    "birthDate": "1965-07-31",
    "nationality": "British"
  }'
```

## JOOQ Advantages Demonstrated

1. **Type Safety**: Compile-time checking of SQL queries
2. **IDE Support**: Full autocomplete and refactoring support
3. **Database Agnostic**: Works with multiple database dialects
4. **Performance**: Minimal overhead compared to raw JDBC
5. **Flexibility**: Dynamic query building based on conditions
6. **Maintainability**: SQL queries are part of Java code, easier to maintain

## Key JOOQ Patterns Used

### 1. DSLContext Injection
```java
@Autowired
private DSLContext dsl;
```

### 2. Type-safe Field References
```java
field("books.title", String.class)
field("authors.id", Long.class)
```

### 3. Complex Joins
```java
dsl.select()
   .from(table("books"))
   .innerJoin(table("authors"))
   .on(field("books.author_id").eq(field("authors.id")))
```

### 4. Aggregations with Grouping
```java
dsl.select(count().as("book_count"), avg(field("price")))
   .from(table("books"))
   .groupBy(field("author_id"))
   .having(count().greaterThan(1))
```

### 5. Dynamic Query Building
```java
SelectConditionStep<?> query = dsl.select().from(table("books"));
if (minPrice != null) {
    query = query.where(field("price").greaterOrEqual(minPrice));
}
```

This POC provides a comprehensive foundation for understanding and implementing JOOQ in Spring Boot applications, showcasing both basic and advanced features of this powerful SQL building library.
