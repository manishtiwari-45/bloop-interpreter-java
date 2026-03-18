package lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tokenizer {

    private final String source; // The full source code as one string.
    private int current; // Our current position in the source string.
    private int line; // line we are currently on.
    private final List<Token> tokens; // list of tokens
    private static final Map<String, TokenType> KEYWORDS = new HashMap<>();

    // use it to fill the KEYWORDS map with all BLOOP keywords.
    static {
        KEYWORDS.put("put", TokenType.PUT);
        KEYWORDS.put("into",   TokenType.INTO);
        KEYWORDS.put("print",  TokenType.PRINT);
        KEYWORDS.put("if",     TokenType.IF);
        KEYWORDS.put("then",   TokenType.THEN);
        KEYWORDS.put("repeat", TokenType.REPEAT);
        KEYWORDS.put("times",  TokenType.TIMES);
    }
    // CONSTRUCTOR
    // Takes the full source code string.
    public Tokenizer(String source){
        this.source = source;
        this.current = 0;
        this.line = 1;
        this.tokens = new ArrayList<>();
    }
    // Keep scanning until we hit the end,
    // then add EOF and return the completed list.
    public List<Token> tokenize(){
        while (!isAtEnd()){
            scanToken();
        }
        tokens.add(new Token(TokenType.EOF, "", line));
        return tokens;
    }

    // HELPER : isAtEnd()
    // Returns true if we have moved past the last character.
    private boolean isAtEnd(){
        return current >= source.length();
    }

    // CORE: scanToken
    // Called once per loop iteration in tokenize().
    // Reads ONE character and decides what to do with it.
    private void scanToken(){
        char c = advance();
        switch (c) {
            case '+':
                addToken(TokenType.PLUS, "+");
                break;

            case '-':
                addToken(TokenType.MINUS, "-");
                break;

            case '*':
                addToken(TokenType.STAR, "*");
                break;

            case '/':
                addToken(TokenType.SLASH, "/");
                break;

            case '>':
                addToken(TokenType.GREATER, ">");
                break;

            case '<':
                addToken(TokenType.LESS, "<");
                break;

            case ':':
                addToken(TokenType.COLON,":");
                break;

            case '=':
                if(peekNext() == '='){
                    // It's "==" — consume the second '=' by calling advance()
                    advance();
                    addToken(TokenType.EQUAL_EQUAL, "==");
                }
                break;

            case ' ':
            case '\t', '\r':
                break;

            case '\n':
                addToken(TokenType.NEWLINE, "\\n");
                line++; // moved to the next line
                break;

            // DEFAULT
            // If we reach here, the character is something we haven't
            default:
                System.err.println("Warning: unknown character '" + c
                        + "' on line " + line);
                break;
        }
    }

    // HELPER : advance()
    // Returns the CURRENT character AND moves current forward by 1
    public char advance(){
        char c = source.charAt(current);
        current++;
        return c;
    }

    // HELPER: addToken()
    // Creates a new Token and adds it to tokens list.
    private void addToken(TokenType type, String value){
        tokens.add(new Token(type, value, line));
    }

    // HELPER: peekNext
    // Returns the character ONE AHEAD of current, without moving.
    private char peekNext(){
        if(current + 1 >= source.length()){
            return '\0';
        }
        return source.charAt(current+1);
    }
}