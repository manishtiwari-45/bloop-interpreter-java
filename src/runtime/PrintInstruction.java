package runtime;

import ast.Expression;

// This class Handles: print <expression>
// Evaluates the expression, prints the result to standard output.

public final class PrintInstruction implements Instruction {
    private final Expression expression;
    private final OutputFormatter formatter;

    // Default constructor: uses DefaultOutputFormatter 
    public PrintInstruction(Expression expression){
        this(expression, new DefaultOutputFormatter());
    }

    // Full constructor: inject any OutputFormatter (DIP + testability) 
    public PrintInstruction(Expression expression, OutputFormatter formatter) { 
        this.expression = expression; 
        this.formatter  = formatter; 
    } 

    // Evaluate the expression → convert result to string → print.
    @Override
    public void execute(VariableStore store){
        Object value = expression.evaluate(store);
        String output = formatter.format(value);
        System.out.println(output);
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