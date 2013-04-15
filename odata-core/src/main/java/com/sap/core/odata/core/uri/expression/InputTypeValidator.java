/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.core.uri.expression;

import java.util.List;

import com.sap.core.odata.api.edm.EdmType;

public interface InputTypeValidator {

  public EdmType validateParameterSet(List<ParameterSet> allowedParameterTypes, List<EdmType> actualParameterTypes) throws ExpressionParserInternalError;

  public static class TypePromotionValidator implements InputTypeValidator {

    @Override
    public EdmType validateParameterSet(final List<ParameterSet> allowedParameterTypes, final List<EdmType> actualParameterTypes) throws ExpressionParserInternalError
    {
      //first check for exact parameter combination
      for (ParameterSet parameterSet : allowedParameterTypes)
      {
        boolean s = parameterSet.equals(actualParameterTypes, false);
        if (s) {
          return parameterSet.getReturnType();
        }
      }

      //first check for parameter combination with promotion
      for (ParameterSet parameterSet : allowedParameterTypes)
      {
        boolean s = parameterSet.equals(actualParameterTypes, true);
        if (s) {
          return parameterSet.getReturnType();
        }
      }
      return null;
    }
  }
}
