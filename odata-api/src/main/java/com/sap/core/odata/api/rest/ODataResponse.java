package com.sap.core.odata.api.rest;

public abstract class ODataResponse {

  protected ODataResponse() {}

  public abstract int getStatus();

  public static ODataResponseBuilder status(int status) {
    ODataResponseBuilder b = ODataResponseBuilder.newInstance();
    b.status(status);
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

  }

}
