grammar ODataFilter4;

/*
	 (c) 2013 by SAP AG
*/

options {
    language  = Java; 
    output = AST;
}
tokens{
PARAM; FUNCTION;FUNC_NAME;LAMBDA_PREFIX;NQEntityType;MEMBER;LAMBDA_VAR;CMPLX_KEY;EntityColFunction; EntityFunction;ComplexColFunction;
ComplexFunction;PrimitiveColFunction;PrimitiveFunction; ENTITYTYPENAME;PROPERTY; NAMESPACE; PROPERTY;}
@parser::header {
    package com.sap.core.odata.core.uri.expression.antlr;

	}

@lexer::header {
    package com.sap.core.odata.core.uri.expression.antlr;
    }
    
/*@members {
	
	public ODataWithPredicatesParser(TokenStream input, EdmClass edm) {
		this(input);
		this.edm = edm;	
	}	
	
  	EdmClass edm;
  	boolean isFunction=true;
  
   }
   */

boolCommonExpr	
	: WSP? orExpr-> orExpr
	;
orExpr
	:	andExpr ( WSP! 'or'^ WSP! andExpr )* 
	;
andExpr
	:	equalityExpr ( WSP! 'and'^ WSP! equalityExpr)*
	;
equalityExpr
	:	relationalExpr ( WSP! ('eq'|'ne')^ WSP! relationalExpr)*
//	|	isofExpr
	|	notExpr
	|	boolMethodCallExpr
	;
relationalExpr
	:	commonExpr (WSP! ( 'gt'|'lt'|'le'|'ge')^ WSP! commonExpr)*
	;
commonExpr
	:	mulExpr (WSP! ('add' |'sub')^ WSP!mulExpr)*
	;
mulExpr
	:	atom (WSP! ('mul'|'div'|'mod')^ WSP! atom)*
	;
atom	:	boolParenExpr
	|	methodCallExpr
	|	primitiveLiteral
	|	firstMemberExpr
//	|	functionExpr -> ^(FUNCTION functionExpr)
//	|	negateExpr

//	|	castExpr
	;
boolParenExpr 
	:	'(' boolCommonExpr WSP? ')'-> boolCommonExpr
	;

negateExpr	
	:  '-' WSP? commonExpr -> ^('-' commonExpr)
	;

notExpr 
	: 	'not' WSP? '(' orExpr ')' -> ^('not' orExpr)
	|	'not' WSP BOOLEAN -> ^('not' BOOLEAN)
	|	'not' WSP boolMethodCallExpr  ->^('not'boolMethodCallExpr )
	|	'not' WSP notExpr -> ^('not' notExpr)
	;

/*
isofExpr 
	: 'isof' '(' WSP* ( commonExpr COMMA )? qualifiedTypeName WSP* ')'
	;
castExpr 
	: 'cast' '(' WSP* ( commonExpr COMMA )? qualifiedTypeName WSP* ')'
	;
*/

methodCallExpr
	:	indexOfMethodCallExpr
	|	toLowerMethodCallExpr
	|	toUpperMethodCallExpr
	|	trimMethodCallExpr
	|	substringMethodCallExpr
	|	concatMethodCallExpr
	|	lengthMethodCallExpr
	|	yearMethodCallExpr
	|	yearsMethodCallExpr 
    |	monthMethodCallExpr 
    |	monthsMethodCallExpr 
    |	dayMethodCallExpr 
    |	daysMethodCallExpr 
    |	hourMethodCallExpr 
    |	hoursMethodCallExpr 
    |	minuteMethodCallExpr 
    |	minutesMethodCallExpr 
    |	secondMethodCallExpr 
    |	secondsMethodCallExpr 
    |	roundMethodCallExpr 
    |	floorMethodCallExpr 
    |	ceilingMethodCallExpr 
    |	distanceMethodCallExpr 
    |	geoLengthMethodCallExpr 
    |	getTotalOffsetMinutesExpr
	; 
boolMethodCallExpr 
	:	endsWithMethodCallExpr
	|	startsWithMethodCallExpr
	|	substringOfMethodCallExpr
	|	intersectsMethodCallExpr
	;
substringOfMethodCallExpr
	: 	'substringof' '(' WSP? first=commonExpr  ','  second=commonExpr WSP? ')' -> ^('substringof' $first $second)	
	;
startsWithMethodCallExpr
	:       'startswith' '(' WSP?  first=commonExpr ',' second=commonExpr WSP? ')'-> ^('startswith' $first $second )
	; 
endsWithMethodCallExpr
	:	'endswith' '(' WSP?  first=commonExpr ',' second=commonExpr WSP? ')'-> ^('endswith' $first $second )
	;
