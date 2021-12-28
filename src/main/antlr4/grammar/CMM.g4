grammar CMM;

// Datove typy
INT: 'int';
BOOL: 'bool';
FLOAT: 'float';
STRING: 'string';

// Aritmeticka operatory
PLUS: '+';
MINUS: '-';
MULT: '*';
DIV: '/';
MOD: '%';

// Porovnani
GREATER: '>';
LESSER: '<';
GREATER_OR_EQUAL: '>=';
LESSER_OR_EQUAL: '<=';
EQUAL: '==';
NOT_EQUAL: '!=';

// Binarni operatory
AND: '&&';
OR: '||';
NOT: '!';

EQUALS: '=';
TERNARY_QM: '?';
INSTANCE_OF: 'is';

// Keywordy
IF: 'if';
ELSE: 'else';
FOR: 'for';
WHILE: 'while';
REPEAT: 'repeat';
UNTIL: 'until';
DO: 'do';
TRUE: 'true';
FALSE: 'false';
VOID: 'void';
RETURN: 'return';


DIGIT: [0-9];


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
IN: 'IN';
TRIPLE_DOT: '...';
CONST: 'const';
EXP: 'e';

WHITESPACE: [\r\t \n] -> skip;
ALPHABET_LETTER: [A-Za-z];

INTEGER_NUMBER: DIGIT+;

// TODO check
STRING_TEXT: [A-Z a-z()0-9!#%&`*+,_\-.\\;[\]^{}~|]; // TODO utf emoji /s
legalVariableLiterals: INTEGER_NUMBER | STRING_TEXT
                        | TRUE | FALSE
                        | (INTEGER_NUMBER* DOT INTEGER_NUMBER+)
                        | (INTEGER_NUMBER+ DOT);

legalDataTypes: INT | BOOL | FLOAT | STRING;

// identifikator pro promennou nebo jmeno funkce
identifier: (ALPHABET_LETTER | UNDERLINE)+ (ALPHABET_LETTER | UNDERLINE | DIGIT)* ;

// = x
chainAssignment: EQUALS identifier;

variableAssignment: identifier chainAssignment* EQUALS (legalVariableLiterals | expression) SEMICOLON;
variableDeclaration: legalDataTypes identifier SEMICOLON;
variableInitialization: legalDataTypes identifier chainAssignment* EQUALS (legalVariableLiterals | expression) SEMICOLON;
constVariableInitialization: CONST variableInitialization;


functionDataTypes: (VOID | legalDataTypes);

// deklarace funkce
functionDeclaration: functionDataTypes identifier LEFT_PAREN functionParameters? RIGHT_PAREN blockScope;

// funkcni parametr
functionParameter: legalDataTypes identifier;

// skupina funkcnich parametru
functionParameters: functionParameter | functionParameter COMMA functionParameters;

identifierChain: identifier | identifier COMMA identifierChain; // (x1) (x1, x2, ... )

functionCall: identifier LEFT_PAREN identifierChain? RIGHT_PAREN SEMICOLON; // x(); nebo x(identifier_chain)

///
///     CONTROL FLOW + obecna pravidla
///

// pocatecni pravidlo
entrypoint: statement+;

blockScope: LEFT_CURLY (statement)* RIGHT_CURLY; // { } nebo { var x = 1; } nebo { int x() {} ...}

// FOREACH ?
statement:
    blockScope #blockOfCode
    | IF parenthesesExpression blockScope (ELSE blockScope)? #ifStatement
    | FOR forExpression blockScope #forStatement
    | WHILE parenthesesExpression blockScope #whileStatement
    | DO blockScope WHILE parenthesesExpression #doWhileStatement
//    | SWITCH parenthesesExpression switchBlock #switchStatement // TODO doimplementovat
    | variableDeclaration #variableDeclarationStatement
    | variableInitialization #variableInitializationStatement
    | constVariableInitialization #constVariableInitializationStatement
    | variableAssignment #variableAssignmentStatement
    | functionDeclaration #functionDeclarationStatement
    | functionCall #functionCallStatement
;

// Odzavorkovany vyraz
parenthesesExpression: LEFT_PAREN expression RIGHT_PAREN;

// Expression ve for
// neni c-like ale je to hezke
forExpression: FOR LEFT_PAREN identifier IN INTEGER_NUMBER (DOUBLE_DOT | IN | TRIPLE_DOT) INTEGER_NUMBER RIGHT_PAREN;


expression:
    identifier #identifierExpression
    // operace pro deleni nasobeni a modulo maji stejnou vahu a chceme resolve jako prvni
    | expression op = (DIV | MOD | MULT) expression #multiplicationExpression
    // operace +-
    | expression op = (PLUS | MINUS) expression #additionExpression
    // operace pro provnani
    | expression op = (GREATER | GREATER_OR_EQUAL | EQUAL | LESSER | LESSER_OR_EQUAL | NOT_EQUAL) expression
    #comparisonExpression
    // binarni operace
    | expression op = (AND | OR) expression #booleanOperationExpression
    // odzavorkovani
    | LEFT_PAREN expression RIGHT_PAREN #parenthesizedExpression
    // negace vyrazu
    | NOT expression #negationExpression
    // Unarni minus
    | op=(PLUS | MINUS) expression #unaryMinusExpression;
