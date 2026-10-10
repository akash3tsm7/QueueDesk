package com.akash.queuedesk;

import java.util.PriorityQueue;

public class TicketTriageQueue {

    private final PriorityQueue<Ticket> naturalQueue;
    private final PriorityQueue<Ticket> customQueue;

    public TicketTriageQueue() {
        naturalQueue = new PriorityQueue<>();

        customQueue = new PriorityQueue<>(
                TicketComparators.BY_STATUS_THEN_AGE
        );
    }

    public void addToNaturalQueue(Ticket ticket) {
        naturalQueue.offer(ticket);
    }

    public void addToCustomQueue(Ticket ticket) {
        customQueue.offer(ticket);
    }

    public Ticket serveNextNatural() {
        return naturalQueue.poll();
    }

    public Ticket serveNextCustom() {
        return customQueue.poll();
    }
}