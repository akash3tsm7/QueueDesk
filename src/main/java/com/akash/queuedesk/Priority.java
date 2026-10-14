package com.akash.queuedesk;

public enum Priority{
    LOW(1),
    MEDIUM(2),
    HIGH(3),
    CRITICAL(4);
    private int rank;

    Priority(int rank){
        this.rank = rank;
    }

    public int getRank(){
        return this.rank;
    }
}



