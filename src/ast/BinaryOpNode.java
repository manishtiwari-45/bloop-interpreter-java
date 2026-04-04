package ast;
import runtime.VariableStore;



// Represents a binary operation like a + b or x > y
public final class BinaryOpNode implements Expression {

    private final Expression left;
    private final Operator operator; // imported from ast.Operator
    private final Expression right;

    public BinaryOpNode(Expression left, Operator operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
    
    @Override
    public Object evaluate(VariableStore store) {
        Double l = toDouble(left.evaluate(store));
        Double r = toDouble(right.evaluate(store));
        return operator.apply.apply(l, r);
    }

    private Double toDouble(Object val) {
        if (val instanceof Double d) return d;
        if (val instanceof Integer i) return i.doubleValue();
        try { return Double.parseDouble(String.valueOf(val)); }
        catch (NumberFormatException e) {
            throw new RuntimeException("Expected a number but got: '" + val + "'");
        }
    }
   
    // Getters
    public Expression getLeft() {
        return left;
    }

    public Operator getOperator() {
        return operator;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "BinaryOpNode(" + left + " " + operator + " " + right + ")";
    }
}