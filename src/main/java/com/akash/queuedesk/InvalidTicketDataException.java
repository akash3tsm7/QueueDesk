package com.akash.queuedesk;
import java.util.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.io.*;
class InvalidTicketDataException extends Exception{
    InvalidTicketDataException(String message){
        super(message);
    }
}