package guru.springframework.jdbc.dao;

import java.util.Optional;

import guru.springframework.jdbc.domain.Author;

public interface AuthorDao {
    Author saveAuthor(Author author);
    Author getById(Long id);
    Author findAuthorByName(String firstName, String lastName);
    Optional<Author> updateAuthor(Author author);
    void deleteAuthorById(Long id);
    Optional<Author> getByIdWithBooks(Long id);
}
