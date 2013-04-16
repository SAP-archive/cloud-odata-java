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

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataMessageException;

/**
 * Exception thrown while traversing/visiting a filter expression tree
 * @author SAP AG
 */
public class ExceptionVisitExpression extends ODataMessageException {
  private static final long serialVersionUID = 7701L;

  public static final MessageReference COMMON = createMessageReference(ExceptionVisitExpression.class, "COMMON");

  private CommonExpression filterTree;

  public ExceptionVisitExpression() {
    super(COMMON);
  }

  /**
   * Create {@link ExceptionVisitExpression} with given {@link MessageReference}.
   * 
   * @param messageReference
   *   references the message text (and additional values) of this {@link ExceptionVisitExpression}
   */
  public ExceptionVisitExpression(final MessageReference messageReference) {
    super(messageReference);
  }

  /**
   * Create {@link ExceptionVisitExpression} with given {@link MessageReference} and cause {@link Throwable} which caused
   * this {@link ExceptionVisitExpression}.
   * 
   * @param message
   *   references the message text (and additional values) of this {@link ExceptionVisitExpression}
   * @param cause
   *   exception which caused this {@link ExceptionVisitExpression}
   */
  public ExceptionVisitExpression(final MessageReference message, final Throwable cause) {
    super(message, cause);
  }

  /**
   * Get erroneous filter for debug information
   * @return Erroneous filter tree 
   */
  public CommonExpression getFilterTree() {
    return filterTree;
  }

  /**
   * Sets erroneous filter tree for debug information.
   * @param filterTree Erroneous filter tree
   */
  public void setFilterTree(final CommonExpression filterTree) {
    this.filterTree = filterTree;
  }
}
