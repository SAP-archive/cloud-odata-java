package com.sap.core.odata.api.enums;

public enum MediaType {
  WILDCARD("*/*"),
  APPLICATION_XML("application/xml"),
  APPLICATION_ATOM_XML_ENTRY("application/atom+xml;type=entry"),
  APPLICATION_ATOM_XML_FEED("application/atom+xml;type=feed"),
  APPLICATION_ATOM_SVC("application/atomsvc+xml"),
  APPLICATION_JSON("application/json"),
  APPLICATION_OCTET_STREAM("application/octet-stream"),
  TEXT_PLAIN("text/plain");

  private String value;

  MediaType(String value) {
    this.value = value;
  }

  public String toString() {
    return this.value;
  }

}
