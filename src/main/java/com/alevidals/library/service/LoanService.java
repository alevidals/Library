package com.alevidals.library.service;

import com.alevidals.library.model.Loan;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoanService {
    Loan save(Loan loan);

    List<Loan> findAll();

    Optional<Loan> findById(UUID id);

    void delete(UUID id);

    boolean exists(UUID id);

    Loan partialUpdate(UUID id, Loan loan);
}
