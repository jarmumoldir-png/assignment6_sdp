package com.library.observer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.library.observer.events.BookAvailabilityEvent;
import com.library.observer.listeners.OverdueAlertService;

class OverdueAlertServiceTest {

    @Test
    void aggregatesPerShelf() {
        EventBus bus = new EventBus();
        OverdueAlertService svc = new OverdueAlertService(bus);

        svc.onEvent(new BookAvailabilityEvent("1", "S1", false, 0));
        svc.onEvent(new BookAvailabilityEvent("2", "S1", false, 0));
        svc.onEvent(new BookAvailabilityEvent("3", "S2", false, 0));

        assertEquals(2, svc.overdueCount("S1"));
        assertEquals(1, svc.overdueCount("S2"));
    }

    @Test
    void availableEventsAreIgnored() {
        EventBus bus = new EventBus();
        OverdueAlertService svc = new OverdueAlertService(bus);
        svc.onEvent(new BookAvailabilityEvent("1", "S1", true, 0));
        assertEquals(0, svc.overdueCount("S1"));
    }
}