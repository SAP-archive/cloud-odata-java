package org.odata4j.test.integration.producer.jpa.oneoff.oneoff06;

import junit.framework.Assert;

import org.junit.Test;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.core.ODataConstants.Charsets;
import org.odata4j.test.integration.producer.jpa.oneoff.AbstractOneoffBaseTest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

public class Oneoff06_JsonCreateTest extends AbstractOneoffBaseTest {

  public Oneoff06_JsonCreateTest(RuntimeFacadeType type) {
    super(type);
  }

  /*
   * TODO currently not decoupled from Jersey -> introduce response facade in dependency of CXF options
   */
  private ClientResponse response;

  @Test
  public void createCountry() throws Exception {
    ODataConsumer c = this.rtFacade.createODataConsumer(endpointUri, null);
    Assert.assertEquals(0, c.getEntities("Country").execute().count());

    this.requestPost();

    System.out.println(this.getResponseEntity());
    Assert.assertEquals(1, c.getEntities("Country").execute().count());
    Assert.assertEquals(201, this.getResponseStatus());
    Assert.assertEquals("application/json;charset=" + Charsets.Lower.UTF_8, this.getResponseType());
  }

  private String getResponseEntity() {
    return this.response.getEntity(String.class);
  }

  private int getResponseStatus() {
    return this.response.getStatus();
  }

  private String getResponseType() {
    return this.response.getType().toString();
  }

  private void requestPost() {
    Client client = Client.create();
    this.response = client.resource(endpointUri)
        .path("Country")
        .accept("application/json") // will fail without this line
        .type("application/json;charset=" + Charsets.Lower.UTF_8)
        .post(ClientResponse.class, "{ \"name\":\"Ireland\"}");
  }

}
