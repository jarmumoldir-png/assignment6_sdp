package com.library.observer.events;

import java.util.List;

import com.library.observer.Event;
import com.library.observer.EventType;

public record InspectionReportEvent(
        String shelfId, List<String> overdueIsbns, int count) implements Event {

    @Override public EventType type() { return EventType.INSPECTION_REPORT; }
}