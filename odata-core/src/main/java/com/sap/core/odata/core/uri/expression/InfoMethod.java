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
import com.sap.core.odata.api.uri.expression.MethodOperator;

/**
 * Describes a method expression which is allowed in OData expressions
 * @author SAP AG
 */
class InfoMethod
{

  public MethodOperator method;
  public String syntax;
  public int minParameter;
  public int maxParameter;
  ParameterSetCombination combination;

  public InfoMethod(final MethodOperator method, final ParameterSetCombination combination)
  {
    this.method = method;
    syntax = method.toUriLiteral();
    minParameter = 1;
    maxParameter = 1;
    this.combination = combination;
  }

  public InfoMethod(final MethodOperator method, final int minParameters, final int maxParameters, final ParameterSetCombination combination)
  {
    this.method = method;
    syntax = method.toUriLiteral();
    minParameter = minParameters;
    maxParameter = maxParameters;
    this.combination = combination;
  }

  public InfoMethod(final MethodOperator method, final String string, final int minParameters, final int maxParameters, final ParameterSetCombination combination) {
    this.method = method;
    syntax = string;
    minParameter = minParameters;
    maxParameter = maxParameters;
    this.combination = combination;
  }

  public MethodOperator getMethod()
  {
    return method;
  }

  public String getSyntax()
  {
    return syntax;
  }

  public int getMinParameter()
  {
    return minParameter;
  }

  public int getMaxParameter()
  {
    return maxParameter;
  }

  public ParameterSet validateParameterSet(final List<EdmType> actualParameterTypes) throws ExpressionParserInternalError
  {
    return combination.validate(actualParameterTypes);
  }

  /**
   * Returns the EdmType of the returned value of a Method
   * If a method may have different return types (depending on the input type) null will be returned. 
   */
  public EdmType getReturnType() {
    return combination.getReturnType();

  }
}
