package lexer;

import java.util.Optional;

// Immutable token class (no setters, only getters)
public final class Token {
    // Cannot be extended (ensures immutability)

    private final TokenType type; // what kind of token
    private final String value; // actual text
    private final int line; // line number of source code

    // Constructor
    public Token(TokenType type, String value, int line) {
        this.type = type;
        this.value = value;
        this.line = line;
    }

    // Getters
    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public int getLine() {
        return line;
    }

    // Returns value as Optional (empty if no meaningful value)
    public Optional<String> getValueOptional() {
        return value.isEmpty() ? Optional.empty() : Optional.of(value);
    }

    // Checks if token type matches expected type
    public boolean isType(TokenType expected) {
        return this.type == expected;
    }

    // Checks if token is a keyword
    public boolean isKeyword() {
        return type.isKeyword();
    }

    // Checks both type and value match
    public boolean matches(TokenType expectedType, String expectedValue) {
        return this.type == expectedType && this.value.equals(expectedValue);
    }

    // Returns formatted string for debugging
    @Override
    public String toString() {
        return String.format("Token[%s | \"%s\" | line %d]", type, value, line);
    }

    // Checks equality based on type, value, and line
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true; // same object
        if (!(obj instanceof Token))
            return false; // type check

        Token other = (Token) obj;
        return this.type == other.type
                && this.value.equals(other.value)
                && this.line == other.line;
    }

    // Generates hash based on type, value, and line
    @Override
    public int hashCode() {
        return java.util.Objects.hash(type, value, line);
    }
}
