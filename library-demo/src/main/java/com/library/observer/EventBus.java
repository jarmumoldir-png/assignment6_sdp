package com.library.observer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public final class EventBus implements Subject<Event> {

    private final Map<EventType, CopyOnWriteArrayList<Observer<Event>>> registry
            = new ConcurrentHashMap<>();

    @Override
    public void subscribe(EventType type, Observer<Event> observer) {
        registry.computeIfAbsent(type, k -> new CopyOnWriteArrayList<>()).add(observer);
    }

    @Override
    public void unsubscribe(EventType type, Observer<Event> observer) {
        var list = registry.get(type);
        if (list != null) list.remove(observer);
    }

    @Override
    public void publish(Event event) {
        var list = registry.get(event.type());
        if (list == null) return;
        for (var observer : list) {
            observer.onEvent(event);   // snapshot iteration, safe to unsubscribe inside
        }
    }
}