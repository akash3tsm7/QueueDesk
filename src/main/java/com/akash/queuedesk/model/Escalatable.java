package com.akash.queuedesk.model;

public interface Escalatable{

    void escalate();

    boolean isEscalated();

    int escalationLevel();

    default String escalationBadge(){
        return isEscalated()?"ESCALATED":"NORMAL";
    }

}