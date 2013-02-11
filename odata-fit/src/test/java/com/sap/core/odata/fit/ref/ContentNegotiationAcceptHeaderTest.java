package com.sap.core.odata.fit.ref;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.processor.ODataSingleProcessorService;
import com.sap.core.odata.core.uri.UriType;
import com.sap.core.odata.ref.edm.ScenarioEdmProvider;
import com.sap.core.odata.ref.model.DataContainer;
import com.sap.core.odata.ref.processor.ListsProcessor;
import com.sap.core.odata.ref.processor.ScenarioDataSource;
import com.sap.core.odata.testutil.fit.AbstractFitTest;
import com.sap.core.odata.testutil.helper.StringHelper;

/**
 * @author SAP AG
 */
public class ContentNegotiationAcceptHeaderTest extends AbstractFitTest {
  
  final static List<String> ACCEPT_HEADER_VALUES = Arrays.asList(
      "", // for request with none 'Accept-Header' set
      "text/plain",
      "text/plain; charset=utf-8",
      "application/json",
      "application/json; charset=utf-8",
      "application/xml",
      "application/xml; charset=utf-8",
      "application/atom+xml",
      "application/atom+xml; charset=utf-8",
      "application/atomsvc+xml",
      "application/atomsvc+xml; charset=utf-8"
      );
  private final static List<String> QUERY_OPTIONS = Arrays.asList(
      "",
      "?$format=xml",
      "?$format=atom",
      "?$format=json"
      );

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

  @Test
  public void testURI_0_ServiceDocument() throws Exception {
    // create test set
    FitTestSet testSet = new FitTestSet(UriType.URI0, "/");

    // set specific response 'Content-Type's for '$format'
    testSet.setTestParam(Arrays.asList("?$format=xml"), ACCEPT_HEADER_VALUES, 200, "application/xml; charset=utf-8");
    testSet.setTestParam(Arrays.asList("?$format=atom"), ACCEPT_HEADER_VALUES, 200, "application/atomsvc+xml; charset=utf-8");
    testSet.setTestParam(Arrays.asList(""), Arrays.asList(""), 200, "application/atomsvc+xml; charset=utf-8");

    // set all 'NOT ACCEPTED' requests
    final List<String> notAcceptedHeaderValues = Arrays.asList(
        "text/plain",
        "application/atom+xml",
        "text/plain; charset=utf-8",
        "application/atom+xml; charset=utf-8"
        );
    testSet.setTestParam(Arrays.asList(""), notAcceptedHeaderValues, 406, "application/xml");

    //
    final List<String> notAcceptedJsonHeaderValues = Arrays.asList(
        "application/json",
        "application/json; charset=utf-8"
        );
    // TODO: check which behavior is currently wanted
    testSet.setTestParam(Arrays.asList("?$format=json"), ACCEPT_HEADER_VALUES, 406, "application/xml");
    testSet.setTestParam(Arrays.asList("", "?$format=json"), notAcceptedJsonHeaderValues, 406, "application/json");
    
    // execute all defined tests
    testSet.execute(getEndpoint());
  }
  
  @Test
  public void testURI_1_EntitySet() throws Exception {
    // create test set
    FitTestSet testSet = new FitTestSet(UriType.URI1, "/Employees");

    // set specific response 'Content-Type's for '$format'
    testSet.setTestParam(Arrays.asList("?$format=xml"), ACCEPT_HEADER_VALUES, 200, "application/xml; charset=utf-8");
    testSet.setTestParam(Arrays.asList("?$format=atom"), ACCEPT_HEADER_VALUES, 200, "application/atom+xml; type=feed; charset=utf-8");
    testSet.setTestParam(Arrays.asList(""), Arrays.asList("", "application/atom+xml", "application/atom+xml; charset=utf-8"), 
        200, "application/atom+xml; type=feed; charset=utf-8");

    // set all 'NOT ACCEPTED' requests
    final List<String> notAcceptedHeaderValues = Arrays.asList(
        "text/plain",
        "text/plain; charset=utf-8",
        "application/atomsvc+xml",
        "application/atomsvc+xml; charset=utf-8"
        );
    testSet.setTestParam(Arrays.asList(""), notAcceptedHeaderValues, 406, "application/xml");

    //
    final List<String> notAcceptedJsonHeaderValues = Arrays.asList(
        "application/json",
        "application/json; charset=utf-8"
        );
    // TODO: check which behavior is currently wanted
    testSet.setTestParam(Arrays.asList("?$format=json"), ACCEPT_HEADER_VALUES, 406, "application/xml");
    testSet.setTestParam(Arrays.asList("", "?$format=json"), notAcceptedJsonHeaderValues, 406, "application/json");
    
    // execute all defined tests
    testSet.execute(getEndpoint());
  }
  
