package runtime;

import ast.Expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Handles: while <condition> do: <body>
// Keeps running body as long as condition is truthy.
public final class WhileInstruction implements Instruction{
    private final Expression condition;
    private final List<Instruction> body;

    public WhileInstruction(Expression condition, List<Instruction> body){
        this.condition = condition;
        this.body = Collections.unmodifiableList(new ArrayList<>(body));
    }

    @Override
    public void execute(Environment<Object> env) {
        while(isTruthy(condition.evaluate(env))){
            body.forEach(instruction -> instruction.execute(env));
        }
    }

    private boolean isTruthy(Object value) {
        if (value instanceof Boolean b) return b;
        if (value instanceof Double d) return d != 0;
        return false;
    }
    public Expression getCondition() { return condition; }
    public List<Instruction> getBody() { return body; }

    @Override
    public String toString() {
        return "WhileInstruction(while " + condition + " do: " + body.size() + " instructions)";
    }

}
