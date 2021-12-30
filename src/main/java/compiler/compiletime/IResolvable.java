package compiler.compiletime;

import compiler.pl0.PL0Instruction;
import java.util.ArrayList;
import java.util.List;

public interface IResolvable {

    default List<PL0Instruction> resolve(GeneratorContext context) {
        return new ArrayList<>();
    }
}