  @Test
  public void testURI_2_Entity() throws Exception {
    // create test set
    FitTestSet testSet = new FitTestSet(UriType.URI2, "/Employees('1')");

    // set specific response 'Content-Type's for '$format'
    testSet.setTestParam(Arrays.asList("?$format=xml"), ACCEPT_HEADER_VALUES, 200, "application/xml; charset=utf-8");
    testSet.setTestParam(Arrays.asList("?$format=atom"), ACCEPT_HEADER_VALUES, 200, "application/atom+xml; type=entry; charset=utf-8");
    testSet.setTestParam(Arrays.asList(""), Arrays.asList("", "application/atom+xml", "application/atom+xml; charset=utf-8"), 
        200, "application/atom+xml; type=entry; charset=utf-8");

    // set all 'NOT ACCEPTED' requests
    final List<String> notAcceptedHeaderValues = Arrays.asList(
        "text/plain",
        "text/plain; charset=utf-8",
        "application/atomsvc+xml",
        "application/atomsvc+xml; charset=utf-8"
        );
    testSet.setTestParam(Arrays.asList(""), notAcceptedHeaderValues, 406, "application/xml");

    //
    final List<String> notAcceptedJsonHeaderValues = Arrays.asList(
        "application/json",
        "application/json; charset=utf-8"
        );
    // TODO: check which behavior is currently wanted
    testSet.setTestParam(Arrays.asList("?$format=json"), ACCEPT_HEADER_VALUES, 406, "application/xml");
    testSet.setTestParam(Arrays.asList("", "?$format=json"), notAcceptedJsonHeaderValues, 406, "application/json");
    
    // execute all defined tests
    testSet.execute(getEndpoint());
  }

  @Test
  public void testURI_3_EntityComplexProperty() throws Exception {
    // create test set
    FitTestSet testSet = new FitTestSet(UriType.URI3, "/Employees('1')/Location");

    // set specific response 'Content-Type's for '$format'
    testSet.setTestParam(Arrays.asList("?$format=xml"), ACCEPT_HEADER_VALUES, 200, "application/xml; charset=utf-8");
    testSet.setTestParam(Arrays.asList(""), Arrays.asList(""), 200, "application/xml; charset=utf-8");

    // set all 'NOT ACCEPTED' requests
    final List<String> notAcceptedHeaderValues = Arrays.asList(
        "text/plain",
        "text/plain; charset=utf-8",
        "application/atom+xml",
        "application/atom+xml; charset=utf-8",
        "application/atomsvc+xml",
        "application/atomsvc+xml; charset=utf-8"
        );
    testSet.setTestParam(Arrays.asList(""), notAcceptedHeaderValues, 406, "application/xml");
    // '$format=atom' it not allowed
    testSet.setTestParam(Arrays.asList("?$format=atom"), ACCEPT_HEADER_VALUES, 406, "application/xml");

    // JSON is not supported
    final List<String> notAcceptedJsonHeaderValues = Arrays.asList(
        "application/json",
        "application/json; charset=utf-8"
        );
    // TODO: check which behavior is currently wanted
    testSet.setTestParam(Arrays.asList("?$format=json"), ACCEPT_HEADER_VALUES, 406, "application/xml");
    testSet.setTestParam(Arrays.asList("", "?$format=json", "?$format=atom"), notAcceptedJsonHeaderValues, 406, "application/json");
    
    // execute all defined tests
    testSet.execute(getEndpoint());
  }


