package com.sap.core.odata.core.uri.expression;

/*TODO remove all @SuppressWarnings("unused")*/
//TODO check all Exceptions
//TODO check variableNames

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmStructuralType;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;
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
import com.sap.core.odata.api.uri.expression.PropertyExpression;
import com.sap.core.odata.api.uri.expression.UnaryExpression;
import com.sap.core.odata.api.uri.expression.UnaryOperator;
import com.sap.core.odata.core.edm.EdmSimpleTypeFacadeImpl;

public class FilterParserImpl implements FilterParser
{
  /*do the static initialization*/
  static Map<String, InfoBinaryOperator> availableBinaryOperators;
  static Map<String, InfoMethod> availableFunctions;
  static Map<String, InfoUnaryOperator> availableUnaryOperators;
  
  static
  {
    initAvialTables();
  }

  /*instance attributes*/
  protected boolean promoteParameters = true;
  protected Edm edm = null;
  protected EdmType resourceEntityType = null;
  protected TokenList tokenList = null;

  /**
   * Creates a new FilterParser implementation
   * @param edm EntityDataModel   
   * @param resourceEntityType EntityType of the resource on which the filter is applied
   */
  public FilterParserImpl(Edm edm, EdmType resourceEntityType)
  {
    this.edm = edm;
    this.resourceEntityType = resourceEntityType;
  }

  @Override
  public FilterExpression ParseExpression(String filterExpression) throws FilterParserException, ExceptionExpressionInternalError
  {
    CommonExpression node = null;

    try
    {
      Tokenizer tokenizer = new Tokenizer();
      tokenList = tokenizer.tokenize(filterExpression); //throws TokenizerMessage
    } catch (ExceptionTokenizer tokenizerException)
    {
      //wrap the tokenizer exception
      throw new FilterParserException(FilterParserException.ERROR_IN_TOKENIZER).setCause(tokenizerException);
    }

    //if token list is empty
    if (!tokenList.hasTokens())
      return new FilterExpressionImpl(filterExpression, null);

    try
    {
      CommonExpression nodeLeft = readElement();
      node = readElements(nodeLeft, 0);
    } catch (FilterParserException expressionException)
    {
      FilterExpression fe = new FilterExpressionImpl(filterExpression, null);
      //add info an rethrow
      throw expressionException.setFilterTree(fe);
    }

    //post check
    //TODO verify if is an internal error or an user error. E.g. Test "a eq b b" or " a b"
    if (tokenList.tokenCount() > tokenList.currentToken) //this indicates that not all tokens have been read
      throw new FilterParserException(FilterParserException.INVALID_TRAILING_TOKEN_DETECTED_AFTER_PARSING);

    //create and return filterExpression node
    return new FilterExpressionImpl(filterExpression, node);
  }

  public void setPromoteParameters(boolean promoteParameters) {
    this.promoteParameters = promoteParameters;
  }

