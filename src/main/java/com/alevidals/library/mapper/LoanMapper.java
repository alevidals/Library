package com.alevidals.library.mapper;

import com.alevidals.library.dto.loan.DetailedLoanDto;
import com.alevidals.library.dto.loan.LoanDto;
import com.alevidals.library.model.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LoanMapper {

    LoanMapper MAPPER = Mappers.getMapper(LoanMapper.class);

    @Mappings({
            @Mapping(target = "bookId", source = "book.id"),
            @Mapping(target = "readerId", source = "reader.id")
    })
    LoanDto loanToLoanDto(Loan loan);

    @Mappings({
            @Mapping(target = "book.id", source = "bookId"),
            @Mapping(target = "reader.id", source = "readerId")
    })
    Loan loanDtoToLoan(LoanDto loanDto);

    DetailedLoanDto loanToDetailedLoanDto(Loan loan);
}
