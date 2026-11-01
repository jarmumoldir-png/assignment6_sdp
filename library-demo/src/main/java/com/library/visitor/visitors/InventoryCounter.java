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

public final class InventoryCounter implements LibraryVisitor<Void> {

    private int books, journals, ebooks;
    private final Map<String, Integer> perSection = new HashMap<>();
    private String currentSection;

    @Override public Void visit(Section s) {
        String previous = currentSection;
        currentSection = s.getName();
        for (LibraryNode n : s.getChildren()) n.accept(this);
        currentSection = previous;
        return null;
    }
    @Override public Void visit(Shelf sh) {
        for (LibraryNode n : sh.getItems()) n.accept(this);
        return null;
    }
    @Override public Void visit(Book b)    { books++;    bump(); return null; }
    @Override public Void visit(Journal j) { journals++; bump(); return null; }
    @Override public Void visit(EBook e)   { ebooks++;   bump(); return null; }
    @Override public Void visit(Loan l)    { return null; }

    private void bump() {
        if (currentSection != null)
            perSection.merge(currentSection, 1, Integer::sum);
    }

    public int getBooks()    { return books; }
    public int getJournals() { return journals; }
    public int getEbooks()   { return ebooks; }
    public Map<String, Integer> getPerSection() { return Map.copyOf(perSection); }

    @Override public Void visit(Audiobook a) { return null; }
}