lengthMethodCallExpr
	:	'length' '('  WSP? first=commonExpr WSP? ')'-> ^('length' $first)
	;
indexOfMethodCallExpr
	:	'indexof' '(' WSP?  first=commonExpr ',' second=commonExpr WSP? ')'->^('indexof' $first $second)
	;
substringMethodCallExpr
	:	'substring'  '(' WSP? first=commonExpr ',' second=commonExpr (',' third=commonExpr)? WSP? ')'->  ^('substring' $first $second $third?)
	; 
toLowerMethodCallExpr
	:	'tolower' '(' WSP? first=commonExpr WSP? ')'->^('tolower' $first)
	;
toUpperMethodCallExpr
	:	'toupper' '(' WSP? first=commonExpr WSP? ')'->^('toupper' $first)
	;
trimMethodCallExpr
	:	'trim' '(' WSP? first=commonExpr WSP?')' -> ^('trim' $first)
	;
concatMethodCallExpr
	:	'concat' '(' WSP? first=commonExpr ',' second=commonExpr WSP? ')' -> ^('concat' $first $second)	
	; 

yearMethodCallExpr
	:	'year'  '(' WSP? first=commonExpr WSP? ')' ->^('year' $first)
	;
yearsMethodCallExpr
	:	'years'  '(' WSP? first=commonExpr WSP? ')' ->^('years' $first)
	;
monthMethodCallExpr
	:	'month'  '(' WSP? first=commonExpr WSP? ')'->^('month' $first)
	;
monthsMethodCallExpr
	:	'months'  '(' WSP? first=commonExpr WSP? ')'->^('months' $first)
	;
dayMethodCallExpr
	:	'day'  '(' WSP? first=commonExpr WSP? ')'->^('day' $first)
	;
daysMethodCallExpr
	:	'days'  '(' WSP? first=commonExpr WSP? ')'->^('days' $first)
	;
hourMethodCallExpr
	:	'hour'  '(' WSP? first=commonExpr WSP?')'->^('hour' $first)
	;
hoursMethodCallExpr
	:	'hours'  '(' WSP? first=commonExpr WSP?')'->^('hours' $first)
	;
minuteMethodCallExpr
	:	'minute' '(' WSP?  first=commonExpr WSP? ')'->^('minute' $first)
	;
minutesMethodCallExpr
	:	'minutes' '(' WSP?  first=commonExpr WSP? ')'->^('minutes' $first)
	;
secondMethodCallExpr
	:	'second' '('  WSP? first=commonExpr WSP? ')'->^('second' $first)
	;
secondsMethodCallExpr
	:	'seconds' '('  WSP? first=commonExpr WSP? ')'->^('seconds' $first)
	;
roundMethodCallExpr
	:	'round' '(' WSP? first=commonExpr WSP? ')'->^('round' $first)
	;
floorMethodCallExpr
	:	'floor'  '(' WSP? first=commonExpr WSP? ')'->^('floor' $first)
	;
ceilingMethodCallExpr
	:	'ceiling' '(' WSP? first=commonExpr WSP? ')'->^('ceiling' $first)
	; 
getTotalOffsetMinutesExpr 
	:	'gettotaloffsetminutes' '(' WSP? first=commonExpr WSP? ')'->^('gettotaloffsetminutes' $first)	
	;
distanceMethodCallExpr
	:	'geo.distance''(' WSP? first=commonExpr WSP?',' second=commonExpr WSP? ')'-> ^('geo.distance' $first $second)
	;
geoLengthMethodCallExpr
	:	'geo.length' '('  WSP?first=commonExpr WSP?')'->^('geo.length' $first)
	;
intersectsMethodCallExpr
	:	'geo.intersects' '(' WSP?first=commonExpr WSP? ',' second=commonExpr WSP? ')'-> ^('geo.intersects' $first $second)
	;
firstMemberExpr
	: (qualifiedEntityTypeName '/')? property-> ^(MEMBER property qualifiedEntityTypeName? )
	;
