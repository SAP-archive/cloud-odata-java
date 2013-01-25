package com.sap.core.odata.fit.ref;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.exceptions.XpathException;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * Tests employing the reference scenario reading the service document in XML format
 * @author SAP AG
 */
public class ServiceDocumentTest extends AbstractRefTest {

  @Test
  public void serviceDocumentDefault() throws Exception {
    HttpResponse response = callUri("");
    final String payload = getBody(response);

    //
    String contentTypeHeader = response.getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue();
    assertEquals("application/atomsvc+xml; charset=utf-8", contentTypeHeader);
    
    validateXmlServiceDocument(payload);
  }
  
  @Test
  public void serviceDocumentDollarFormatAtom() throws Exception {
    HttpResponse response = callUri("?$format=atom");
    final String payload = getBody(response);

    //
    String contentTypeHeader = response.getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue();
    assertEquals("application/atomsvc+xml; charset=utf-8", contentTypeHeader);
    
    validateXmlServiceDocument(payload);
  }

  @Test
  public void serviceDocumentDollarFormatXml() throws Exception {
    HttpResponse response = callUri("?$format=xml");
    final String payload = getBody(response);

    //
    String contentTypeHeader = response.getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue();
    assertEquals("application/xml; charset=utf-8", contentTypeHeader);
    
    validateXmlServiceDocument(payload);
  }

  
  @Test
  public void serviceDocumentAcceptHeaderAtom() throws Exception {
    HttpResponse response = callUri("", HttpHeaders.ACCEPT, "application/atom+xml");
    final String payload = getBody(response);

    //
    String contentTypeHeader = response.getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue();
    assertEquals("application/atomsvc+xml; charset=utf-8", contentTypeHeader);
    
    //
    validateXmlServiceDocument(payload);
  }

  @Test
  public void serviceDocumentAcceptHeaderUtf8Atom() throws Exception {
    HttpResponse response = callUri("", HttpHeaders.ACCEPT, "application/atom+xml; charset=utf-8");
    final String payload = getBody(response);

    //
    String contentTypeHeader = response.getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue();
    assertEquals("application/atomsvc+xml; charset=utf-8", contentTypeHeader);
    
    //
    validateXmlServiceDocument(payload);
  }

  private void validateXmlServiceDocument(final String payload) throws IOException, SAXException, XpathException {
    Map<String, String> prefixMap = new HashMap<String, String>();
    prefixMap.put("atom", "http://www.w3.org/2005/Atom");
    prefixMap.put("a", "http://www.w3.org/2007/app");
    XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(prefixMap));

