package org.odata4j.exceptions;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

import org.odata4j.core.OError;

public class ServerErrorException extends ODataProducerException {

  private static final long serialVersionUID = 1L;

  public ServerErrorException() {
    this(null, null);
  }

  public ServerErrorException(String message) {
    this(message, null);
  }

  public ServerErrorException(Throwable cause) {
    this(null, cause);
  }

  public ServerErrorException(String message, Throwable cause) {
    super(message, cause);
  }

  @Override
  public StatusType getHttpStatus() {
    return Status.INTERNAL_SERVER_ERROR;
  }

  private ServerErrorException(OError error) {
    super(error);
  }

  public static class Factory implements ExceptionFactory<ServerErrorException> {

    @Override
    public int getStatusCode() {
      return Status.INTERNAL_SERVER_ERROR.getStatusCode();
    }

    @Override
    public ServerErrorException createException(OError error) {
      return new ServerErrorException(error);
    }
  }
}
