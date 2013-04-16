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
package com.sap.core.odata.api.edm;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataMessageException;

/**
 * @com.sap.core.odata.DoNotImplement
 * An exception for problems regarding the Entity Data Model.
 * @author SAP AG
 */
public class EdmException extends ODataMessageException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createMessageReference(EdmException.class, "COMMON");
  public static final MessageReference PROVIDERPROBLEM = createMessageReference(EdmException.class, "PROVIDERPROBLEM");

  public EdmException(final MessageReference messageReference) {
    super(messageReference);
  }

  public EdmException(final MessageReference messageReference, final Throwable cause) {
    super(messageReference, cause);
  }

  public EdmException(final MessageReference messageReference, final String errorCode) {
    super(messageReference, errorCode);
  }

  public EdmException(final MessageReference messageReference, final Throwable cause, final String errorCode) {
    super(messageReference, cause, errorCode);
  }

}
