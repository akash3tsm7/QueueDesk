package com.akash.queuedesk;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TicketReport {

    private static final long CRITICAL_OVERDUE_HOURS = 24;

    void generateReport(List<Ticket> tickets) {


        Map<Status, Integer> statusCount = new HashMap<>();

        Map<Priority, Integer> priorityCount = new HashMap<>();

        for (Ticket ticket : tickets) {
            Status status = ticket.getStatus();
            Priority priority = ticket.getPriority();

            statusCount.put(status,statusCount.getOrDefault(status,0) + 1);
            priorityCount.put(priority,priorityCount.getOrDefault(priority,0) + 1);
        }

        Ticket oldestOpenTicket = null;

        for (Ticket ticket : tickets) {
            if (ticket.getStatus() == Status.OPEN) {

                if (oldestOpenTicket == null || ticket.getCreatedAt().isBefore(oldestOpenTicket.getCreatedAt())) {
                    oldestOpenTicket = ticket;
                }
            }
        }

        StringBuilder report = new StringBuilder();

        report.append("========== END-OF-DAY SUMMARY ==========\n\n");

        report.append("Status Summary:\n");

        for (Map.Entry<Status, Integer> entry : statusCount.entrySet()) {

            report.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        report.append("\nPriority Summary:\n");

        for (Map.Entry<Priority, Integer> entry : priorityCount.entrySet()) {
            report.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        report.append("\nOldest OPEN Ticket:\n");

        if (oldestOpenTicket != null) {
            report.append(oldestOpenTicket).append("\n");
        }
        else {
            report.append("No open tickets.\n");
        }

        report.append("\nOverdue CRITICAL Tickets:\n");

        boolean foundOverdue = false;

        for (Ticket ticket : tickets) {

            if (isOverdueCritical(ticket)) {

                report.append("ID: ").append(ticket.getId()).append(" | Title: ").append(ticket.getTitle()).append(" | Status: ").append(ticket.getStatus()).append(" | Age: ").append(ticket.ageInHours()).append(" hours\n");

                foundOverdue = true;
            }
        }

        if (!foundOverdue) {
            report.append("No overdue critical tickets.\n");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("report.txt"))) {

            writer.write(report.toString());

            System.out.println("Report generated successfully: report.txt");

        } catch (IOException e) {
            System.err.println("Error writing report: " + e.getMessage());
        }
    }

    private boolean isOverdueCritical(Ticket ticket) {

        Status status = ticket.getStatus();

        return ticket.getPriority() == Priority.CRITICAL
                && status != Status.RESOLVED
                && status != Status.CLOSED
                && ticket.ageInHours() >= CRITICAL_OVERDUE_HOURS;
    }
}