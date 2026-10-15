package com.akash.queuedesk.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.akash.queuedesk.model.Ticket;
import com.akash.queuedesk.exception.TicketNotFoundException;

public class Repository<T extends Ticket>{

    private List<T> tickets=new ArrayList<>();

    public void add(T ticket){
        tickets.add(ticket);
    }

    public List<T> all(){
        return Collections.unmodifiableList(tickets);
    }

    public T findById(int id){
        for(T ticket:tickets){
            if(ticket.getId()==id){
                return ticket;
            }
        }
        throw new TicketNotFoundException(id);
    }

    public Map<String, List<T>> groupByAssignee(){
        Map<String,List<T>> ticketsByAssignee=new HashMap();
        for(T ticket:tickets){
            ticketsByAssignee.computeIfAbsent(ticket.getAssignee(),k->new ArrayList<>()).add(ticket);
        }
        return ticketsByAssignee;
    }

}