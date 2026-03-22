package lexer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Tokenizer {

    private final String source; // source code — immutable reference
    private int current = 0; // current character position
    private int line = 1; // current line number
    private final List<Token> tokens = new ArrayList<>();

    public Tokenizer(String source) {
        this.source = source;
    }

    // ── tokenize ─────────────────────────────────────────────────
    // Main method. Returns an UNMODIFIABLE list — callers can read only.
    public List<Token> tokenize() {
        while (!isAtEnd()) {
            scanToken();
        }
        tokens.add(new Token(TokenType.EOF, "", line));
        return Collections.unmodifiableList(tokens); // protect the result
    }

    // ── scanToken ────────────────────────────────────────────────
    private void scanToken() {
        char c = advance();
        switch (c) {
            case '+': addToken(TokenType.PLUS, "+"); break;
            case '-': addToken(TokenType.MINUS, "-"); break;
            case '*': addToken(TokenType.STAR, "*"); break;
            case '/': addToken(TokenType.SLASH, "/"); break;
            case '>': addToken(TokenType.GREATER, ">"); break;
            case '<': addToken(TokenType.LESS, "<"); break;
            case ':': addToken(TokenType.COLON, ":"); break;
            case '=':
                if (!isAtEnd() && peek() == '=') {
                    advance();
                    addToken(TokenType.EQUAL_EQUAL, "==");
                }
                break;
            case '"': scanString(); break;
            case '\n':
                addToken(TokenType.NEWLINE, "\\n");
                line++;
                break;
            case '\r':
            case ' ':
            case '\t':
                break; // skip silently
            default:
                if (Character.isDigit(c)) scanNumber(c);
                else if (Character.isLetter(c) || c == '_') scanWord(c);
                else System.err.println("Unknown char '" + c + "' line " + line);
        }
    }

    // ── Generic helper: readWhile ─────────────────────────────────
    // Reads characters as long as condition.test(nextChar) is true.
    // Predicate<Character> is 'char -> boolean' — a functional interface.
    // This removes duplication between scanNumber and scanWord.
    private String readWhile(Predicate<Character> condition) {
        StringBuilder sb = new StringBuilder();
        while (!isAtEnd() && condition.test(peek())) {
            sb.append(advance());
        }
        return sb.toString();
    }

    // ── scanNumber ───────────────────────────────────────────────
    // Uses readWhile with method reference Character::isDigit.
    private void scanNumber(char firstDigit) {
        // readWhile reads all remaining digit characters
        String rest = readWhile(Character::isDigit);
        StringBuilder number = new StringBuilder().append(firstDigit).append(rest);

        // Handle decimal: peek for '.' followed by a digit
        if (!isAtEnd() && peek() == '.' && Character.isDigit(peekNext())) {
            number.append(advance()); // consume '.'
            number.append(readWhile(Character::isDigit)); // digits after dot
        }

        addToken(TokenType.NUMBER, number.toString());
    }

    // ── scanWord ─────────────────────────────────────────────────
    // Uses readWhile with a lambda. Then delegates keyword lookup to
    // TokenType.fromText() — using our upgraded enum.
    private void scanWord(char firstChar) {
        String rest = readWhile(c -> Character.isLetterOrDigit(c) || c == '_');
        String word = firstChar + rest;

        // TokenType.fromText returns Optional<TokenType>.
        // .orElse(IDENTIFIER) gives us a fallback if word is not a keyword.
        TokenType type = TokenType.fromText(word)
                .orElse(TokenType.IDENTIFIER);

        addToken(type, word);
    }

    // ── scanString ───────────────────────────────────────────────
    // Returns Optional<Token> — Optional.empty() if string is unterminated.
    // This makes the error case explicit instead of silent.
    private Optional<Token> scanString() {
        StringBuilder str = new StringBuilder();

        while (!isAtEnd() && peek() != '"') {
            if (peek() == '\n') line++;
            str.append(advance());
        }

        if (isAtEnd()) {
            System.err.println("Error: unterminated string on line " + line);
            return Optional.empty(); // explicit: nothing was produced
        }

        advance(); // consume closing '"'

        Token t = new Token(TokenType.STRING, str.toString(), line);
        tokens.add(t);
        return Optional.of(t); // explicit: here is what was produced
    }

    // ── Low-level helpers ─────────────────────────────────────────
    private boolean isAtEnd() { return current >= source.length(); }

    private char peek() {
        return isAtEnd() ? '\0' : source.charAt(current);
    }

    private char peekNext() {
        return (current + 1 >= source.length()) ? '\0' : source.charAt(current + 1);
    }

    private char advance() { return source.charAt(current++); }

    private void addToken(TokenType type, String value) {
        tokens.add(new Token(type, value, line));
    }

    // ── Debug helper: printTokens ─────────────────────────────────
    // Functional style: stream + forEach to print the token list cleanly.
    // Call this during testing: new Tokenizer(src).tokenize();
    printTokens(tokens);

    public static void printTokens(List<Token> tokens) {
        System.out.println("=== Token List (" + tokens.size() + " tokens) ===");
        tokens.forEach(System.out::println); // method reference — clean
    }
}