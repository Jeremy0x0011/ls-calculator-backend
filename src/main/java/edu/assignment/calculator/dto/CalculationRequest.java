package edu.assignment.calculator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CalculationRequest(
        @NotBlank(message = "Expression must not be blank.")
        @Size(max = 256, message = "Expression must be 256 characters or fewer.")
        String expression) {
}
