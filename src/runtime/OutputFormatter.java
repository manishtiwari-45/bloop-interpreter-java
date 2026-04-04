package runtime;

// Abstraction for formatting a BLOOP value before printing. 
// SRP: formatting logic is isolated from instruction execution logic. 
// OCP: add a new formatter (e.g. JSON, debug, verbose) without changing PrintInstruction.  

@FunctionalInterface 
public interface OutputFormatter { 
    String format(Object value); 
} 