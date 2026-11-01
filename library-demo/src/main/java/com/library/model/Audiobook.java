package com.library.model;

import com.library.visitor.LibraryVisitor;

public final class Audiobook implements LibraryNode {
    private final String isbn;
    private final String title;
    private final int durationMinutes;

    public Audiobook(String isbn, String title, int durationMinutes) {
        this.isbn = isbn;
        this.title = title;
        this.durationMinutes = durationMinutes;
    }
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public int getDurationMinutes() { return durationMinutes; }

    @Override
    public <R> R accept(LibraryVisitor<R> visitor) { return visitor.visit(this); }
}