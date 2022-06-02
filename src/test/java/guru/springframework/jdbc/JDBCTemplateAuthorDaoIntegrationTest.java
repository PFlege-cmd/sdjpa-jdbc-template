package guru.springframework.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import guru.springframework.jdbc.dao.AuthorDao;
import guru.springframework.jdbc.dao.JDBCTemplateAuthorDaoImpl;
import guru.springframework.jdbc.domain.Author;

@DataJpaTest
@ActiveProfiles("local")
@ComponentScan(basePackages = {"guru.springframework.jdbc.dao"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JDBCTemplateAuthorDaoIntegrationTest {


    @Autowired JdbcTemplate jdbcTemplate;

    private AuthorDao authorDao;

    @BeforeEach
    void setUp(){
        this.authorDao = new JDBCTemplateAuthorDaoImpl(jdbcTemplate);
    }

    @Test
    void testFindAuthorByLastNameOrderByFirstName(){
        List<Author> smiths = authorDao.findAuthorByLastNameSortByFirstName("Smith",
                PageRequest.of(0, 10, Sort.by(Sort.Order.asc("first_name"))));
        assertThat(smiths).isNotNull();
        assertThat(smiths.get(0).getFirstName()).isEqualTo("Ahmed");
    }

    @Test
    void testFindAuthorByLastNameOrderByFirstName_page_2(){
        List<Author> smiths = authorDao.findAuthorByLastNameSortByFirstName("Smith",
                PageRequest.of(1, 10, Sort.by(Sort.Order.asc("first_name"))));
        assertThat(smiths).isNotNull();
        assertThat(smiths.get(0).getFirstName()).isEqualTo("Dinesh");
    }

    @Test
    void testGetAuthorById(){
        assertThat(authorDao.getById(1L)).isNotNull();
    }

    @Test
    void testFindAuthorByName(){
        Author author = authorDao.findAuthorByName("Craig", "Walls");

        Assertions.assertThatThrownBy(
                () -> authorDao.findAuthorByName("Grorg", "Lizard")).isInstanceOf(DataAccessException.class);
        assertThat(author.getId() == 1);
        assertThat(author.getFirstName()).isEqualTo("Craig");
        assertThat(author.getLastName()).isEqualTo("Walls");
    }

    @Test
    void testSaveAuthor(){
        Author author = new Author();
        author.setFirstName("Patrick");
        author.setLastName("Flege");

        Author savedAuthor = authorDao.saveAuthor(author);

        assertThat(savedAuthor).isNotNull();
        assertThat(savedAuthor.getId()).isNotNull();
    }

    @Test
    void updateAuthor(){
        Author author = new Author();
        author.setFirstName("Peter");
        author.setLastName("Fliege");

        Author savedAuthor = authorDao.saveAuthor(author);
        savedAuthor.setFirstName("P");
        savedAuthor.setLastName("FL");

        Optional<Author> updatedAuthorOptional = authorDao.updateAuthor(savedAuthor);
        Author updatedAuthor = updatedAuthorOptional.orElse(null);

        assertThat(updatedAuthor).isNotNull();
        assertThat(updatedAuthor.getFirstName()).isNotEqualTo(author.getFirstName());
        assertThat(updatedAuthor.getLastName()).isNotEqualTo(author.getLastName());
        assertThat(updatedAuthor.getId()).isEqualTo(savedAuthor.getId());
    }

    @Test
    void testDeleteBook() {
        Author authorToSave = new Author();
        authorToSave.setLastName("Van Hoff");
        authorToSave.setFirstName("Pim");
        Author savedBook = authorDao.saveAuthor(authorToSave);

        authorDao.deleteAuthorById(savedBook.getId());
        Assertions.assertThatThrownBy(
                () -> authorDao.getById(authorToSave.getId())
                ).isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    void testGetAuthorWithBooks(){
        Optional<Author> authorOptional = authorDao.getByIdWithBooks(1L);

        Author author = authorOptional.orElseGet(() -> null);

        SoftAssertions.assertSoftly(
                softAssertions -> {
                    softAssertions.assertThat(author).isNotNull();
                    softAssertions.assertThat(author.getBookList()).isNotNull();
                    softAssertions.assertThat(author.getBookList()).hasSize(3);
                }
        );
    }

}
