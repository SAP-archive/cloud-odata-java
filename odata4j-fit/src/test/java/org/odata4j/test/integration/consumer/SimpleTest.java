package org.odata4j.test.integration.consumer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.odata4j.test.integration.TestInMemoryProducers.SIMPLE_ENTITY_SET_NAME;
import static org.odata4j.test.integration.TestInMemoryProducers.SIMPLE_PRODUCER;

import org.junit.Test;
import org.odata4j.producer.resources.DefaultODataProducerProvider;
import org.odata4j.test.integration.AbstractODataConsumerTest;
import org.odata4j.test.integration.TestInMemoryProducers;

public class SimpleTest extends AbstractODataConsumerTest {

  public SimpleTest(RuntimeFacadeType type) {
    super(type);
  }

  @Override
  protected void registerODataProducer() throws Exception {
    DefaultODataProducerProvider.setInstance(TestInMemoryProducers.simple());
  }

  @Test
  public void serviceRootUriEqualsBaseUri() throws Exception {
    assertThat(consumer.getServiceRootUri(), is(BASE_URI));
  }

  @Test
  public void enitySetsCountIs1() throws Exception {
    assertThat(consumer.getEntitySets().count(), is(1));
  }

  @Test
  public void firstEnitySetTitleEqualsSimpleEntitySetName() throws Exception {
    assertThat(consumer.getEntitySets().first().getTitle(), is(SIMPLE_ENTITY_SET_NAME));
  }

  @Test
  public void metaDataIsNotNull() throws Exception {
    assertThat(consumer.getMetadata(), notNullValue());
  }

  @Test
  public void metaDataVersionIs2_0() throws Exception {
    assertThat(consumer.getMetadata().getVersion(), is("2.0"));
  }

  @Test
  public void edmEntityTypeFoundByNameIsNotNull() throws Exception {
    assertThat(consumer.getMetadata().findEdmEntityType(SIMPLE_PRODUCER + "." + SIMPLE_ENTITY_SET_NAME), notNullValue());
  }
}
