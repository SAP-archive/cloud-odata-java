package com.sap.core.odata.fit.ref;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

  @Test
  public void testSimpleBatch() throws Exception {
    String responseBody = execute("/simple.batch");
    assertFalse(responseBody.contains("<error xmlns=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\">"));
    assertTrue(responseBody.contains("<edmx:Edmx Version=\"1.0\""));
  }

  @Test
  public void testChangeSetBatch() throws Exception {
    String responseBody = execute("/changeset.batch");
    assertTrue(responseBody.contains("Frederic Fall MODIFIED"));
  }

  private String execute(String batchResource) throws Exception {
    final HttpPost post = new HttpPost(URI.create(getEndpoint().toString() + "$batch"));
    post.setHeader("Content-Type", "multipart/mixed;boundary=batch_123");

    String body = StringHelper.inputStreamToString(this.getClass().getResourceAsStream(batchResource), true);
    HttpEntity entity = new StringEntity(body);
    post.setEntity(entity);
    HttpResponse response = getHttpClient().execute(post);

    assertNotNull(response);
    assertEquals(202, response.getStatusLine().getStatusCode());

    String responseBody = StringHelper.inputStreamToString(response.getEntity().getContent());
    return responseBody;
  }
  
}
