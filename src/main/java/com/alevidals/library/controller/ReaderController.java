package com.alevidals.library.controller;

import com.alevidals.library.dto.ReaderDto;
import com.alevidals.library.mapper.ReaderMapper;
import com.alevidals.library.model.Reader;
import com.alevidals.library.service.ReaderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/readers")
public class ReaderController {

    private ReaderService readerService;

    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @GetMapping
    public List<ReaderDto> getReaders() {
        List<Reader> readers = readerService.findAll();

        return readers.stream().map(ReaderMapper.MAPPER::readerToReaderDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReaderDto> getReader(@PathVariable UUID id) {
        Optional<Reader> reader = readerService.findById(id);

        return reader.map(foundReader -> {
            ReaderDto readerDto = ReaderMapper.MAPPER.readerToReaderDto(foundReader);
            return new ResponseEntity<>(readerDto, HttpStatus.OK);
        }).orElse(
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    @PostMapping
    public ResponseEntity<ReaderDto> createReader(@RequestBody ReaderDto readerDto) {
        Reader reader = ReaderMapper.MAPPER.readerDtoToReader(readerDto);
        Reader savedReader = readerService.save(reader);

        return new ResponseEntity<>(
                ReaderMapper.MAPPER.readerToReaderDto(savedReader), HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReaderDto> fullUpdateReader(
            @RequestBody ReaderDto readerDto,
            @PathVariable UUID id
    ) {
        if (!readerService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Reader reader = ReaderMapper.MAPPER.readerDtoToReader(readerDto);
        Reader updatedReader = readerService.save(id, reader);
        return new ResponseEntity<>(ReaderMapper.MAPPER.readerToReaderDto(updatedReader), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReaderDto> partialUpdateReader(
            @RequestBody ReaderDto readerDto,
            @PathVariable UUID id
    ) {
        if (!readerService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Reader reader = ReaderMapper.MAPPER.readerDtoToReader(readerDto);
        Reader updatedReader = readerService.partialUpdate(id, reader);
        return new ResponseEntity<>(ReaderMapper.MAPPER.readerToReaderDto(updatedReader), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReader(@PathVariable UUID id) {
        if (!readerService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        readerService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
