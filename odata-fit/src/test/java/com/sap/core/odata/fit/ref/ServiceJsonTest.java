package com.sap.core.odata.fit.ref;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Ignore;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.edm.Edm;

/**
 * Tests employing the reference scenario reading the service document in JSON format
 * @author SAP AG
 */
public class ServiceJsonTest extends AbstractRefTest {
  @Test
  @Ignore("Current implementation does not support JSON. See 'serviceDocumentDollarFormatJsonUnsupported'")
  public void serviceDocumentDollarFormatJson() throws Exception {
    HttpResponse response = callUri("?$format=json");

    checkMediaType(response, HttpContentType.APPLICATION_JSON_UTF8);

    final String payload = getBody(response);
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
