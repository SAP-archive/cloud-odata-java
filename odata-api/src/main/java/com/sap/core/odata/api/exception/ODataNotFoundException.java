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
package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * Exceptions of this class will result in a HTTP status 404 not found
 * @author SAP AG
 */
public class ODataNotFoundException extends ODataHttpException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference ENTITY = createMessageReference(ODataNotFoundException.class, "ENTITY");
  public static final MessageReference MATRIX = createMessageReference(ODataNotFoundException.class, "MATRIX");

  public ODataNotFoundException(final MessageReference messageReference) {
    super(messageReference, HttpStatusCodes.NOT_FOUND);
  }

  public ODataNotFoundException(final MessageReference messageReference, final String errorCode) {
    super(messageReference, HttpStatusCodes.NOT_FOUND, errorCode);
  }

  public ODataNotFoundException(final MessageReference messageReference, final Throwable cause) {
    super(messageReference, cause, HttpStatusCodes.NOT_FOUND);
  }

  public ODataNotFoundException(final MessageReference messageReference, final Throwable cause, final String errorCode) {
    super(messageReference, cause, HttpStatusCodes.NOT_FOUND, errorCode);
  }
}
