package com.alevidals.library.dto.loan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailedLoanDto {

    private UUID id;

    private BookDto book;

    private ReaderDto reader;

    private Date returnDate;

    private Date dueDate;

    private Date createdAt;
}
