package com.library.observer.events;

import com.library.observer.Event;
import com.library.observer.EventType;

public record BookAvailabilityEvent(
        String isbn, String shelfId, boolean available, long timestamp) implements Event {

    @Override public EventType type() {
        return available ? EventType.BOOK_AVAILABLE : EventType.BOOK_BORROWED;
    }
}