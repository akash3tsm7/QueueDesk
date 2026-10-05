package com.akash.queuedesk;
import java.util.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.io.*;
class BugTicket extends Ticket implements Escalatable{
    enum Severity{ LOW, MEDIUM, HIGH};
    Severity severity;
    String stepsToReproduce;
    boolean escalated=false;
    int escalateLevel=0;
    BugTicket(String title,String description,String requester,Priority p,Severity severity,String stepsToReproduce){
        super(title, description, requester, p);
        this.severity = severity;
        this.stepsToReproduce = stepsToReproduce;
    }
    @Override
    public double estimateEffortHours(){
        switch(this.severity){
            case LOW:
                return 1.0;
            case MEDIUM:
                return 3.0;
            case HIGH:
                return 5.0;
            default:
                return 0.0;
        }
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
