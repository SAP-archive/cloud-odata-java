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
package com.sap.core.odata.processor.api.jpa.exception;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.processor.api.jpa.factory.ODataJPAFactory;

/**
 * The exception is thrown for any unexpected errors raising while accessing
 * data from Java Persistence Models.
 * 
 * The exception object is created with localized error texts provided error
 * texts are maintained in localized languages.
 * 
 * @author SAP AG
 * 
 */
public class ODataJPARuntimeException extends ODataJPAException {

  public static final MessageReference ENTITY_MANAGER_NOT_INITIALIZED = createMessageReference(
      ODataJPARuntimeException.class, "ENTITY_MANAGER_NOT_INITIALIZED");
  public static final MessageReference RESOURCE_NOT_FOUND = createMessageReference(
      ODataJPARuntimeException.class, "RESOURCE_NOT_FOUND");
  public static final MessageReference GENERAL = createMessageReference(
      ODataJPARuntimeException.class, "GENERAL");
  public static final MessageReference INNER_EXCEPTION = createMessageReference(
      ODataJPARuntimeException.class, "INNER_EXCEPTION");
  public static final MessageReference JOIN_CLAUSE_EXPECTED = createMessageReference(
      ODataJPARuntimeException.class, "JOIN_CLAUSE_EXPECTED");
  public static final MessageReference ERROR_JPQLCTXBLDR_CREATE = createMessageReference(
      ODataJPARuntimeException.class, "ERROR_JPQLCTXBLDR_CREATE");
  public static final MessageReference ERROR_ODATA_FILTER_CONDITION = createMessageReference(
      ODataJPARuntimeException.class, "ERROR_ODATA_FILTER_CONDITION");
  public static final MessageReference ERROR_JPQL_QUERY_CREATE = createMessageReference(
      ODataJPARuntimeException.class, "ERROR_JPQL_QUERY_CREATE");
  public static final MessageReference ERROR_JPQL_CREATE_REQUEST = createMessageReference(
      ODataJPARuntimeException.class, "ERROR_JPQL_CREATE_REQUEST");
  public static final MessageReference ERROR_JPQL_UPDATE_REQUEST = createMessageReference(
      ODataJPARuntimeException.class, "ERROR_JPQL_UPDATE_REQUEST");
  public static final MessageReference ERROR_JPQL_DELETE_REQUEST = createMessageReference(
      ODataJPARuntimeException.class, "ERROR_JPQL_DELETE_REQUEST");
  public static final MessageReference ERROR_JPQL_KEY_VALUE = createMessageReference(
      ODataJPARuntimeException.class, "ERROR_JPQL_KEY_VALUE");
  public static final MessageReference ERROR_JPQL_PARAM_VALUE = createMessageReference(
      ODataJPARuntimeException.class, "ERROR_JPQL_PARAM_VALUE");
  public static final MessageReference ERROR_JPQL_UNIQUE_CONSTRAINT = createMessageReference(
      ODataJPARuntimeException.class, "ERROR_JPQL_UNIQUE_CONSTRAINT");
  public static final MessageReference ERROR_JPQL_INTEGRITY_CONSTRAINT = createMessageReference(
      ODataJPARuntimeException.class, "ERROR_JPQL_INTEGRITY_CONSTRAINT");
  public static final MessageReference RELATIONSHIP_INVALID = createMessageReference(
      ODataJPARuntimeException.class, "RELATIONSHIP_INVALID");
  public static final MessageReference RESOURCE_X_NOT_FOUND = createMessageReference(
      ODataJPARuntimeException.class, "RESOURCE_X_NOT_FOUND");

  private ODataJPARuntimeException(String localizedMessage, Throwable e,
      MessageReference msgRef) {
    super(localizedMessage, e, msgRef);
  }

  /**
   * The method creates an exception object of type ODataJPARuntimeException
   * with localized error texts.
   * 
   * @param messageReference
   *            is a <b>mandatory</b> parameter referring to a literal that
   *            could be translated to localized error texts.
   * @param e
   *            is an optional parameter representing the previous exception
   *            in the call stack
   * @return an instance of ODataJPARuntimeException which can be then raised.
   * @throws ODataJPARuntimeException
   */
  public static ODataJPARuntimeException throwException(
      MessageReference messageReference, Throwable e) {
    ODataJPAMessageService messageService;
    messageService = ODataJPAFactory.createFactory()
        .getODataJPAAccessFactory()
        .getODataJPAMessageService(DEFAULT_LOCALE);
    String message = messageService
        .getLocalizedMessage(messageReference, e);
    return new ODataJPARuntimeException(message, e, messageReference);
  }

  private static final long serialVersionUID = -5230976355642443012L;

}
