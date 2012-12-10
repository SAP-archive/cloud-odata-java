package com.sap.core.odata.core;

import java.util.HashMap;

import com.sap.core.odata.api.enums.HttpHeaders;
import com.sap.core.odata.api.enums.HttpStatusCodes;
import com.sap.core.odata.api.ep.ODataEntityContent;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataResponse.ODataResponseBuilder;

public class ODataResponseBuilderImpl extends ODataResponseBuilder {

  private HttpStatusCodes status = HttpStatusCodes.OK;
  private Object entity;
  private HashMap<String, String> header = new HashMap<String, String>();
  private String idLiteral;
  private String eTag;

  @Override
  public ODataResponse build() {
    ODataResponseImpl response = new ODataResponseImpl();

    if (entity instanceof ODataEntityContent) {
      ODataEntityContent content = (ODataEntityContent) entity;

      response.setEntity(content.getContent());
      header.put(HttpHeaders.CONTENT_TYPE, content.getContentHeader());
      response.setHeader(this.header);

      if (content.getETag() != null) {
        response.setETag(content.getETag());
      }
    } else {
      response.setEntity(this.entity);
      response.setHeader(this.header);
      response.setETag(this.eTag);
    }

    response.setStatus(this.status);
    response.setIdLiteral(this.idLiteral);

    return response;
  }

  @Override
  public ODataResponseBuilder status(HttpStatusCodes status) {
    this.status = status;
    return this;
  }

  @Override
  public ODataResponseBuilder entity(Object entity) {
    this.entity = entity;
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

  @Override
  public ODataResponseBuilder idLiteral(String idLiteral) {
    this.idLiteral = idLiteral;
    return this;
  }

  @Override
  public ODataResponseBuilder eTag(String eTag) {
    this.eTag = eTag;
    return this;
  }

}
