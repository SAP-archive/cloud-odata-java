package org.odata4j.test.integration;

public class ResponseData {

  public ResponseData(int statusCode, String entity) {
    super();
    this.statusCode = statusCode;
    this.entity = entity;
  }

  int statusCode;
  String entity;

  public int getStatusCode() {
    return statusCode;
  }

  public String getEntity() {
    return entity;
  }
}
