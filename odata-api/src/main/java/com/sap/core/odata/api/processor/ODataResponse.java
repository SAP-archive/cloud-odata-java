/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.api.processor;

import java.io.IOException;
import java.util.Set;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.rt.RuntimeDelegate;

/**
 * <p>An <code>ODataResponse</code> is usually created by an {@link ODataProcessor}
 * during request handling.</p>
 * <p>The handler can use a serializer to create an 
 * OData body (== response entity) and can set various response headers.
 * A response can be created using the builder pattern:
 * <pre>
 * {@code
 * ODataResponse response = ODataResponse.entity("hello world").setStatus(HttpStatusCodes.OK).build();
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
   * @return HTTP status code of this response
   */
  public abstract HttpStatusCodes getStatus();

  /**
   * @return a response entity which becomes the body part of a response message
   */
  public abstract Object getEntity();

  /**
   * Close the underlying entity input stream (if such a stream is available) and release all with this repsonse associated resources.
   * 
   * @throws IOException if something goes wrong during close of {@link ODataResponse}
   */
  public abstract void close() throws IOException;

  /**
   * @param name HTTP response header name
   * @return a header value or null if not set
   */
  public abstract String getHeader(String name);

  /**
   * @return Content-Type header value or null if not set
   */
  public abstract String getContentHeader();

  /**
   * @return Location header value or null if not set
   */
  public abstract String getIdLiteral();

  /**
   * @return ETag header value or null if not available
   */
  public abstract String getETag();

  /**
   * @return a set of all available header names
   */
  public abstract Set<String> getHeaderNames();

  /**
   * Case insensitive check if the header is available in this ODataResponse
   * @param header header name
   * @return true/false
   */
  public abstract boolean containsHeader(String header);

  /**
   * @param status HTTP status code
   * @return a builder object
   */
  public static ODataResponseBuilder status(final HttpStatusCodes status) {
    return newBuilder().status(status);
  }

  /**
   * @param response
   * @return a new builder object
   */
  public static ODataResponseBuilder fromResponse(final ODataResponse response) {
    return newBuilder().fromResponse(response);
  }

  /**
   * @param entity
   * @return a builder object
   */
  public static ODataResponseBuilder entity(final Object entity) {
    return newBuilder().entity(entity);
  }

  /**
   * @param name  HTTP header name
   * @param value associated value
   * @return a builder object
   */
  public static ODataResponseBuilder header(final String name, final String value) {
    return newBuilder().header(name, value);
  }

  /**
   * @param value content header value
   * @return a builder object
   */
  public static ODataResponseBuilder contentHeader(final String value) {
    return newBuilder().contentHeader(value);
  }

  /**
   * @return returns a new builder object
   */
  public static ODataResponseBuilder newBuilder() {
    return ODataResponseBuilder.newInstance();
  }

  /**
   * Implementation of the builder pattern to create instances of this type of object. 
   * @author SAP AG
   */
  public static abstract class ODataResponseBuilder {

    protected ODataResponseBuilder() {}

    private static ODataResponseBuilder newInstance() {
      return RuntimeDelegate.createODataResponseBuilder();
    }

    public abstract ODataResponse build();

    public abstract ODataResponseBuilder status(HttpStatusCodes status);

    public abstract ODataResponseBuilder entity(Object entity);

    public abstract ODataResponseBuilder header(String name, String value);

    public abstract ODataResponseBuilder idLiteral(String idLiteral);

    public abstract ODataResponseBuilder eTag(String eTag);

    public abstract ODataResponseBuilder contentHeader(String contentHeader);

    protected abstract ODataResponseBuilder fromResponse(ODataResponse response);
  }
}
