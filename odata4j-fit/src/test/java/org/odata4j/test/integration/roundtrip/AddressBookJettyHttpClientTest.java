package org.odata4j.test.integration.roundtrip;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;
import static org.odata4j.format.FormatType.JSON;

import java.util.List;

import org.eclipse.jetty.client.ContentExchange;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.odata4j.examples.producer.inmemory.AddressBookInMemoryExample;
import org.odata4j.examples.producer.jpa.AddressBookJpaExample;
import org.odata4j.format.FormatType;
import org.odata4j.producer.ODataProducer;
import org.odata4j.producer.resources.DefaultODataProducerProvider;
import org.odata4j.test.integration.AbstractJettyHttpClientTest;
import org.odata4j.test.integration.AbstractRuntimeTest;
import org.odata4j.test.integration.ParameterizedTestHelper;
import org.odata4j.test.integration.ProducerImpl;

@RunWith(Parameterized.class)
public class AddressBookJettyHttpClientTest extends AbstractJettyHttpClientTest {

  @Parameters
  public static List<Object[]> data() {
    List<Object[]> parametersList = AbstractRuntimeTest.data();
    parametersList = ParameterizedTestHelper.addVariants(parametersList, FormatType.JSON, FormatType.ATOM);
    parametersList = ParameterizedTestHelper.addVariants(parametersList, ProducerImpl.IN_MEMORY, ProducerImpl.JPA);
    return parametersList;
  }

  private final FormatType format;
  private final ProducerImpl producerImpl;

  public AddressBookJettyHttpClientTest(RuntimeFacadeType type, FormatType format, ProducerImpl producerImpl) {
    super(type);
    this.format = format;
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
  public void stringProperty() throws Exception {
    ContentExchange exchange = sendRequest("Persons(1)/Name", format);
    if (format.equals(JSON))
      assertThat(exchange.getResponseContent(), containsString("\"Susan Summer\""));
    else
      assertThat(exchange.getResponseContent(), containsString(">Susan Summer<"));
  }

  @Test
  public void dateTimeProperty() throws Exception {
    ContentExchange exchange = sendRequest("Persons(2)/BirthDay", format);
    if (format.equals(JSON))
      assertThat(exchange.getResponseContent(), containsString("\"\\/Date(-62121600000)\\/\""));
    else
      assertThat(exchange.getResponseContent(), containsString(">1968-01-13T00:00<"));
  }

  private ContentExchange sendRequest(String requestUri, FormatType format) throws Exception {
    return sendRequest(BASE_URI + requestUri + (!requestUri.contains("?") ? "?" : "") + "$format=" + format);
  }
}
