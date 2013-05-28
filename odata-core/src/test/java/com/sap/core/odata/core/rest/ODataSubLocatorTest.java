package com.sap.core.odata.core.rest;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.commons.ODataHttpMethod;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.UriParser;
import com.sap.core.odata.core.Dispatcher;
import com.sap.core.odata.core.ODataContextImpl;
import com.sap.core.odata.core.PathInfoImpl;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.uri.UriInfoImpl;
import com.sap.core.odata.core.uri.UriParserImpl;
import com.sap.core.odata.testutil.fit.BaseTest;

/**
 * @author SAP AG
 */
@Ignore
public class ODataSubLocatorTest extends BaseTest {

  private void negotiateContentTypeCharset(final String requestType, final String supportedType, final boolean asFormat)
      throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, ODataException {
    ODataSubLocator locator = new ODataSubLocator();
    mockSubLocatorForContentNegotiation(locator, asFormat, requestType, supportedType);

    assertEquals(supportedType, locator.handleGet().getHeaderString(HttpHeaders.CONTENT_TYPE));
  }

  @Test
  public void contentNegotiationDefaultCharset() throws Exception {
    negotiateContentTypeCharset("application/xml", "application/xml; charset=utf-8", false);
  }

  @Test
  public void contentNegotiationDefaultCharsetAsDollarFormat() throws Exception {
    negotiateContentTypeCharset("application/xml", "application/xml; charset=utf-8", true);
  }

  @Test
  public void contentNegotiationSupportedCharset() throws Exception {
    negotiateContentTypeCharset("application/xml; charset=utf-8", "application/xml; charset=utf-8", false);
  }

  @Test
  public void contentNegotiationSupportedCharsetAsDollarFormat() throws Exception {
    negotiateContentTypeCharset("application/xml; charset=utf-8", "application/xml; charset=utf-8", true);
  }

  private void mockSubLocatorForContentNegotiation(final ODataSubLocator locator, final boolean asFormat, final String reqContentType, final String... supportedContentTypes)
      throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, ODataException {

    UriParser parser = mockUriParser(locator);

    UriInfoImpl uriInfo = new UriInfoImpl();
    if (asFormat) {
      uriInfo.setFormat(reqContentType);
    } else {
      List<ContentType> acceptedContentTypes = contentTypes(reqContentType);
      setField(locator, "acceptHeaderContentTypes", acceptedContentTypes);
    }

    Mockito.when(parser.parse(Matchers.anyListOf(com.sap.core.odata.api.uri.PathSegment.class), Matchers.anyMapOf(String.class, String.class))).thenReturn(uriInfo);
    mockODataService(locator);
    ODataContextImpl context = mockODataContext(locator);
    Mockito.when(context.getPathInfo()).thenReturn(new PathInfoImpl());
    Dispatcher dispatcher = mockDispatcher(locator);
    String contentHeader = "application/xml; charset=utf-8";
    ODataResponse odataResponse = ODataResponse.status(HttpStatusCodes.OK).contentHeader(contentHeader).build();
    Mockito.when(dispatcher.dispatch(Matchers.any(ODataHttpMethod.class),
        Matchers.any(UriInfoImpl.class),
        Matchers.any(InputStream.class),
        Matchers.anyString(),
        Matchers.anyListOf(String.class))).thenReturn(odataResponse);
  }

  private Dispatcher mockDispatcher(final ODataSubLocator locator) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
    Dispatcher dispatcher = Mockito.mock(Dispatcher.class);
    setField(locator, "dispatcher", dispatcher);
    return dispatcher;
  }

  private ODataContextImpl mockODataContext(final ODataSubLocator locator) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
    ODataContextImpl context = Mockito.mock(ODataContextImpl.class);
    setField(locator, "context", context);
    return context;
  }

  private ODataService mockODataService(final ODataSubLocator locator) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
    ODataService service = Mockito.mock(ODataService.class);
    setField(locator, "service", service);
    return service;
  }

  private UriParser mockUriParser(final ODataSubLocator locator) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
    UriParser parser = Mockito.mock(UriParserImpl.class);
    setField(locator, "uriParser", parser);
    return parser;
  }

  private static void setField(final Object instance, final String fieldname, final Object value) throws SecurityException,
      NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
    Field field = instance.getClass().getDeclaredField(fieldname);
    boolean access = field.isAccessible();
    field.setAccessible(true);

    field.set(instance, value);

    field.setAccessible(access);
  }

  private List<ContentType> contentTypes(final String... contentType) {
    List<ContentType> ctList = new ArrayList<ContentType>();
    for (String ct : contentType) {
      ctList.add(ContentType.create(ct));
    }
    return ctList;
  }
}