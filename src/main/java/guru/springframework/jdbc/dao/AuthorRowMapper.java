package guru.springframework.jdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import guru.springframework.jdbc.domain.Author;

public class AuthorRowMapper implements RowMapper<Author> {
    public Author mapRow(ResultSet resultSet, int i) throws SQLException {
        Author author = new Author();
        author.setFirstName(resultSet.getString("first_name"));
        author.setLastName(resultSet.getString("last_name"));
        author.setId(resultSet.getLong("id"));
        return author;
    }
}
