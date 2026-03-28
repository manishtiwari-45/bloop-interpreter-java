package ast;

import runtime.Environment;

  // Represents a numeric literal in the AST.
  // This node stores a constant number value.
public final class NumberNode implements Expression {

    // holds the number value
    private final Double value;

    // Constructor to set the value once.
    // Value cannot be changed later.
    public NumberNode(Double value) {
        this.value = value;
    }

    // Returns the stored number.
    // env is not used because it is a constant.
    @Override
    public Object evaluate(Environment env) {
        return value;
    }

    public Double getValue() {
        return value;
    }
    // Returns string format of the node.
    @Override
    public String toString() {
        return "NumberNode(" + value + ")";
    }
}