    assertXpathExists("/a:service", payload);
    assertXpathExists("/a:service/a:workspace", payload);
    assertXpathExists("/a:service/a:workspace/atom:title", payload);
    assertXpathExists("/a:service/a:workspace/a:collection[@href=\"Employees\"]", payload);
    assertXpathExists("/a:service/a:workspace/a:collection[@href=\"Employees\"]/atom:title", payload);
    assertXpathExists("/a:service/a:workspace/a:collection[@href=\"Teams\"]", payload);
    assertXpathExists("/a:service/a:workspace/a:collection[@href=\"Teams\"]/atom:title", payload);
    assertXpathExists("/a:service/a:workspace/a:collection[@href=\"Rooms\"]", payload);
    assertXpathExists("/a:service/a:workspace/a:collection[@href=\"Rooms\"]/atom:title", payload);
    assertXpathExists("/a:service/a:workspace/a:collection[@href=\"Managers\"]", payload);
    assertXpathExists("/a:service/a:workspace/a:collection[@href=\"Managers\"]/atom:title", payload);
    assertXpathExists("/a:service/a:workspace/a:collection[@href=\"Buildings\"]", payload);
    assertXpathExists("/a:service/a:workspace/a:collection[@href=\"Buildings\"]/atom:title", payload);
    assertXpathExists("/a:service/a:workspace/a:collection[@href=\"Container2.Photos\"]", payload);
    assertXpathExists("/a:service/a:workspace/a:collection[@href=\"Container2.Photos\"]/atom:title", payload);
  }

  @Test
  public void serviceDocumentAcceptHeaderXml() throws Exception {
    HttpResponse response = callUri("", HttpHeaders.ACCEPT, "application/xml");
    final String payload = getBody(response);

    //
    String contentTypeHeader = response.getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue();
    assertEquals("application/xml; charset=utf-8", contentTypeHeader);
    
    validateXmlServiceDocument(payload);
  }

  @Test
  public void serviceDocumentAcceptHeaderUtf8Xml() throws Exception {
    HttpResponse response = callUri("", HttpHeaders.ACCEPT, "application/xml; charset=utf-8");
    final String payload = getBody(response);

    //
    String contentTypeHeader = response.getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue();
    assertEquals("application/xml; charset=utf-8", contentTypeHeader);
    
    validateXmlServiceDocument(payload);
  }

  @Test
  @Ignore("Current implementation does not support JSON. See 'serviceDocumentDollarFormatJsonUnsupported'")
  public void serviceDocumentDollarFormatJson() throws Exception {
    
    HttpResponse response = callUri("?$format=json");
    final String payload = getBody(response);

    //
    String contentTypeHeader = response.getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue();
    assertEquals("application/json; charset=utf-8", contentTypeHeader);

    assertTrue(payload.contains("EntitySets"));
    assertTrue(payload.contains("Employees"));
    assertTrue(payload.contains("Teams"));
    assertTrue(payload.contains("Rooms"));
    assertTrue(payload.contains("Managers"));
    assertTrue(payload.contains("Buildings"));
    assertTrue(payload.contains("Container2.Photos"));
  }
  @Test
  @Ignore("Current implementation does not support JSON. See 'serviceDocumentDollarFormatJsonUnsupported'")
  public void serviceDocumentAcceptHeaderJson() throws Exception {
    
    HttpResponse response = callUri("", HttpHeaders.ACCEPT, "application/json");
    final String payload = getBody(response);

    //
    String contentTypeHeader = response.getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue();
    assertEquals("application/json; charset=utf-8", contentTypeHeader);

    assertTrue(payload.contains("EntitySets"));
    assertTrue(payload.contains("Employees"));
    assertTrue(payload.contains("Teams"));
    assertTrue(payload.contains("Rooms"));
    assertTrue(payload.contains("Managers"));
    assertTrue(payload.contains("Buildings"));
    assertTrue(payload.contains("Container2.Photos"));
  }

  /**
   * Test for currently not supported JSON service document request.
   * If JSON format is implemented replace this test with @see {@link #serviceDocumentAcceptHeaderJson()}
   * 
   * @throws Exception
   */
  @Test
  public void serviceDocumentAcceptHeaderJsonUnsupported() throws Exception {
    
    HttpResponse response = callUri("?$format=json", HttpStatusCodes.NOT_ACCEPTABLE);
    
    String body = getBody(response);
    Map<String, String> prefixMap = new HashMap<String, String>();
    prefixMap.put("a", "http://schemas.microsoft.com/ado/2007/08/dataservices/metadata");
    XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(prefixMap));
    assertXpathExists("/a:error", body);
    assertXpathExists("/a:error/a:code", body);
    assertXpathExists("/a:error/a:message", body);
  }

  /**
   * Test for currently not supported JSON service document request.
   * If JSON format is implemented replace this test with @see {@link #serviceDocumentDollarFormatJson()}
   * 
   * @throws Exception
   */
  @Test
  public void serviceDocumentDollarFormatJsonUnsupported() throws Exception {
    callUri("", HttpHeaders.ACCEPT, "application/json", HttpStatusCodes.NOT_ACCEPTABLE);
  }

  @Test
  public void serviceDocumentAcceptHeaderInvalidCharset() throws Exception {
    HttpResponse response = callUri("", HttpHeaders.ACCEPT, "application/xml; charset=iso-latin-1", HttpStatusCodes.NOT_ACCEPTABLE);
    
    String body = getBody(response);
    Map<String, String> prefixMap = new HashMap<String, String>();
    prefixMap.put("a", "http://schemas.microsoft.com/ado/2007/08/dataservices/metadata");
    XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(prefixMap));
    assertXpathExists("/a:error", body);
    assertXpathExists("/a:error/a:code", body);
    assertXpathExists("/a:error/a:message", body);
  }
}
