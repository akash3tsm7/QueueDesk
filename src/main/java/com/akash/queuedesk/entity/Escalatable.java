package com.akash.queuedesk.entity;

public interface Escalatable{

    void escalate();

    boolean isEscalated();

    int escalationLevel();

    default String escalationBadge(){
        return isEscalated()?"ESCALATED":"NORMAL";
    }

}