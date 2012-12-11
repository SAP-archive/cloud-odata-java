package com.sap.core.odata.core;

import java.util.HashMap;
import java.util.Set;

import com.sap.core.odata.api.enums.HttpHeaders;
import com.sap.core.odata.api.enums.HttpStatusCodes;
import com.sap.core.odata.api.ep.ODataEntityContent;
import com.sap.core.odata.api.processor.ODataResponse;

public class ODataResponseImpl extends ODataResponse {

  private HttpStatusCodes status;
  private Object entity;
  private HashMap<String, String> header;
  private String idLiteral;
  private String eTag;

  @Override
  public HttpStatusCodes getStatus() {
    return this.status;
  }

  @Override
  public Object getEntity() {
    return this.entity;
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

  public class ODataResponseBuilderImpl extends ODataResponseBuilder {
    private HttpStatusCodes status = HttpStatusCodes.OK;
    private Object entity;
    private HashMap<String, String> header = new HashMap<String, String>();
    private String idLiteral;
    private String eTag;

    @Override
    public ODataResponse build() {
      if (entity instanceof ODataEntityContent) {
        ODataEntityContent content = (ODataEntityContent) entity;

        ODataResponseImpl.this.entity = content.getContent();
        header.put(HttpHeaders.CONTENT_TYPE, content.getContentHeader());
        ODataResponseImpl.this.header = this.header;

        if (content.getETag() != null) {
          ODataResponseImpl.this.eTag = content.getETag();
        }
      } else {
        ODataResponseImpl.this.entity = this.entity;
        ODataResponseImpl.this.header = this.header;
        ODataResponseImpl.this.eTag = this.eTag;
      }

      ODataResponseImpl.this.status = this.status;
      ODataResponseImpl.this.idLiteral = this.idLiteral;

      return ODataResponseImpl.this;
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

}
