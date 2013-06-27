package com.sap.core.odata.fit.ref;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.junit.Test;

import com.sap.core.odata.testutil.helper.StringHelper;

/**
 * 
 * @author SAP AG
 */
public class BatchTest extends AbstractRefTest {

  private static final String LF = "\n";
  private static final String REG_EX_BOUNDARY = "(([a-zA-Z0-9_\\-\\.'\\+]{1,70})|\"([a-zA-Z0-9_\\-\\.'\\+\\s\\(\\),/:=\\?]{1,69}[a-zA-Z0-9_\\-\\.'\\+\\(\\),/:=\\?])\")";
  private static final String REG_EX = "multipart/mixed;\\s*boundary=" + REG_EX_BOUNDARY + "\\s*";

  private static final String REQUEST_PAYLOAD =
      "--batch_98c1-8b13-36bb" + LF
          + "Content-Type: application/http" + LF
          + "Content-Transfer-Encoding: binary" + LF
          + LF
          + "GET Employees('1')/EmployeeName HTTP/1.1" + LF
          + "Host: localhost:19000" + LF
          + "Accept: application/atomsvc+xml;q=0.8, application/json;odata=verbose;q=0.5, */*;q=0.1" + LF
          + "Accept-Language: en" + LF
          + "MaxDataServiceVersion: 2.0" + LF
          + LF
          + LF
          + "--batch_98c1-8b13-36bb" + LF
          + "Content-Type: multipart/mixed; boundary=changeset_f980-1cb6-94dd" + LF
          + LF
          + "--changeset_f980-1cb6-94dd" + LF
          + "Content-Type: application/http" + LF
          + "Content-Transfer-Encoding: binary" + LF
          + LF
          + "PUT Employees('1')/EmployeeName HTTP/1.1" + LF
          + "Host: localhost:19000" + LF
          + "Content-Type: application/json;odata=verbose" + LF
          + "MaxDataServiceVersion: 2.0" + LF
          + LF
          + "{\"EmployeeName\":\"Walter Winter MODIFIED\"}" + LF
          + LF
          + "--changeset_f980-1cb6-94dd--" + LF
          + LF
          + "--batch_98c1-8b13-36bb--";

  @Test
  public void testBatch() throws Exception {
    final HttpPost post = new HttpPost(URI.create(getEndpoint().toString() + "$batch"));
    post.setHeader("Content-Type", "multipart/mixed;boundary=batch_98c1-8b13-36bb");
    HttpEntity entity = new StringEntity(REQUEST_PAYLOAD);
    post.setEntity(entity);
    HttpResponse response = getHttpClient().execute(post);

    assertNotNull(response);
    assertEquals(202, response.getStatusLine().getStatusCode());
    assertEquals("HTTP/1.1", response.getProtocolVersion().toString());
    assertTrue(response.containsHeader("Content-Length"));
    assertTrue(response.containsHeader("Content-Type"));
    assertTrue(response.containsHeader("DataServiceVersion"));
    assertTrue(response.getEntity().getContentType().getValue().matches(REG_EX));
    assertNotNull(response.getEntity().getContent());
  }
  
  @Test
  public void testSimpleBatch() throws Exception {
    
    final HttpPost post = new HttpPost(URI.create(getEndpoint().toString() + "$batch"));
    post.setHeader("Content-Type", "multipart/mixed;boundary=batch_123");

    String body = StringHelper.inputStreamToString(this.getClass().getResourceAsStream("/simple.batch"), true);
    HttpEntity entity = new StringEntity(body);
    post.setEntity(entity);
    HttpResponse response = getHttpClient().execute(post);

    assertNotNull(response);
    assertEquals(202, response.getStatusLine().getStatusCode());
  }

}
