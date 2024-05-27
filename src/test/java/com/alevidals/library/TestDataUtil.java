package com.alevidals.library;

import com.alevidals.library.model.Book;
import com.alevidals.library.model.Loan;
import com.alevidals.library.model.Reader;

public final class TestDataUtil {

    public static Book createTestBook() {
        return Book.builder()
                .isbn("978-3-16-148410-0")
                .title("The book")
                .build();
    }

    public static Book createTestBook2() {
        return Book.builder()
                .isbn("978-3-16-148410-1")
                .title("The book")
                .build();
    }

    public static Reader createTestReader() {
        return Reader.builder()
                .document("44444444K")
                .name("Foo")
                .surnames("Bar")
                .build();
    }

    public static Reader createTestReader2() {
        return Reader.builder()
                .document("11111111K")
                .name("Foo")
                .surnames("Bar")
                .build();
    }

    public static Loan createTestLoan(Book book, Reader reader) {
        return Loan.builder()
                .book(book)
                .reader(reader)
                .build();
    }
}
