package com.sap.core.odata.api.enums;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * 
 */
public class ContentType {

  private String type;
  private String subtype;
  private Map<String, String> parameters;
  private ODataFormat odataFormat;

  private static final String PARAMETER_SEPARATOR = ";";
  private static final String MEDIA_TYPE_WILDCARD = "*";

  public final static ContentType WILDCARD = new ContentType(MEDIA_TYPE_WILDCARD, MEDIA_TYPE_WILDCARD);

  public final static ContentType APPLICATION_XML = new ContentType("application", "xml", ODataFormat.XML);
  public static final ContentType APPLICATION_ATOM_XML = new ContentType("application", "atom+xml", ODataFormat.ATOM);
  public final static ContentType APPLICATION_ATOM_XML_ENTRY = new ContentType("application", "atom+xml", ODataFormat.ATOM).addParameter("type", "entry");
  public final static ContentType APPLICATION_ATOM_XML_FEED = new ContentType("application", "atom+xml", ODataFormat.ATOM).addParameter("type", "feed");
  public final static ContentType APPLICATION_ATOM_SVC = new ContentType("application", "atomsvc+xml", ODataFormat.ATOM);
  public final static ContentType APPLICATION_JSON = new ContentType("application", "json", ODataFormat.JSON);
  public final static ContentType APPLICATION_OCTET_STREAM = new ContentType("application", "octet-stream");
  public final static ContentType TEXT_PLAIN = new ContentType("text", "plain");
  public static final ContentType MULTIPART_MIXED = new ContentType("multipart", "mixed");

  private ContentType(String type, String subtype) {
    this(type, subtype, ODataFormat.CUSTOM);
  }

  private ContentType(String type, String subtype, ODataFormat odataFormat) {

    this.odataFormat = odataFormat;
    this.type = type == null ? MEDIA_TYPE_WILDCARD : type;
    this.subtype = subtype == null ? MEDIA_TYPE_WILDCARD : subtype;

    parameters = new TreeMap<String, String>(new Comparator<String>() {

      @Override
      public int compare(String o1, String o2) {
        return o1.compareToIgnoreCase(o2);
      }
    });
  }

  public static ContentType create(String type, String subtype) {
    return new ContentType(type, subtype);
  }

  /**
   * Create a {@link ContentType} based on given input string (<code>format</code>).
   * 
   * Supported format is <code>HTTP Accept HEADER</code> format as defined in <code>RFC 2616 chapter 14.1</code>
   * 
   * @param format
   * @return
   */
  public static ContentType create(String format) {
    // split 'types' and 'parameters'
    String[] typesAndParameters = format.split(PARAMETER_SEPARATOR, 2);
    String types = typesAndParameters[0];
    String parameters = (typesAndParameters.length > 1 ? typesAndParameters[1] : null);
    //
    Map<String, String> parametersMap = splitParameters(parameters);
    //
    if (types.contains("/")) {
      String[] tokens = types.split("/");
      if (tokens.length == 2) {
        return new ContentType(tokens[0], tokens[1]).addParameters(parametersMap);
      } else {
        throw new IllegalArgumentException("Too many '/' in format '" + format + "'.");
      }
    } else {
      return new ContentType(types, "*").addParameters(parametersMap);
    }
  }

  /**
   * Valid input <code>;</code> separated <code>key = value</code> pairs.
   * 
   * @param format
   * @return
   */
  private static Map<String, String> splitParameters(String format) {
    Map<String, String> parameters = new HashMap<String, String>();
    if (format != null) {
      String[] formatParmeters = format.split(PARAMETER_SEPARATOR);
      for (String parameter : formatParmeters) {
        String[] keyValue = parameter.split("=");
        String key = keyValue[0];
        String value = (keyValue.length > 1 ? keyValue[1] : null);
        parameters.put(key, value);
      }
    }
    return parameters;
  }

  public String getType() {
    return type;
  }

  public String getSubtype() {
    return subtype;
  }

  public ContentType addParameter(String key, String value) {
    parameters.put(key, value);
    return this;
  }

  public Map<String, String> getParameters() {
    return Collections.unmodifiableMap(parameters);
  }

  @Override
  public int hashCode() {
//    final int prime = 31;
//    int result = 1;
//
//    Set<Entry<String, String>> entries = parameters.entrySet();
//    for (Entry<String, String> entry : entries) {
//      String key = entry.getKey();
//      String value = entry.getValue();
//      result = prime * result + ((key == null) ? 0 : key.hashCode());
//      result = prime * result + ((value == null) ? 0 : value.hashCode());
//    }
//
//    result = prime * result + ((subtype == null) ? 0 : subtype.hashCode());
//    result = prime * result + ((type == null) ? 0 : type.hashCode());
//    return result;
    return 1;
  }

  @Override
  public boolean equals(Object obj) {
    // basic checks
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;

    ContentType other = (ContentType) obj;
    // parameter checks
    if (parameters == null) {
      if (other.parameters != null) {
        return false;
      }
    } else if (parameters.size() == other.parameters.size()) {
      Iterator<Entry<String, String>> entries = parameters.entrySet().iterator();
      Iterator<Entry<String, String>> otherEntries = other.parameters.entrySet().iterator();
      while (entries.hasNext()) {
        Entry<String, String> e = entries.next();
        Entry<String, String> oe = otherEntries.next();

        if (!e.equals(oe)) {
          return false;
        }
      }
    }

    // subtype checks
    if (subtype == null) {
      if (other.subtype != null) {
        return false;
      }
    } else if (!subtype.equals(other.subtype)) {
      if (subtype.equals(MEDIA_TYPE_WILDCARD) || other.subtype.equals(MEDIA_TYPE_WILDCARD)) {
        return true;
      }
      return false;
    }

    // type checks
    if (type == null) {
      if (other.type != null) {
        return false;
      }
    } else if (!type.equals(other.type)) {
      if (type.equals(MEDIA_TYPE_WILDCARD) || other.type.equals(MEDIA_TYPE_WILDCARD)) {
        return true;
      }
      return false;
    }
    return true;
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

  public ContentType addParameters(Map<String, String> parameters) {
    this.parameters.putAll(parameters);
    return this;
  }

  public ODataFormat getODataFormat() {
    return odataFormat;
  }

  public enum ODataFormat {
    ATOM, XML, JSON, CUSTOM
  }
}
