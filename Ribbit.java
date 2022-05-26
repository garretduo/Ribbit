/* Garret Duo
AP Computer Science A
Period 4
Compiler Project */

import java.io.*;
import java.nio.file.*;
import java.util.*;

class Ribbit {
    static boolean hasError = false;
    static boolean hasRuntimeError = false;
    private static Interpreter interpreter = new Interpreter();
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java -jar Ribbit.jar [fileName].txt");
            System.exit(2);
        }
        else if (args.length == 1) {
            try {
                runFile(args[0]);
            } catch (IOException ex) {
                System.out.println("Error: txt file not found!");
                hasError = true;
            }
            if (hasError) {
                System.exit(3);
            }
            if (hasRuntimeError) {
                System.exit(4);
            }
        }
    }

    public static void runFile(String path) throws FileNotFoundException, IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        String src = new String(bytes);
        Tokenizer tokenizer = new Tokenizer(src);
        tokenizer.scan();
        if (hasError) {
            return;
        }
        Parser parser = new Parser(tokenizer.getTokens());
        List<Statement> statements = parser.parse();
        if (hasError) {
            return;
        }
        interpreter.interpret(statements);
    }

    public static void error(int line, String msg) {
        hasError = true;
        report(line, "", msg);
    }

    private static void report(int line, String location, String msg) {
        System.out.println("Compile-time error at [line " + line + "]" + location + ": " + msg);
    }

    static void error(Token token, String message) {
        hasError = true;
        report(token.getLine(), " at '" + token.getLexeme() + "'", message);
    }

    public static void runtimeError(RuntimeError error) {
        System.out.println("Runtime error at [line " + error.getLine() + "]: " + error.getMessage());
        hasRuntimeError = true;
    }
}