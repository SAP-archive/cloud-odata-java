package com.sap.core.odata.fit.basic;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.testutil.mock.EdmTestProvider;

/**
 * @author SAP AG
 */
public class AcceptHeaderTypeTest extends AbstractBasicTest {

  private ODataSingleProcessor processor = mock(ODataSingleProcessor.class);

  @Override
  protected ODataSingleProcessor createProcessor() throws ODataException {
    String contentType = "application/atom+xml";
    ODataResponse response = ODataResponse.status(HttpStatusCodes.OK).contentHeader(contentType).entity("Test passed.").build();
    when(processor.readEntity(any(GetEntityUriInfo.class), any(String.class))).thenReturn(response);
    when(processor.readEntitySet(any(GetEntitySetUriInfo.class), any(String.class))).thenReturn(response);

    return processor;
  }

  @Override
  protected EdmProvider createEdmProvider() {
    return new EdmTestProvider();
  }

  private HttpResponse testGetRequest(final String uriExtension, final String acceptHeader, final HttpStatusCodes expectedStatus, final String expectedContentType)
      throws ClientProtocolException, IOException, ODataException {
    // prepare
    ODataResponse expectedResponse = ODataResponse.contentHeader(expectedContentType).entity("Test passed.").build();
    when(processor.readEntity(any(GetEntityUriInfo.class), any(String.class))).thenReturn(expectedResponse);
    when(processor.readEntitySet(any(GetEntitySetUriInfo.class), any(String.class))).thenReturn(expectedResponse);

    HttpGet getRequest = new HttpGet(URI.create(getEndpoint().toString() + uriExtension));
    getRequest.setHeader(HttpHeaders.ACCEPT, acceptHeader);

    // execute
    HttpResponse response = getHttpClient().execute(getRequest);

    // validate
    assertEquals(expectedStatus.getStatusCode(), response.getStatusLine().getStatusCode());
    Header[] contentTypeHeaders = response.getHeaders("Content-Type");
    assertEquals("Found more then one content type header in response.", 1, contentTypeHeaders.length);
    assertEquals("Received content type does not match expected.", expectedContentType, contentTypeHeaders[0].getValue());
    assertEquals("Received status code does not match expected.", expectedStatus.getStatusCode(), response.getStatusLine().getStatusCode());
    //
    return response;
  }

  @Test
  public void contentTypeApplicationAtomXml() throws Exception {
    HttpGet getRequest = new HttpGet(URI.create(getEndpoint().toString() + "Employees('1')"));
    getRequest.setHeader(HttpHeaders.ACCEPT, "application/atom+xml");
    final HttpResponse response = getHttpClient().execute(getRequest);
    assertEquals(HttpStatusCodes.OK.getStatusCode(), response.getStatusLine().getStatusCode());
  }

  @Test
  public void contentTypeApplicationXml() throws Exception {
    testGetRequest("Employees('1')", "application/xml", HttpStatusCodes.OK, "application/atom+xml");
  }

  @Test
  public void contentTypeApplicationJson() throws Exception {
    testGetRequest("Rooms('1')", "application/json", HttpStatusCodes.OK, "application/json");
  }

  @Test
  public void contentTypeApplicationXmlOnSet() throws Exception {
    testGetRequest("Employees", "application/xml", HttpStatusCodes.OK, "application/atom+xml");
  }

  @Test
  public void contentTypeApplicationJsononSet() throws Exception {
    testGetRequest("Rooms", "application/json", HttpStatusCodes.OK, "application/json");
  }

  @Test
  public void illegalSpaceInContentType() throws Exception {
    testGetRequest("Rooms('1')", "application    /json", HttpStatusCodes.BAD_REQUEST, "application/json");
  }

  @Test
  public void illegalSpaceInContentSubType() throws Exception {
    testGetRequest("Employees('1')", "application/    xml", HttpStatusCodes.BAD_REQUEST, "application/xml");
  }

  @Test
  public void illegalSpacesInAcceptHeader() throws Exception {
    testGetRequest("Employees('1')", "    applic  ation/    xml   ", HttpStatusCodes.BAD_REQUEST, "application/xml");
  }

  @Test
  public void illegalSpacesInAcceptHeaderOnSet() throws Exception {
    testGetRequest("Employees", "    applic  ation/    xml   ", HttpStatusCodes.BAD_REQUEST, "application/xml");
  }

  @Test
  public void illegalSpacesInAcceptHeaderParameterOnSet() throws Exception {
    testGetRequest("Employees", "application/xml; param= alskdf", HttpStatusCodes.BAD_REQUEST, "application/xml");
  }

  @Test
  public void illegalLwsInAcceptHeaderParameter() throws Exception {
    testGetRequest("Employees('1')", "application/xml; param=\talskdf;", HttpStatusCodes.BAD_REQUEST, "application/xml");
  }

  @Test
  public void illegalSpacesBetweenAcceptHeaderParameterOnSet() throws Exception {
    testGetRequest("Employees", "application/xml; another=  test ; param=alskdf", HttpStatusCodes.BAD_REQUEST, "application/xml");
  }

  @Test
  public void legalSpacesBetweenAcceptHeaderParameterOnSet() throws Exception {
    testGetRequest("Employees", "application/xml; another=test   ;   param=alskdf", HttpStatusCodes.NOT_ACCEPTABLE, "application/xml");
  }
}
