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

/**
 * @com.sap.core.odata.DoNotImplement
 * Exception for violation of the OData URI construction rules, resulting in a 400 Bad Request response
 * @author SAP AG
 */
public class EdmLiteralException extends EdmException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference NOTEXT = createMessageReference(EdmLiteralException.class, "NOTEXT");
  public static final MessageReference LITERALFORMAT = createMessageReference(EdmLiteralException.class, "LITERALFORMAT");
  public static final MessageReference UNKNOWNLITERAL = createMessageReference(EdmLiteralException.class, "UNKNOWNLITERAL");

  public EdmLiteralException(final MessageReference MessageReference) {
    super(MessageReference);
  }

  public EdmLiteralException(final MessageReference messageReference, final Throwable cause) {
    super(messageReference, cause);
  }

  public EdmLiteralException(final MessageReference MessageReference, final String errorCode) {
    super(MessageReference, errorCode);
  }

  public EdmLiteralException(final MessageReference messageReference, final Throwable cause, final String errorCode) {
    super(messageReference, cause, errorCode);
  }
}
