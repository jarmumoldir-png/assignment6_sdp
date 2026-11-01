package com.library.observer.listeners;

import com.library.observer.Event;
import com.library.observer.Observer;
import com.library.observer.events.BookAvailabilityEvent;

public final class ReservationNotifier implements Observer<Event> {

    @Override public void onEvent(Event event) {
        if (event instanceof BookAvailabilityEvent e && e.available()) {
            System.out.println("[ReservationNotifier] Notify waiting list for "
                    + e.isbn() + " on shelf " + e.shelfId());
        }
    }
}