package com.library.visitor.visitors;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.library.model.Audiobook;
import com.library.model.Book;
import com.library.model.EBook;
import com.library.model.Journal;
import com.library.model.LibraryNode;
import com.library.model.Loan;
import com.library.model.Section;
import com.library.model.Shelf;
import com.library.visitor.LibraryVisitor;

public final class FineCalculator implements LibraryVisitor<Double> {

    private final LocalDate today;
    private final double dailyRate;

    public FineCalculator(LocalDate today, double dailyRate) {
        this.today = today;
        this.dailyRate = dailyRate;
    }

    @Override public Double visit(Section s) {
        double sum = 0;
        for (LibraryNode n : s.getChildren()) sum += n.accept(this);
        return sum;
    }
    @Override public Double visit(Shelf sh) {
        double sum = 0;
        for (LibraryNode n : sh.getItems()) sum += n.accept(this);
        return sum;
    }
    @Override public Double visit(Book b) { return 0.0; }
    @Override public Double visit(Journal j) { return 0.0; }
    @Override public Double visit(EBook e) { return 0.0; }
    @Override public Double visit(Loan l) {
        if (l.getReturnedOn() != null || !l.isOverdue(today)) return 0.0;
        long days = ChronoUnit.DAYS.between(l.getDueDate(), today);
        return days * dailyRate;
    }
    @Override public Double visit(Audiobook a) { return 0.0; }
}