package com.akash.queuedesk;

class TicketNotFoundException extends RuntimeException {
    TicketNotFoundException(int id){
        super("Ticket with ID " + id + " not found");
    }
}