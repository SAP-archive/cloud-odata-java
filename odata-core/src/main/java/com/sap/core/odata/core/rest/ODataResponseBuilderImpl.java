package com.sap.core.odata.core.rest;

import java.util.HashMap;

import com.sap.core.odata.api.enums.HttpStatus;
import com.sap.core.odata.api.rest.ODataResponse;
import com.sap.core.odata.api.rest.ODataResponse.ODataResponseBuilder;

public class ODataResponseBuilderImpl extends ODataResponseBuilder {

  private HttpStatus status = HttpStatus.OK;
  private String entity;
  private HashMap<String, String> header = new HashMap<String, String>();
  
  
  @Override
  public ODataResponse build() {
    ODataResponseImpl response = new ODataResponseImpl();

    response.setStatus(this.status);
    response.setEntity(this.entity);
    response.setHeader(this.header);
    
    return response;
  }

  @Override
  public ODataResponseBuilder status(HttpStatus status) {
    this.status = status;
    return this;
  }

  @Override
  public ODataResponseBuilder entity(String entity) {
    this.entity= entity;
    return this;
  }

  @Override
  public ODataResponseBuilder header(String name, String value) {
    if (value == null) {
      this.header.remove(name);
    } else {
      this.header.put(name, value);
    }
    
    return this;
  }

}
