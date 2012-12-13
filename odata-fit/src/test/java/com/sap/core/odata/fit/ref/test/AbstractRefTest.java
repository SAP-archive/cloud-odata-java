package com.sap.core.odata.fit.ref.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.enums.HttpStatusCodes;
import com.sap.core.odata.api.enums.MediaType;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.service.ODataSingleProcessorService;
import com.sap.core.odata.ref.edm.ScenarioEdmProvider;
import com.sap.core.odata.ref.model.DataContainer;
import com.sap.core.odata.ref.processor.ListsProcessor;
import com.sap.core.odata.ref.processor.ScenarioDataSource;
import com.sap.core.odata.testutils.fit.AbstractFitTest;
import com.sap.core.odata.testutils.helper.StringHelper;

/**
 * Abstract base class for tests employing the reference scenario
 * @author SAP AG
 */
public class AbstractRefTest extends AbstractFitTest {

  protected static final String IMAGE_JPEG = "image/jpeg";

  protected static final String EMPLOYEE_1_NAME = "Walter Winter";
  protected static final String EMPLOYEE_2_NAME = "Frederic Fall";
  protected static final String EMPLOYEE_3_NAME = "Jonathan Smith";
  protected static final String EMPLOYEE_4_NAME = "Peter Burke";
  protected static final String EMPLOYEE_5_NAME = "John Field";
  protected static final String EMPLOYEE_6_NAME = "Susan Bay";
  protected static final String MANAGER_NAME = EMPLOYEE_1_NAME;

  protected static final String EMPLOYEE_2_AGE = "32";
  protected static final String EMPLOYEE_3_AGE = "56";
  protected static final String EMPLOYEE_6_AGE = "29";

  protected static final String CITY_2_NAME = "Walldorf";

  @Override
  protected ODataSingleProcessorService createService() {
    DataContainer dataContainer = new DataContainer();
    dataContainer.reset();
    ODataSingleProcessor processor = new ListsProcessor(new ScenarioDataSource(dataContainer));
    EdmProvider provider = new ScenarioEdmProvider();

    return new ODataSingleProcessorService(provider, processor) {};
  }

  protected HttpResponse callUri(final String uri, final HttpStatusCodes expectedStatusCode) throws Exception {
    final HttpGet request = new HttpGet(this.getEndpoint() + uri);
    final HttpResponse response = this.getHttpClient().execute(request);

    assertNotNull(response);
    assertEquals(expectedStatusCode.getStatusCode(), response.getStatusLine().getStatusCode());

    if (expectedStatusCode == HttpStatusCodes.OK) {
      assertNotNull(response);
      assertNotNull(response.getEntity());
      assertNotNull(response.getEntity().getContent());
    } else if (expectedStatusCode == HttpStatusCodes.NO_CONTENT) {
      assertTrue(response.getEntity() == null || response.getEntity().getContent() == null);
    }

    return response;
  }

  protected HttpResponse callUri(final String urlString) throws Exception {
    return callUri(urlString, HttpStatusCodes.OK);
  }

  protected void checkUri(final String urlString) throws Exception {
    assertNotNull(getBody(callUri(urlString)));
  }

  protected void badRequest(final String urlString) throws Exception {
    final HttpResponse response = callUri(urlString, HttpStatusCodes.BAD_REQUEST);
    assertNotNull(getBody(response));
  }

  protected void notFound(final String urlString) throws Exception {
    final HttpResponse response = callUri(urlString, HttpStatusCodes.NOT_FOUND);
    assertNotNull(getBody(response));
  }

  protected String getBody(final HttpResponse response) throws Exception {
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertNotNull(response.getEntity().getContent());
    return StringHelper.inputStreamToString(response.getEntity().getContent());
  }

  protected void checkMediaType(final HttpResponse response, final MediaType expectedMediaType) {
    checkMediaType(response, expectedMediaType, true);
  }

  protected void checkMediaType(final HttpResponse response, final String expectedMediaType) {
    checkMediaType(response, expectedMediaType, true);
  }

  protected void checkMediaType(final HttpResponse response, final MediaType expectedMediaType, boolean withDefaultCharset) {
    checkMediaType(response, expectedMediaType.toString(), withDefaultCharset);
  }

  protected void checkMediaType(final HttpResponse response, final String expectedMediaType, boolean withDefaultCharset) {
    String expected = expectedMediaType.toString();
    if(withDefaultCharset) {
      expected += "; charset=utf-8";
    }
    assertEquals("MediaType was not excepected (charset expected=[" + withDefaultCharset +"]).", 
        expected, response.getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue());
  }

  protected void checkEtag(final HttpResponse response, final String expectedEtag) {
    assertNotNull(response.getFirstHeader(HttpHeaders.ETAG));
    final String entityTag = response.getFirstHeader(HttpHeaders.ETAG).getValue();
    assertNotNull(entityTag);
    assertEquals(expectedEtag, entityTag);
  }
}
