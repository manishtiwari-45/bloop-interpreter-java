package runtime;

import ast.Expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Represents an immutable IF instruction (condition + body)
public final class IfInstruction implements Instruction {

    private final Expression condition;

    // List of instructions to execute if conditiosn is true
    private final List<Instruction> body;

    // Constructor: initializes condition and makes body unmodifiable
    public IfInstruction(Expression condition, List<Instruction> body) {
        this.condition = condition;
        this.body = Collections.unmodifiableList(new ArrayList<>(body));
    }

    // Executes body if condition evaluates to true
    @Override
    public void execute(Environment<Object> env) {
        Object result = condition.evaluate(env);

        if (isTruthy(result)) {
            body.forEach(instruction -> instruction.execute(env));
        }
    }

    // Checks truthiness (Boolean true or non-zero Double)
    private boolean isTruthy(Object value) {
        if (value instanceof Boolean b) return b;
        if (value instanceof Double d) return d != 0;
        return false;
    }

    public Expression getCondition() { return condition; }

    public List<Instruction> getBody() { return body; }

    @Override
    public String toString() {
        return "IfInstruction(if " + condition + " then " + body.size() + " instructions)";
    }
}