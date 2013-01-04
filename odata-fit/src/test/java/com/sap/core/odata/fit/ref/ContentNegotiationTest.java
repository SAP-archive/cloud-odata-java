package com.sap.core.odata.fit.ref;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.service.ODataSingleProcessorService;
import com.sap.core.odata.core.enums.ContentType;
import com.sap.core.odata.ref.edm.ScenarioEdmProvider;
import com.sap.core.odata.ref.model.DataContainer;
import com.sap.core.odata.ref.processor.ListsProcessor;
import com.sap.core.odata.ref.processor.ScenarioDataSource;
import com.sap.core.odata.testutils.fit.AbstractFitTest;
import com.sap.core.odata.testutils.helper.StringHelper;

public class ContentNegotiationTest extends AbstractFitTest {

  @Override
  protected ODataSingleProcessorService createService() throws ODataException {
    DataContainer dataContainer = new DataContainer();
    dataContainer.reset();
    ODataSingleProcessor processor = new ListsProcessor(new ScenarioDataSource(dataContainer));
    EdmProvider provider = new ScenarioEdmProvider();

    return new ODataSingleProcessorService(provider, processor);
  }

  @Test
  public void testContentTypeMetadata() throws Exception {
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "$metadata"));

    HttpResponse response = this.getHttpClient().execute(get);

    assertEquals(200, response.getStatusLine().getStatusCode());
    Header header = response.getFirstHeader(HttpHeaders.CONTENT_TYPE);
    assertEquals(ContentType.APPLICATION_XML.toString(), header.getValue());
  }

  @Test
  public void testContentTypeMetadataNotAccepted() throws Exception {
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "$metadata"));
    get.addHeader(HttpHeaders.ACCEPT, "image/gif");
    
    HttpResponse response = this.getHttpClient().execute(get);
    assertEquals(406, response.getStatusLine().getStatusCode());
  }

  @Test
  public void testBrowserAcceptHeader() throws Exception {
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "$metadata"));
    get.addHeader(HttpHeaders.ACCEPT, "text/html, application/xhtml+xml, application/xml;q=0.9, */*;q=0.8");
    
    HttpResponse response = this.getHttpClient().execute(get);
    assertEquals(200, response.getStatusLine().getStatusCode());
    assertEquals("application/xml", response.getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue());
  }
  
  @Test
  public void testContentTypeServiceDocumentWoAcceptHeader() throws Exception {
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() ));

    HttpResponse response = this.getHttpClient().execute(get);

    assertEquals(200, response.getStatusLine().getStatusCode());
    Header header = response.getFirstHeader(HttpHeaders.CONTENT_TYPE);
    assertEquals(ContentType.APPLICATION_ATOM_SVC.toString() + "; charset=utf-8", header.getValue());
    
    String responseBody = StringHelper.httpEntityToString(response.getEntity());
    assertTrue(responseBody.length() > 100);
    log.debug(responseBody);
  }
  
  @Test
  public void testContentTypeServiceDocumentAcceptHeaders() throws Exception {
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() ));
    get.setHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
    
    HttpResponse response = this.getHttpClient().execute(get);

    assertEquals(200, response.getStatusLine().getStatusCode());
    Header header = response.getFirstHeader(HttpHeaders.CONTENT_TYPE);
    assertEquals(ContentType.APPLICATION_XML.toString() + "; charset=utf-8", header.getValue());
    
    String responseBody = StringHelper.httpEntityToString(response.getEntity());
    assertTrue(responseBody.length() > 100);
    log.debug(responseBody);
  }

}
