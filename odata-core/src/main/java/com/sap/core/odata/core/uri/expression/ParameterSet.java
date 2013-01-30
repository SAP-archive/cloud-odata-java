package com.sap.core.odata.core.uri.expression;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmType;

/**
 * Parameter set is a vector of 1 or more EDM types, it is used to store the possible 
 * input and return types of a <i>OData filter operator</i> or <i>OData filter method</i>
 * @see InfoMethod 
 * @see InfoBinaryOperator 
 * @see InfoUnaryOperator
 * @author SAP AG 
 */
class ParameterSet
{
  private EdmType returnType = null;
  public ArrayList<EdmSimpleType> types = new ArrayList<EdmSimpleType>();
  private EdmSimpleType furtherType = null;

  public ParameterSet(EdmType returnType, EdmSimpleType type1)
  {
    this.returnType = returnType;
    types.add(type1);
  }

  public ParameterSet(EdmSimpleType returnType, EdmSimpleType type1, EdmSimpleType type2)
  {
    this.returnType = returnType;
    types.add(type1);
    types.add(type2);
  }

  public ParameterSet(EdmSimpleType returnType, EdmSimpleType type1, EdmSimpleType type2, EdmSimpleType type3)
  {
    this.returnType = returnType;
    types.add(type1);
    types.add(type2);
    types.add(type3);
  }

  public EdmType getReturnType()
  {
    return returnType;
  }

  public EdmSimpleType getFurtherType()
  {
    return furtherType;
  }

  public ParameterSet setFurtherType(EdmSimpleType furtherType)
  {
    this.furtherType = furtherType;
    return this;
  }

  /**
   * Compares a list of EdmTypes with the EdmTypes stored in {@link #types}.
   * The lists are compared sequentially, e.g index N of actualParameterTypes with index N of {@link #types}.
   * If the input list contains more elements than stored in {@link #types} (which is allowed  when validating the <i>concat</i> method
   * which takes a variable number of input parameters), the actual parameter type is compared against the {@link #furtherType}.
   * @param actualParameterTypes
   * @param allowPromotion
   * @return
   * @throws ExpressionParserInternalError
   */
  public boolean equals(List<EdmType> actualParameterTypes, boolean allowPromotion) throws ExpressionParserInternalError
  {
    int actSize = actualParameterTypes.size();
    int paramSize = types.size();

    if (actSize < paramSize)
      return false;
    //throw FilterParserInternalError.createINVALID_TYPE_COUNT(); //to few actual Parameters

    //This may happen if the number of supplied actual method parameters is higher then than the number
    //of allowed method parameters but this should be checked before, hence this is an internal error in the parser
    if ((actSize > paramSize) && (furtherType == null))
      return false;
    //throw FilterParserInternalError.createINVALID_TYPE_COUNT();

    for (int i = 0; i < actSize; i++)
    {
      EdmType actType = actualParameterTypes.get(i);
      if (actType == null)
        return false;

      EdmSimpleType paramType = null;
      if (i < paramSize) {
        paramType = types.get(i);
      } else {
        paramType = furtherType; // for methods with variable amount of method parameters  
      }

      if (!actType.equals(paramType))
      {
        //this the parameter type does not fit and if it is not allowed to promoted the actual parameter then
        //this parameter combination does not fit
        if (!allowPromotion)
          return false;

        //Its allowed to promoted the actual parameter!!!

        //The type simply don't match
        if (!paramType.isCompatible((EdmSimpleType) actType))
          return false;
      }
    }
    return true;
  }
}
