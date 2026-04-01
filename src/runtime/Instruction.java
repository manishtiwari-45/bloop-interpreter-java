package runtime;

// Functional interface for all instructions
@FunctionalInterface
public interface Instruction {

    // Executes instruction using current environment
    void execute(Environment<Object> env);
}