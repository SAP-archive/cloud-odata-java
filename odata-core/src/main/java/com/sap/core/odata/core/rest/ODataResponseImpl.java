package com.sap.core.odata.core.rest;

import java.util.HashMap;
import java.util.Set;

import com.sap.core.odata.api.enums.HttpStatus;
import com.sap.core.odata.api.processor.ODataResponse;

public class ODataResponseImpl extends ODataResponse {

  private HttpStatus status;
  private String entity;
  private HashMap<String, String> header;
  private String idLiteral;
  private String eTag;
  
  @Override
  public HttpStatus getStatus() {
    return this.status;
  }

  public void setStatus(HttpStatus status) {
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

  @Override
  public String getIdLiteral() {
    return this.idLiteral;
  }

  @Override
  public String getETag() {
    return this.eTag;
  }

  public void setIdLiteral(String idLiteral) {
    this.idLiteral = idLiteral;
  }

  public void setETag(String eTag) {
    this.eTag = eTag;
  }

}
