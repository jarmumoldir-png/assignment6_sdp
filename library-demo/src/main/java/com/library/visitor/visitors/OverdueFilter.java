package com.library.visitor.visitors;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.library.model.Audiobook;
import com.library.model.Book;
import com.library.model.EBook;
import com.library.model.Journal;
import com.library.model.LibraryNode;
import com.library.model.Loan;
import com.library.model.Section;
import com.library.model.Shelf;
import com.library.visitor.LibraryVisitor;

public final class OverdueFilter implements LibraryVisitor<List<Loan>> {

    private final LocalDate today;

    public OverdueFilter(LocalDate today) { this.today = today; }

    @Override public List<Loan> visit(Section s) {
        List<Loan> out = new ArrayList<>();
        for (LibraryNode n : s.getChildren()) out.addAll(n.accept(this));
        return out;
    }
    @Override public List<Loan> visit(Shelf sh) {
        List<Loan> out = new ArrayList<>();
        for (LibraryNode n : sh.getItems()) out.addAll(n.accept(this));
        return out;
    }
    @Override public List<Loan> visit(Book b)    { return List.of(); }
    @Override public List<Loan> visit(Journal j) { return List.of(); }
    @Override public List<Loan> visit(EBook e)   { return List.of(); }
    @Override public List<Loan> visit(Loan l) {
        return l.isOverdue(today) ? List.of(l) : List.of();
    }
    @Override public List<Loan> visit(Audiobook a) { return List.of(); }
}