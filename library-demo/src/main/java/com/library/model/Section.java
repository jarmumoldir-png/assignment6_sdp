package com.library.model;

import com.library.visitor.LibraryVisitor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Section implements LibraryNode {

    private final String name;
    private final List<LibraryNode> children = new ArrayList<>();

    public Section(String name) { this.name = name; }

    public String getName() { return name; }
    public List<LibraryNode> getChildren() { return Collections.unmodifiableList(children); }
    public void add(LibraryNode node) { children.add(node); }

    @Override
    public <R> R accept(LibraryVisitor<R> visitor) { return visitor.visit(this); }
}