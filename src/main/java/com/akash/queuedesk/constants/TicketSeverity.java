package com.akash.queuedesk.constants;

public enum TicketSeverity {

    LOW(1),
    MEDIUM(2),
    HIGH(3);

    private int rank;

    TicketSeverity(int rank){
        this.rank = rank;
    }

    public int getRank(){
        return this.rank;
    }
}
