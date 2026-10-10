package com.akash.queuedesk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Repository<T extends Ticket>{

    private List<T> tickets=new ArrayList<>();

    void add(T ticket){
        tickets.add(ticket);
    }

    List<T> all(){
        return Collections.unmodifiableList(tickets);
    }

    T findById(int id){
        for(T ticket:tickets){
            if(ticket.getId()==id){
                return ticket;
            }
        }
        throw new TicketNotFoundException(id);
    }

    Map<String, List<T>> groupByAssignee(){
        Map<String,List<T>> m=new HashMap();
        for(T ticket:tickets){
            String assignee=ticket.getAssignee();
            if(m.containsKey(assignee)){
                m.get(assignee).add(ticket);
            }
            else{
                List<T> l=new ArrayList<>();
                l.add(ticket);
                m.put(assignee,l);  
            }
        }
        return m;
    }

}