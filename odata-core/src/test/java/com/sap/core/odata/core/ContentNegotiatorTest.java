package com.sap.core.odata.core;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotAcceptableException;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.uri.UriInfoImpl;
import com.sap.core.odata.core.uri.UriType;

public class ContentNegotiatorTest {
  private void negotiateContentType(final List<ContentType> contentTypes, final List<ContentType> supportedTypes, final String expected) throws ODataException {
    final ContentType contentType = new ContentNegotiator().contentNegotiation(contentTypes, supportedTypes);
    assertEquals(expected, contentType.toContentTypeString());
  }
  
  @Test
  public void contentNegotiationEmptyRequest() throws Exception {
    negotiateContentType(
        contentTypes(),
        contentTypes("sup/111", "sup/222"),
        "sup/111");
  }
  
  @Test
  public void contentNegotiationConcreteRequest() throws Exception {
    negotiateContentType(
        contentTypes("sup/222"),
        contentTypes("sup/111", "sup/222"),
        "sup/222");
  }
  
  @Test(expected = ODataNotAcceptableException.class)
  public void contentNegotiationNotSupported() throws Exception {
    negotiateContentType(contentTypes("image/gif"), contentTypes("sup/111", "sup/222"), null);
  }
  
  @Test
  public void contentNegotiationSupportedWildcard() throws Exception {
    negotiateContentType(
        contentTypes("image/gif"),
        contentTypes("sup/111", "sup/222", "*/*"),
        "image/gif");
  }
  
  @Test
  public void contentNegotiationSupportedSubWildcard() throws Exception {
    negotiateContentType(
        contentTypes("image/gif"),
        contentTypes("sup/111", "sup/222", "image/*"),
        "image/gif");
  }
  
  @Test
  public void contentNegotiationRequestWildcard() throws Exception {
    negotiateContentType(
        contentTypes("*/*"),
        contentTypes("sup/111", "sup/222"),
        "sup/111");
  }
  
  @Test
  public void contentNegotiationRequestSubWildcard() throws Exception {
    negotiateContentType(
        contentTypes("sup/*", "*/*"),
        contentTypes("bla/111", "sup/222"),
        "sup/222");
  }
  
  @Test
  public void contentNegotiationRequestSubtypeWildcard() throws Exception {
    negotiateContentType(
        contentTypes("sup2/*"),
        contentTypes("sup1/111", "sup2/222", "sup2/333"),
        "sup2/222");
  }
  
  @Test
  public void contentNegotiationRequestResponseWildcard() throws Exception {
    negotiateContentType(contentTypes("*/*"), contentTypes("*/*"), "*/*");
  }
  
  @Test
  public void contentNegotiationManyRequests() throws Exception {
    negotiateContentType(
        contentTypes("bla/111", "bla/blub", "sub2/222"),
        contentTypes("sub1/666", "sub2/222", "sub3/333"),
        "sub2/222");
  }
  
  @Test(expected = ODataNotAcceptableException.class)
  public void contentNegotiationCharsetNotSupported() throws Exception {
    negotiateContentType(
        contentTypes("text/plain; charset=iso-8859-1"),
        contentTypes("sup/111", "sup/222"),
        "sup/222");
  }
  
  @Test
  public void contentNegotiationWithODataVerbose() throws Exception {
    negotiateContentType(
        contentTypes("text/plain; q=0.5", "application/json;odata=verbose;q=0.2", "*/*"),
        contentTypes("application/json; charset=utf-8", "sup/222"),
        "application/json; charset=utf-8");
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
  
  
  private void negotiateContentTypeCharset(final String requestType, final String supportedType, final boolean asFormat)
      throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, ODataException {
  
  
    UriInfoImpl uriInfo = new UriInfoImpl();
    uriInfo.setUriType(UriType.URI1); // 
    if (asFormat) {
      uriInfo.setFormat(requestType);
    }
    
    List<String> acceptedContentTypes = Arrays.asList(requestType);
    List<String> supportedContentTypes = Arrays.asList(supportedType);
  
    ContentNegotiator negotiator = new ContentNegotiator();
    String negotiatedContentType = negotiator.doContentNegotiation(uriInfo, acceptedContentTypes, supportedContentTypes);
    
    assertEquals(supportedType, negotiatedContentType);
  }
  
  private List<ContentType> contentTypes(final String... contentType) {
    List<ContentType> ctList = new ArrayList<ContentType>();
    for (String ct : contentType) {
      ctList.add(ContentType.create(ct));
    }
    return ctList;
  }
}

