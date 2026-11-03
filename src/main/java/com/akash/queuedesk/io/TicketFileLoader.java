package com.akash.queuedesk.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.akash.queuedesk.exception.InvalidTicketDataException;

import com.akash.queuedesk.model.Ticket;
import com.akash.queuedesk.model.TicketPriority;
import com.akash.queuedesk.model.TicketStatus;
import com.akash.queuedesk.model.TicketSeverity;
import com.akash.queuedesk.model.BugTicket;
import com.akash.queuedesk.model.FeatureRequestTicket;
import com.akash.queuedesk.model.AccessRequestTicket;

public class TicketFileLoader{

    public static List<Ticket> load(String path) throws IOException, InvalidTicketDataException {
        List<Ticket> tickets = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            collectTicketsFromReader(reader, tickets);
        }
        return tickets;
    }

    private static void collectTicketsFromReader(BufferedReader reader, List<Ticket> tickets) throws IOException, InvalidTicketDataException {
        String line;
        int lineNumber = 0;

        while ((line = reader.readLine()) != null){
            lineNumber++;
            if (!line.trim().isEmpty()) {
                tickets.add(parseTicketLine(line, lineNumber));
            }
        }
    }

    private static Ticket parseTicketLine(String line , int lineNumber) throws InvalidTicketDataException{
        try{
            String[] fields = splitAndTrimFields(line);
            validateFieldCount(fields, lineNumber);
            return buildTicketFromFields(fields, lineNumber);
        }
        catch (InvalidTicketDataException e) {
            throw e;
        }
        catch (RuntimeException e) {
            throw new InvalidTicketDataException(lineNumber+"Invalid ticket data: " + e.getMessage());
        }
    }

    private static String[] splitAndTrimFields(String line){
        String[] fields = line.split("\\|");
        for (int i = 0; i < fields.length; i++) {
            fields[i] = fields[i].trim();
        }
        return fields;
    }

    private static void validateFieldCount(String[] fields , int lineNumber) throws InvalidTicketDataException{
        if (fields.length != 14) {
            throw new InvalidTicketDataException(lineNumber+ "Wrong field count. Expected 14, found " + fields.length);
        }
    }

    private static Ticket buildTicketFromFields(String[] fields , int lineNumber) throws InvalidTicketDataException{

        String type = fields[0];
        String title = fields[1];
        String description = fields[2];
        String requester = fields[3];
        TicketPriority priority = parsePriority(fields[4], lineNumber);
        TicketStatus status = parseStatus(fields[5], lineNumber);
        String assignee=fields[6].isEmpty() ? null : fields[6];
        Ticket ticket = createTypedTicket(type, title, description, requester, priority, fields, lineNumber);
        ticket.setStatus(status);

        if (assignee != null) {
            ticket.setAssignee(assignee);
        }

        applyTags(ticket, fields[7]);

        return ticket;
    }

    private static TicketPriority parsePriority(String value , int lineNumber) throws InvalidTicketDataException{
        try {
            return TicketPriority.valueOf(value);
        }
        catch (IllegalArgumentException e) {
            throw new InvalidTicketDataException("lineNumber "+lineNumber+" "+"Invalid Priority: " + value);
        }
    }

    private static TicketStatus parseStatus(String value, int lineNumber) throws InvalidTicketDataException {
        try {
            return TicketStatus.valueOf(value);
        }
        catch (IllegalArgumentException e) {
            throw new InvalidTicketDataException("Line " + lineNumber + ": Invalid Status: " + value);
        }
    }

    private static Ticket createTypedTicket(String type, String title, String description, String requester, TicketPriority priority, String[] fields, int lineNumber) throws InvalidTicketDataException{

        switch (type.toUpperCase()) {

            case "BUG":
                return createBugTicket(title, description, requester, priority, fields, lineNumber);

            case "FEATURE":
                return createFeatureTicket(title, description, requester, priority, fields, lineNumber);

            case "ACCESS":
                return createAccessTicket(title, description, requester, priority, fields);

            default:
                throw new InvalidTicketDataException("Line " + lineNumber + ": Unknown ticket type: " + type);
        }

    }

    private static Ticket createBugTicket(String title, String description, String requester, TicketPriority priority, String[] fields, int lineNumber) throws InvalidTicketDataException {

        TicketSeverity severity = parseSeverity(fields[8], lineNumber);
        String stepsToReproduce = fields[9];

        return new BugTicket(title, description, requester, priority, severity, stepsToReproduce);
    }

    private static TicketSeverity parseSeverity(String value, int lineNumber) throws InvalidTicketDataException {

        try {
            return TicketSeverity.valueOf(value);
        }
        catch (IllegalArgumentException e) {
            throw new InvalidTicketDataException("Line " + lineNumber + ": Invalid severity: " + value);
        }
    }

    private static Ticket createFeatureTicket(String title, String description, String requester, TicketPriority priority, String[] fields, int lineNumber) throws InvalidTicketDataException {

        int votes;

        try {
            votes = Integer.parseInt(fields[10]);
        }
        catch (NumberFormatException e) {
            throw new InvalidTicketDataException("Line " + lineNumber + ": Invalid feature vote count: " + fields[10]);
        }

        String businessJustification = fields[11];

        return new FeatureRequestTicket(title, description, requester, priority, votes, businessJustification);
    }

    private static Ticket createAccessTicket(String title, String description, String requester, TicketPriority priority, String[] fields) {

        String systemName = fields[12];
        String accessLevel = fields[13];

        return new AccessRequestTicket(title, description, requester, priority, systemName, accessLevel);
    }

    private static void applyTags(Ticket ticket, String tagField) {
        Set<String> tags = new LinkedHashSet<>();

        for (String tag : tagField.split(",")) {
            String trimmedTag = tag.trim();

            if (!trimmedTag.isEmpty()) {
                tags.add(trimmedTag);
            }
        }

        ticket.setTags(tags);
    }

}
