package ast;

import runtime.VariableStore;

// Immutable leaf node - holds a variable name.
public final class VariableNode implements Expression {
    private final String name;
    // Uses Optional to handle the case where the variable does not exist yet.

    public VariableNode(String name){
        this.name = name;
    }

    // Look up the variable in env.
     // env.get(name) returns Optional<Object> - the variable may not exist.
    public Object evaluate(VariableStore store) {
        return store.get(name)
                    .orElseThrow(() ->
                       new RuntimeException("Undefined variable: '" + name + "'"));
    }
    public String getName(){
        return name;
    }
    public String toString(){
        return "VariableNode(" + name + ")";
    }
}