  @Test
  public void testURI_4_EntityComplexPropertySimpleProperty() throws Exception {
    // create test set
    FitTestSet testSet = new FitTestSet(UriType.URI4, "/Employees('1')/Location/Country");

    // set specific response 'Content-Type's for '$format'
    testSet.setTestParam(Arrays.asList("?$format=xml"), ACCEPT_HEADER_VALUES, 200, "application/xml; charset=utf-8");
    testSet.setTestParam(Arrays.asList(""), Arrays.asList(""), 200, "application/xml; charset=utf-8");

    // set all 'NOT ACCEPTED' requests
    final List<String> notAcceptedHeaderValues = Arrays.asList(
        "text/plain",
        "text/plain; charset=utf-8",
        "application/atom+xml",
        "application/atom+xml; charset=utf-8",
        "application/atomsvc+xml",
        "application/atomsvc+xml; charset=utf-8"
        );
    testSet.setTestParam(Arrays.asList(""), notAcceptedHeaderValues, 406, "application/xml");
    // '$format=atom' it not allowed
    testSet.setTestParam(Arrays.asList("?$format=atom"), ACCEPT_HEADER_VALUES, 406, "application/xml");

    // JSON is not supported
    final List<String> notAcceptedJsonHeaderValues = Arrays.asList(
        "application/json",
        "application/json; charset=utf-8"
        );
    // TODO: check which behavior is currently wanted
    testSet.setTestParam(Arrays.asList("?$format=json"), ACCEPT_HEADER_VALUES, 406, "application/xml");
    testSet.setTestParam(Arrays.asList("", "?$format=json", "?$format=atom"), notAcceptedJsonHeaderValues, 406, "application/json");
    
    // execute all defined tests
    testSet.execute(getEndpoint());
  }

  @Test
  public void testURI_5_EntitySimpleProperty() throws Exception {
    // create test set
    FitTestSet testSet = new FitTestSet(UriType.URI4, "/Employees('1')/Age");

    // set specific response 'Content-Type's for '$format'
    testSet.setTestParam(Arrays.asList("?$format=xml"), ACCEPT_HEADER_VALUES, 200, "application/xml; charset=utf-8");
    testSet.setTestParam(Arrays.asList(""), Arrays.asList(""), 200, "application/xml; charset=utf-8");

    // set all 'NOT ACCEPTED' requests
    final List<String> notAcceptedHeaderValues = Arrays.asList(
        "text/plain",
        "text/plain; charset=utf-8",
        "application/atom+xml",
        "application/atom+xml; charset=utf-8",
        "application/atomsvc+xml",
        "application/atomsvc+xml; charset=utf-8"
        );
    testSet.setTestParam(Arrays.asList(""), notAcceptedHeaderValues, 406, "application/xml");
    // '$format=atom' it not allowed
    testSet.setTestParam(Arrays.asList("?$format=atom"), ACCEPT_HEADER_VALUES, 406, "application/xml");

    // JSON is not supported
    final List<String> notAcceptedJsonHeaderValues = Arrays.asList(
        "application/json",
        "application/json; charset=utf-8"
        );
    // TODO: check which behavior is currently wanted
    testSet.setTestParam(Arrays.asList("?$format=json"), ACCEPT_HEADER_VALUES, 406, "application/xml");
    testSet.setTestParam(Arrays.asList("", "?$format=json", "?$format=atom"), notAcceptedJsonHeaderValues, 406, "application/json");
    
    // execute all defined tests
    testSet.execute(getEndpoint());
  }

  @Test
  public void testURI_6A_EntityNavigationProperty() throws Exception {
    // create test set
    FitTestSet testSet = new FitTestSet(UriType.URI6A, "/Employees('1')/ne_Room");

    // set specific response 'Content-Type's for '$format'
    testSet.setTestParam(Arrays.asList("?$format=xml"), ACCEPT_HEADER_VALUES, 200, "application/xml; charset=utf-8");
    testSet.setTestParam(Arrays.asList("?$format=atom"), ACCEPT_HEADER_VALUES, 200, "application/atom+xml; type=entry; charset=utf-8");
    testSet.setTestParam(Arrays.asList(""), Arrays.asList("", "application/atom+xml", "application/atom+xml; charset=utf-8"), 
        200, "application/atom+xml; type=entry; charset=utf-8");

    // set all 'NOT ACCEPTED' requests
    final List<String> notAcceptedHeaderValues = Arrays.asList(
        "text/plain",
        "text/plain; charset=utf-8",
        "application/atomsvc+xml",
        "application/atomsvc+xml; charset=utf-8"
        );
    testSet.setTestParam(Arrays.asList(""), notAcceptedHeaderValues, 406, "application/xml");

    //
    final List<String> notAcceptedJsonHeaderValues = Arrays.asList(
        "application/json",
        "application/json; charset=utf-8"
        );
    // TODO: check which behavior is currently wanted
    testSet.setTestParam(Arrays.asList("?$format=json"), ACCEPT_HEADER_VALUES, 406, "application/xml");
    testSet.setTestParam(Arrays.asList("", "?$format=json"), notAcceptedJsonHeaderValues, 406, "application/json");
    
    // execute all defined tests
    testSet.execute(getEndpoint());
  }

