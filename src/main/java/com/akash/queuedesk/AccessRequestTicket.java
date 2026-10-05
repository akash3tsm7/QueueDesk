package com.akash.queuedesk;
import java.util.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.io.*;class AccessRequestTicket extends Ticket implements Escalatable{
    String systemName;
    String accessLevel;
    boolean escalated=false;
    int escalateLevel=0;
    AccessRequestTicket(String title,String description,String requester, Priority p,String systemName, String accessLevel){
        super(title, description, requester, p);
        this.systemName = systemName;
        this.accessLevel = accessLevel;
    }
    @Override
    public double estimateEffortHours(){
        if(this.accessLevel.equals("Read")) return 1.0;
        else if(this.accessLevel.equals("Write")) return 3.0;
        else if(this.accessLevel.equals("Admin")) return 5.0;
        else return 0.0;
    }
    @Override
    public void escalate(){
        escalated=true;
        escalateLevel++;
    } 
    @Override
    public boolean isEscalated(){
        return escalated;
    }
    @Override
    public int escalationLevel(){
        return escalateLevel;
    }
}
