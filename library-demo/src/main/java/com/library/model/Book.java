package com.library.model;

import com.library.visitor.LibraryVisitor;

public final class Book implements LibraryNode {

    private final String isbn;
    private final String title;
    private final boolean available;
    private final double replacementCost;

    public Book(String isbn, String title, boolean available, double replacementCost) {
        this.isbn = isbn;
        this.title = title;
        this.available = available;
        this.replacementCost = replacementCost;
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public boolean isAvailable() { return available; }
    public double getReplacementCost() { return replacementCost; }

    @Override
    public <R> R accept(LibraryVisitor<R> visitor) { return visitor.visit(this); }
}