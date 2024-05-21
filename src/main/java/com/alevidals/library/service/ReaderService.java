package com.alevidals.library.service;

import com.alevidals.library.model.Reader;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReaderService {
    List<Reader> findAll();

    Reader save(Reader reader);

    Reader save(UUID id, Reader reader);

    Optional<Reader> findById(UUID id);

    boolean exists(UUID id);

    void delete(UUID id);

    Reader partialUpdate(UUID id, Reader reader);
}
