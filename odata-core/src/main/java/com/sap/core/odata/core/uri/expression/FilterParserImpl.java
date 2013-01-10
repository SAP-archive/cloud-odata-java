package com.sap.core.odata.core.uri.expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.uri.expression.BinaryExpression;
import com.sap.core.odata.api.uri.expression.BinaryOperator;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.FilterParser;
import com.sap.core.odata.api.uri.expression.FilterParserException;
import com.sap.core.odata.api.uri.expression.LiteralExpression;
import com.sap.core.odata.api.uri.expression.MethodExpression;
import com.sap.core.odata.api.uri.expression.MethodOperator;
import com.sap.core.odata.api.uri.expression.UnaryExpression;
import com.sap.core.odata.api.uri.expression.UnaryOperator;
import com.sap.core.odata.core.edm.EdmSimpleTypeFacadeImpl;

/**
 * @author SAP AG
 */
public class FilterParserImpl implements FilterParser
{
  /*do the static initialization*/
  protected static Map<String, InfoBinaryOperator> availableBinaryOperators;
  protected static Map<String, InfoMethod> availableMethods;
  protected static Map<String, InfoUnaryOperator> availableUnaryOperators;

  static
  {
    initAvialTables();
  }

  /*instance attributes*/
  protected Edm edm = null;
  protected EdmEntityType resourceEntityType = null;
  protected TokenList tokenList = null;
  protected String curExpression;

  /**
   * Creates a new FilterParser implementation
   * @param edm EntityDataModel   
   * @param edmType EntityType of the resource on which the filter is applied
   */
  public FilterParserImpl(Edm edm, EdmEntityType edmType)
  {
    this.edm = edm;
    this.resourceEntityType = edmType;
  }

  public void addTestfunctions()
  {
    ParameterSetCombination combination = null;
    //create type helpers

    EdmSimpleType string = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.String);

