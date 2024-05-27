package com.alevidals.library.controller;

import com.alevidals.library.TestDataUtil;
import com.alevidals.library.model.Reader;
import com.alevidals.library.service.ReaderService;
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
public class ReaderControllerIntegrationTests {

    private MockMvc mockMvc;

    private ReaderService readerService;

    private ObjectMapper objectMapper;

    @Autowired
    public ReaderControllerIntegrationTests(MockMvc mockMvc, ReaderService readerService, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.readerService = readerService;
        this.objectMapper = objectMapper;
    }

    @Test
    public void testThatCreateReaderCreateReaderSuccessfully() throws Exception {
        Reader reader = TestDataUtil.createTestReader();
        String jsonReader = objectMapper.writeValueAsString(reader);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/readers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonReader)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.document").value(reader.getDocument())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(reader.getName())
        );
    }

    @Test
    public void testThatGetReadersReturnsAListOfReaders() throws Exception {
        Reader reader = TestDataUtil.createTestReader();
        readerService.save(reader);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/readers")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].document").value(reader.getDocument())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].name").value(reader.getName())
        );
    }

    @Test
    public void testThatGetReaderReturnsASpecificReaderIfExists() throws Exception {
        Reader reader = TestDataUtil.createTestReader();
        readerService.save(reader);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/readers/" + reader.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.document").value(reader.getDocument())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(reader.getName())
        );
    }

    @Test
    public void testThatGetReaderReturnsNotFoundIfNoExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/readers/" + UUID.randomUUID())
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFullUpdateReaderSuccessfullyUpdateReaderIfExists() throws Exception {
        Reader reader = TestDataUtil.createTestReader();
        readerService.save(reader);
        reader.setDocument("UPDATED DOCUMENT");
        reader.setName("UPDATED NAME");
        String jsonReader = objectMapper.writeValueAsString(reader);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/readers/" + reader.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonReader)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.document").value("UPDATED DOCUMENT")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("UPDATED NAME")
        );
    }

    @Test
    public void testThatFullUpdateReturnsNotFoundIfNoExists() throws Exception {
        Reader reader = TestDataUtil.createTestReader();
        reader.setDocument("UPDATED DOCUMENT");
        reader.setName("UPDATED NAME");
        String jsonReader = objectMapper.writeValueAsString(reader);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/readers/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonReader)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatPartialUpdateReaderSuccessfullyUpdateReaderIfExists() throws Exception {
        Reader reader = TestDataUtil.createTestReader();
        readerService.save(reader);
        reader.setDocument("UPDATED DOCUMENT");
        reader.setName("UPDATED NAME");
        String jsonReader = objectMapper.writeValueAsString(reader);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/readers/" + reader.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonReader)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.document").value("UPDATED DOCUMENT")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("UPDATED NAME")
        );
    }

    @Test
    public void testThatPartialUpdateReturnsNotFoundIfNoExists() throws Exception {
        Reader reader = TestDataUtil.createTestReader();
        reader.setName("UPDATED NAME");
        reader.setDocument("UPDATED DOCUMENt");
        String jsonReader = objectMapper.writeValueAsString(reader);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/readers/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonReader)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatDeleteReaderDeleteBookIfExists() throws Exception {
        Reader reader = TestDataUtil.createTestReader();
        readerService.save(reader);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/readers/" + reader.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void testThatDeleteReaderReturnsNotFoundIfNoExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/readers/" + UUID.randomUUID())
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }
}
