package com.library.visitor.visitors;

import com.library.model.Book;
import com.library.model.EBook;
import com.library.model.Journal;
import com.library.model.LibraryNode;
import com.library.model.Loan;
import com.library.model.Section;
import com.library.model.Shelf;
import com.library.visitor.LibraryVisitor;

public final class CatalogJsonExporter implements LibraryVisitor<String> {

    @Override public String visit(Section s) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"section\":\"").append(esc(s.getName())).append("\",\"children\":[");
        boolean first = true;
        for (LibraryNode n : s.getChildren()) {
            if (!first) sb.append(',');
            first = false;
            sb.append(n.accept(this));
        }
        return sb.append("]}").toString();
    }
    @Override public String visit(Shelf sh) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"shelf\":\"").append(esc(sh.getId())).append("\",\"items\":[");
        boolean first = true;
        for (LibraryNode n : sh.getItems()) {
            if (!first) sb.append(',');
            first = false;
            sb.append(n.accept(this));
        }
        return sb.append("]}").toString();
    }
    @Override public String visit(Book b) {
        return "{\"type\":\"book\",\"isbn\":\"" + esc(b.getIsbn())
             + "\",\"title\":\"" + esc(b.getTitle())
             + "\",\"available\":" + b.isAvailable() + "}";
    }
    @Override public String visit(Journal j) {
        return "{\"type\":\"journal\",\"issn\":\"" + esc(j.getIssn())
             + "\",\"title\":\"" + esc(j.getTitle())
             + "\",\"issue\":" + j.getIssueNumber() + "}";
    }
    @Override public String visit(EBook e) {
        return "{\"type\":\"ebook\",\"isbn\":\"" + esc(e.getIsbn())
             + "\",\"title\":\"" + esc(e.getTitle())
             + "\",\"format\":\"" + esc(e.getFormat()) + "\"}";
    }
    @Override public String visit(Loan l) {
        return "{\"type\":\"loan\",\"isbn\":\"" + esc(l.getIsbn())
             + "\",\"due\":\"" + l.getDueDate() + "\"}";
    }

    private static String esc(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}