    //TESTING
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(string, string, string).setFurtherType(string));
    availableMethods.put("testingMINMAX1", new InfoMethod(MethodOperator.CONCAT, "testingMINMAX1", -1, -1, combination));

    //TESTING
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(string, string, string).setFurtherType(string));
    availableMethods.put("testingMINMAX2", new InfoMethod(MethodOperator.CONCAT, "testingMINMAX2", 0, -1, combination));

    //TESTING
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(string, string, string).setFurtherType(string));
    availableMethods.put("testingMINMAX3", new InfoMethod(MethodOperator.CONCAT, "testingMINMAX3", 2, -1, combination));

    //TESTING
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(string, string, string).setFurtherType(string));
    availableMethods.put("testingMINMAX4", new InfoMethod(MethodOperator.CONCAT, "testingMINMAX4", -1, 0, combination));

    //TESTING
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(string, string, string).setFurtherType(string));
    availableMethods.put("testingMINMAX5", new InfoMethod(MethodOperator.CONCAT, "testingMINMAX5", -1, 2, combination));

    //TESTING
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(string, string, string).setFurtherType(string));
    availableMethods.put("testingMINMAX6", new InfoMethod(MethodOperator.CONCAT, "testingMINMAX6", 1, 2, combination));

    //TESTING
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(string, string, string).setFurtherType(string));
    availableMethods.put("testingMINMAX7", new InfoMethod(MethodOperator.CONCAT, "testingMINMAX7", 1, 1, combination));
  }

  @Override
  public FilterExpression parseFilterString(String filterExpression) throws FilterParserException, FilterParserInternalError
  {
    CommonExpression node = null;
    curExpression = filterExpression;
    try
    {
      // Throws TokenizerException and FilterParserException. FilterParserException is catch somewhere above 
      tokenList = new Tokenizer(filterExpression).tokenize();
      if (!tokenList.hasTokens())
      {
        return new FilterExpressionImpl(filterExpression);
      }
    } catch (TokenizerException tokenizerException)
    {
      // Tested with TestParserExceptions.TestPMparseFilterString
      throw FilterParserExceptionImpl.createERROR_IN_TOKENIZER(tokenizerException);
    }

    try
    {
      CommonExpression nodeLeft = readElement(null);
      node = readElements(nodeLeft, 0);
    } catch (FilterParserException filterParserException)
    {
      // Add empty filterTree to Exception
      // Tested for original throw point
      throw filterParserException.setFilterTree(new FilterExpressionImpl(filterExpression));
    }

    // Post check
    if (tokenList.tokenCount() > tokenList.currentToken) //this indicates that not all tokens have been read
    {
      // Tested with TestParserExceptions.TestPMparseFilterString
      throw FilterParserExceptionImpl.createINVALID_TRAILING_TOKEN_DETECTED_AFTER_PARSING(tokenList.elementAt(tokenList.currentToken), filterExpression);
    }

    // Create and return filterExpression node
    return new FilterExpressionImpl(filterExpression, node);
  }

  protected CommonExpression readElements(CommonExpression leftExpression, int priority) throws FilterParserException, FilterParserInternalError
  {
    CommonExpression leftNode = leftExpression;
    CommonExpression rightNode;
    BinaryExpression binaryNode;

    ActualBinaryOperator operator = readBinaryOperator();
    ActualBinaryOperator nextOperator;

    while ((operator != null) && (operator.getOP().getPriority() >= priority))
    {
      tokenList.next(); //eat the operator
      rightNode = readElement(leftNode); //throws FilterParserException, FilterParserInternalError
      nextOperator = readBinaryOperator();

      // It must be "while" because for example in "Filter=a or c eq d and e eq f"
      // after reading the "eq" operator the "and" operator must be consumed too. This is due to the fact that "and" has a higher priority than "or" 
      while ((nextOperator != null) && (nextOperator.getOP().getPriority() > operator.getOP().getPriority()))
      {
        //recurse until the a binary operator with a lower priority is detected 
        rightNode = readElements(rightNode, nextOperator.getOP().getPriority());
        nextOperator = readBinaryOperator();
      }

      // Although the member operator is also a binary operator, there is some special handling in the filterTree
      if (operator.getOP().getOperator() == BinaryOperator.PROPERTY_ACCESS)
      {
        binaryNode = new MemberExpressionImpl(leftNode, rightNode);
      }
      else
      {
        binaryNode = new BinaryExpressionImpl(operator.getOP(), leftNode, rightNode, operator.getToken());
      }

      try
      {
        validateBinaryOperator(binaryNode);
      } catch (FilterParserException expressionException)
      {
        // Extend the error information
        // Tested for original throw point
        throw expressionException.setFilterTree(binaryNode);
      }

      leftNode = binaryNode;
      operator = readBinaryOperator();
    }

    return leftNode;
  }

  /**
   * Reads the content between parenthesis. Its is expected that the current token is of kind {@link TokenKind#OPENPAREN}
   * because it MUST be check in the calling method ( when read the method name and the '(' is read).  
   * @return An expression which reflects the content within the parenthesis
   * @throws FilterParserException
   *   While reading the elements in the parenthesis an error occurred
   * @throws TokenizerMessage 
   *   The next token did not match the expected token
   */
  protected CommonExpression readParenthesis() throws FilterParserException, FilterParserInternalError
  {
    // The existing of a '(' is verified BEFORE this method is called --> so it's a internal error
    Token openParenthesis = tokenList.expectToken(TokenKind.OPENPAREN, true);

    CommonExpression firstExpression = readElement(null);
    CommonExpression parenthesisExpression = readElements(firstExpression, 0);

    // check for ')'
    try {
      tokenList.expectToken(TokenKind.CLOSEPAREN); //TokenizerMessage
    } catch (TokenizerExpectError e)
    {
      // Internal parsing error, even if there are no more token (then there should be a different exception).
      // Tested with TestParserExceptions.TestPMreadParenthesis
      throw FilterParserExceptionImpl.createMISSING_CLOSING_PHARENTHESIS(openParenthesis.getPosition(), curExpression, e);
    }
    return parenthesisExpression;
  }

  /**
   * Read the parameters of a method expression
   * @param methodInfo
   *   Signature information about the method whose parameters should be read
   * @param methodExpression
   *   Method expression to which the read parameters are added 
   * @return
   *   The method expression input parameter 
   * @throws FilterParserException
   * @throws FilterParserInternalError 
   * @throws TokenizerExpectError 
   *   The next token did not match the expected token
   */
  protected MethodExpression readParameters(InfoMethod methodInfo, MethodExpressionImpl methodExpression, Token methodToken) throws FilterParserException, FilterParserInternalError
  {
    CommonExpression expression;
    boolean expectAnotherExpression = false;

    // The existing of a '(' is verified BEFORE this method is called --> so it's a internal error
    Token openParenthesis = tokenList.expectToken(TokenKind.OPENPAREN, true); //throws FilterParserInternalError

    Token token = tokenList.lookToken();
    if (token == null)
    {
      //Tested with TestParserExceptions.TestPMreadParameters CASE 1 e.g. "$filter=concat("
      throw FilterParserExceptionImpl.createEXPRESSION_EXPECTED_AFTER_POS(openParenthesis);
    }

    while (token.getKind() != TokenKind.CLOSEPAREN)
    {
      expression = readElement(null);
      if (expression != null) expression = readElements(expression, 0);

      if ((expression == null) && (expectAnotherExpression == true))
      {
        //Tested with TestParserExceptions.TestPMreadParameters CASE 4 e.g. "$filter=concat(,"
        throw FilterParserExceptionImpl.createEXPRESSION_EXPECTED_AFTER_POS(token);
      }
      else if (expression != null) //parameter list may be empty
      {
        methodExpression.appendParameter(expression);
      }

      token = tokenList.lookToken();
      if (token == null)
      {
        //Tested with TestParserExceptions.TestPMreadParameters CASE 2 e.g. "$filter=concat(123"
        throw FilterParserExceptionImpl.createCOMMA_OR_CLOSING_PHARENTHESIS_EXPECTED_AFTER_POS(tokenList.lookPrevToken());
      }

      if (token.getKind() == TokenKind.COMMA)
      {
        expectAnotherExpression = true;
        if (expression == null)
        {
          //Tested with TestParserExceptions.TestPMreadParameters CASE 3 e.g. "$filter=concat(,"
          throw FilterParserExceptionImpl.createEXPRESSION_EXPECTED_AT_POS(token);
        }

        tokenList.expectToken(Character.toString(CharConst.GC_STR_COMMA), true);
        /*
        //eat the ','
        try {
          tokenList.expectToken(Character.toString(CharConst.GC_STR_COMMA));
        } catch (TokenizerExpectError e)
        {
          throw FilterParserInternalError.createERROR_PARSING_PARENTHESIS(e);
        }*/
      }
    }

    // because the while loop above only exits if a ')' has been found it is an  
    // internal error if there is not ')'
    tokenList.expectToken(TokenKind.CLOSEPAREN, true);

    //---check parameter count
    int count = methodExpression.getParameters().size();
    if ((methodInfo.getMinParameter() > -1) && (count < methodInfo.getMinParameter()))
    {
      //Tested with TestParserExceptions.TestPMreadParameters CASE 12
      throw FilterParserExceptionImpl.createMETHOD_WRONG_ARG_COUNT(curExpression, methodExpression, methodToken);
    }

    if ((methodInfo.getMaxParameter() > -1) && (count > methodInfo.getMaxParameter()))
    {
      //Tested with TestParserExceptions.TestPMreadParameters CASE 15
      throw FilterParserExceptionImpl.createMETHOD_WRONG_ARG_COUNT(curExpression, methodExpression, methodToken);
    }

    return methodExpression;
  }

  /**
   * Reads: Unary operators, Methods, Properties, ...
   * but not binary operators which are handelt in {@link #readElements(CommonExpression, int)}
   * @param leftExpression 
   *   Used while parsing properties. In this case ( e.g. parsing "a/b") the property "a" ( as leftExpression of "/") is relevant 
   *   to verify whether the property "b" exists inside the edm
   * @return
   * @throws FilterParserException
   * @throws FilterParserInternalError 
   * @throws TokenizerMessage 
   */
  protected CommonExpression readElement(CommonExpression leftExpression) throws FilterParserException, FilterParserInternalError
  {
    CommonExpression node = null;
    Token token;
    Token lookToken;
    lookToken = tokenList.lookToken();
    if (lookToken == null)
      return null;

    switch (lookToken.getKind())
    {
    case OPENPAREN:
      node = readParenthesis();
      return node;
    case CLOSEPAREN: // ')'  finishes a parenthesis (it is no extra token)" +
    case COMMA: //. " ','  is a separator for function parameters (it is no extra token)" +
      return null;
    }

    //-->Check if the token is a unary operator
    InfoUnaryOperator unaryOperator = isUnaryOperator(lookToken);
    if (unaryOperator != null)
    {
      return readUnaryoperator(lookToken, unaryOperator);
    }

    //---expect the look ahead token
    try {
      token = tokenList.expectToken(lookToken.getUriLiteral());
    } catch (TokenizerExpectError e)
    {
      //Internal parsing error, even if there are no more token (then there should be a different exception).
      throw FilterParserInternalError.createCOMMON(e);
    }
    lookToken = tokenList.lookToken();

    //-->Check if the token is a method 
    //To avoid name clashes between method names and property names we accept here only method names if a "(" follows.
    //Hence the parser accepts a property named "concat"
    InfoMethod methodOperator = isMethod(token, lookToken);
    if (methodOperator != null)
    {
      return readMethod(token, methodOperator);
    }

    //-->Check if token is a terminal 
    //is a terminal e.g. a Value like an EDM.String 'hugo' or  125L or 1.25D" 
    if (token.getKind() == TokenKind.SIMPLE_TYPE)
    {
      LiteralExpression literal = new LiteralExpressionImpl(token.getUriLiteral(), token.getJavaLiteral());
      return literal;
    }

    //-->Check if token is a property, e.g. "name" or "address"
    if (token.getKind() == TokenKind.LITERAL)
    {
      PropertyExpressionImpl property = new PropertyExpressionImpl(token.getUriLiteral(), token.getJavaLiteral());
      validateEdmProperty(leftExpression, property);
      return property;
    }

    throw FilterParserInternalError.createCOMMON();
  }

  protected CommonExpression readUnaryoperator(Token lookToken, InfoUnaryOperator unaryOperator) throws FilterParserException, FilterParserInternalError
  {
    //---read token
    try {
      tokenList.expectToken(lookToken.getUriLiteral());
    } catch (TokenizerExpectError e) {

      throw FilterParserInternalError.createERROR_PARSING_PARENTHESIS(e);
    }

    CommonExpression operand = readElement(null);
    UnaryExpression unaryExpression = new UnaryExpressionImpl(unaryOperator, operand);
    validateUnaryOperator(unaryExpression); //throws ExpressionInvalidOperatorTypeException
    return unaryExpression;
  }

  protected CommonExpression readMethod(Token token, InfoMethod methodOperator) throws FilterParserException, FilterParserInternalError
  {
    MethodExpressionImpl method = new MethodExpressionImpl(methodOperator);

    readParameters(methodOperator, method, token);
    validateMethodTypes(method); //throws ExpressionInvalidOperatorTypeException

    return method;
  }

  protected ActualBinaryOperator readBinaryOperator()
  {
    InfoBinaryOperator operator = null;
    Token token = tokenList.lookToken();
    if (token == null)
      return null;
    if ((token.getKind() == TokenKind.SYMBOL) && (token.getUriLiteral().equals("/")))
    {
      operator = availableBinaryOperators.get(token.getUriLiteral());
    }
    else if (token.getKind() == TokenKind.LITERAL)
    {
      operator = availableBinaryOperators.get(token.getUriLiteral());
    }

    if (operator == null)
      return null;
    return new ActualBinaryOperator(operator, token);
  }

  /**
   * Check if a token is a UnaryOperator ( e.g. "not" or "-" ) 
   * 
   * @param token Token to be checked
   *   
   * @return
   *   <li>An instance of {@link InfoUnaryOperator} containing information about the specific unary operator</li> 
   *   <li><code>null</code> if the token is not an unary operator</li>
   */
  protected InfoUnaryOperator isUnaryOperator(Token token)
  {
    if ((token.getKind() == TokenKind.LITERAL) || (token.getKind() == TokenKind.SYMBOL))
    {
      InfoUnaryOperator operator = availableUnaryOperators.get(token.getUriLiteral());
      return operator;
    }
    return null;
  }

  protected InfoMethod isMethod(Token token, Token lookToken)
  {
    if ((lookToken != null) && (lookToken.getKind() == TokenKind.OPENPAREN))
    {
      return availableMethods.get(token.getUriLiteral());
    }
    return null;
  }

  static void initAvialTables()
  {

    availableBinaryOperators = new HashMap<String, InfoBinaryOperator>();
    availableMethods = new HashMap<String, InfoMethod>();
    availableUnaryOperators = new HashMap<String, InfoUnaryOperator>();

    //create type validators
    //InputTypeValidator typeValidatorPromotion = new InputTypeValidator.TypePromotionValidator();
    ParameterSetCombination combination = null;
    //create type helpers
    EdmSimpleType boolean_ = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.Boolean);
    EdmSimpleType sbyte = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.SByte);
    EdmSimpleType byte_ = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.Byte);
    EdmSimpleType int16 = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.Int16);
    EdmSimpleType int32 = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.Int32);
    EdmSimpleType int64 = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.Int64);
    EdmSimpleType single = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.Single);
    EdmSimpleType double_ = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.Double);
    EdmSimpleType decimal = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.Decimal);
    EdmSimpleType string = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.String);
    EdmSimpleType time = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.Time);
    EdmSimpleType datetime = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.DateTime);
    EdmSimpleType datetimeoffset = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.DateTimeOffset);
    EdmSimpleType guid = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.Guid);
    EdmSimpleType binary = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.Binary);

    //---Memeber member access---
    availableBinaryOperators.put("/",
        new InfoBinaryOperator(BinaryOperator.PROPERTY_ACCESS, "Primary", "/", 100, new ParameterSetCombination.PSCReturnTypeEqLastParameter()));//todo fix this

    //---Multiplicative---
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(sbyte, sbyte, sbyte));
    combination.add(new ParameterSet(byte_, byte_, byte_));
    combination.add(new ParameterSet(int16, int16, int16));
    combination.add(new ParameterSet(int32, int32, int32));
    combination.add(new ParameterSet(int64, int64, int64));
    combination.add(new ParameterSet(single, single, single));
    combination.add(new ParameterSet(double_, double_, double_));

    combination.add(new ParameterSet(decimal, decimal, decimal));

    availableBinaryOperators.put(CharConst.GC_OPERATOR_MUL,
        new InfoBinaryOperator(BinaryOperator.MUL, "Multiplicative", CharConst.GC_OPERATOR_MUL, 60, combination));
    availableBinaryOperators.put(CharConst.GC_OPERATOR_DIV,
        new InfoBinaryOperator(BinaryOperator.DIV, "Multiplicative", CharConst.GC_OPERATOR_DIV, 60, combination));
    availableBinaryOperators.put(CharConst.GC_OPERATOR_MOD,
        new InfoBinaryOperator(BinaryOperator.MODULO, "Multiplicative", CharConst.GC_OPERATOR_MOD, 60, combination));

    //---Additive---
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(sbyte, sbyte, sbyte));
    combination.add(new ParameterSet(byte_, byte_, byte_));
    combination.add(new ParameterSet(int16, int16, int16));
    combination.add(new ParameterSet(int32, int32, int32));
    combination.add(new ParameterSet(int64, int64, int64));
    combination.add(new ParameterSet(single, single, single));
    combination.add(new ParameterSet(double_, double_, double_));
    combination.add(new ParameterSet(decimal, decimal, decimal));

    availableBinaryOperators.put(CharConst.GC_OPERATOR_ADD,
        new InfoBinaryOperator(BinaryOperator.ADD, "Additive", CharConst.GC_OPERATOR_ADD, 50, combination));
    availableBinaryOperators.put(CharConst.GC_OPERATOR_SUB,
        new InfoBinaryOperator(BinaryOperator.SUB, "Additive", CharConst.GC_OPERATOR_SUB, 50, combination));

    //---Relational---
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(boolean_, string, string));
    combination.add(new ParameterSet(boolean_, time, time));
    combination.add(new ParameterSet(boolean_, datetime, datetime));
    combination.add(new ParameterSet(boolean_, datetimeoffset, datetimeoffset));
    combination.add(new ParameterSet(boolean_, guid, guid));
    combination.add(new ParameterSet(boolean_, sbyte, sbyte));
    combination.add(new ParameterSet(boolean_, byte_, byte_));
    combination.add(new ParameterSet(boolean_, int16, int16));
    combination.add(new ParameterSet(boolean_, int32, int32));
    combination.add(new ParameterSet(boolean_, int64, int64));
    combination.add(new ParameterSet(boolean_, single, single));
    combination.add(new ParameterSet(boolean_, double_, double_));
    combination.add(new ParameterSet(boolean_, decimal, decimal));
    combination.add(new ParameterSet(boolean_, binary, binary));

    availableBinaryOperators.put(CharConst.GC_OPERATOR_LT,
        new InfoBinaryOperator(BinaryOperator.LT, "Relational", CharConst.GC_OPERATOR_LT, 40, combination));
    availableBinaryOperators.put(CharConst.GC_OPERATOR_GT,
        new InfoBinaryOperator(BinaryOperator.GT, "Relational", CharConst.GC_OPERATOR_GT, 40, combination));
    availableBinaryOperators.put(CharConst.GC_OPERATOR_GE,
        new InfoBinaryOperator(BinaryOperator.GE, "Relational", CharConst.GC_OPERATOR_GE, 40, combination));
    availableBinaryOperators.put(CharConst.GC_OPERATOR_LE,
        new InfoBinaryOperator(BinaryOperator.LE, "Relational", CharConst.GC_OPERATOR_LE, 40, combination));

    //---Equality---
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(boolean_, boolean_, boolean_));
    combination.add(new ParameterSet(boolean_, string, string));
    combination.add(new ParameterSet(boolean_, time, time));
    combination.add(new ParameterSet(boolean_, datetime, datetime));
    combination.add(new ParameterSet(boolean_, datetimeoffset, datetimeoffset));
    combination.add(new ParameterSet(boolean_, guid, guid));
    combination.add(new ParameterSet(boolean_, sbyte, sbyte));
    combination.add(new ParameterSet(boolean_, byte_, byte_));
    combination.add(new ParameterSet(boolean_, int16, int16));
    combination.add(new ParameterSet(boolean_, int32, int32));
    combination.add(new ParameterSet(boolean_, int64, int64));
    combination.add(new ParameterSet(boolean_, single, single));
    combination.add(new ParameterSet(boolean_, double_, double_));
    combination.add(new ParameterSet(boolean_, decimal, decimal));
    combination.add(new ParameterSet(boolean_, binary, binary));

    availableBinaryOperators.put(CharConst.GC_OPERATOR_EQ,
        new InfoBinaryOperator(BinaryOperator.EQ, "Equality", CharConst.GC_OPERATOR_EQ, 30, combination));
    availableBinaryOperators.put(CharConst.GC_OPERATOR_NE,
        new InfoBinaryOperator(BinaryOperator.NE, "Equality", CharConst.GC_OPERATOR_NE, 30, combination));

    //"---Conditinal AND---
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(boolean_, boolean_, boolean_));

    availableBinaryOperators.put(CharConst.GC_OPERATOR_AND,
        new InfoBinaryOperator(BinaryOperator.AND, "Conditinal", CharConst.GC_OPERATOR_AND, 20, combination));

    //---Conditinal OR---
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(boolean_, boolean_, boolean_));

    availableBinaryOperators.put(CharConst.GC_OPERATOR_OR,
        new InfoBinaryOperator(BinaryOperator.OR, "Conditinal", CharConst.GC_OPERATOR_OR, 10, combination));

    //endswith
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(boolean_, string, string));
    availableMethods.put(CharConst.GC_FUNCTION_ENDSWITH,
        new InfoMethod(MethodOperator.ENDSWITH, CharConst.GC_FUNCTION_ENDSWITH, 2, 2, combination));

    //indexof
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(int32, string, string));
    availableMethods.put(CharConst.GC_FUNCTION_INDEXOF, new InfoMethod(MethodOperator.INDEXOF, CharConst.GC_FUNCTION_INDEXOF, 2, 2, combination));

    //startswith
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(boolean_, string, string));
    availableMethods.put(CharConst.GC_FUNCTION_STARTSWITH, new InfoMethod(MethodOperator.STARTSWITH, CharConst.GC_FUNCTION_STARTSWITH, 2, 2, combination));

    //tolower
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(string, string));
    availableMethods.put(CharConst.GC_FUNCTION_TOLOWER, new InfoMethod(MethodOperator.TOLOWER, CharConst.GC_FUNCTION_TOLOWER, combination));

    //toupper
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(string, string));
    availableMethods.put(CharConst.GC_FUNCTION_TOUPPER, new InfoMethod(MethodOperator.TOUPPER, CharConst.GC_FUNCTION_TOUPPER, combination));

    //trim
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(string, string));
    availableMethods.put(CharConst.GC_FUNCTION_TRIM, new InfoMethod(MethodOperator.TRIM, CharConst.GC_FUNCTION_TRIM, combination));

    //substring
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(string, string, int32));
    combination.add(new ParameterSet(string, string, int32, int32));
    availableMethods.put(CharConst.GC_FUNCTION_SUBSTRING, new InfoMethod(MethodOperator.SUBSTRING, CharConst.GC_FUNCTION_SUBSTRING, 1, -1, combination));

    //substringof
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(boolean_, string, string));
    availableMethods.put(CharConst.GC_FUNCTION_SUBSTRINGOF, new InfoMethod(MethodOperator.SUBSTRINGOF, CharConst.GC_FUNCTION_SUBSTRINGOF, 1, -1, combination));

    //concat
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(string, string, string).setFurtherType(string));
    availableMethods.put(CharConst.GC_FUNCTION_CONCAT, new InfoMethod(MethodOperator.CONCAT, CharConst.GC_FUNCTION_CONCAT, 2, -1, combination));

    //length
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(int32, string));
    availableMethods.put(CharConst.GC_FUNCTION_LENGTH, new InfoMethod(MethodOperator.LENGTH, CharConst.GC_FUNCTION_LENGTH, combination));

    //year
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(int32, datetime));
    availableMethods.put(CharConst.GC_FUNCTION_YEAR, new InfoMethod(MethodOperator.YEAR, CharConst.GC_FUNCTION_YEAR, combination));

    //month
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(int32, datetime));
    availableMethods.put(CharConst.GC_FUNCTION_MONTH, new InfoMethod(MethodOperator.MONTH, CharConst.GC_FUNCTION_MONTH, combination));

    //day
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(int32, datetime));
    availableMethods.put(CharConst.GC_FUNCTION_DAY, new InfoMethod(MethodOperator.DAY, CharConst.GC_FUNCTION_DAY, combination));

    //hour
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(int32, datetime));
    combination.add(new ParameterSet(int32, time));
    combination.add(new ParameterSet(int32, datetimeoffset));
    availableMethods.put(CharConst.GC_FUNCTION_HOUR, new InfoMethod(MethodOperator.HOUR, CharConst.GC_FUNCTION_HOUR, combination));

    //minute
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(int32, datetime));
    combination.add(new ParameterSet(int32, time));
    combination.add(new ParameterSet(int32, datetimeoffset));
    availableMethods.put(CharConst.GC_FUNCTION_MINUTE, new InfoMethod(MethodOperator.MINUTE, CharConst.GC_FUNCTION_MINUTE, combination));

    //second
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(int32, datetime));
    combination.add(new ParameterSet(int32, time));
    combination.add(new ParameterSet(int32, datetimeoffset));
    availableMethods.put(CharConst.GC_FUNCTION_SECOND, new InfoMethod(MethodOperator.SECOND, CharConst.GC_FUNCTION_SECOND, combination));

    //round
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(decimal, decimal));
    combination.add(new ParameterSet(double_, double_));
    availableMethods.put(CharConst.GC_FUNCTION_ROUND, new InfoMethod(MethodOperator.ROUND, CharConst.GC_FUNCTION_ROUND, combination));

    //ceiling
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(decimal, decimal));
    combination.add(new ParameterSet(double_, double_));
    availableMethods.put(CharConst.GC_FUNCTION_CEILING, new InfoMethod(MethodOperator.CEILING, CharConst.GC_FUNCTION_CEILING, combination));

    //floor
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(decimal, decimal));
    combination.add(new ParameterSet(double_, double_));
    availableMethods.put(CharConst.GC_FUNCTION_FLOOR, new InfoMethod(MethodOperator.FLOOR, CharConst.GC_FUNCTION_FLOOR, combination));

    //---unary---

    //minus
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(sbyte, sbyte));
    combination.add(new ParameterSet(byte_, byte_));
    combination.add(new ParameterSet(int16, int16));
    combination.add(new ParameterSet(int32, int32));
    combination.add(new ParameterSet(int64, int64));
    combination.add(new ParameterSet(single, single));
    combination.add(new ParameterSet(double_, double_));
    combination.add(new ParameterSet(decimal, decimal));

    //minus
    availableUnaryOperators.put(CharConst.GC_OPERATOR_MINUS, new InfoUnaryOperator(UnaryOperator.MINUS, "minus", CharConst.GC_OPERATOR_MINUS, combination));
    //not
    availableUnaryOperators.put(CharConst.GC_OPERATOR_NOT, new InfoUnaryOperator(UnaryOperator.NOT, "not", CharConst.GC_OPERATOR_NOT, combination));

  }

  protected void validateEdmProperty(CommonExpression leftExpression, PropertyExpressionImpl property) throws FilterParserException, FilterParserInternalError {

    // Exist if no edm provided
    if ((this.edm == null) || (this.resourceEntityType == null))
      return;

    if (leftExpression == null)
    {
      //e.g. "$filter='Hong Kong' eq city" --> "city" is checked against the resource entity type of the last URL segment 
      validateEdmPropertyOfEntityType(property, this.resourceEntityType);
      return;
    }

    //e.g. "$filter='Hong Kong' eq address/city" --> city is "checked" against the type of the property "address".
    //     "address" itself must be a (navigation)property of the resource entity type of the last URL segment AND
    //     "address" must have a structural edm type
    EdmType parentType = leftExpression.getEdmType(); //parentType point now to the type of property "address"

    if (parentType instanceof EdmEntityType)
    {
      //e.g. "$filter='Hong Kong' eq navigationProp/city" --> "navigationProp" is a navigation property with a entity type
      validateEdmPropertyOfEntityType(property, (EdmEntityType) parentType);
    }
    else if (parentType instanceof EdmComplexType)
    {
      //e.g. "$filter='Hong Kong' eq address/city" --> "address" is a property with a complex type 
      validateEdmPropertyOfComplexType(property, (EdmComplexType) parentType);
    }
    else
    {
      //e.g. "$filter='Hong Kong' eq name/city" --> "name is of type String" 
      throw FilterParserExceptionImpl.createLEFT_SIDE_NOT_STRUCTURAL_TYPE();
    }

    return;
  }

  /*
  private Object getSave(Object object, Class class1) throws FilterParserInternalError {
    if (object.getClass() == class1)
    {
      return  object;
    }
    throw FilterParserInternalError.createERROR_ACCESSING_EDM();
  }
  */

  protected void validateEdmPropertyOfComplexType(PropertyExpressionImpl property, EdmComplexType parentType) throws FilterParserException, FilterParserInternalError
  {
    try {
      String propertyName = property.getUriLiteral();
      EdmTyped edmProperty = parentType.getProperty(propertyName);

      if (edmProperty != null)
      {
        property.setEdmProperty(edmProperty);
        property.setEdmType(edmProperty.getType());
      }
      else
      {
        throw FilterParserExceptionImpl.createPROPERTY_NAME_NOT_FOUND_IN_TYPE(parentType, property);
      }

    } catch (EdmException e) {
      throw FilterParserInternalError.createERROR_ACCESSING_EDM(e);
    }
  }

  protected void validateEdmPropertyOfEntityType(PropertyExpressionImpl property, EdmEntityType parentType) throws FilterParserException, FilterParserInternalError
  {
    try {
      String propertyName = property.getUriLiteral();
      EdmTyped edmProperty = parentType.getProperty(propertyName);

      if (edmProperty != null)
      {
        property.setEdmProperty(edmProperty);
        property.setEdmType(edmProperty.getType());
      }
      else
      {
        throw FilterParserExceptionImpl.createPROPERTY_NAME_NOT_FOUND_IN_TYPE(parentType, property);
      }

    } catch (EdmException e) {
      throw FilterParserInternalError.createERROR_ACCESSING_EDM(e);
    }
  }

  protected void validateUnaryOperator(UnaryExpression unaryExpression) throws FilterParserInternalError
  {
    InfoUnaryOperator unOpt = availableUnaryOperators.get(unaryExpression.getOperator().toUriLiteral());

    //List<ParameterSet> allowedParameterTypesCombinations = binOpt.getParameterSet();
    List<EdmType> actualParameterTypes = new ArrayList<EdmType>();
    actualParameterTypes.add(unaryExpression.getOperand().getEdmType());

    EdmType edmType = unOpt.validateParameterSet(actualParameterTypes);
    unaryExpression.setEdmType(edmType);

  }

  protected void validateBinaryOperator(BinaryExpression binaryExpression) throws FilterParserException, FilterParserInternalError
  {
    InfoBinaryOperator binOpt = availableBinaryOperators.get(binaryExpression.getOperator().toUriLiteral());

    //List<ParameterSet> allowedParameterTypesCombinations = binOpt.getParameterSet();
    List<EdmType> actualParameterTypes = new ArrayList<EdmType>();
    EdmType operand = binaryExpression.getLeftOperand().getEdmType();
    if (operand == null) return;
    actualParameterTypes.add(operand);

    operand = binaryExpression.getRightOperand().getEdmType();
    if (operand == null) return;
    actualParameterTypes.add(operand);

    EdmType edmType = binOpt.validateParameterSet(actualParameterTypes);
    if (edmType == null)
    {
      BinaryExpressionImpl binaryExpressionImpl = (BinaryExpressionImpl) binaryExpression;

      // Tested with TestParserExceptions.TestPMvalidateBinaryOperator
      throw FilterParserExceptionImpl.createINVALID_TYPES_FOR_BINARY_OPERATOR(
          binaryExpressionImpl.getToken().getPosition(), curExpression,
          binaryExpression.getOperator(),
          binaryExpression.getLeftOperand().getEdmType(),
          binaryExpression.getLeftOperand().getEdmType()
          );

    }

    binaryExpression.setEdmType(edmType);
  }

  protected void validateMethodTypes(MethodExpression methodExpression) throws FilterParserInternalError
  {
    InfoMethod methOpt = availableMethods.get(methodExpression.getMethod().toUriLiteral());

    List<EdmType> actualParameterTypes = new ArrayList<EdmType>();

    for (CommonExpression parameter : methodExpression.getParameters())
    {
      actualParameterTypes.add(parameter.getEdmType());
    }

    EdmType edmType = methOpt.validateParameterSet(actualParameterTypes);
    methodExpression.setEdmType(edmType);
  }
}
