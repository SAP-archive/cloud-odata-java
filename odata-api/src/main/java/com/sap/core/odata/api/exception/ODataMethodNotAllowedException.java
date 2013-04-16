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
 * Exceptions of this class will result in a HTTP status 405 method not allowed
 * @author SAP AG
 */
public class ODataMethodNotAllowedException extends ODataHttpException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference DISPATCH = createMessageReference(ODataMethodNotAllowedException.class, "DISPATCH");
  public static final MessageReference TUNNELING = createMessageReference(ODataMethodNotAllowedException.class, "TUNNELING");

  public ODataMethodNotAllowedException(final MessageReference messageReference) {
    super(messageReference, HttpStatusCodes.METHOD_NOT_ALLOWED);
  }

  public ODataMethodNotAllowedException(final MessageReference messageReference, final Throwable cause) {
    super(messageReference, cause, HttpStatusCodes.METHOD_NOT_ALLOWED);
  }

  public ODataMethodNotAllowedException(final MessageReference messageReference, final String errorCode) {
    super(messageReference, HttpStatusCodes.METHOD_NOT_ALLOWED, errorCode);
  }

  public ODataMethodNotAllowedException(final MessageReference messageReference, final Throwable cause, final String errorCode) {
    super(messageReference, cause, HttpStatusCodes.METHOD_NOT_ALLOWED, errorCode);
  }

}
