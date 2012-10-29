package com.sap.core.odata.api.processor;

import java.util.Set;

import com.sap.core.odata.api.RuntimeDelegate;
import com.sap.core.odata.api.enums.HttpStatus;

public abstract class ODataResponse {

  protected ODataResponse() {}

  public abstract HttpStatus getStatus();

  public abstract String getEntity();

  public abstract String getHeader(String name);

  public abstract String getIdLiteral();
  
  public abstract String getETag();

  public abstract Set<String> getHeaderNames();

  public static ODataResponseBuilder status(HttpStatus status) {
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

    public abstract ODataResponseBuilder status(HttpStatus status);

    public abstract ODataResponseBuilder entity(String entity);

    public abstract ODataResponseBuilder header(String name, String value);

    public abstract ODataResponseBuilder idLiteral(String idLiteral);

    public abstract ODataResponseBuilder eTag(String eTag);
}

}
