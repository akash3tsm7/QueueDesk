package com.akash.queuedesk.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akash.queuedesk.model.Ticket;
import com.akash.queuedesk.model.TicketStatus;
import com.akash.queuedesk.model.TicketPriority;

public class TicketReport {

    private static final long CRITICAL_OVERDUE_HOURS = 24;

    public static void generateReport(List<Ticket> tickets) {

        String reportContent = buildReportContent(tickets);
        writeReportToFile(reportContent);

    }

    private static String buildReportContent(List<Ticket> tickets) {

        StringBuilder report = new StringBuilder();
        report.append("========== END-OF-DAY SUMMARY ==========\n\n");

        appendStatusSummary(report, countByStatus(tickets));
        appendPrioritySummary(report, countByPriority(tickets));
        appendOldestOpenTicket(report, findOldestOpenTicket(tickets));
        appendOverdueCriticalTickets(report, tickets);

        return report.toString();
    }

    private static Map<TicketStatus, Integer> countByStatus(List<Ticket> tickets) {
        Map<TicketStatus, Integer> statusCount = new HashMap<>();
        for (Ticket ticket : tickets) {
            TicketStatus status = ticket.getStatus();
            statusCount.put(status, statusCount.getOrDefault(status, 0) + 1);
        }
        return statusCount;
    }

    private static Map<TicketPriority, Integer> countByPriority(List<Ticket> tickets) {
        Map<TicketPriority, Integer> priorityCount = new HashMap<>();
        for (Ticket ticket : tickets) {
            TicketPriority priority = ticket.getPriority();
            priorityCount.put(priority, priorityCount.getOrDefault(priority, 0) + 1);
        }
        return priorityCount;
    }

    private static void appendStatusSummary(StringBuilder report, Map<TicketStatus, Integer> statusCount) {
        report.append("Status Summary:\n");
        for (Map.Entry<TicketStatus, Integer> entry : statusCount.entrySet()) {
            report.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
    }

    private static void appendPrioritySummary(StringBuilder report, Map<TicketPriority, Integer> priorityCount) {
        report.append("\nPriority Summary:\n");
        for (Map.Entry<TicketPriority, Integer> entry : priorityCount.entrySet()) {
            report.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
    }

    private static Ticket findOldestOpenTicket(List<Ticket> tickets) {
        Ticket oldest = null;
        for (Ticket ticket : tickets) {
            if (ticket.getStatus() == TicketStatus.OPEN) {
                if (oldest == null || ticket.getCreatedAt().isBefore(oldest.getCreatedAt())) {
                    oldest = ticket;
                }
            }
        }
        return oldest;
    }

    private static void appendOldestOpenTicket(StringBuilder report, Ticket oldest) {
        report.append("\nOldest OPEN Ticket:\n");
        report.append(oldest != null ? oldest.toString() : "No open tickets.").append("\n");
    }

    private static void appendOverdueCriticalTickets(StringBuilder report, List<Ticket> tickets) {

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

    private static void appendOverdueTicketLine(StringBuilder report, Ticket ticket) {
        report.append("ID: ").append(ticket.getId()).append(" | Title: ").append(ticket.getTitle()).append(" | Status: ").append(ticket.getStatus()).append(" | Age: ").append(ticket.ageInHours()).append(" hours\n");
    }

    private static void writeReportToFile(String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("report.txt"))) {
            writer.write(content);
            System.out.println("Report generated successfully: report.txt");
        } catch (IOException e) {
            System.err.println("Error writing report: " + e.getMessage());
        }
    }

    private static boolean isOverdueCritical(Ticket ticket) {

        TicketStatus status = ticket.getStatus();

        return ticket.getPriority() == TicketPriority.CRITICAL && status != TicketStatus.RESOLVED && status != TicketStatus.CLOSED && ticket.ageInHours() >= CRITICAL_OVERDUE_HOURS;
    }

}