package com.sap.core.odata.core;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.enums.HttpStatusCodes;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotFoundException;
import com.sap.core.odata.api.uri.UriSyntaxException;
import com.sap.core.odata.core.exception.MessageService;
import com.sap.core.odata.core.exception.ODataRuntimeException;
import com.sap.core.odata.testutils.helper.StringHelper;

/**
 * @author SAP AG
 */
public class ODataExceptionMapperImplTest {

  ODataExceptionMapperImpl exceptionMapper;

  @BeforeClass
  public static void setup() throws Exception {
    Map<String, String> prefixMap = new HashMap<String, String>();
    prefixMap.put("a", Edm.NAMESPACE_M_2007_08);
    XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(prefixMap));
  }

  @Before
  public void setUp() {
    exceptionMapper = new ODataExceptionMapperImpl();
    exceptionMapper.httpHeaders = mock(HttpHeaders.class);
    exceptionMapper.uriInfo = mock(UriInfo.class);
    MultivaluedHashMap<String, String> map = new MultivaluedHashMap<String, String>();
    when(exceptionMapper.uriInfo.getQueryParameters()).thenReturn(map);
  }

  @Test
  public void testODataNotFoundException() throws Exception {
    // prepare
    Exception exception = new ODataNotFoundException(ODataNotFoundException.ENTITY);

    // execute
    Response response = exceptionMapper.toResponse(exception);

    // verify
    assertNotNull(response);
    assertEquals(HttpStatusCodes.NOT_FOUND.getStatusCode(), response.getStatus());
    String errorMessage = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertXpathEvaluatesTo(ODataNotFoundException.ENTITY.getKey(), "/a:error/a:code", errorMessage);
    assertXpathEvaluatesTo(MessageService.getMessage(Locale.ENGLISH, ODataNotFoundException.ENTITY).getText(), "/a:error/a:message", errorMessage);
  }

  @Test
  public void testODataNotFoundExceptionDe() throws Exception {
    // prepare
    Exception exception = new ODataNotFoundException(ODataNotFoundException.ENTITY);
    when(exceptionMapper.httpHeaders.getAcceptableLanguages())
        .thenReturn(Arrays.asList(Locale.GERMAN));

    // execute
    Response response = exceptionMapper.toResponse(exception);

    // verify
    assertNotNull(response);
    assertEquals(HttpStatusCodes.NOT_FOUND.getStatusCode(), response.getStatus());
    String errorMessage = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertXpathEvaluatesTo(ODataNotFoundException.ENTITY.getKey(), "/a:error/a:code", errorMessage);
    assertXpathEvaluatesTo(MessageService.getMessage(Locale.GERMAN, ODataNotFoundException.ENTITY).getText(), "/a:error/a:message", errorMessage);
  }

  @Test
  public void testWrappedODataNotFoundException() throws Exception {
    // prepare
    Exception causeException = new ODataNotFoundException(ODataNotFoundException.ENTITY);
    String exceptionMessage = "Some odd exception";
    Exception exception = new ODataException(exceptionMessage, causeException);

    // execute
    Response response = exceptionMapper.toResponse(exception);

    // verify
    assertNotNull(response);
    assertEquals(HttpStatusCodes.NOT_FOUND.getStatusCode(), response.getStatus());
    String errorMessage = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertXpathEvaluatesTo(ODataNotFoundException.ENTITY.getKey(), "/a:error/a:code", errorMessage);
    assertXpathEvaluatesTo(MessageService.getMessage(Locale.ENGLISH, ODataNotFoundException.ENTITY).getText(), "/a:error/a:message", errorMessage);
  }

  @Test
  public void testODataApplicationException() throws Exception {
    // prepare
    String message = "expected exception message";
    Exception exception = new ODataApplicationException(message);

    // execute
    Response response = exceptionMapper.toResponse(exception);

    // verify
    assertNotNull(response);
    assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    String errorMessage = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertXpathEvaluatesTo(exception.getClass().getName(), "/a:error/a:code", errorMessage);
    assertXpathEvaluatesTo(message, "/a:error/a:message", errorMessage);
  }

  @Test
  public void testODataApplicationExceptionWrapped() throws Exception {
    // prepare
    String message = "expected exception message";
    Exception exception = new ODataException(new ODataApplicationException(message));

    // execute
    Response response = exceptionMapper.toResponse(exception);

    // verify
    assertNotNull(response);
    assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    String errorMessage = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertXpathEvaluatesTo(ODataApplicationException.class.getName(), "/a:error/a:code", errorMessage);
    assertXpathEvaluatesTo(message, "/a:error/a:message", errorMessage);
  }

  @Test
  public void testODataApplicationExceptionWithStatus() throws Exception {
    // prepare
    String message = "expected exception message";
    HttpStatusCodes status = HttpStatusCodes.OK;
    Exception exception = new ODataApplicationException(message, status);

    // execute
    Response response = exceptionMapper.toResponse(exception);

    // verify
    assertNotNull(response);
    assertEquals(Status.OK.getStatusCode(), response.getStatus());
    String errorMessage = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertXpathEvaluatesTo(exception.getClass().getName(), "/a:error/a:code", errorMessage);
    assertXpathEvaluatesTo(message, "/a:error/a:message", errorMessage);
  }

  @Test
  public void testODataApplicationExceptionWithStatusWrapped() throws Exception {
    // prepare
    String message = "expected exception message";
    HttpStatusCodes status = HttpStatusCodes.OK;
    Exception exception = new ODataException(new ODataApplicationException(message, status));

    // execute
    Response response = exceptionMapper.toResponse(exception);

    // verify
    assertNotNull(response);
    assertEquals(Status.OK.getStatusCode(), response.getStatus());
    String errorMessage = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertXpathEvaluatesTo(ODataApplicationException.class.getName(), "/a:error/a:code", errorMessage);
    assertXpathEvaluatesTo(message, "/a:error/a:message", errorMessage);
  }

  @Test
  public void testUriParserException() throws Exception {
    // prepare
    Exception exception = new UriSyntaxException(UriSyntaxException.EMPTYSEGMENT);

    // execute
    Response response = exceptionMapper.toResponse(exception);

    // verify
    assertNotNull(response);
    assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    String errorMessage = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertXpathEvaluatesTo(UriSyntaxException.EMPTYSEGMENT.getKey(), "/a:error/a:code", errorMessage);
    assertXpathEvaluatesTo(MessageService.getMessage(Locale.ENGLISH, UriSyntaxException.EMPTYSEGMENT).getText(), "/a:error/a:message", errorMessage);
  }

  @Test
  public void testUriParserExceptionWrapped() throws Exception {
    // prepare
    Exception exception = new ODataException("outer exception", new UriSyntaxException(UriSyntaxException.EMPTYSEGMENT));

    // execute
    Response response = exceptionMapper.toResponse(exception);

    // verify
    assertNotNull(response);
    assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    String errorMessage = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertXpathEvaluatesTo(UriSyntaxException.EMPTYSEGMENT.getKey(), "/a:error/a:code", errorMessage);
    assertXpathEvaluatesTo(MessageService.getMessage(Locale.ENGLISH, UriSyntaxException.EMPTYSEGMENT).getText(), "/a:error/a:message", errorMessage);
  }

  @Test
  public void testIoException() throws Exception {
    // prepare
    String message = "expected exception message";
    Exception exception = new IOException(message);

    // execute
    Response response = exceptionMapper.toResponse(exception);

    // verify
    assertNotNull(response);
    assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    String errorMessage = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertXpathEvaluatesTo(exception.getClass().getName(), "/a:error/a:code", errorMessage);
    assertXpathEvaluatesTo(message, "/a:error/a:message", errorMessage);
  }

  @Test
  public void testODataException() throws Exception {
    // prepare
    String exceptionMessage = "Some odd exception";
    Exception exception = new ODataException(exceptionMessage);

    // execute
    Response response = exceptionMapper.toResponse(exception);

    // verify
    assertNotNull(response);
    assertEquals(HttpStatusCodes.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    String errorMessage = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertXpathEvaluatesTo(exception.getClass().getName(), "/a:error/a:code", errorMessage);
    assertXpathEvaluatesTo(exceptionMessage, "/a:error/a:message", errorMessage);
  }

  @Test
  public void testODataExceptionWithoutText() throws Exception {
    final String text = null;
    final Exception exception = new ODataException(text);
    final Response response = exceptionMapper.toResponse(exception);

    assertNotNull(response);
    final String errorMessage = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertXpathExists("/a:error/a:message", errorMessage);
    assertXpathEvaluatesTo("en", "/a:error/a:message/@xml:lang", errorMessage);
  }

  @Test
  public void testODataRuntimeException() throws Exception {
    // prepare
    String exceptionMessage = "Some odd runtime exception";
    Exception exception = new ODataRuntimeException(exceptionMessage);

    // execute
    Response response = exceptionMapper.toResponse(exception);

    // verify
    assertNotNull(response);
    assertEquals(HttpStatusCodes.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    String errorMessage = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertXpathEvaluatesTo(exception.getClass().getName(), "/a:error/a:code", errorMessage);
    assertXpathEvaluatesTo(exceptionMessage, "/a:error/a:message", errorMessage);
  }

  @Test
  public void innerError() throws Exception {
    exceptionMapper.uriInfo.getQueryParameters().add("odata.debug", "true");
    final Exception exception = new UriSyntaxException(UriSyntaxException.INVALIDKEYPREDICATE.addContent("a"), new NumberFormatException());
    final Response response = exceptionMapper.toResponse(exception);
    assertNotNull(response);
    final String errorMessage = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertXpathExists("/a:error/a:innererror/text()", errorMessage);
  }
}
