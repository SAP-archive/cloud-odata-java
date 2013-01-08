package com.sap.core.odata.core.uri.expression;

import org.junit.Test;

import com.sap.core.odata.api.uri.expression.FilterParserException;

public class TestParserExceptions extends TestBase {

  @Test
  public void TestPMparseFilterString() /*PM = Parsermethod*/
  {
    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter='123
    //GetPTF("'123").aExType(FilterParserException.class)
    //    .aExKey(FilterParserException.TOKEN_UNDETERMINATED_STRING)
    //    .aExMsgText("Unterminated string literal at position 4 in ''123'.");

    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=1%20add%20'123
    //GetPTF("1 add '123").aExType(FilterParserException.class)
    //    .aExKey(FilterParserException.TOKEN_UNDETERMINATED_STRING)
    //    .aExMsgText("Unterminated string literal at position 10 in '1 add '123'.");
    
    
    //http://services.odata.org/Northwind/Northwind.svc/Products(1)/Supplier?$filter=1%20and%20datetime'123'
    GetPTF("datetime'123").aExType(FilterParserException.class)
        .aExMsgPrint();
    
  }
}
