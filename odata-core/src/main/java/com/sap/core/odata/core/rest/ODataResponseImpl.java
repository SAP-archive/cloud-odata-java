package com.sap.core.odata.core.rest;

import com.sap.core.odata.api.rest.ODataResponse;

public class ODataResponseImpl extends ODataResponse {

  private int status;

  @Override
  public int getStatus() {
    return this.status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

}
