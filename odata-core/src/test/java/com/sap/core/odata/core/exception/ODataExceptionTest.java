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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmLiteralException;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;
import com.sap.core.odata.api.exception.ODataApplicationException;
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
import com.sap.core.odata.testutil.fit.BaseTest;
import com.sap.core.odata.testutil.helper.ODataMessageTextVerifier;

/**
 * @author SAP AG
 */
public class ODataExceptionTest extends BaseTest {

  @Test
  public void testNoCause() {
    ODataException exception = new ODataException("Some message.");
    assertFalse(exception.isCausedByHttpException());
  }

  @Test
  public void testNPECause() {
    ODataException exception = new ODataException("Some message.", new NullPointerException());
    assertFalse(exception.isCausedByHttpException());
  }

  @Test
  public void testODataContextedCause() {
    ODataException exception = new ODataException("Some message.", new ODataNotFoundException(ODataNotFoundException.ENTITY));
    assertTrue(exception.isCausedByHttpException());
  }

  @Test
  public void testODataContextedCauseLayer3() {
    ODataException exception = new ODataException("Some message.",
        new IllegalArgumentException(
            new ODataNotFoundException(ODataNotFoundException.ENTITY)));
    assertTrue(exception.isCausedByHttpException());
  }

  //The following tests verify whether all fields of type {@link MessageReference} of 
  //the tested (Exception) class are provided in the <b>i18n.properties</b> file.
  @Test
  public void TestMessagesOfODataException() {
    ODataMessageTextVerifier.TestClass(ODataException.class);
  }

  @Test
  public void TestMessagesOfODataApplicationException() {
    ODataMessageTextVerifier.TestClass(ODataApplicationException.class);
  }

  @Test
  public void TestMessagesOfODataMessageException() {
    ODataMessageTextVerifier.TestClass(ODataMessageException.class);
  }

  @Test
  public void TestMessagesOfUriNotMatchingException() {
    ODataMessageTextVerifier.TestClass(UriNotMatchingException.class);
  }

  @Test
  public void TestMessagesOfUriSyntaxException() {
    ODataMessageTextVerifier.TestClass(UriSyntaxException.class);
  }

  @Test
  public void TestMessagesOfEdmLiteralException() {
    ODataMessageTextVerifier.TestClass(EdmLiteralException.class);
  }

  @Test
  public void TestMessagesOfEdmException() {
    ODataMessageTextVerifier.TestClass(EdmException.class);
  }

  @Test
  public void TestMessagesOfEdmSimpleTypeException() {
    ODataMessageTextVerifier.TestClass(EdmSimpleTypeException.class);
  }

  @Test
  public void TestMessagesOfODataHttpException() {
    ODataMessageTextVerifier.TestClass(ODataHttpException.class);
  }

  @Test
  public void TestMessagesOfODataBadRequestException() {
    ODataMessageTextVerifier.TestClass(ODataBadRequestException.class);
  }

  @Test
  public void TestMessagesOfODataConflictException() {
    ODataMessageTextVerifier.TestClass(ODataConflictException.class);
  }

  @Test
  public void TestMessagesOfODataForbiddenException() {
    ODataMessageTextVerifier.TestClass(ODataForbiddenException.class);
  }

  @Test
  public void TestMessagesOfODataNotFoundException() {
    ODataMessageTextVerifier.TestClass(ODataNotFoundException.class);
  }

  @Test
  public void TestMessagesOfODataMethodNotAllowedException() {
    ODataMessageTextVerifier.TestClass(ODataMethodNotAllowedException.class);
  }

  @Test
  public void TestMessagesOfODataNotAcceptableException() {
    ODataMessageTextVerifier.TestClass(ODataNotAcceptableException.class);
  }

  @Test
  public void TestMessagesOfODataPreconditionFailedException() {
    ODataMessageTextVerifier.TestClass(ODataPreconditionFailedException.class);
  }

  @Test
  public void TestMessagesOfODataPreconditionRequiredException() {
    ODataMessageTextVerifier.TestClass(ODataPreconditionRequiredException.class);
  }

  @Test
  public void TestMessagesOfODataServiceUnavailableException() {
    ODataMessageTextVerifier.TestClass(ODataServiceUnavailableException.class);
  }

  @Test
  public void TestMessagesOfODataUnsupportedMediaTypeException() {
    ODataMessageTextVerifier.TestClass(ODataUnsupportedMediaTypeException.class);
  }

  @Test
  public void TestMessagesOfODataNotImplementedException() {
    ODataMessageTextVerifier.TestClass(ODataNotImplementedException.class);
  }

  @Test
  public void TestRootCauseNpe() {
    NullPointerException rootCauseException = new NullPointerException();
    ODataApplicationException intermediateException1 = new ODataApplicationException("bla", Locale.ENGLISH, rootCauseException);
    ODataMessageException intermediateException2 = new EdmException(EdmException.COMMON, intermediateException1);
    ODataException outerException = new ODataException(intermediateException2);

    Throwable rootException = outerException.getRootCause();
    assertEquals(NullPointerException.class, rootException.getClass());
  }

  @Test
  public void TestRootCauseODataException() {
    ODataException rootCauseException = new ODataException();
    ODataApplicationException intermediateException1 = new ODataApplicationException("bla", Locale.ENGLISH, rootCauseException);
    ODataMessageException intermediateException2 = new EdmException(EdmException.COMMON, intermediateException1);
    ODataException outerException = new ODataException(intermediateException2);

    Throwable rootException = outerException.getRootCause();
    assertEquals(ODataException.class, rootException.getClass());
  }

  @Test
  public void TestRootCauseNone() {
    ODataException outerException = new ODataException();
    Throwable rootException = outerException.getRootCause();
    assertEquals(null, rootException);
  }

}
