package org.odata4j.core;

/**
 * A static factory to create immutable {@link OError} instances.
 */
public class OErrors {

  /**
   * Creates a new {@link OError} instance.
   *
   * @param code  the technical error code
   * @param message  the human-readable error message
   * @param innerError  details about the error, e.g., the stack trace
   * @return an instance of {@link OError}
   */
  public static OError error(final String code, final String message, final String innerError) {
    return new OErrorImpl(code, message, innerError);
  }

  private static class OErrorImpl implements OError {

    private final String code;
    private final String message;
    private final String innerError;

    private OErrorImpl(String code, String message, String innerError) {
      this.code = code;
      this.message = message;
      this.innerError = innerError;
    }

    public String getCode() {
      return code;
    }

    public String getMessage() {
      return message;
    }

    public String getInnerError() {
      return innerError;
    }
  }
}
