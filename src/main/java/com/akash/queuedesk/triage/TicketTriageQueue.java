package com.akash.queuedesk.triage;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Comparator;

import com.akash.queuedesk.model.Ticket;

public class TicketTriageQueue {

    private final Map<String,PriorityQueue<Ticket>> queues = new HashMap<>();

    public void registerQueue(String queueName , Comparator<Ticket> comparator){

        PriorityQueue<Ticket> queue = (queueName == null) ? new PriorityQueue<>() : new PriorityQueue<>(comparator);

        queues.put(queueName, queue);
    }

    public void addToQueue(String queueName , Ticket ticket){
        queues.get(queueName).offer(ticket);
    }

    public Ticket serveNext(String queueName) {
        return queues.get(queueName).poll();
    }

}