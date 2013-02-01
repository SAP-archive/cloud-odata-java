package com.sap.core.odata.fit.ref;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.commons.ODataHttpMethod;
import com.sap.core.odata.core.processor.ODataSingleProcessorService;
import com.sap.core.odata.ref.edm.ScenarioEdmProvider;
import com.sap.core.odata.ref.model.DataContainer;
import com.sap.core.odata.ref.processor.ListsProcessor;
import com.sap.core.odata.ref.processor.ScenarioDataSource;
import com.sap.core.odata.testutil.fit.AbstractFitTest;
import com.sap.core.odata.testutil.helper.StringHelper;

/**
 * Abstract base class for tests employing the reference scenario
 * @author SAP AG
 */
public class AbstractRefTest extends AbstractFitTest {

  protected static final String IMAGE_GIF = "image/gif";
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

  protected static final String CITY_1_NAME = "Heidelberg";
  protected static final String CITY_2_NAME = "Walldorf";

  protected static final String BUILDING_3_NAME = "Building 3";

  @Override
  protected ODataSingleProcessorService createService() {
    DataContainer dataContainer = new DataContainer();
    dataContainer.reset();
    ODataSingleProcessor processor = new ListsProcessor(new ScenarioDataSource(dataContainer));
    EdmProvider provider = new ScenarioEdmProvider();

    return new ODataSingleProcessorService(provider, processor) {};
  }

  protected HttpResponse callUri(
      final ODataHttpMethod httpMethod, final String uri,
      final String additionalHeader, final String additionalHeaderValue,
      final String requestBody, final String requestContentType,
      final HttpStatusCodes expectedStatusCode) throws Exception {

    HttpRequestBase request =
        httpMethod == ODataHttpMethod.GET ? new HttpGet() :
            httpMethod == ODataHttpMethod.DELETE ? new HttpDelete() :
                httpMethod == ODataHttpMethod.POST ? new HttpPost() :
                    httpMethod == ODataHttpMethod.PUT ? new HttpPut() : new HttpPatch();
    request.setURI(URI.create(getEndpoint() + uri));
    if (additionalHeader != null)
      request.addHeader(additionalHeader, additionalHeaderValue);
    if (requestBody != null) {
      ((HttpEntityEnclosingRequest) request).setEntity(new StringEntity(requestBody));
      request.setHeader(HttpHeaders.CONTENT_TYPE, requestContentType);
    }

    final HttpResponse response = getHttpClient().execute(request);

    assertNotNull(response);
    assertEquals(expectedStatusCode.getStatusCode(), response.getStatusLine().getStatusCode());

    if (expectedStatusCode == HttpStatusCodes.OK) {
      assertNotNull(response);
      assertNotNull(response.getEntity());
      assertNotNull(response.getEntity().getContent());
    } else if (expectedStatusCode == HttpStatusCodes.CREATED) {
      assertNotNull(response.getFirstHeader(HttpHeaders.LOCATION));
    } else if (expectedStatusCode == HttpStatusCodes.NO_CONTENT) {
      assertTrue(response.getEntity() == null || response.getEntity().getContent() == null);
    }

    return response;
  }

  protected HttpResponse callUri(final String uri, final String additionalHeader, final String additionalHeaderValue, final HttpStatusCodes expectedStatusCode) throws Exception {
    return callUri(ODataHttpMethod.GET, uri, additionalHeader, additionalHeaderValue, null, null, expectedStatusCode);
  }

  protected HttpResponse callUri(final String uri, final String additionalHeader, final String additionalHeaderValue) throws Exception {
    return callUri(ODataHttpMethod.GET, uri, additionalHeader, additionalHeaderValue, null, null, HttpStatusCodes.OK);
  }

  protected HttpResponse callUri(final String uri, final HttpStatusCodes expectedStatusCode) throws Exception {
    return callUri(uri, null, null, expectedStatusCode);
  }

  protected HttpResponse callUri(final String uri) throws Exception {
    return callUri(uri, HttpStatusCodes.OK);
  }

  protected void checkUri(final String uri) throws Exception {
    assertNotNull(getBody(callUri(uri)));
  }

  protected void badRequest(final String uri) throws Exception {
    final HttpResponse response = callUri(uri, HttpStatusCodes.BAD_REQUEST);
    assertNotNull(getBody(response));
  }

  protected void notFound(final String uri) throws Exception {
    final HttpResponse response = callUri(uri, HttpStatusCodes.NOT_FOUND);
    assertNotNull(getBody(response));
  }

  protected String getBody(final HttpResponse response) throws Exception {
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertNotNull(response.getEntity().getContent());
    return StringHelper.inputStreamToString(response.getEntity().getContent());
  }

  protected void checkMediaType(final HttpResponse response, final String expectedMediaType) {
    checkMediaType(response, expectedMediaType, true);
  }

  protected void checkMediaType(final HttpResponse response, final String expectedMediaType, final boolean withDefaultCharset) {
    ContentType expected = ContentType.create(expectedMediaType);
    if (withDefaultCharset && !expectedMediaType.contains("charset=utf-8")) {
      expected = ContentType.create(expected, ContentType.PARAMETER_CHARSET, ContentType.CHARSET_UTF_8);
    }
    assertEquals("MediaType was not expected (charset expected=[" + withDefaultCharset + "]).",
        expected.toContentTypeString(), response.getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue());
  }

  protected void checkEtag(final HttpResponse response, final String expectedEtag) {
    assertNotNull(response.getFirstHeader(HttpHeaders.ETAG));
    final String entityTag = response.getFirstHeader(HttpHeaders.ETAG).getValue();
    assertNotNull(entityTag);
    assertEquals(expectedEtag, entityTag);
  }

  protected void deleteUri(final String uri, final HttpStatusCodes expectedStatusCode) throws Exception, AssertionError {
    final HttpResponse response = callUri(ODataHttpMethod.DELETE, uri, null, null, null, null, expectedStatusCode);
    if (expectedStatusCode != HttpStatusCodes.NO_CONTENT)
      response.getEntity().getContent().close();
  }

  protected void deleteUriOk(final String uri) throws Exception {
    deleteUri(uri, HttpStatusCodes.NO_CONTENT);
  }

  protected HttpResponse postUri(final String uri, final String requestBody, final String requestContentType, final HttpStatusCodes expectedStatusCode) throws Exception {
    return callUri(ODataHttpMethod.POST, uri, null, null, requestBody, requestContentType, expectedStatusCode);
  }

  protected void putUri(final String uri,
      final String requestBody, final String requestContentType,
      final HttpStatusCodes expectedStatusCode) throws Exception {
    final HttpResponse response = callUri(ODataHttpMethod.PUT, uri, null, null, requestBody, requestContentType, expectedStatusCode);
    if (expectedStatusCode != HttpStatusCodes.NO_CONTENT)
      response.getEntity().getContent().close();
  }
}
