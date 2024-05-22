package com.alevidals.library.service.impl;

import com.alevidals.library.model.Book;
import com.alevidals.library.repository.BookRepository;
import com.alevidals.library.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public boolean existsByIsbn(String isbn) {
        return bookRepository.existsByIsbn(isbn);
    }

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book save(UUID id, Book book) {
        book.setId(id);
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
        return StreamSupport.stream(
                bookRepository.findAll().spliterator(), false
        ).toList();
    }

    @Override
    public Optional<Book> findById(UUID id) {
        return bookRepository.findById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return bookRepository.existsById(id);
    }

    @Override
    public void delete(UUID id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Book partialUpdate(UUID id, Book book) {
        return bookRepository.findById(id).map(existingBook -> {
            Optional.ofNullable(book.getIsbn()).ifPresent(existingBook::setIsbn);
            Optional.ofNullable(book.getTitle()).ifPresent(existingBook::setTitle);

            return bookRepository.save(existingBook);
        }).orElseThrow(() -> new RuntimeException("Book does not exists"));
    }
}
