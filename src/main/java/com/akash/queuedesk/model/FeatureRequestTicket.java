package com.akash.queuedesk.model;

public class FeatureRequestTicket extends Ticket{

    private int votes;
    private String businessJustification;

    public FeatureRequestTicket(String title,String description,String requester,Priority priority,int votes,String businessJustification){
        super(title, description, requester, priority);
        this.votes = votes;
        this.businessJustification = businessJustification;
    }

    public int getVotes(){
        return this.votes;
    }

    public String getBusinessJustification(){
        return this.businessJustification;
    }

    @Override
    public double estimateEffortHours(){
        if(this.votes<10) return 1.0;
        else if(this.votes>=10 && this.votes<=50) return 3.0;
        else return 5.0;
    }
}
