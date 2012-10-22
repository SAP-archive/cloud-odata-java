package com.sap.core.odata.core.rest;

import java.util.HashMap;
import java.util.Set;

import com.sap.core.odata.api.rest.ODataResponse;

public class ODataResponseImpl extends ODataResponse {

  private int status;
  private String entity;
  private HashMap<String, String> header;

  @Override
  public int getStatus() {
    return this.status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public void setEntity(String entity) {
    this.entity = entity;
  }

  @Override
  public String getEntity() {
    return this.entity;
  }

  public void setHeader(HashMap<String, String> header) {
    this.header = header;
  }

  @Override
  public String getHeader(String name) {
    return this.header.get(name);
  }

  @Override
  public Set<String> getHeaderNames() {
    return this.header.keySet();
  }

}
