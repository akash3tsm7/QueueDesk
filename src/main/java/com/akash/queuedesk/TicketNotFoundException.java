package com.akash.queuedesk;
import java.util.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.io.*;
class TicketNotFoundException extends RuntimeException {
    TicketNotFoundException(int id){
        super("Ticket with ID " + id + " not found");
    }
}