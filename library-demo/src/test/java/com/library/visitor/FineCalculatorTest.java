package com.library.visitor;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.library.model.Loan;
import com.library.model.Section;
import com.library.model.Shelf;
import com.library.visitor.visitors.FineCalculator;

class FineCalculatorTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 3, 1);

    @Test
    void handCalculatedFinesMatch() {
        Shelf shelf = new Shelf("S1");
        // 5 days late × 0.50 = 2.50
        shelf.add(new Loan("1", TODAY.minusDays(5), null));
        // 10 days late × 0.50 = 5.00
        shelf.add(new Loan("2", TODAY.minusDays(10), null));
        // returned on time → 0
        shelf.add(new Loan("3", TODAY.minusDays(20), TODAY.minusDays(5)));

        double result = shelf.accept(new FineCalculator(TODAY, 0.50));

        assertEquals(7.50, result, 0.001);
    }

    @Test
    void emptyTreeReturnsZero() {
        Section root = new Section("Root");
        assertEquals(0.0, root.accept(new FineCalculator(TODAY, 1.0)), 0.001);
    }
}