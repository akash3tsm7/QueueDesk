package com.akash.queuedesk.model;

public class AccessRequestTicket extends Ticket implements Escalatable{

    private String systemName;
    private String accessLevel;
    private EscalatableSupport escalationSupport = new EscalatableSupport();

    public AccessRequestTicket(String title,String description,String requester, Priority priority,String systemName, String accessLevel){
        super(title, description, requester, priority);
        this.systemName = systemName;
        this.accessLevel = accessLevel;
    }

    public String getSystemName(){
        return this.systemName;
    }

    public String getAccessLevel(){
        return this.accessLevel;
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
