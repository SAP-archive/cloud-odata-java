package com.sap.core.odata.fit.basic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpHeaders;
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

  // TODO: Don't use reference scenario in basic tests.
  @Override
  protected ODataService createService() throws ODataException {
    DataContainer dataContainer = new DataContainer();
    dataContainer.reset();
    final ODataSingleProcessor processor = new ListsProcessor(new ScenarioDataSource(dataContainer));
    final EdmProvider provider = new ScenarioEdmProvider();
    return new ODataSingleProcessorService(provider, processor) {};
  }

  @Test
  public void acceptHeaderAppAtomXml() throws Exception {
    HttpGet get = new HttpGet(URI.create(getEndpoint() + "Rooms('1')"));
    get.setHeader(HttpHeaders.ACCEPT, HttpContentType.APPLICATION_ATOM_XML);
    final HttpResponse response = getHttpClient().execute(get);

    final String contentType = response.getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue();
    assertEquals(ContentType.create(HttpContentType.APPLICATION_ATOM_XML_ENTRY_UTF8), ContentType.create(contentType));

    assertNotNull(StringHelper.inputStreamToString(response.getEntity().getContent()));
  }

  @Test
  public void acceptHeaderAppXml() throws Exception {
    HttpGet get = new HttpGet(URI.create(getEndpoint() + "Rooms('1')"));
    get.setHeader(HttpHeaders.ACCEPT, HttpContentType.APPLICATION_XML);
    final HttpResponse response = getHttpClient().execute(get);

    final String contentType = response.getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue();
    assertEquals(HttpContentType.APPLICATION_XML_UTF8, contentType);
    assertEquals(ContentType.create(HttpContentType.APPLICATION_XML_UTF8), ContentType.create(contentType));

    assertNotNull(StringHelper.inputStreamToString(response.getEntity().getContent()));
  }

  @Test
  public void acceptHeaderAppXmlCharsetUtf8() throws Exception {
    HttpGet get = new HttpGet(URI.create(getEndpoint() + "Rooms('1')"));
    get.setHeader(HttpHeaders.ACCEPT, HttpContentType.APPLICATION_XML_UTF8);
    final HttpResponse response = getHttpClient().execute(get);

    final String contentType = response.getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue();
    assertEquals(HttpContentType.APPLICATION_XML_UTF8, contentType);
    assertEquals(ContentType.create(HttpContentType.APPLICATION_XML_UTF8), ContentType.create(contentType));

    assertNotNull(StringHelper.inputStreamToString(response.getEntity().getContent()));
  }
}
