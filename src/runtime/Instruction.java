package runtime;

// Functional interface for all instructions
@FunctionalInterface
public interface Instruction {

    // Executes instruction using current environment
    // Updates variables or produces output
    void execute(Environment<Object> env);
}