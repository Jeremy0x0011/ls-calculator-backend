package edu.assignment.calculator.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class ExpressionEvaluatorTest {
    @Test
    void evaluatesOperatorPrecedenceAndParentheses() {
        assertDecimalEquals("7", "1 + 2 * 3");
        assertDecimalEquals("9", "(1 + 2) * 3");
    }

    @Test
    void evaluatesUnarySignsAndDecimals() {
        assertDecimalEquals("3", "-5 + 8");
        assertDecimalEquals("-6", "3 * -2");
        assertDecimalEquals("0.5", ".25 + .25");
    }

    @Test
    void rejectsMalformedExpressionsAndDivisionByZero() {
        assertThrows(IllegalArgumentException.class, () -> ExpressionEvaluator.evaluate("1 +"));
        assertThrows(IllegalArgumentException.class, () -> ExpressionEvaluator.evaluate("(1 + 2"));
        assertThrows(IllegalArgumentException.class, () -> ExpressionEvaluator.evaluate("1..2"));
        assertThrows(IllegalArgumentException.class, () -> ExpressionEvaluator.evaluate("2 / 0"));
        assertThrows(IllegalArgumentException.class, () -> ExpressionEvaluator.evaluate("1 + x"));
    }

    private void assertDecimalEquals(String expected, String expression) {
        assertEquals(0, new BigDecimal(expected).compareTo(ExpressionEvaluator.evaluate(expression)));
    }
}
