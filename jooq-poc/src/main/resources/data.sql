-- Insert sample authors
INSERT INTO authors (first_name, last_name, birth_date, nationality) VALUES
('George', 'Orwell', '1903-06-25', 'British'),
('Jane', 'Austen', '1775-12-16', 'British'),
('Gabriel', 'García Márquez', '1927-03-06', 'Colombian'),
('Haruki', 'Murakami', '1949-01-12', 'Japanese'),
('Toni', 'Morrison', '1931-02-18', 'American');

-- Insert sample categories
INSERT INTO categories (name, description) VALUES
('Fiction', 'Literary fiction and novels'),
('Science Fiction', 'Futuristic and speculative fiction'),
('Classic Literature', 'Timeless literary works'),
('Romance', 'Romantic literature'),
('Fantasy', 'Fantasy and magical realism'),
('Dystopian', 'Dystopian and post-apocalyptic fiction');

-- Insert sample books
INSERT INTO books (title, isbn, publication_year, pages, price, author_id) VALUES
('1984', '978-0-452-28423-4', 1949, 328, 13.99, 1),
('Animal Farm', '978-0-452-28424-1', 1945, 95, 9.99, 1),
('Pride and Prejudice', '978-0-14-143951-8', 1813, 432, 12.99, 2),
('Emma', '978-0-14-143959-4', 1815, 474, 14.99, 2),
('One Hundred Years of Solitude', '978-0-06-088328-7', 1967, 417, 16.99, 3),
('Norwegian Wood', '978-0-375-70474-1', 1987, 296, 15.99, 4),
('The Wind-Up Bird Chronicle', '978-0-679-77543-9', 1994, 607, 17.99, 4),
('Beloved', '978-1-4000-3341-6', 1987, 324, 15.99, 5);

-- Insert book-category relationships
INSERT INTO book_categories (book_id, category_id) VALUES
(1, 2), (1, 6), -- 1984: Science Fiction, Dystopian
(2, 1), (2, 3), -- Animal Farm: Fiction, Classic Literature
(3, 1), (3, 3), (3, 4), -- Pride and Prejudice: Fiction, Classic Literature, Romance
(4, 1), (4, 3), (4, 4), -- Emma: Fiction, Classic Literature, Romance
(5, 1), (5, 5), -- One Hundred Years of Solitude: Fiction, Fantasy
(6, 1), (6, 4), -- Norwegian Wood: Fiction, Romance
(7, 1), (7, 5), -- The Wind-Up Bird Chronicle: Fiction, Fantasy
(8, 1), (8, 3); -- Beloved: Fiction, Classic Literature

-- Insert sample reviews
INSERT INTO reviews (book_id, reviewer_name, rating, comment, review_date) VALUES
(1, 'Alice Johnson', 5, 'A masterpiece of dystopian fiction. Orwell''s vision is both terrifying and prescient.', '2024-01-15'),
(1, 'Bob Smith', 4, 'Compelling read, though quite dark. The concepts are still relevant today.', '2024-02-20'),
(3, 'Carol Davis', 5, 'Austen''s wit and social commentary are unmatched. A timeless romance.', '2024-01-10'),
(3, 'David Wilson', 4, 'Beautiful prose and well-developed characters. A classic for good reason.', '2024-03-05'),
(5, 'Eva Martinez', 5, 'Magical realism at its finest. García Márquez weaves an incredible tale.', '2024-02-14'),
(6, 'Frank Lee', 4, 'Murakami''s exploration of love and loss is deeply moving.', '2024-01-28'),
(8, 'Grace Chen', 5, 'Morrison''s powerful narrative about trauma and healing is unforgettable.', '2024-03-12');
