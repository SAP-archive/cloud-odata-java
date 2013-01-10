package com.sap.core.odata.core.uri.expression;

import org.junit.Test;

import com.sap.core.odata.api.uri.expression.FilterParserException;

public class TestParserExceptions extends TestBase {
  @Test
  public void TestPMreadParameters()
  {

    //TODO add tests for

    //OK
    GetPTF("concat('A','B')").aSerialized("{concat('A','B')}");

    //CASE 12
    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=startswith()
    //-->No applicable function found for 'startswith' at position 0 with the specified arguments. The functions considered are: startswith(System.String, System.String).
    GetPTF("startswith()").aExType(FilterParserException.class)
        .aExMsgText("No applicable method found for \"startswith\" at position 1 in \"startswith()\" with the specified arguments. Method \"startswith\" requires exact 2 argument(s).");

    //CASE 13
    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=startswith('A')
    //-->No applicable function found for 'startswith' at position 0 with the specified arguments. The functions considered are: startswith(System.String, System.String).
    GetPTF("startswith('A')").aExType(FilterParserException.class)
        .aExMsgText("No applicable method found for \"startswith\" at position 1 in \"startswith('A')\" with the specified arguments. Method \"startswith\" requires exact 2 argument(s).");

    //CASE 14
    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=startswith('A','B')
    //-->Resource not found for the segment 'Supplier'.
    GetPTF("startswith('A','B')").aSerialized("{startswith('A','B')}");

    //CASE 15
    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=startswith('A','B','C')
    //-->No applicable function found for 'startswith' at position 0 with the specified arguments. The functions considered are: startswith(System.String, System.String).
    GetPTF("startswith('A','B','C')").aExType(FilterParserException.class)
        .aExMsgText("No applicable method found for \"startswith\" at position 1 in \"startswith('A','B','C')\" with the specified arguments. Method \"startswith\" requires exact 2 argument(s).");

    //CASE 16
    GetPTF("concat()")
        .aExMsgText("No applicable method found for \"concat\" at position 1 in \"concat()\" with the specified arguments. Method \"concat\" requires 2 or more arguments.");
    //CASE 17
    GetPTF("concat('A')")
        .aExMsgText("No applicable method found for \"concat\" at position 1 in \"concat('A')\" with the specified arguments. Method \"concat\" requires 2 or more arguments.");
    //CASE 18
    GetPTF("concat('A','B')")
        .aSerialized("{concat('A','B')}");
    //CASE 19
    GetPTF("concat('A','B','C')")
        .aSerialized("{concat('A','B','C')}");

    //CASE 20
    GetPTF("'A' and concat('A')")
        .aExMsgText("No applicable method found for \"concat\" at position 9 in \"'A' and concat('A')\" with the specified arguments. Method \"concat\" requires 2 or more arguments.");

    //CASE 1
    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=concat(
    //-->Expression expected at position 7.
    GetPTF("concat(").aExType(FilterParserException.class)
        .aExMsgText("Expression expected after position 7.");

    //CASE 2
    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=concat(123
    //-->')' or ',' expected at position 10.
    GetPTF("concat(123").aExType(FilterParserException.class)
        .aExMsgText("\")\" or \",\" expected after position 10.");
    //.aExMsgText("Missing closing parenthesis \")\" for opening parenthesis \"(\" at position 7 in \"concat(\".");

    //CASE 3 
    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=concat(,
    //Expression expected at position 7.
    GetPTF("concat(,").aExType(FilterParserException.class)
        .aExMsgText("Expression expected at position 8.");

    //CASE 4
    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=concat(123,
    //-->Expression expected at position 11.
    GetPTF("concat(123,").aExType(FilterParserException.class)
        .aExMsgText("Expression expected after position 11.");

    //CASE 5
    //min = -1, max = -1, 
    GetPTF("testingMINMAX1()").aSerialized("{concat()}");
    GetPTF("testingMINMAX1('A')").aSerialized("{concat('A')}");
    GetPTF("testingMINMAX1('A','B')").aSerialized("{concat('A','B')}");
    GetPTF("testingMINMAX1('A','B','C')").aSerialized("{concat('A','B','C')}");

    //CASE 6
    //min = 0, max = -1, 
    GetPTF("testingMINMAX2()").aSerialized("{concat()}");
    GetPTF("testingMINMAX2('A')").aSerialized("{concat('A')}");
    GetPTF("testingMINMAX2('A','B')").aSerialized("{concat('A','B')}");
    GetPTF("testingMINMAX2('A','B','C')").aSerialized("{concat('A','B','C')}");

    //CASE 7
    //min = 2, max = -1, 
    GetPTF("testingMINMAX3()").aExType(FilterParserException.class)
        .aExMsgText("No applicable method found for \"concat\" at position 1 in \"testingMINMAX3()\" with the specified arguments. Method \"concat\" requires 2 or more arguments.");
    GetPTF("testingMINMAX3('A')").aExType(FilterParserException.class)
        .aExMsgText("No applicable method found for \"concat\" at position 1 in \"testingMINMAX3('A')\" with the specified arguments. Method \"concat\" requires 2 or more arguments.");
    GetPTF("testingMINMAX3('A','B')").aSerialized("{concat('A','B')}");
    GetPTF("testingMINMAX3('A','B','C')").aSerialized("{concat('A','B','C')}");

    //CASE 8
    //min =-1, max = 0, 
    GetPTF("testingMINMAX4()").aSerialized("{concat()}");
    GetPTF("testingMINMAX4('A')").aExType(FilterParserException.class)
        .aExMsgText("No applicable method found for \"concat\" at position 1 in \"testingMINMAX4('A')\" with the specified arguments. Method \"concat\" requires maximal 0 arguments.");
    GetPTF("testingMINMAX4('A','B')").aExType(FilterParserException.class)
        .aExMsgText("No applicable method found for \"concat\" at position 1 in \"testingMINMAX4('A','B')\" with the specified arguments. Method \"concat\" requires maximal 0 arguments.");
    GetPTF("testingMINMAX4('A','B','C')").aExType(FilterParserException.class)
        .aExMsgText("No applicable method found for \"concat\" at position 1 in \"testingMINMAX4('A','B','C')\" with the specified arguments. Method \"concat\" requires maximal 0 arguments.");

    //CASE 9
    //min =-1, max = 2, 
    GetPTF("testingMINMAX5()").aSerialized("{concat()}");
    GetPTF("testingMINMAX5('A')").aSerialized("{concat('A')}");
    GetPTF("testingMINMAX5('A','B')").aSerialized("{concat('A','B')}");
    GetPTF("testingMINMAX5('A','B','C')").aExType(FilterParserException.class)
        .aExMsgText("No applicable method found for \"concat\" at position 1 in \"testingMINMAX5('A','B','C')\" with the specified arguments. Method \"concat\" requires maximal 2 arguments.");

    //CASE 10
    // min =1, max = 2, 
    GetPTF("testingMINMAX6()").aExType(FilterParserException.class)
        .aExMsgText("No applicable method found for \"concat\" at position 1 in \"testingMINMAX6()\" with the specified arguments. Method \"concat\" requires between 1 and 2 arguments.");
    GetPTF("testingMINMAX6('A')").aSerialized("{concat('A')}");
    GetPTF("testingMINMAX6('A','B')").aSerialized("{concat('A','B')}");
    GetPTF("testingMINMAX6('A','B','C')").aExType(FilterParserException.class)
        .aExMsgText("No applicable method found for \"concat\" at position 1 in \"testingMINMAX6('A','B','C')\" with the specified arguments. Method \"concat\" requires between 1 and 2 arguments.");

    //CASE 11
    // min =1, max = 2, 
    GetPTF("testingMINMAX7()").aExType(FilterParserException.class)
        .aExMsgText("No applicable method found for \"concat\" at position 1 in \"testingMINMAX7()\" with the specified arguments. Method \"concat\" requires exact 1 argument(s).");
    GetPTF("testingMINMAX7('A')").aSerialized("{concat('A')}");
    GetPTF("testingMINMAX7('A','B')").aExType(FilterParserException.class)
        .aExMsgText("No applicable method found for \"concat\" at position 1 in \"testingMINMAX7('A','B')\" with the specified arguments. Method \"concat\" requires exact 1 argument(s).");
    GetPTF("testingMINMAX7('A','B','C')").aExType(FilterParserException.class)
        .aExMsgText("No applicable method found for \"concat\" at position 1 in \"testingMINMAX7('A','B','C')\" with the specified arguments. Method \"concat\" requires exact 1 argument(s).");

  }

  @Test
  public void TestPMreadParenthesis()
  {
    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=(123
    //-->')' or operator expected at position 4.
    GetPTF("(123").aExType(FilterParserException.class)
        .aExMsgText("Missing closing parenthesis \")\" for opening parenthesis \"(\" at position 1 in \"(123\".");

    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=(123 add (456
    //-->')' or operator expected at position 13.
    GetPTF("(123 add (456").aExType(FilterParserException.class)
        .aExMsgText("Missing closing parenthesis \")\" for opening parenthesis \"(\" at position 10 in \"(123 add (456\".");

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
        .aExMsgText("Invalid token \"abc\" detected after parsing at position 5 in \"abc abc\".");
  }
}
