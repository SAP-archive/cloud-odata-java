package com.sap.core.odata.core.rest;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotAcceptableException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.PathSegment;
import com.sap.core.odata.api.uri.UriParser;
import com.sap.core.odata.core.Dispatcher;
import com.sap.core.odata.core.ODataContextImpl;
import com.sap.core.odata.core.PathInfoImpl;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.commons.ODataHttpMethod;
import com.sap.core.odata.core.uri.UriInfoImpl;
import com.sap.core.odata.core.uri.UriParserImpl;
import com.sap.core.odata.testutil.fit.BaseTest;

/**
 * @author SAP AG
 */
public class ODataSubLocatorTest extends BaseTest {
  
  @Test
  public void testContentNegotiationEmptyRequest() throws Exception {
    ODataSubLocator locator = new ODataSubLocator();

    List<ContentType> contentTypes = contentTypes();
    List<ContentType> supportedContentTypes = contentTypes("sup/111", "sup/222");
    ContentType contentType = locator.contentNegotiation(contentTypes, supportedContentTypes);

    assertEquals("sup/111", contentType.toContentTypeString());
  }

  @Test
  public void testContentNegotiationConcreteRequest() throws Exception {
    ODataSubLocator locator = new ODataSubLocator();

    List<ContentType> contentTypes = contentTypes("sup/222");
    List<ContentType> supportedContentTypes = contentTypes("sup/111", "sup/222");
    ContentType contentType = locator.contentNegotiation(contentTypes, supportedContentTypes);

    assertEquals("sup/222", contentType.toContentTypeString());
  }

  @Test(expected = ODataNotAcceptableException.class)
  public void testContentNegotiationNotSupported() throws Exception {
    ODataSubLocator locator = new ODataSubLocator();

    List<ContentType> contentTypes = contentTypes("image/gif");
    List<ContentType> supportedContentTypes = contentTypes("sup/111", "sup/222");
    ContentType contentType = locator.contentNegotiation(contentTypes, supportedContentTypes);

    assertEquals("sup/222", contentType.toContentTypeString());
  }

  @Test
  public void testContentNegotiationSupportedWildcard() throws Exception {
    ODataSubLocator locator = new ODataSubLocator();

    List<ContentType> contentTypes = contentTypes("image/gif");
    List<ContentType> supportedContentTypes = contentTypes("sup/111", "sup/222", "*/*");
    ContentType contentType = locator.contentNegotiation(contentTypes, supportedContentTypes);

    assertEquals("image/gif", contentType.toContentTypeString());
  }

  @Test
  public void testContentNegotiationSupportedSubWildcard() throws Exception {
    ODataSubLocator locator = new ODataSubLocator();

    List<ContentType> contentTypes = contentTypes("image/gif");
    List<ContentType> supportedContentTypes = contentTypes("sup/111", "sup/222", "image/*");
    ContentType contentType = locator.contentNegotiation(contentTypes, supportedContentTypes);

    assertEquals("image/gif", contentType.toContentTypeString());
  }

  @Test
  public void testContentNegotiationRequestWildcard() throws Exception {
    ODataSubLocator locator = new ODataSubLocator();

    List<ContentType> contentTypes = contentTypes("*/*");
    List<ContentType> supportedContentTypes = contentTypes("sup/111", "sup/222");
    ContentType contentType = locator.contentNegotiation(contentTypes, supportedContentTypes);

    assertEquals("sup/111", contentType.toContentTypeString());
  }

  @Test
  public void testContentNegotiationRequestSubWildcard() throws Exception {
    ODataSubLocator locator = new ODataSubLocator();

    List<ContentType> contentTypes = contentTypes("sup/*", "*/*");
    List<ContentType> supportedContentTypes = contentTypes("bla/111", "sup/222");
    ContentType contentType = locator.contentNegotiation(contentTypes, supportedContentTypes);

    assertEquals("sup/222", contentType.toContentTypeString());
  }

  @Test
  public void testContentNegotiationRequestSubtypeWildcard() throws Exception {
    ODataSubLocator locator = new ODataSubLocator();

    List<ContentType> contentTypes = contentTypes("sup2/*");
    List<ContentType> supportedContentTypes = contentTypes("sup1/111", "sup2/222", "sup2/333");
    ContentType contentType = locator.contentNegotiation(contentTypes, supportedContentTypes);

    assertEquals("sup2/222", contentType.toContentTypeString());
  }

