package com.sap.core.odata.core.fit.basic.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;
import org.odata4j.exceptions.NotFoundException;

import com.sap.core.odata.core.edm.Edm;
import com.sap.core.odata.core.edm.EdmServiceMetadata;
import com.sap.core.odata.core.producer.Entity;
import com.sap.core.odata.core.producer.Metadata;
import com.sap.core.odata.core.producer.ODataProducer;
import com.sap.core.odata.fit.AbstractFitTest;
import com.sap.core.odata.fit.StringStreamHelper;

public class ErrorResponseTest extends AbstractFitTest {

  private ODataProducer producer;

  @Override
  protected ODataProducer createProducer() {
    this.producer = mock(ODataProducer.class);

    EdmServiceMetadata edmsm = mock(EdmServiceMetadata.class);
    when(edmsm.getDataServiceVersion()).thenReturn("2.0");
    
    Edm edm = mock(Edm.class);
    when(edm.getServiceMetadata()).thenReturn(edmsm);
    
    Metadata metadata = mock(Metadata.class);
    when(metadata.getEdm()).thenReturn(edm);
    
    Entity entity = mock(Entity.class);
    
    when(this.producer.getMetadata()).thenReturn(metadata);
    when(this.producer.getEntity()).thenReturn(entity);
    
    return this.producer;
  }

  @Test
  public void test404NotFound() throws ClientProtocolException, IOException {

    Entity entityProducer = this.producer.getEntity();

    doThrow(new NotFoundException("Buhh")).when(entityProducer).read();

    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "xyz"));
    HttpResponse response = this.getHttpClient().execute(get);
    assertEquals(404, response.getStatusLine().getStatusCode());

    this.log.debug(StringStreamHelper.inputStreamToString(response.getEntity().getContent()));
  }

}
