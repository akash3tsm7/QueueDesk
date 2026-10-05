package com.akash.queuedesk;
import java.util.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.io.*;
class FeatureRequestTicket extends Ticket{
    int votes;
    String businessJustification;
    FeatureRequestTicket(String title,String description,String requester,Priority p,int votes,String businessJustification){
        super(title, description, requester, p);
        this.votes = votes;
        this.businessJustification = businessJustification;
    }
    @Override
    public double estimateEffortHours(){
        if(this.votes<10) return 1.0;
        else if(this.votes>=10&&this.votes<=50) return 3.0;
        else return 5.0;
    }
}