  @Test
  public void testURI_7A_EntityNavigationProperty() throws Exception {
    // create test set
    FitTestSet testSet = new FitTestSet(UriType.URI7A, "/Employees('1')/$links/ne_Room");

    // set specific response 'Content-Type's for '$format'
    testSet.setTestParam(Arrays.asList("?$format=xml"), ACCEPT_HEADER_VALUES, 200, "application/xml; charset=utf-8");
    testSet.setTestParam(Arrays.asList(""), Arrays.asList(""), 200, "application/xml; charset=utf-8");

    // set all 'NOT ACCEPTED' requests
    final List<String> notAcceptedHeaderValues = Arrays.asList(
        "text/plain",
        "text/plain; charset=utf-8",
        "application/atom+xml",
        "application/atom+xml; charset=utf-8",
        "application/atomsvc+xml",
        "application/atomsvc+xml; charset=utf-8"
        );
    testSet.setTestParam(Arrays.asList(""), notAcceptedHeaderValues, 406, "application/xml");

    //
    final List<String> notAcceptedJsonHeaderValues = Arrays.asList(
        "application/json",
        "application/json; charset=utf-8"
        );
    // TODO: check which behavior is currently wanted
    testSet.setTestParam(Arrays.asList("?$format=json", "?$format=atom"), ACCEPT_HEADER_VALUES, 406, "application/xml");
    testSet.setTestParam(Arrays.asList("", "?$format=json", "?$format=atom"), notAcceptedJsonHeaderValues, 406, "application/json");
    
    // execute all defined tests
    testSet.execute(getEndpoint());
  }

  @Test
  public void testURI_8_Metadata() throws Exception {
    // create test set
    FitTestSet testSet = new FitTestSet(UriType.URI8, "/$metadata");

    // set specific response 'Content-Type's for '$format'
    testSet.setTestParam(Arrays.asList(""), Arrays.asList("", "application/xml", "application/xml; charset=utf-8"), 
        200, "application/xml; charset=utf-8");

    // set all 'NOT ACCEPTED' requests
    final List<String> notAcceptedHeaderValues = Arrays.asList(
        "text/plain",
        "text/plain; charset=utf-8",
        "application/atom+xml",
        "application/atom+xml; charset=utf-8",
        "application/atomsvc+xml",
        "application/atomsvc+xml; charset=utf-8"
        );
    testSet.setTestParam(Arrays.asList(""), notAcceptedHeaderValues, 406, "application/xml");

    // every combination of $format and $metadata is a 'BAD REQUEST'
    testSet.setTestParam(Arrays.asList("?$format=json", "?$format=xml", "?$format=atom"), ACCEPT_HEADER_VALUES, 400, "application/xml");
    //
    final List<String> jsonAcceptHeaders = Arrays.asList(
        "application/json",
        "application/json; charset=utf-8"
        );
    // TODO: check which behavior is currently wanted
    testSet.setTestParam(Arrays.asList("?$format=json", "?$format=xml", "?$format=atom"), jsonAcceptHeaders, 400, "application/json");
    testSet.setTestParam(Arrays.asList(""), jsonAcceptHeaders, 406, "application/json");
    
    // execute all defined tests
    testSet.execute(getEndpoint());
  }

  @Test
  @Ignore("Currently ignored because of a BUG")
  public void testURI_17_EntityMediaResourceDollarValue() throws Exception {
    // create test set
    FitTestSet testSet = new FitTestSet(UriType.URI17, "/Employees('1')/$value", 200, "image/jpeg");
    
    // every combination of $format and $value is a 'BAD REQUEST'
    testSet.setTestParam(Arrays.asList("?$format=json", "?$format=xml", "?$format=atom"), ACCEPT_HEADER_VALUES, 400, "application/xml");
    //
    final List<String> jsonAcceptHeaders = Arrays.asList(
        "application/json",
        "application/json; charset=utf-8"
        );
    // TODO: check which behavior is currently wanted
    testSet.setTestParam(Arrays.asList("?$format=json", "?$format=xml", "?$format=atom"), jsonAcceptHeaders, 400, "application/json");

    testSet.execute(getEndpoint());
  }

