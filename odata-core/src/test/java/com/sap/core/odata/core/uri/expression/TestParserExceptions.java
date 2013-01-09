package com.sap.core.odata.core.uri.expression;

import org.junit.Test;

import com.sap.core.odata.api.uri.expression.FilterParserException;

public class TestParserExceptions extends TestBase {
  
  @Test
  public void TestPMreadParenthesis()
  {
    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=(123
    //-->')' or operator expected at position 4.
    GetPTF("(123").aExType(FilterParserException.class)
    .aExMsgText("Missing closing parenthesis \")\" for opening parenthesis \"(\" at position  1 in \"(123\".");
    
  }
  
  @Test
  public void TestPMvalidateBinaryOperator() /*PM = Parsermethod*/
  {
    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=123 add 'abc'
    //-->Operator 'add' incompatible with operand types 'System.Int32' and 'System.String' at position 4.
    GetPTF("123 add 'abc'").aExType(FilterParserException.class)
        .aExKey(FilterParserException.INVALID_TYPES_FOR_BINARY_OPERATOR)
        .aExMsgText("Operator \"add\" incompatible with operand types \"System.Uint7\" and \"System.Uint7\" at position 5 in \"123 add 'abc'\".");
  }

  @Test
  public void TestPMparseFilterString() /*PM = Parsermethod*/
  {
    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter='123
    //-->Unterminated string literal at position 4 in ''123'.
    GetPTF("'123").aExType(FilterParserException.class)
        .aExKey(FilterParserException.TOKEN_UNDETERMINATED_STRING)
        .aExMsgText("Unterminated string literal at position 1 in \"'123\".");

    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=1%20add%20'123
    //-->Unterminated string literal at position 10 in '1 add '123'.
    GetPTF("1 add '123").aExType(FilterParserException.class)
        .aExKey(FilterParserException.TOKEN_UNDETERMINATED_STRING)
        .aExMsgText("Unterminated string literal at position 7 in \"1 add '123\".");

    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=datetime'123
    //-->Unterminated literal at position 12 in 'datetime'123'.
    GetPTF("datetime'123").aExType(FilterParserException.class)
        .aExKey(FilterParserException.TOKEN_UNDETERMINATED_STRING)
        .aExMsgText("Unterminated string literal at position 9 in \"datetime'123\".");

    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=123%20456
    //-->Expression of type 'System.Boolean' expected at position 0.
    GetPTF("123 456").aExType(FilterParserException.class)
        .aExKey(FilterParserException.INVALID_TRAILING_TOKEN_DETECTED_AFTER_PARSING)
        .aExMsgText("Invalid token \"456\" detected after parsing at position 4 in \"123 456\".");

    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=123%20456
    //-->Expression of type 'System.Boolean' expected at position 0.
    GetPTF("123 456 789").aExType(FilterParserException.class)
        .aExKey(FilterParserException.INVALID_TRAILING_TOKEN_DETECTED_AFTER_PARSING)
        .aExMsgText("Invalid token \"456\" detected after parsing at position 4 in \"123 456 789\".");

    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=abc%20abc
    //-->No property 'abc' exists in type 'ODataWeb.Northwind.Model.Supplier' at position 0.
    GetPTF("abc abc").aExType(FilterParserException.class)
        .aExKey(FilterParserException.INVALID_TRAILING_TOKEN_DETECTED_AFTER_PARSING)
        .aExMsgText("Invalid token \"abc\" detected after parsing at position 4 in \"abc abc\".");
  }
}
