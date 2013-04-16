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

import com.sap.core.odata.api.exception.ODataApplicationException;

/**
 * The interface {@link Visitable} is part of the visitor pattern used to traverse 
 * the expression tree build from a $filter expression string or $orderby expression string.
 * It is implemented by each class used as node in an expression tree.
 * @author SAP AG
 * @see ExpressionVisitor
 *
 */
public interface Visitable {

  /**
   * Method {@link #accept(ExpressionVisitor)} is called when traversing the expression tree. This method is invoked on each 
   * expression used as node in an expression tree. The implementations should  
   * behave as follows:
   * <li>Call accept on all sub nodes and store the returned Objects
   * <li>Call the appropriate method on the {@link ExpressionVisitor} instance and provide the stored objects to that instance 
   * <li>Return the object which should be passed to the processing algorithm of the parent expression node
   * <br>
   * <br>
   * @param visitor 
   *   Object ( implementing {@link ExpressionVisitor}) whose methods are called during traversing a expression node of the expression tree.
   * @return
   *   Object which should be passed to the processing algorithm of the parent expression node  
   * @throws ExceptionVisitExpression
   *   Exception occurred the OData library while traversing the tree
   * @throws ODataApplicationException
   *   Exception thrown by the application who implemented the visitor  
   */
  Object accept(ExpressionVisitor visitor) throws ExceptionVisitExpression, ODataApplicationException;
}
