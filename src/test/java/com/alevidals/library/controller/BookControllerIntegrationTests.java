package com.alevidals.library.controller;

import com.alevidals.library.TestDataUtil;
import com.alevidals.library.model.Book;
import com.alevidals.library.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookControllerIntegrationTests {

    private MockMvc mockMvc;

    private BookService bookService;

    private ObjectMapper objectMapper;

    @Autowired
    public BookControllerIntegrationTests(MockMvc mockMvc, BookService bookService, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.bookService = bookService;
        this.objectMapper = objectMapper;
    }

    @Test
    public void testThatCreateBookCreateBookSuccessfully() throws Exception {
        Book book = TestDataUtil.createTestBook();
        String jsonBook = objectMapper.writeValueAsString(book);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBook)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle())
        );
    }

    @Test
    public void testThatCreateBooksReturnsConflictIfIsbnActuallyExists() throws Exception {
        Book book = TestDataUtil.createTestBook();
        bookService.save(book);
        String jsonBook = objectMapper.writeValueAsString(book);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBook)
        ).andExpect(
                MockMvcResultMatchers.status().isConflict()
        );
    }

    @Test
    public void testThatGetBooksReturnsAListOfBooks() throws Exception {
        Book book = TestDataUtil.createTestBook();
        bookService.save(book);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].isbn").value(book.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].title").value(book.getTitle())
        );
    }

    @Test
    public void testThatGetBookReturnsASpecificBookIfExists() throws Exception {
        Book book = TestDataUtil.createTestBook();
        bookService.save(book);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/" + book.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle())
        );
    }

    @Test
    public void testThatGetBookReturnsNotFoundIfNoExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/" + UUID.randomUUID())
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFullUpdateBookSuccessfullyUpdateBookIfExists() throws Exception {
        Book book = TestDataUtil.createTestBook();
        bookService.save(book);
        book.setIsbn("UPDATED ISBN");
        book.setTitle("UPDATED TITLE");
        String jsonBook = objectMapper.writeValueAsString(book);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBook)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value("UPDATED ISBN")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value("UPDATED TITLE")
        );
    }

    @Test
    public void testThatFullUpdateReturnsNotFoundIfNoExists() throws Exception {
        Book book = TestDataUtil.createTestBook();
        book.setIsbn("UPDATED ISBN");
        book.setTitle("UPDATED TITLE");
        String jsonBook = objectMapper.writeValueAsString(book);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBook)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatPartialUpdateBookSuccessfullyUpdateBookIfExists() throws Exception {
        Book book = TestDataUtil.createTestBook();
        bookService.save(book);
        book.setIsbn("UPDATED ISBN");
        book.setTitle("UPDATED TITLE");
        String jsonBook = objectMapper.writeValueAsString(book);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/" + book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBook)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value("UPDATED ISBN")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value("UPDATED TITLE")
        );
    }

    @Test
    public void testThatPartialUpdateReturnsNotFoundIfNoExists() throws Exception {
        Book book = TestDataUtil.createTestBook();
        book.setIsbn("UPDATED ISBN");
        book.setTitle("UPDATED TITLE");
        String jsonBook = objectMapper.writeValueAsString(book);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBook)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatDeleteBookDeleteBookIfExists() throws Exception {
        Book book = TestDataUtil.createTestBook();
        bookService.save(book);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/" + book.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void testThatDeleteBookReturnsNotFoundIfNoExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/" + UUID.randomUUID())
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }
}
