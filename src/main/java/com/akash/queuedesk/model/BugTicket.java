package com.akash.queuedesk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BugTicket extends Ticket implements Escalatable{

    private TicketSeverity severity;
    private String stepsToReproduce;
    private EscalatableSupport escalationSupport = new EscalatableSupport();

    public BugTicket(){}

    public BugTicket(String title, String description, String requester, TicketPriority priority, TicketSeverity severity, String stepsToReproduce){
        super(title, description, requester, priority);
        this.severity = severity;
        this.stepsToReproduce = stepsToReproduce;
    }

    public TicketSeverity getSeverity(){
        return this.severity;
    }

    public String getStepsToReproduce(){
        return this.stepsToReproduce;
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
        escalationSupport.escalate();
    }

    @JsonIgnore
    @Override
    public boolean isEscalated(){
        return escalationSupport.isEscalated();
    }

    @Override
    public int escalationLevel(){
        return escalationSupport.escalationLevel();
    }
}
