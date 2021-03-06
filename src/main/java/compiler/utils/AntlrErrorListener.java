package compiler.utils;

import lombok.Getter;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class AntlrErrorListener extends BaseErrorListener {

    @Getter
    private static final AntlrErrorListener Instance = new AntlrErrorListener();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                            int charPositionInLine, String msg, RecognitionException e) {
        System.err.println("An error has occured at line " + line + ": " + msg);
        System.out.println("Compilation cannot proceed further. Fix the issue and recompile again");
        System.exit(1);
    }
}
