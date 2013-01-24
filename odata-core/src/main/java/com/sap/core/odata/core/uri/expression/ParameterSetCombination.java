package com.sap.core.odata.core.uri.expression;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmType;

public interface ParameterSetCombination {

  void add(ParameterSet parameterSet);

  EdmType validate(List<EdmType> actualParameterTypes) throws ExpressionParserInternalError;

  void addFirst(ParameterSet parameterSet);

  EdmType getReturnType();

  public static class PSCflex implements ParameterSetCombination
  {
    public List<ParameterSet> combinations = new ArrayList<ParameterSet>();

    @Override
    public void add(ParameterSet parameterSet)
    {
      combinations.add(parameterSet);
    }

    @Override
    public EdmType getReturnType() {
      int parameterCount = combinations.size();
      if (parameterCount == 0)
        return null;

      if (parameterCount == 1)
        return combinations.get(0).getReturnType();

      //There are more than 1 possible return type, check if they are equal, if not return null.
      EdmType returnType = combinations.get(0).getReturnType();
      for (int i = 1; i < parameterCount; i++)
      {
        if (returnType != combinations.get(i))
        {
          return null;
        }
      }

      return returnType;

    }

    public void addFirst(ParameterSet parameterSet)
    {
      List<ParameterSet> oldCombinations = combinations;
      combinations = new ArrayList<ParameterSet>();
      combinations.add(parameterSet);
      for (ParameterSet parameterSet1 : oldCombinations)
      {
        combinations.add(parameterSet1);
      }

    }

    @Override
    public EdmSimpleType validate(List<EdmType> actualParameterTypes) throws ExpressionParserInternalError
    {
      //first check for exact parameter combination
      for (ParameterSet parameterSet : combinations)
      {
        boolean s = parameterSet.equals(actualParameterTypes, false);
        if (s)
          return parameterSet.getReturnType();
      }

      //first check for parameter combination with promotion
      for (ParameterSet parameterSet : combinations)
      {
        boolean s = parameterSet.equals(actualParameterTypes, true);
        if (s)
          return parameterSet.getReturnType();
      }
      return null;
    }

  }

  public static class PSCReturnTypeEqLastParameter implements ParameterSetCombination
  {

    @Override
    public void add(ParameterSet parameterSet) {
      throw new IllegalStateException();
    }

    @Override
    public void addFirst(ParameterSet parameterSet) {
      throw new IllegalStateException();
    }

    @Override
    public EdmType validate(List<EdmType> actualParameterTypes) throws ExpressionParserInternalError {
      return actualParameterTypes.get(actualParameterTypes.size() - 1);
    }

    @Override
    public EdmType getReturnType() {
      //If the return type is always the type of the last parameter of the actual parameters ( e.g. when using the method operator) 
      //then the return type can not predicted.
      return null;
    }
  }

}
