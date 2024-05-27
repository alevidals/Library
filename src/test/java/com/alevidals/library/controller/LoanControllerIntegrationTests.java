package com.alevidals.library.controller;

import com.alevidals.library.TestDataUtil;
import com.alevidals.library.dto.loan.LoanDto;
import com.alevidals.library.model.Book;
import com.alevidals.library.model.Loan;
import com.alevidals.library.model.Reader;
import com.alevidals.library.repository.LoanRepository;
import com.alevidals.library.service.BookService;
import com.alevidals.library.service.LoanService;
import com.alevidals.library.service.ReaderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class LoanControllerIntegrationTests {

    private final MockMvc mockMvc;

    private final LoanRepository loanRepository;

    private final ObjectMapper objectMapper;

    private final BookService bookService;

    private final ReaderService readerService;

    @Autowired
    public LoanControllerIntegrationTests(MockMvc mockMvc, LoanRepository loanRepository, ObjectMapper objectMapper, BookService bookService, ReaderService readerService, LoanService loanService) {
        this.mockMvc = mockMvc;
        this.loanRepository = loanRepository;
        this.objectMapper = objectMapper;
        this.bookService = bookService;
        this.readerService = readerService;
    }

    @Test
    public void testThatLoanCanBeCreated() throws Exception {
        Book book = TestDataUtil.createTestBook();
        bookService.save(book);
        Reader reader = TestDataUtil.createTestReader();
        readerService.save(reader);
        Loan loan = TestDataUtil.createTestLoan(book, reader);
        LoanDto loanDto = LoanDto.builder()
                .bookId(loan.getBook().getId())
                .readerId(loan.getReader().getId())
                .build();
        String jsonLoan = objectMapper.writeValueAsString(loanDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLoan)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.bookId").value(loan.getBook().getId().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.readerId").value(loan.getReader().getId().toString())
        );
    }

    @Test
    public void testThatLoanCanNotBeCreatedIfBookIsOnLoan() throws Exception {
        Book book = TestDataUtil.createTestBook();
        bookService.save(book);
        Reader reader = TestDataUtil.createTestReader();
        readerService.save(reader);
        Loan loan = TestDataUtil.createTestLoan(book, reader);
        LoanDto loanDto = LoanDto.builder()
                .bookId(loan.getBook().getId())
                .readerId(loan.getReader().getId())
                .build();
        loanRepository.save(loan);

        String jsonLoan = objectMapper.writeValueAsString(loanDto);

        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.post("/loans")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonLoan)
            );
        } catch (Exception e) {
            assertEquals("Request processing failed: java.lang.RuntimeException: Book is actually on loan", e.getMessage());
        }
    }

    @Test
    public void testThatGetLoansReturnsLoans() throws Exception {
        Book book = TestDataUtil.createTestBook();
        bookService.save(book);
        Reader reader = TestDataUtil.createTestReader();
        readerService.save(reader);
        Loan loan = TestDataUtil.createTestLoan(book, reader);
        loanRepository.save(loan);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/loans")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].book.isbn").value(loan.getBook().getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].reader.document").value(loan.getReader().getDocument())
        );
    }

    @Test
    public void testThatGetLoanReturnsSpecificLoan() throws Exception {
        Book book = TestDataUtil.createTestBook();
        bookService.save(book);
        Reader reader = TestDataUtil.createTestReader();
        readerService.save(reader);
        Loan loan = TestDataUtil.createTestLoan(book, reader);
        loanRepository.save(loan);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/loans/" + loan.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.book.isbn").value(loan.getBook().getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.reader.document").value(loan.getReader().getDocument())
        );
    }

    @Test
    public void testThatGetLoanReturnsNotFound() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/loans/" + UUID.randomUUID())
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatUpdateLoanUpdateLoanSuccessfully() throws Exception {
        Book book = TestDataUtil.createTestBook();
        bookService.save(book);
        Reader reader = TestDataUtil.createTestReader();
        readerService.save(reader);
        Loan loan = TestDataUtil.createTestLoan(book, reader);
        loanRepository.save(loan);

        Date updatedDate = new Date();
        LoanDto loanDto = LoanDto.builder()
                .dueDate(updatedDate)
                .build();
        String jsonLoan = objectMapper.writeValueAsString(loanDto);

        LocalDate date = LocalDate.now();
        String expectedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        System.out.println(expectedDate);

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.patch("/loans/" + loan.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLoan)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();

        String extractedValue = JsonPath.read(jsonResponse, "$.dueDate").toString().split("T")[0];

        assertEquals(extractedValue, expectedDate);
    }

    @Test
    public void testThatUpdateLoanReturnsNotFound() throws Exception {
        Book book = TestDataUtil.createTestBook();
        bookService.save(book);
        Reader reader = TestDataUtil.createTestReader();
        readerService.save(reader);
        Loan loan = TestDataUtil.createTestLoan(book, reader);
        LoanDto loanDto = LoanDto.builder()
                .bookId(loan.getBook().getId())
                .readerId(loan.getReader().getId())
                .build();

        String jsonLoan = objectMapper.writeValueAsString(loanDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/loans/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLoan)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatDeleteLoanDeleteLoanSuccessfully() throws Exception {
        Book book = TestDataUtil.createTestBook();
        bookService.save(book);
        Reader reader = TestDataUtil.createTestReader();
        readerService.save(reader);
        Loan loan = TestDataUtil.createTestLoan(book, reader);
        loanRepository.save(loan);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/loans/" + loan.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void testThatDeleteLoanReturnsNotFound() throws Exception {
        Book book = TestDataUtil.createTestBook();
        bookService.save(book);
        Reader reader = TestDataUtil.createTestReader();
        readerService.save(reader);
        Loan loan = TestDataUtil.createTestLoan(book, reader);
        LoanDto loanDto = LoanDto.builder()
                .bookId(loan.getBook().getId())
                .readerId(loan.getReader().getId())
                .build();
        String jsonLoan = objectMapper.writeValueAsString(loanDto);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/loans/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLoan)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

}
