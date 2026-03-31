public class parser {

    // Parses a block of instructions (body of IF/REPEAT)
    private List<Instruction> parseBody() {
        List<Instruction> body = new ArrayList<>();

        // Skip empty lines after ':'
        skipNewLines();

        // Read statements until top-level start or EOF
        while (!isAtEnd() && !isTopLevelStart()) {
            Instruction instruction = parseStatement();

            // Add instruction if valid
            if (instruction != null) {
                body.add(instruction);
            }

            // Skip extra new lines
            skipNewLines();
        }

        // Return parsed instructions
        return body;
    }

    // Checks if current token is a top-level statement start
    private boolean isTopLevelStart() {
        TokenType type = peek().getType();

        // Valid top-level tokens
        return type == TokenType.PUT ||
                type == TokenType.PRINT ||
                type == TokenType.IF ||
                type == TokenType.REPEAT ||
                type == TokenType.EOF;
    }
}