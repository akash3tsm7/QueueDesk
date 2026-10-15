package com.akash.queuedesk;

import java.io.IOException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.akash.queuedesk.model.Ticket;
import com.akash.queuedesk.model.BugTicket;
import com.akash.queuedesk.model.FeatureRequestTicket;
import com.akash.queuedesk.model.AccessRequestTicket;
import com.akash.queuedesk.model.Escalatable;

import com.akash.queuedesk.repository.Repository;

import com.akash.queuedesk.exception.InvalidTicketDataException;

import com.akash.queuedesk.io.TicketFileLoader;
import com.akash.queuedesk.io.TicketReport;

import com.akash.queuedesk.triage.TriageBoard;
import com.akash.queuedesk.triage.TicketTriageQueue;

import com.akash.queuedesk.comparator.TicketComparators;


class QueueDesk {

    public static void main(String[] args) {

        try {
            List<Ticket> tickets = loadTickets("src/main/resources/tickets_complex.txt");
            demonstrateTriageBoard(tickets);
            demonstrateTicketDetails(tickets);
            demonstrateOrdering(tickets);
            Repository<Ticket> repository = buildRepository(tickets);
            demonstrateRepository(repository, tickets);
            demonstrateTriageQueues(tickets);
            demonstrateOldestTicket(tickets);
            demonstrateEscalation(tickets);
            demonstrateDescriptionValidation(tickets);
            printTicketStatistics(tickets);
            generateEndOfDayReport(tickets);
        }
        catch (IOException | InvalidTicketDataException e) {
            System.err.println("Fatal error: " + e.getMessage());
        }
    }

    private static List<Ticket> loadTickets(String path) throws IOException, InvalidTicketDataException {

        List<Ticket> tickets = new TicketFileLoader().load(path);

        System.out.println("Tickets loaded: " + tickets.size());

        return tickets;

    }

    private static void demonstrateTriageBoard(List<Ticket> tickets) {

        System.out.println("\n========== TRIAGE BOARD ==========");

        TriageBoard.printTriageBoard(tickets);

    }

    private static void demonstrateTicketDetails(List<Ticket> tickets) {

        System.out.println("\n========== TICKET DETAILS ==========");

        for (Ticket ticket : tickets) {
            printTicketTypeDetails(ticket);
            System.out.println();
        }

    }

    private static void printTicketTypeDetails(Ticket ticket) {

        if (ticket instanceof BugTicket bug) {
            printBugDetails(bug);
        }

        else if (ticket instanceof FeatureRequestTicket feature) {
            printFeatureDetails(feature);
        }

        else if (ticket instanceof AccessRequestTicket access) {
            printAccessDetails(access);
        }

    }

    private static void printBugDetails(BugTicket bug) {

        System.out.println("Bug: "               + bug.getTitle());
        System.out.println("Severity: "          + bug.getSeverity());
        System.out.println("Steps to reproduce: "+ bug.getStepsToReproduce());

    }

    private static void printFeatureDetails(FeatureRequestTicket feature) {

        System.out.println("Feature: "               + feature.getTitle());
        System.out.println("Votes: "                 + feature.getVotes());
        System.out.println("Business justification: "+ feature.getBusinessJustification());

    }

    private static void printAccessDetails(AccessRequestTicket access) {

        System.out.println("Access: "      + access.getTitle());
        System.out.println("System: "      + access.getSystemName());
        System.out.println("Access level: "+ access.getAccessLevel());

    }

    private static void demonstrateOrdering(List<Ticket> tickets) {

        printOriginalOrder(tickets);

        Collections.sort(tickets);

        printNaturalOrder(tickets);

        tickets.sort(TicketComparators.BY_ASSIGNEE_THEN_PRIORITY);

        printAssigneeThenPriorityOrder(tickets);

        tickets.sort(TicketComparators.BY_STATUS_THEN_AGE);

        printStatusThenAgeOrder(tickets);

    }

    private static void printOriginalOrder(List<Ticket> tickets) {

        System.out.println("\n========== ORIGINAL ORDER ==========");

        for (Ticket ticket : tickets) {
            System.out.println(ticket.getPriority() + " - " + ticket.getCreatedAt());
        }

    }

    private static void printNaturalOrder(List<Ticket> tickets) {

        System.out.println("\n========== NATURAL ORDER ==========");

        for (Ticket ticket : tickets) {
            System.out.println(ticket.getPriority() + " - " + ticket.getCreatedAt());
        }

    }

    private static void printAssigneeThenPriorityOrder(List<Ticket> tickets) {

        System.out.println("\n========== ASSIGNEE THEN PRIORITY ==========");

        for (Ticket ticket : tickets) {
            System.out.println(ticket.getAssignee() + " - " + ticket.getPriority());
        }

    }

    private static void printStatusThenAgeOrder(List<Ticket> tickets) {

        System.out.println("\n========== STATUS THEN AGE ==========");

        for (Ticket ticket : tickets) {
            System.out.println(ticket.getStatus() + " - " + ticket.ageInHours());
        }
    }

