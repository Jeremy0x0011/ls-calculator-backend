package edu.assignment.calculator.controller;

import java.util.List;

import edu.assignment.calculator.dto.CalculationRequest;
import edu.assignment.calculator.dto.CalculationResponse;
import edu.assignment.calculator.dto.HistoryRecordResponse;
import edu.assignment.calculator.dto.MessageResponse;
import edu.assignment.calculator.service.CalculatorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CalculatorController {
    private final CalculatorService calculatorService;

    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @PostMapping("/calculate")
    public CalculationResponse calculate(@Valid @RequestBody CalculationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request body must contain an expression.");
        }
        return calculatorService.calculate(request.expression());
    }

    @GetMapping("/history")
    public List<HistoryRecordResponse> getHistory() {
        return calculatorService.getHistory();
    }

    @DeleteMapping("/history/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponse deleteHistory(@PathVariable long id) {
        calculatorService.deleteHistory(id);
        return new MessageResponse(true, "History record deleted.");
    }
}
