package com.library.observer.listeners;

import java.util.concurrent.atomic.AtomicLong;

import com.library.observer.Event;
import com.library.observer.Observer;
import com.library.observer.events.BookAvailabilityEvent;

public class StatisticsTracker implements Observer<Event>{
    
    private final AtomicLong borrowed = new AtomicLong();
    private final AtomicLong returned = new AtomicLong();

    @Override public void onEvent(Event event) {
        if (event instanceof BookAvailabilityEvent e) {
            if (e.available()) returned.incrementAndGet();
            else borrowed.incrementAndGet();
        }
    }

    public long getBorrowed() { return borrowed.get(); }
    public long getReturned() { return returned.get(); }
}