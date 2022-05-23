package guru.springframework.jdbc;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;

import guru.springframework.jdbc.dao.BookDao;
import guru.springframework.jdbc.domain.Book;

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan("guru.springframework.jdbc.dao")
public class BookDaoIntegrationTest {

    @Autowired
    BookDao bookDao;

    @Test
    void testGetById(){
        Book byId = bookDao.getById(1L);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(byId).isNotNull();
            softAssertions.assertThat(byId.getTitle()).isNotNull();
        });
    }

    @Test
    void testGetByTitle(){

        Book byTitle = bookDao.findByTitle("Clean Code");

        SoftAssertions.assertSoftly(
                softAssertions -> {
                    softAssertions.assertThat(byTitle).isNotNull();
                    softAssertions.assertThat(byTitle.getTitle()).isEqualTo("Clean Code");
                }
        );
    }

    @Test
    void saveNewBook(){
        Book bookToSave = new Book();
        bookToSave.setTitle("Lord of the Flies");
        bookToSave.setPublisher("Gordon & Sons");
        bookToSave.setIsbn("123456");
        bookToSave.setAuthorId(1L);

        Book savedBook = bookDao.saveBook(bookToSave).orElseGet(() -> null);

        Assertions.assertThat(savedBook).isNotNull();
        SoftAssertions.assertSoftly(
                softAssertions -> {
                    softAssertions.assertThat(savedBook).isNotNull();
                    softAssertions.assertThat(savedBook.getId()).isNotNull();
                }
        );
    }

    @Test
    void testUpdateTitle(){
        Book updatedBook = new Book();
        updatedBook.setId(1L);
        updatedBook.setTitle("Spring in INAction");
        updatedBook.setIsbn("978-1617294945");
        updatedBook.setPublisher("Simon & Schuster");
        updatedBook.setAuthorId(1L);
        Book fetchedBook = bookDao.updateBook(updatedBook).orElseGet(() -> null);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(fetchedBook).isNotNull();
            softAssertions.assertThat(fetchedBook.getTitle()).isEqualTo("Spring in INAction");
        });

    }

    @Test
    void testDeleteBook(){
        Book bookToSave = new Book();
        bookToSave.setTitle("Lord of the Rings");
        bookToSave.setPublisher("Mumford & Sons");
        bookToSave.setIsbn("123456");
        bookToSave.setAuthorId(2L);

        Book savedBook = bookDao.saveBook(bookToSave).orElseGet(() -> new Book());
        Long savedBookId = savedBook.getId();

        bookDao.deleteById(savedBookId);

        SoftAssertions.assertSoftly(
                softAssertions -> { softAssertions.assertThat(savedBookId).isNotNull();
                softAssertions.assertThatThrownBy(() -> bookDao.getById(savedBookId))
                        .isInstanceOf(EmptyResultDataAccessException.class);}
        );
    }
}
