package com.akash.queuedesk.io;

import java.util.List;
import java.io.IOException;
import java.nio.file.Path;

import com.akash.queuedesk.config.TicketJsonConfig;
import com.akash.queuedesk.exception.InvalidTicketDataException;
import com.akash.queuedesk.entity.Ticket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


public class TicketJsonStore {

    private static final ObjectMapper mapper = TicketJsonConfig.getMapper();

    public static void save(List<Ticket> tickets, Path file) throws InvalidTicketDataException {

        try{
            mapper.writerFor(new TypeReference<List<Ticket>>() {}).writeValue(file.toFile(), tickets);
        }

        catch(JsonProcessingException e){
            throw new InvalidTicketDataException("Could not convert tickets to JSON for " + file + ": " + e.getMessage(), e);
        }

        catch(IOException e){
            throw new InvalidTicketDataException("Could not write JSON file " + file + ": " + e.getMessage(), e);
        }

    }

    public static List<Ticket> load(Path file) throws InvalidTicketDataException {

        try{
            return mapper.readValue(file.toFile(), new TypeReference<List<Ticket>>() {});
        }

        catch(JsonProcessingException e){
            throw new InvalidTicketDataException("Invalid JSON ticket data in " + file + ": " + e.getMessage(), e);
        }

        catch(IOException e){
            throw new InvalidTicketDataException("Could not read JSON file " + file + ": " + e.getMessage(), e);
        }

    }

}


