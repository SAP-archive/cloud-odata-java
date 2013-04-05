package com.sap.core.odata.core.uri.expression;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.rt.RuntimeDelegate;
import com.sap.core.odata.testutil.mock.TecEdmInfo;
import com.sap.core.odata.testutil.mock.TechnicalScenarioEdmProvider;

/**
 * @author SAP AG
 */
public class TestBase {

  protected TecEdmInfo edmInfo = null;

  public TestBase() {
    final Edm edm = RuntimeDelegate
        .createEdm(new TechnicalScenarioEdmProvider());
    edmInfo = new TecEdmInfo(edm);
  }

  static public ParserTool GetPTF(final String expression) {
    return new ParserTool(expression, false, true, false);
  }

  static public ParserTool GetPTF_onlyBinary(final String expression) {
    return new ParserTool(expression, false, true, true);
  }

  static public ParserTool GetPTFE(final String expression) {
    return new ParserTool(expression, false, true, false);
  }

  static public ParserTool GetPTF(final EdmEntityType resourceEntityType,
      final String expression) {
    return new ParserTool(expression, false, true, false,
        resourceEntityType);
  }

  static public ParserTool GetPTO(final String expression) {
    return new ParserTool(expression, true, true, false);
  }

  static public ParserTool GetPTO(final EdmEntityType resourceEntityType,
      final String expression) {
    return new ParserTool(expression, true, true, false, resourceEntityType);
  }

  static public ParserTool GetPTF_noTEST(final String expression) {
    return new ParserTool(expression, false, false, false);
  }

  static public ParserTool GetPTF_noTEST(
      final EdmEntityType resourceEntityType, final String expression) {
    return new ParserTool(expression, false, false, true,
        resourceEntityType);
  }

  static public ParserTool GetPTO_noTEST(final String expression) {
    return new ParserTool(expression, true, false, true);
  }

  static public ParserTool GetPTO_noTEST(
      final EdmEntityType resourceEntityType, final String expression) {
    return new ParserTool(expression, true, false, true, resourceEntityType);
  }

}
