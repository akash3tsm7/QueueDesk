package com.akash.queuedesk;

import java.util.Scanner;

class QueueNumberSimulator {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int startTicketNumber = sc.nextInt();
        int customerCount = sc.nextInt();

        int totalServed = 0;
        int vipCount = 0;
        int priorityRecheckCount = 0;

        for (int customerIndex = 0;
             customerIndex < customerCount;
             customerIndex++) {

            int currentTicketNumber = startTicketNumber + customerIndex;

            System.out.printf(
                    "Now serving ticket #%d%n",
                    currentTicketNumber
            );

            totalServed++;

            if (totalServed % 5 == 0) {
                System.out.println("VIP lane");
                vipCount++;
            }

            if (totalServed % 10 == 0) {
                System.out.println("priority recheck");
                priorityRecheckCount++;
            }
        }

        System.out.println("Total Served: " + totalServed);
        System.out.println("Total VIP: " + vipCount);
        System.out.println("Total priority-recheck: " + priorityRecheckCount);

        sc.close();
    }
}