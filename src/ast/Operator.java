package ast;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;

public enum Operator {
    ADD ("+", (a, b) -> a + b),
    SUBTRACT("-", (a, b) -> a - b),
    MULTIPLY("*", (a, b) -> a * b),
    DIVIDE ("/", (a, b) -> {
        if (b == 0) throw new RuntimeException("Division by zero");
        return a / b;
    }),
    GREATER (">", (a, b) -> a > b),
    LESS ("<", (a, b) -> a < b),
    EQUAL ("==", (a, b) -> a.equals(b));

    public final String symbol;
    public final BiFunction<Double, Double, Object> apply;

    Operator(String symbol, BiFunction<Double, Double, Object> apply) {
    this.symbol = symbol;
    this.apply = apply;
    }

    public static Optional<Operator> fromSymbol(String symbol) {
        return Arrays.stream(values())
                     .filter(op -> op.symbol.equals(symbol))
                     .findFirst();
    }
    // @Override
    public String toString() { return symbol; }
}