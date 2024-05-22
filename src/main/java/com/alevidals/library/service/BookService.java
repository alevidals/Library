package com.alevidals.library.service;

import com.alevidals.library.model.Book;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookService {
    boolean existsByIsbn(String isbn);

    Book save(Book book);

    Book save(UUID id, Book book);

    List<Book> findAll();

    Optional<Book> findById(UUID id);

    boolean existsById(UUID id);

    void delete(UUID id);

    Book partialUpdate(UUID id, Book book);
}
