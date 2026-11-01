package com.library.visitor.visitors;

import java.util.HashMap;
import java.util.Map;

import com.library.model.Audiobook;
import com.library.model.Book;
import com.library.model.EBook;
import com.library.model.Journal;
import com.library.model.LibraryNode;
import com.library.model.Loan;
import com.library.model.Section;
import com.library.model.Shelf;
import com.library.visitor.LibraryVisitor;

// A new visitor that calculates late fees by section, without touching any model classes.
public final class LateFeeAuditVisitor implements LibraryVisitor<Void> {

    private final Map<String, Double> feesBySection = new HashMap<>();
    private String currentSection = "Unknown";

    @Override public Void visit(Section s) {
        String prev = currentSection;
        currentSection = s.getName();
        for (LibraryNode n : s.getChildren()) n.accept(this);
        currentSection = prev;
        return null;
    }
    @Override public Void visit(Shelf sh) {
        for (LibraryNode n : sh.getItems()) n.accept(this);
        return null;
    }
    @Override public Void visit(Loan l) {
        if (l.isOverdue(java.time.LocalDate.now())) {
            feesBySection.merge(currentSection, 5.0, Double::sum);
        }
        return null;
    }
    @Override public Void visit(Book b) { return null; }
    @Override public Void visit(Journal j) { return null; }
    @Override public Void visit(EBook e) { return null; }

    public Map<String, Double> getFeesBySection() { return Map.copyOf(feesBySection); }

    @Override public Void visit(Audiobook a) { return null; }
}