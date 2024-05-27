package com.alevidals.library.service;

import com.alevidals.library.TestDataUtil;
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
public class ReaderServiceIntegrationTests {

    private ReaderService readerService;

    @Autowired
    public ReaderServiceIntegrationTests(ReaderService readerService) {
        this.readerService = readerService;
    }

    @Test
    public void testThatReaderCanBeCreatedAndRecalled() {
        Reader reader = TestDataUtil.createTestReader();
        readerService.save(reader);
        Optional<Reader> result = readerService.findById(reader.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(reader);
    }

    @Test
    public void testThatMultipleReadersCanBeCreatedAndRecalled() {
        Reader reader = TestDataUtil.createTestReader();
        readerService.save(reader);
        Reader reader2 = TestDataUtil.createTestReader2();
        readerService.save(reader2);
        List<Reader> result = readerService.findAll();
        assertThat(result).hasSize(2).containsExactly(reader, reader2);
    }

    @Test
    public void testThatLoanCanBeUpdated() {
        Reader reader = TestDataUtil.createTestReader();
        readerService.save(reader);

        reader.setName("UPDATED");
        readerService.save(reader.getId(), reader);
        assertThat(reader.getName()).isEqualTo("UPDATED");
    }

    @Test
    public void testThatBooksCanBeDeleted() {
        Reader reader = TestDataUtil.createTestReader();
        readerService.save(reader);

        Optional<Reader> resultBeforeDelete = readerService.findById(reader.getId());
        assertThat(resultBeforeDelete).isPresent();
        assertThat(resultBeforeDelete.get()).isEqualTo(reader);

        readerService.delete(reader.getId());
        Optional<Reader> resultAfterDelete = readerService.findById(reader.getId());
        assertThat(resultAfterDelete).isNotPresent();
    }
}
