package com.sap.core.odata.core.rest;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.sap.core.odata.api.exception.ODataNotAcceptableException;
import com.sap.core.odata.core.enums.ContentType;
import com.sap.core.odata.core.rest.ODataSubLocator;

public class ODataSubLocatorTest {

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
  

  @Test(expected=ODataNotAcceptableException.class)
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
  
  private List<ContentType> contentTypes(String ... contentType) {
    List<ContentType> ctList = new ArrayList<ContentType>();
    for (String ct: contentType) {
      ctList.add(ContentType.create(ct));
    }
    return ctList;
  }
}