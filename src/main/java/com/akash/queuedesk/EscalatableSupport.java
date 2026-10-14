package com.akash.queuedesk;

public class EscalatableSupport {

    private int escalationLevel = 0;

    void escalate() {
        escalationLevel++;
    }

    boolean isEscalated() {
        return escalationLevel > 0;
    }

    int escalationLevel() {
        return escalationLevel;
    }
}
