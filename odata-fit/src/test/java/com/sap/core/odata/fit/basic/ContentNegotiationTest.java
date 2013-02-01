package com.sap.core.odata.fit.basic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.processor.ODataSingleProcessorService;
import com.sap.core.odata.ref.edm.ScenarioEdmProvider;
import com.sap.core.odata.ref.model.DataContainer;
import com.sap.core.odata.ref.processor.ListsProcessor;
import com.sap.core.odata.ref.processor.ScenarioDataSource;
import com.sap.core.odata.testutil.fit.AbstractFitTest;
import com.sap.core.odata.testutil.helper.StringHelper;

/**
 * @author SAP AG
 */
public class ContentNegotiationTest extends AbstractFitTest {

  @Override
  protected ODataService createService() throws ODataException {
    DataContainer dataContainer = new DataContainer();
    dataContainer.reset();
    ODataSingleProcessor processor = new ListsProcessor(new ScenarioDataSource(dataContainer));
    EdmProvider provider = new ScenarioEdmProvider();
    return new ODataSingleProcessorService(provider, processor) {};
  }

  @Test
  public void acceptHeaderAppAtomXml() throws Exception {
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "Rooms('1')"));
    get.setHeader("Accept", "application/atom+xml");
    HttpResponse response = this.getHttpClient().execute(get);

    String xml = StringHelper.inputStreamToString(response.getEntity().getContent());
  
    String contentType = response.getFirstHeader("Content-Type").getValue();
    final String expectedContentType = "application/atom+xml; type=entry; charset=utf-8";
    ContentType ct = ContentType.create(contentType);
    assertEquals(ContentType.create(expectedContentType), ct);
    
    assertNotNull(xml);
  }

  @Test
  public void acceptHeaderAppXml() throws Exception {
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "Rooms('1')"));
    get.setHeader("Accept", "application/xml");
    HttpResponse response = this.getHttpClient().execute(get);

    String xml = StringHelper.inputStreamToString(response.getEntity().getContent());
  
    String contentType = response.getFirstHeader("Content-Type").getValue();
    final String expectedContentType = "application/xml; charset=utf-8";
    assertEquals(expectedContentType, contentType);
    ContentType ct = ContentType.create(contentType);
    assertEquals(ContentType.create(expectedContentType), ct);
    
    assertNotNull(xml);
  }

  @Test
  public void acceptHeaderAppXmlCharsetUtf8() throws Exception {
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "Rooms('1')"));
    get.setHeader("Accept", "application/xml; charset=utf-8");
    HttpResponse response = this.getHttpClient().execute(get);

    String xml = StringHelper.inputStreamToString(response.getEntity().getContent());
  
    String contentType = response.getFirstHeader("Content-Type").getValue();
    final String expectedContentType = "application/xml; charset=utf-8";
    assertEquals(expectedContentType, contentType);
    ContentType ct = ContentType.create(contentType);
    assertEquals(ContentType.create(expectedContentType), ct);
    
    assertNotNull(xml);
  }
}
