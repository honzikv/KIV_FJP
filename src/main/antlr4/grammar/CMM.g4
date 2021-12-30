grammar CMM;

// Datove typy
INT: 'int'; BOOL: 'bool'; FLOAT: 'float'; STRING: 'string';

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

FOR: 'for'; WHILE: 'while'; REPEAT: 'repeat'; UNTIL: 'until'; DO: 'do';

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
IN: 'IN';
TRIPLE_DOT: '...';
CONST: 'const';

// identifikator pro promennou nebo jmeno funkce
//identifier: (ALPHABET_LETTER | UNDERLINE)+ (ALPHABET_LETTER | UNDERLINE | DIGIT)* ;
IDENTIFIER: (UPPERCASE_LETTER | LOWERCASE_LETTER | '_')+ (UPPERCASE_LETTER | LOWERCASE_LETTER | '_' | DIGIT)*;

ALPHABET_LETTER: [A-Za-z];
fragment LOWERCASE_LETTER: [a-z];
fragment UPPERCASE_LETTER: [A-Z];
fragment DIGIT: [0-9];

INTEGER_NUMBER: DIGIT+;

// preskakovani whitespaces
WHITESPACE: [\r\t \n] -> skip;

// TODO check
//STRING_TEXT: [A-Z a-z()0-9!#%&`*+,_\-.\\;[\]^{}~|]; // TODO fixnout - vubec nefunguje pri parsovani

//IDENTIFIER: (UPPERCASE_LETTER | LOWERCASE_LETTER | '_')+ (UPPERCASE_LETTER | LOWERCASE_LETTER | '_' | DIGIT)*;


//legalVariableLiterals: DIGIT+ | STRING_TEXT+ | TRUE | FALSE | (DIGIT* DOT DIGIT+)| (DIGIT+ DOT);
//stringLiteral: (SINGLE_QUOTE STRING_TEXT SINGLE_QUOTE) | (DOUBLE_QUOTE STRING_TEXT DOUBLE_QUOTE);
legalVariableLiterals: INTEGER_NUMBER | TRUE | FALSE | (INTEGER_NUMBER? DOT INTEGER_NUMBER) | (INTEGER_NUMBER DOT);


chainAssignment: EQUALS IDENTIFIER;
variableAssignment: IDENTIFIER chainAssignment* EQUALS (legalVariableLiterals | expression) SEMICOLON;
variableDeclaration: legalDataTypes IDENTIFIER SEMICOLON;
variableInitialization: legalDataTypes IDENTIFIER chainAssignment* EQUALS (legalVariableLiterals | expression) SEMICOLON;
constVariableInitialization: CONST variableInitialization;


functionDataTypes: (VOID | legalDataTypes);
blockScope: LEFT_CURLY (statement)* RIGHT_CURLY; // { } nebo { var x = 1; } nebo { int x() {} ...}
returnStatement: RETURN (IDENTIFIER | legalVariableLiterals | expression)? SEMICOLON;
functionBlockScope: LEFT_CURLY (statement)* returnStatement RIGHT_CURLY;

// deklarace funkce
functionDeclaration: functionDataTypes IDENTIFIER LEFT_PAREN functionParameters? RIGHT_PAREN functionBlockScope;

// funkcni parametr
functionParameter: legalDataTypes IDENTIFIER;

// skupina funkcnich parametru
functionParameters: functionParameter | functionParameter COMMA functionParameters;

identifierChain: IDENTIFIER | IDENTIFIER COMMA identifierChain; // (x1) (x1, x2, ... )

functionCall: IDENTIFIER LEFT_PAREN identifierChain? RIGHT_PAREN; // x(); nebo x(identifier_chain)

///
///     CONTROL FLOW + obecna pravidla
///

// pocatecni pravidlo
entrypoint: (functionDeclaration | statement)*;


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
//    | functionDeclaration #functionDeclarationStatement
    | functionCall SEMICOLON #functionCallStatement
;

// Odzavorkovany vyraz
parenthesesExpression: LEFT_PAREN expression RIGHT_PAREN;

// Expression ve for
// neni c-like ale je to hezke
forExpression: FOR LEFT_PAREN IDENTIFIER IN INTEGER_NUMBER (DOUBLE_DOT | IN | TRIPLE_DOT) INTEGER_NUMBER RIGHT_PAREN;

expression:
    functionCall #functionCallExpression //TODO pridat
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
legalDataTypes: INT | BOOL | FLOAT | STRING;
