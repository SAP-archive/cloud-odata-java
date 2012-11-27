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
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmStructuralType;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.uri.expression.BinaryExpression;
import com.sap.core.odata.api.uri.expression.CommonExpression;

import com.sap.core.odata.api.uri.expression.BinaryOperator;
import com.sap.core.odata.api.uri.expression.FunctionExpression;
import com.sap.core.odata.api.uri.expression.LiteralExpression;
import com.sap.core.odata.api.uri.expression.MemberExpression;
import com.sap.core.odata.api.uri.expression.MethodOperator;
import com.sap.core.odata.api.uri.expression.UnaryOperator;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.FilterParser;
import com.sap.core.odata.api.uri.expression.MethodExpression;

import com.sap.core.odata.api.uri.expression.ExpressionException;
import com.sap.core.odata.api.uri.expression.PropertyExpression;
import com.sap.core.odata.api.uri.expression.UnaryExpression;
import com.sap.core.odata.core.edm.EdmSimpleTypeFacadeImpl;

public class FilterParserImpl implements FilterParser
{
  static Map<String, InfoBinaryOperator> availableBinaryOperators;
  static Map<String, InfoMethod> availableFunctions;
  static Map<String, InfoUnaryOperator> availableUnaryOperators;

  protected boolean useParameterPromotiom;
  protected Edm edm;
  protected EdmType resourceEntityType;

  CommonExpression MO_EXPR_NODE;
  TokenList tokenList = null;

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
  public FilterExpression ParseExpression(String filterExpression) throws ExpressionException
  {
    try
    {
      Tokenizer tokenizer = new Tokenizer();
      tokenList = tokenizer.tokenize(filterExpression); //throws TokenizerMessage

      //if token list is empty
      if (!tokenList.hasTokens())
        return new FilterExpressionImpl(filterExpression, null);

      CommonExpression nodeLeft = readElement();
      CommonExpression node = readElements(nodeLeft, 0);

      //post check
      //TODO verify if is an internal error or an user error
      //E.g. Test "a eq b b" or " a b"
      if (tokenList.tokenCount() >= tokenList.currentToken)
        throw new ExpressionException(ExpressionException.INVALID_TRAILING_TOKEN, null);

      //create and return filterExpression node
      return new FilterExpressionImpl(filterExpression, node);
    } catch (ExpressionException expressionException)
    {
      throw new ExpressionException(ExpressionException.INVALID_TRAILING_TOKEN, null);
    } catch (TokenizerMessage e) {
      //TODO
    }
    return null;
  }

  public void setUseParameterPromotiom(boolean useParameterPromotiom) {
    this.useParameterPromotiom = useParameterPromotiom;
  }

  Token expectToken(String expected) throws ExpressionException
  {
    Token rs_token = tokenList.GetToken();
    if (rs_token.getUriLiteral() != expected)
    {
      throw new ParserExceptionSyntax(0);
      //TODO add text for /iwcor/cx_ds_expr_syntax_error=>unexpected_token
    }
    return rs_token;
  }

  InfoBinaryOperator LookBinaerOperator()
  {
    Token token = tokenList.lookToken();

    if (token == null)
      return null;

    return availableBinaryOperators.get(token.getUriLiteral());
  }

  CommonExpression readElements(CommonExpression leftExpression, int priority) throws ExpressionException, TokenizerMessage
  {
    CommonExpression lo_tmp_node;
    CommonExpression leftNode;
    CommonExpression rightNode;
    BinaryExpression lo_binary;

    //lo_expression_error TYPE REF TO /iwcor/cx_ds_expr_error;

    leftNode = leftExpression;
    InfoBinaryOperator lv_op1 = LookBinaerOperator();
    
    while ((lv_op1 != null) && (lv_op1.priority >= priority))
    {
      tokenList.next();
      rightNode = readElement();
      
      InfoBinaryOperator lv_op2 = LookBinaerOperator();
          
      if  ((lv_op2 != null) && (lv_op2.priority > lv_op1.priority ))
      {
        rightNode = readElements(rightNode, lv_op2.priority);//op2 is read in read_elements.
        lv_op2 = LookBinaerOperator();
      }
      
      lo_binary = new BinaryExpressionImpl(lv_op1, leftNode, rightNode);

      try
      {
        VALIDATE_BINARY_TYPES(lo_binary);
      } catch (Exception ex)
      {
        //TODO
      }

      lo_tmp_node = lo_binary;

      leftNode = lo_tmp_node;

      lv_op1 = LookBinaerOperator();
    }

    /*
    ro_node = lo_left_node.
    */

    return leftNode;
  }

