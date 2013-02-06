tree grammar ODataTreeWalker;
 
options{
language = Java; 
    
    tokenVocab = ODataFilter4;   // ANTLR gets a set of predefined tokens and token types  
    ASTLabelType = CommonTree; // Set the target language type for all  tree labels and 
    // tree-valued expressions  
 }
 
@header {
    package com.sap.core.odata.core.uri.expression.antlr;
    import com.sap.core.odata.api.uri.expression.CommonExpression;
    import com.sap.core.odata.api.uri.expression.BinaryOperator;
    import com.sap.core.odata.api.uri.expression.UnaryOperator;
    import com.sap.core.odata.api.uri.expression.MethodOperator;
    import com.sap.core.odata.core.uri.expression.PropertyExpressionImpl;
    
    import com.sap.core.odata.core.uri.expression.LiteralExpressionImpl;
    import com.sap.core.odata.core.uri.expression.MemberExpressionImpl;
    import com.sap.core.odata.core.edm.EdmString;
    import com.sap.core.odata.api.edm.EdmLiteral;
    }
    
   expr	returns [CommonExpression value]
   		:	
 	 // ................ BinaryExpression ................//
  		^('or' leftSide=expr rightSide=expr)  {$value=new MyBinaryExpressionImpl(BinaryOperator.OR, leftSide, rightSide);}
 	|	^('and' leftSide=expr rightSide=expr)  {$value=new MyBinaryExpressionImpl(BinaryOperator.AND, leftSide, rightSide);}
 	|	^('sub' leftSide=expr rightSide=expr)  {$value=new MyBinaryExpressionImpl(BinaryOperator.SUB, leftSide, rightSide);}
 	|	^('add' leftSide=expr rightSide=expr)  {$value=new MyBinaryExpressionImpl(BinaryOperator.ADD, leftSide, rightSide);}
 	|	^('eq' leftSide=expr rightSide=expr)  {$value=new MyBinaryExpressionImpl(BinaryOperator.EQ, leftSide, rightSide);}
 	|	^('ne' leftSide=expr rightSide=expr)  {$value=new MyBinaryExpressionImpl(BinaryOperator.NE, leftSide, rightSide);}
 	|	^('gt' leftSide=expr rightSide=expr)  {$value=new MyBinaryExpressionImpl(BinaryOperator.GT, leftSide, rightSide);}
 	|	^('lt' leftSide=expr rightSide=expr)  {$value=new MyBinaryExpressionImpl(BinaryOperator.LT, leftSide, rightSide);}
 	|	^('ge' leftSide=expr rightSide=expr)  {$value=new MyBinaryExpressionImpl(BinaryOperator.GE, leftSide, rightSide);}
 	|	^('le' leftSide=expr rightSide=expr)  {$value=new MyBinaryExpressionImpl(BinaryOperator.LE, leftSide, rightSide);}
    |   ^('mul' leftSide=expr rightSide=expr)  {$value=new  MyBinaryExpressionImpl(BinaryOperator.MUL, leftSide, rightSide);}
    |   ^('div' leftSide=expr rightSide=expr)  {$value=new  MyBinaryExpressionImpl(BinaryOperator.DIV, leftSide, rightSide);}
    |   ^('mod' leftSide=expr rightSide=expr)  {$value=new  MyBinaryExpressionImpl(BinaryOperator.MODULO, leftSide, rightSide);}
	
	
	// ................ UnaryExpression ................//
	|	^('not' operand=expr) {$value = new MyUnaryExpressionImpl(UnaryOperator.NOT, operand);}
	|	^('-' operand=expr )  {$value=new MyUnaryExpressionImpl(UnaryOperator.MINUS, operand);}
  	
  
  	// ................ MethodExpression ................// (InfoMethod ist nicht public -> MyMethodInfo ist Kopie von InfoMethod)
  	|	^('substringof' expr1=expr expr2=expr)	{$value=new MyMethodExpressionImpl(new MyInfoMethod(MethodOperator.SUBSTRINGOF, "syntax",2,2,null)).appendParameter(expr1).appendParameter(expr2);}
    | 	^('startswith' expr1=expr expr2=expr) {$value=new MyMethodExpressionImpl(new MyInfoMethod(MethodOperator.STARTSWITH, "syntax",2,2,null)).appendParameter(expr1).appendParameter(expr2);}
  	|	^('endswith'expr1=expr expr2=expr ) {$value=new MyMethodExpressionImpl(new MyInfoMethod(MethodOperator.ENDSWITH, "syntax",2,2,null)).appendParameter(expr1).appendParameter(expr2);}
	|	^('length' expr1=expr) {$value=new MyMethodExpressionImpl(new MyInfoMethod(MethodOperator.LENGTH, "syntax",null)).appendParameter(expr1);}
	|	^('indexof' expr1=expr expr2=expr) {$value=new MyMethodExpressionImpl(new MyInfoMethod(MethodOperator.INDEXOF, "syntax",2,2,null)).appendParameter(expr1).appendParameter(expr2);}
	|	^('substring' expr1=expr expr2=expr (expr3=expr)?) {
					if(expr3!=null){
						$value=new MyMethodExpressionImpl(new MyInfoMethod(MethodOperator.SUBSTRING, "syntax",2,3,null)).appendParameter(expr1).appendParameter(expr2).appendParameter(expr3);
					}else{
						$value=new MyMethodExpressionImpl(new MyInfoMethod(MethodOperator.SUBSTRING, "syntax",2,3,null)).appendParameter(expr1).appendParameter(expr2);
					}
				}
    | 	^('concat' expr1=expr expr2=expr) {$value=new MyMethodExpressionImpl(new MyInfoMethod(MethodOperator.CONCAT, "syntax",2,2,null)).appendParameter(expr1).appendParameter(expr2);}	
    |	^('tolower' expr1=expr){$value=new MyMethodExpressionImpl(new MyInfoMethod(MethodOperator.TOLOWER, "syntax",null)).appendParameter(expr1);}
	
	
	// ................ MemberExpression ................//
	|	^(MEMBER  property=expr (path=expr)? ){
						if(path!=null){
							$value= new MemberExpressionImpl(path,property);
						}else{
							$value=property;
						}
					}
	|	^(ENTITYTYPENAME  a=expr b=expr) {String qetypename=a.getUriLiteral()+"."+b.getUriLiteral(); $value = new LiteralExpressionImpl(qetypename,  new EdmLiteral(EdmString.getInstance(), qetypename)) ;}
	|	^(PROPERTY ODATA_ID) {$value=new PropertyExpressionImpl($ODATA_ID.text, null);}
	
	
	// ................ LiteralExpression ..............//
    |	INT {$value=new LiteralExpressionImpl($INT.text, new EdmLiteral(EdmString.getInstance(), $INT.text));}
    |	STRING{$value=new LiteralExpressionImpl($STRING.text, new EdmLiteral(EdmString.getInstance(), $STRING.text));}
    |	BOOLEAN{$value=new LiteralExpressionImpl($BOOLEAN.text, new EdmLiteral(EdmString.getInstance(), $BOOLEAN.text));}
  	|   ODATA_ID {$value=new LiteralExpressionImpl($ODATA_ID.text, new EdmLiteral(EdmString.getInstance(), $ODATA_ID.text));} 
	
	;

