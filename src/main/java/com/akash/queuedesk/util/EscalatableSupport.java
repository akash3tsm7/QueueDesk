package com.akash.queuedesk.util;

public class EscalatableSupport {

    private int escalationLevel = 0;

    public void escalate() {
        escalationLevel++;
    }

    public boolean isEscalated() {
        return escalationLevel > 0;
    }

    public int escalationLevel() {
        return escalationLevel;
    }
}
