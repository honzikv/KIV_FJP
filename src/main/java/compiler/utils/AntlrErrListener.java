package compiler.utils;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class AntlrErrListener extends BaseErrorListener {

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                            int charPositionInLine, String msg, RecognitionException e) {
        System.err.println("An error has occured at line " + line + ": " + msg);
        System.out.println("Compilation cannot procede further. Fix the issue and recompile again");
        System.exit(1);
    }
}
