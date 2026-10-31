package com.library.model;

import com.library.visitor.LibraryVisitor;

public final class EBook implements LibraryNode {

    private final String isbn;
    private final String title;
    private final String format;

    public EBook(String isbn, String title, String format) {
        this.isbn = isbn;
        this.title = title;
        this.format = format;
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getFormat() { return format; }

    @Override
    public <R> R accept(LibraryVisitor<R> visitor) { return visitor.visit(this); }
}