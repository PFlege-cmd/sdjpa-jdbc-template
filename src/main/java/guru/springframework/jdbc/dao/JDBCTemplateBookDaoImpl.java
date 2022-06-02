package guru.springframework.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;

import guru.springframework.jdbc.domain.Book;

public class JDBCTemplateBookDaoImpl implements BookDao{

    private final JdbcTemplate jdbcTemplate;

    public JDBCTemplateBookDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override public List<Book> findAll() {
        return jdbcTemplate.query("SELECT * from book", getBookRowMapper());
    }

    @Override public List<Book> findAll(int limit, int offset) {
        return jdbcTemplate.query("SELECT * from book limit ? offset ?", getBookRowMapper(), limit, offset);
    }

    @Override public List<Book> findAll(Pageable pageable) {
        return jdbcTemplate.query("SELECT * from book limit ? offset ?", getBookRowMapper(), pageable.getPageSize(), pageable.getOffset());
    }

    @Override
    public Book getById(Long id) {
        return jdbcTemplate.queryForObject("select * from book where id = ?", getBookRowMapper(), id);
    }

    @Override
    public Book findByTitle(String title) {
        return jdbcTemplate.queryForObject("SELECT * from book where title = ?", getBookRowMapper(), title);
    }

    @Override
    public Optional<Book> saveBook(Book bookToSave) {
        jdbcTemplate.update("INSERT into book(isbn, publisher, title, author_id) values(?,?,?,?)",
                bookToSave.getIsbn(),
                bookToSave.getPublisher(),
                bookToSave.getTitle(),
                bookToSave.getAuthorId());

        Long lastInsertId = jdbcTemplate.queryForObject("select LAST_INSERT_ID()", Long.class);
        return Optional.of(getById(lastInsertId));
    }

    @Override
    public Optional<Book> updateBook(Book updatedBook) {
        Long updatedBookId = updatedBook.getId();
        jdbcTemplate.update("UPDATE book set title = ? where id = ?",
                updatedBook.getTitle(),
                updatedBookId);

        return Optional.of(getById(updatedBookId));
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.execute(
                new PreparedStatementCreator() {

                    @Override public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        return connection.prepareStatement("DELETE from book where id = ?");
                    }
                }, new PreparedStatementCallback<Book>() {

                    @Override public Book doInPreparedStatement(PreparedStatement preparedStatement)
                            throws SQLException, DataAccessException {
                        preparedStatement.setLong(1, id);
                        preparedStatement.execute();
                        return null;
                    }
                }
        );
    }

    private RowMapper<Book> getBookRowMapper(){
        return new BookRowMapper();
    }
}
