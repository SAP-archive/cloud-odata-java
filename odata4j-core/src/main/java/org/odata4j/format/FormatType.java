package org.odata4j.format;

import javax.ws.rs.core.MediaType;

public enum FormatType {

  ATOM(MediaType.APPLICATION_XML, MediaType.APPLICATION_ATOM_XML),
  JSON(MediaType.APPLICATION_JSON);

  private FormatType(String... mediaTypes) {
    this.mediaTypes = mediaTypes;
  }

  private final String[] mediaTypes;

  public String[] getAcceptableMediaTypes() {
    return mediaTypes;
  }

  public static FormatType parse(String format) {
    if ("json".equalsIgnoreCase(format))
      return JSON;
    if ("atom".equalsIgnoreCase(format))
      return ATOM;
    throw new UnsupportedOperationException("Unsupported format " + format);
  }
}
