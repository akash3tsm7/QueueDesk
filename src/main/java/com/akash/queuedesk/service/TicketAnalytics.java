package com.akash.queuedesk.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.Comparator;

import com.akash.queuedesk.constants.TicketStatus;
import com.akash.queuedesk.entity.Ticket;
import com.akash.queuedesk.constants.TicketPriority;

public class TicketAnalytics {

    public static Map<TicketPriority, Long>  countByPriority(List<Ticket> tickets){

        Map<TicketPriority, Long> priorityCount = tickets.stream()
                .collect(Collectors.groupingBy(Ticket::getPriority, Collectors.counting()));

        return priorityCount;

    }

    public static  Map<String, List<Ticket>> groupByAssignee(List<Ticket> tickets){

        Map<String, List<Ticket>> ticketsByAssignee = tickets.stream()
                .collect(Collectors.groupingBy(ticket -> ticket.getAssignee() == null ? "unassigned" : ticket.getAssignee()));

        return ticketsByAssignee;

    }

    public static double averageEffortHours(List<Ticket> tickets){

        OptionalDouble averageEffort = tickets.stream()
                .mapToDouble(Ticket::estimateEffortHours)
                .average();

        return averageEffort.orElse(0.0);

    }

    public static double totalEffortHours(List<Ticket> tickets){

        double totalEffort = tickets.stream()
                .map(Ticket::estimateEffortHours)
                .reduce(0.0, Double::sum, Double::sum);

        return totalEffort;

    }

    public static Optional<Ticket> oldestOpenTicket(List<Ticket> tickets){

        return  tickets.stream()
                .filter(ticket -> ticket.getStatus()== TicketStatus.OPEN)
                .min(Comparator.comparing(Ticket::getCreatedAt));

    }

    public static String tagLine(List<Ticket> tickets){

        return tickets.stream()
                .flatMap(ticket -> ticket.getTags().stream())
                .distinct()
                .sorted()
                .collect(Collectors.joining(", "));
    }

}