  @Test
  public void testContentNegotiationRequestResponseWildcard() throws Exception {
    ODataSubLocator locator = new ODataSubLocator();

    List<ContentType> contentTypes = contentTypes("*/*");
    List<ContentType> supportedContentTypes = contentTypes("*/*");
    ContentType contentType = locator.contentNegotiation(contentTypes, supportedContentTypes);

    assertEquals("*/*", contentType.toContentTypeString());
  }

  @Test
  public void testContentNegotiationManyRequests() throws Exception {
    ODataSubLocator locator = new ODataSubLocator();

    List<ContentType> contentTypes = contentTypes("bla/111", "bla/blub", "sub2/222");
    List<ContentType> supportedContentTypes = contentTypes("sub1/666", "sub2/222", "sub3/333");
    ContentType contentType = locator.contentNegotiation(contentTypes, supportedContentTypes);

    assertEquals("sub2/222", contentType.toContentTypeString());
  }

  
  
  @Test
  @Ignore
  public void testContentNegotiationDefaultCharset_OLD() throws Exception {
    ODataSubLocator locator = new ODataSubLocator();

    UriParser parser = mockUriParser(locator);
    UriInfoImpl uriInfo = new UriInfoImpl();
    Mockito.when(parser.parse(Mockito.anyListOf(PathSegment.class), Mockito.anyMapOf(String.class, String.class))).thenReturn(uriInfo);
    ODataService service = mockODataService(locator);
    ODataContextImpl context = mockODataContext(locator);
    Mockito.when(context.getPathInfo()).thenReturn(new PathInfoImpl());
    Dispatcher dispatcher = mockDispatcher(locator);
    String contentHeader = "application/xml; charset=utf-8";
    ODataResponse odataResponse = ODataResponse.status(HttpStatusCodes.OK).contentHeader(contentHeader).build();
    Mockito.when(dispatcher.dispatch(Mockito.any(ODataHttpMethod.class), 
        Mockito.any(UriInfoImpl.class), 
        Mockito.any(InputStream.class), 
        Mockito.anyString(), 
        Mockito.refEq(contentHeader))).thenReturn(odataResponse);
    List<ContentType> acceptedContentTypes = contentTypes("application/xml");
    setField(locator, "acceptHeaderContentTypes", acceptedContentTypes);
    List<String> supportedContentTypes = Arrays.asList("application/xml; charset=utf-8");
    Mockito.when(service.getSupportedContentTypes(Mockito.any(Class.class))).thenReturn(supportedContentTypes);

    // test
    Response response = locator.handleGet();
    String contentType = response.getHeaderString(HttpHeaders.CONTENT_TYPE);
    
    assertEquals("application/xml; charset=utf-8", contentType);
  }

  @Test
  public void testContentNegotiationDefaultCharset() throws Exception {
    ODataSubLocator locator = new ODataSubLocator();

    boolean asDollarFormat = true;
    String reqContentType = "application/xml";
    String supportedContentTypes = "application/xml; charset=utf-8";
    mockSubLocatorForContentNegotiation(locator, asDollarFormat, reqContentType, supportedContentTypes);
    
    // test
    Response response = locator.handleGet();
    String contentType = response.getHeaderString(HttpHeaders.CONTENT_TYPE);
    
    assertEquals("application/xml; charset=utf-8", contentType);
  }

  @Test
  public void testContentNegotiationDefaultCharsetAsDollarFormat() throws Exception {
    ODataSubLocator locator = new ODataSubLocator();

    boolean asDollarFormat = true;
    String reqContentType = "application/xml";
    String supportedContentTypes = "application/xml; charset=utf-8";
    mockSubLocatorForContentNegotiation(locator, asDollarFormat, reqContentType, supportedContentTypes);
    
    // test
    Response response = locator.handleGet();
    String contentType = response.getHeaderString(HttpHeaders.CONTENT_TYPE);
    
    assertEquals("application/xml; charset=utf-8", contentType);
  }


  @Test
  public void testContentNegotiationSupportedCharset() throws Exception {
    // prepare
    ODataSubLocator locator = new ODataSubLocator();
    boolean asDollarFormat = false;
    String reqContentType = "application/xml; charset=utf-8";
    String supportedContentTypes = "application/xml; charset=utf-8";
    mockSubLocatorForContentNegotiation(locator, asDollarFormat, reqContentType, supportedContentTypes);
    
    // test
    Response response = locator.handleGet();
    
    // verify
    String contentType = response.getHeaderString(HttpHeaders.CONTENT_TYPE);
    assertEquals("application/xml; charset=utf-8", contentType);
  }

