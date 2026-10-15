package com.akash.queuedesk.triage;

import java.util.List;
import com.akash.queuedesk.model.Ticket;
import com.akash.queuedesk.model.Escalatable;

public class TriageBoard {

    public static void printTriageBoard(List<Ticket> tickets) {
        for (Ticket ticket : tickets) {
            printTicketRow(ticket);
        }
    }

    private static void printTicketRow(Ticket ticket) {
        printEffortAndDetails(ticket);
        if (ticket instanceof Escalatable escalatable) {
            printEscalationInfo(escalatable);
        }
    }

    private static void printEffortAndDetails(Ticket ticket) {
        System.out.println("Estimated Effort: " + ticket.estimateEffortHours() + " hours");
        System.out.println(ticket);
    }

    private static void printEscalationInfo(Escalatable escalatable) {
        System.out.println("Escalation: "       + escalatable.escalationBadge());
        System.out.println("Escalation Level: " + escalatable.escalationLevel());
    }

}
