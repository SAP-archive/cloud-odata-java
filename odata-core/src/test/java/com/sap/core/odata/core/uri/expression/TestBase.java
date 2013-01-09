package com.sap.core.odata.core.uri.expression;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntityType;

public class TestBase {

  static public ParserTool GetPTF(String expression)
  {
    return new ParserTool(expression, false);
  }

  static public ParserTool GetPTF(Edm edm, EdmEntityType resourceEntityType, String expression) {
    return new ParserTool(edm, resourceEntityType, expression, false);
  }

  static public ParserTool GetPTO(String expression)
  {
    return new ParserTool(expression, true);
  }

  static public ParserTool GetPTO(Edm edm, EdmEntityType resourceEntityType, String expression) {
    return new ParserTool(edm, resourceEntityType, expression, true);
  }

}
