package com.sap.core.odata.api.rest;


public abstract class ODataResponse {

  protected ODataResponse() {}

  public abstract int getStatus();

  public abstract String getEntity();

  public abstract String getHeader(String name);

  
  public static ODataResponseBuilder status(int status) {
    ODataResponseBuilder b = ODataResponseBuilder.newInstance();
    b.status(status);
    return b;
  }

  public static ODataResponseBuilder entity(String entity) {
    ODataResponseBuilder b = ODataResponseBuilder.newInstance();
    b.entity(entity);
    return b;
  }

  public static ODataResponseBuilder header(String name, String value) {
    ODataResponseBuilder b = ODataResponseBuilder.newInstance();
    b.header(name, value);
    return b;
  }

  public static abstract class ODataResponseBuilder {

    protected ODataResponseBuilder() {

    }

    public static ODataResponseBuilder newInstance() {
      ODataResponseBuilder b = RuntimeDelegate.getInstance().createODataResponseBuilder();
      return b;
    }

    public abstract ODataResponse build();

    public abstract ODataResponseBuilder status(int status);

    public abstract ODataResponseBuilder entity(String entity);

    public abstract ODataResponseBuilder header(String name, String value);
    
  }

}