  @Test
  public void testURI_17_EntitySimpleTypeDollarValue() throws Exception {
    // create test set
    FitTestSet testSet = new FitTestSet(UriType.URI17, "/Employees('1')/Age/$value", 200, "text/plain; charset=utf-8");
    
    // every combination of $format and $value is a 'BAD REQUEST'
    testSet.setTestParam(Arrays.asList("?$format=json", "?$format=xml", "?$format=atom"), ACCEPT_HEADER_VALUES, 400, "application/xml");
    //
    final List<String> jsonAcceptHeaders = Arrays.asList(
        "application/json",
        "application/json; charset=utf-8"
        );
    // TODO: check which behavior is currently wanted
    testSet.setTestParam(Arrays.asList("?$format=json", "?$format=xml", "?$format=atom"), jsonAcceptHeaders, 400, "application/json");

    testSet.execute(getEndpoint());
  }
  
  
  
  /**
   * A set of {@link FitTest} which can be executed against a service endpoint.
   * 
   */
  private static class FitTestSet {
    private final Set<FitTest> testParameters = new HashSet<ContentNegotiationAcceptHeaderTest.FitTest>();
    
    private final UriType uriType;
    private final String path;
    
    public FitTestSet(UriType uriType, String path) {
      super();
      this.uriType = uriType;
      this.path = path;
      
      populate(HttpStatusCodes.OK.getStatusCode());
    }

    public FitTestSet(UriType uriType, String path, int defaultStatusCode, String defaultContentType) {
      super();
      this.uriType = uriType;
      this.path = path;
      
      populate(defaultStatusCode, defaultContentType);
    }

    private void populate(int defaultStatusCode) {
      testParameters.addAll(FitTest.create(uriType, path, QUERY_OPTIONS, ACCEPT_HEADER_VALUES, defaultStatusCode));
    }

    private void populate(int defaultStatusCode, String defaultContentType) {
      testParameters.addAll(FitTest.create(uriType, path, QUERY_OPTIONS, ACCEPT_HEADER_VALUES, defaultStatusCode, defaultContentType));
    }

    public void setTestParam(List<String> queryOptions, List<String> acceptHeader, int expectedStatusCode, String expectedContentType) {
      List<FitTest> tp = FitTest.create(uriType, path, queryOptions, acceptHeader, expectedStatusCode, expectedContentType);
      replaceTestParameters(tp);
    }

    private void replaceTestParameters(List<FitTest> tp) {
      testParameters.removeAll(tp);
      testParameters.addAll(tp);
    }

    
    public void execute(URI serviceEndpoint) throws Exception {
      Map<FitTest, AssertionError> test2Failure = new HashMap<ContentNegotiationAcceptHeaderTest.FitTest, AssertionError>();
      List<FitTest> successTests = new ArrayList<ContentNegotiationAcceptHeaderTest.FitTest>();
      
      for (FitTest testParam : testParameters) {
        try {
          testParam.execute(serviceEndpoint);
          successTests.add(testParam);
        } catch(AssertionError e) {
          test2Failure.put(testParam, e);
        }
      }
      
//      System.out.println("#########################################");
//      System.out.println("# Success: '" + successTests.size() + "', failed '" + test2Failure.size() +
//          "', total '" + testParameters.size() + "'.");
//      System.out.println("#########################################");

      if(!test2Failure.isEmpty()) {
        Set<Entry<FitTest, AssertionError>> failedTests = test2Failure.entrySet();
        List<AssertionError> errors = new ArrayList<AssertionError>();
        for (Entry<FitTest, AssertionError> entry : failedTests) {
          errors.add(entry.getValue());
        }
        Assert.fail("Found '" + test2Failure.size() + "' test failures. See [\n" + errors + "]");
      }
    }
  }
  
  
  /**
   * Combination of test parameters and expected test result which can be tested/executed against a service endpoint.
   */
  private static class FitTest {
    private UriType uriType;
    private String path;
    private String queryOptions;
    private String acceptHeader;
    
    private String requestLine;
    
    private int expectedStatusCode;
    private String expectedContentType;
    private boolean isContentExpected;
    
