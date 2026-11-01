package com.library;

import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.library.integration.AlertToInspectionService;
import com.library.model.Book;
import com.library.model.EBook;
import com.library.model.Journal;
import com.library.model.Loan;
import com.library.model.Section;
import com.library.model.Shelf;
import com.library.observer.EventBus;
import com.library.observer.EventType;
import com.library.observer.events.BookAvailabilityEvent;
import com.library.observer.listeners.CatalogRefreshListener;
import com.library.observer.listeners.LibrarianPanel;
import com.library.observer.listeners.OverdueAlertService;
import com.library.observer.listeners.ReservationNotifier;
import com.library.observer.listeners.StatisticsTracker;

public final class Main {

    public static void main(String[] args) throws InterruptedException {
        EventBus bus = new EventBus();

        // 1. Build the stable tree
        Section root = new Section("Main Library");
        Shelf fiction = new Shelf("S1");
        fiction.add(new Book("978-1", "Dune", true, 20.0));
        fiction.add(new Book("978-2", "Foundation", false, 18.0));
        fiction.add(new Loan("978-2", LocalDate.now().minusDays(10), null)); // This is overdue

        Shelf science = new Shelf("S2");
        science.add(new Journal("1111-2222", "Nature", 42));
        science.add(new EBook("978-9", "Clean Code", "PDF"));

        root.add(fiction);
        root.add(science);

        // 2. Wire up the observers
        var stats = new StatisticsTracker();
        var reservations = new ReservationNotifier();
        var librarian = new LibrarianPanel();
        var catalog = new CatalogRefreshListener();
        var alerts = new OverdueAlertService(bus);
        var integration = new AlertToInspectionService(fiction, bus, LocalDate.now());

        bus.subscribe(EventType.BOOK_AVAILABLE, stats);
        bus.subscribe(EventType.BOOK_AVAILABLE, reservations);
        bus.subscribe(EventType.BOOK_BORROWED, stats);
        bus.subscribe(EventType.BOOK_BORROWED, catalog);
        bus.subscribe(EventType.BOOK_RETURNED, catalog);
        bus.subscribe(EventType.BOOK_BORROWED, alerts);
        bus.subscribe(EventType.BOOK_BORROWED, integration);
        bus.subscribe(EventType.INSPECTION_REPORT, librarian);

        System.out.println("Starting Library Simulation");
        System.out.println("Simulating 2 threads, 10 events each...\n");

        // 3. Concurrency: 2 threads, 10 events each (total 20 events)
        int threads = 2;
        int eventsPerThread = 10;
        ExecutorService producer = Executors.newFixedThreadPool(threads);

        for (int t = 0; t < threads; t++) {
            final int threadId = t;
            producer.submit(() -> {
                for (int i = 0; i < eventsPerThread; i++) {
                    // Alternate between borrowing and returning books
                    boolean available = (i % 2 == 0);
                    String action = available ? "RETURNED" : "BORROWED";
                    
                    System.out.println("Thread " + threadId + " -> " + action + " 978-1");
                    bus.publish(new BookAvailabilityEvent("978-1", "S1", available, System.nanoTime()));
                    
                    try { Thread.sleep(50); } catch (InterruptedException e) { } // slight delay for readability
                }
            });
        }

        // 4. Graceful shutdown
        producer.shutdown();
        producer.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("\nSimulation Finished");
        System.out.println("Total Borrowed Events: " + stats.getBorrowed());
        System.out.println("Total Returned Events: " + stats.getReturned());
    }
}