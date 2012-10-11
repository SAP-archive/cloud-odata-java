package com.sap.core.odata.core.fit.basic.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;
import org.odata4j.exceptions.NotFoundException;

import com.sap.core.odata.fit.AbstractFitTest;
import com.sap.core.odata.fit.StringStreamHelper;
import com.sap.core.odata.producer.Entity;
import com.sap.core.odata.producer.ODataProducer;

public class ErrorResponseTest extends AbstractFitTest {

  private ODataProducer producer;

  @Override
  protected ODataProducer createProducer() {
    this.producer = mock(ODataProducer.class, withSettings().extraInterfaces(Entity.class));
    return this.producer;
  }

  @Test
  public void test404NotFound() throws ClientProtocolException, IOException {

    Entity entityProducer = (Entity) this.producer;

    doThrow(new NotFoundException("Bähh")).when(entityProducer).read();

    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "xyz"));
    HttpResponse response = this.getHttpClient().execute(get);
    assertEquals(404, response.getStatusLine().getStatusCode());

    this.log.debug(StringStreamHelper.inputStreamToString(response.getEntity().getContent()));
  }

}
