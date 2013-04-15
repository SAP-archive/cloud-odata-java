/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.core.uri.expression;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.uri.expression.MethodOperator;

/**
 * @author SAP AG
 */
public class FilterParserImplTool extends FilterParserImpl {

  public FilterParserImplTool(final EdmEntityType resourceEntityType)
  {
    super(resourceEntityType);
  }

  public void addTestfunctions()
  {
    Map<String, InfoMethod> lAvailableMethods = new HashMap<String, InfoMethod>(availableMethods);
    ParameterSetCombination combination = null;
    //create type helpers

    //TESTING
    combination = new ParameterSetCombination.PSCflex();
    lAvailableMethods.put("testingMINMAX1", new InfoMethod(MethodOperator.CONCAT, "testingMINMAX1", -1, -1, combination));

    //TESTING
    combination = new ParameterSetCombination.PSCflex();
    lAvailableMethods.put("testingMINMAX2", new InfoMethod(MethodOperator.CONCAT, "testingMINMAX2", 0, -1, combination));

    //TESTING
    combination = new ParameterSetCombination.PSCflex();
    lAvailableMethods.put("testingMINMAX3", new InfoMethod(MethodOperator.CONCAT, "testingMINMAX3", 2, -1, combination));

    //TESTING
    combination = new ParameterSetCombination.PSCflex();
    lAvailableMethods.put("testingMINMAX4", new InfoMethod(MethodOperator.CONCAT, "testingMINMAX4", -1, 0, combination));

    //TESTING
    combination = new ParameterSetCombination.PSCflex();
    lAvailableMethods.put("testingMINMAX5", new InfoMethod(MethodOperator.CONCAT, "testingMINMAX5", -1, 2, combination));

    //TESTING
    combination = new ParameterSetCombination.PSCflex();
    lAvailableMethods.put("testingMINMAX6", new InfoMethod(MethodOperator.CONCAT, "testingMINMAX6", 1, 2, combination));

    //TESTING
    combination = new ParameterSetCombination.PSCflex();
    lAvailableMethods.put("testingMINMAX7", new InfoMethod(MethodOperator.CONCAT, "testingMINMAX7", 1, 1, combination));

    availableMethods = Collections.unmodifiableMap(lAvailableMethods);

  }

}
