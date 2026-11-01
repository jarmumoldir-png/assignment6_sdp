package com.library.visitor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.library.model.Book;
import com.library.model.Section;
import com.library.model.Shelf;
import com.library.visitor.visitors.CatalogJsonExporter;

class CatalogJsonExporterTest {

    @Test
    void nestedStructureIsCorrect() {
        Section root = new Section("Root");
        Shelf shelf = new Shelf("S1");
        shelf.add(new Book("1", "Dune", true, 10));
        root.add(shelf);

        String json = root.accept(new CatalogJsonExporter());

        assertEquals("{\"section\":\"Root\",\"children\":"
                + "[{\"shelf\":\"S1\",\"items\":"
                + "[{\"type\":\"book\",\"isbn\":\"1\",\"title\":\"Dune\",\"available\":true}]}]}",
                json);
    }

    @Test
    void quotesAreEscaped() {
        Shelf shelf = new Shelf("S\"1");
        String json = shelf.accept(new CatalogJsonExporter());
        assertTrue(json.contains("S\\\"1"));
    }
}