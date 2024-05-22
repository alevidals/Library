package com.alevidals.library.controller;

import com.alevidals.library.dto.BookDto;
import com.alevidals.library.mapper.BookMapper;
import com.alevidals.library.model.Book;
import com.alevidals.library.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookDto> getBooks() {
        List<Book> books = bookService.findAll();

        return books.stream().map(BookMapper.MAPPER::bookToBookDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable UUID id) {
        Optional<Book> book = bookService.findById(id);

        return book.map(foundBook -> {
            BookDto bookDto = BookMapper.MAPPER.bookToBookDto(foundBook);
            return new ResponseEntity<>(bookDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookDto) {
        if (bookService.existsByIsbn(bookDto.getIsbn())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Book book = BookMapper.MAPPER.bookDtoToBook(bookDto);
        Book savedBook = bookService.save(book);
        return new ResponseEntity<>(
                BookMapper.MAPPER.bookToBookDto(savedBook),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> fullUpdateBook(
            @PathVariable UUID id,
            @RequestBody BookDto bookDto
    ) {
        if (!bookService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Book book = BookMapper.MAPPER.bookDtoToBook(bookDto);
        Book updatedBook = bookService.save(id, book);
        return new ResponseEntity<>(
                BookMapper.MAPPER.bookToBookDto(updatedBook),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookDto> partialUpdateBook(
            @PathVariable UUID id,
            @RequestBody BookDto bookDto
    ) {
        if (!bookService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Book book = BookMapper.MAPPER.bookDtoToBook(bookDto);
        Book updatedBook = bookService.partialUpdate(id, book);
        return new ResponseEntity<>(
                BookMapper.MAPPER.bookToBookDto(updatedBook),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable UUID id) {
        if (!bookService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        bookService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
