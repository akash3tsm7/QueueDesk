package com.akash.queuedesk.model;

public enum Status{

    OPEN(1),
    IN_PROGRESS(2),
    RESOLVED(3),
    CLOSED(4);

    private int rank;

    Status(int rank){
        this.rank = rank;
    }

    public int getRank(){
        return this.rank;
    }

}