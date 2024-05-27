package com.alevidals.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne()
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne()
    @JoinColumn(name = "reader_id", nullable = false)
    private Reader reader;

    @Column(name = "return_date")
    private Date returnDate;

    @Column(name = "due_date")
    private Date dueDate;

    @Column(name = "created_at")
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        if (getCreatedAt() == null) {
            setCreatedAt(new Date());
        }

        if (getDueDate() == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_YEAR, 7);
            setDueDate(calendar.getTime());
        }

        validateDates();
    }

    @PreUpdate
    protected void onUpdate() {
        validateDates();
    }

    private void validateDates() {
        if (getReturnDate() != null && getReturnDate().before(getCreatedAt())) {
            throw new IllegalArgumentException("Returned date cannot be before loan date");
        }
    }
}
