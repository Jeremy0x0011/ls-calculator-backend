package edu.assignment.calculator.repository;

import java.util.List;

import edu.assignment.calculator.model.CalculationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalculationHistoryRepository extends JpaRepository<CalculationHistory, Long> {
    List<CalculationHistory> findAllByOrderByCreatedAtDescIdDesc();
}