  @Test
  public void testContentNegotiationSupportedCharsetAsDollarFormat() throws Exception {
    // prepare
    ODataSubLocator locator = new ODataSubLocator();
    boolean asDollarFormat = true;
    String reqContentType = "application/xml; charset=utf-8";
    String supportedContentTypes = "application/xml; charset=utf-8";
    mockSubLocatorForContentNegotiation(locator, asDollarFormat, reqContentType, supportedContentTypes);
    
    // test
    Response response = locator.handleGet();
    
    // verify
    String contentType = response.getHeaderString(HttpHeaders.CONTENT_TYPE);
    assertEquals("application/xml; charset=utf-8", contentType);
  }

  @Test(expected = ODataNotAcceptableException.class)
  public void testContentNegotiationCharsetNotSupported() throws Exception {
    ODataSubLocator locator = new ODataSubLocator();

    List<ContentType> contentTypes = contentTypes("text/plain; charset=iso-8859-1");
    List<ContentType> supportedContentTypes = contentTypes("sup/111", "sup/222");
    ContentType contentType = locator.contentNegotiation(contentTypes, supportedContentTypes);

    assertEquals("sup/222", contentType.toContentTypeString());
  }

  
  @SuppressWarnings("unchecked")
  private void mockSubLocatorForContentNegotiation(ODataSubLocator locator, boolean asFormat, String reqContentType, String ... supportedContentTypes) 
        throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, ODataException {
    
    UriParser parser = mockUriParser(locator);
    
    UriInfoImpl uriInfo = new UriInfoImpl();
    if(asFormat) {
      uriInfo.setFormat(reqContentType);
    } else {
      List<ContentType> acceptedContentTypes = contentTypes(reqContentType);
      setField(locator, "acceptHeaderContentTypes", acceptedContentTypes);
    }
    
    Mockito.when(parser.parse(Mockito.anyList(), Mockito.anyMap())).thenReturn(uriInfo);
    ODataService service = mockODataService(locator);
    ODataContextImpl context = mockODataContext(locator);
    Mockito.when(context.getPathInfo()).thenReturn(new PathInfoImpl());
    Dispatcher dispatcher = mockDispatcher(locator);
    String contentHeader = "application/xml; charset=utf-8";
    ODataResponse odataResponse = ODataResponse.status(HttpStatusCodes.OK).contentHeader(contentHeader).build();
    Mockito.when(dispatcher.dispatch(Mockito.any(ODataHttpMethod.class), 
        Mockito.any(UriInfoImpl.class), 
        Mockito.any(InputStream.class), 
        Mockito.anyString(), 
        Mockito.refEq(contentHeader))).thenReturn(odataResponse);
    Mockito.when(service.getSupportedContentTypes(Mockito.any(Class.class))).thenReturn(Arrays.asList(supportedContentTypes));
  }
  
  private Dispatcher mockDispatcher(ODataSubLocator locator) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
    Dispatcher dispatcher = Mockito.mock(Dispatcher.class);
    setField(locator, "dispatcher", dispatcher);
    return dispatcher;
  }

  private ODataContextImpl mockODataContext(ODataSubLocator locator) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
    ODataContextImpl context = Mockito.mock(ODataContextImpl.class);
    setField(locator, "context", context);
    return context;
  }

  private ODataService mockODataService(ODataSubLocator locator) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
    ODataService service = Mockito.mock(ODataService.class);
    setField(locator, "service", service);
    return service;
  }

  private UriParser mockUriParser(ODataSubLocator locator) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
    UriParser parser = Mockito.mock(UriParserImpl.class);
    setField(locator, "uriParser", parser);
    return parser;
  }
  
  private static void setField(Object instance, String fieldname, Object value) throws SecurityException, 
      NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
    Field field = instance.getClass().getDeclaredField(fieldname);
    boolean access = field.isAccessible();
    field.setAccessible(true);
    
    field.set(instance, value);
    
    field.setAccessible(access);
  }
 
  private List<ContentType> contentTypes(String... contentType) {
    List<ContentType> ctList = new ArrayList<ContentType>();
    for (String ct : contentType) {
      ctList.add(ContentType.create(ct));
    }
    return ctList;
  }
}