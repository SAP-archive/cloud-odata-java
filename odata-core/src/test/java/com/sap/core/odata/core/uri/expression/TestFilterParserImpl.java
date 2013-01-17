package com.sap.core.odata.core.uri.expression;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.uri.expression.MethodOperator;
import com.sap.core.odata.core.edm.EdmSimpleTypeFacadeImpl;

public class TestFilterParserImpl extends FilterParserImpl {

  public TestFilterParserImpl(Edm edm, EdmEntityType edmType) 
  {
    super(edm, edmType);
  }
  
  public void addTestfunctions()
  {
    Map<String, InfoMethod> lAvailableMethods = new HashMap<String, InfoMethod>(availableMethods);
    ParameterSetCombination combination = null;
    //create type helpers

    EdmSimpleType string = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.String);

    //TESTING
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(string, string, string).setFurtherType(string));
    lAvailableMethods.put("testingMINMAX1", new InfoMethod(MethodOperator.CONCAT, "testingMINMAX1", -1, -1, combination));

    //TESTING
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(string, string, string).setFurtherType(string));
    lAvailableMethods.put("testingMINMAX2", new InfoMethod(MethodOperator.CONCAT, "testingMINMAX2", 0, -1, combination));

    //TESTING
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(string, string, string).setFurtherType(string));
    lAvailableMethods.put("testingMINMAX3", new InfoMethod(MethodOperator.CONCAT, "testingMINMAX3", 2, -1, combination));

    //TESTING
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(string, string, string).setFurtherType(string));
    lAvailableMethods.put("testingMINMAX4", new InfoMethod(MethodOperator.CONCAT, "testingMINMAX4", -1, 0, combination));

    //TESTING
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(string, string, string).setFurtherType(string));
    lAvailableMethods.put("testingMINMAX5", new InfoMethod(MethodOperator.CONCAT, "testingMINMAX5", -1, 2, combination));

    //TESTING
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(string, string, string).setFurtherType(string));
    lAvailableMethods.put("testingMINMAX6", new InfoMethod(MethodOperator.CONCAT, "testingMINMAX6", 1, 2, combination));

    //TESTING
    combination = new ParameterSetCombination.PSCflex();
    combination.add(new ParameterSet(string, string, string).setFurtherType(string));
    lAvailableMethods.put("testingMINMAX7", new InfoMethod(MethodOperator.CONCAT, "testingMINMAX7", 1, 1, combination));
    
    availableMethods = Collections.unmodifiableMap(lAvailableMethods);
    
  }

}
