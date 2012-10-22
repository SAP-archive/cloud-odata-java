package com.sap.core.odata.core.rest;

import com.sap.core.odata.api.rest.ODataResponse;
import com.sap.core.odata.api.rest.ODataResponse.ODataResponseBuilder;

public class ODataResponseBuilderImpl extends ODataResponseBuilder {

  private int status;

  @Override
  public ODataResponse build() {
    ODataResponseImpl response = new ODataResponseImpl();

    response.setStatus(this.status);
    
    return response;
  }

  @Override
  public ODataResponseBuilder status(int status) {
    this.status = status;
    return this;
  }

}
