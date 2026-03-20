import lexer.Token;
import lexer.Tokenizer;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws Exception {

        // Read program1 source code into a string
        String source = new String(
                Files.readAllBytes(Paths.get("samples/program1_arithmetic.bloop"))
        );

        // Run the tokenizer
        Tokenizer tokenizer = new Tokenizer(source);
        List<Token> tokens = tokenizer.tokenize();

        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}