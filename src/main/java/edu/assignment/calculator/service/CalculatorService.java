package edu.assignment.calculator.service;

import java.math.BigDecimal;
import java.util.List;

import edu.assignment.calculator.dto.CalculationResponse;
import edu.assignment.calculator.dto.HistoryRecordResponse;
import edu.assignment.calculator.model.CalculationHistory;
import edu.assignment.calculator.repository.CalculationHistoryRepository;
import edu.assignment.calculator.util.ExpressionEvaluator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CalculatorService {
    private final CalculationHistoryRepository repository;

    public CalculatorService(CalculationHistoryRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public CalculationResponse calculate(String expression) {
        String normalizedExpression = normalize(expression);
        BigDecimal result = ExpressionEvaluator.evaluate(normalizedExpression);
        String formattedResult = result.stripTrailingZeros().toPlainString();

        repository.save(new CalculationHistory(normalizedExpression, formattedResult));
        return new CalculationResponse(true, normalizedExpression, formattedResult);
    }

    @Transactional(readOnly = true)
    public List<HistoryRecordResponse> getHistory() {
        return repository.findAllByOrderByCreatedAtDescIdDesc().stream()
                .map(record -> new HistoryRecordResponse(
                        record.getId(),
                        record.getExpression(),
                        record.getResult(),
                        record.getCreatedAt()))
                .toList();
    }

    @Transactional
    public void deleteHistory(long id) {
        if (!repository.existsById(id)) {
            throw new HistoryNotFoundException(id);
        }
        repository.deleteById(id);
    }

    private String normalize(String expression) {
        if (expression == null || expression.isBlank()) {
            throw new IllegalArgumentException("Expression must not be blank.");
        }
        String normalized = expression.replace('×', '*').replace('÷', '/').trim();
        if (normalized.length() > 256) {
            throw new IllegalArgumentException("Expression must be 256 characters or fewer.");
        }
        return normalized;
    }
}
