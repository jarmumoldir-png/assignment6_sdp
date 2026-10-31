package com.library.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.library.visitor.LibraryVisitor;

public final class Shelf implements LibraryNode {

    private final String id;
    private final List<LibraryNode> items = new ArrayList<>();

    public Shelf(String id) { this.id = id; }

    public String getId() { return id; }
    public List<LibraryNode> getItems() { return Collections.unmodifiableList(items); }
    public void add(LibraryNode node) { items.add(node); }

    @Override
    public <R> R accept(LibraryVisitor<R> visitor) { return visitor.visit(this); }
}