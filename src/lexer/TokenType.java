package lexer;

/* 
  TokenType represents all possible types of tokens
  that can appear in our scripting language.
*/

public enum TokenType {
    //  Keywords (reserved words in the language)
    PUT,        
    INTO,       
    PRINT,      
    IF,        
    THEN,      
    REPEAT,     
    TIMES,

    //  Literals (actual values in code)
    NUMBER,    
    STRING,

    //  Identifiers
    IDENTIFIER,

    //  Arithmetic Operators
    PLUS,      
    MINUS,      
    STAR,       
    SLASH,

    //  Comparison Operators
    GREATER,       
    LESS,           
    EQUAL_EQUAL,

    //  Special Symbols
    COLON,
    NEWLINE, 

    //  End of File
    EOF    
}
