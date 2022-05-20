package guru.springframework.jdbc.dao;

import java.util.Optional;

import guru.springframework.jdbc.domain.Book;

public interface BookDao {
    Book getById(Long id);

    Book findByTitle(String title);

    Optional<Book> saveBook(Book bookToSave);

    Optional<Book> updateBook(Book updatedBook);

    void deleteById(Long id);
}