/*
firstMemberExpr:(lambda=lambdaPredicatePrefixExpr)? {edm.isMemberExpression(input.LT(1).getText())}? memberExpr -> ^(MEMBER  memberExpr $lambda?);

memberExpr: (qualifiedEntityTypeName '/')? 
	(	entityColNavigationProperty  (collectionNavigationExpr)? 
            |	entityNavigationProperty    (singleNavigationExpr)?
            |	complexColProperty          (collectionPathExpr)?
            |	complexProperty (complexPathExpr)?
            |	primitiveColProperty        (collectionPathExpr)?
  		 	|	primitiveProperty (singlePathExpr)?
            |	streamProperty 
            |	boundFunctionExpr 
             )
	;

	
collectionNavigationExpr
	: count
	| '/' (qualifiedEntityTypeName '/' )? 
                           ( 	boundFunctionExpr 
                           |	anyExpr
                           |	allExpr 
                           )
        ;
singleNavigationExpr	
	:	 '/' memberExpr
	;
	
collectionPathExpr
	:	 count 
    |	 '/' boundFunctionExpr
    |	 '/' anyExpr
    |	 '/' allExpr
    ;

complexPathExpr
	:	'/' (qualifiedComplexTypeName '/')? 
		(	primitiveProperty (singlePathExpr)? 
		|	complexProperty (complexPathExpr)?
		|	boundFunctionExpr)	
	;
singlePathExpr 	
	: '/' boundFunctionExpr
	;
boundFunctionExpr
	:	functionExpr;
functionExpr
@init{
String nextToken ;}
	:	 (operationQualifier)? {nextToken = input.LT(1).getText();}{edm.findFunction(nextToken)}?
		(	entityColFunction[nextToken] param=functionExprParameters (collectionNavigationExpr)? //->^(EntityColFunction entityColFunction $param collectionNavigationExpr? )
		|	entityFunction[nextToken] param=functionExprParameters (singleNavigationExpr)? //-> ^(EntityFunction entityColFunction $param singleNavigationExpr?)
		|	complexColFunction[nextToken] param=functionExprParameters (collectionPathExpr)?//->^(ComplexColFunction entityColFunction $param collectionPathExpr?)
		|	complexFunction[nextToken]	param=functionExprParameters (complexPathExpr)? //->^(ComplexFunction entityColFunction $param complexPathExpr?)
		|	primitiveColFunction[nextToken]	param=functionExprParameters (collectionPathExpr)? //->^(PrimitiveColFunction entityColFunction $param collectionPathExpr?)
		|	primitiveFunction[nextToken] param=functionExprParameters (singlePathExpr)? //->^(PrimitiveFunction entityColFunction $param singlePathExpr?)
		)
		;
functionExprParameters
	:	'(' (functionExprParameter (',' functionExprParameter)* )?')'-> ^(PARAM functionExprParameter*)
	;
functionExprParameter
	:	name=functionParameterName '=' parameterValue -> ^('=' $name parameterValue)
	|	name=functionParameterName '=' firstMemberExpr -> ^('=' $name firstMemberExpr)
	;	
functionParameterName 
	: ODATA_ID
	;
parameterValue
	:	primitiveLiteral
//	|	complexInUri
//	|	complexColInUri
//	|	primitiveColInUri
	;
anyExpr
	:  'any' '(' (lambdaVar=lambdaVariableExpr ':' lambdaPred=lambdaPredicateExpr)? ')'
	-> ^('any' $lambdaVar? $lambdaPred?)
	;
allExpr
	:  'all' '(' (lambdaVar=lambdaVariableExpr ':' lambdaPred=lambdaPredicateExpr)?')'
		-> ^('all' $lambdaVar? $lambdaPred?)
	;
lambdaPredicatePrefixExpr
	:	varExpr=inscopeVariableExpr '/'-> ^(LAMBDA_PREFIX $varExpr)
	;
	
inscopeVariableExpr
	:	implicitVariableExpr
	|	lambdaVariableExpr
	;
	
implicitVariableExpr
	:	'$it'	
	;
	
lambdaVariableExpr
	:	ID ->^(LAMBDA_VAR ID)	
	;
lambdaPredicateExpr
	: 	boolCommonExpr
	;
	
qualifiedTypeName
	:	qualifiedEntityTypeName
	|	qualifiedComplexTypeName
	|	qualifiedEnumerationTypeName
		qualifiedXTypeName
	|	primitiveTypeName
	|	'collection(' 
		( qualifiedXTypeName
		(qualifiedEntityTypeName
	|	qualifiedComplexTypeName
	|	qualifiedEnumerationTypeName
	|	primitiveTypeName ) ')'
	;*/
primitiveLiteral
	:	STRING | INT | BOOLEAN
	;
// Sp�ter l�schen die n�chste Regel!!!!
property	
	:	ODATA_ID -> ^(PROPERTY ODATA_ID)
	;
