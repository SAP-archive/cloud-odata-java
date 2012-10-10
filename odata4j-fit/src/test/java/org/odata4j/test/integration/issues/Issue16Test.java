package org.odata4j.test.integration.issues;

import junit.framework.Assert;

import org.core4j.Enumerable;
import org.junit.Test;
import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityKey;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.producer.EntitiesResponse;
import org.odata4j.producer.ODataContext;
import org.odata4j.producer.QueryInfo;
import org.odata4j.producer.Responses;
import org.odata4j.producer.inmemory.InMemoryProducer;
import org.odata4j.producer.resources.DefaultODataProducerProvider;
import org.odata4j.test.integration.AbstractJettyHttpClientTest;

public class Issue16Test extends AbstractJettyHttpClientTest {

  final String[] actualNavProp = new String[1];

  public Issue16Test(RuntimeFacadeType type) {
    super(type);
  }

  @Override
  protected void registerODataProducer() throws Exception {
    InMemoryProducer producer = new InMemoryProducer("Issue16") {
      @Override
      public EntitiesResponse getNavProperty(ODataContext context, String entitySetName, OEntityKey entityKey, String navProp, QueryInfo queryInfo) {
        actualNavProp[0] = navProp;
        return Responses.entities(Enumerable.<OEntity> create().toList(), EdmEntitySet.newBuilder().setName("messageLog").build(), null, null);
      }
    };

    DefaultODataProducerProvider.setInstance(producer);
  }

  @Test
  public void issue16() throws Exception {
    sendRequest(BASE_URI + "Message(124L)/messageLog()").waitForDone();

    Assert.assertNotNull(actualNavProp[0]);
    Assert.assertEquals("messageLog", actualNavProp[0]);
  }
}
