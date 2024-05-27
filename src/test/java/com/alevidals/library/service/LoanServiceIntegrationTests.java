package com.alevidals.library.service;

import com.alevidals.library.TestDataUtil;
import com.alevidals.library.model.Book;
import com.alevidals.library.model.Loan;
import com.alevidals.library.model.Reader;
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
public class LoanServiceIntegrationTests {

    private final LoanService loanService;

    private final BookService bookService;

    private final ReaderService readerService;

    @Autowired
    public LoanServiceIntegrationTests(LoanService loanService, BookService bookService, ReaderService readerService) {
        this.loanService = loanService;
        this.bookService = bookService;
        this.readerService = readerService;
    }

    @Test
    public void testThatLoanCanBeCreatedAndRecalled() {
        Book book = TestDataUtil.createTestBook();
        bookService.save(book);
        Reader reader = TestDataUtil.createTestReader();
        readerService.save(reader);
        Loan loan = TestDataUtil.createTestLoan(book, reader);
        loanService.save(loan);

        Optional<Loan> result = loanService.findById(loan.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(loan.getId());
        assertThat(result.get().getBook()).isEqualTo(loan.getBook());
        assertThat(result.get().getReader()).isEqualTo(loan.getReader());
    }

    @Test
    public void testThatMultipleLoansCanBeCreatedAndRecalled() {
        Book book = TestDataUtil.createTestBook();
        bookService.save(book);
        Book book2 = TestDataUtil.createTestBook2();
        bookService.save(book2);
        Reader reader = TestDataUtil.createTestReader();
        readerService.save(reader);
        Loan loan = TestDataUtil.createTestLoan(book, reader);
        Loan loan2 = TestDataUtil.createTestLoan(book2, reader);
        loanService.save(loan);
        loanService.save(loan2);

        List<Loan> result = loanService.findAll();
        assertThat(result).hasSize(2);
    }

    @Test
    public void testThatLoanCanBeUpdated() {
        Book book = TestDataUtil.createTestBook();
        bookService.save(book);
        Book book2 = TestDataUtil.createTestBook2();
        bookService.save(book2);
        Reader reader = TestDataUtil.createTestReader();
        readerService.save(reader);
        Loan loan = TestDataUtil.createTestLoan(book, reader);
        loanService.save(loan);

        loan.setBook(book2);
        loanService.partialUpdate(loan.getId(), loan);
        assertThat(loan.getBook()).isEqualTo(book2);
    }

    @Test
    public void testThatLoanCanBeDeleted() {
        Book book = TestDataUtil.createTestBook();
        bookService.save(book);
        Reader reader = TestDataUtil.createTestReader();
        readerService.save(reader);
        Loan loan = TestDataUtil.createTestLoan(book, reader);
        loanService.save(loan);

        Optional<Loan> resultBeforeDelete = loanService.findById(loan.getId());
        assertThat(resultBeforeDelete).isPresent();
        assertThat(resultBeforeDelete.get().getId()).isEqualTo(loan.getId());

        loanService.delete(loan.getId());
        Optional<Loan> resultAfterDelete = loanService.findById(loan.getId());
        assertThat(resultAfterDelete).isNotPresent();
    }
}
