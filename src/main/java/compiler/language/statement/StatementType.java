package compiler.language.statement;

/**
 * Typ statementu v jazyce
 */
public enum StatementType {
    VariableAssignment, // prirazeni -> int x = 2;
    ConstVariableInitialization,
    VariableInitialization,
    VariableDeclaration,
    IfStatement,
    BlockStatement,
    ForLoop, // for i in (0, 10) { }
    WhileLoop, // while (expression) { }
    DoWhileLoop,
    ForEachLoop, // for each item in array { }
    RepeatUntilLoop, // repeat { } until (expression)
    FunctionDeclaration, // function int myFunction(int param1) { }
    FunctionParameter,
    FunctionCall // myFunction();
    ;

}
