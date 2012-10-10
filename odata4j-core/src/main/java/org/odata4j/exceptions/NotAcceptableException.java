package org.odata4j.exceptions;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

import org.odata4j.core.OError;

public class NotAcceptableException extends ODataProducerException {

  private static final long serialVersionUID = 1L;

  public NotAcceptableException() {
    this(null, null);
  }

  public NotAcceptableException(String message) {
    this(message, null);
  }

  public NotAcceptableException(Throwable cause) {
    this(null, cause);
  }

  public NotAcceptableException(String message, Throwable cause) {
    super(message, cause);
  }

  @Override
  public StatusType getHttpStatus() {
    return Status.NOT_ACCEPTABLE;
  }

  private NotAcceptableException(OError error) {
    super(error);
  }

  public static class Factory implements ExceptionFactory<NotAcceptableException> {

    @Override
    public int getStatusCode() {
      return Status.NOT_ACCEPTABLE.getStatusCode();
    }

    @Override
    public NotAcceptableException createException(OError error) {
      return new NotAcceptableException(error);
    }
  }
}
