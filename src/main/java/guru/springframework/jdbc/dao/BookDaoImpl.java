package guru.springframework.jdbc.dao;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import guru.springframework.jdbc.domain.Book;

@Repository
public class BookDaoImpl implements BookDao{
    @Override public Book getById(Long id) {
        return null;
    }

    @Override public Book findByTitle(String title) {
        return null;
    }

    @Override public Optional<Book> saveBook(Book bookToSave) {
        return Optional.empty();
    }

    @Override public Optional<Book> updateBook(Book updatedBook) {
        return Optional.empty();
    }

    @Override public void deleteById(Long id) {

    }
}
