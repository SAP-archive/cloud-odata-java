package org.odata4j.exceptions;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response.StatusType;

import org.odata4j.core.OError;
import org.odata4j.core.Throwables;

/**
 * A static factory to create {@link ODataProducerException} instances.
 * <p>This factory is used after deserialization of error responses retrieved from an OData
 * producer; it returns one of the following exceptions based on the HTTP status of the response:
 * <ul><li>{@link BadRequestException},</li>
 * <li>{@link ForbiddenException},</li>
 * <li>{@link MethodNotAllowedException},</li>
 * <li>{@link NotAcceptableException},</li>
 * <li>{@link NotAuthorizedException},</li>
 * <li>{@link NotFoundException},</li>
 * <li>{@link NotImplementedException},</li>
 * <li>{@link ServerErrorException},</li>
 * <li>{@link UnsupportedMediaTypeException}</li></ul>
 * In case the HTTP status cannot be mapped to a specific sub-class, an unspecific ODataProducerException is thrown.</p>
 */
public class ODataProducerExceptions {

  private static ODataProducerExceptions SINGLETON = new ODataProducerExceptions();

  private ODataProducerExceptions() {}

  private Map<Integer, ExceptionFactory<?>> exceptionFactories = null;

  private void initializeExceptionMap() throws InstantiationException, IllegalAccessException {
    exceptionFactories = new HashMap<Integer, ExceptionFactory<?>>();

    add(new BadRequestException.Factory());
    add(new ForbiddenException.Factory());
    add(new MethodNotAllowedException.Factory());
    add(new NotAcceptableException.Factory());
    add(new NotAuthorizedException.Factory());
    add(new NotFoundException.Factory());
    add(new NotImplementedException.Factory());
    add(new ServerErrorException.Factory());
    add(new UnsupportedMediaTypeException.Factory());
  }

  private static void ensureThatExceptionFactoryMapIsInitialized() throws InstantiationException, IllegalAccessException {
    if (SINGLETON.exceptionFactories == null)
      SINGLETON.initializeExceptionMap();
  }

  /**
   * Adds an {@link ExceptionFactory} and thus makes the created exception available (and catchable) for OData consumers.
   *
   * @param exceptionFactory  the exception factory to add
   */
  public static void add(final ExceptionFactory<?> exceptionFactory) {
    try {
      ensureThatExceptionFactoryMapIsInitialized();

      SINGLETON.exceptionFactories.put(exceptionFactory.getStatusCode(), exceptionFactory);
    } catch (Exception e) {
      Throwables.propagate(e);
    }
  }

  /**
   * Restores this factory to its initial state.
   */
  public static void restore() {
    try {
      SINGLETON.initializeExceptionMap();
    } catch (Exception e) {
      Throwables.propagate(e);
    }
  }

  /**
   * Creates a new {@link ODataProducerException}.
   *
   * @param status  the HTTP status received in conjunction with this error
   * @param error  the OData error message returned by the producer
   * @return an instance of {@link ODataProducerException}
   */
  public static ODataProducerException create(final StatusType status, final OError error) {
    try {
      ensureThatExceptionFactoryMapIsInitialized();

      ExceptionFactory<?> exceptionFactory = SINGLETON.exceptionFactories.get(status.getStatusCode());
      if (exceptionFactory != null)
        return exceptionFactory.createException(error);
      else
        return unspecificException(status, error);
    } catch (Exception e) {
      Throwables.propagate(e);
      return null;
    }
  }

  private static ODataProducerException unspecificException(final StatusType status, final OError error) {
    return new ODataProducerException(error) {

      private static final long serialVersionUID = 1L;

      @Override
      public StatusType getHttpStatus() {
        return status;
      }
    };
  }
}
