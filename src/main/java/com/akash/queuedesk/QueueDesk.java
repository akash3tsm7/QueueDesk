package com.akash.queuedesk;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class QueueDesk {

    static <T extends Ticket> T oldest(List<T> tickets){
        T oldestTicket=null;
        for(T ticket:tickets){
            if(oldestTicket==null||ticket.getCreatedAt().isBefore(oldestTicket.getCreatedAt())){
                oldestTicket=ticket;
            }
        }
        return oldestTicket;
    }

    public static void main(String[] args)
            throws IOException, InvalidTicketDataException {

        // 1. Load tickets from the file
        TicketFileLoader loader = new TicketFileLoader();

        List<Ticket> tickets = loader.load("src/main/resources/tickets.txt");

        System.out.println("Tickets loaded: " + tickets.size());

        // 2. Display the triage board
        System.out.println("\n========== TRIAGE BOARD ==========");
        TriageBoard.printTriageBoard(tickets);

        // 3. Demonstrate polymorphism and subclass-specific fields
        System.out.println("\n========== TICKET DETAILS ==========");

        for (Ticket ticket : tickets) {

            if (ticket instanceof BugTicket bug) {
                System.out.println("Bug: " + bug.getTitle());
                System.out.println("Severity: " + bug.getSeverity());
                System.out.println("Steps to reproduce: "
                        + bug.getStepsToReproduce());
            }
            else if (ticket instanceof FeatureRequestTicket feature) {
                System.out.println("Feature: " + feature.getTitle());
                System.out.println("Votes: " + feature.getVotes());
                System.out.println("Business justification: "
                        + feature.getBusinessJustification());
            }
            else if (ticket instanceof AccessRequestTicket access) {
                System.out.println("Access: " + access.getTitle());
                System.out.println("System: " + access.getSystemName());
                System.out.println("Access level: " + access.getAccessLevel());
            }

            System.out.println();
        }

        // 4. Display the original priority and creation-time order
        System.out.println("\n========== ORIGINAL ORDER ==========");

        for (Ticket ticket : tickets) {
            System.out.println(
                    ticket.getPriority() + " - " + ticket.getCreatedAt());
        }

        // 5. Sort using Ticket's natural ordering
        // Priority descending, then creation time ascending
        Collections.sort(tickets);

        System.out.println("\n========== NATURAL ORDER ==========");

        for (Ticket ticket : tickets) {
            System.out.println(
                    ticket.getPriority() + " - " + ticket.getCreatedAt());
        }

        // 6. Sort using the custom assignee-then-priority comparator
        tickets.sort(TicketComparators.BY_ASSIGNEE_THEN_PRIORITY);

        System.out.println("\n========== ASSIGNEE THEN PRIORITY ==========");

        for (Ticket ticket : tickets) {
            System.out.println(
                    ticket.getAssignee() + " - " + ticket.getPriority());
        }

        // 7. Sort using the custom assignee-then-priority comparator
        tickets.sort(TicketComparators.BY_STATUS_THEN_AGE);

        System.out.println("\n========== STATUS THEN AGE ==========");

        for (Ticket ticket : tickets) {
            System.out.println(
                    ticket.getStatus() + " - " + ticket.ageInHours());
        }

        // 8. Repository: store mixed ticket types
        Repository<Ticket> repository = new Repository<>();

        for (Ticket ticket : tickets) {
            repository.add(ticket);
        }

        System.out.println("\n========== ALL REPOSITORY TICKETS ==========");
        System.out.println(repository.all());

        // 9. Group tickets by assignee
        System.out.println("\n========== GROUPED BY ASSIGNEE ==========");

        Map<String, List<Ticket>> groupedTickets =
                repository.groupByAssignee();

        for (Map.Entry<String, List<Ticket>> entry
                : groupedTickets.entrySet()) {

            System.out.println(entry.getKey() + " - " + entry.getValue());
        }


        // 10. Find a ticket by ID
        System.out.println("\n========== FIND TICKET ==========");

        if (!tickets.isEmpty()) {
            int existingId = tickets.get(0).getId();

            System.out.println(
                    "Found ticket: " + repository.findById(existingId));
        }

        // 11. Demonstrate natural priority queue
        System.out.println("\n========== NATURAL PRIORITY QUEUE ==========");

        TicketTriageQueue triageQueue = new TicketTriageQueue();

        for (Ticket ticket : tickets) {
            triageQueue.addToNaturalQueue(ticket);
        }

        Ticket currentTicket;

        while ((currentTicket = triageQueue.serveNextNatural()) != null) {
            System.out.println(
                    currentTicket.getPriority() + " - " + currentTicket.getCreatedAt());
        }

        // 12. Demonstrate custom priority queue
        System.out.println("\n========== CUSTOM PRIORITY QUEUE ==========");

        for (Ticket ticket : tickets) {
            triageQueue.addToCustomQueue(ticket);
        }

        while ((currentTicket = triageQueue.serveNextCustom()) != null) {
            System.out.println(
                    currentTicket.getAssignee() + " - " + currentTicket.getPriority());
        }

        // 13. Find the oldest ticket
        System.out.println("\n========== OLDEST TICKET ==========");

        Ticket oldestTicket = oldest(tickets);

        if (oldestTicket != null) {
            System.out.println(oldestTicket);
        } else {
            System.out.println("No tickets available.");
        }

        // 14. Demonstrate escalation for supported ticket types
        System.out.println("\n========== ESCALATION ==========");

        for (Ticket ticket : tickets) {
            if (ticket instanceof Escalatable escalatable) {
                System.out.println(ticket.getTitle() + " - " + escalatable.escalationBadge() + " - Level: " + escalatable.escalationLevel());
            }
        }

        // 15. Demonstrate description validation
        System.out.println("\n========== DESCRIPTION VALIDATION ==========");

        if (!tickets.isEmpty()) {
            Ticket ticket = tickets.get(0);

            ticket.setDescription("  Updated test description  ");
            System.out.println(
                    "Trimmed description: " + ticket.getDescription());

            try {
                ticket.setDescription("   ");
            } catch (IllegalArgumentException exception) {
                System.out.println(
                        "Blank description rejected: " + exception.getMessage());
            }

        }

        // 16. Display total tickets created
        System.out.println("\n========== TICKET STATISTICS ==========");
        System.out.println("Tickets loaded: " + tickets.size());
        System.out.println("Total tickets created: " + Ticket.totalCreated());

        //17. Report Generation
        TicketReport report = new TicketReport();
        report.generateReport(tickets);

    }
}
