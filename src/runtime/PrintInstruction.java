package runtime;

import ast.Expression;

// This class Handles: print <expression>
// Evaluates the expression, prints the result to standard output.

public final class PrintInstruction implements Instruction {
    private final Expression expression;

    public PrintInstruction(Expression expression){
        this.expression = expression;
    }

    // Evaluate the expression → convert result to string → print.
    @Override
    public void execute(Environment<Object> env){
        Object value = expression.evaluate(env);
        System.out.println(formatOutput(value));
    }
    // Format: if value is a whole number Double, print without decimal.
    private String formatOutput(Object value){
        if(value instanceof Double d){
            // If d is a whole number (no fractional part), print as integer.
            return (d == Math.floor(d)) ? String.valueOf(d.longValue()): d.toString();
        }
        return String.valueOf(value);
    }
    public Expression getExpression(){
        return expression;
    }
    @Override
    public String toString(){
        return "PrintInstruction(print " + expression + ")";
    }
}