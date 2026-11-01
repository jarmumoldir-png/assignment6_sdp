package com.library.integration;

import java.time.LocalDate;
import java.util.List;

import com.library.model.Shelf;
import com.library.observer.Event;
import com.library.observer.Observer;
import com.library.observer.Subject;
import com.library.observer.events.BookAvailabilityEvent;
import com.library.observer.events.InspectionReportEvent;
import com.library.visitor.visitors.OverdueFilter;

/** Observer -> Visitor bridge: alert on one shelf triggers a visitor on that subtree only. */
public final class AlertToInspectionService implements Observer<Event> {

    private final Shelf root;
    private final Subject<Event> publisher;
    private final LocalDate today;

    public AlertToInspectionService(Shelf root, Subject<Event> publisher, LocalDate today) {
        this.root = root;
        this.publisher = publisher;
        this.today = today;
    }

    @Override public void onEvent(Event event) {
        if (!(event instanceof BookAvailabilityEvent e) || e.available()) return;

        List<String> overdue = root.accept(new OverdueFilter(today))
                .stream().map(l -> l.getIsbn()).toList();

        publisher.publish(new InspectionReportEvent(root.getId(), overdue, overdue.size()));
    }
}