  private void VALIDATE_BINARY_TYPES(BinaryExpression lo_binary) {
    // TODO Auto-generated method stub

  }

  /**
   * Reads a Parenthesis expression. Its is expected that the current token is of kind {@link TokenKind#OPENPAREN} 
   * @return
   * @throws ExpressionException
   * @throws TokenizerMessage 
   */
  CommonExpression readParenthesis() throws ExpressionException, TokenizerMessage
  {
    tokenList.expectToken(TokenKind.OPENPAREN);

    CommonExpression expression = readElement();
    //CommonExpression node = readElements(expression, 0);

    tokenList.expectToken(TokenKind.CLOSEPAREN);

    return null;//TODO
  }

  MethodExpression readParameters(InfoMethod IS_FUNC, MethodExpression methodExpression) throws ExpressionException, TokenizerMessage

  {
    CommonExpression ls_tmp_node;
    boolean lv_done = false;
    Token lv_token;
    int lv_pcount;
    CommonExpression lo_node;
    FunctionExpression ro_node = null;

    expectToken(Character.toString(CharConst.GC_STR_OPENPAREN));

    while (lv_done == false)
    {
      lo_node = readElement();
      //ls_tmp_node = readElements(lo_node, 0);
      if (lo_node != null) //parameter list may be emty
      {
        ro_node.AppendParameter(lo_node);
      }
      lv_token = tokenList.lookToken();

      if (lv_token.getKind() == TokenKind.COMMA)

      {
        expectToken(Character.toString(CharConst.GC_STR_COMMA));
      }

      else if (!(lv_token.getKind() == TokenKind.CLOSEPAREN))
      {
        lv_done = true;
      }
      else
      {
        //throw new Exception();///iwcor/cx_ds_expr_syntax_error=>function_invalid_parameter
      }
    } //end while

    lv_pcount = ro_node.GetParameters().size();
    if (lv_pcount < IS_FUNC.getMinParameter())
    {
      //TODO raise exception /iwcor/cx_ds_expr_syntax_error=>function_to_few_parameter
    }

    if ((IS_FUNC.getMaxParameter() > -1) && (lv_pcount > IS_FUNC.getMinParameter()))
    {
      //TODO raise exception /iwcor/cx_ds_expr_syntax_error=>function_to_many_parameter
    }

    expectToken(Character.toString(CharConst.GC_STR_CLOSEPAREN));

    return null; //TODO fix
  }

