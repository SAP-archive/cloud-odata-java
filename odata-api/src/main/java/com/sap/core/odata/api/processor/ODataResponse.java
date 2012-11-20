package com.sap.core.odata.api.processor;

import java.util.Set;

import com.sap.core.odata.api.enums.HttpStatusCodes;
import com.sap.core.odata.api.rt.RuntimeDelegate;

/**
 * An <code>ODataResponse</code> is usually created by a {@link ODataProcessor} during request handling. The handler can use a serializer to create an 
 * OData body (== response entity) and can set various response headers.<p>
 * A respons can be created using the builder pattern:
 * <pre>
 * {@code
 * ODataResponse response = ODataResponse.entity("hello world").setStatus(200).build();
 * }
 * </pre>
 * @author SAP AG
 */
public abstract class ODataResponse {

  /**
   * Do not subclass ODataResponse!
   */
  protected ODataResponse() {}

  /**
   * @return http status code of this response
   */
  public abstract HttpStatusCodes getStatus();

  /**
   * @return a response entity which become the body part of a response message
   */
  public abstract Object getEntity();

  /**
   * @param name http response header name
   * @return a header value or null if not set
   */
  public abstract String getHeader(String name);

  public abstract String getIdLiteral();

  /**
   * @return etag value or null if not available
   */
  public abstract String getETag();

  /**
   * @return a set of all available header names
   */
  public abstract Set<String> getHeaderNames();

  /**
   * @param status http stauts code
   * @return a builder object
   */
  public static ODataResponseBuilder status(HttpStatusCodes status) {
    ODataResponseBuilder b = ODataResponseBuilder.newInstance();
    b.status(status);
    return b;
  }

  /**
   * @param entity
   * @return a builder object
   */
  public static ODataResponseBuilder entity(Object entity) {
    ODataResponseBuilder b = ODataResponseBuilder.newInstance();
    b.entity(entity);
    return b;
  }

  /**
   * @param name http header name
   * @param value associated value
   * @return a builder object
   */
  public static ODataResponseBuilder header(String name, String value) {
    ODataResponseBuilder b = ODataResponseBuilder.newInstance();
    b.header(name, value);
    return b;
  }

  /**
   * Implementation of the builder pattern to create instances of these type of object. 
   * @author SAP AG
   */
  public static abstract class ODataResponseBuilder {

    protected ODataResponseBuilder() {}

    private static ODataResponseBuilder newInstance() {
      ODataResponseBuilder b = RuntimeDelegate.getInstance().createODataResponseBuilder();
      return b;
    }

    public abstract ODataResponse build();

    public abstract ODataResponseBuilder status(HttpStatusCodes status);

    public abstract ODataResponseBuilder entity(Object entity);

    public abstract ODataResponseBuilder header(String name, String value);

    public abstract ODataResponseBuilder idLiteral(String idLiteral);

    public abstract ODataResponseBuilder eTag(String eTag);
  }

}
