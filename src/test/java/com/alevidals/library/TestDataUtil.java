package com.alevidals.library;

import com.alevidals.library.model.Book;

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
}
