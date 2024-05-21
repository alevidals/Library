package com.alevidals.library.repository;

import com.alevidals.library.model.Reader;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ReaderRepository extends CrudRepository<Reader, UUID> {
}
