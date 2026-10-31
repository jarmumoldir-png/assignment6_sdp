package com.library.model;

import com.library.visitor.LibraryVisitor;

public interface LibraryNode {
    <R> R accept(LibraryVisitor<R> visitor);
}