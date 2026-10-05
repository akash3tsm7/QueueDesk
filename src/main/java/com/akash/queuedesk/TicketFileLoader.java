package com.akash.queuedesk;
import java.util.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.io.*;
class TicketFileLoader{
    List<Ticket> load(String path) throws IOException, InvalidTicketDataException{
        List<Ticket> tickets = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {

                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    String[] parts = line.split("\\|");
                    if (parts.length != 14) {
                        throw new InvalidTicketDataException(
                                lineNumber+
                                "Wrong field count. Expected 14, found "
                                        + parts.length
                        );
                    }
                    for (int i = 0; i < parts.length; i++) {
                        parts[i] = parts[i].trim();
                    }

                    String type = parts[0];

                    String title = parts[1];
                    String description = parts[2];
                    String requester = parts[3];
                    Ticket.Priority priority;
                    try {
                        priority = Ticket.Priority.valueOf(parts[4]);
                    } 
                    catch (IllegalArgumentException e) {
                        throw new InvalidTicketDataException("lineNumber "+lineNumber+" "+"Invalid Priority: " + parts[4]);
                    }
                    Ticket.Status status;
                    try {
                        status = Ticket.Status.valueOf(parts[5]);
                    } 
                    catch (IllegalArgumentException e) {
                        throw new InvalidTicketDataException(lineNumber+"Invalid Status: " + parts[5]);
                    }
                    String assignee=parts[6].isEmpty() ? null : parts[6];

                    Ticket ticket;
                    switch (type.toUpperCase()) {

                        case "BUG":
                            BugTicket.Severity severity = BugTicket.Severity.valueOf(parts[8]);
                            String stepsToReproduce = parts[9];
                            ticket = new BugTicket(
                                    title,
                                    description,
                                    requester,
                                    priority,
                                    severity,
                                    stepsToReproduce
                            );

                            break;

                        case "FEATURE":

                            int votes = Integer.parseInt(parts[10]);

                            String businessJustification = parts[11];

                            ticket = new FeatureRequestTicket(
                                    title,
                                    description,
                                    requester,
                                    priority,
                                    votes,
                                    businessJustification
                            );

                            break;

                        case "ACCESS":

                            String systemName = parts[12];
                            String accessLevel = parts[13];

                            ticket = new AccessRequestTicket(
                                    title,
                                    description,
                                    requester,
                                    priority,
                                    systemName,
                                    accessLevel
                            );

                            break;

                        default:
                            throw new IllegalArgumentException(
                                    "Unknown ticket type: " + type
                            );
                    }

                    ticket.setStatus(status);

                    if (assignee != null) {
                        ticket.setAssignee(assignee);
                    }
                    if (!parts[7].isEmpty()) {

                        String[] tagArray = parts[7].split(",");

                        for (String tag : tagArray) {
                            tag = tag.trim();

                            if (!tag.isEmpty()) {
                                ticket.setTags(Set.of(tag));
                            }
                        }
                    }
                    tickets.add(ticket);
                }
                catch (InvalidTicketDataException e) {
                    throw e;

                }
                catch (RuntimeException e) {
                    throw new InvalidTicketDataException(lineNumber+"Invalid ticket data: " + e.getMessage());
                }
            }
        }
        return tickets;
    }
}
