package org.odata4j.producer;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

import org.odata4j.exceptions.ODataProducerException;

/**
 * This class provides implementations of the {@link ErrorResponseExtension} interface for common
 * use cases.
 */
public class ErrorResponseExtensions {

  /**
   * Returns inner errors in all cases.
   */
  public static final ErrorResponseExtension returnInnerErrors() {
    return new ErrorResponseExtension() {
      public boolean returnInnerError(HttpHeaders httpHeaders, UriInfo uriInfo, ODataProducerException exception) {
        return true;
      }
    };
  }

  /**
   * Default system property and query parameter that drives whether or not inner errors are returned.
   *
   * @see ErrorResponseExtensions#returnInnerErrorsBasedOnDefaultSystemProperty
   * @see ErrorResponseExtensions#returnInnerErrorsBasedOnDefaultQueryParameter
   */
  public static final String ODATA4J_DEBUG = "odata4j.debug";

  /**
   * Returns inner errors when the default system property is set to {@code true}.
   *
   * @see ErrorResponseExtensions#ODATA4J_DEBUG
   */
  public static final ErrorResponseExtension returnInnerErrorsBasedOnDefaultSystemProperty() {
    return returnInnerErrorsBasedOnSystemProperty(ODATA4J_DEBUG);
  }

  /**
   * Returns inner errors when a system property is set to {@code true}.
   *
   * @param propertyName  the system property key
   */
  public static final ErrorResponseExtension returnInnerErrorsBasedOnSystemProperty(final String propertyName) {
    return new ErrorResponseExtension() {
      public boolean returnInnerError(HttpHeaders httpHeaders, UriInfo uriInfo, ODataProducerException exception) {
        return Boolean.parseBoolean(System.getProperty(propertyName));
      }
    };
  }

  /**
   * Returns inner errors when the default query parameter is set to {@code true}.
   *
   * @see ErrorResponseExtensions#ODATA4J_DEBUG
   */
  public static final ErrorResponseExtension returnInnerErrorsBasedOnDefaultQueryParameter() {
    return returnInnerErrorsBasedOnQueryParameter(ODATA4J_DEBUG);
  }

  /**
   * Returns inner errors when a custom query parameter is set to {@code true}.
   *
   * @param queryParameterName  the custom parameter key
   */
  public static final ErrorResponseExtension returnInnerErrorsBasedOnQueryParameter(final String queryParameterName) {
    return new ErrorResponseExtension() {
      public boolean returnInnerError(HttpHeaders httpHeaders, UriInfo uriInfo, ODataProducerException exception) {
        return Boolean.parseBoolean(uriInfo.getQueryParameters().getFirst(queryParameterName));
      }
    };
  }

}
