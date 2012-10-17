package com.sap.core.odata.core.fit.basic.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.core.edm.Edm;
import com.sap.core.odata.core.edm.EdmEntityContainer;
import com.sap.core.odata.core.edm.EdmEntitySet;
import com.sap.core.odata.core.producer.EntitySet;
import com.sap.core.odata.fit.StringStreamHelper;

public class ErrorResponseTest extends AbstractBasicTest {

  @Before
  public void before() throws Exception {
    super.before();

    EdmEntitySet edmEntitySet = mock(EdmEntitySet.class);
    EdmEntityContainer edmEntityContainer = mock(EdmEntityContainer.class);
    when(edmEntityContainer.getEntitySet("entityset")).thenReturn(edmEntitySet);
    Edm edm = this.getProducer().getMetadata().getEdm();
    when(edm.getDefaultEntityContainer()).thenReturn(edmEntityContainer);
    EntitySet entitySet = this.getProducer().getEntitySet();
    when(entitySet.read()).thenReturn(Response.ok().entity("entityset").build());
  }

  
  @Test
  public void test500RuntimeError() throws ClientProtocolException, IOException {

    EntitySet entitySetProducer = this.getProducer().getEntitySet();

    doThrow(new RuntimeException("Bumms")).when(entitySetProducer).read();

    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "entityset"));
    HttpResponse response = this.getHttpClient().execute(get);
    assertEquals(500, response.getStatusLine().getStatusCode());

    this.log.debug(StringStreamHelper.inputStreamToString(response.getEntity().getContent()));
  }

}
