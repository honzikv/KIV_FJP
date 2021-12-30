package compiler.parsing.statement;

/**
 * Typ statementu v jazyce
 */
public enum StatementType {
    VariableAssignment, // prirazeni -> int x = 2;
    VariableInitialization,
    VariableDeclaration,
    IfStatement,
    BlockStatement,
    ForLoop, // for i in (0, 10) { }
    WhileLoop, // while (expression) { }
    DoWhileLoop,
    ForEachLoop, // for each item in array { }
    RepeatUntilLoop, // repeat { } until (expression)
    ReturnStatement,
    FunctionParameter,
    FunctionCall // myFunction();
    ;

}
