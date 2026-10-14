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
    private Priority priority;
    private Status status;
    private LocalDateTime createdAt;
    private static int nextId=1;

    Ticket(String title,String description,String requester,Priority priority){
        this.id=nextId++;
        this.title=title;
        this.description=description;
        this.requester=requester;
        this.priority=priority;
        this.status=Status.OPEN;
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
        return this.priority;
    }
    Status getStatus() {
        return this.status;
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

        this.priority = priority;
    }
    public void setStatus(Status status) {
        if (status == null) {
            throw new IllegalArgumentException(
                    "Status cannot be null"
            );
        }

        this.status = status;
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
                ", priority=" + priority +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public int compareTo(Ticket other){
        if(this.priority==other.priority){
            return this.createdAt.compareTo(other.createdAt);
        }
        return other.priority.compareTo(this.priority);
    }
}
