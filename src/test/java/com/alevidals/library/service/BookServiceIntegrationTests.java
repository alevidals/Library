package com.alevidals.library.service;

import com.alevidals.library.TestDataUtil;
import com.alevidals.library.model.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookServiceIntegrationTests {

    private BookService bookService;

    @Autowired
    public BookServiceIntegrationTests(BookService bookService) {
        this.bookService = bookService;
    }

    @Test
    public void testThatBookCanBeCreatedAndRecalled() {
        Book book = TestDataUtil.createTestBook();
        bookService.save(book);
        Optional<Book> result = bookService.findById(book.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(book);
    }

    @Test
    public void testThatMultipleBooksCanBeCreatedAndRecalled() {
        Book book = TestDataUtil.createTestBook();
        bookService.save(book);
        Book book2 = TestDataUtil.createTestBook2();
        bookService.save(book2);
        List<Book> result = bookService.findAll();
        assertThat(result).hasSize(2).containsExactly(book, book2);
    }

    @Test
    public void testThatBookCanBeFullUpdated() {
        Book book = TestDataUtil.createTestBook();
        bookService.save(book);

        book.setTitle("UPDATED");
        bookService.save(book.getId(), book);
        assertThat(book.getTitle()).isEqualTo("UPDATED");
    }

    @Test
    public void testThatBookCanBePartialUpdated() {
        Book book = TestDataUtil.createTestBook();
        bookService.save(book);
        book.setTitle("UPDATED");
        bookService.partialUpdate(book.getId(), book);
        assertThat(book.getTitle()).isEqualTo("UPDATED");
    }

    @Test
    public void testThatBooksCanBeDeleted() {
        Book book = TestDataUtil.createTestBook();
        bookService.save(book);

        Optional<Book> resultBeforeDelete = bookService.findById(book.getId());
        assertThat(resultBeforeDelete).isPresent();
        assertThat(resultBeforeDelete.get()).isEqualTo(book);

        bookService.delete(book.getId());
        Optional<Book> resultAfterDelete = bookService.findById(book.getId());
        assertThat(resultAfterDelete).isNotPresent();
    }
}
