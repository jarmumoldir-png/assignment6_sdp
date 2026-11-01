package com.library.visitor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.library.model.Book;
import com.library.model.EBook;
import com.library.model.Journal;
import com.library.model.Section;
import com.library.model.Shelf;
import com.library.visitor.visitors.InventoryCounter;

class InventoryCounterTest {

    @Test
    void countsAccumulateAcrossWholeTree() {
        Section root = new Section("RootSection");
        Shelf s1 = new Shelf("S1");
        s1.add(new Book("1", "A", true, 10));
        s1.add(new Book("2", "B", true, 10));
        
        Shelf s2 = new Shelf("S2");
        s2.add(new Journal("j1", "Nature", 1));
        s2.add(new EBook("e1", "Clean Code", "PDF"));
        
        root.add(s1);
        root.add(s2);

        InventoryCounter counter = new InventoryCounter();
        root.accept(counter);

        assertEquals(2, counter.getBooks());
        assertEquals(1, counter.getJournals());
        assertEquals(1, counter.getEbooks());
        assertEquals(4, counter.getPerSection().get("RootSection")); // Total items in section
    }
}