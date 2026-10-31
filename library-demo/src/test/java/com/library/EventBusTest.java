package com.library;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.library.observer.Event;
import com.library.observer.EventBus;
import com.library.observer.EventType;
import com.library.observer.Observer;

class EventBusTest {

    record SimpleEvent(EventType type) implements Event { }

    @Test
    void subscribeThenPublishDeliversEvent() {
        EventBus bus = new EventBus();
        AtomicInteger received = new AtomicInteger();
        bus.subscribe(EventType.BOOK_AVAILABLE, e -> received.incrementAndGet());

        bus.publish(new SimpleEvent(EventType.BOOK_AVAILABLE));

        assertEquals(1, received.get());
    }

    @Test
    void unsubscribeStopsDelivery() {
        EventBus bus = new EventBus();
        AtomicInteger received = new AtomicInteger();
        Observer<Event> observer = e -> received.incrementAndGet();

        bus.subscribe(EventType.BOOK_AVAILABLE, observer);
        bus.unsubscribe(EventType.BOOK_AVAILABLE, observer);
        bus.publish(new SimpleEvent(EventType.BOOK_AVAILABLE));

        assertEquals(0, received.get());
    }

    @Test
    void listenerCanUnsubscribeInsideHandler() {
        EventBus bus = new EventBus();
        AtomicInteger received = new AtomicInteger();
        Observer<Event>[] self = new Observer[1];
        self[0] = e -> {
            received.incrementAndGet();
            bus.unsubscribe(EventType.BOOK_AVAILABLE, self[0]);
        };
        bus.subscribe(EventType.BOOK_AVAILABLE, self[0]);

        bus.publish(new SimpleEvent(EventType.BOOK_AVAILABLE));
        bus.publish(new SimpleEvent(EventType.BOOK_AVAILABLE));

        assertEquals(1, received.get()); // only first delivery should count
    }

    @Test
    void subscribersOnlyReceiveTheirEventType() {
        EventBus bus = new EventBus();
        AtomicInteger available = new AtomicInteger();
        AtomicInteger borrowed = new AtomicInteger();
        bus.subscribe(EventType.BOOK_AVAILABLE, e -> available.incrementAndGet());
        bus.subscribe(EventType.BOOK_BORROWED, e -> borrowed.incrementAndGet());

        bus.publish(new SimpleEvent(EventType.BOOK_AVAILABLE));

        assertEquals(1, available.get());
        assertEquals(0, borrowed.get());
    }
}