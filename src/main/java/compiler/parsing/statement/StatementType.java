package compiler.parsing.statement;

/**
 * Typ statementu v jazyce
 */
public enum StatementType {
    VariableAssignment, // prirazeni -> int x = 2;
    VariableInitialization,
    VariableDeclaration,
    IfStatement,
    BlockScope,
    ForLoop, // for (i in 0..100)
    WhileLoop, // while (expression) { }
    DoWhileLoop,
    //    ForEachLoop, // for each item in array { } nevyuzito
    RepeatUntilLoop, // repeat { } until (expression)
    ReturnStatement,
    FunctionParameter,
    FunctionCall // myFunction();

}
