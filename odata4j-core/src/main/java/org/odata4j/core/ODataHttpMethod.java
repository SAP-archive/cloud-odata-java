package org.odata4j.core;

/** Common http methods supported by the OData protocol. */
public enum ODataHttpMethod {
  GET, PUT, POST, DELETE, PATCH, MERGE, OPTIONS, HEAD;

  public static ODataHttpMethod fromString(String m) {
    ODataHttpMethod result;

    if ("get".equalsIgnoreCase(m)) {
      result = ODataHttpMethod.GET;
    } else if ("put".equalsIgnoreCase(m)) {
      result = ODataHttpMethod.PUT;
    } else if ("post".equalsIgnoreCase(m)) {
      result = ODataHttpMethod.POST;
    } else if ("delete".equalsIgnoreCase(m)) {
      result = ODataHttpMethod.DELETE;
    } else if ("patch".equalsIgnoreCase(m)) {
      result = ODataHttpMethod.PATCH;
    } else if ("merge".equalsIgnoreCase(m)) {
      result = ODataHttpMethod.MERGE;
    } else if ("options".equalsIgnoreCase(m)) {
      result = ODataHttpMethod.OPTIONS;
    } else if ("head".equalsIgnoreCase(m)) {
      result = ODataHttpMethod.HEAD;
    } else {
      throw new IllegalArgumentException("unsupported OData HTTP method name: " + m);
    }

    return result;
  }

}
