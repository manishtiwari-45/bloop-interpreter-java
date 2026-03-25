package ast;

import runtime.Enviroment;

// Immutable leaf node - holds a string literal from source code.
public final class StringNode implements Expression {
    private final String value;

    public StringNode(String value){
        this.value = value;
    }
    // A string literal always evaluates to itself - no lookup needed.
    public Object evaluate(Environment env){
        return value;
    }
    public String getValue() {
        return  value;
    }
    public String toString() {
        return "StringNode(\"" + value + "\")";
    }
}