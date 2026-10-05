package com.akash.queuedesk;
import java.util.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.io.*;
interface Escalatable{
    void escalate(); 
    boolean isEscalated(); 
    int escalationLevel();
    default String escalationBadge(){
        return isEscalated()?"ESCALATED":"NORMAL";
    }
}