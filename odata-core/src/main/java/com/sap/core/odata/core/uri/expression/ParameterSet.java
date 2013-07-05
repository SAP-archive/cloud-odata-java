/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.core.uri.expression;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;

/**
 * Parameter set is a vector of 1 or more EDM types, it is used to store the possible 
 * input and return types of a <i>OData filter operator</i> or <i>OData filter method</i>
 * @see InfoMethod 
 * @see InfoBinaryOperator 
 * @see InfoUnaryOperator
 * @author SAP AG 
 */
@SuppressWarnings("javadoc")
public class ParameterSet {
  private EdmType returnType = null;
  public ArrayList<EdmSimpleType> types = new ArrayList<EdmSimpleType>();
  private EdmSimpleType furtherType = null;

  public ParameterSet(final EdmType returnType, final EdmSimpleType type1) {
    this.returnType = returnType;
    types.add(type1);
  }

  public ParameterSet(final EdmSimpleType returnType, final EdmSimpleType type1, final EdmSimpleType type2) {
    this.returnType = returnType;
    types.add(type1);
    types.add(type2);
  }

  public ParameterSet(final EdmSimpleType returnType, final EdmSimpleType type1, final EdmSimpleType type2, final EdmSimpleType type3) {
    this.returnType = returnType;
    types.add(type1);
    types.add(type2);
    types.add(type3);
  }

  public EdmType getReturnType() {
    return returnType;
  }

  public EdmSimpleType getFurtherType() {
    return furtherType;
  }

  public ParameterSet setFurtherType(final EdmSimpleType furtherType) {
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
   * @return true if equals
   * @throws ExpressionParserInternalError
   */
  public boolean equals(final List<EdmType> actualParameterTypes, final boolean allowPromotion) throws ExpressionParserInternalError {
    int actSize = actualParameterTypes.size();
    int paramSize = types.size();

    if (actSize < paramSize) {
      return false;
      //throw FilterParserInternalError.createINVALID_TYPE_COUNT(); //to few actual Parameters
    }

    //This may happen if the number of supplied actual method parameters is higher then than the number
    //of allowed method parameters but this should be checked before, hence this is an internal error in the parser
    if ((actSize > paramSize) && (furtherType == null)) {
      return false;
      //throw FilterParserInternalError.createINVALID_TYPE_COUNT();
    }

    for (int i = 0; i < actSize; i++) {
      EdmType actType = actualParameterTypes.get(i);
      if (actType == null) {
        return false;
      }

      EdmSimpleType paramType = null;
      if (i < paramSize) {
        paramType = types.get(i);
      } else {
        paramType = furtherType; // for methods with variable amount of method parameters  
      }

      if (!actType.equals(paramType)) {
        //this the parameter type does not fit and if it is not allowed to promoted the actual parameter then
        //this parameter combination does not fit
        if (!allowPromotion) {
          return false;
        }

        //Its allowed to promoted the actual parameter!!!

        //Promotion only allowed for simple types
        if (actType.getKind() != EdmTypeKind.SIMPLE) {
          return false; //Tested with TestParserExceptions.testAdditionalStuff CASE 8
        }

        //The type simply don't match          
        if (!paramType.isCompatible((EdmSimpleType) actType)) {
          return false;
        }
      }
    }
    return true;
  }
}
