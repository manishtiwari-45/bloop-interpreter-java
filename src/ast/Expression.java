package ast;

import runtime.Environment;

/*
 Defines a functional contract for all expression nodes in the AST.
 Provides a single evaluation method compatible with lambda expressions.
 */
@FunctionalInterface
public interface Expression {

    /*
    Evaluates the expression within the provided runtime environment.
    Returns a generic Object to support multiple result types.
     */
    Object evaluate(Environment env);
}