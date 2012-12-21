package com.sap.core.odata.core.uri.expression;

import java.util.List;

import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmType;

public interface InputTypeValidator {
  
  public EdmSimpleType validateParameterSet(List<ParameterSet>allowedParameterTypes,  List<EdmType> actualParameterTypes) throws FilterParserInternalError;

  public static class TypePromotionValidator implements InputTypeValidator{
    
    @Override
    public EdmSimpleType validateParameterSet(List<ParameterSet>allowedParameterTypes,  List<EdmType> actualParameterTypes) throws FilterParserInternalError
    {
      //first check for exact parameter combination
      for (ParameterSet parameterSet : allowedParameterTypes)
      {
        boolean s = parameterSet.equals(actualParameterTypes, false);
        if (s)
          return parameterSet.getReturnType();
      }

      //first check for parameter combination with promotion
      for (ParameterSet parameterSet : allowedParameterTypes)
      {
        boolean s = parameterSet.equals(actualParameterTypes, true);
        if (s)
          return parameterSet.getReturnType();
      }
      return null;
    }
  }
}