    private static Repository<Ticket> buildRepository(List<Ticket> tickets) {

        Repository<Ticket> repository = new Repository<>();

        for (Ticket ticket : tickets){
            repository.add(ticket);
        }

        return repository;
    }

    private static void demonstrateRepository(Repository<Ticket> repository, List<Ticket> tickets) {

        printAllRepositoryTickets(repository);
        printGroupedByAssignee(repository);
        demonstrateFindById(repository, tickets);

    }

    private static void printAllRepositoryTickets(Repository<Ticket> repository) {

        System.out.println("\n========== ALL REPOSITORY TICKETS ==========");
        System.out.println(repository.all());

    }

    private static void printGroupedByAssignee(Repository<Ticket> repository) {

        System.out.println("\n========== GROUPED BY ASSIGNEE ==========");

        for (Map.Entry<String, List<Ticket>> entry : repository.groupByAssignee().entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }

    }

    private static void demonstrateFindById(Repository<Ticket> repository, List<Ticket> tickets) {

        System.out.println("\n========== FIND TICKET ==========");

        if (!tickets.isEmpty()) {
            int existingId = tickets.get(0).getId();
            System.out.println("Found ticket: " + repository.findById(existingId));
        }
    }

    private static void demonstrateTriageQueues(List<Ticket> tickets) {

        TicketTriageQueue triageQueue = new TicketTriageQueue();

        demonstrateNaturalQueue(triageQueue, tickets);
        demonstrateCustomQueue(triageQueue, tickets);

    }

    private static void demonstrateNaturalQueue(TicketTriageQueue triageQueue, List<Ticket> tickets) {

        triageQueue.registerQueue("natural",  null);

        System.out.println("\n========== NATURAL PRIORITY QUEUE ==========");

        for (Ticket ticket : tickets){
            triageQueue.addToQueue("natural" , ticket);
        }

        Ticket currentTicket;

        while ((currentTicket = triageQueue.serveNext("natural")) != null){
            System.out.println(currentTicket.getPriority() + " - " + currentTicket.getCreatedAt());
        }

    }

    private static void demonstrateCustomQueue(TicketTriageQueue triageQueue, List<Ticket> tickets) {

        triageQueue.registerQueue("byStatus" , TicketComparators.BY_STATUS_THEN_AGE);

        System.out.println("\n========== CUSTOM PRIORITY QUEUE ==========");

        for (Ticket ticket : tickets){
            triageQueue.addToQueue("byStatus" , ticket);
        }

        Ticket currentTicket;

        while ((currentTicket = triageQueue.serveNext("byStatus")) != null){
            System.out.println(currentTicket.getStatus() + " - " + currentTicket.ageInHours());
        }

    }

    private static void demonstrateOldestTicket(List<Ticket> tickets) {

        System.out.println("\n========== OLDEST TICKET ==========");

        Ticket oldestTicket = oldest(tickets);

        System.out.println(oldestTicket != null ? oldestTicket : "No tickets available.");
    }

    private static void demonstrateEscalation(List<Ticket> tickets) {

        System.out.println("\n========== ESCALATION ==========");

        for (Ticket ticket : tickets) {
            if (ticket instanceof Escalatable escalatable) {
                System.out.println(ticket.getTitle() + " - "
                        + escalatable.escalationBadge() + " - Level: "
                        + escalatable.escalationLevel());
            }
        }

    }

    private static void demonstrateDescriptionValidation(List<Ticket> tickets) {

        System.out.println("\n========== DESCRIPTION VALIDATION ==========");

        if (tickets.isEmpty()) return;

        Ticket ticket = tickets.get(0);

        applyValidDescription(ticket);
        attemptBlankDescription(ticket);
    }

    private static void applyValidDescription(Ticket ticket) {

        ticket.setDescription("  Updated test description  ");

        System.out.println("Trimmed description: " + ticket.getDescription());
    }

    private static void attemptBlankDescription(Ticket ticket) {

        try {
            ticket.setDescription("   ");
        }
        catch (IllegalArgumentException exception) {
            System.out.println("Blank description rejected: " + exception.getMessage());
        }

    }

    private static void printTicketStatistics(List<Ticket> tickets) {

        System.out.println("\n========== TICKET STATISTICS ==========");
        System.out.println("Tickets loaded: "       + tickets.size());
        System.out.println("Total tickets created: " + Ticket.totalCreated());

    }

    private static void generateEndOfDayReport(List<Ticket> tickets) {

        new TicketReport().generateReport(tickets);

    }

    static <T extends Ticket> T oldest(List<T> tickets) {

        T oldestTicket = null;

        for (T ticket : tickets) {
            if (oldestTicket == null || ticket.getCreatedAt().isBefore(oldestTicket.getCreatedAt())) {
                oldestTicket = ticket;
            }
        }

        return oldestTicket;

    }
}
