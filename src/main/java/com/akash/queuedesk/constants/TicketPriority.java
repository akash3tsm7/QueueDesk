package com.akash.queuedesk.constants;

public enum TicketPriority {

    LOW(0),
    MEDIUM(1),
    HIGH(2),
    CRITICAL(4),
    LESS_CRITICAL(3);

    private int rank;

    TicketPriority(int rank){
        this.rank = rank;
    }

    public int getRank(){
        return this.rank;
    }
}



