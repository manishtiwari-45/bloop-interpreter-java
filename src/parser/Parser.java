package parser;

import ast.*;
import lexer.Token;
import lexer.TokenType;
import runtime.*;

import java.util.ArrayList;
import java.util.List;

import java.util.function.Supplier;
import java.util.Map;
import java.util.HashMap;

public class Parser implements IParser{

    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    // OCP: registering a new handler does not touch existing dispatch logic.
    private final Map<TokenType, Supplier<Instruction>> statementParsers = new HashMap<>();

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        registerStatementParsers();
    }

     // Register all known statement parsers HERE. 
    // To add WHILE: add one line. No other method changes. 
    private void registerStatementParsers() { 
        statementParsers.put(TokenType.PUT,    this::parseAssign); 
        statementParsers.put(TokenType.PRINT,  this::parsePrint); 
        statementParsers.put(TokenType.IF,     this::parseIf); 
        statementParsers.put(TokenType.REPEAT, this::parseRepeat); 
        // Future:  statementParsers.put(TokenType.WHILE, this::parseWhile); 
    } 

    // Called by Interpreter: parse all tokens → return list of Instructions.
    // Keeps parsing statements until we hit EOF.
    @Override
    public List<Instruction> parse() {
        List<Instruction> instructions = new ArrayList<>();

        skipNewLines();
        while (!isAtEnd()) {
            Instruction instruction = parseStatement();
            if (instruction != null) {
                instructions.add(instruction);
            }
            skipNewLines();
        }
        return instructions;
    }

 
    // Dispatch is now CLOSED for modification. 
    // It looks up the handler and calls it. It never needs to change. 
    private Instruction parseStatement() { 
        TokenType type = peek().getType(); 
  
        // Look up the handler for this token type 
        Supplier<Instruction> handler = statementParsers.get(type); 
  
        if (handler != null) { 
            return handler.get();   // call the registered parser 
        } 
        if (peek().isType(TokenType.NEWLINE)) { 
            advance(); return null; 
        } 
        throw new RuntimeException( 
            "Unexpected token '" + peek().getValue() + 
            "' on line " + peek().getLine()); 
    } 

    // Handles the SIMPLEST expressions: a single value with no operator.
    private Expression parsePrimary() {
        Token token = advance();
        switch (token.type()) {
            case NUMBER: {
                double value = Double.parseDouble(token.value());
                return new NumberNode(value);
            }
            case STRING: {
                return new StringNode(token.value());
            }
            case IDENTIFIER: {
                return new VariableNode(token.value());
            }
            default:
                throw new RuntimeException(
                        "Expected a value (number, string, or variable) " +
                                "but got '" + token.value() + "' on line " + token.line());
        }
    }

    // Handles * and / (higher priority than + and -)
    // Calls parsePrimary() to get operands.
    private Expression parseTerm() {
        Expression left = parsePrimary();

        // While the next token is * or /, keep consuming
        while (check(TokenType.STAR) || check(TokenType.SLASH)) {
            Token opToken = advance();

            // Map token type → Operator enum
            BinaryOpNode.Operator operator = opToken.isType(TokenType.STAR)
                    ? BinaryOpNode.Operator.MULTIPLY
                    : BinaryOpNode.Operator.DIVIDE;

            Expression right = parsePrimary();
            left = new BinaryOpNode(left, operator, right);
            // 'left' now holds the result and becomes the new left
        }
        return left;
    }

    // Handles '+' and '-' (lower priority —> outermost level)
    // Calls parseTerm() to get operands, this ensures * runs before +
    private Expression parseExpression() {
        Expression left = parseTerm();

        // While next token is + or -, keep consuming
        while (check(TokenType.PLUS) || check(TokenType.MINUS) ||
                check(TokenType.GREATER) || check(TokenType.LESS) ||
                check(TokenType.EQUAL_EQUAL)) {

            Token opToken = advance();

            BinaryOpNode.Operator operator = switch (opToken.type()) {
                case PLUS -> BinaryOpNode.Operator.ADD;
                case MINUS -> BinaryOpNode.Operator.SUBTRACT;
                case GREATER -> BinaryOpNode.Operator.GREATER;
                case LESS -> BinaryOpNode.Operator.LESS;
                case EQUAL_EQUAL -> BinaryOpNode.Operator.EQUAL;
                default -> throw new RuntimeException("Unknown operator: " + opToken.value());
            };
            Expression right = parseTerm();
            left = new BinaryOpNode(left, operator, right);
        }
        return left;
    }

    // PARSING

    // Helper methods
    // peek() —> look at the current token WITHOUT consuming it
    private Token peek() {
        return tokens.get(current);
    }

    // peekNext() — look ONE AHEAD without consuming
    private Token peekNext() {
        if (current + 1 >= tokens.size()) {
            return tokens.get(tokens.size() - 1);
        }
        return tokens.get(current + 1);
    }

    // advance() — return the current token AND move forward by one
    private Token advance() {
        Token token = tokens.get(current);
        if (!isAtEnd()) {
            current++;
        }
        return token;
    }

    // check() — is the current token this type? Does NOT consume.
    private boolean check(TokenType type) {
        if (isAtEnd()) {
            return false;
        }
        return peek().isType(type);
    }

    // match() — if current token matches, consume it and return true
    private boolean match(TokenType type) {
        if (check(type)) {
            advance();
            return true;
        }
        return false;
    }

    // isAtEnd() — are we at the EOF token?
    private boolean isAtEnd() {
        return peek().isType(TokenType.EOF);
    }

    // expect() — MUST be this type. If not, throw a clear error.
    // Used when a token is REQUIRED by BLOOP syntax.
    // Example: after PUT we MUST see a value — if not, that's a syntax error.
    private Token expect(TokenType type, String errorMessage) {
        if (check(type)) {
            return advance();
        }
        Token bad = peek();
        throw new RuntimeException(errorMessage + " - got '" + bad.value() +
                "' on line " + bad.line());
    }

    // skipNewlines() — consume any NEWLINE tokens silently
    private void skipNewLines() {
        while (check(TokenType.NEWLINE)) {
            advance();
        }
    }

    // Parses a block of instructions (body of IF/REPEAT)
    private List<Instruction> parseBody() {
        List<Instruction> body = new ArrayList<>();

        // Skip empty lines after ':'
        skipNewLines();

        // Read statements until top-level or EOF
        while (!isAtEnd() && !isTopLevelStart()) {
            Instruction instruction = parseStatement();

            if (instruction != null) {
                body.add(instruction);
            }

            skipNewLines();
        }

        return body;
    }

    // Checks if current token is a top-level statement start
    private boolean isTopLevelStart() {
        TokenType type = peek().type();

        // Valid top-level tokens
        return type == TokenType.PUT ||
                type == TokenType.PRINT ||
                type == TokenType.IF ||
                type == TokenType.REPEAT ||
                type == TokenType.EOF;
    }

    // Parses: put<expression> into <variableName>
    private Instruction parseAssign() {

        expect(TokenType.PUT, "Expected 'put'");
        Expression expression = parseExpression();
        expect(TokenType.INTO, "Expected 'into' after expression in put statement");

        Token nameToken = expect(TokenType.IDENTIFIER, "Expected a variable name after 'into'");
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

    // Method to parse an IF statement
    private Instruction parseIf() {

        expect(TokenType.IF, "Expected 'if'");

        Expression condition = parseExpression();

        expect(TokenType.THEN, "Expected 'then' after condition in if statement");

        expect(TokenType.COLON, "Expected ':' after 'then'");

        match(TokenType.NEWLINE);

        List<Instruction> body = parseBody();

        return new IfInstruction(condition, body);
    }

    // — parseRepeat —
    // Parses: repeat <count> times: <body>
    // Token sequence:
    // REPEAT expression... TIMES COLON NEWLINE <body instructions>
    private Instruction parseRepeat() {

        // Consume REPEAT
        expect(TokenType.REPEAT, "Expected 'repeat'");
        Expression count = parseExpression();
        expect(TokenType.TIMES, "Expected 'times' after count in repeat statement");
        expect(TokenType.COLON, "Expected ':' after 'times'");

        match(TokenType.NEWLINE);
        List<Instruction> body = parseBody();

        return new RepeatInstruction(count, body);
    }
}