/*
count 
	:	'/$count';
primitiveProperty	
	:	{edm.findPrimitiveProperty(input.LT(1).getText())}? ODATA_ID
	;
complexProperty
	: {edm.findComplexProperty(input.LT(1).getText())}? ODATA_ID
	;
complexColProperty
	:	{edm.findComplexColProperty(input.LT(1).getText())}? ODATA_ID
	;
navigationProperty
	:	{edm.findPrimitiveProperty(input.LT(1).getText())}? ODATA_ID
	;
entityNavigationProperty
	:	{edm.findEntityNavigationProperty(input.LT(1).getText())}? ODATA_ID
	;
entityColNavigationProperty 
	:	{edm.findEntityColNavigationProperty(input.LT(1).getText())}? ODATA_ID
	; 
primitiveColProperty	
	:	{edm.findPrimitiveColProperty(input.LT(1).getText())}? ODATA_ID
	;
streamProperty	
	:	{edm.findStreamProperty(input.LT(1).getText())}? ODATA_ID;
operationQualifier	
	:	(namespace '.')? entityContainerName '.';	
	*/
qualifiedEntityTypeName
	:	namespace '.' entityTypeName-> ^(ENTITYTYPENAME namespace entityTypeName);
// Sp�ter l�schen die n�chste Regel!!!!
namespace: ODATA_ID // ('.' ODATA_ID)*->ODATA_ID+
	;
// Sp�ter l�schen die n�chste Regel!!!!
entityTypeName :  ODATA_ID;
	/*
qualifiedComplexTypeName 
	:	namespace '.' 	complexTypeName;
qualifiedEnumerationTypeName
	:	namespace '.' enumerationTypeName
	;
qualifiedXTypeName
	:	namespace '.' (entityTypeName | complexTypeName | enumerationTypeName)
	;
primitiveTypeName
	:	('edm')? ('binary')
	;
entitySetName : ODATA_ID ;
entityTypeName : {edm.findEntityType(input.LT(1).getText())}? ODATA_ID;
complexTypeName : {edm.findComplexType(input.LT(1).getText())}? ODATA_ID;
enumerationTypeName	
	: 	{edm.findEnumerationType(input.LT(1).getText())}? ODATA_ID;
entityContainerName 		
	:	ODATA_ID;
entityFunction[String nextToken]
	:	{edm.findEntityFunction(nextToken)}? ODATA_ID;
entityColFunction[String nextToken]
	:	{edm.findColFunction(nextToken)}? ODATA_ID;
complexFunction[String nextToken]
	:	{edm.findComplexFunction(nextToken)}? ODATA_ID;
complexColFunction[String nextToken]
	:	{edm.findComplexColFunction(nextToken)}? ODATA_ID;
primitiveFunction[String nextToken]
	:	{edm.findPrimitiveFunction(nextToken)}? ODATA_ID;
primitiveColFunction[String nextToken]
	:	{edm.findPrimitiveColFunction(nextToken)}? ODATA_ID;
namespace: ODATA_ID ('.' ODATA_ID)*;
*/
BOOLEAN : 'true' | 'false';
INT	:	DIGIT+;
STRING @init{String a="";}
	:	QUOTA{a=a+"'"; setText(a);} 
		(PCHAR{a=a+$PCHAR.text; setText(a);}
		|DOUBLE_QUOTA{a=a+"'"; setText(a);}
		|UNENCODED{a=a+$UNENCODED.text; setText(a);})* QUOTA {a=a+"'"; setText(a);};
ODATA_ID	
	:	IDLeadingCharacter IDCharacter*;
fragment
IDLeadingCharacter
	:	
	(ALPHA | '_');
fragment IDCharacter
	:	ALPHA|DIGIT|'_';
ID	:	PCHAR+;
fragment
PCHAR	:	UNRESERVED|PCT_Encoded| '!' | '$' | '&' | '*' | '+' | ';'| ':'| '@'; // ANMERKUNG!!!!'':'' ist ausgeschlossen. Nachfragen!
fragment
UNRESERVED    
	:	 ALPHA| DIGIT | '-' | '_' | '~'; // ANMERKUNG!!!!'.' ist ausgeschlossen. Nachfragen!
fragment UNENCODED : HTAB | SP | '"' | '{' | '}' | '[' | ']'; 
fragment SP : ' '; 
fragment HTAB : '\t';
fragment
PCT_Encoded 
	:	 '%' HexDigit HexDigit
	;
fragment ALPHA:  'a'..'z'|'A'..'Z';
fragment
HexDigit : ('0'..'9'|'a'..'f'|'A'..'F') ;
fragment
DIGIT	:	'0'..'9';
fragment QUOTA
	:	'\'';
fragment
DOUBLE_QUOTA : '\'\'';
COMMA : ',';
WSP	:	HTAB| SP+;