package com.akash.queuedesk;
import java.util.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.io.*;
class QueueDesk {
    static void printTriageBoard(List<Ticket> tickets){
        for (Ticket ticket:tickets){
            System.out.println("Estimated Effort: " + ticket.estimateEffortHours() + " hours");
            System.out.println(ticket.toString());
        }
    }
    static PriorityQueue<Ticket> naturalQueue=new PriorityQueue<>();
    static PriorityQueue<Ticket> customQueue=new PriorityQueue<>(TicketComparators.BY_ASSIGNEE_THEN_PRIORITY);
    void addToNaturalQueue(Ticket ticket) {
        naturalQueue.offer(ticket);
    }

    void addToCustomQueue(Ticket ticket) {
        customQueue.offer(ticket);
    }

    Ticket serveNatural() {
        return naturalQueue.poll();
    }

    Ticket serveCustom() {
        return customQueue.poll();
    }
    static <T extends Ticket> T oldest(List<T> tickets){
        T oldestTicket=null;
        for(T ticket:tickets){
            if(oldestTicket==null||ticket.getCreatedAt().isBefore(oldestTicket.getCreatedAt())){
                oldestTicket=ticket;
            }
        }
        return oldestTicket;
    }
    public static void main(String args[]) throws IOException, InvalidTicketDataException{
        TicketFileLoader loader=new TicketFileLoader();
        List<Ticket> tickets=loader.load("src/main/resources/tickets.txt");
        System.out.println("Tickets loaded: " + tickets.size());
        printTriageBoard(tickets);
        for(Ticket ticket:tickets){
            if (ticket instanceof BugTicket b) {
                System.out.println("Bug: " + b.getTitle());
                System.out.println(b.severity);
                System.out.println(b.stepsToReproduce);
            }
            else if(ticket instanceof FeatureRequestTicket f){
                System.out.println("Feature: " + f.getTitle());
                System.out.println(f.votes);
                System.out.println(f.businessJustification);
            }
            else if(ticket instanceof AccessRequestTicket a){
                System.out.println("Access: " + a.getTitle());
                System.out.println(a.systemName);
                System.out.println(a.accessLevel);
            }
        }
        for(Ticket t:tickets){
            System.out.print(t.currPriority+" ");
            System.out.print(t.getCreatedAt()+" ");
        }
        System.out.println();
        Collections.sort(tickets);
        for(Ticket t:tickets){
            System.out.print(t.currPriority+" ");
            System.out.print(t.getCreatedAt()+" ");
        }
        for(Ticket t:tickets){
            System.out.print(t.assignee+" ");
            System.out.print(t.getPriority()+" ");
        }
        System.out.println();
        Collections.sort(tickets,TicketComparators.BY_ASSIGNEE_THEN_PRIORITY);
        for(Ticket t:tickets){
            System.out.print(t.assignee+" ");
            System.out.print(t.getPriority()+" ");
        }
        Repository<Ticket> r=new Repository();
        for(Ticket t:tickets){
            r.add(t);
        }
        System.out.println(r.all().toString());
        Map<String,List<Ticket>> m=r.groupByAssignee();
        for(Map.Entry<String,List<Ticket>> entry:m.entrySet()){
            System.out.print(entry.getKey()+"-");
            System.out.print(entry.getValue().toString());
            System.out.println();
        }
        for(Ticket t:tickets){
            naturalQueue.offer(t);
        }
        while(!naturalQueue.isEmpty()){
            Ticket temp=naturalQueue.poll();
            System.out.println(temp.getPriority()+"-"+temp.getCreatedAt());
            System.out.println();

        }
        System.out.println("Custom-Queue");
        for(Ticket t:tickets){
            customQueue.offer(t);
        }
        while(!customQueue.isEmpty()){
            Ticket temp=customQueue.poll();
            System.out.println(temp.assignee+"-"+temp.getPriority());
            System.out.println();
        }
        System.out.println(oldest(tickets));
        
    }
}
