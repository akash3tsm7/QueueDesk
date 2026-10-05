package com.akash.queuedesk;
import java.util.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.io.*;
class TicketReport{
    void reportGenerator(List<Ticket> tickets){
        Map<Ticket.Status,Integer> statusCount=new HashMap<>();
        for(Ticket ticket:tickets){
            Ticket.Status status=ticket.getCurrStatus();
            statusCount.put(status,statusCount.getOrDefault(status,0)+1);
        }
        Map<Ticket.Priority,Integer> priorityCount=new HashMap<>();
        for(Ticket ticket:tickets){
            Ticket.Priority priority=ticket.getPriority();
            priorityCount.put(priority,priorityCount.getOrDefault(priority,0)+1);   
        }
        Ticket oldestTicket=QueueDesk.oldest(tickets);
        StringBuilder report=new StringBuilder();
        report.append("                End-of-day summary                    \n");
        report.append("Status Summary:\n");
        for(Map.Entry<Ticket.Status,Integer> entry:statusCount.entrySet()){
            report.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        report.append("Priority Summary:\n");
        for(Map.Entry<Ticket.Priority,Integer> entry:priorityCount.entrySet()){
            report.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        report.append("Oldest Ticket:\n");
        report.append(oldestTicket.toString()).append("\n");
        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter("report.txt"))) {

            writer.write(report.toString());
        }
        catch(IOException e){
            System.err.println("Error writing report: " + e.getMessage());
        }
    }
}