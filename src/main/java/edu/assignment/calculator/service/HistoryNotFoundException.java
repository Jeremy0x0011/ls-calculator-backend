package edu.assignment.calculator.service;

public class HistoryNotFoundException extends RuntimeException {
    public HistoryNotFoundException(long id) {
        super("History record " + id + " was not found.");
    }
}
