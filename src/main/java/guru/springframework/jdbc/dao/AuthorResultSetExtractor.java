package guru.springframework.jdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import guru.springframework.jdbc.domain.Author;
import guru.springframework.jdbc.domain.Book;

public class AuthorResultSetExtractor implements ResultSetExtractor<Author> {
    @Override public Author extractData(ResultSet resultSet) throws SQLException, DataAccessException {

        if (resultSet.next()) {
            Author author = new AuthorRowMapper().mapRow(resultSet, 1);
            if (author == null){
                return null;
            }
            author.setBookList(new ArrayList<>());
            do {
                Book book = new Book();
                book.setId(resultSet.getLong("book_id"));
                book.setIsbn(resultSet.getString("book.isbn"));
                book.setTitle(resultSet.getString("book.title"));
                book.setAuthorId(resultSet.getLong("book.author_id"));
                author.getBookList().add(book);
            } while (resultSet.next());
            return author;
        }
        return null;
    }
}
