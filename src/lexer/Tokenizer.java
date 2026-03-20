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
            // We call scanString() to read until the closing quote    
            case '"':
                scanString();
                break;

            // DEFAULT
            // If we reach here, the character is something we haven't
            default:
                System.err.println("Warning: unknown character '" + c
                        + "' on line " + line);
                break;

                // Check if the current character is a digit (0–9)
                // Character.isDigit(c) is a built-in method that returns true for numeric characters
                if (Character.isDigit(c)) {
                        scanNumber(c); // handle the full number
                }

                // Check if the character is a letter (a–z, A–Z) or underscore (_)
                // Underscore is allowed to support variable names like my_var
                else if (Character.isLetter(c) || c == '_') {
                        scanWord(c); // handle keyword or identifier — Day 11
                }
                else {
                        System.err.println("Warning: unknown character '"
                            + c + "' on line " + line);
                }
                break;
        } 

    }

    // scanNumber: reads full integer/decimal and creates NUMBER token
    private void scanNumber(char firstDigit) {

        // Start with first digit
        StringBuilder number = new StringBuilder();
        number.append(firstDigit); // e.g. starts as "3"

        // Read continuous digits
        while (!isAtEnd() && Character.isDigit(peek())) {
            number.append(advance()); // consume the digit and add to our string
        }

        // Check and read decimal part
        if (!isAtEnd() && peek() == '.' && Character.isDigit(peekNext())) {
            number.append(advance()); // consume the '.' dot character

            // Read digits after decimal
            while (!isAtEnd() && Character.isDigit(peek())) {
                number.append(advance());
            }
        }

        // Create NUMBER token
        addToken(TokenType.NUMBER, number.toString());
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

    // HELPER: peek()
    // Returns the current character WITHOUT moving forward.
    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    // HELPER: peekNext
    // Returns the character ONE AHEAD of current, without moving.
    private char peekNext(){
        if(current + 1 >= source.length()){
            return '\0';
        }
        return source.charAt(current+1);
    }


    // HELPER: scanString
    // Reads characters until closing " and creates a STRING token
    private void scanString() {

        StringBuilder str = new StringBuilder(); // build the string content here

        // Keep reading until we find the closing '"' or run out of source.
        while (!isAtEnd() && peek() != '"') {
            if (peek() == '\n') line++;
            str.append(advance()); // consume each character inside the string
        }

        if (isAtEnd()) {
            // We reached the end of the file without finding a closing quote.
            System.err.println("Error: unterminated string on line " + line);
            return; // stop — don't add a broken token
        }

        // Consume the closing 
        advance();

        // Create the STRING token. Value is just the inner text, no quotes.
        addToken(TokenType.STRING, str.toString());
    }


    // HELPER: scanString
    //Reads letters/digits/_ to form a word and decides if it's keyword or identifier
    private void scanWord(char firstChar) {

        StringBuilder word = new StringBuilder();
        word.append(firstChar); // start with first character

        while (!isAtEnd()
               && (Character.isLetterOrDigit(peek()) || peek() == '_')) {
            word.append(advance());  // build full word
        }

        String text = word.toString(); // complete word
        TokenType type = KEYWORDS.get(text); // check if keyword

        if (type == null) {
            type = TokenType.IDENTIFIER; // else identifier
        }


        addToken(type, text);
    }






}
