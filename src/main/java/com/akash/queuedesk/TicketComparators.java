package com.akash.queuedesk;
import java.util.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.io.*;class TicketComparators {

    static Comparator<Ticket> BY_ASSIGNEE_THEN_PRIORITY =
            Comparator.comparing((Ticket t) -> t.assignee,Comparator.nullsLast(Comparator.naturalOrder()))
                      .thenComparing((Ticket t) -> t.currPriority);

    static Comparator<Ticket> BY_STATUS_THEN_AGE =
            Comparator.comparing((Ticket t) -> t.currStatus)
                      .thenComparing((Ticket t) -> t.ageInHours());
}