package com.library.integration;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import com.library.model.Loan;
import com.library.model.Shelf;
import com.library.observer.EventBus;
import com.library.observer.EventType;
import com.library.observer.events.BookAvailabilityEvent;
import com.library.observer.events.InspectionReportEvent;

class AlertToInspectionServiceTest {

    @Test
    void alertTriggersInspectionOnSubtreeOnly() {
        LocalDate today = LocalDate.of(2026, 3, 1);
        EventBus bus = new EventBus();

        Shelf target = new Shelf("S1");
        target.add(new Loan("late-1", today.minusDays(5), null));

        AlertToInspectionService service = new AlertToInspectionService(target, bus, today);

        AtomicReference<InspectionReportEvent> report = new AtomicReference<>();
        bus.subscribe(EventType.INSPECTION_REPORT, e -> report.set((InspectionReportEvent) e));

        // Alert for shelf S1 triggers only the subtree inspection
        service.onEvent(new BookAvailabilityEvent("978-2", "S1", false, 0));

        assertNotNull(report.get());
        assertEquals("S1", report.get().shelfId());
        assertEquals(1, report.get().count());
        assertEquals("late-1", report.get().overdueIsbns().get(0));
    }
}