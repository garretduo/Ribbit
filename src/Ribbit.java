/* Garret Duo
AP Computer Science A
Period 4
Compiler Project */

import java.io.*;
import java.nio.file.*;
import java.util.*;

class Ribbit {
    static boolean hasError = false; // compile time errors
    static boolean hasRuntimeError = false; // runtime errors
    private static Interpreter interpreter = new Interpreter();
    public static void main(String[] args) throws IOException {
        if (args.length != 1) { // no file passed in
            System.out.println("Usage: java -jar Ribbit.jar [fileName].txt");
            System.exit(2);
        }
        else if (args.length == 1) {
            try {
                runFile(args[0]);
            } catch (IOException ex) { // if file does not exist
                System.out.println("Error: txt file not found!");
                hasError = true;
            }
            if (hasError) { // compile-time error
                System.exit(3);
            }
            if (hasRuntimeError) { // runtime error
                System.exit(4);
            }
        }
    }

    public static void runFile(String path) throws FileNotFoundException, IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path)); // converts raw source code into string
        String src = new String(bytes);
        Tokenizer tokenizer = new Tokenizer(src);
        tokenizer.scan(); // scans tokens, generating token list
        if (hasError) { // exits out of program if compilation error while tokenizing
            return;
        }
        Parser parser = new Parser(tokenizer.getTokens());
        List<Statement> statements = parser.parse(); // parses tokens to form AST
        if (hasError) { // exits out of program is compilation error while parsing
            return;
        }
        interpreter.interpret(statements); // executes AST
    }

    // Everything below is for error reporting

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