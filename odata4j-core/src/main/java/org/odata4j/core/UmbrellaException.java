package org.odata4j.core;

import java.util.Collection;

/** Unchecked exception that rolls up one or more underlying exceptions. */
public class UmbrellaException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final Collection<Exception> exceptions;

  public UmbrellaException(Collection<Exception> exceptions) {
    this.exceptions = exceptions;
  }

  public Collection<Exception> getExceptions() {
    return exceptions;
  }

}
