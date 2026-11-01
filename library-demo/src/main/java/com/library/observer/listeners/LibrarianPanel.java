package com.library.observer.listeners;

import com.library.observer.Event;
import com.library.observer.Observer;
import com.library.observer.events.InspectionReportEvent;

public class LibrarianPanel implements Observer<Event>{
    
    @Override public void onEvent(Event event) {
        if (event instanceof InspectionReportEvent r) {
            System.out.println("[LibrarianPanel] Inspection on shelf " + r.shelfId()
                    + ": " + r.count() + " overdue (" + r.overdueIsbns() + ")");
        }
    }
}
