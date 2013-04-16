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
package com.sap.core.odata.api.uri.expression;

import java.util.List;

/**
 * Represents a method expression in the expression tree returned by the methods:
 * <li>{@link com.sap.core.odata.api.uri.UriParser#parseFilterString(com.sap.core.odata.api.edm.EdmEntityType, String) }</li>
 * <li>{@link com.sap.core.odata.api.uri.UriParser#parseOrderByString(com.sap.core.odata.api.edm.EdmEntityType, String) }</li> 
 * <br>
 * <br>
 * <p>A method expression node is inserted in the expression tree for any valid
 * OData method operator in {@link MethodOperator} (e.g. for "substringof", "concat", "year", ... )
 * <br>
 * <br>
 * @author SAP AG
 */
public interface MethodExpression extends CommonExpression {

  /**
   * @return Returns the method object that represents the used method 
   * @see MethodOperator
   */
  public MethodOperator getMethod();

  /**
   * @return Returns the number of provided method parameters
   */
  public int getParameterCount();

  /**
   * @return Returns a ordered list of expressions defining the input parameters for the used method
   * @see CommonExpression
   */
  public List<CommonExpression> getParameters();

}
