package com.akash.queuedesk.model;

public enum Severity{

    LOW(1),
    MEDIUM(2),
    HIGH(3);

    private int rank;

    Severity(int rank){
        this.rank = rank;
    }

    public int getRank(){
        return this.rank;
    }
}
