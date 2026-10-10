package com.akash.queuedesk;

class InvalidTicketDataException extends Exception{
    InvalidTicketDataException(String message){
        super(message);
    }
}