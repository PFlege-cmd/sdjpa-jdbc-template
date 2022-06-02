package guru.springframework.jdbc.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import guru.springframework.jdbc.domain.Book;

public interface BookDao {

    List<Book> findAll();

    List<Book> findAll(int limit, int offset);

    List<Book> findAll(Pageable pageable);

    Book getById(Long id);

    Book findByTitle(String title);

    Optional<Book> saveBook(Book bookToSave);

    Optional<Book> updateBook(Book updatedBook);

    void deleteById(Long id);
}
