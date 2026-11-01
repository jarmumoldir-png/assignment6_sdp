package com.library.visitor;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.library.model.Loan;
import com.library.model.Shelf;
import com.library.visitor.visitors.OverdueFilter;

class OverdueFilterTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 3, 1);

    @Test
    void returnsOnlyOverdueLoansOnSubtree() {
        Shelf target = new Shelf("S1");
        target.add(new Loan("late-1", TODAY.minusDays(3), null));
        target.add(new Loan("ok-1", TODAY.plusDays(3), null));

        Shelf other = new Shelf("S2");
        other.add(new Loan("late-2", TODAY.minusDays(9), null));

        List<Loan> result = target.accept(new OverdueFilter(TODAY));

        assertEquals(1, result.size());
        assertEquals("late-1", result.get(0).getIsbn());
    }

    @Test
    void returnedLoansAreNotOverdue() {
        Shelf shelf = new Shelf("S1");
        shelf.add(new Loan("1", TODAY.minusDays(30), TODAY.minusDays(20)));
        assertTrue(shelf.accept(new OverdueFilter(TODAY)).isEmpty());
    }
}