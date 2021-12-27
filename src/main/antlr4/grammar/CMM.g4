grammar CMM;

// Datove typy
INT: 'int';
BOOL: 'bool';
FLOAT: 'float';
STRING: 'string';
ARRAY: '[]';

// Aritmeticka operatory
ADD: '+';
SUB: '-';
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


VAR_ASSIGNMENT: '=';
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
FUNCTION: 'fun';
VOID: 'void';
RETURN: 'return';

// Zavorky a oddelovatka
LBRACE: '{';
RBRACE: '}';
LPAREN: '(';
RPAREN: ')';
SEMICOLON: ';';
COMMA: ',';

fragment LETTER: [A-Za-z];
fragment NUMBER: [0-9]+;
fragment COLON: ':';

LEGAL_BOOLEAN_VALUES: TRUE | FALSE;
LEGAL_TYPE_VALUES: INT | BOOL | FLOAT | STRING;
LEGAL_ARRAY_TYPE_VALUES: INT ARRAY | BOOL ARRAY | FLOAT ARRAY;

// ignorujeme whitespaces
WHITESPACE: [\r\t \n] -> skip;
