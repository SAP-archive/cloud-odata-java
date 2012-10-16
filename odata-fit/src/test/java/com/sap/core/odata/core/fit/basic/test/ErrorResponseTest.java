package com.sap.core.odata.core.fit.basic.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.junit.Ignore;
import org.junit.Test;
import org.odata4j.exceptions.NotFoundException;

import com.sap.core.odata.core.producer.Entity;
import com.sap.core.odata.fit.StringStreamHelper;

public class ErrorResponseTest extends AbstractBasicTest {

  @Test
  @Ignore
  public void test404NotFound() throws ClientProtocolException, IOException {

    Entity entityProducer = this.getProducer().getEntity();

    doThrow(new NotFoundException("Buhh")).when(entityProducer).read();

    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "xyz"));
    HttpResponse response = this.getHttpClient().execute(get);
    assertEquals(404, response.getStatusLine().getStatusCode());

    this.log.debug(StringStreamHelper.inputStreamToString(response.getEntity().getContent()));
  }

}
