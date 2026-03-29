package runtime

// Represents an immutable IF instruction (condition + body)
public final class IfInstruction implements Instruction {

    // Condition to evaluate (e.g., score > 50)
    private final Expression condition;

    // List of instructions to execute if condition is true
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

    // Returns condition
    public Expression getCondition() { return condition; }

    // Returns body (already unmodifiable)
    public List<Instruction> getBody() { return body; }

    // String representation of instruction
    @Override
    public String toString() {
        return "IfInstruction(if " + condition + " then " + body.size() + " instructions)";
    }
}