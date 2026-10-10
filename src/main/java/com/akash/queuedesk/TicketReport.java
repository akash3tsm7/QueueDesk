package com.akash.queuedesk;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TicketReport {

    private static final long CRITICAL_OVERDUE_HOURS = 24;

    void reportGenerator(List<Ticket> tickets) {


        Map<Status, Integer> statusCount = new HashMap<>();

        Map<Priority, Integer> priorityCount = new HashMap<>();

        for (Ticket ticket : tickets) {
            Status status = ticket.getCurrStatus();
            Priority priority = ticket.getPriority();

            statusCount.put(status,statusCount.getOrDefault(status,0) + 1);
            priorityCount.put(priority,priorityCount.getOrDefault(priority,0) + 1);
        }

        // 3. Find the oldest OPEN ticket
        Ticket oldestOpenTicket = null;

        for (Ticket ticket : tickets) {
            if (ticket.getCurrStatus() == Status.OPEN) {

                if (oldestOpenTicket == null || ticket.getCreatedAt().isBefore(oldestOpenTicket.getCreatedAt())) {
                    oldestOpenTicket = ticket;
                }
            }
        }

        // 4. Build the report
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

        // 5. Add the oldest OPEN ticket
        report.append("\nOldest OPEN Ticket:\n");

        if (oldestOpenTicket != null) {
            report.append(oldestOpenTicket).append("\n");
        }
        else {
            report.append("No open tickets.\n");
        }

        // 6. Add every overdue CRITICAL ticket
        report.append("\nOverdue CRITICAL Tickets:\n");

        boolean foundOverdue = false;

        for (Ticket ticket : tickets) {

            if (isOverdueCritical(ticket)) {

                report.append("ID: ").append(ticket.getId()).append(" | Title: ").append(ticket.getTitle()).append(" | Status: ").append(ticket.getCurrStatus()).append(" | Age: ").append(ticket.ageInHours()).append(" hours\n");

                foundOverdue = true;
            }
        }

        if (!foundOverdue) {
            report.append("No overdue critical tickets.\n");
        }

        // 7. Write the report to a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("report.txt"))) {

            writer.write(report.toString());

            System.out.println("Report generated successfully: report.txt");

        } catch (IOException e) {
            System.err.println("Error writing report: " + e.getMessage());
        }
    }

    // A critical ticket is overdue if it has been active
    // for more than 24 hours.
    private boolean isOverdueCritical(Ticket ticket) {

        Status status = ticket.getCurrStatus();

        return ticket.getPriority() == Priority.CRITICAL
                && status != Status.RESOLVED
                && status != Status.CLOSED
                && Duration.between(ticket.getCreatedAt(), LocalDateTime.now()).compareTo(Duration.ofHours(CRITICAL_OVERDUE_HOURS)) > 0;
    }
}