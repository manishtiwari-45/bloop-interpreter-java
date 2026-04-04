package runtime;

import ast.Expression;

// Assigns evaluated expression to a variable
public final class AssignInstruction implements Instruction {

    // Variable name
    private final String varName;

    // Expression to evaluate
    private final Expression expression;

    // Constructor
    public AssignInstruction(String varName, Expression expression) {
        this.varName = varName;
        this.expression = expression;
    }

    // Execute: evaluate expression and store result
    @Override
    public void execute(VariableStore store) {
        Object value = expression.evaluate(store); // evaluate expression
        store.set(varName, value); // store in environment
    }
    // Get variable name
    public String getVarName() { return varName; }

    // Get expression
    public Expression getExpression() { return expression; }

    // String representation
    @Override
    public String toString() {
        return "AssignInstruction(put " + expression + " into " + varName + ")";
    }
}