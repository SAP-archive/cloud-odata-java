package org.odata4j.exceptions;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

import org.odata4j.core.OError;

public class NotAuthorizedException extends ODataProducerException {

  private static final long serialVersionUID = 1L;

  public NotAuthorizedException() {
    this(null, null);
  }

  public NotAuthorizedException(String message) {
    this(message, null);
  }

  public NotAuthorizedException(Throwable cause) {
    this(null, cause);
  }

  public NotAuthorizedException(String message, Throwable cause) {
    super(message, cause);
  }

  @Override
  public StatusType getHttpStatus() {
    return Status.UNAUTHORIZED;
  }

  private NotAuthorizedException(OError error) {
    super(error);
  }

  public static class Factory implements ExceptionFactory<NotAuthorizedException> {

    @Override
    public int getStatusCode() {
      return Status.UNAUTHORIZED.getStatusCode();
    }

    @Override
    public NotAuthorizedException createException(OError error) {
      return new NotAuthorizedException(error);
    }
  }
}
