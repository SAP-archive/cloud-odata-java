package org.odata4j.exceptions;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

import org.odata4j.core.OError;

public class BadRequestException extends ODataProducerException {

  private static final long serialVersionUID = 1L;

  public BadRequestException() {
    this(null, null);
  }

  public BadRequestException(String message) {
    this(message, null);
  }

  public BadRequestException(Throwable cause) {
    this(null, cause);
  }

  public BadRequestException(String message, Throwable cause) {
    super(message, cause);
  }

  @Override
  public StatusType getHttpStatus() {
    return Status.BAD_REQUEST;
  }

  private BadRequestException(OError error) {
    super(error);
  }

  public static class Factory implements ExceptionFactory<BadRequestException> {

    @Override
    public int getStatusCode() {
      return Status.BAD_REQUEST.getStatusCode();
    }

    @Override
    public BadRequestException createException(OError error) {
      return new BadRequestException(error);
    }
  }
}
