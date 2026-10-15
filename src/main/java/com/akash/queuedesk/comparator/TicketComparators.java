package com.akash.queuedesk.comparator;

import java.util.Comparator;
import com.akash.queuedesk.model.Ticket;

public class TicketComparators {

    public static Comparator<Ticket> BY_ASSIGNEE_THEN_PRIORITY =
            Comparator.comparing((Ticket t) -> t.getAssignee(),Comparator.nullsLast(Comparator.naturalOrder()))
                      .thenComparing((Ticket t) -> t.getPriority().getRank());

    public static Comparator<Ticket> BY_STATUS_THEN_AGE =
            Comparator.comparing((Ticket t) -> t.getStatus().getRank())
                      .thenComparing((Ticket t) -> t.ageInHours());

}