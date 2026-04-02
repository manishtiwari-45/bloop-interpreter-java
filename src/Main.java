import interpreter.Interpreter;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Main {
    public static void main(String[] args){

        // Validate arguments
        if(args.length == 0){
            System.err.println("Usage: java Main <filename.bloop>");
            System.err.println("       java Main <filename.bloop> --debug");
            System.exit(1);
        }
        String filePath = args[0];
        boolean debug = args.length > 1 && args[1].equals("--debug");

        // Read .bloop file
        String source;
        try {
            source = Files.readString(Paths.get(filePath));
        } catch (NoSuchFileException e) {
            System.err.println("Error: file not found - " + filePath);
            System.exit(1);
            return;
        } catch (IOException e){
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
            return;
        }

        // Run the interpreter
        Interpreter interpreter = new Interpreter();
        try {
            if(debug){
                interpreter.runWithDebug(source);
            } else {
                interpreter.run(source);
            }
        } catch (RuntimeException e) {
            // Catch errors from the interpreter (undefined variable, syntax error, etc.)
            System.err.println("\nBLOOP Error: " + e.getMessage());
            System.exit(1);
        }
    }
}