  /**
   * Reads: Unary operators, Methods, Properties, ...
   * but not binary operators which are handelt in {@link #readElements(CommonExpression, int)}
   * @return
   * @throws ExpressionException
   * @throws TokenizerMessage 
   */
  CommonExpression readElement() throws ExpressionException, TokenizerMessage
  {
    CommonExpression node = null;
    Token token;
    Token lookToken;
    lookToken = tokenList.lookToken();

    if (lookToken.getKind() == TokenKind.OPENPAREN) //."if token a '(' then process read a new note for the '(' and return it.
    {
      node = readParenthesis();
      return node;
    }
    else if (lookToken.getKind() == TokenKind.CLOSEPAREN)// " ')'  finishes a pharenthesiz (it is no extra token)" +
    {
      return node;
    }

    else if (lookToken.getKind() == TokenKind.COMMA)//. " ','  is a separator for function parameter (it is no extra token)" +

    {
      return node;
    }

    //-->Check if the token is a unary operator
    Object operator = isUnaryOperator(lookToken);
    if (operator != null)
    {

      token = tokenList.expectToken(lookToken.getUriLiteral());
      CommonExpression operand = readElement();
      UnaryExpression unaryExpression = new UnaryExpressionImpl((InfoUnaryOperator) operator, operand);
      validateUnaryTypes(unaryExpression, token); //throws ExpressionInvalidOperatorTypeException
      return unaryExpression;
    }

    //-->Check if the token is a method 
    //To avoid name clashes between method names and property names we accept here only method names if a "(" follows.
    //Hence the parser accepts a property named "concat" 
    token = tokenList.expectToken(lookToken.getUriLiteral());
    lookToken = tokenList.lookToken();

    operator = isMethod(token, lookToken);
    if (operator != null)
    {
      //check for function if the next token is a '('
      MethodExpression method = new MethodExpressionImpl((InfoMethod) operator);
      readParameters((InfoMethod) operator, method);
      validateMethodTypes(method, token); //throws ExpressionInvalidOperatorTypeException
      return method;
    }
    if ((lookToken != null) && (lookToken.getKind() == TokenKind.OPENPAREN))
    {
      //TODO error '(' without function name ahead
    }

    //-->Check if token is a terminal 
    //is a terminal e.g. a Value like an EDM.String 'hugo' or  125L or 1.25D" 
    if (token.getKind() == TokenKind.SIMPLE_TYPE)
    {
      LiteralExpression literal = new LiteralExpressionImpl(token.getUriLiteral(), token.getJavaLiteral());
      return literal;
    }

    //-->Check if token is a property
    if (token.getKind() == TokenKind.LITERAL)
    {
      node = new PropertyExpressinImpl(token.getUriLiteral());
    }

    //" e.g. "name" or "adress"
    if ((this.edm != null) && (this.resourceEntityType != null))
    {
      PropertyExpression property = new PropertyExpressinImpl(token.getUriLiteral());
      validatePropertyTypes(property);
    }

    // "resource type could be an /IWCOR/IF_DS_EDM_STRUCT_TYPE
    //ERROR

    return node;

  }

  private void validatePropertyTypes(PropertyExpression property) {
    // TODO Auto-generated method stub

  }

  private InfoMethod isMethod(Token token, Token lookToken)
  {
    if ((lookToken != null) && (lookToken.getKind() == TokenKind.OPENPAREN))
    {
      return availableFunctions.get(token.getUriLiteral());
    }
    return null;
  }

  @SuppressWarnings("unused")
  private void validateUnaryTypes(UnaryExpression unaryExpression, Token token) throws ExpressionInvalidOperatorTypeException {
    //TODO check types 
  }

  private void VALIDATE_FUNCTION_TYPES(FunctionExpression lo_function) {
    // TODO Auto-generated method stub

  }

  CommonExpression readElementForMember(PropertyExpression io_parent_property) throws Exception
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
        return new PropertyExpressinImpl(lv_token.getUriLiteral());
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
      ro_node = new PropertyExpressinImpl(lv_token.getUriLiteral(), (EdmTyped) lo_edm_prop, lv_token.getUriLiteral());

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

  InfoUnaryOperator isUnaryOperator(Token IS_TOKEN)
  {
    if (IS_TOKEN.getKind() == TokenKind.LITERAL)
    {

      InfoUnaryOperator operator = availableUnaryOperators.get(IS_TOKEN.getUriLiteral());
      return operator;

    }
    return null;
  }

