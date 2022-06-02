package guru.springframework.jdbc.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import guru.springframework.jdbc.domain.Author;

@Repository
public class JDBCTemplateAuthorDaoImpl implements AuthorDao {

    private final JdbcTemplate jdbcTemplate;

    public JDBCTemplateAuthorDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override public Author saveAuthor(Author author) {
        jdbcTemplate.update("INSERT into author(first_name, last_name) values (?,?)", author.getFirstName(), author.getLastName());
        Long id = jdbcTemplate.queryForObject("select LAST_INSERT_ID()",Long.class);
        return jdbcTemplate.queryForObject("select * from author where id = ?",getRowMapper(),id);
    }

    @Override public Author getById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * from author where id = ?", getRowMapper(), id);
    }

    @Override public Author findAuthorByName(String firstName, String lastName) {
        return jdbcTemplate.queryForObject("SELECT * from author where first_name = ? and last_name = ?",
                getRowMapper(),
                firstName,
                lastName);
    }

    @Override public Optional<Author> updateAuthor(Author author) {
        jdbcTemplate.update("UPDATE author set first_name = ?, last_name = ? where id = ?", author.getFirstName(), author.getLastName(), author.getId());
        return Optional.of(jdbcTemplate.queryForObject("SELECT * from author where id = ?", getRowMapper(),author.getId()));
    }

    @Override public void deleteAuthorById(Long id) {
        jdbcTemplate.update("DELETE from author where id = ?", id);
    }

    @Override public Optional getByIdWithBooks(Long id){
        String sql = "select author.id as id, author.first_name, "
                + "author.last_name, book.id as book_id, book.author_id,"
                + "book.title, book.isbn, book.publisher "
                + "from author left outer join book on author.id = book.author_id"
                + " where author.id = ?";

        Author authorWithBooks = jdbcTemplate.query(sql, new AuthorResultSetExtractor(), id);
        return Optional.of(authorWithBooks);
    }

    @Override public List<Author> findAuthorByLastNameSortByFirstName(String lastName, Pageable pageable) {
        String sql = "SELECT * from author where last_name = ? order by first_name "
                + pageable.getSort().getOrderFor("first_name").getDirection() + " limit ? offset ?";

        return jdbcTemplate.query(sql, getRowMapper(), lastName, pageable.getPageSize(), pageable.getOffset());
    }

    private RowMapper<Author> getRowMapper(){
        return new AuthorRowMapper();
    }
}
