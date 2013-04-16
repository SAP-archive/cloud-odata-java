package com.sap.core.odata.testutil.helper;

import java.net.URI;

import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.methods.HttpRequestBase;

@NotThreadSafe
public class HttpMerge extends HttpRequestBase {

  public final static String METHOD_NAME = "MERGE";

  public HttpMerge() {
    super();
  }

  public HttpMerge(final URI uri) {
    super();
    setURI(uri);
  }

  /**
   * @throws IllegalArgumentException if the uri is invalid.
   */
  public HttpMerge(final String uri) {
    super();
    setURI(URI.create(uri));
  }

  @Override
  public String getMethod() {
    return METHOD_NAME;
  }

}
