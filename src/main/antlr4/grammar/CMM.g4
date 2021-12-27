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

INTEGER_NUMBER: DIGIT+;


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

WHITESPACE: [\r\t \n] -> skip;
ALPHABET_LETTER: [A-Za-z];



///
///        PRAVIDLA PRO PROMENNE
///

// identifikator pro promennou nebo jmeno funkce
identifier: (ALPHABET_LETTER | UNDERLINE)+ (ALPHABET_LETTER | UNDERLINE | DIGIT)* ;


// = x
chainAssignment: EQUALS identifier;

LEGAL_STRING_LITERALS: [a-z A-Z()0-9!#%&`*+,_\-.\\;[\]^{}~|]; // TODO utf emoji /s

// Deklarace - pouze deklarujeme typ bez prirazeni napr. int x;
variableDeclaration: legalDataType identifier SEMICOLON;

// Assignment je mysleno napr. x = 2;
intVariableAssignment: identifier chainAssignment* EQUALS INTEGER_NUMBER | expression;
boolVariableAssignment: identifier chainAssignment* EQUALS (TRUE | FALSE | expression);
stringVariableAssignment: identifier chainAssignment* EQUALS
                          ((SINGLE_QUOTE LEGAL_STRING_LITERALS SINGLE_QUOTE) | (DOUBLE_QUOTE LEGAL_STRING_LITERALS DOUBLE_QUOTE));
variableAssignment: (intVariableAssignment | stringVariableAssignment | boolVariableAssignment) SEMICOLON;

// Initialization - zaroven vytvorime a priradime. Napr. bool y = false;
intVariableIntialization: INT intVariableAssignment;
boolVariableInitialization: BOOL boolVariableAssignment;
stringVariableInitialization: STRING stringVariableAssignment;
variableInitialization: (intVariableIntialization | stringVariableInitialization | boolVariableInitialization) SEMICOLON;
constVariableInitialization: CONST variableInitialization;


///
///         PRAVIDLA PRO FUNKCE
///

// Povoleny datovy typ
legalDataType: INT | BOOL | FLOAT;

// deklarace funkce
functionDeclaration: (VOID | legalDataType) identifier LEFT_PAREN functionParameters* RIGHT_PAREN;

// funkcni parametr
functionParameter: legalDataType identifier;

// skupina funkcnich parametru
functionParameters: functionParameter | functionParameter COMMA functionParameters;

functionCall: identifier LEFT_PAREN RIGHT_PAREN SEMICOLON; // x();

///
///     CONTROL FLOW + obecna pravidla
///

// pocatecni pravidlo
entrypoint: statement*;

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
    // operace pro deleni nasobeni a modulo maji stejnou vahu a chceme resolve jako prvni
    expression op = (DIV | MOD | MULT) expression #multiplicationExpression
    // operace +-
    | expression op = (PLUS | MINUS) expression #additionExpression
    // operace pro provnani
    | expression op = (GREATER | GREATER_OR_EQUAL | EQUAL | LESSER | LESSER_OR_EQUAL | NOT_EQUAL) expression
    #comparisonExpression
    // binarni operace
    | expression op = (AND | OR) expression #booleanPriorityLevel
    // odzavorkovani
    | LEFT_PAREN expression RIGHT_PAREN #parenthesizedExpression
    // negace vyrazu
    | NOT expression #negation
    // Unarni minus
    | op=(PLUS | MINUS) expression #unaryMinusExpression;
