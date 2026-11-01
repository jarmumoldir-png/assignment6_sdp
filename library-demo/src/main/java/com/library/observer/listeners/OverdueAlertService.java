package com.library.observer.listeners;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.library.observer.Event;
import com.library.observer.EventType;
import com.library.observer.Observer;
import com.library.observer.Subject;
import com.library.observer.events.BookAvailabilityEvent;

/** Observer AND Subject: aggregates overdue events per shelf, then republishes. */
public final class OverdueAlertService implements Observer<Event>, Subject<Event> {

    private final Subject<Event> publisher;
    private final Map<String, AtomicInteger> overdueByShelf = new ConcurrentHashMap<>();

    public OverdueAlertService(Subject<Event> publisher) {
        this.publisher = publisher;
    }

    @Override public void onEvent(Event event) {
        if (event instanceof BookAvailabilityEvent e && !e.available()) {
            overdueByShelf.computeIfAbsent(e.shelfId(), k -> new AtomicInteger())
                          .incrementAndGet();
        }
    }

    public int overdueCount(String shelfId) {
        var c = overdueByShelf.get(shelfId);
        return c == null ? 0 : c.get();
    }

    @Override public void subscribe(EventType type, Observer<Event> observer) {
        publisher.subscribe(type, observer);
    }
    @Override public void unsubscribe(EventType type, Observer<Event> observer) {
        publisher.unsubscribe(type, observer);
    }
    @Override public void publish(Event event) { publisher.publish(event); }
}