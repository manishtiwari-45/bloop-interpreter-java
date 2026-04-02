import lexer.Token;
import lexer.Tokenizer;
import parser.Parser;
import runtime.Instruction;
import java.nio.file.Files;
import java.nio..file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        String source= new String(
            Files.readAllBytes(Paths.get("sample/program1_arithmetic.bloop"))
        );

        Tokenizer tokenizer = new Tokenizer(source);
        List<Token> token = tokenizer.tokenize();

        System.out.println("===TOKENS===");
        Tokenizer.printTokens(tokens);

        Parser parser = new Parser(tokens);
        List<Instruction> instruction = parser.parser();
        System.out.println("\n===INSTRUCTIONS===");
        for(Instruction inst: instruction){
            System.out.println(inst);
        }

  
    }
}