package com.sap.core.odata.core.producer;

import java.util.HashMap;
import java.util.Set;

public final class ODataResponse {

  protected ODataResponse() {}

  protected ODataResponse(int status, Object entity) {
    this.status = status;
    this.entity = entity;
  }

  private int status;
  private Object entity;
  private HashMap<String, Object> headers = new HashMap<String, Object>();

  public static ODataResponseBuilder status(int status) {
    return ODataResponseBuilder.newInstance().status(status);
  }

  public static ODataResponseBuilder entity(Object entity) {
    return ODataResponseBuilder.newInstance().entity(entity);
  }
  
  public static ODataResponseBuilder header(String name, String value) {
    return ODataResponseBuilder.newInstance().header(name, value);
  }

  public int getStatus() {
    return this.status;
  }

  public Object getEntity() {
    return entity;
  }

  public Object getHeader(String name) {
    return this.headers.get(name);
  }

  private void addHeaders(HashMap<String, Object> headers) {
    this.headers = headers;
  }

  public static final class ODataResponseBuilder {

    protected ODataResponseBuilder() {}

    public ODataResponseBuilder header(String name, String value) {
      ODataResponseBuilder.this.setHeader(name, value);
      return this;
    }

    private int status = 200;
    private Object entity;
    private HashMap<String, Object> headers = new HashMap<String, Object>();

    public ODataResponseBuilder status(int status) {
      ODataResponseBuilder.this.status = status;
      return this;
    }

    public ODataResponseBuilder entity(Object entity) {
      ODataResponseBuilder.this.entity = entity;
      return this;
    }


    public ODataResponse build() {
      ODataResponse response = new ODataResponse(this.status, this.entity);
      response.addHeaders(this.headers);
      return response;
    }

    private static ODataResponseBuilder newInstance() {
      return new ODataResponseBuilder();
    }

    private ODataResponseBuilder setHeader(String name, Object value) {
      if (value == null) {
        headers.remove(name);
      } else {
        headers.put(name, value.toString());
      }
      return this;
    }

  }

  public Set<String> getHeaderNames() {
    return this.headers.keySet();
  }
}
