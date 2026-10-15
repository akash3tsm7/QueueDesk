package com.akash.queuedesk.exception;

public class TicketNotFoundException extends RuntimeException {

    public TicketNotFoundException(int id){
        super("Ticket with ID " + id + " not found");
    }

}