package ast;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;
import runtime.Environment;

// Represents a binary operation like a + b or x > y
public final class BinaryOpNode implements Expression {

    // Enum for operators (+, -, *, etc.)
    public enum Operator {

        ADD("+", (a, b) -> a + b),
        SUBTRACT("-", (a, b) -> a - b),
        MULTIPLY("*", (a, b) -> a * b),

        // Handle divide safely
        DIVIDE("/", (a, b) -> {
            if (b == 0)
                throw new RuntimeException("Division by zero");
            return a / b;
        }),

        MODULO("%",(a,b) -> {
            if(b == 0){
                throw new RuntimeException("Modulo by zero");
            }
            return a%b;
        }),

        GREATER(">", (a, b) -> a > b),
        LESS("<", (a, b) -> a < b),
        EQUAL("==", (a, b) -> a.equals(b));

        // Symbol of operator
        final String symbol;

        // Function to apply operation
        final BiFunction<Double, Double, Object> apply;

        // Constructor
        Operator(String symbol, BiFunction<Double, Double, Object> apply) {
            this.symbol = symbol;
            this.apply = apply;
        }

        // Find operator by symbol
        public static Optional<Operator> fromSymbol(String symbol) {
            return Arrays.stream(values())
                    .filter(op -> op.symbol.equals(symbol))
                    .findFirst();
        }

        @Override
        public String toString() {
            return symbol;
        }
    }

    // Left expression
    private final Expression left;

    // Operator
    private final Operator operator;

    // Right expression
    private final Expression right;

    // Constructor
    public BinaryOpNode(Expression left, Operator operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    // Evaluate expression
    @Override
    public Object evaluate(Environment env) {

        // Evaluate both sides
        Object leftVal = left.evaluate(env);
        Object rightVal = right.evaluate(env);

        // Convert to Double
        Double l = toDouble(leftVal);
        Double r = toDouble(rightVal);

        // Apply operator
        return operator.apply.apply(l, r);
    }

    // Convert Object to Double
    private Double toDouble(Object val) {

        if (val instanceof Double)
            return (Double) val;

        if (val instanceof Integer)
            return ((Integer) val).doubleValue();

        if (val instanceof String) {
            try {
                return Double.parseDouble((String) val);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Expected number but got: " + val);
            }
        }

        // Invalid type
        throw new RuntimeException("Invalid value for arithmetic: " + val);
    }

    // Getters
    public Expression getLeft() {
        return left;
    }

    public Operator getOperator() {
        return operator;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "BinaryOpNode(" + left + " " + operator + " " + right + ")";
    }
}