package edu.assignment.calculator.dto;

public record CalculationResponse(boolean success, String expression, String result) {
}
