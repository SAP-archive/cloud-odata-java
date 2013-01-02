package com.sap.core.odata.core.uri.expression;

import static org.junit.Assert.fail;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.FilterParserException;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.api.uri.expression.OrderByParserException;

public class TestBase {
  
  static public ParserTool GetPTF(String expression)
  {
    try {
      FilterParserImpl parser = new FilterParserImpl(null, null);
      FilterExpression root = parser.parseFilterString(expression);
      return new ParserTool(expression, root);
    } catch (FilterParserInternalError e) {
      fail("Error in parser" + e.getLocalizedMessage());
    } catch (FilterParserException e) {
      fail("Error in parser" + e.getLocalizedMessage());
    }
    return null;
  }

  static public ParserTool GetPTF(Edm edm, EdmEntityType resourceEntityType, String expression) {
    try {
      FilterParserImpl parser = new FilterParserImpl(edm, resourceEntityType);
      FilterExpression root = parser.parseFilterString(expression);
      return new ParserTool(expression, root);
    } catch (FilterParserInternalError e) {
      fail("Error in parser" + e.getLocalizedMessage());
    } catch (FilterParserException e) {
      fail("Error in parser" + e.getLocalizedMessage());
    }
    return null;

  }

  static public ParserTool GetPTO(String expression)
  {
    try {
      OrderByParserImpl parser = new OrderByParserImpl(null, null);
      OrderByExpression root = parser.parseOrderByString(expression);
      return new ParserTool(expression, root);
    } catch (FilterParserInternalError e) {
      fail("Error in parser" + e.getLocalizedMessage());
    } catch (OrderByParserException e) {
      fail("Error in parser" + e.getLocalizedMessage());
    }
    return null;
  }

  static public ParserTool GetPTO(Edm edm, EdmEntityType resourceEntityType, String expression) {
    try {
      OrderByParserImpl parser = new OrderByParserImpl(edm, resourceEntityType);
      OrderByExpression root = parser.parseOrderByString(expression);
      return new ParserTool(expression, root);
    } catch (FilterParserInternalError e) {
      fail("Error in parser" + e.getLocalizedMessage());
    } catch (OrderByParserException e) {
      fail("Error in parser" + e.getLocalizedMessage());
    }
    return null;
  }
  

  
}