  protected CommonExpression readElements(CommonExpression leftExpression, int priority) throws FilterParserException, ExceptionExpressionInternalError
  {
    CommonExpression leftNode;
    CommonExpression rightNode;
    BinaryExpression binaryNode;

    leftNode = leftExpression;
    InfoBinaryOperator operator = readBinaryOperator();

    while ((operator != null) && (operator.priority >= priority))
    {
      tokenList.next();
      rightNode = readElement();//throws ExceptionParseExpression, ExceptionExpressionInternalError

      InfoBinaryOperator nextOperator = readBinaryOperator();

      if ((nextOperator != null) && (nextOperator.priority > operator.priority))
      {
        rightNode = readElements(rightNode, nextOperator.priority);//op2 is read in read_elements.
        nextOperator = readBinaryOperator();
      }

      binaryNode = new BinaryExpressionImpl(operator, leftNode, rightNode);

      try
      {
        validateBinaryOperator(binaryNode);
      } catch (FilterParserException expressionException)
      {
        //Attach error information
        expressionException.setFilterTree(binaryNode);
        throw expressionException;
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
   *   While reading the elements in the parenthesis an error occured
   * @throws TokenizerMessage 
   *   The next token did not match the expected token
   */
  protected CommonExpression readParenthesis() throws FilterParserException, ExceptionExpressionInternalError
  {
    //---check for '('    
    try {
      tokenList.expectToken(TokenKind.OPENPAREN);
    } catch (ExceptionTokenizerExpect e)
    { //Internal parsing error, even if there are no more token (then there should be a different exception).  
      throw new ExceptionExpressionInternalError(ExceptionExpressionInternalError.ERROR_PARSING_PARENTHESIS, e);
    }

    CommonExpression firstExpression = readElement();
    CommonExpression parenthesisExpression = readElements(firstExpression, 0);

    //---check for ')'
    try {
      tokenList.expectToken(TokenKind.CLOSEPAREN); //TokenizerMessage
    } catch (ExceptionTokenizerExpect e)
    {
      //Internal parsing error, even if there are no more token (then there should be a different exception).
      ExceptionExpressionInternalError eie = new ExceptionExpressionInternalError(ExceptionExpressionInternalError.ERROR_PARSING_PARENTHESIS, e);
      eie.setExpression(parenthesisExpression);
      throw eie;
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
   * @throws ExceptionExpressionInternalError 
   * @throws ExceptionTokenizerExpect 
   *   The next token did not match the expected token
   */
  protected MethodExpression readParameters(InfoMethod methodInfo, MethodExpressionImpl methodExpression) throws FilterParserException, ExceptionExpressionInternalError
  {
    CommonExpression expression;
    boolean expectAnotherExpression = false;

    //---check for '('
    try {
      tokenList.expectToken(TokenKind.OPENPAREN); //throws ExceptionTokenizerExpect
    } catch (ExceptionTokenizerExpect e) {
      throw new ExceptionExpressionInternalError(ExceptionExpressionInternalError.ERROR_PARSING_PARENTHESIS, e);
    }

    Token token = tokenList.lookToken();
    while (token.getKind() != TokenKind.CLOSEPAREN)
    {
      expression = readElement();
      //After a ',' inside the parenthesis which define the method parameters a expression is expected 
      //E.g. $filter=startswith(Country,'UK',) --> is wrong
      //E.g. $filter=startswith(Country,) --> is also wrong 
      if ((expression == null) && (expectAnotherExpression != true))
      {
        throw new FilterParserException(FilterParserException.EXPRESSION_EXPECTED_AT_POS);
      }
      else if (expression != null) //parameter list may be empty
      {
        methodExpression.appendParameter(expression);
      }


      token = tokenList.lookToken();

      if (token.getKind() == TokenKind.COMMA)
      {
        //eat the ','
        try {
          tokenList.expectToken(Character.toString(CharConst.GC_STR_COMMA));
        } catch (ExceptionTokenizerExpect e)
        {
          ExceptionExpressionInternalError eie = new ExceptionExpressionInternalError(ExceptionExpressionInternalError.ERROR_PARSING_PARENTHESIS, e);
          throw eie;
        }
      }
    }

    //---check for ')'
    try {
      tokenList.expectToken(TokenKind.CLOSEPAREN);
    } catch (ExceptionTokenizerExpect e)
    {
      //Internal parsing error, even if there are no more token (then there should be a different exception).
      throw new ExceptionExpressionInternalError(ExceptionExpressionInternalError.ERROR_PARSING_PARENTHESIS, e);
    }

    //---check parameter count
    int count = methodExpression.getParameters().size();
    if (count < methodInfo.getMinParameter())
    {
      throw FilterParserException.NewToFewParameters(methodExpression);
    }

    if ((methodInfo.getMaxParameter() > -1) && (count > methodInfo.getMinParameter()))
    {
      throw FilterParserException.NewToManyParameters(methodExpression);
    }

    return methodExpression;
  }

  /**
   * Reads: Unary operators, Methods, Properties, ...
   * but not binary operators which are handelt in {@link #readElements(CommonExpression, int)}
   * @return
   * @throws FilterParserException
   * @throws ExceptionExpressionInternalError 
   * @throws TokenizerMessage 
   */
  protected CommonExpression readElement() throws FilterParserException, ExceptionExpressionInternalError
  {
    CommonExpression node = null;
    Token token;
    Token lookToken;
    lookToken = tokenList.lookToken();

    if (lookToken.getKind() == TokenKind.OPENPAREN) //."if token a '(' then process read a new note for the '(' and return it.
    {
      node = readParenthesis(); //throws ExceptionParseExpression,ExceptionExpressionInternalError
      return node;
    }
    else if (lookToken.getKind() == TokenKind.CLOSEPAREN)// " ')'  finishes a parenthesis (it is no extra token)" +
    {
      return null;
    }

    else if (lookToken.getKind() == TokenKind.COMMA)//. " ','  is a separator for function parameter (it is no extra token)" +

    {
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
    } catch (ExceptionTokenizerExpect e)
    {
      //Internal parsing error, even if there are no more token (then there should be a different exception).  
      throw new ExceptionExpressionInternalError(ExceptionExpressionInternalError.ERROR_PARSING_PARENTHESIS, e);
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

    //-->Check if token is a property, e.g. "name" or "adress"
    if (token.getKind() == TokenKind.LITERAL)
    {
      PropertyExpression property = new PropertyExpressionImpl(token.getUriLiteral(), null, token.getJavaLiteral());
      validatePropertyTypes(property);
      return property;
    }

    throw new FilterParserException(FilterParserException.INVALID_TOKEN);

  }

  protected CommonExpression readUnaryoperator(Token lookToken, InfoUnaryOperator unaryOperator) throws FilterParserException, ExceptionExpressionInternalError
  {
    Token token = null;

    //---read token
    try {
      token = tokenList.expectToken(lookToken.getUriLiteral());
    } catch (ExceptionTokenizerExpect e) {
      throw new ExceptionExpressionInternalError(ExceptionExpressionInternalError.ERROR_PARSING_PARENTHESIS, e);
    }

    CommonExpression operand = readElement();
    UnaryExpression unaryExpression = new UnaryExpressionImpl(unaryOperator, operand);
    validataUnaryOperator(unaryExpression, token); //throws ExpressionInvalidOperatorTypeException
    return unaryExpression;
  }

  protected CommonExpression readMethod(Token token, InfoMethod methodOperator) throws FilterParserException, ExceptionExpressionInternalError
  {
    MethodExpressionImpl method = new MethodExpressionImpl((InfoMethod) methodOperator);

    readParameters((InfoMethod) methodOperator, method);
    validateMethodTypes(method, token); //throws ExpressionInvalidOperatorTypeException
    return method;
  }

  protected InfoBinaryOperator readBinaryOperator()
  {
    Token token = tokenList.lookToken();
    if (token == null) return null;
    if (token.getKind() == TokenKind.LITERAL)
    {
      InfoBinaryOperator operator = availableBinaryOperators.get(token.getUriLiteral());
      return operator;
    }

    return null;
  }

  protected CommonExpression readElementForMember(PropertyExpression io_parent_property) throws Exception
  {
    Token lv_token;
    Token lv_look_token;
    PropertyExpression lo_edm_prop;
    EdmTyped lo_parent_edm_prop;
    //read next token
    lv_token = tokenList.lookToken();
    CommonExpression ro_node;

    if (lv_token.getKind() == TokenKind.OPENPAREN) //."if token a '(' then process read a new note for the '(' and return it.
    {
      ro_node = readParenthesis();
      return ro_node; //no edm cross check for parenthesis.
    }
    else if ((lv_token.getKind() == TokenKind.LITERAL) || (lv_token.getKind() == TokenKind.LITERAL))
    {

      tokenList.expectToken((String) lv_token.getUriLiteral());//TODO check this 

      lv_look_token = tokenList.lookToken();

      if (lv_look_token.getKind() == TokenKind.OPENPAREN)
      {
        //rigth hand sems to be a function
        throw new Exception();//   /iwcor/cx_ds_expr_parser_error=>member_access_wrong_right_hand.        
      }

      //if no EDM check is necesarry
      if (edm == null)
      {
        //create expression property node without reference to edm property
        return new PropertyExpressionImpl(lv_look_token.getUriLiteral(), null, lv_look_token.getJavaLiteral());
      }

      //EDM data is available so we do the check
      if (io_parent_property == null)
      {
        //inconsistence in Parser - io_parent_node MUST be filled
        throw new Exception();//internal error
      }

      //get parent edm property of the parent expression node
      lo_parent_edm_prop = io_parent_property.getEdmProperty();
      if (lo_parent_edm_prop == null)
      {
        throw new Exception();//internal error
      }

      try
      {
        //get parent edm type of parent edm property
        lo_parent_edm_prop = lo_parent_edm_prop;
        if (lo_parent_edm_prop == null)
        {
          throw new Exception(); //inconsistence in EDM (a property MUST have always a type
        }

        //do typing for property; may be /IWCOR/IF_DS_EDM_PROPERTY or /IWCOR/IF_DS_EDM_NAV_PROPERTY
        lo_edm_prop = (PropertyExpression) getProperty(lo_parent_edm_prop.getType(), lv_token);
      } catch (Exception ex)
      {
        throw ex;
      }
      //create property node with reference to edm property
      ro_node = new PropertyExpressionImpl(lv_token.getUriLiteral(), (EdmProperty) lo_edm_prop, lv_token.getJavaLiteral());

      try
      {
        ro_node.setEdmType(lo_edm_prop.getEdmType());
      } catch (Exception ex)
      {
        /*TODO
         * CATCH /iwcor/cx_ds_edm_error INTO lx_edm_error.
            RAISE EXCEPTION TYPE /iwcor/cx_ds_internal_error
              EXPORTING
                previous = lx_edm_error.
          CATCH cx_sy_move_cast_error.
            RAISE EXCEPTION TYPE /iwcor/cx_ds_internal_error
              EXPORTING
                previous = lx_edm_error.
         */
      }
    } else
    {
      //TODO raise  textid = /iwcor/cx_ds_expr_parser_error=>member_access_wrong_right_hand.

    }
    CommonExpression RO_NODE = null;
    return RO_NODE;
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
      return availableFunctions.get(token.getUriLiteral());
    }
    return null;
  }

  static void initAvialTables()
  {
    availableBinaryOperators = new HashMap<String, InfoBinaryOperator>();
    availableFunctions = new HashMap<String, InfoMethod>();
    availableUnaryOperators = new HashMap<String, InfoUnaryOperator>();
    Vector<ParameterSet> combination = null;

    EdmSimpleType boolean_ = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.Boolean);
    EdmSimpleType sbyte = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.SByte);
    EdmSimpleType byte_ = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.Byte);
    EdmSimpleType int16 = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.Int16);
    EdmSimpleType int32 = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.Int32);
    EdmSimpleType int64 = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.Int64);
    EdmSimpleType single = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.Single);
    EdmSimpleType double_ = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.Double);
    EdmSimpleType decimal_ = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.Decimal);
    EdmSimpleType string = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.String);
    EdmSimpleType time = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.Time);
    EdmSimpleType datetime = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.DateTime);
    EdmSimpleType datetimeoffset = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.DateTimeOffset);
    EdmSimpleType guid = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.Guid);
    EdmSimpleType binary = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.Binary);

    //---Memeber member access---
    availableBinaryOperators.put("/", new InfoBinaryOperator(BinaryOperator.PROPERTY_ACCESS, "Primary", "/", 100));

    //---Multiplicative---
    combination = new Vector<ParameterSet>();
    combination.add(new ParameterSet(sbyte, sbyte, sbyte));
    combination.add(new ParameterSet(byte_, byte_, byte_));
    combination.add(new ParameterSet(int16, int16, int16));
    combination.add(new ParameterSet(int32, int32, int32));
    combination.add(new ParameterSet(int64, int64, int64));
    combination.add(new ParameterSet(single, single, single));
    combination.add(new ParameterSet(double_, double_, double_));
    combination.add(new ParameterSet(decimal_, decimal_, decimal_));
    //mul

    //XXX_MT_FUOPS.put(CharConst.GC_OPERATOR_MUL, combination);
    availableBinaryOperators.put(CharConst.GC_OPERATOR_MUL, new InfoBinaryOperator(BinaryOperator.MUL, "Multiplicative", CharConst.GC_OPERATOR_MUL, 60));
    //div
    //XXX_MT_FUOPS.put(CharConst.GC_OPERATOR_DIV, combination);
    availableBinaryOperators.put(CharConst.GC_OPERATOR_DIV, new InfoBinaryOperator(BinaryOperator.DIV, "Multiplicative", CharConst.GC_OPERATOR_DIV, 60));
    //mod
    //XXX_MT_FUOPS.put(CharConst.GC_OPERATOR_MOD, combination);
    availableBinaryOperators.put(CharConst.GC_OPERATOR_MOD, new InfoBinaryOperator(BinaryOperator.MODULO, "Multiplicative", CharConst.GC_OPERATOR_MOD, 60));

    //---Additive---
    combination = new Vector<ParameterSet>();
    combination.add(new ParameterSet(sbyte, sbyte, sbyte));
    combination.add(new ParameterSet(byte_, byte_, byte_));
    combination.add(new ParameterSet(int16, int16, int16));
    combination.add(new ParameterSet(int32, int32, int32));
    combination.add(new ParameterSet(int64, int64, int64));
    combination.add(new ParameterSet(single, single, single));
    combination.add(new ParameterSet(double_, double_, double_));
    combination.add(new ParameterSet(decimal_, decimal_, decimal_));

    //XXX_MT_FUOPS.put(CharConst.GC_OPERATOR_ADD, combination);
    availableBinaryOperators.put(CharConst.GC_OPERATOR_ADD, new InfoBinaryOperator(BinaryOperator.ADD, "Multiplicative", CharConst.GC_OPERATOR_ADD, 50));
    //XXX_MT_FUOPS.put(CharConst.GC_OPERATOR_SUB, combination);
    availableBinaryOperators.put(CharConst.GC_OPERATOR_SUB, new InfoBinaryOperator(BinaryOperator.SUB, "Multiplicative", CharConst.GC_OPERATOR_SUB, 50));

    //---Relational---
    combination = new Vector<ParameterSet>();
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
    combination.add(new ParameterSet(boolean_, decimal_, decimal_));
    combination.add(new ParameterSet(boolean_, binary, binary));

    //lt

    //XXX_MT_FUOPS.put(CharConst.GC_OPERATOR_LT, combination);
    availableBinaryOperators.put(CharConst.GC_OPERATOR_LT, new InfoBinaryOperator(BinaryOperator.LT, "Multiplicative", CharConst.GC_OPERATOR_LT, 40));
    //gt
    //XXX_MT_FUOPS.put(CharConst.GC_OPERATOR_GT, combination);
    availableBinaryOperators.put(CharConst.GC_OPERATOR_GT, new InfoBinaryOperator(BinaryOperator.GT, "Multiplicative", CharConst.GC_OPERATOR_GT, 40));
    //ge
    //XXX_MT_FUOPS.put(CharConst.GC_OPERATOR_GE, combination);
    availableBinaryOperators.put(CharConst.GC_OPERATOR_GE, new InfoBinaryOperator(BinaryOperator.GE, "Multiplicative", CharConst.GC_OPERATOR_GE, 40));
    //le
    //XXX_MT_FUOPS.put(CharConst.GC_OPERATOR_LE, combination);
    availableBinaryOperators.put(CharConst.GC_OPERATOR_LE, new InfoBinaryOperator(BinaryOperator.LE, "Multiplicative", CharConst.GC_OPERATOR_LE, 40));

    //---Equality---
    combination = new Vector<ParameterSet>();
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
    combination.add(new ParameterSet(boolean_, decimal_, decimal_));
    combination.add(new ParameterSet(boolean_, binary, binary));

    //eq

    //XXX_MT_FUOPS.put(CharConst.GC_OPERATOR_EQ, combination);
    availableBinaryOperators.put(CharConst.GC_OPERATOR_EQ, new InfoBinaryOperator(BinaryOperator.EQ, "Multiplicative", CharConst.GC_OPERATOR_EQ, 30));
    //ne
    //XXX_MT_FUOPS.put(CharConst.GC_OPERATOR_NE, combination);
    availableBinaryOperators.put(CharConst.GC_OPERATOR_NE, new InfoBinaryOperator(BinaryOperator.NE, "Multiplicative", CharConst.GC_OPERATOR_NE, 30));

    //"---Conditinal AND---
    combination = new Vector<ParameterSet>();
    combination.add(new ParameterSet(boolean_, boolean_, boolean_));

    //XXX_MT_FUOPS.put(CharConst.GC_OPERATOR_AND, combination);
    availableBinaryOperators.put(CharConst.GC_OPERATOR_AND, new InfoBinaryOperator(BinaryOperator.AND, "Multiplicative", CharConst.GC_OPERATOR_AND, 20));

    //---Conditinal OR---
    combination = new Vector<ParameterSet>();
    combination.add(new ParameterSet(boolean_, boolean_, boolean_));

    //XXX_MT_FUOPS.put(CharConst.GC_OPERATOR_OR, combination);
    availableBinaryOperators.put(CharConst.GC_OPERATOR_OR, new InfoBinaryOperator(BinaryOperator.OR, "Multiplicative", CharConst.GC_OPERATOR_OR, 10));

    //endswith
    combination = new Vector<ParameterSet>();
    combination.add(new ParameterSet(boolean_, string, string));
    //XXX_MT_FUOPS.put(CharConst.GC_FUNCTION_ENDSWITH, combination);
    availableFunctions.put(CharConst.GC_FUNCTION_ENDSWITH, new InfoMethod(MethodOperator.ENDSWITH, CharConst.GC_FUNCTION_ENDSWITH, 2, 2));

    //indexof
    combination = new Vector<ParameterSet>();
    combination.add(new ParameterSet(int32, string, string));
    //XXX_MT_FUOPS.put(CharConst.GC_FUNCTION_INDEXOF, combination);
    availableFunctions.put(CharConst.GC_FUNCTION_INDEXOF, new InfoMethod(MethodOperator.INDEXOF, CharConst.GC_FUNCTION_INDEXOF, 2, 2));

    //startswith
    combination = new Vector<ParameterSet>();
    combination.add(new ParameterSet(boolean_, string, string));
    //XXX_MT_FUOPS.put(CharConst.GC_FUNCTION_STARTSWITH, combination);
    availableFunctions.put(CharConst.GC_FUNCTION_STARTSWITH, new InfoMethod(MethodOperator.STARTSWITH, CharConst.GC_FUNCTION_STARTSWITH, 2, 2));

    //tolower
    combination = new Vector<ParameterSet>();
    combination.add(new ParameterSet(string, string));
    //XXX_MT_FUOPS.put(CharConst.GC_FUNCTION_TOLOWER, combination);
    availableFunctions.put(CharConst.GC_FUNCTION_TOLOWER, new InfoMethod(MethodOperator.TOLOWER, CharConst.GC_FUNCTION_TOLOWER, 1, 1));

    //toupper
    combination = new Vector<ParameterSet>();
    combination.add(new ParameterSet(string, string));
    //XXX_MT_FUOPS.put(CharConst.GC_FUNCTION_TOUPPER, combination);
    availableFunctions.put(CharConst.GC_FUNCTION_TOUPPER, new InfoMethod(MethodOperator.TOUPPER, CharConst.GC_FUNCTION_TOUPPER, 1, 1));

    //trim
    combination = new Vector<ParameterSet>();
    combination.add(new ParameterSet(string, string));
    //XXX_MT_FUOPS.put(CharConst.GC_FUNCTION_TRIM, combination);
    availableFunctions.put(CharConst.GC_FUNCTION_TRIM, new InfoMethod(MethodOperator.TRIM, CharConst.GC_FUNCTION_TRIM, 1, 1));

    //substring
    combination = new Vector<ParameterSet>();
    combination.add(new ParameterSet(string, string, int32));
    combination.add(new ParameterSet(string, string, int32, int32));
    //XXX_MT_FUOPS.put(CharConst.GC_FUNCTION_SUBSTRING, combination);
    availableFunctions.put(CharConst.GC_FUNCTION_SUBSTRING, new InfoMethod(MethodOperator.SUBSTRING, CharConst.GC_FUNCTION_SUBSTRING, 1, -1));

    //substringof
    combination = new Vector<ParameterSet>();
    combination.add(new ParameterSet(boolean_, string, string));
    //XXX_MT_FUOPS.put(CharConst.GC_FUNCTION_SUBSTRINGOF, combination);
    availableFunctions.put(CharConst.GC_FUNCTION_SUBSTRINGOF, new InfoMethod(MethodOperator.SUBSTRINGOF, CharConst.GC_FUNCTION_SUBSTRINGOF, 1, -1));

    //concat
    combination = new Vector<ParameterSet>();
    combination.add(new ParameterSet(string, string, string));
    //XXX_MT_FUOPS.put(CharConst.GC_FUNCTION_CONCAT, combination);
    availableFunctions.put(CharConst.GC_FUNCTION_CONCAT, new InfoMethod(MethodOperator.CONCAT, CharConst.GC_FUNCTION_CONCAT, 2, -1));

    //length
    combination = new Vector<ParameterSet>();
    combination.add(new ParameterSet(int32, string));
    //XXX_MT_FUOPS.put(CharConst.GC_FUNCTION_LENGTH, combination);
    availableFunctions.put(CharConst.GC_FUNCTION_LENGTH, new InfoMethod(MethodOperator.LENGTH, CharConst.GC_FUNCTION_LENGTH, 1, 1));

    //year
    combination = new Vector<ParameterSet>();
    combination.add(new ParameterSet(int32, datetime));
    //XXX_MT_FUOPS.put(CharConst.GC_FUNCTION_YEAR, combination);
    availableFunctions.put(CharConst.GC_FUNCTION_YEAR, new InfoMethod(MethodOperator.YEAR, CharConst.GC_FUNCTION_YEAR, 1, 1));

    //month
    combination = new Vector<ParameterSet>();
    combination.add(new ParameterSet(int32, datetime));
    //XXX_MT_FUOPS.put(CharConst.GC_FUNCTION_MONTH, combination);
    availableFunctions.put(CharConst.GC_FUNCTION_MONTH, new InfoMethod(MethodOperator.MONTH, CharConst.GC_FUNCTION_MONTH, 1, 1));

    //day
    combination = new Vector<ParameterSet>();
    combination.add(new ParameterSet(int32, datetime));
    //XXX_MT_FUOPS.put(CharConst.GC_FUNCTION_DAY, combination);
    availableFunctions.put(CharConst.GC_FUNCTION_DAY, new InfoMethod(MethodOperator.DAY, CharConst.GC_FUNCTION_DAY, 1, 1));

    //hour
    combination = new Vector<ParameterSet>();
    combination.add(new ParameterSet(int32, datetime));
    combination.add(new ParameterSet(int32, time));
    combination.add(new ParameterSet(int32, datetimeoffset));

    //XXX_MT_FUOPS.put(CharConst.GC_FUNCTION_HOUR, combination);
    availableFunctions.put(CharConst.GC_FUNCTION_HOUR, new InfoMethod(MethodOperator.HOUR, CharConst.GC_FUNCTION_HOUR, 1, 1));

    //minute
    combination = new Vector<ParameterSet>();
    combination.add(new ParameterSet(int32, datetime));
    combination.add(new ParameterSet(int32, time));
    combination.add(new ParameterSet(int32, datetimeoffset));

    //XXX_MT_FUOPS.put(CharConst.GC_FUNCTION_MINUTE, combination);
    availableFunctions.put(CharConst.GC_FUNCTION_MINUTE, new InfoMethod(MethodOperator.MINUTE, CharConst.GC_FUNCTION_MINUTE, 1, 1));

    //second
    combination = new Vector<ParameterSet>();
    combination.add(new ParameterSet(int32, datetime));
    combination.add(new ParameterSet(int32, time));
    combination.add(new ParameterSet(int32, datetimeoffset));
    //XXX_MT_FUOPS.put(CharConst.GC_FUNCTION_SECOND, combination);
    availableFunctions.put(CharConst.GC_FUNCTION_SECOND, new InfoMethod(MethodOperator.SECOND, CharConst.GC_FUNCTION_SECOND, 1, 1));

    //round
    combination = new Vector<ParameterSet>();
    combination.add(new ParameterSet(decimal_, decimal_));
    combination.add(new ParameterSet(double_, double_));

    //XXX_MT_FUOPS.put(CharConst.GC_FUNCTION_ROUND, combination);
    availableFunctions.put(CharConst.GC_FUNCTION_ROUND, new InfoMethod(MethodOperator.ROUND, CharConst.GC_FUNCTION_ROUND, 1, 1));

    //ceiling
    combination = new Vector<ParameterSet>();
    combination.add(new ParameterSet(decimal_, decimal_));
    combination.add(new ParameterSet(double_, double_));

    //XXX_MT_FUOPS.put(CharConst.GC_FUNCTION_CEILING, combination);
    availableFunctions.put(CharConst.GC_FUNCTION_CEILING, new InfoMethod(MethodOperator.CEILING, CharConst.GC_FUNCTION_CEILING, 1, 1));

    //floor
    combination = new Vector<ParameterSet>();
    combination.add(new ParameterSet(decimal_, decimal_));
    combination.add(new ParameterSet(double_, double_));

    //XXX_MT_FUOPS.put(CharConst.GC_FUNCTION_FLOOR, combination);
    availableFunctions.put(CharConst.GC_FUNCTION_FLOOR, new InfoMethod(MethodOperator.FLOOR, CharConst.GC_FUNCTION_FLOOR, 1, 1));

    //---unary---

    //minus
    combination = new Vector<ParameterSet>();
    combination.add(new ParameterSet(sbyte, sbyte));
    combination.add(new ParameterSet(byte_, byte_));
    combination.add(new ParameterSet(int16, int16));
    combination.add(new ParameterSet(int32, int32));
    combination.add(new ParameterSet(int64, int64));
    combination.add(new ParameterSet(single, single));
    combination.add(new ParameterSet(double_, double_));
    combination.add(new ParameterSet(decimal_, decimal_));

    //minus

    //XXX_MT_FUOPS.put(CharConst.GC_OPERATOR_MINUS, combination);
    availableUnaryOperators.put(CharConst.GC_OPERATOR_MINUS, new InfoUnaryOperator(UnaryOperator.MINUS, "minus", CharConst.GC_OPERATOR_MINUS));

    //not
    //XXX_MT_FUOPS.put(CharConst.GC_OPERATOR_NOT, combination);
    availableUnaryOperators.put(CharConst.GC_OPERATOR_NOT, new InfoUnaryOperator(UnaryOperator.NOT, "not", CharConst.GC_OPERATOR_NOT));

  }

  static String getTypeName(EdmType IP_TYPE) throws EdmException
  {
    String RV_TYPENAME = IP_TYPE.getName();
    return RV_TYPENAME;
    /*TODO check for zero pointer*/
    /*
     * DATA: lx_ds_edm_error TYPE REF TO /iwcor/cx_ds_edm_error.
        if io_type IS BOUND.
          TRY.
              rv_typename = io_type->/iwcor/if_ds_edm_named~get_name( ).
            CATCH /iwcor/cx_ds_edm_error INTO lx_ds_edm_error.
              RAISE EXCEPTION TYPE /iwcor/cx_ds_internal_error "OK
                EXPORTING
                  previous = lx_ds_edm_error.
          ENDTRY.
        ENDIF.
        if rv_typename IS INITIAL.
          rv_typename = '<unknown>'.
        ENDIF.
     */
  }

  static EdmTyped getProperty(EdmType IO_RESOURCE_TYPE, Token IS_TOKEN) throws EdmException
  {
    EdmTyped RO_PROPERTY = null;

    EdmType lo_resource_type;
    EdmTyped lo_edm_nav_typed;
    EdmStructuralType lo_edm_struct_type;
    EdmTyped lo_untyped_edm_property;

    String lv_type_name;

    lo_resource_type = IO_RESOURCE_TYPE;
    lv_type_name = getTypeName(IO_RESOURCE_TYPE);

    try
    {
      //if navigation type resolve it
      if (lo_resource_type.getKind() == EdmTypeKind.NAVIGATION)
      {
        lo_edm_nav_typed = (EdmTyped) lo_resource_type;
        lo_resource_type = lo_edm_nav_typed.getType(); //should be /IWCOR/IF_DS_EDM_ENTITY_TYPE
      }

      //do typing for mo_resource_type
      if ((lo_resource_type.getKind() == EdmTypeKind.COMPLEX) || (lo_resource_type.getKind() == EdmTypeKind.ENTITY))
      {

        //only these type can have properties at the moment
        lo_edm_struct_type = (EdmStructuralType) lo_resource_type;
        lo_untyped_edm_property = lo_edm_struct_type.getProperty((String) IS_TOKEN.getUriLiteral());
      }
      else
      {
        throw new Exception();/*TODO*/
        /*   RAISE EXCEPTION TYPE /iwcor/cx_ds_expr_parser_error "OK
             EXPORTING
               position = is_property_token-position
               textid = /iwcor/cx_ds_expr_parser_error=>type_has_no_properties
               type   = lv_type_name.*/
      }

      if (lo_untyped_edm_property != null)
      {
        //do typing for property
        RO_PROPERTY = lo_untyped_edm_property;//may be /IWCOR/IF_DS_EDM_PROPERTY or /IWCOR/IF_DS_EDM_NAV_PROPERTY" +
      }
      else
      {
        throw new Exception();/*TODO*//*


                                                           RAISE EXCEPTION TYPE /iwcor/cx_ds_expr_parser_error "OK
                                                           EXPORTING
                                                           textid   = /iwcor/cx_ds_expr_parser_error=>property_not_in_type
                                                           type     = lv_type_name
                                                           property = is_property_token-value
                                                           position = is_property_token-position.
                                                           ENDIF.*/


      }
    } catch (Exception ex)
    {/*
     
     CATCH /iwcor/cx_ds_edm_error INTO lx_ds_edm_error.
       RAISE EXCEPTION TYPE /iwcor/cx_ds_internal_error "OK
         EXPORTING
           previous = lx_ds_edm_error.*/
    }

    return RO_PROPERTY;
  }

  protected void validateBinaryOperator(BinaryExpression lo_binary) throws FilterParserException
  {
    // TODO Auto-generated method stub
  }

  protected void validateMethodTypes(MethodExpression methodExpression, Token token)
  /*void VALIDATE_FUNCTION_TYPES(MethodExpression IO_EXPR_FUNCTION,
      Vector<EdmType> ET_ACTUAL_PARAMETERS,
      XXX_fuop_types_s ES_FORMAL_PARAM_COMBI)*/

  {
    /*
    String ld_string;
    CommonExpression  lo_node;
    CommonExpression      ld_funk_param_iter ;
    Vector<EdmType> lt_act_param_types;
    Vector<param_types_s>ls_param_combinations;
    //fuop_types_s ls_param_combinations;
    param_types_s ls_used_combination;
    
    boolean lv_promoted;
    boolean lv_dynamic;

    //get actual parameter types
    for (CommonExpression tmp : IO_EXPR_FUNCTION.GetParameters())
    {
      lt_act_param_types.add(ld_funk_param_iter.GetEdmType());
    }
     

     //get the possible prameter combinations
    ls_param_combinations = MT_FUOPS.get(IO_EXPR_FUNCTION.GetFunctionName());

        VALIDATE_PARAMETER_TYPES(lt_act_param_types,ls_param_combinations,  ls_used_combination,lv_promoted );

        io_expr_function->/iwcor/if_ds_expr_node~type = /iwcor/cl_ds_edm_simple_type=>get_instance( iv_name = ls_used_combination-return_type ).

        if lv_dynamic = abap_true.
          RETURN.
        ENDIF.

        "set return type
        lo_node ?= io_expr_function.
        lo_node->type = /iwcor/cl_ds_edm_simple_type=>get_instance( iv_name = ls_used_combination-return_type ).

        if  mv_parameter_promotion = abap_true AND lv_promoted = abap_true.
          LOOP AT ls_used_combination-params REFERENCE INTO ld_string.
            READ TABLE io_expr_function->parameters INDEX sy-tabix INTO ld_funk_param_iter.
            ld_funk_param_iter->type = /iwcor/cl_ds_edm_simple_type=>get_instance( iv_name = ld_string->* ).
          ENDLOOP.
        ENDIF.
     */
  }

  protected void validataUnaryOperator(UnaryExpression IO_EXPR_UNARY,
      Token token)
  {/*
   DATA:
          lv_string TYPE string,
          lt_act_param_types TYPE TABLE OF REF TO /iwcor/if_ds_edm_type WITH DEFAULT KEY,
          ls_param_combinations TYPE fuop_types_s,
          ls_used_combination TYPE param_types_s,
          lv_promoted TYPE abap_bool,
          lv_dynamic TYPE abap_bool.

        APPEND io_expr_unary->operand->type TO lt_act_param_types.

        "get the possible prameter comibnations
        READ TABLE mt_fuops WITH KEY name = io_expr_unary->operator INTO ls_param_combinations.

        validate_parameter_types(
            EXPORTING
              it_actual_parameters  = lt_act_param_types
              is_formal_param_combi = ls_param_combinations
            IMPORTING
              ev_dynamic            = lv_dynamic
              es_used_combi         = ls_used_combination
              ev_promoted           = lv_promoted ).

        io_expr_unary->/iwcor/if_ds_expr_node~type = /iwcor/cl_ds_edm_simple_type=>get_instance( iv_name = ls_used_combination-return_type ).

        if lv_dynamic = abap_true.
          RETURN.
        ENDIF.


        if  mv_parameter_promotion = abap_true AND lv_promoted = abap_true.
          READ TABLE ls_used_combination-params INDEX 1 INTO lv_string.
          io_expr_unary->operand->type = /iwcor/cl_ds_edm_simple_type=>get_instance( iv_name = lv_string ).
        ENDIF.*/}

  protected void validateParameterSet(Vector<EdmType> IT_ACTUAL_PARAMETERS,
      Vector<EdmType> ET_ACTUAL_PARAMETERS,

      boolean ev_dynamic,
      //param_types_s es_used_combi,
      boolean ev_promoted)
  {
    /*
    String lv_act;
    String lv_form;
    //String lo_string;
    boolean lv_exact_match = false;
    boolean lv_promotion_match = false;
    EdmType lo_type;
    EdmSimpleType lo_simple_type;
    EdmSimpleType lo_simple_string_type = null;
    //param_types_s ld_parameter_combi;

    for (EdmType type : IT_ACTUAL_PARAMETERS)
    {
      if (type == null)
      {
        ev_dynamic = false;
        return;
      }
    }

    //READ TABLE it_actual_parameters INDEX sy-tabix INTO lo_type.

    try {
      //loop at possible parameter combinations and check if an exact match exists" +
      for (param_types_s ld_parameter_combi : IS_FORMAL_PARAM_COMBI.combination)
      {
        lv_exact_match = true;
        //"loop at paramneters in the combination
        for (int index = 0; index < ld_parameter_combi.params.size(); index++)
        {
          String lo_string = ld_parameter_combi.params.elementAt(index);
          //get the aktual param type
          lo_type = IT_ACTUAL_PARAMETERS.elementAt(index);

          lv_act = lo_type.getName();
          lv_form = lo_string;

          if (lo_string != lo_type.getName())
          {
            lv_exact_match = false;
            break;
          }
        }

        if (lv_exact_match == true)
        {
          es_used_combi = ld_parameter_combi;
          ev_promoted = false;
          return;
        }
      }

      //loop at possible parameter combinations and check if the parameter can be promoted" +
      for (param_types_s ld_parameter_combi : IS_FORMAL_PARAM_COMBI.combination)
      {
        lv_promotion_match = true;

        //loop at params in combination
        for (int index = 0; index < ld_parameter_combi.params.size(); index++)
        {
          String lo_string = ld_parameter_combi.params.elementAt(index);
          //get the aktual param type
          lo_type = IT_ACTUAL_PARAMETERS.elementAt(index);

          try {
            //EdmSimpleTypeFacade edmTypeFacade = new EdmSimpleTypeFacade();
            lo_simple_type = (EdmSimpleType) lo_type;
            //lo_simple_string_type = edmTypeFacade.dateTimeOffsetInstance()  lo_string);
            if (lo_simple_string_type.isCompatible(lo_simple_type) == false)
            {
              lv_promotion_match = false;
              break;
            }

          } catch (Exception ex)
          {
            throw ex;*/
    /*TODO
     * 
     * RAISE EXCEPTION TYPE /iwcor/cx_ds_expr_syntax_error
      EXPORTING
        textid   = /iwcor/cx_ds_expr_syntax_error=>function_invalid_param_type
        function = is_formal_param_combi-name.*//*

                                                }
                                                }

                                                if (lv_promotion_match == true)
                                                {
                                                es_used_combi = ld_parameter_combi;
                                                ev_promoted = true;
                                                return;
                                                }

                                                }
                                                } catch (Exception ex)
                                                { *//* TODO
                                                      CATCH /iwcor/cx_ds_edm_error INTO lo_ds_edm_error.
                                                        RAISE EXCEPTION TYPE /iwcor/cx_ds_internal_error
                                                          EXPORTING
                                                            textid   = /iwcor/cx_ds_internal_error=>/iwcor/cx_ds_internal_error
                                                            previous = lo_ds_edm_error.*//*

                                                                                         }
                                                                                         /*TODO
                                                                                         RAISE EXCEPTION TYPE /iwcor/cx_ds_expr_syntax_error
                                                                                         EXPORTING
                                                                                         textid   = /iwcor/cx_ds_expr_syntax_error=>function_invalid_param_type
                                                                                         function = is_formal_param_combi-name.
                                                                                         */
  }

  

  protected void validatePropertyTypes(PropertyExpression property) {
    // TODO Auto-generated method stub

  }
}
