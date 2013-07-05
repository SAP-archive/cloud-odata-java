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
package com.sap.core.odata.core.exception;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmLiteralException;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.exception.ODataBadRequestException;
import com.sap.core.odata.api.exception.ODataConflictException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataForbiddenException;
import com.sap.core.odata.api.exception.ODataHttpException;
import com.sap.core.odata.api.exception.ODataMessageException;
import com.sap.core.odata.api.exception.ODataMethodNotAllowedException;
import com.sap.core.odata.api.exception.ODataNotAcceptableException;
import com.sap.core.odata.api.exception.ODataNotFoundException;
import com.sap.core.odata.api.exception.ODataNotImplementedException;
import com.sap.core.odata.api.exception.ODataPreconditionFailedException;
import com.sap.core.odata.api.exception.ODataPreconditionRequiredException;
import com.sap.core.odata.api.exception.ODataServiceUnavailableException;
import com.sap.core.odata.api.exception.ODataUnsupportedMediaTypeException;
import com.sap.core.odata.api.uri.UriNotMatchingException;
import com.sap.core.odata.api.uri.UriSyntaxException;
import com.sap.core.odata.api.uri.expression.ExceptionVisitExpression;
import com.sap.core.odata.api.uri.expression.ExpressionParserException;
import com.sap.core.odata.core.uri.expression.ExpressionParserInternalError;
import com.sap.core.odata.core.uri.expression.FilterParserExceptionImpl;
import com.sap.core.odata.core.uri.expression.TokenizerException;
import com.sap.core.odata.core.uri.expression.TokenizerExpectError;
import com.sap.core.odata.testutil.fit.BaseTest;
import com.sap.core.odata.testutil.helper.ODataMessageTextVerifier;

/**
 * @author SAP AG
 */
public class ODataExceptionTest extends BaseTest {

  @Test
  public void noCause() {
    ODataException exception = new ODataException("Some message.");
    assertFalse(exception.isCausedByHttpException());
  }

  @Test
  public void nullPointerExceptionCause() {
    ODataException exception = new ODataException("Some message.", new NullPointerException());
    assertFalse(exception.isCausedByHttpException());
  }

  @Test
  public void oDataContextedCause() {
    ODataException exception = new ODataException("Some message.", new ODataNotFoundException(ODataNotFoundException.ENTITY));
    assertTrue(exception.isCausedByHttpException());
  }

  @Test
  public void oDataContextedCauseLayer3() {
    ODataException exception = new ODataException("Some message.",
        new IllegalArgumentException(
            new ODataNotFoundException(ODataNotFoundException.ENTITY)));
    assertTrue(exception.isCausedByHttpException());
  }

  //The following tests verify whether all fields of type {@link MessageReference} of 
  //the tested (Exception) class are provided in the <b>i18n.properties</b> file.
  @Test
  public void messagesOfODataMessageExceptions() {
    ODataMessageTextVerifier.TestClass(ODataMessageException.class);

    ODataMessageTextVerifier.TestClass(UriNotMatchingException.class);
    ODataMessageTextVerifier.TestClass(UriSyntaxException.class);
    ODataMessageTextVerifier.TestClass(ExceptionVisitExpression.class);

    ODataMessageTextVerifier.TestClass(EdmLiteralException.class);
    ODataMessageTextVerifier.TestClass(EdmException.class);
    ODataMessageTextVerifier.TestClass(EdmSimpleTypeException.class);

    ODataMessageTextVerifier.TestClass(EntityProviderException.class);

    ODataMessageTextVerifier.TestClass(ODataHttpException.class);
    ODataMessageTextVerifier.TestClass(ODataBadRequestException.class);
    ODataMessageTextVerifier.TestClass(ODataConflictException.class);
    ODataMessageTextVerifier.TestClass(ODataForbiddenException.class);
    ODataMessageTextVerifier.TestClass(ODataNotFoundException.class);
    ODataMessageTextVerifier.TestClass(ODataMethodNotAllowedException.class);
    ODataMessageTextVerifier.TestClass(ODataNotAcceptableException.class);
    ODataMessageTextVerifier.TestClass(ODataPreconditionFailedException.class);
    ODataMessageTextVerifier.TestClass(ODataPreconditionRequiredException.class);
    ODataMessageTextVerifier.TestClass(ODataServiceUnavailableException.class);
    ODataMessageTextVerifier.TestClass(ODataUnsupportedMediaTypeException.class);
    ODataMessageTextVerifier.TestClass(ODataNotImplementedException.class);

    ODataMessageTextVerifier.TestClass(ExpressionParserException.class);
    ODataMessageTextVerifier.TestClass(FilterParserExceptionImpl.class);
    ODataMessageTextVerifier.TestClass(ExpressionParserInternalError.class);
    ODataMessageTextVerifier.TestClass(TokenizerException.class);
    ODataMessageTextVerifier.TestClass(TokenizerExpectError.class);
  }
}
