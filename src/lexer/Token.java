package lexer;

public class Token {

    private final TokenType type;       // what kind of token
    private final String value;         // actual text
    private final int line;             // line number of source code

    // Constructor
    public Token(TokenType type, String value, int line){
        this.type = type;
        this.value = value;
        this.line = line;
    }

    // Getters
    public TokenType getType(){
        return type;
    }

    public String getValue(){
        return value;
    }

    public int getLine(){
        return line;
    }


    // To print an object
    @Override
    public String toString(){
        return "Token(" + type + ", \"" + value + "\", line=" + line + ")";
    }
    
}
