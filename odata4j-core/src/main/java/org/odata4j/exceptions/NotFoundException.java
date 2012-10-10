package org.odata4j.exceptions;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

import org.odata4j.core.OError;

public class NotFoundException extends ODataProducerException {

  private static final long serialVersionUID = 1L;

  public NotFoundException() {
    this(null, null);
  }

  public NotFoundException(String message) {
    this(message, null);
  }

  public NotFoundException(Throwable cause) {
    this(null, cause);
  }

  public NotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  @Override
  public StatusType getHttpStatus() {
    return Status.NOT_FOUND;
  }

  private NotFoundException(OError error) {
    super(error);
  }

  public static class Factory implements ExceptionFactory<NotFoundException> {

    @Override
    public int getStatusCode() {
      return Status.NOT_FOUND.getStatusCode();
    }

    @Override
    public NotFoundException createException(OError error) {
      return new NotFoundException(error);
    }
  }
}
