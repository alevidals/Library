package com.alevidals.library.controller;

import com.alevidals.library.dto.loan.DetailedLoanDto;
import com.alevidals.library.dto.loan.LoanDto;
import com.alevidals.library.mapper.LoanMapper;
import com.alevidals.library.model.Loan;
import com.alevidals.library.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping
    public ResponseEntity<List<DetailedLoanDto>> getLoans() {
        List<Loan> loans = loanService.findAll();

        return new ResponseEntity<>(
                loans.stream().map(LoanMapper.MAPPER::loanToDetailedLoanDto).toList(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetailedLoanDto> getLoan(@PathVariable UUID id) {
        Optional<Loan> loan = loanService.findById(id);

        return loan.map(foundLoan -> {
            DetailedLoanDto detailedLoanDto = LoanMapper.MAPPER.loanToDetailedLoanDto(foundLoan);
            return new ResponseEntity<>(detailedLoanDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<LoanDto> createLoan(@RequestBody LoanDto loanDto) {
        Loan loan = LoanMapper.MAPPER.loanDtoToLoan(loanDto);
        Loan savedLoan = loanService.save(loan);

        return new ResponseEntity<>(
                LoanMapper.MAPPER.loanToLoanDto(savedLoan),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LoanDto> partialUpdateLoan(
            @PathVariable UUID id,
            @RequestBody LoanDto loanDto
    ) {
        if (!loanService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Loan loan = LoanMapper.MAPPER.loanDtoToLoan(loanDto);
        Loan updatedLoan = loanService.partialUpdate(id, loan);

        return new ResponseEntity<>(
                LoanMapper.MAPPER.loanToLoanDto(updatedLoan),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLoan(@PathVariable UUID id) {
        if (!loanService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        loanService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