    public FitTest(UriType uriType, String path, String queryOptions, String acceptHeader, int expectedStatusCode, String expectedContentType, boolean isContentExpected) {
      super();
      this.uriType = uriType;
      this.path = path;
      this.queryOptions = queryOptions;
      this.acceptHeader = acceptHeader;
      this.expectedStatusCode = expectedStatusCode;
      this.expectedContentType = expectedContentType;
      this.isContentExpected = isContentExpected;
    }
    

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
      return "TestParam [testUrl=" + requestLine + ", \n\turiType=" + uriType + ", path=" + path + ", queryOption=" + queryOptions + ", acceptHeader=" + acceptHeader + ", expectedStatusCode=" + expectedStatusCode + ", expectedContentType=" + expectedContentType + ", isContentExpected="
          + isContentExpected + "]";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((acceptHeader == null) ? 0 : acceptHeader.hashCode());
      result = prime * result + ((path == null) ? 0 : path.hashCode());
      result = prime * result + ((queryOptions == null) ? 0 : queryOptions.hashCode());
      return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      FitTest other = (FitTest) obj;
      if (acceptHeader == null) {
        if (other.acceptHeader != null)
          return false;
      } else if (!acceptHeader.equals(other.acceptHeader))
        return false;
      if (path == null) {
        if (other.path != null)
          return false;
      } else if (!path.equals(other.path))
        return false;
      if (queryOptions == null) {
        if (other.queryOptions != null)
          return false;
      } else if (!queryOptions.equals(other.queryOptions))
        return false;
      return true;
    }

    public void execute(URI serviceEndpoint) throws Exception {
      HttpGet get = null;
      
      try {
        String endpoint = serviceEndpoint.toASCIIString();
        String requestUrl = endpoint.substring(0, endpoint.length()-1) + path;
        if(queryOptions != null) {
          requestUrl += queryOptions;
        }
        get = new HttpGet(URI.create(requestUrl));
        
        requestLine = get.getRequestLine().toString();
        if(acceptHeader != null && acceptHeader.length() > 0) {
          get.setHeader(HttpHeaders.ACCEPT, acceptHeader);
        }
        HttpClient httpClient = new DefaultHttpClient();
        final HttpResponse response = httpClient.execute(get);
        
        int resultStatusCode = response.getStatusLine().getStatusCode();
        assertEquals("Unexpected status code for " + toString(), expectedStatusCode, resultStatusCode);
    
        final String contentType = response.getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue();
        assertEquals("Unexpected content type for " + toString(), ContentType.create(expectedContentType), ContentType.create(contentType));
    
        if(isContentExpected) {
          assertNotNull("Unexpected content for " + toString(), StringHelper.inputStreamToString(response.getEntity().getContent()));
        }
      } finally {
        if(get != null) {
          get.releaseConnection();
        }
      }
    }

    public static FitTest create(UriType uriType, String path, String queryOption, String acceptHeader, int expectedStatusCode, String expectedContentType, boolean isContentExpected) {
      return new FitTest(uriType, path, queryOption, acceptHeader, expectedStatusCode, expectedContentType, isContentExpected);
    }
    
    private static List<FitTest> create(UriType uriType, String path, List<String> queryOptions, List<String> acceptHeaders, int expectedStatusCode) {
      Map<String, ContentType> acceptHeader2ContentType  = Collections.emptyMap();
      return create(uriType, path, queryOptions, acceptHeaders, acceptHeader2ContentType, expectedStatusCode);
    }
    
    private static List<FitTest> create(UriType uriType, String path, List<String> queryOptions, List<String> acceptHeaders, int expectedStatusCode, String expectedContentType) {
      Map<String, ContentType> acc2Ct = new HashMap<String, ContentType>();
      for (String acceptHeader : acceptHeaders) {
        acc2Ct.put(acceptHeader, ContentType.create(expectedContentType));
      }
      
      return create(uriType, path, queryOptions, acceptHeaders, acc2Ct, expectedStatusCode);
    }
    
    private static List<FitTest> create(UriType uriType, String path, List<String> queryOptions, List<String> acceptHeaders, 
        Map<String, ContentType> acceptHeader2ContentType, int expectedStatusCode) {

      List<FitTest> testParameters = new ArrayList<ContentNegotiationAcceptHeaderTest.FitTest>();
      boolean isContentExpected = false;
      
      for (String queryOption: queryOptions) {
        for (String acceptHeader: acceptHeaders) {
          ContentType expectedContentType = acceptHeader2ContentType.get(acceptHeader);
          if(expectedContentType == null) {
            expectedContentType = ContentType.create(ContentType.create(acceptHeader), ContentType.PARAMETER_CHARSET, ContentType.CHARSET_UTF_8);
          }
          FitTest tp = FitTest.create(uriType, path, queryOption, acceptHeader, 
              expectedStatusCode, expectedContentType.toContentTypeString(), isContentExpected);
          testParameters.add(tp);
        }
      }

      return testParameters;
    }
  }
}
