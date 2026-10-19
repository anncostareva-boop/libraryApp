USE librarydb;
-- ------------------------
-- Користувачі
-- ------------------------
CREATE TABLE users (
        id BIGINT PRIMARY KEY AUTO_INCREMENT,
        username VARCHAR(50) UNIQUE NOT NULL,
        email VARCHAR(100) UNIQUE NOT NULL,
        password VARCHAR(255) NOT NULL,
        role VARCHAR(20) NOT NULL
);
-- ------------------------
-- Книги
-- ------------------------
CREATE TABLE books (
        id BIGINT PRIMARY KEY AUTO_INCREMENT,
        title VARCHAR(255),
        author VARCHAR(255),
        description TEXT,
        genre VARCHAR(50),
        status VARCHAR(20) DEFAULT 'IN_STOCK'
);
-- ------------------------
-- Копії книг
-- ------------------------
CREATE TABLE book_copies (
        id BIGINT PRIMARY KEY AUTO_INCREMENT,
        book_id BIGINT,
        status VARCHAR(20) DEFAULT 'IN_STOCK',

        FOREIGN KEY (book_id) REFERENCES books(id)
);
-- ------------------------
-- Запити на видачу
-- ------------------------
CREATE TABLE borrow_requests (
        id BIGINT PRIMARY KEY AUTO_INCREMENT,
        user_id BIGINT NOT NULL,
        book_id BIGINT NOT NULL,
        copy_id BIGINT,
        created_at DATETIME,
        status VARCHAR(20) DEFAULT 'PENDING',

        FOREIGN KEY (user_id) REFERENCES users(id),
        FOREIGN KEY (book_id) REFERENCES books(id),
        FOREIGN KEY (copy_id) REFERENCES book_copies(id)
);
-- ------------------------
-- Історія видачі
-- ------------------------
CREATE TABLE issuance_history (
        id BIGINT PRIMARY KEY AUTO_INCREMENT,
        user_id BIGINT NOT NULL,
        copy_id BIGINT NOT NULL,
        issuance_date DATETIME NOT NULL,
        due_date DATETIME NOT NULL,
        returning_date DATETIME,

        FOREIGN KEY (user_id) REFERENCES users(id),
        FOREIGN KEY (copy_id) REFERENCES book_copies(id)
);
-- ------------------------
-- Штрафи
-- ------------------------
CREATE TABLE fines (
        id BIGINT PRIMARY KEY AUTO_INCREMENT,
        user_id BIGINT,
        amount DECIMAL(10,2),
        paid BOOLEAN DEFAULT FALSE,
        reason VARCHAR(20),

        FOREIGN KEY (user_id) REFERENCES users(id)
);
-- ------------------------
-- Повідомлення
-- ------------------------
CREATE TABLE notifications (
        id BIGINT PRIMARY KEY AUTO_INCREMENT,
        title VARCHAR(255),
        message TEXT,
        time DATETIME,
        is_read BOOLEAN DEFAULT FALSE,

        receiver_id BIGINT,

        FOREIGN KEY (receiver_id) REFERENCES users(id)
);


