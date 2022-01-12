grammar CMM;

// Datove typy
INT: 'int'; BOOL: 'bool'; FLOAT: 'float'; // float se nakonec nepouzil

// Aritmeticka operatory
PLUS: '+'; MINUS: '-'; MULT: '*'; DIV: '/'; MOD: '%';

// Porovnani
GREATER: '>'; LESSER: '<'; GREATER_OR_EQUAL: '>='; LESSER_OR_EQUAL: '<='; EQUAL: '=='; NOT_EQUAL: '!=';

// Binarni operatory
AND: '&&'; OR: '||'; NOT: '!';

EQUALS: '=';
INSTANCE_OF: 'is';

// Keywordy
IF: 'if'; ELSE: 'else';

FOR: 'for'; WHILE: 'while'; DO: 'do';

TRUE: 'true'; FALSE: 'false';
VOID: 'void';
RETURN: 'return';

UNDERLINE: '_';
SEMICOLON: ';';
LEFT_PAREN: '(';
RIGHT_PAREN: ')';
LEFT_CURLY: '{';
RIGHT_CURLY: '}';
SINGLE_QUOTE: '\'';
DOUBLE_QUOTE: '"';
COMMA: ',';
DOT: '.';
DOUBLE_DOT: '..';
IN: 'in';
TRIPLE_DOT: '...';
CONST: 'const';

// identifikator pro promennou nebo jmeno funkce
IDENTIFIER: (UPPERCASE_LETTER | LOWERCASE_LETTER | '_')+ (UPPERCASE_LETTER | LOWERCASE_LETTER | '_' | DIGIT)*;

ALPHABET_LETTER: [A-Za-z];
fragment LOWERCASE_LETTER: [a-z];
fragment UPPERCASE_LETTER: [A-Z];
fragment DIGIT: [0-9];

INTEGER_NUMBER: DIGIT+;

// preskakovani whitespaces
WHITESPACE: [\r\t \n] -> skip;

// Legalni symboly pro prirazeni hodnoty promenne
legalVariableLiterals: INTEGER_NUMBER | TRUE | FALSE;

chainAssignment: EQUALS IDENTIFIER; // retezove prirazeni = x1 ... = x2 apod
variableAssignment: IDENTIFIER chainAssignment* EQUALS (legalVariableLiterals | expression) SEMICOLON;
variableDeclaration: legalDataTypes IDENTIFIER SEMICOLON;
variableInitialization: legalDataTypes IDENTIFIER EQUALS (legalVariableLiterals | expression) SEMICOLON;
constVariableInitialization: CONST variableInitialization;

functionDataTypes: (VOID | legalDataTypes); // navratove hodnoty funkci
blockScope: LEFT_CURLY (statement)* RIGHT_CURLY; // { } nebo { var x = 1; } nebo { int x() {} ...}
returnStatement: RETURN (expression)? SEMICOLON; // return;
functionBlockScope: LEFT_CURLY (statement)* returnStatement RIGHT_CURLY;

// definice funkce
functionDefinition: functionDataTypes IDENTIFIER LEFT_PAREN functionParameters? RIGHT_PAREN functionBlockScope;

// funkcni parametr
functionParameter: legalDataTypes IDENTIFIER;

// skupina funkcnich parametru
functionParameters: functionParameter | functionParameter COMMA functionParameters;

// Ve volani funkce muze byt jakykoliv vyraz
expressionChain: (IDENTIFIER | expression) (COMMA expressionChain)?; // (x1) (x1, x2, ... )

functionCall: IDENTIFIER LEFT_PAREN expressionChain? RIGHT_PAREN; // x(); nebo x(identifier_chain)

// pocatecni pravidlo
entrypoint: (functionDefinition | statement)+;

statement:
    blockScope #blockOfCode
    | IF parenthesesExpression blockScope (ELSE blockScope)? #ifStatement
    | FOR forExpression blockScope #forStatement
    | WHILE parenthesesExpression blockScope #whileStatement
    | DO blockScope WHILE parenthesesExpression #doWhileStatement
    | variableDeclaration #variableDeclarationStatement
    | variableInitialization #variableInitializationStatement
    | constVariableInitialization #constVariableInitializationStatement
    | variableAssignment #variableAssignmentStatement
    | functionCall SEMICOLON #functionCallStatement
;

// Odzavorkovany vyraz
parenthesesExpression: LEFT_PAREN expression RIGHT_PAREN;

// Expression ve for
// neni c-like ale je to hezke
forExpression: LEFT_PAREN IDENTIFIER IN expression DOUBLE_DOT expression RIGHT_PAREN;

expression:
    functionCall #functionCallExpression
    | (IDENTIFIER | legalDataTypes) INSTANCE_OF legalDataTypes #instanceOfExpression
    | valueExpr #valueExpression
    | IDENTIFIER #identifierExpression
    // operace pro deleni nasobeni a modulo maji stejnou vahu a chceme resolve jako prvni
    | expression operation = (DIV | MOD | MULT) expression #multiplicationExpression // / * %
    | expression operation = (PLUS | MINUS) expression #additionExpression // + -
    | expression operation = (GREATER | GREATER_OR_EQUAL | EQUAL | LESSER | LESSER_OR_EQUAL | NOT_EQUAL) expression
    #comparisonExpression
    | expression operation = (AND | OR) expression #booleanOperationExpression
    | LEFT_PAREN expression RIGHT_PAREN #parenthesizedExpression
    | NOT expression #negationExpression
    | operation = (PLUS | MINUS) expression #unaryMinusExpression;


valueExpr: INTEGER_NUMBER | TRUE | FALSE | (INTEGER_NUMBER? DOT INTEGER_NUMBER) | (INTEGER_NUMBER DOT);


// Legalni datove typy
legalDataTypes: INT | BOOL;
