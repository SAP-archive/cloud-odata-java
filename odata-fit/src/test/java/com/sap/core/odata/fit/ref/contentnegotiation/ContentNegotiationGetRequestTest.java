package com.sap.core.odata.fit.ref.contentnegotiation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Ignore;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.uri.UriType;
import com.sap.core.odata.testutil.helper.StringHelper;

/**
 * 
 */
public class ContentNegotiationGetRequestTest extends AbstractContentNegotiationTest {

  @Test
  public void acceptHeaderAppAtomXml() throws Exception {
    HttpGet get = new HttpGet(URI.create(getEndpoint() + "Rooms('1')"));
    get.setHeader(HttpHeaders.ACCEPT, HttpContentType.APPLICATION_ATOM_XML);
    final HttpResponse response = new DefaultHttpClient().execute(get);

    final String contentType = response.getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue();
    assertEquals(ContentType.create(HttpContentType.APPLICATION_ATOM_XML_ENTRY_UTF8), ContentType.create(contentType));

    assertNotNull(StringHelper.inputStreamToString(response.getEntity().getContent()));
  }

  @Test
  public void acceptHeaderAppXml() throws Exception {
    HttpGet get = new HttpGet(URI.create(getEndpoint() + "Rooms('1')"));
    get.setHeader(HttpHeaders.ACCEPT, HttpContentType.APPLICATION_XML);
    final HttpResponse response = new DefaultHttpClient().execute(get);

    final String contentType = response.getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue();
    assertEquals(HttpContentType.APPLICATION_XML_UTF8, contentType);
    assertEquals(ContentType.create(HttpContentType.APPLICATION_XML_UTF8), ContentType.create(contentType));

    assertNotNull(StringHelper.inputStreamToString(response.getEntity().getContent()));
  }

  @Test
  public void acceptHeaderAppXmlCharsetUtf8() throws Exception {
    HttpGet get = new HttpGet(URI.create(getEndpoint() + "Rooms('1')"));
    get.setHeader(HttpHeaders.ACCEPT, HttpContentType.APPLICATION_XML_UTF8);
    final HttpResponse response = new DefaultHttpClient().execute(get);

    final String contentType = response.getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue();
    assertEquals(HttpContentType.APPLICATION_XML_UTF8, contentType);
    assertEquals(ContentType.create(HttpContentType.APPLICATION_XML_UTF8), ContentType.create(contentType));

    assertNotNull(StringHelper.inputStreamToString(response.getEntity().getContent()));
  }

  @Test
  public void testURI_0_ServiceDocument() throws Exception {
    // create test set
    FitTestSet testSet = FitTestSet.create(UriType.URI0, "/").init();

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
    FitTestSet testSet = FitTestSet.create(UriType.URI1, "/Employees").init();

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
    FitTestSet testSet = FitTestSet.create(UriType.URI2, "/Employees('1')").init();

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
    FitTestSet testSet = FitTestSet.create(UriType.URI3, "/Employees('1')/Location").init();

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
    FitTestSet testSet = FitTestSet.create(UriType.URI4, "/Employees('1')/Location/Country").init();

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
    FitTestSet testSet = FitTestSet.create(UriType.URI4, "/Employees('1')/Age").init();

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
    FitTestSet testSet = FitTestSet.create(UriType.URI6A, "/Employees('1')/ne_Room").init();

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
    FitTestSet testSet = FitTestSet.create(UriType.URI7A, "/Employees('1')/$links/ne_Room").init();

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
    FitTestSet testSet = FitTestSet.create(UriType.URI8, "/$metadata").init();

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
    FitTestSet testSet = FitTestSet.create(UriType.URI17, "/Employees('1')/$value").expectedStatusCode(200).expectedContentType("image/jpeg").init();
    
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
    FitTestSet testSet = FitTestSet.create(UriType.URI17, "/Employees('1')/Age/$value")
                                    .expectedStatusCode(200).expectedContentType("text/plain; charset=utf-8").init();
    
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

}
