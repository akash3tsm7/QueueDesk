package com.akash.queuedesk.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akash.queuedesk.model.Ticket;
import com.akash.queuedesk.model.Status;
import com.akash.queuedesk.model.Priority;

public class TicketReport {

    private static final long CRITICAL_OVERDUE_HOURS = 24;

    public void generateReport(List<Ticket> tickets) {

        String reportContent = buildReportContent(tickets);
        writeReportToFile(reportContent);

    }

    private String buildReportContent(List<Ticket> tickets) {

        StringBuilder report = new StringBuilder();
        report.append("========== END-OF-DAY SUMMARY ==========\n\n");

        appendStatusSummary(report, countByStatus(tickets));
        appendPrioritySummary(report, countByPriority(tickets));
        appendOldestOpenTicket(report, findOldestOpenTicket(tickets));
        appendOverdueCriticalTickets(report, tickets);

        return report.toString();
    }

    private Map<Status, Integer> countByStatus(List<Ticket> tickets) {
        Map<Status, Integer> statusCount = new HashMap<>();
        for (Ticket ticket : tickets) {
            Status status = ticket.getStatus();
            statusCount.put(status, statusCount.getOrDefault(status, 0) + 1);
        }
        return statusCount;
    }

    private Map<Priority, Integer> countByPriority(List<Ticket> tickets) {
        Map<Priority, Integer> priorityCount = new HashMap<>();
        for (Ticket ticket : tickets) {
            Priority priority = ticket.getPriority();
            priorityCount.put(priority, priorityCount.getOrDefault(priority, 0) + 1);
        }
        return priorityCount;
    }

    private void appendStatusSummary(StringBuilder report, Map<Status, Integer> statusCount) {
        report.append("Status Summary:\n");
        for (Map.Entry<Status, Integer> entry : statusCount.entrySet()) {
            report.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
    }

    private void appendPrioritySummary(StringBuilder report, Map<Priority, Integer> priorityCount) {
        report.append("\nPriority Summary:\n");
        for (Map.Entry<Priority, Integer> entry : priorityCount.entrySet()) {
            report.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
    }

    private Ticket findOldestOpenTicket(List<Ticket> tickets) {
        Ticket oldest = null;
        for (Ticket ticket : tickets) {
            if (ticket.getStatus() == Status.OPEN) {
                if (oldest == null || ticket.getCreatedAt().isBefore(oldest.getCreatedAt())) {
                    oldest = ticket;
                }
            }
        }
        return oldest;
    }

    private void appendOldestOpenTicket(StringBuilder report, Ticket oldest) {
        report.append("\nOldest OPEN Ticket:\n");
        report.append(oldest != null ? oldest.toString() : "No open tickets.").append("\n");
    }

    private void appendOverdueCriticalTickets(StringBuilder report, List<Ticket> tickets) {

        report.append("\nOverdue CRITICAL Tickets:\n");
        boolean foundOverdue = false;
        for (Ticket ticket : tickets) {
            if (isOverdueCritical(ticket)) {
                appendOverdueTicketLine(report, ticket);
                foundOverdue = true;
            }
        }
        if (!foundOverdue) report.append("No overdue critical tickets.\n");

    }

    private void appendOverdueTicketLine(StringBuilder report, Ticket ticket) {
        report.append("ID: ").append(ticket.getId()).append(" | Title: ").append(ticket.getTitle()).append(" | Status: ").append(ticket.getStatus()).append(" | Age: ").append(ticket.ageInHours()).append(" hours\n");
    }

    private void writeReportToFile(String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("report.txt"))) {
            writer.write(content);
            System.out.println("Report generated successfully: report.txt");
        } catch (IOException e) {
            System.err.println("Error writing report: " + e.getMessage());
        }
    }

    private boolean isOverdueCritical(Ticket ticket) {

        Status status = ticket.getStatus();

        return ticket.getPriority() == Priority.CRITICAL && status != Status.RESOLVED && status != Status.CLOSED && ticket.ageInHours() >= CRITICAL_OVERDUE_HOURS;
    }

}