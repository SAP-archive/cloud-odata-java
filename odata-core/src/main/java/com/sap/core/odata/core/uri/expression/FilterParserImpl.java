package com.sap.core.odata.core.uri.expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmStructuralType;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.uri.expression.BinaryExpression;
import com.sap.core.odata.api.uri.expression.BinaryOperator;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.ExpressionParserException;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.LiteralExpression;
import com.sap.core.odata.api.uri.expression.MethodExpression;
import com.sap.core.odata.api.uri.expression.MethodOperator;
import com.sap.core.odata.api.uri.expression.UnaryExpression;
import com.sap.core.odata.api.uri.expression.UnaryOperator;
import com.sap.core.odata.core.edm.EdmBoolean;
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
    initAvailTables();
  }

  /*instance attributes*/
  protected EdmEntityType resourceEntityType = null;
  protected TokenList tokenList = null;
  protected String curExpression;

  /**
   * Creates a new FilterParser implementation
   * @param resourceEntityType EntityType of the resource on which the filter is applied
   */
  public FilterParserImpl(final EdmEntityType resourceEntityType)
  {
    this.resourceEntityType = resourceEntityType;
  }

  @Override
  public FilterExpression parseFilterString(final String filterExpression) throws ExpressionParserException, ExpressionParserInternalError
  {
    return parseFilterString(filterExpression, false);
  }

  public FilterExpression parseFilterString(final String filterExpression, final boolean allowOnlyBinary) throws ExpressionParserException, ExpressionParserInternalError
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
      throw FilterParserExceptionImpl.createERROR_IN_TOKENIZER(tokenizerException, curExpression);
    }

    try
    {
      CommonExpression nodeLeft = readElement(null);
      node = readElements(nodeLeft, 0);
    } catch (ExpressionParserException filterParserException)
    {
      // Add empty filterTree to Exception
      // Tested for original throw point
      filterParserException.setFilterTree(new FilterExpressionImpl(filterExpression));
      throw filterParserException;
    }

    // Post check
    if (tokenList.tokenCount() > tokenList.currentToken) //this indicates that not all tokens have been read
    {
      // Tested with TestParserExceptions.TestPMparseFilterString
      throw FilterParserExceptionImpl.createINVALID_TRAILING_TOKEN_DETECTED_AFTER_PARSING(tokenList.elementAt(tokenList.currentToken), filterExpression);
    }

    // Create and return filterExpression node
    if ((allowOnlyBinary == true) && (node.getEdmType() != null) && (node.getEdmType() != EdmSimpleTypeKind.Boolean.getEdmSimpleTypeInstance()))
    {
      // Tested with TestParserExceptions.testAdditionalStuff CASE 9
      throw FilterParserExceptionImpl.createTYPE_EXPECTED_AT(EdmBoolean.getInstance(), node.getEdmType(), 1, curExpression);
    }

    return new FilterExpressionImpl(filterExpression, node);
  }

  protected CommonExpression readElements(final CommonExpression leftExpression, final int priority) throws ExpressionParserException, ExpressionParserInternalError
  {
    CommonExpression leftNode = leftExpression;
    CommonExpression rightNode;
    BinaryExpression binaryNode;

    ActualBinaryOperator operator = readBinaryOperator();
    ActualBinaryOperator nextOperator;

    while ((operator != null) && (operator.getOP().getPriority() >= priority))
    {
      tokenList.next(); //eat the operator
      rightNode = readElement(leftNode, operator); //throws FilterParserException, FilterParserInternalError
      if (rightNode == null)
      {
    	  // Tested with TestParserExceptions.testAdditionalStuff CASE 10
    	  throw FilterParserExceptionImpl.createEXPRESSION_EXPECTED_AFTER_POS(operator.getToken().getPosition()+ operator.getToken().getUriLiteral().length(), curExpression);
      }
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
        validateBinaryOperatorTypes(binaryNode);
      } catch (ExpressionParserException expressionException)
      {
        // Extend the error information
        // Tested for original throw point
        expressionException.setFilterTree(binaryNode);
        throw expressionException;
      }

      leftNode = binaryNode;
      operator = readBinaryOperator();
    }

    //Add special handling for expressions like $filter=notsupportedfunction('a')
    //If this special handling is not in place the error text would be 
    //-->Invalid token "(" detected after parsing at position 21 in "notsupportedfunction('a')".
    //with this special handling we ensure that the error text would be

    Token token = tokenList.lookToken();
    if (token != null)
    {
      if ((leftNode.getKind() == ExpressionKind.PROPERTY) && (tokenList.lookToken().getKind() == TokenKind.OPENPAREN))
      {
        // Tested with TestParserExceptions.testAdditionalStuff CASE 2
        throw FilterParserExceptionImpl.createINVALID_METHOD_CALL(leftNode, tokenList.lookPrevToken(), curExpression);
      }
    }

    return leftNode;
  }

  /**
   * Reads the content between parenthesis. Its is expected that the current token is of kind {@link TokenKind#OPENPAREN}
   * because it MUST be check in the calling method ( when read the method name and the '(' is read).  
   * @return An expression which reflects the content within the parenthesis
   * @throws ExpressionParserException
   *   While reading the elements in the parenthesis an error occurred
   * @throws TokenizerMessage 
   *   The next token did not match the expected token
   */
  protected CommonExpression readParenthesis() throws ExpressionParserException, ExpressionParserInternalError
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
   * @throws ExpressionParserException
   * @throws ExpressionParserInternalError 
   * @throws TokenizerExpectError 
   *   The next token did not match the expected token
   */
  protected MethodExpression readParameters(final InfoMethod methodInfo, final MethodExpressionImpl methodExpression, final Token methodToken) throws ExpressionParserException, ExpressionParserInternalError
  {
    CommonExpression expression;
    boolean expectAnotherExpression = false;
    boolean readComma = true;

    // The existing of a '(' is verified BEFORE this method is called --> so it's a internal error
    Token openParenthesis = tokenList.expectToken(TokenKind.OPENPAREN, true); //throws FilterParserInternalError

    Token token = tokenList.lookToken();
    if (token == null)
    {
      //Tested with TestParserExceptions.TestPMreadParameters CASE 1 e.g. "$filter=concat("
      throw FilterParserExceptionImpl.createEXPRESSION_EXPECTED_AFTER_POS(openParenthesis, curExpression);
    }

    while (token.getKind() != TokenKind.CLOSEPAREN)
    {
      if (readComma == false)
      {
        //Tested with TestParserExceptions.TestPMreadParameters CASE 12 e.g. "$filter=concat('a' 'b')"
        throw FilterParserExceptionImpl.createCOMMA_OR_CLOSING_PHARENTHESIS_EXPECTED_AFTER_POS(tokenList.lookPrevToken(), curExpression);
      }
      expression = readElement(null);
      if (expression != null) {
        expression = readElements(expression, 0);
      }

      if ((expression == null) && (expectAnotherExpression == true))
      {
        //Tested with TestParserExceptions.TestPMreadParameters CASE 4 e.g. "$filter=concat(,"
        throw FilterParserExceptionImpl.createEXPRESSION_EXPECTED_AFTER_POS(token, curExpression);
      }
      else if (expression != null) //parameter list may be empty
      {
        methodExpression.appendParameter(expression);
      }

      token = tokenList.lookToken();
      if (token == null)
      {
        //Tested with TestParserExceptions.TestPMreadParameters CASE 2 e.g. "$filter=concat(123"
        throw FilterParserExceptionImpl.createCOMMA_OR_CLOSING_PHARENTHESIS_EXPECTED_AFTER_POS(tokenList.lookPrevToken(), curExpression);
      }

      if (token.getKind() == TokenKind.COMMA)
      {
        expectAnotherExpression = true;
        if (expression == null)
        {
          //Tested with TestParserExceptions.TestPMreadParameters CASE 3 e.g. "$filter=concat(,"
          throw FilterParserExceptionImpl.createEXPRESSION_EXPECTED_AT_POS(token, curExpression);
        }

        tokenList.expectToken(",", true);
        readComma = true;
      }
      else
      {
        readComma = false;
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
      throw FilterParserExceptionImpl.createMETHOD_WRONG_ARG_COUNT(methodExpression, methodToken, curExpression);
    }

    if ((methodInfo.getMaxParameter() > -1) && (count > methodInfo.getMaxParameter()))
    {
      //Tested with TestParserExceptions.TestPMreadParameters CASE 15
      throw FilterParserExceptionImpl.createMETHOD_WRONG_ARG_COUNT(methodExpression, methodToken, curExpression);
    }

    return methodExpression;
  }

  protected CommonExpression readElement(final CommonExpression leftExpression) throws ExpressionParserException, ExpressionParserInternalError
  {
    return readElement(leftExpression, null);
  }

  /**
   * Reads: Unary operators, Methods, Properties, ...
   * but not binary operators which are handelt in {@link #readElements(CommonExpression, int)}
   * @param leftExpression 
   *   Used while parsing properties. In this case ( e.g. parsing "a/b") the property "a" ( as leftExpression of "/") is relevant 
   *   to verify whether the property "b" exists inside the edm
   * @return a CommonExpression
   * @throws ExpressionParserException
   * @throws ExpressionParserInternalError 
   * @throws TokenizerMessage 
   */
  protected CommonExpression readElement(final CommonExpression leftExpression, final ActualBinaryOperator leftOperator) throws ExpressionParserException, ExpressionParserInternalError
  {
    CommonExpression node = null;
    Token token;
    Token lookToken;
    lookToken = tokenList.lookToken();
    if (lookToken == null) {
      return null;
    }

    switch (lookToken.getKind())
    {
    case OPENPAREN:
      node = readParenthesis();
      return node;
    case CLOSEPAREN: // ')'  finishes a parenthesis (it is no extra token)" +
    case COMMA: //. " ','  is a separator for function parameters (it is no extra token)" +
      return null;
    default:
      // continue
    }

    //-->Check if the token is a unary operator
    InfoUnaryOperator unaryOperator = isUnaryOperator(lookToken);
    if (unaryOperator != null)
    {
      return readUnaryoperator(lookToken, unaryOperator);
    }

    //---expect the look ahead token
    token = tokenList.expectToken(lookToken.getUriLiteral(), true);
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
      validateEdmProperty(leftExpression, property, token, leftOperator);
      return property;
    }

    // not Tested, should not occur 
    throw ExpressionParserInternalError.createCOMMON();
  }

  protected CommonExpression readUnaryoperator(final Token lookToken, final InfoUnaryOperator unaryOperator) throws ExpressionParserException, ExpressionParserInternalError
  {
    tokenList.expectToken(lookToken.getUriLiteral(), true);

    CommonExpression operand = readElement(null);
    UnaryExpression unaryExpression = new UnaryExpressionImpl(unaryOperator, operand);
    validateUnaryOperatorTypes(unaryExpression); //throws ExpressionInvalidOperatorTypeException

    return unaryExpression;
  }

  protected CommonExpression readMethod(final Token token, final InfoMethod methodOperator) throws ExpressionParserException, ExpressionParserInternalError
  {
    MethodExpressionImpl method = new MethodExpressionImpl(methodOperator);

    readParameters(methodOperator, method, token);
    validateMethodTypes(method, token); //throws ExpressionInvalidOperatorTypeException

    return method;
  }

  protected ActualBinaryOperator readBinaryOperator()
  {
    InfoBinaryOperator operator = null;
    Token token = tokenList.lookToken();
    if (token == null) {
      return null;
    }
    if ((token.getKind() == TokenKind.SYMBOL) && (token.getUriLiteral().equals("/")))
    {
      operator = availableBinaryOperators.get(token.getUriLiteral());
    }
    else if (token.getKind() == TokenKind.LITERAL)
    {
      operator = availableBinaryOperators.get(token.getUriLiteral());
    }

    if (operator == null) {
      return null;
    }

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
  protected InfoUnaryOperator isUnaryOperator(final Token token)
  {
    if ((token.getKind() == TokenKind.LITERAL) || (token.getKind() == TokenKind.SYMBOL))
    {
      InfoUnaryOperator operator = availableUnaryOperators.get(token.getUriLiteral());
      return operator;
    }
    return null;
  }

  protected InfoMethod isMethod(final Token token, final Token lookToken)
  {
    if ((lookToken != null) && (lookToken.getKind() == TokenKind.OPENPAREN))
    {
      return availableMethods.get(token.getUriLiteral());
    }
    return null;
  }

  protected void validateEdmProperty(final CommonExpression leftExpression, final PropertyExpressionImpl property, final Token propertyToken, final ActualBinaryOperator actBinOp) throws ExpressionParserException, ExpressionParserInternalError {

    // Exist if no edm provided
    if (resourceEntityType == null) {
      return;
    }

    if (leftExpression == null)
    {
      //e.g. "$filter=city eq 'Hong Kong'" --> "city" is checked against the resource entity type of the last URL segment 
      validateEdmPropertyOfStructuredType(resourceEntityType, property, propertyToken);
      return;
    }
    //e.g. "$filter='Hong Kong' eq address/city" --> city is "checked" against the type of the property "address".
    //     "address" itself must be a (navigation)property of the resource entity type of the last URL segment AND
    //     "address" must have a structural edm type
    EdmType parentType = leftExpression.getEdmType(); //parentType point now to the type of property "address"

    if ((actBinOp != null) && (actBinOp.operator.getOperator() != BinaryOperator.PROPERTY_ACCESS))
    {
      validateEdmPropertyOfStructuredType(resourceEntityType, property, propertyToken);
      return;
    }
    else
    {
      if ((leftExpression.getKind() != ExpressionKind.PROPERTY) && (leftExpression.getKind() != ExpressionKind.MEMBER))
      {
        if (actBinOp != null) {
          //Tested with TestParserExceptions.TestPMvalidateEdmProperty CASE 6
          throw FilterParserExceptionImpl.createLEFT_SIDE_NOT_A_PROPERTY(actBinOp.token, curExpression);
        } else {
          // not Tested, should not occur 
          throw ExpressionParserInternalError.createCOMMON();
        }

      }
    }

    if (parentType instanceof EdmEntityType)
    {
      //e.g. "$filter='Hong Kong' eq navigationProp/city" --> "navigationProp" is a navigation property with a entity type
      validateEdmPropertyOfStructuredType((EdmStructuralType) parentType, property, propertyToken);
    }
    else if (parentType instanceof EdmComplexType)
    {
      //e.g. "$filter='Hong Kong' eq address/city" --> "address" is a property with a complex type 
      validateEdmPropertyOfStructuredType((EdmStructuralType) parentType, property, propertyToken);
    }
    else
    {
      //e.g. "$filter='Hong Kong' eq name/city" --> "name is of type String"
      //Tested with TestParserExceptions.TestPMvalidateEdmProperty CASE 5
      throw FilterParserExceptionImpl.createLEFT_SIDE_NOT_STRUCTURAL_TYPE(parentType, property, propertyToken, curExpression);
    }

    return;
  }

  protected void validateEdmPropertyOfStructuredType(final EdmStructuralType parentType, final PropertyExpressionImpl property, final Token propertyToken) throws ExpressionParserException, ExpressionParserInternalError
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
        //Tested with TestParserExceptions.TestPMvalidateEdmProperty CASE 3
        throw FilterParserExceptionImpl.createPROPERTY_NAME_NOT_FOUND_IN_TYPE(parentType, property, propertyToken, curExpression);
      }

    } catch (EdmException e) {
      // not Tested, should not occur
      throw ExpressionParserInternalError.createERROR_ACCESSING_EDM(e);
    }
  }

  /*
    protected void validateEdmPropertyOfComplexType1(EdmComplexType parentType, PropertyExpressionImpl property, Token propertyToken) throws FilterParserException, FilterParserInternalError
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
          //Tested with TestParserExceptions.TestPMvalidateEdmProperty CASE 3
          throw FilterParserExceptionImpl.createPROPERTY_NAME_NOT_FOUND_IN_TYPE(parentType, property, propertyToken, curExpression);
        }

      } catch (EdmException e) {
        // not Tested, should not occur
        throw FilterParserInternalError.createERROR_ACCESSING_EDM(e);
      }
    }

    protected void validateEdmPropertyOfEntityType1(EdmEntityType parentType, PropertyExpressionImpl property, Token propertyToken) throws FilterParserException, FilterParserInternalError
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
          //Tested with TestParserExceptions.TestPMvalidateEdmProperty CASE 1
          throw FilterParserExceptionImpl.createPROPERTY_NAME_NOT_FOUND_IN_TYPE(parentType, property, propertyToken, curExpression);
        }

      } catch (EdmException e) {
        // not Tested, should not occur
        throw FilterParserInternalError.createERROR_ACCESSING_EDM(e);
      }
    }*/

  protected void validateUnaryOperatorTypes(final UnaryExpression unaryExpression) throws ExpressionParserInternalError
  {
    InfoUnaryOperator unOpt = availableUnaryOperators.get(unaryExpression.getOperator().toUriLiteral());
    EdmType operandType = unaryExpression.getOperand().getEdmType();

    if ((operandType == null) && (resourceEntityType == null)) {
      return;
    }

    List<EdmType> actualParameterTypes = new ArrayList<EdmType>();
    actualParameterTypes.add(operandType);

    ParameterSet parameterSet = unOpt.validateParameterSet(actualParameterTypes);
    if (parameterSet != null)
    {
      unaryExpression.setEdmType(parameterSet.getReturnType());
    }
  }

  protected void validateBinaryOperatorTypes(final BinaryExpression binaryExpression) throws ExpressionParserException, ExpressionParserInternalError
  {
    InfoBinaryOperator binOpt = availableBinaryOperators.get(binaryExpression.getOperator().toUriLiteral());

    List<EdmType> actualParameterTypes = new ArrayList<EdmType>();
    EdmType operand = binaryExpression.getLeftOperand().getEdmType();

    if ((operand == null) && (resourceEntityType == null)) {
      return;
    }
    actualParameterTypes.add(operand);

    operand = binaryExpression.getRightOperand().getEdmType();

    if ((operand == null) && (resourceEntityType == null)) {
      return;
    }
    actualParameterTypes.add(operand);

    ParameterSet parameterSet = binOpt.validateParameterSet(actualParameterTypes);
    if (parameterSet == null)
    {
      BinaryExpressionImpl binaryExpressionImpl = (BinaryExpressionImpl) binaryExpression;

      // Tested with TestParserExceptions.TestPMvalidateBinaryOperator
      throw FilterParserExceptionImpl.createINVALID_TYPES_FOR_BINARY_OPERATOR(
          binaryExpression.getOperator(),
          binaryExpression.getLeftOperand().getEdmType(),
          binaryExpression.getRightOperand().getEdmType(),
          binaryExpressionImpl.getToken(), curExpression
          );
    }
    binaryExpression.setEdmType(parameterSet.getReturnType());
  }

  protected void validateMethodTypes(final MethodExpression methodExpression, final Token methodToken) throws ExpressionParserException, ExpressionParserInternalError
  {
    InfoMethod methOpt = availableMethods.get(methodExpression.getUriLiteral());

    List<EdmType> actualParameterTypes = new ArrayList<EdmType>();

    //If there are no parameter then don't perform a type check
    if (methodExpression.getParameters().size() == 0)
    {
      return;
    }

    for (CommonExpression parameter : methodExpression.getParameters())
    {
      //If there is not at parsing time its not possible to determine the type of eg myPropertyName.
      //Since this should not cause validation errors null type node arguments are leading to bypass
      //the validation
      if ((parameter.getEdmType() == null) && (resourceEntityType == null))
      {
        return;
      }
      actualParameterTypes.add(parameter.getEdmType());
    }

    ParameterSet parameterSet = methOpt.validateParameterSet(actualParameterTypes);
    //If there is not returntype then the input parameter 
    if (parameterSet == null)
    {
      // Tested with TestParserExceptions.testPMvalidateMethodTypes CASE 1
      throw FilterParserExceptionImpl.createMETHOD_WRONG_INPUT_TYPE((MethodExpressionImpl) methodExpression, methodToken, curExpression);
    }
    methodExpression.setEdmType(parameterSet.getReturnType());
  }

  static void initAvailTables()
  {
    Map<String, InfoBinaryOperator> lAvailableBinaryOperators = new HashMap<String, InfoBinaryOperator>();
    Map<String, InfoMethod> lAvailableMethods = new HashMap<String, InfoMethod>();
    Map<String, InfoUnaryOperator> lAvailableUnaryOperators = new HashMap<String, InfoUnaryOperator>();

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
    lAvailableBinaryOperators.put("/",
        new InfoBinaryOperator(BinaryOperator.PROPERTY_ACCESS, "Primary", 100, new ParameterSetCombination.PSCReturnTypeEqLastParameter()));//todo fix this

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

    lAvailableBinaryOperators.put(BinaryOperator.MUL.toUriLiteral(),
        new InfoBinaryOperator(BinaryOperator.MUL, "Multiplicative", 60, combination));
    lAvailableBinaryOperators.put(BinaryOperator.DIV.toUriLiteral(),
        new InfoBinaryOperator(BinaryOperator.DIV, "Multiplicative", 60, combination));
    lAvailableBinaryOperators.put(BinaryOperator.MODULO.toUriLiteral(),
        new InfoBinaryOperator(BinaryOperator.MODULO, "Multiplicative", 60, combination));

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

    lAvailableBinaryOperators.put(BinaryOperator.ADD.toUriLiteral(),
        new InfoBinaryOperator(BinaryOperator.ADD, "Additive", 50, combination));
    lAvailableBinaryOperators.put(BinaryOperator.SUB.toUriLiteral(),
        new InfoBinaryOperator(BinaryOperator.SUB, "Additive", 50, combination));

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

    lAvailableBinaryOperators.put(BinaryOperator.LT.toUriLiteral(),
        new InfoBinaryOperator(BinaryOperator.LT, "Relational", 40, combination));
    lAvailableBinaryOperators.put(BinaryOperator.GT.toUriLiteral(),
        new InfoBinaryOperator(BinaryOperator.GT, "Relational", 40, combination));
    lAvailableBinaryOperators.put(BinaryOperator.GE.toUriLiteral(),
        new InfoBinaryOperator(BinaryOperator.GE, "Relational", 40, combination));
    lAvailableBinaryOperators.put(BinaryOperator.LE.toUriLiteral(),
        new InfoBinaryOperator(BinaryOperator.LE, "Relational", 40, combination));

    //---Equality---
    //combination = new ParameterSetCombination.PSCflex();
    combination.addFirst(new ParameterSet(boolean_, boolean_, boolean_));
    /*combination.add(new ParameterSet(boolean_, string, string));
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
    combination.add(new ParameterSet(boolean_, binary, binary));*/

    lAvailableBinaryOperators.put(BinaryOperator.EQ.toUriLiteral(),
        new InfoBinaryOperator(BinaryOperator.EQ, "Equality", 30, combination));
    lAvailableBinaryOperators.put(BinaryOperator.NE.toUriLiteral(),
        new InfoBinaryOperator(BinaryOperator.NE, "Equality", 30, combination));

    //"---Conditinal AND---
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(boolean_, boolean_, boolean_));

    lAvailableBinaryOperators.put(BinaryOperator.AND.toUriLiteral(),
        new InfoBinaryOperator(BinaryOperator.AND, "Conditinal", 20, combination));

    //---Conditinal OR---
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(boolean_, boolean_, boolean_));

    lAvailableBinaryOperators.put(BinaryOperator.OR.toUriLiteral(),
        new InfoBinaryOperator(BinaryOperator.OR, "Conditinal", 10, combination));

    //endswith
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(boolean_, string, string));
    lAvailableMethods.put(MethodOperator.ENDSWITH.toUriLiteral(),
        new InfoMethod(MethodOperator.ENDSWITH, 2, 2, combination));

    //indexof
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(int32, string, string));
    lAvailableMethods.put(MethodOperator.INDEXOF.toUriLiteral(),
        new InfoMethod(MethodOperator.INDEXOF, 2, 2, combination));

    //startswith
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(boolean_, string, string));
    lAvailableMethods.put(MethodOperator.STARTSWITH.toUriLiteral(),
        new InfoMethod(MethodOperator.STARTSWITH, 2, 2, combination));

    //tolower
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(string, string));
    lAvailableMethods.put(MethodOperator.TOLOWER.toUriLiteral(),
        new InfoMethod(MethodOperator.TOLOWER, combination));

    //toupper
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(string, string));
    lAvailableMethods.put(MethodOperator.TOUPPER.toUriLiteral(),
        new InfoMethod(MethodOperator.TOUPPER, combination));

    //trim
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(string, string));
    lAvailableMethods.put(MethodOperator.TRIM.toUriLiteral(),
        new InfoMethod(MethodOperator.TRIM, combination));

    //substring
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(string, string, int32));
    combination.add(new ParameterSet(string, string, int32, int32));
    lAvailableMethods.put(MethodOperator.SUBSTRING.toUriLiteral(),
        new InfoMethod(MethodOperator.SUBSTRING, 1, -1, combination));

    //substringof
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(boolean_, string, string));
    lAvailableMethods.put(MethodOperator.SUBSTRINGOF.toUriLiteral(),
        new InfoMethod(MethodOperator.SUBSTRINGOF, 1, -1, combination));

    //concat
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(string, string, string).setFurtherType(string));
    lAvailableMethods.put(MethodOperator.CONCAT.toUriLiteral(),
        new InfoMethod(MethodOperator.CONCAT, 2, -1, combination));

    //length
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(int32, string));
    lAvailableMethods.put(MethodOperator.LENGTH.toUriLiteral(),
        new InfoMethod(MethodOperator.LENGTH, combination));

    //year
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(int32, datetime));
    lAvailableMethods.put(MethodOperator.YEAR.toUriLiteral(),
        new InfoMethod(MethodOperator.YEAR, combination));

    //month
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(int32, datetime));
    lAvailableMethods.put(MethodOperator.MONTH.toUriLiteral(),
        new InfoMethod(MethodOperator.MONTH, combination));

    //day
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(int32, datetime));
    lAvailableMethods.put(MethodOperator.DAY.toUriLiteral(),
        new InfoMethod(MethodOperator.DAY, combination));

    //hour
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(int32, datetime));
    combination.add(new ParameterSet(int32, time));
    combination.add(new ParameterSet(int32, datetimeoffset));
    lAvailableMethods.put(MethodOperator.HOUR.toUriLiteral(),
        new InfoMethod(MethodOperator.HOUR, combination));

    //minute
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(int32, datetime));
    combination.add(new ParameterSet(int32, time));
    combination.add(new ParameterSet(int32, datetimeoffset));
    lAvailableMethods.put(MethodOperator.MINUTE.toUriLiteral(),
        new InfoMethod(MethodOperator.MINUTE, combination));

    //second
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(int32, datetime));
    combination.add(new ParameterSet(int32, time));
    combination.add(new ParameterSet(int32, datetimeoffset));
    lAvailableMethods.put(MethodOperator.SECOND.toUriLiteral(),
        new InfoMethod(MethodOperator.SECOND, combination));

    //round
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(decimal, decimal));
    combination.add(new ParameterSet(double_, double_));
    lAvailableMethods.put(MethodOperator.ROUND.toUriLiteral(),
        new InfoMethod(MethodOperator.ROUND, combination));

    //ceiling
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(decimal, decimal));
    combination.add(new ParameterSet(double_, double_));
    lAvailableMethods.put(MethodOperator.CEILING.toUriLiteral(),
        new InfoMethod(MethodOperator.CEILING, combination));

    //floor
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(decimal, decimal));
    combination.add(new ParameterSet(double_, double_));
    lAvailableMethods.put(MethodOperator.FLOOR.toUriLiteral(),
        new InfoMethod(MethodOperator.FLOOR, combination));

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
    lAvailableUnaryOperators.put(UnaryOperator.MINUS.toUriLiteral(),
        new InfoUnaryOperator(UnaryOperator.MINUS, "minus", combination));

    //not
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(boolean_, boolean_));
    lAvailableUnaryOperators.put(UnaryOperator.NOT.toUriLiteral(),
        new InfoUnaryOperator(UnaryOperator.NOT, "not", combination));

    availableBinaryOperators = Collections.unmodifiableMap(lAvailableBinaryOperators);
    availableMethods = Collections.unmodifiableMap(lAvailableMethods);
    availableUnaryOperators = Collections.unmodifiableMap(lAvailableUnaryOperators);
  }
}
