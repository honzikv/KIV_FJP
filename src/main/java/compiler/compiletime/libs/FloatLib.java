package compiler.compiletime.libs;

import compiler.compiletime.GeneratorContext;
import org.apache.commons.lang3.NotImplementedException;

public class FloatLib {
    public static void addOnStack(GeneratorContext context, Float value) {
        throw new NotImplementedException("TODO implement this feature");
    }

    public static int sizeOf() {
        return 4;
    }

    public static void loadToVariable(GeneratorContext context, long variableAddress) {
        throw new NotImplementedException("TODO implement this feature");
    }
}
