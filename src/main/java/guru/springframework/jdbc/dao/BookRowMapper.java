package guru.springframework.jdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import guru.springframework.jdbc.domain.Book;

public class BookRowMapper implements RowMapper<Book> {
    @Override public Book mapRow(ResultSet resultSet, int i) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getLong("id"));
        book.setAuthorId(resultSet.getLong("author_id"));
        book.setTitle(resultSet.getString("title"));
        book.setIsbn(resultSet.getString("isbn"));
        book.setPublisher(resultSet.getString("publisher"));
        return book;
    }
}
