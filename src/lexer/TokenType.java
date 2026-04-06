package lexer;

import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/* 
  TokenType represents all possible types of tokens
  that can appear in our scripting language.
*/

public enum TokenType {
  // Keywords (reserved words in the language)
  PUT,
  INTO,
  PRINT,
  IF,
  THEN,
  REPEAT,
  TIMES,

  // Literals (actual values in code)
  NUMBER,
  STRING,

  // Identifiers
  IDENTIFIER,

  // Arithmetic Operators
  PLUS,
  MINUS,
  STAR,
  SLASH,

  // Comparison Operators
  GREATER,
  LESS,
  EQUAL_EQUAL,

  // Special Symbols
  COLON,
  NEWLINE,
  INDENT,

  // End of File
  EOF;

  // This map stores: keyword (String) → corresponding TokenType
  // Example: "if" → IF, "print" → PRINT
  private static final Map<String, TokenType> KEYWORD_MAP;

  // Static block runs only once when the class is loaded
  static {

    // Create a LinkedHashMap to store keywords
    // LinkedHashMap preserves insertion order
    Map<String, TokenType> map = new LinkedHashMap<>();

    // Add language keywords and their corresponding token types
    map.put("put", PUT);
    map.put("into", INTO);
    map.put("print", PRINT);
    map.put("if", IF);
    map.put("then", THEN);
    map.put("repeat", REPEAT);
    map.put("times", TIMES);

    // Any attempt to modify it later will throw an exception
    KEYWORD_MAP = Collections.unmodifiableMap(map);
  }

  // Set of all keyword TokenTypes (for quick lookup)
  private static final Set<TokenType> KEYWORD_TYPES =
      // Copy all values from KEYWORD_MAP into an efficient enum set
      EnumSet.copyOf(KEYWORD_MAP.values());


  public static Optional<TokenType> fromText(String text) {
    return Optional.ofNullable(KEYWORD_MAP.get(text));
  }

  // Checks if this TokenType is a keyword
  public boolean isKeyword() {
    // Returns true if present in keyword set
    return KEYWORD_TYPES.contains(this);
  }

  public static Map<String, TokenType> getKeywordMap() {
    return KEYWORD_MAP; // cannot be modified
  }
}
