package com.alevidals.library.service.impl;

import com.alevidals.library.model.Book;
import com.alevidals.library.model.Loan;
import com.alevidals.library.model.Reader;
import com.alevidals.library.repository.LoanRepository;
import com.alevidals.library.service.BookService;
import com.alevidals.library.service.LoanService;
import com.alevidals.library.service.ReaderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;

    private final ReaderService readerService;

    private final BookService bookService;

    public LoanServiceImpl(LoanRepository loanRepository, ReaderService readerService, BookService bookService) {
        this.loanRepository = loanRepository;
        this.readerService = readerService;
        this.bookService = bookService;
    }

    @Override
    public List<Loan> findAll() {
        return StreamSupport.stream(
                loanRepository.findAll().spliterator(), false
        ).toList();
    }

    @Override
    public Optional<Loan> findById(UUID id) {
        return loanRepository.findById(id);
    }

    @Override
    public Loan save(Loan loan) {
        Optional<Book> book = bookService.findById(loan.getBook().getId());
        if (book.isEmpty()) {
            throw new RuntimeException("Book does not exists");
        }

        Optional<Reader> reader = readerService.findById(loan.getReader().getId());
        if (reader.isEmpty()) {
            throw new RuntimeException("Reader does not exists");
        }

        if (loanRepository.isBookOnLoan(book.get().getId())) {
            throw new RuntimeException("Book is actually on loan");
        }


        loan.setBook(book.get());
        loan.setReader(reader.get());

        return loanRepository.save(loan);
    }


    @Override
    public Loan partialUpdate(UUID id, Loan loan) {
        return loanRepository.findById(id).map(existingLoan -> {
            Optional.ofNullable(loan.getBook().getId()).ifPresent(bookId -> {
                Optional<Book> book = bookService.findById(bookId);
                if (book.isEmpty()) {
                    throw new RuntimeException("Book does not exists");
                }

                System.out.println(loanRepository.isBookOnLoan(book.get().getId()));

                if (loanRepository.isBookOnLoan(book.get().getId())) {
                    throw new RuntimeException("Book is actually on loan");
                }

                existingLoan.setBook(book.get());
            });

            Optional.ofNullable(loan.getReader().getId()).ifPresent(readerId -> {
                Optional<Reader> reader = readerService.findById(readerId);
                if (reader.isEmpty()) {
                    throw new RuntimeException("Reader does not exists");
                }

                existingLoan.setReader(reader.get());
            });

            Optional.ofNullable(loan.getDueDate()).ifPresent(existingLoan::setDueDate);
            Optional.ofNullable(loan.getReturnDate()).ifPresent(existingLoan::setReturnDate);

            return loanRepository.save(existingLoan);
        }).orElseThrow(() -> new RuntimeException("Loan does not exists"));
    }

    @Override
    public void delete(UUID id) {
        loanRepository.deleteById(id);
    }

    @Override
    public boolean exists(UUID id) {
        return loanRepository.existsById(id);
    }
}

