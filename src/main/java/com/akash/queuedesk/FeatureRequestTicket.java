package com.akash.queuedesk;

class FeatureRequestTicket extends Ticket{

    private int votes;
    private String businessJustification;

    int getVotes(){
        return this.votes;
    }

    String getBusinessJustification(){
        return this.businessJustification;
    }

    FeatureRequestTicket(String title,String description,String requester,Priority priority,int votes,String businessJustification){
        super(title, description, requester, priority);
        this.votes = votes;
        this.businessJustification = businessJustification;
    }

    @Override
    public double estimateEffortHours(){
        if(this.votes<10) return 1.0;
        else if(this.votes>=10 && this.votes<=50) return 3.0;
        else return 5.0;
    }
}
