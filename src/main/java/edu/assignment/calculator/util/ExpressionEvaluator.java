package edu.assignment.calculator.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class ExpressionEvaluator {
    private static final int DIVISION_SCALE = 12;

    private ExpressionEvaluator() {
    }

    public static BigDecimal evaluate(String expression) {
        if (expression == null || expression.isBlank()) {
            throw new IllegalArgumentException("Expression must not be blank.");
        }

        Parser parser = new Parser(expression);
        BigDecimal result = parser.parseExpression();
        parser.skipWhitespace();
        if (!parser.isAtEnd()) {
            throw new IllegalArgumentException("Invalid expression near '" + parser.current() + "'.");
        }
        return result;
    }

    private static final class Parser {
        private final String input;
        private int position;

        private Parser(String input) {
            this.input = input;
        }

        private BigDecimal parseExpression() {
            BigDecimal value = parseTerm();
            while (true) {
                skipWhitespace();
                if (consume('+')) {
                    value = value.add(parseTerm());
                } else if (consume('-')) {
                    value = value.subtract(parseTerm());
                } else {
                    return value;
                }
            }
        }

        private BigDecimal parseTerm() {
            BigDecimal value = parseUnary();
            while (true) {
                skipWhitespace();
                if (consume('*')) {
                    value = value.multiply(parseUnary());
                } else if (consume('/')) {
                    BigDecimal divisor = parseUnary();
                    if (divisor.signum() == 0) {
                        throw new IllegalArgumentException("Division by zero is not allowed.");
                    }
                    value = value.divide(divisor, DIVISION_SCALE, RoundingMode.HALF_UP);
                } else {
                    return value;
                }
            }
        }

        private BigDecimal parseUnary() {
            skipWhitespace();
            if (consume('+')) {
                return parseUnary();
            }
            if (consume('-')) {
                return parseUnary().negate();
            }
            return parsePrimary();
        }

        private BigDecimal parsePrimary() {
            skipWhitespace();
            if (consume('(')) {
                BigDecimal value = parseExpression();
                skipWhitespace();
                if (!consume(')')) {
                    throw new IllegalArgumentException("Missing closing parenthesis.");
                }
                return value;
            }
            return parseNumber();
        }

        private BigDecimal parseNumber() {
            skipWhitespace();
            int start = position;
            boolean hasDigit = false;
            boolean hasDecimalPoint = false;

            while (!isAtEnd()) {
                char character = current();
                if (Character.isDigit(character)) {
                    hasDigit = true;
                    position++;
                } else if (character == '.' && !hasDecimalPoint) {
                    hasDecimalPoint = true;
                    position++;
                } else {
                    break;
                }
            }

            if (!hasDigit) {
                throw new IllegalArgumentException(
                        isAtEnd() ? "Expected a number." : "Invalid character '" + current() + "'.");
            }

            try {
                return new BigDecimal(input.substring(start, position));
            } catch (NumberFormatException exception) {
                throw new IllegalArgumentException("Invalid number.");
            }
        }

        private boolean consume(char expected) {
            if (isAtEnd() || current() != expected) {
                return false;
            }
            position++;
            return true;
        }

        private void skipWhitespace() {
            while (!isAtEnd() && Character.isWhitespace(current())) {
                position++;
            }
        }

        private boolean isAtEnd() {
            return position >= input.length();
        }

        private char current() {
            return input.charAt(position);
        }
    }
}
