package com.library;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.library.observer.EventBus;
import com.library.observer.EventType;
import com.library.observer.events.BookAvailabilityEvent;
import com.library.observer.listeners.StatisticsTracker;

class ConcurrencyStressTest {

    @Test
    void stressTest4Threads1000EventsExactCount() throws InterruptedException {
        EventBus bus = new EventBus();
        StatisticsTracker stats = new StatisticsTracker();
        bus.subscribe(EventType.BOOK_BORROWED, stats);

        int threads = 4;
        int eventsPerThread = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);

        for (int t = 0; t < threads; t++) {
            executor.submit(() -> {
                try {
                    start.await();
                    for (int i = 0; i < eventsPerThread; i++) {
                        bus.publish(new BookAvailabilityEvent("978-1", "S1", false, System.nanoTime()));
                    }
                } catch (InterruptedException ignored) {
                } finally {
                    done.countDown();
                }
            });
        }

        start.countDown();
        assertTrue(done.await(10, TimeUnit.SECONDS), "producers hung");
        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));

        // Exact count: no lost events, no double counting
        assertEquals(threads * eventsPerThread, stats.getBorrowed());
    }
}