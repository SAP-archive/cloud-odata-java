/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.core.uri.expression;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.ExpressionParserException;

/**
 * @author SAP AG
 */
public class TestParserExceptions extends TestBase {

  @Test
  public void testOPMparseOrderByString()
  {
    EdmEntityType edmEtAllTypes = edmInfo.getTypeEtAllTypes();

    //CASE 1
    //http://services.odata.org/Northwind/Northwind.svc/Products/?$orderby=UnitPrice%20ascc
    //-->Syntax error at position 10.
    GetPTO(edmEtAllTypes, "String ascc")
        .aExMsgText("Invalid sort order in OData orderby parser at position 8 in \"String ascc\".");

    //CASE 2
    //http://services.odata.org/Northwind/Northwind.svc/Products/?$orderby=UnitPrice%20asc,
    //-->Expression expected at position 12.
    GetPTO(edmEtAllTypes, "String asc,")
        .aExMsgText("Expression expected after position 11 in \"String asc,\".");

    //CASE 3
    //http://services.odata.org/Northwind/Northwind.svc/Products/?$orderby=UnitPrice%20asc%20d
    //-->Syntax error at position 14.
    GetPTO(edmEtAllTypes, "String asc a")
        .aExMsgText("Comma or end of expression expected at position 12 in \"String asc a\".");

    //CASE 4
    //http://services.odata.org/Northwind/Northwind.svc/Products/?$orderby=UnitPrice b
    //-->Syntax error at position 10.
    GetPTO(edmEtAllTypes, "String b")
        .aExMsgText("Invalid sort order in OData orderby parser at position 8 in \"String b\".");

    //CASE 5
    //http://services.odata.org/Northwind/Northwind.svc/Products/?$orderby=UnitPrice, UnitPrice b
    //-->Syntax error at position 21.
    GetPTO(edmEtAllTypes, "String, String b")
        .aExMsgText("Invalid sort order in OData orderby parser at position 16 in \"String, String b\".");

    //CASE 6
    //http://services.odata.org/Northwind/Northwind.svc/Products/?$orderby=UnitPrice a, UnitPrice desc
    //-->Syntax error at position 10.
    GetPTO(edmEtAllTypes, "String a, String desc")
        .aExMsgText("Invalid sort order in OData orderby parser at position 8 in \"String a, String desc\".");

    //CASE 7
    //http://services.odata.org/Northwind/Northwind.svc/Products/?$orderby=UnitPrice asc, UnitPrice b
    //-->Syntax error at position 25.
    GetPTO(edmEtAllTypes, "String asc, String b")
        .aExMsgText("Invalid sort order in OData orderby parser at position 20 in \"String asc, String b\".");

  }

