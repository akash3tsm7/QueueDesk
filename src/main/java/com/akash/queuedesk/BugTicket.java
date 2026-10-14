package com.akash.queuedesk;

class BugTicket extends Ticket implements Escalatable{
    enum Severity{ LOW, MEDIUM, HIGH};
    private Severity severity;
    private String stepsToReproduce;
    private boolean escalated=false;
    private int escalateLevel=0;

    BugTicket(String title,String description,String requester,Priority priority,Severity severity,String stepsToReproduce){
        super(title, description, requester, priority);
        this.severity = severity;
        this.stepsToReproduce = stepsToReproduce;
    }

    Severity getSeverity(){
        return this.severity;
    }

    String getStepsToReproduce(){
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
