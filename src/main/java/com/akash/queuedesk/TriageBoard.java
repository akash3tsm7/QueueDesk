package com.akash.queuedesk;

import java.util.List;

public class TriageBoard {
    public static void printTriageBoard(List<Ticket> tickets) {
        for (Ticket ticket : tickets) {

            System.out.println("Estimated Effort: "
                    + ticket.estimateEffortHours() + " hours");

            System.out.println(ticket);

            if (ticket instanceof Escalatable e) {
                System.out.println("Escalation: "
                        + e.escalationBadge());

                System.out.println("Escalation Level: "
                        + e.escalationLevel());
            }
        }
    }
}
