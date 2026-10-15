package com.akash.queuedesk.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import java.util.HashSet;
import java.util.Set;

public abstract class Ticket implements Comparable<Ticket>{

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

    public long ageInHours(){
        return ChronoUnit.HOURS.between(createdAt,LocalDateTime.now());
    }

    public static int totalCreated(){
        return nextId-1;
    }

    public abstract double estimateEffortHours();

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    String getRequester(){
        return this.requester;
    }

    public String getAssignee(){
        return this.assignee;
    }

    public Priority getPriority() {
        return this.priority;
    }

    public Status getStatus() {
        return this.status;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setDescription(String description) {
        validateDescription(description);
        this.description = description.trim();
    }

    private void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or blank");
        }
    }

    void setPriority(Priority priority) {
        validateNotNull(priority, "Priority");
        this.priority = priority;
    }

    public void setStatus(Status status) {
        validateNotNull(status, "Status");
        this.status = status;
    }

    private void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
    }

    public void setAssignee(String assignee){
        this.assignee=assignee;
    }

    public void setTags(Set<String> tags){
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
        if(this.priority.getRank()==other.priority.getRank()){
            return this.createdAt.compareTo(other.createdAt);
        }
        return Integer.compare(other.getPriority().getRank(), this.getPriority().getRank());
    }

}
