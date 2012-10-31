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
import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.enums.HttpStatus;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.aspect.EntitySet;
import com.sap.core.odata.core.uri.UriParserResultImpl;
import com.sap.core.testutils.StringHelper;


public class ErrorResponseTest extends AbstractBasicTest {

  @Before
  public void before() throws Exception {
    super.before();

    EdmEntitySet edmEntitySet = mock(EdmEntitySet.class);
    EdmEntityContainer edmEntityContainer = mock(EdmEntityContainer.class);
    when(edmEntityContainer.getEntitySet("entityset")).thenReturn(edmEntitySet);
    Edm edm = this.getProcessor().getMetadataProcessor().getEdm();
    when(edm.getDefaultEntityContainer()).thenReturn(edmEntityContainer);
    EntitySet entitySet = this.getProcessor().getEntitySetProcessor();
    UriParserResultImpl uriParserResult = mock(UriParserResultImpl.class);
    when(entitySet.readEntitySet(uriParserResult)).thenReturn(ODataResponse.status(HttpStatus.OK).entity("entityset").build());
  }

  
  @Test
  public void test500RuntimeError() throws ClientProtocolException, IOException, ODataException {

    EntitySet entitySetProcessor = this.getProcessor().getEntitySetProcessor();
    UriParserResultImpl uriParserResult = mock(UriParserResultImpl.class);
    doThrow(new RuntimeException("Bumms")).when(entitySetProcessor).readEntitySet(uriParserResult);

    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "entityset"));
    HttpResponse response = this.getHttpClient().execute(get);
    assertEquals(500, response.getStatusLine().getStatusCode());

    this.log.debug(StringHelper.inputStreamToString(response.getEntity().getContent()));
  }

}
