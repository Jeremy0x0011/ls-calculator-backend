package edu.assignment.calculator.dto;

import java.time.Instant;

public record HistoryRecordResponse(long id, String expression, String result, Instant createdAt) {
}
