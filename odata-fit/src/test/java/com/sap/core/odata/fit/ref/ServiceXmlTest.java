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
public class ServiceXmlTest extends AbstractRefTest {

  @Test
  public void serviceDocument() throws Exception {
    final HttpResponse response = callUri("/");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_SVC_UTF8);
    final String body = getBody(response);
    assertTrue(body.contains("Employees"));
    assertTrue(body.contains("Teams"));
    assertTrue(body.contains("Rooms"));
    assertTrue(body.contains("Managers"));
    assertTrue(body.contains("Buildings"));
    assertTrue(body.contains("Container2.Photos"));

    notFound("invalid.svc");
  }

  @Test
  public void serviceDocumentDefault() throws Exception {
    final HttpResponse response = callUri("");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_SVC_UTF8);
    validateXmlServiceDocument(getBody(response));
  }

  @Test
  public void serviceDocumentDollarFormatAtom() throws Exception {
    HttpResponse response = callUri("?$format=atom");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_SVC_UTF8);
    validateXmlServiceDocument(getBody(response));
  }

  @Test
  public void serviceDocumentDollarFormatXml() throws Exception {
    HttpResponse response = callUri("?$format=xml");
    checkMediaType(response, HttpContentType.APPLICATION_XML_UTF8);
    validateXmlServiceDocument(getBody(response));
  }

  @Test
  public void serviceDocumentAcceptHeaderAtom() throws Exception {
    final HttpResponse response = callUri("", HttpHeaders.ACCEPT, HttpContentType.APPLICATION_ATOM_XML, HttpStatusCodes.NOT_ACCEPTABLE);
    checkMediaType(response, HttpContentType.APPLICATION_XML);
    validateXmlError(getBody(response));
  }

  @Test
  public void serviceDocumentAcceptHeaderUtf8Atom() throws Exception {
    final HttpResponse response = callUri("", HttpHeaders.ACCEPT, HttpContentType.APPLICATION_ATOM_XML_UTF8, HttpStatusCodes.NOT_ACCEPTABLE);
    checkMediaType(response, HttpContentType.APPLICATION_XML);
    validateXmlError(getBody(response));
  }

  @Test
  public void serviceDocumentAcceptHeaderXml() throws Exception {
    HttpResponse response = callUri("", HttpHeaders.ACCEPT, HttpContentType.APPLICATION_XML);
    checkMediaType(response, HttpContentType.APPLICATION_XML_UTF8);
    validateXmlServiceDocument(getBody(response));
  }

  @Test
  public void serviceDocumentAcceptHeaderUtf8Xml() throws Exception {
    HttpResponse response = callUri("", HttpHeaders.ACCEPT, HttpContentType.APPLICATION_XML_UTF8);
    checkMediaType(response, HttpContentType.APPLICATION_XML_UTF8);
    validateXmlServiceDocument(getBody(response));
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

  private void validateXmlError(final String payload) throws XpathException, IOException, SAXException {
    Map<String, String> prefixMap = new HashMap<String, String>();
    prefixMap.put("a", Edm.NAMESPACE_M_2007_08);
    XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(prefixMap));

    assertXpathExists("a:error", payload);
    assertXpathExists("/a:error/a:code", payload);
    assertXpathExists("/a:error/a:message[@xml:lang=\"en\"]", payload);
  }
}
