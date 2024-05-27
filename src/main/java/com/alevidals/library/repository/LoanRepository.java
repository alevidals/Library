package com.alevidals.library.repository;

import com.alevidals.library.model.Loan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface LoanRepository extends CrudRepository<Loan, UUID> {
    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Loan l WHERE l.book.id = ?1 AND l.returnDate IS NULL")
    boolean isBookOnLoan(UUID id);
}
