package com.sap.core.odata.fit.ref;

import static org.junit.Assert.assertTrue;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.junit.Ignore;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * @author SAP AG
 */
public class ContentNegotiationTest extends AbstractRefTest {

  @Test
  public void formatOverwriteAcceptHeader() throws Exception {
    final HttpResponse response = callUri("?$format=xml", HttpHeaders.ACCEPT, IMAGE_GIF, HttpStatusCodes.OK);
    checkMediaType(response, HttpContentType.APPLICATION_XML_UTF8);
  }

  @Test
  public void formatXml() throws Exception {
    final HttpResponse response = callUri("?$format=xml");
    checkMediaType(response, HttpContentType.APPLICATION_XML_UTF8);
  }

  @Test
  @Ignore("JSON is currently not supported")
  public void formatJson() throws Exception {
    final HttpResponse response = callUri("?$format=json");
    checkMediaType(response, HttpContentType.APPLICATION_JSON_UTF8);
  }

  @Test
  public void formatAtom() throws Exception {
    final HttpResponse response = callUri("Rooms('1')?$format=atom");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + "; type=entry");
  }

  @Test
  public void formatNotSupported() throws Exception {
    callUri("?$format=XXXML", HttpStatusCodes.NOT_ACCEPTABLE);
  }

  @Test
  public void contentTypeMetadata() throws Exception {
    final HttpResponse response = callUri("$metadata");
    checkMediaType(response, HttpContentType.APPLICATION_XML_UTF8);
  }

  @Test
  public void contentTypeMetadataNotAccepted() throws Exception {
    callUri("$metadata", HttpHeaders.ACCEPT, IMAGE_GIF, HttpStatusCodes.NOT_ACCEPTABLE);
  }

  @Test
  public void browserAcceptHeader() throws Exception {
    final HttpResponse response = callUri("$metadata",
        HttpHeaders.ACCEPT, "text/html, application/xhtml+xml, application/xml;q=0.9, */*;q=0.8",
        HttpStatusCodes.OK);
    checkMediaType(response, HttpContentType.APPLICATION_XML_UTF8);
  }

  @Test
  public void contentTypeServiceDocumentWoAcceptHeader() throws Exception {
    final HttpResponse response = callUri("");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_SVC_UTF8);
    assertTrue(getBody(response).length() > 100);
  }

  @Test
  public void contentTypeServiceDocumentAtomXmlNotAccept() throws Exception {
    final HttpResponse response = callUri("", HttpHeaders.ACCEPT, HttpContentType.APPLICATION_ATOM_XML, HttpStatusCodes.NOT_ACCEPTABLE);
    checkMediaType(response, HttpContentType.APPLICATION_XML);
    String body = getBody(response);
    assertTrue(body.length() > 100);
    assertTrue(body.contains("error"));
  }

  @Test
  public void contentTypeServiceDocumentXml() throws Exception {
    final HttpResponse response = callUri("", HttpHeaders.ACCEPT, HttpContentType.APPLICATION_XML, HttpStatusCodes.OK);
    checkMediaType(response, HttpContentType.APPLICATION_XML_UTF8);
    assertTrue(getBody(response).length() > 100);
  }

  @Test
  public void contentTypeServiceDocumentAtomSvcXml() throws Exception {
    final HttpResponse response = callUri("", HttpHeaders.ACCEPT, HttpContentType.APPLICATION_ATOM_SVC, HttpStatusCodes.OK);
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_SVC_UTF8);
    assertTrue(getBody(response).length() > 100);
  }

  @Test
  public void contentTypeServiceDocumentAcceptHeaders() throws Exception {
    final HttpResponse response = callUri("",
        HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
        HttpStatusCodes.OK);
    checkMediaType(response, HttpContentType.APPLICATION_XML_UTF8);
    assertTrue(getBody(response).length() > 100);
  }

  @Test
  public void requestContentTypeDifferent() throws Exception {
    final HttpResponse response = postUri("Rooms",
        getBody(callUri("Rooms('1')")), HttpContentType.APPLICATION_XML,
        HttpStatusCodes.CREATED);
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + "; type=entry");
    assertTrue(getBody(response).length() > 100);
  }
}
