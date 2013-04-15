/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.testutil.helper;

import java.net.URI;

import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.methods.HttpRequestBase;

@NotThreadSafe
public class HttpSomethingUnsupported extends HttpRequestBase {

  public final static String METHOD_NAME = "SOMETHING_UNSUPPORTED";

  public HttpSomethingUnsupported() {
    super();
  }

  public HttpSomethingUnsupported(final URI uri) {
    super();
    setURI(uri);
  }

  /**
   * @throws IllegalArgumentException if the uri is invalid.
   */
  public HttpSomethingUnsupported(final String uri) {
    super();
    setURI(URI.create(uri));
  }

  @Override
  public String getMethod() {
    return METHOD_NAME;
  }

}
