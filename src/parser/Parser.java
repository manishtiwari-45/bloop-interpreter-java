package parser;

import ast.*;
import lexer.Token;
import lexer.TokenType;
import runtime.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens){
        this.tokens = tokens;
    }


    // Called by Interpreter: parse all tokens → return list of Instructions.
    // Keeps parsing statements until we hit EOF.
    public List<Instruction> parse(){
        List<Instruction> instructions = new ArrayList<>();


        return instructions;
    }

    // Looks at the CURRENT token and decides which instruction to build.
    // Each BLOOP statement starts with a unique keyword. Handled each case.
    private  Instruction parseStatement(){
        return null;
    }

    // Handles the SIMPLEST expressions: a single value with no operator.
    private Expression parsePrimary(){
        return null;
    }

    // Handles  *  and  /  (higher priority than + and -)
    // Calls parsePrimary() to get operands.
    private Expression parseTerm() {
        return null;
    }

    // Handles  '+'  and  '-'  (lower priority —> outermost level)
    // Calls parseTerm() to get operands, this ensures * runs before +
    private Expression parseExpression() {
        return null;
    }

    // PARSING
    private Instruction parseIf(){
        return null;
    }

    private Instruction parseRepeat() {
        return null;
    }

    // Helper methods
    // peek() —> look at the current token WITHOUT consuming it
    private Token peek(){
       return tokens.get(current);
    }

    // peekNext() — look ONE AHEAD without consuming
    private Token peekNext(){
        if(current + 1 >= tokens.size()) {
            return tokens.get(tokens.size() - 1);
        }
        return tokens.get(current + 1);
    }

    // advance() — return the current token AND move forward by one
    private Token advance(){
        Token token = tokens.get(current);
        if(!isAtEnd()){
            current++;
        }
        return token;
    }

    // check() — is the current token this type? Does NOT consume.
    private boolean check(TokenType type){
        if(isAtEnd()){
            return false;
        }
        return peek().isType(type);
    }

    // match() — if current token matches, consume it and return true
    private boolean match(TokenType type){
        if(check(type)){
            advance();
            return true;
        }
        return false;
    }

    // isAtEnd() — are we at the EOF token?
    private boolean isAtEnd(){
        return peek().isType(TokenType.EOF);
    }

    // expect() — MUST be this type. If not, throw a clear error.
    // Used when a token is REQUIRED by BLOOP syntax.
    // Example: after PUT we MUST see a value — if not, that's a syntax error.
    private Token expect(TokenType type, String errorMessage){
        if(check(type)){
            return advance();
        }
        Token bad = peek();
        throw new RuntimeException(errorMessage + " - got '" + bad.value() +
                        "' on line " + bad.line());
    }

    // skipNewlines() — consume any NEWLINE tokens silently
    private void skipNewLines(){
        while (check(TokenType.NEWLINE)){
            advance();
        }
    }

    // parseBody() — parse an indented block of instructions
    // Used by if and repeat. Reads until a non-indented line is found
    private List<Instruction> parseBody(){
        return new ArrayList<>();
    }

    // Parses: put<expression> into <variableName>
    private Instruction parseAssign() {

        expect(TokenType.PUT, "Expected 'put'");
        Expression expression = parseExpression();
        expect(TokenType.INTO, "Expected 'into' after expression in put statement");

        Token nameToken = expect(TokenType.IDENTIFIER,"Expected a variable name after 'into'");
        match(TokenType.NEWLINE);
        return new AssignInstruction(nameToken.value(), expression);
    } 

    // print <expression>
    private Instruction parsePrint() {

        expect(TokenType.PRINT, "Expected 'print'");
        // parse expression after print
        Expression expression = parseExpression();

        match(TokenType.NEWLINE);
        return new PrintInstruction(expression);
    } 
}