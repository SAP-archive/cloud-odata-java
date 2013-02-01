package com.sap.core.odata.fit.ref;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
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

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.edm.Edm;

/**
 * Tests employing the reference scenario reading the service document in XML format
 * @author SAP AG
 */
public class ServiceTest extends AbstractRefTest {

  @Test
  public void serviceDocument() throws Exception {
    final HttpResponse response = callUri("/");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_SVC_UTF8);
    assertTrue(getBody(response).contains("Employees"));

    notFound("invalid.svc");
  }

  @Test
  public void serviceDocumentDefault() throws Exception {
    final HttpResponse response = callUri("");

    checkMediaType(response, HttpContentType.APPLICATION_ATOM_SVC_UTF8);

    final String payload = getBody(response);
    validateXmlServiceDocument(payload);
  }

  @Test
  public void serviceDocumentDollarFormatAtom() throws Exception {
    HttpResponse response = callUri("?$format=atom");
    final String payload = getBody(response);

    checkMediaType(response, HttpContentType.APPLICATION_ATOM_SVC_UTF8);

    validateXmlServiceDocument(payload);
  }

  @Test
  public void serviceDocumentDollarFormatXml() throws Exception {
    HttpResponse response = callUri("?$format=xml");
    final String payload = getBody(response);

    checkMediaType(response, HttpContentType.APPLICATION_XML_UTF8);

    validateXmlServiceDocument(payload);
  }

  @Test
  public void serviceDocumentAcceptHeaderAtom() throws Exception {
    HttpResponse response = callUri("", HttpHeaders.ACCEPT, HttpContentType.APPLICATION_ATOM_XML);
    final String payload = getBody(response);

    checkMediaType(response, HttpContentType.APPLICATION_ATOM_SVC_UTF8);

    validateXmlServiceDocument(payload);
  }

  @Test
  public void serviceDocumentAcceptHeaderUtf8Atom() throws Exception {
    HttpResponse response = callUri("", HttpHeaders.ACCEPT, HttpContentType.APPLICATION_ATOM_XML_UTF8);
    final String payload = getBody(response);

    checkMediaType(response, HttpContentType.APPLICATION_ATOM_SVC_UTF8);
    validateXmlServiceDocument(payload);
  }

  private void validateXmlServiceDocument(final String payload) throws IOException, SAXException, XpathException {
    Map<String, String> prefixMap = new HashMap<String, String>();
    prefixMap.put("atom", Edm.NAMESPACE_ATOM_2005);
    prefixMap.put("a", Edm.NAMESPACE_APP_2007);
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
    HttpResponse response = callUri("", HttpHeaders.ACCEPT, HttpContentType.APPLICATION_XML);
    final String payload = getBody(response);

    checkMediaType(response, HttpContentType.APPLICATION_XML_UTF8);

    validateXmlServiceDocument(payload);
  }

  @Test
  public void serviceDocumentAcceptHeaderUtf8Xml() throws Exception {
    HttpResponse response = callUri("", HttpHeaders.ACCEPT, HttpContentType.APPLICATION_XML_UTF8);
    final String payload = getBody(response);

    checkMediaType(response, HttpContentType.APPLICATION_XML_UTF8);

    validateXmlServiceDocument(payload);
  }

  @Test
  @Ignore("Current implementation does not support JSON. See 'serviceDocumentDollarFormatJsonUnsupported'")
  public void serviceDocumentDollarFormatJson() throws Exception {
    HttpResponse response = callUri("?$format=json");
    final String payload = getBody(response);

    checkMediaType(response, HttpContentType.APPLICATION_JSON_UTF8);

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

    HttpResponse response = callUri("", HttpHeaders.ACCEPT, HttpContentType.APPLICATION_JSON);
    final String payload = getBody(response);

    checkMediaType(response, HttpContentType.APPLICATION_JSON_UTF8);

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
   */
  @Test
  public void serviceDocumentAcceptHeaderJsonUnsupported() throws Exception {
    HttpResponse response = callUri("?$format=json", HttpStatusCodes.NOT_ACCEPTABLE);

    String body = getBody(response);
    Map<String, String> prefixMap = new HashMap<String, String>();
    prefixMap.put("a", Edm.NAMESPACE_M_2007_08);
    XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(prefixMap));
    assertXpathExists("/a:error", body);
    assertXpathExists("/a:error/a:code", body);
    assertXpathExists("/a:error/a:message", body);
  }

  /**
   * Test for currently not supported JSON service document request.
   * If JSON format is implemented replace this test with @see {@link #serviceDocumentDollarFormatJson()}
   */
  @Test
  public void serviceDocumentDollarFormatJsonUnsupported() throws Exception {
    callUri("", HttpHeaders.ACCEPT, HttpContentType.APPLICATION_JSON, HttpStatusCodes.NOT_ACCEPTABLE);
  }

  @Test
  public void serviceDocumentAcceptHeaderInvalidCharset() throws Exception {
    HttpResponse response = callUri("", HttpHeaders.ACCEPT, HttpContentType.APPLICATION_XML + "; charset=iso-latin-1", HttpStatusCodes.NOT_ACCEPTABLE);

    String body = getBody(response);
    Map<String, String> prefixMap = new HashMap<String, String>();
    prefixMap.put("a", Edm.NAMESPACE_M_2007_08);
    XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(prefixMap));
    assertXpathExists("/a:error", body);
    assertXpathExists("/a:error/a:code", body);
    assertXpathExists("/a:error/a:message", body);
  }
}
