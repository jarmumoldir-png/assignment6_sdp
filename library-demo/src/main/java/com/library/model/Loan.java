package com.library.model;

import java.time.LocalDate;

import com.library.visitor.LibraryVisitor;

public final class Loan implements LibraryNode {

    private final String isbn;
    private final LocalDate dueDate;
    private final LocalDate returnedOn;   // null if not returned

    public Loan(String isbn, LocalDate dueDate, LocalDate returnedOn) {
        this.isbn = isbn;
        this.dueDate = dueDate;
        this.returnedOn = returnedOn;
    }

    public String getIsbn() { return isbn; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnedOn() { return returnedOn; }
    public boolean isOverdue(LocalDate today) {
        return returnedOn == null && dueDate.isBefore(today);
    }

    @Override
    public <R> R accept(LibraryVisitor<R> visitor) { return visitor.visit(this); }
}