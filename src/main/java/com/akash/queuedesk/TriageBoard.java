package com.akash.queuedesk;

import java.util.List;

public class TriageBoard {
    public static void printTriageBoard(List<Ticket> tickets) {
        for (Ticket ticket : tickets) {

            System.out.println("Estimated Effort: "
                    + ticket.estimateEffortHours() + " hours");

            System.out.println(ticket);

            if (ticket instanceof Escalatable escalatable) {
                System.out.println("Escalation: "
                        + escalatable.escalationBadge());

                System.out.println("Escalation Level: "
                        + escalatable.escalationLevel());
            }
        }
    }
}
