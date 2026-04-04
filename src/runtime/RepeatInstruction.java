package runtime;

import ast.Expression;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

// Handles: repeat <count> times: <body>
// count is an Expression that evaluates to a number.
// body is the list of instructions to repeat.
public final class RepeatInstruction implements Instruction {

    private final Expression count; // how many times to repeat
    private final List<Instruction> body; // what to repeat

    public RepeatInstruction(Expression count, List<Instruction> body) {
        this.count = count;
        this.body = Collections.unmodifiableList(new java.util.ArrayList<>(body));
    }

    // Execute: evaluate count → run body that many times.
    @Override
    public void execute(VariableStore store) {
        int times = toInt(count.evaluate(store));

        IntStream.range(0, times)
                .forEach(i ->
                        body.forEach(instruction -> instruction.execute(store))
                );
    }

    // Convert the count value to an int safely.
    private int toInt(Object value) {
        if (value instanceof Double d) return d.intValue();
        throw new RuntimeException("repeat count must be a number, got: " + value);
    }

    public Expression getCount() {
        return count;
    }

    public List<Instruction> getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "RepeatInstruction(repeat " + count + " times, "
                + body.size() + " instructions)";
    }
}