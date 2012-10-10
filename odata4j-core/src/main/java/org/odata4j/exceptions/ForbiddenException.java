package org.odata4j.exceptions;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

import org.odata4j.core.OError;

public class ForbiddenException extends ODataProducerException {

  private static final long serialVersionUID = 1L;

  public ForbiddenException() {
    this(null, null);
  }

  public ForbiddenException(String message) {
    this(message, null);
  }

  public ForbiddenException(Throwable cause) {
    this(null, cause);
  }

  public ForbiddenException(String message, Throwable cause) {
    super(message, cause);
  }

  @Override
  public StatusType getHttpStatus() {
    return Status.FORBIDDEN;
  }

  private ForbiddenException(OError error) {
    super(error);
  }

  public static class Factory implements ExceptionFactory<ForbiddenException> {

    @Override
    public int getStatusCode() {
      return Status.FORBIDDEN.getStatusCode();
    }

    @Override
    public ForbiddenException createException(OError error) {
      return new ForbiddenException(error);
    }
  }
}
