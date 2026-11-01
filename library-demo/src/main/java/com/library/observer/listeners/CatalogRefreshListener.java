package com.library.observer.listeners;

import com.library.observer.Event;
import com.library.observer.EventType;
import com.library.observer.Observer;

public final class CatalogRefreshListener implements Observer<Event> {

    @Override public void onEvent(Event event) {
        if (event.type() == EventType.BOOK_BORROWED || event.type() == EventType.BOOK_RETURNED) {
            System.out.println("[CatalogRefreshListener] event " + event.type());
        }
    }
}