  @Test
  public void testPMvalidateEdmProperty()
  {
    EdmEntityType edmEtAllTypes = edmInfo.getTypeEtAllTypes();

    //OK
    GetPTF(edmEtAllTypes, "'text' eq String")
        .aKind(ExpressionKind.BINARY)
        .aSerialized("{'text' eq String}");

    //CASE 1
    //http://services.odata.org/Northwind/Northwind.svc/Products/?$filter=NotAProperty
    //-->No property 'NotAProperty' exists in type 'ODataWeb.Northwind.Model.Product' at position 10.
    GetPTF(edmEtAllTypes, "NotAProperty")
        .aExMsgText("No property \"NotAProperty\" exists in type \"TecRefScenario.EtAllTypes\" at position 1 in \"NotAProperty\".");

    //CASE 2
    //http://services.odata.org/Northwind/Northwind.svc/Products/?$filter='text'%20eq%20NotAProperty
    //-->No property 'NotAProperty' exists in type 'ODataWeb.Northwind.Model.Product' at position 10.
    GetPTF(edmEtAllTypes, "'text' eq NotAProperty")
        .aExMsgText("No property \"NotAProperty\" exists in type \"TecRefScenario.EtAllTypes\" at position 11 in \"'text' eq NotAProperty\".");

    //CASE 3
    GetPTF(edmEtAllTypes, "Complex/NotAProperty")
        .aExMsgText("No property \"NotAProperty\" exists in type \"TecRefScenario.CtAllTypes\" at position 9 in \"Complex/NotAProperty\".");

    //CASE 4
    GetPTF(edmEtAllTypes, "'text' eq Complex/NotAProperty")
        .aExMsgText("No property \"NotAProperty\" exists in type \"TecRefScenario.CtAllTypes\" at position 19 in \"'text' eq Complex/NotAProperty\".");

    //CASE 5
    GetPTF(edmEtAllTypes, "String/NotAProperty")
        .aExMsgText("No property \"NotAProperty\" exists in type \"Edm.String\" at position 8 in \"String/NotAProperty\".");

    //CASE 6
    //http://services.odata.org/Northwind/Northwind.svc/Products/?$filter='aText'/NotAProperty
    //-->Exception Stack
    GetPTF(edmEtAllTypes, "'aText'/NotAProperty")
        .aExMsgText("Leftside of method operator at position 8 is not a property in \"'aText'/NotAProperty\".");

    //CASE 7
    //http://services.odata.org/Northwind/Northwind.svc/Products/?$filter='Hong Kong' eq ProductName/city
    //--> No property 'city' exists in type 'System.String' at position 27. 
    GetPTF(edmEtAllTypes, "'Hong Kong' eq DateTime/city")
        .aExMsgText("No property \"city\" exists in type \"Edm.DateTime\" at position 25 in \"'Hong Kong' eq DateTime/city\".");

    //CASE 8
    //http://services.odata.org/Northwind/Northwind.svc/Products/?$filter='Hong Kong' eq ProductName/city
    //--> No property 'city' exists in type 'System.String' at position 27. 
    GetPTF(edmEtAllTypes, "'Hong Kong' eq String/city")
        .aExMsgText("No property \"city\" exists in type \"Edm.String\" at position 23 in \"'Hong Kong' eq String/city\".");

  }

