package com.library.visitor;

import com.library.model.Book;
import com.library.model.EBook;
import com.library.model.Journal;
import com.library.model.Loan;
import com.library.model.Section;
import com.library.model.Shelf;

public interface LibraryVisitor<R> {
    R visit(Section section);
    R visit(Shelf shelf);
    R visit(Book book);
    R visit(Journal journal);
    R visit(EBook ebook);
    R visit(Loan loan);
}