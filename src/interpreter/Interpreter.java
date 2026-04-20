package interpreter;

import java.util.List;
import lexer.Token;
import lexer.Tokenizer;
import parser.Parser;
import runtime.Environment;
import runtime.Instruction;

// Connects all three pipeline stages and runs a BLOOP program
public class Interpreter {

    // Takes raw BLOOP source code as a string.
    // Runs it through the full pipeline and produces output to stdout.
    // This is the one method that drives everything.
    public void run(String source){

        // Stage 1: Tokenize
        // Tokenizer reads the source string character by character.
        // Produces an unmodifiable List<Token>.
        Tokenizer tokenizer = new Tokenizer(source);
        List<Token> tokens = tokenizer.tokenize();

        // Stage 2: Parse
        // Parser reads the token list and builds instruction objects.
        // Produces a List<Instruction> ready to execute.
        Parser parser = new Parser(tokens);
        List<Instruction> instructions = parser.parse();

        // Stage 3: Execute
        // Execute each instruction in order, passing the same env to all.
        // Instructions share the env so variables assigned in one instruction
        // are available in the next.
        Environment<Object> env = new Environment<>();
        instructions.forEach(instruction -> instruction.execute(env));

    }
    // runWithDebug
    // Same as run() but prints tokens and instructions before executing.
    // Useful during development — shows the full pipeline in action
    public void runWithDebug(String source){

        System.out.println("═══ BLOOP Debug Mode ═══");

        Tokenizer tokenizer = new Tokenizer(source);
        List<Token> tokens = tokenizer.tokenize();

        System.out.println("═══ Tokens ═══");
        Tokenizer.printTokens(tokens);

        Parser parser = new Parser(tokens);
        List<Instruction> instructions = parser.parse();

        System.out.println("\n═══ Instructions ═══");
        instructions.forEach(System.out::println);

        System.out.println("\n═══ Output ═══");
        Environment<Object> env = new Environment<>();
        instructions.forEach(instruction -> instruction.execute(env));

        System.out.println("\n═══ Final Environment ═══");
        System.out.println(env);
    }
}