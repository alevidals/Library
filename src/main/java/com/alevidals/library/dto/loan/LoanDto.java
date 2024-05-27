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
public class LoanDto {

    private UUID id;

    private UUID bookId;

    private UUID readerId;

    private Date returnDate;

    private Date dueDate;

    private Date createdAt;
}
