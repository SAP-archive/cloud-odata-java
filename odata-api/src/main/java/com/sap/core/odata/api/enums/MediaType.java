package com.sap.core.odata.api.enums;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class MediaType {

  private String type;
  private String subtype;
  private Map<String, String> parameters;

  public static final String CHARSET_PARAMETER = "charset";
  public static final String MEDIA_TYPE_WILDCARD = "*";
  
  public final static MediaType WILDCARD = new MediaType(MEDIA_TYPE_WILDCARD, MEDIA_TYPE_WILDCARD);

  public final static MediaType APPLICATION_XML = new MediaType("application", "xml");
  public final static MediaType APPLICATION_ATOM_XML_ENTRY = new MediaType("application","atom+xml").addParameter("type", "entry");
  public final static MediaType APPLICATION_ATOM_XML_FEED = new MediaType("application","atom+xml").addParameter("type", "feed");
  public final static MediaType APPLICATION_ATOM_SVC = new MediaType("application","atomsvc+xml");
  public final static MediaType APPLICATION_JSON = new MediaType("application","json");
  public final static MediaType APPLICATION_OCTET_STREAM = new MediaType("application","octet-stream");
  public final static MediaType TEXT_PLAIN = new MediaType("text","plain");


  private MediaType(String type, String subtype) {

    this.type = type == null ? MEDIA_TYPE_WILDCARD : type;
    this.subtype = subtype == null ? MEDIA_TYPE_WILDCARD : subtype;

    parameters = new TreeMap<String, String>(new Comparator<String>() {

      @Override
      public int compare(String o1, String o2) {
        return o1.compareToIgnoreCase(o2);
      }
    });
  }

  public static MediaType create(String type, String subtype) {
    return new MediaType(type, subtype);
  }

  public String getType() {
    return type;
  }

  public String getSubtype() {
    return subtype;
  }

  public MediaType addParameter(String key, String value) {
    parameters.put(key, value);
    return this;
  }

  public Map<String, String> getParameters() {
    return Collections.unmodifiableMap(parameters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(type).append("/").append(subtype);
    for (String key : parameters.keySet()) {
      String value = parameters.get(key);
      sb.append(";").append(key).append("=").append(value);
    }
    return sb.toString();
  }
}
