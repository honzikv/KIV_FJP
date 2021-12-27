package compiler.language.statement;

/**
 * Typ statementu v jazyce
 */
public enum StatementType {
    Assignment, // prirazeni -> int x = 2;
    IfStatement,
    BlockStatement,
    ElseStatement,
    ForLoop, // for i in (0, 10) { }
    WhileLoop, // while (expression) { }
    ForEachLoop, // for each item in array { }
    RepeatUntilLoop, // repeat { } until (expression)
    FunctionDeclaration, // function int myFunction(int param1) { }
    FunctionCall // myFunction();
}
