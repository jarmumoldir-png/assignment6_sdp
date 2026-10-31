package com.library.model;

import com.library.visitor.LibraryVisitor;

public final class Journal implements LibraryNode {

    private final String issn;
    private final String title;
    private final int issueNumber;

    public Journal(String issn, String title, int issueNumber) {
        this.issn = issn;
        this.title = title;
        this.issueNumber = issueNumber;
    }

    public String getIssn() { return issn; }
    public String getTitle() { return title; }
    public int getIssueNumber() { return issueNumber; }

    @Override
    public <R> R accept(LibraryVisitor<R> visitor) { return visitor.visit(this); }
}