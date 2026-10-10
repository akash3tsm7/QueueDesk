package com.akash.queuedesk;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

abstract class Ticket implements Comparable<Ticket>{
    private int id;
    private String title;
    private String description;
    private String requester;
    private String assignee=null;
    private Set<String> tags=new HashSet<>();
    private Priority currPriority;
    private Status currStatus;
    private LocalDateTime createdAt;
    private static int nextId=1;
    Ticket(String title,String description,String requester,Priority priority){
        this.id=nextId++;
        this.title=title;
        this.description=description;
        this.requester=requester;
        this.currPriority=priority;
        this.currStatus=Status.OPEN;
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

    int getId() {
        return this.id;
    }
    String getTitle() {
        return this.title;
    }
    String getDescription() {
        return this.description;
    }
    String getRequester(){
        return this.requester;
    }
    String getAssignee(){
        return this.assignee;
    }
    Priority getPriority() {
        return this.currPriority;
    }
    Status getCurrStatus() {
        return this.currStatus;
    }
    LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    void setDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException(
                    "Description cannot be null or blank"
            );
        }

        this.description = description.trim();
    }
    public void setPriority(Priority priority) {
        if (priority == null) {
            throw new IllegalArgumentException(
                    "Priority cannot be null"
            );
        }

        this.currPriority = priority;
    }
    public void setStatus(Status status) {
        if (status == null) {
            throw new IllegalArgumentException(
                    "Status cannot be null"
            );
        }

        this.currStatus = status;
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
