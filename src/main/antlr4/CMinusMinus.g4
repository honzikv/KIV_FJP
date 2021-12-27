grammar CMinusMinus;

// Datove typy
INT: 'int';
BOOL: 'bool';
FLOAT: 'float';
STRING: 'string';
ARRAY: '[]';

// Operatory
ADD: '+';
SUB: '-';
MULT: '*';
DIV: '/';
MOD: '%';
GREATER: '>';
LESSER: '<';
GREATER_OR_EQUAL: '>=';
LESSER_OR_EQUAL: '<=';
EQUAL: '==';
VAR_ASSIGN: '=';
AND: '&&';
OR: '||';
NOT: '!';
TERNARY_QM: '?';
FOR_EACH: ':';
INSTANCE_OF: 'is';

// Control flow
IF: 'if';
ELSE: 'else';
FOR: 'for';
WHILE: 'while';
REPEAT: 'repeat';
UNTIL: 'until';
DO: 'do';
TRUE: 'true';
FALSE: 'false';
LBRACE: '{';
RBRACE: '}';
LPARENTHESIS: '(';
RPARENTHESIS: ')';
RETURN: 'return';
SEMICOLON: ';';
COMMA: ',';
FUNCTION: 'fun';
VOID: 'void';

fragment LOWERCASE: [a-z];
fragment UPPERCASE: [A-Z];
fragment NUMBER: [0-9]+;

LegalBooleanValues: TRUE | FALSE;
