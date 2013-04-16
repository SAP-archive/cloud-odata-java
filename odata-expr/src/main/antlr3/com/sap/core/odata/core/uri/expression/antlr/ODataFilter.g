grammar ODataFilter;
options {
language = Java;
output = AST;
}

@parser::header {
    package com.sap.core.odata.core.uri.expression.antlr;

	}

@lexer::header {
    package com.sap.core.odata.core.uri.expression.antlr;
    }

expr	:	mulExpr (WS! 'add'^ WS! mulExpr)* ';'!
	;
mulExpr	:	atom(WS! 'mul'^ WS! atom)*
	;
atom	:	'('expr')'->expr
	|	INT
	;

ID  :	('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
    ;

INT :	'0'..'9'+
    ;

WS	:	' ';