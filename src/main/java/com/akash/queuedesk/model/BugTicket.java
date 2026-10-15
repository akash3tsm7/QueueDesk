package com.akash.queuedesk.model;

public class BugTicket extends Ticket implements Escalatable{

    private Severity severity;
    private String stepsToReproduce;
    private EscalatableSupport escalationSupport = new EscalatableSupport();

    public BugTicket(String title,String description,String requester,Priority priority,Severity severity,String stepsToReproduce){
        super(title, description, requester, priority);
        this.severity = severity;
        this.stepsToReproduce = stepsToReproduce;
    }

    public Severity getSeverity(){
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

    @Override
    public boolean isEscalated(){
        return escalationSupport.isEscalated();
    }

    @Override
    public int escalationLevel(){
        return escalationSupport.escalationLevel();
    }
}