  static void addOneParameter()
  {
    /*
     * 
            DEFINE add_one_parameter.
              clear ls_param_type.
              ls_param_type-return_type = /iwcor/cl_ds_edm_simple_type=>gc_name_&1.
              append /iwcor/cl_ds_edm_simple_type=>gc_name_&2 to ls_param_type-params.
              append ls_param_type to lt_param_type.
            END-OF-DEFINITION.

            DEFINE add_two_parameters.
              clear ls_param_type.
              ls_param_type-return_type = /iwcor/cl_ds_edm_simple_type=>gc_name_&1.
              append /iwcor/cl_ds_edm_simple_type=>gc_name_&2 to ls_param_type-params.
              append /iwcor/cl_ds_edm_simple_type=>gc_name_&3 to ls_param_type-params.
              append ls_param_type to lt_param_type.
            END-OF-DEFINITION.

            DEFINE add_three_parameters.
              clear ls_param_type.
              ls_param_type-return_type = /iwcor/cl_ds_edm_simple_type=>gc_name_&1.
              append /iwcor/cl_ds_edm_simple_type=>gc_name_&2 to ls_param_type-params.
              append /iwcor/cl_ds_edm_simple_type=>gc_name_&3 to ls_param_type-params.
              append /iwcor/cl_ds_edm_simple_type=>gc_name_&4 to ls_param_type-params.
              append ls_param_type to lt_param_type.
            END-OF-DEFINITION.
     */
  }

  static void InitAvialTables()
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

  static String GetTypeName(EdmType IP_TYPE) throws EdmException
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
    lv_type_name = GetTypeName(IO_RESOURCE_TYPE);

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

  void validateMethodTypes(MethodExpression method, Token token)
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

  void VALIDATE_BINARY_TYPES(BinaryExpression IO_EXPR_BINARY,
      Vector<EdmType> ET_ACTUAL_PARAMETERS
      )
  {
    /*#
     * DATA:
          lv_string TYPE string,
          lt_act_param_types TYPE TABLE OF REF TO /iwcor/if_ds_edm_type WITH DEFAULT KEY,
          ls_param_combinations TYPE fuop_types_s,
          ls_used_combination TYPE param_types_s,
          lv_promoted TYPE abap_bool,
          lv_dynamic TYPE abap_bool.
        APPEND io_expr_binary->left_operand->type TO lt_act_param_types .
        APPEND io_expr_binary->right_operand->type TO lt_act_param_types .

        "get the possible prameter comibnations
        READ TABLE mt_fuops WITH KEY name = io_expr_binary->operator INTO ls_param_combinations.

        validate_parameter_types(
            EXPORTING
              it_actual_parameters  = lt_act_param_types
              is_formal_param_combi = ls_param_combinations
            IMPORTING
              es_used_combi         = ls_used_combination
              ev_promoted           = lv_promoted ).

        io_expr_binary->/iwcor/if_ds_expr_node~type = /iwcor/cl_ds_edm_simple_type=>get_instance( iv_name = ls_used_combination-return_type ).

        if lv_dynamic = abap_true.
          RETURN.
        ENDIF.

        if  mv_parameter_promotion = abap_true AND lv_promoted = abap_true.
          READ TABLE ls_used_combination-params INDEX 1 INTO lv_string.
          io_expr_binary->left_operand->type = /iwcor/cl_ds_edm_simple_type=>get_instance( iv_name = lv_string ).

          READ TABLE ls_used_combination-params INDEX 2 INTO lv_string.
          io_expr_binary->right_operand->type = /iwcor/cl_ds_edm_simple_type=>get_instance( iv_name = lv_string ).
        ENDIF.
     */
  }

  void VALIDATE_UNARY_TYPES(BinaryExpression IO_EXPR_UNARY,
      Vector<EdmType> ET_ACTUAL_PARAMETERS)
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

  void VALIDATE_PARAMETER_TYPES(Vector<EdmType> IT_ACTUAL_PARAMETERS,
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

  /*
        class-methods ATTACH_ERROR_INFORMATION
      importing
        !IO_NODE type ref to /IWCOR/IF_DS_EXPR_NODE
        !IV_POSITION type I
        !IO_EXCEPTION type ref to CX_ROOT
        !IV_TEXT type STRING optional .
        if io_node IS BOUND.
        CREATE DATA io_node->error_information.
        io_node->error_information->position = iv_position.
        io_node->error_information->exception = io_exception.
        io_node->error_information->text = iv_text.
      ENDIF.
    ENDCLASS.

  */

  static
  {
    InitAvialTables();
  }

}
