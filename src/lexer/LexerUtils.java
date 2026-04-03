package lexer;

import java.util.List;

public final class LexerUtils {

    private LexerUtils() {}

    public static void printTokens(List<Token> tokens) {
        System.out.println("=== Token List (" + tokens.size() + " tokens) ===");
        tokens.forEach(System.out::println);
    }

    public static long countOfType(List<Token> tokens, TokenType type) {
        return tokens.stream()
                     .filter(t -> t.isType(type))
                     .count();
    }
}