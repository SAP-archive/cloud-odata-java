package org.odata4j.test.integration.roundtrip;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.odata4j.core.OProperty;
import org.odata4j.examples.producer.inmemory.AddressBookInMemoryExample;
import org.odata4j.examples.producer.jpa.AddressBookJpaExample;
import org.odata4j.format.FormatType;
import org.odata4j.producer.ODataProducer;
import org.odata4j.producer.resources.DefaultODataProducerProvider;
import org.odata4j.test.integration.AbstractODataConsumerTest;
import org.odata4j.test.integration.AbstractRuntimeTest;
import org.odata4j.test.integration.ParameterizedTestHelper;
import org.odata4j.test.integration.ProducerImpl;

@RunWith(Parameterized.class)
public class AddressBookODataConsumerTest extends AbstractODataConsumerTest {

  @Parameters
  public static List<Object[]> data() {
    List<Object[]> parametersList = AbstractRuntimeTest.data();
    parametersList = ParameterizedTestHelper.addVariants(parametersList, FormatType.JSON, FormatType.ATOM);
    parametersList = ParameterizedTestHelper.addVariants(parametersList, ProducerImpl.IN_MEMORY, ProducerImpl.JPA);
    return parametersList;
  }

  private final ProducerImpl producerImpl;

  public AddressBookODataConsumerTest(RuntimeFacadeType type, FormatType format, ProducerImpl producerImpl) {
    super(type, format);
    this.producerImpl = producerImpl;
    log("parameterized format type", format.toString());
    log("parameterized producer implementation", producerImpl.toString());
  }

  @Override
  protected void registerODataProducer() throws Exception {
    ODataProducer producer = producerImpl == ProducerImpl.JPA ? AddressBookJpaExample.createProducer() : AddressBookInMemoryExample.createProducer();
    DefaultODataProducerProvider.setInstance(producer);
  }

  @Test
  public void entityStringProperty() throws Exception {
    OProperty<?> property = consumer.getEntity("Persons", 1).execute().getProperty("Name");
    assertThat((String) property.getValue(), is("Susan Summer"));
  }

  @Test
  public void entityDateTimeProperty() throws Exception {
    OProperty<?> property = consumer.getEntity("Persons", 2).execute().getProperty("BirthDay");
    assertThat((LocalDateTime) property.getValue(), is(new LocalDateTime(1968, 1, 13, 0, 0)));
  }
}
