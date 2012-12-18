package com.sap.core.odata.core;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathValuesEqual;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import junit.framework.Assert;

import org.custommonkey.xmlunit.NamespaceContext;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.sap.core.odata.api.enums.HttpStatusCodes;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotFoundException;
import com.sap.core.odata.api.uri.UriSyntaxException;
import com.sap.core.odata.core.exception.MessageService;
import com.sap.core.odata.core.exception.ODataRuntimeException;
import com.sap.core.odata.testutils.helper.StringHelper;

public class ODataExceptionMapperImplTest {

  ODataExceptionMapperImpl exceptionMapper;

  @Mock
  UriInfo mockedUriInfo;
  @Mock
  HttpHeaders mockedHttpHeaders;

  @BeforeClass
  public static void setup() throws Exception {
    Map<String, String> prefixMap = new HashMap<String, String>();
    prefixMap.put("a", "http://schemas.microsoft.com/ado/2007/08/dataservices/metadata");
    NamespaceContext ctx = new SimpleNamespaceContext(prefixMap);
    XMLUnit.setXpathNamespaceContext(ctx);
  }

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    exceptionMapper = new ODataExceptionMapperImpl();
    exceptionMapper.uriInfo = mockedUriInfo;
    exceptionMapper.httpHeaders = mockedHttpHeaders;
  }

  @Test
  public void testODataNotFoundException() throws Exception {
    // prepare
    Exception exception = new ODataNotFoundException(ODataNotFoundException.ENTITY);

    // execute
    Response response = exceptionMapper.toResponse(exception);

    // verify
    Assert.assertNotNull(response);
    Assert.assertEquals(HttpStatusCodes.NOT_FOUND.getStatusCode(), response.getStatus());
    String errorMessage = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertXpathValuesEqual("\"" + ODataNotFoundException.ENTITY.getKey() + "\"", "/a:error/a:code", errorMessage);
    assertXpathValuesEqual("\"" + MessageService.getMessage(Locale.ENGLISH, ODataNotFoundException.ENTITY).getText() + "\"", "/a:error/a:message", errorMessage);
  }

  @Test
  public void testODataNotFoundExceptionDe() throws Exception {
    // prepare
    Exception exception = new ODataNotFoundException(ODataNotFoundException.ENTITY);
    List<Locale> languages = new ArrayList<Locale>();
    languages.add(Locale.GERMAN);
    Mockito.when(mockedHttpHeaders.getAcceptableLanguages()).thenReturn(languages);

    // execute
    Response response = exceptionMapper.toResponse(exception);

    // verify
    Assert.assertNotNull(response);
    Assert.assertEquals(HttpStatusCodes.NOT_FOUND.getStatusCode(), response.getStatus());
    String errorMessage = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertXpathValuesEqual("\"" + ODataNotFoundException.ENTITY.getKey() + "\"", "/a:error/a:code", errorMessage);
    assertXpathValuesEqual("\"" + MessageService.getMessage(Locale.GERMAN, ODataNotFoundException.ENTITY).getText() + "\"", "/a:error/a:message", errorMessage);
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
    Assert.assertNotNull(response);
    Assert.assertEquals(HttpStatusCodes.NOT_FOUND.getStatusCode(), response.getStatus());
    String errorMessage = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertXpathValuesEqual("\"" + ODataNotFoundException.ENTITY.getKey() + "\"", "/a:error/a:code", errorMessage);
    assertXpathValuesEqual("\"" + MessageService.getMessage(Locale.ENGLISH, ODataNotFoundException.ENTITY).getText() + "\"", "/a:error/a:message", errorMessage);
  }

  @Test
  public void testODataApplicationException() throws Exception {
    // prepare
    String message = "expected exception message";
    Exception exception = new ODataApplicationException(message);

    // execute
    Response response = exceptionMapper.toResponse(exception);

    // verify
    Assert.assertNotNull(response);
    Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    String errorMessage = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertXpathValuesEqual("\"" + exception.getClass().getName() + "\"", "/a:error/a:code", errorMessage);
    assertXpathValuesEqual("\"expected exception message\"", "/a:error/a:message", errorMessage);
  }

  @Test
  public void testODataApplicationExceptionWrapped() throws Exception {
    // prepare
    String message = "expected exception message";
    Exception exception = new ODataException(new ODataApplicationException(message));

    // execute
    Response response = exceptionMapper.toResponse(exception);

    // verify
    Assert.assertNotNull(response);
    Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    String errorMessage = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertXpathValuesEqual("\"" + ODataApplicationException.class.getName() + "\"", "/a:error/a:code", errorMessage);
    assertXpathValuesEqual("\"expected exception message\"", "/a:error/a:message", errorMessage);
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
    Assert.assertNotNull(response);
    Assert.assertEquals(Status.OK.getStatusCode(), response.getStatus());
    String errorMessage = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertXpathValuesEqual("\"" + exception.getClass().getName() + "\"", "/a:error/a:code", errorMessage);
    assertXpathValuesEqual("\"expected exception message\"", "/a:error/a:message", errorMessage);
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
    assertXpathValuesEqual("\"" + ODataApplicationException.class.getName() + "\"", "/a:error/a:code", errorMessage);
    assertXpathValuesEqual("\"expected exception message\"", "/a:error/a:message", errorMessage);
  }

  @Test
  public void testUriParserException() throws Exception {
    // prepare
    Exception exception = new UriSyntaxException(UriSyntaxException.EMPTYSEGMENT);

    // execute
    Response response = exceptionMapper.toResponse(exception);

    // verify
    Assert.assertNotNull(response);
    Assert.assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    String errorMessage = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertXpathValuesEqual("\"" + UriSyntaxException.EMPTYSEGMENT.getKey() + "\"", "/a:error/a:code", errorMessage);
    assertXpathValuesEqual("\"" + MessageService.getMessage(Locale.ENGLISH, UriSyntaxException.EMPTYSEGMENT).getText() + "\"", "/a:error/a:message", errorMessage);
  }

  @Test
  public void testUriParserExceptionWrapped() throws Exception {
    // prepare
    Exception exception = new ODataException("outer exception", new UriSyntaxException(UriSyntaxException.EMPTYSEGMENT));

    // execute
    Response response = exceptionMapper.toResponse(exception);

    // verify
    Assert.assertNotNull(response);
    Assert.assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    String errorMessage = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertXpathValuesEqual("\"" + UriSyntaxException.EMPTYSEGMENT.getKey() + "\"", "/a:error/a:code", errorMessage);
    assertXpathValuesEqual("\"" + MessageService.getMessage(Locale.ENGLISH, UriSyntaxException.EMPTYSEGMENT).getText() + "\"", "/a:error/a:message", errorMessage);
  }

  @Test
  public void testIoException() throws Exception {
    // prepare
    String message = "expected exception message";
    Exception exception = new IOException(message);

    // execute
    Response response = exceptionMapper.toResponse(exception);

    // verify
    Assert.assertNotNull(response);
    Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    String errorMessage = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertXpathValuesEqual("\"" + exception.getClass().getName() + "\"", "/a:error/a:code", errorMessage);
    assertXpathValuesEqual("\"expected exception message\"", "/a:error/a:message", errorMessage);
  }

  @Test
  public void testODataException() throws Exception {
    // prepare
    String exceptionMessage = "Some odd exception";
    Exception exception = new ODataException(exceptionMessage);

    // execute
    Response response = exceptionMapper.toResponse(exception);

    // verify
    Assert.assertNotNull(response);
    Assert.assertEquals(HttpStatusCodes.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    String errorMessage = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertXpathValuesEqual("\"" + exception.getClass().getName() + "\"", "/a:error/a:code", errorMessage);
    assertXpathValuesEqual("\"Some odd exception\"", "/a:error/a:message", errorMessage);
  }

  @Test
  public void testODataRuntimeException() throws Exception {
    // prepare
    String exceptionMessage = "Some odd runtime exception";
    Exception exception = new ODataRuntimeException(exceptionMessage);

    // execute
    Response response = exceptionMapper.toResponse(exception);

    // verify
    Assert.assertNotNull(response);
    Assert.assertEquals(HttpStatusCodes.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    String errorMessage = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertXpathValuesEqual("\"" + exception.getClass().getName() + "\"", "/a:error/a:code", errorMessage);
    assertXpathValuesEqual("\"Some odd runtime exception\"", "/a:error/a:message", errorMessage);
  }
}
