package com.sap.core.odata.fit.ref;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.custommonkey.xmlunit.exceptions.XpathException;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * Tests employing the reference scenario reading the service document in XML format
 * @author SAP AG
 */
public class ServiceXmlTest extends AbstractRefXmlTest {

  @Test
  public void serviceDocument() throws Exception {
    final HttpResponse response = callUri("/");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_SVC_UTF8);
    final String body = getBody(response);
    assertXpathEvaluatesTo("Employees", "/app:service/app:workspace/app:collection[1]/@href", body);
    assertXpathEvaluatesTo("Teams", "/app:service/app:workspace/app:collection[2]/@href", body);
    assertXpathEvaluatesTo("Rooms", "/app:service/app:workspace/app:collection[3]/@href", body);
    assertXpathEvaluatesTo("Managers", "/app:service/app:workspace/app:collection[4]/@href", body);
    assertXpathEvaluatesTo("Buildings", "/app:service/app:workspace/app:collection[5]/@href", body);
    assertXpathEvaluatesTo("Container2.Photos", "/app:service/app:workspace/app:collection[6]/@href", body);

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
    assertXpathExists("/app:service", payload);
    assertXpathExists("/app:service/app:workspace", payload);
    assertXpathExists("/app:service/app:workspace/atom:title", payload);
    assertXpathExists("/app:service/app:workspace/app:collection[@href=\"Employees\"]", payload);
    assertXpathExists("/app:service/app:workspace/app:collection[@href=\"Employees\"]/atom:title", payload);
    assertXpathExists("/app:service/app:workspace/app:collection[@href=\"Teams\"]", payload);
    assertXpathExists("/app:service/app:workspace/app:collection[@href=\"Teams\"]/atom:title", payload);
    assertXpathExists("/app:service/app:workspace/app:collection[@href=\"Rooms\"]", payload);
    assertXpathExists("/app:service/app:workspace/app:collection[@href=\"Rooms\"]/atom:title", payload);
    assertXpathExists("/app:service/app:workspace/app:collection[@href=\"Managers\"]", payload);
    assertXpathExists("/app:service/app:workspace/app:collection[@href=\"Managers\"]/atom:title", payload);
    assertXpathExists("/app:service/app:workspace/app:collection[@href=\"Buildings\"]", payload);
    assertXpathExists("/app:service/app:workspace/app:collection[@href=\"Buildings\"]/atom:title", payload);
    assertXpathExists("/app:service/app:workspace/app:collection[@href=\"Container2.Photos\"]", payload);
    assertXpathExists("/app:service/app:workspace/app:collection[@href=\"Container2.Photos\"]/atom:title", payload);
  }
}
