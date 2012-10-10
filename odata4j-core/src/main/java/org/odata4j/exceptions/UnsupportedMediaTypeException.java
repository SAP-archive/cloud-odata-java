package org.odata4j.exceptions;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

import org.odata4j.core.OError;

public class UnsupportedMediaTypeException extends ODataProducerException {

  private static final long serialVersionUID = 1L;

  public UnsupportedMediaTypeException() {
    this(null, null);
  }

  public UnsupportedMediaTypeException(String message) {
    this(message, null);
  }

  public UnsupportedMediaTypeException(Throwable cause) {
    this(null, cause);
  }

  public UnsupportedMediaTypeException(String message, Throwable cause) {
    super(message, cause);
  }

  @Override
  public StatusType getHttpStatus() {
    return Status.UNSUPPORTED_MEDIA_TYPE;
  }

  private UnsupportedMediaTypeException(OError error) {
    super(error);
  }

  public static class Factory implements ExceptionFactory<UnsupportedMediaTypeException> {

    @Override
    public int getStatusCode() {
      return Status.UNSUPPORTED_MEDIA_TYPE.getStatusCode();
    }

    @Override
    public UnsupportedMediaTypeException createException(OError error) {
      return new UnsupportedMediaTypeException(error);
    }
  }
}
