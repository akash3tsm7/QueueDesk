package com.akash.queuedesk;
import java.util.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.io.*;
abstract class Ticket implements Comparable<Ticket>{
    int id;
    String title;
    String description;
    String requester;
    String assignee=null;
    Set<String> tags=new HashSet<>();
    enum Priority{ LOW, MEDIUM, HIGH,CRITICAL};
    enum Status{OPEN, IN_PROGRESS, RESOLVED, CLOSED};
    Priority currPriority;
    Status currStatus=Status.OPEN;
    LocalDateTime createdAt;
    private static int nextId=1;
    Ticket(String title,String description,String requester,Priority p){
        this.id=nextId++;
        this.title=title;
        this.description=description;
        this.requester=requester;
        this.currPriority=p;
        this.createdAt=LocalDateTime.now();
    }
    Ticket(String title,String requester){
        this(title,"",requester,Priority.MEDIUM);
    }
    long ageInHours(){
        return ChronoUnit.HOURS.between(createdAt,LocalDateTime.now());
    }
    static int totalCreated(){
        return nextId-1;
    }

    abstract double estimateEffortHours();

    String getTitle() {
        return this.title;
    }
    String getDescription() {
        return this.description;
    }
    Priority getPriority() {
        return this.currPriority;
    }
    LocalDateTime getCreatedAt() {
        return this.createdAt;
    }
    int getId() {
        return this.id;
    }
    String getRequester(){
        return this.requester;
    }
    Status getCurrStatus() {
        return this.currStatus;
    }
    void setDescription(String description){
        this.description=description;
    }
    void setPriority(Priority p){
        this.currPriority=p;
    }
    void setStatus(Status s){
        this.currStatus=s;
    }
    void setAssignee(String assignee){
        this.assignee=assignee;
    }
    void setTags(Set<String> tags){
        this.tags=tags;
    }
    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", requester='" + requester + '\'' +
                ", priority=" + currPriority +
                ", status=" + currStatus +
                ", createdAt=" + createdAt +
                '}';
    }
    @Override
    public int compareTo(Ticket other){
        if(this.currPriority==other.currPriority){
            return this.createdAt.compareTo(other.createdAt);
        }
        return other.currPriority.compareTo(this.currPriority);
    }
}
