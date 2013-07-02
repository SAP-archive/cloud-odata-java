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

import java.util.List;

import com.sap.core.odata.api.edm.EdmType;

public interface InputTypeValidator {

  public EdmType validateParameterSet(List<ParameterSet> allowedParameterTypes, List<EdmType> actualParameterTypes) throws ExpressionParserInternalError;

  public static class TypePromotionValidator implements InputTypeValidator {

    @Override
    public EdmType validateParameterSet(final List<ParameterSet> allowedParameterTypes, final List<EdmType> actualParameterTypes) throws ExpressionParserInternalError {
      //first check for exact parameter combination
      for (ParameterSet parameterSet : allowedParameterTypes) {
        boolean s = parameterSet.equals(actualParameterTypes, false);
        if (s) {
          return parameterSet.getReturnType();
        }
      }

      //first check for parameter combination with promotion
      for (ParameterSet parameterSet : allowedParameterTypes) {
        boolean s = parameterSet.equals(actualParameterTypes, true);
        if (s) {
          return parameterSet.getReturnType();
        }
      }
      return null;
    }
  }
}