  @Test
  public void testPMreadParameters()
  {

    //OK
    GetPTF("concat('A','B')").aSerialized("{concat('A','B')}");

    //CASE 12
    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=startswith()
    //-->No applicable function found for 'startswith' at position 0 with the specified arguments. The functions considered are: startswith(System.String, System.String).
    GetPTF("startswith()").aExType(ExpressionParserException.class)
        .aExMsgText("No applicable method found for \"startswith\" at position 1 in \"startswith()\" with the specified arguments. Method \"startswith\" requires exact 2 argument(s).");

    //CASE 13
    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=startswith('A')
    //-->No applicable function found for 'startswith' at position 0 with the specified arguments. The functions considered are: startswith(System.String, System.String).
    GetPTF("startswith('A')").aExType(ExpressionParserException.class)
        .aExMsgText("No applicable method found for \"startswith\" at position 1 in \"startswith('A')\" with the specified arguments. Method \"startswith\" requires exact 2 argument(s).");

    //CASE 14
    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=startswith('A','B')
    //-->Resource not found for the segment 'Supplier'.
    GetPTF("startswith('A','B')").aSerialized("{startswith('A','B')}");

    //CASE 15
    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=startswith('A','B','C')
    //-->No applicable function found for 'startswith' at position 0 with the specified arguments. The functions considered are: startswith(System.String, System.String).
    GetPTF("startswith('A','B','C')").aExType(ExpressionParserException.class)
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
    GetPTF("concat(").aExType(ExpressionParserException.class)
        .aExMsgText("Expression expected after position 7 in \"concat(\".");

    //CASE 2
    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=concat(123
    //-->')' or ',' expected at position 10.
    GetPTF("concat(123").aExType(ExpressionParserException.class)
        .aExMsgText("\")\" or \",\" expected after position 10 in \"concat(123\".");
    //.aExMsgText("Missing closing parenthesis \")\" for opening parenthesis \"(\" at position 7 in \"concat(\".");

    //CASE 3 
    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=concat(,
    //Expression expected at position 7.
    GetPTF("concat(,").aExType(ExpressionParserException.class)
        .aExMsgText("Expression expected at position 8 in \"concat(,\".");

    //CASE 4
    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=concat(123,
    //-->Expression expected at position 11.
    GetPTF("concat(123,").aExType(ExpressionParserException.class)
        .aExMsgText("Expression expected after position 11 in \"concat(123,\".");

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
    GetPTF("testingMINMAX3()").aExType(ExpressionParserException.class)
        .aExMsgText("No applicable method found for \"concat\" at position 1 in \"testingMINMAX3()\" with the specified arguments. Method \"concat\" requires 2 or more arguments.");
    GetPTF("testingMINMAX3('A')").aExType(ExpressionParserException.class)
        .aExMsgText("No applicable method found for \"concat\" at position 1 in \"testingMINMAX3('A')\" with the specified arguments. Method \"concat\" requires 2 or more arguments.");
    GetPTF("testingMINMAX3('A','B')").aSerialized("{concat('A','B')}");
    GetPTF("testingMINMAX3('A','B','C')").aSerialized("{concat('A','B','C')}");

    //CASE 8
    //min =-1, max = 0, 
    GetPTF("testingMINMAX4()").aSerialized("{concat()}");
    GetPTF("testingMINMAX4('A')").aExType(ExpressionParserException.class)
        .aExMsgText("No applicable method found for \"concat\" at position 1 in \"testingMINMAX4('A')\" with the specified arguments. Method \"concat\" requires maximal 0 arguments.");
    GetPTF("testingMINMAX4('A','B')").aExType(ExpressionParserException.class)
        .aExMsgText("No applicable method found for \"concat\" at position 1 in \"testingMINMAX4('A','B')\" with the specified arguments. Method \"concat\" requires maximal 0 arguments.");
    GetPTF("testingMINMAX4('A','B','C')").aExType(ExpressionParserException.class)
        .aExMsgText("No applicable method found for \"concat\" at position 1 in \"testingMINMAX4('A','B','C')\" with the specified arguments. Method \"concat\" requires maximal 0 arguments.");

    //CASE 9
    //min =-1, max = 2, 
    GetPTF("testingMINMAX5()").aSerialized("{concat()}");
    GetPTF("testingMINMAX5('A')").aSerialized("{concat('A')}");
    GetPTF("testingMINMAX5('A','B')").aSerialized("{concat('A','B')}");
    GetPTF("testingMINMAX5('A','B','C')").aExType(ExpressionParserException.class)
        .aExMsgText("No applicable method found for \"concat\" at position 1 in \"testingMINMAX5('A','B','C')\" with the specified arguments. Method \"concat\" requires maximal 2 arguments.");

    //CASE 10
    // min =1, max = 2, 
    GetPTF("testingMINMAX6()").aExType(ExpressionParserException.class)
        .aExMsgText("No applicable method found for \"concat\" at position 1 in \"testingMINMAX6()\" with the specified arguments. Method \"concat\" requires between 1 and 2 arguments.");
    GetPTF("testingMINMAX6('A')").aSerialized("{concat('A')}");
    GetPTF("testingMINMAX6('A','B')").aSerialized("{concat('A','B')}");
    GetPTF("testingMINMAX6('A','B','C')").aExType(ExpressionParserException.class)
        .aExMsgText("No applicable method found for \"concat\" at position 1 in \"testingMINMAX6('A','B','C')\" with the specified arguments. Method \"concat\" requires between 1 and 2 arguments.");

    //CASE 11
    // min =1, max = 2, 
    GetPTF("testingMINMAX7()").aExType(ExpressionParserException.class)
        .aExMsgText("No applicable method found for \"concat\" at position 1 in \"testingMINMAX7()\" with the specified arguments. Method \"concat\" requires exact 1 argument(s).");
    GetPTF("testingMINMAX7('A')").aSerialized("{concat('A')}");
    GetPTF("testingMINMAX7('A','B')").aExType(ExpressionParserException.class)
        .aExMsgText("No applicable method found for \"concat\" at position 1 in \"testingMINMAX7('A','B')\" with the specified arguments. Method \"concat\" requires exact 1 argument(s).");
    GetPTF("testingMINMAX7('A','B','C')").aExType(ExpressionParserException.class)
        .aExMsgText("No applicable method found for \"concat\" at position 1 in \"testingMINMAX7('A','B','C')\" with the specified arguments. Method \"concat\" requires exact 1 argument(s).");

    //CASE 12
    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=concat('a' 'b')
    //-->')' or ',' expected at position 11.
    GetPTF("concat('a' 'b')")
        .aExMsgText("\")\" or \",\" expected after position 10 in \"concat('a' 'b')\".");

  }

  @Test
  public void testPMreadParenthesis()
  {
    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=(123
    //-->')' or operator expected at position 4.
    GetPTF("(123").aExType(ExpressionParserException.class)
        .aExMsgText("Missing closing parenthesis \")\" for opening parenthesis \"(\" at position 1 in \"(123\".");

    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=(123 add (456
    //-->')' or operator expected at position 13.
    GetPTF("(123 add (456").aExType(ExpressionParserException.class)
        .aExMsgText("Missing closing parenthesis \")\" for opening parenthesis \"(\" at position 10 in \"(123 add (456\".");

  }

  @Test
  public void testPMvalidateBinaryOperator() /*PM = Parsermethod*/
  {
    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=123 add 'abc'
    //-->Operator 'add' incompatible with operand types 'System.Int32' and 'System.String' at position 4.
    GetPTF("123 add 'abc'").aExType(ExpressionParserException.class)
        .aExKey(ExpressionParserException.INVALID_TYPES_FOR_BINARY_OPERATOR)
        .aExMsgText("Operator \"add\" incompatible with operand types \"System.Uint7\" and \"Edm.String\" at position 5 in \"123 add 'abc'\".");
  }

  @Test
  public void testPMvalidateMethodTypes() /*PM = Parsermethod*/
  {
    //CASE 1
    //http://services.odata.org/Northwind/Northwind.svc/Products/?$filter=year(327686)
    //-->No applicable function found for 'year' at position 0 with the specified arguments. The functions considered are: year(System.DateTime).
    String aInt32 = "327686";
    GetPTF("year(" + aInt32 + ")")
        .aExMsgText("No applicable method found for \"year\" at position 1 in \"year(327686)\" for the specified argument types.");
  }

  @Test
  public void testPMparseFilterString() /*PM = Parsermethod*/
  {
    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter='123
    //-->Unterminated string literal at position 4 in ''123'.
    GetPTF("'123").aExType(ExpressionParserException.class)
        .aExKey(ExpressionParserException.TOKEN_UNDETERMINATED_STRING)
        .aExMsgText("Unterminated string literal at position 1 in \"'123\".");

    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=1%20add%20'123
    //-->Unterminated string literal at position 10 in '1 add '123'.
    GetPTF("1 add '123").aExType(ExpressionParserException.class)
        .aExKey(ExpressionParserException.TOKEN_UNDETERMINATED_STRING)
        .aExMsgText("Unterminated string literal at position 7 in \"1 add '123\".");

    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=datetime'123
    //-->Unterminated literal at position 12 in 'datetime'123'.
    GetPTF("datetime'123").aExType(ExpressionParserException.class)
        .aExKey(ExpressionParserException.TOKEN_UNDETERMINATED_STRING)
        .aExMsgText("Unterminated string literal at position 9 in \"datetime'123\".");

    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=123%20456
    //-->Expression of type 'System.Boolean' expected at position 0.
    GetPTF("123 456").aExType(ExpressionParserException.class)
        .aExKey(ExpressionParserException.INVALID_TRAILING_TOKEN_DETECTED_AFTER_PARSING)
        .aExMsgText("Invalid token \"456\" detected after parsing at position 5 in \"123 456\".");

    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=123%20456
    //-->Expression of type 'System.Boolean' expected at position 0.
    GetPTF("123 456 789").aExType(ExpressionParserException.class)
        .aExKey(ExpressionParserException.INVALID_TRAILING_TOKEN_DETECTED_AFTER_PARSING)
        .aExMsgText("Invalid token \"456\" detected after parsing at position 5 in \"123 456 789\".");

    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=abc%20abc
    //-->No property 'abc' exists in type 'ODataWeb.Northwind.Model.Supplier' at position 0.
    GetPTF("abc abc").aExType(ExpressionParserException.class)
        .aExKey(ExpressionParserException.INVALID_TRAILING_TOKEN_DETECTED_AFTER_PARSING)
        .aExMsgText("Invalid token \"abc\" detected after parsing at position 5 in \"abc abc\".");
  }

  @Test
  public void testAdditionalStuff()
  {

    GetPTF("( A mul B )/X eq TEST")
        .aSerialized("{{{A mul B}/X} eq TEST}");

    GetPTF("( 1 mul 2 )/X eq TEST")
        .aSerialized("{{{1 mul 2}/X} eq TEST}");

    //CASE 1
    EdmEntityType edmEtAllTypes = edmInfo.getTypeEtAllTypes();
    GetPTF(edmEtAllTypes, "( 1 mul 2 )/X eq TEST")
        .aExMsgText("Leftside of method operator at position 12 is not a property in \"( 1 mul 2 )/X eq TEST\".");

    //CASE 2
    //http://services.odata.org/Northwind/Northwind.svc/Products/?$filter=notsupportedfunction('a')
    //-->Unknown function 'notsupportedfunction' at position 0.
    GetPTF("notsupportedfunction('a')")
        .aExMsgText("Unknown function \"notsupportedfunction\" at position 1 in \"notsupportedfunction('a')\".");

    //CASE 3 
    //http://services.odata.org/Northwind/Northwind.svc/Products/?$filter=notsupportedfunction('a')
    //-->Unknown function 'notsupportedfunction' at position 0.
    GetPTF("notsupportedfunction    ('a')")
        .aExMsgText("Unknown function \"notsupportedfunction\" at position 1 in \"notsupportedfunction    ('a')\".");

    //CASE 4
    //Use \ instead of /
    GetPTF("Address\\NotAProperty")
        .aExMsgText("Error while tokenizing a ODATA expression on token \"\\\" at position 8 in \"Address\\NotAProperty\".");

    //CASE 5
    GetPTF("-(-(- 2d)))")
        .aExMsgText("Invalid token \")\" detected after parsing at position 11 in \"-(-(- 2d)))\".");

    //CASE 6
    GetPTF("a b")
        .aExMsgText("Invalid token \"b\" detected after parsing at position 3 in \"a b\".");

    //CASE 7 
    GetPTF("a eq b b")
        .aExMsgText("Invalid token \"b\" detected after parsing at position 8 in \"a eq b b\".");

    //CASE 8
    GetPTF(edmEtAllTypes, "year(Complex)")
        .aExMsgText("No applicable method found for \"year\" at position 1 in \"year(Complex)\" for the specified argument types.");

    //CASE 9
    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=1%20add%202
    //-->Expression of type 'System.Boolean' expected at position 0.
    GetPTF_onlyBinary("1 add 2")
        .aExMsgText("Expression of type \"Edm.Boolean\" expected at position 1 in \"1 add 2\" (actual type is \"Edm.SByte\").");

    //CASE 10
    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=1%20add
    //-->Expression expected at position 5.
    GetPTF_onlyBinary("1 add")
        .aExMsgText("Expression expected after position 5 in \"1 add\".");

    //CASE 11
    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=1%20add
    //-->Expression expected at position 5.
    GetPTF_onlyBinary("1 add   ")
        .aExMsgText("Expression expected after position 5 in \"1 add   \".");
  }

}
