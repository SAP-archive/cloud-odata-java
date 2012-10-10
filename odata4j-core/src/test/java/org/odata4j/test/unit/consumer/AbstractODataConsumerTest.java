package org.odata4j.test.unit.consumer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.odata4j.consumer.AbstractODataConsumer;
import org.odata4j.consumer.ODataClient;
import org.odata4j.consumer.ODataClientRequest;
import org.odata4j.core.ODataConstants.Headers;
import org.odata4j.core.OEntities;
import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityKey;
import org.odata4j.core.OProperty;
import org.odata4j.edm.EdmEntitySet;

public class AbstractODataConsumerTest {

  private static class MockConsumer extends AbstractODataConsumer {

    public String lastMethodName;
    public Object[] lastMethodArgs;

    private final ODataClient client;

    protected MockConsumer() {
      super("MockServiceRoot");

      InvocationHandler h = new InvocationHandler() {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
          lastMethodName = method.getName();
          lastMethodArgs = args;
          return null;
        }
      };
      client = (ODataClient) Proxy.newProxyInstance(ODataClient.class.getClassLoader(), new Class<?>[] { ODataClient.class }, h);
    }

    @Override
    protected ODataClient getClient() {
      return client;
    }

  }

  @Test
  public void deletesAndEtags() {
    MockConsumer c = new MockConsumer();
    EdmEntitySet ees = new EdmEntitySet.Builder().build();
    OEntityKey entityKey = OEntityKey.create(1);
    String entityTag = "ETAG";
    List<OProperty<?>> properties = Collections.emptyList();

    // with etag
    OEntity entityWithEtag = OEntities.create(ees, null, entityKey, entityTag, properties, null);

    c.deleteEntity(entityWithEtag).execute();
    ODataClientRequest request = (ODataClientRequest) c.lastMethodArgs[0];
    Assert.assertEquals("deleteEntity", c.lastMethodName);
    Assert.assertEquals(entityTag, request.getHeaders().get(Headers.IF_MATCH));

    c.deleteEntity(entityWithEtag).ifMatch("*").execute();
    Assert.assertEquals("deleteEntity", c.lastMethodName);
    request = (ODataClientRequest) c.lastMethodArgs[0];
    Assert.assertEquals("*", request.getHeaders().get(Headers.IF_MATCH));

    c.deleteEntity(entityWithEtag).ifMatch(null).execute();
    Assert.assertEquals("deleteEntity", c.lastMethodName);
    request = (ODataClientRequest) c.lastMethodArgs[0];
    Assert.assertEquals(false, request.getHeaders().containsKey(Headers.IF_MATCH));

    // without etag
    OEntity entityWithoutEtag = OEntities.create(ees, null, entityKey, null, properties, null);
    c.deleteEntity(entityWithoutEtag).execute();
    request = (ODataClientRequest) c.lastMethodArgs[0];
    Assert.assertEquals("deleteEntity", c.lastMethodName);
    Assert.assertEquals(false, request.getHeaders().containsKey(Headers.IF_MATCH));
  }

}
