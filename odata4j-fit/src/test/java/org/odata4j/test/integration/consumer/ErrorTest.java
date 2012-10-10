package org.odata4j.test.integration.consumer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

import org.junit.After;
import org.junit.Test;
import org.odata4j.core.OError;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.exceptions.ExceptionFactory;
import org.odata4j.exceptions.ODataProducerException;
import org.odata4j.exceptions.ODataProducerExceptions;
import org.odata4j.producer.inmemory.InMemoryProducer;
import org.odata4j.producer.resources.DefaultODataProducerProvider;
import org.odata4j.test.integration.AbstractODataConsumerTest;

public class ErrorTest extends AbstractODataConsumerTest {

  public ErrorTest(RuntimeFacadeType type) {
    super(type);
  }

  @Override
  protected void registerODataProducer() throws Exception {
    InMemoryProducer producer = new InMemoryProducer(null) {

      @Override
      public EdmDataServices getMetadata() {
        // the producer pretends to be unavailable
        throw new ServiceUnavailableException();
      }
    };
    DefaultODataProducerProvider.setInstance(producer);
  }

  @After
  public void teardown() throws Exception {
    super.teardown();
    ODataProducerExceptions.restore();
  }

  @Test
  public void catchUnspecificException() throws Exception {
    try {
      consumer.getEntitySets();
      fail("No exception thrown");
    } catch (ServiceUnavailableException e) {
      fail("Wrong exception thrown");
    } catch (ODataProducerException e) {
      // as ServiceUnavailableException is not known by the ODataProducerExceptions
      // factory, an unspecific exception is thrown
      assertThat(e.getOError().getCode(), is("ServiceUnavailableException"));
    }
  }

  @Test
  public void catchSpecificException() throws Exception {
    // add ServiceUnavailableException to the ODataProducerExceptions factory
    ODataProducerExceptions.add(new ServiceUnavailableException.Factory());

    try {
      consumer.getEntitySets();
      fail("No exception thrown");
    } catch (ServiceUnavailableException e) {
      // now the specific exception is thrown
      assertThat(e.getOError().getCode(), is("ServiceUnavailableException"));
    } catch (ODataProducerException e) {
      fail("Wrong exception thrown");
    }
  }

  public static class ServiceUnavailableException extends ODataProducerException {

    private static final long serialVersionUID = 1L;

    public ServiceUnavailableException() {
      super(null, null);
    }

    @Override
    public StatusType getHttpStatus() {
      return Status.SERVICE_UNAVAILABLE;
    }

    private ServiceUnavailableException(OError error) {
      super(error);
    }

    public static class Factory implements ExceptionFactory<ServiceUnavailableException> {

      @Override
      public int getStatusCode() {
        return Status.SERVICE_UNAVAILABLE.getStatusCode();
      }

      @Override
      public ServiceUnavailableException createException(OError error) {
        return new ServiceUnavailableException(error);
      }
    }
  }
}
