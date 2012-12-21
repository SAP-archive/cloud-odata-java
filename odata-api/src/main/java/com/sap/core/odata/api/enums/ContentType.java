package com.sap.core.odata.api.enums;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * 
 */
public class ContentType {



  public enum ODataFormat {
    ATOM, XML, JSON, CUSTOM
  }

  private String type;
  private String subtype;
  private Map<String, String> parameters;
  private ODataFormat odataFormat;

  private static final String PARAMETER_SEPARATOR = ";";
  private static final String TYPE_SUBTYPE_SEPARATOR = "/";
  private static final String MEDIA_TYPE_WILDCARD = "*";

  public static final String PARAMETER_CHARSET = "charset";
  public static final String PARAMETER_Q = "q";

  public final static ContentType WILDCARD = new ContentType(MEDIA_TYPE_WILDCARD, MEDIA_TYPE_WILDCARD);

  public final static ContentType APPLICATION_XML = new ContentType("application", "xml", ODataFormat.XML);
  public static final ContentType APPLICATION_ATOM_XML = new ContentType("application", "atom+xml", ODataFormat.ATOM);
  public final static ContentType APPLICATION_ATOM_XML_ENTRY = new ContentType("application", "atom+xml", ODataFormat.ATOM, parameterMap("type", "entry"));
  public final static ContentType APPLICATION_ATOM_XML_FEED = new ContentType("application", "atom+xml", ODataFormat.ATOM, parameterMap("type", "feed"));
  public final static ContentType APPLICATION_ATOM_SVC = new ContentType("application", "atomsvc+xml", ODataFormat.ATOM);
  public final static ContentType APPLICATION_JSON = new ContentType("application", "json", ODataFormat.JSON);
  public final static ContentType APPLICATION_OCTET_STREAM = new ContentType("application", "octet-stream");
  public final static ContentType TEXT_PLAIN = new ContentType("text", "plain");
  public static final ContentType MULTIPART_MIXED = new ContentType("multipart", "mixed");

  private static Map<String, String> parameterMap(String ... content) {
    Map<String, String> map = new HashMap<String, String>();
    for (int i = 0; i < content.length-1; i+=2) {
      String key = content[i];
      String value = content[i+1];
      map.put(key, value);
    }
    return map;
  }

  private ContentType(String type, String subtype) {
    this(type, subtype, ODataFormat.CUSTOM, null);
  }

  private ContentType(String type, String subtype, Map<String, String> parameters) {
    this(type, subtype, mapToODataFormat(subtype), parameters);
  }

  private ContentType(String type, String subtype, ODataFormat odataFormat) {
    this(type, subtype, odataFormat, null);
  }
  
  private ContentType(String type, String subtype, ODataFormat odataFormat, Map<String, String> parameters) {
    this.odataFormat = odataFormat;
    this.type = type == null ? MEDIA_TYPE_WILDCARD : type;
    this.subtype = subtype == null ? MEDIA_TYPE_WILDCARD : subtype;

    if(parameters == null) {
      this.parameters = Collections.emptyMap();
    } else {
      this.parameters = new TreeMap<String, String>(new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
          return o1.compareToIgnoreCase(o2);
        }
      });
      this.parameters.putAll(parameters);
      this.parameters.remove(PARAMETER_Q);
    }
  }

  private static ODataFormat mapToODataFormat(String subtype) {
    ODataFormat odFormat = null;
    if (subtype.contains("atom")) {
      odFormat = ODataFormat.ATOM;
    } else if (subtype.contains("xml")) {
      odFormat = ODataFormat.XML;
    } else if (subtype.contains("json")) {
      odFormat = ODataFormat.JSON;
    } else {
      odFormat = ODataFormat.CUSTOM;
    }
    return odFormat;
  }
  
  
  public static ContentType create(String type, String subtype) {
    return new ContentType(type, subtype, mapToODataFormat(subtype), null);
  }
  
  public static ContentType create(String type, String subtype, Map<String, String> parameters) {
    ODataFormat odFormat = mapToODataFormat(subtype);
    return new ContentType(type, subtype, odFormat, parameters);
  }

  public static ContentType create(ContentType contentType, String parameterKey, String parameterValue) {
    ContentType ct = new ContentType(contentType.type, contentType.subtype, contentType.odataFormat, contentType.parameters);
    ct.parameters.put(parameterKey, parameterValue);
    return ct;
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
    if (types.contains(TYPE_SUBTYPE_SEPARATOR)) {
      String[] tokens = types.split(TYPE_SUBTYPE_SEPARATOR);
      if (tokens.length == 2) {
        return create(tokens[0], tokens[1], parametersMap);
      } else {
        throw new IllegalArgumentException("Too many '/' in format '" + format + "'.");
      }
    } else {
      return create(types, MEDIA_TYPE_WILDCARD, parametersMap);
    }
  }

  /**
   * Valid input are <code>;</code> separated <code>key = value</code> pairs.
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
        if(isParameterAllowed(key)) {
          String value = (keyValue.length > 1 ? keyValue[1] : null);
          parameters.put(key, value);
        }
      }
    }
    return parameters;
  }

  private static boolean isParameterAllowed(String key) {
    if(key == null) {
      return false;
    } else if(PARAMETER_Q.equals(key.toLowerCase(Locale.US))) {
      return false;
    }
    return true;
  }

  public String getType() {
    return type;
  }

  public String getSubtype() {
    return subtype;
  }

  public Map<String, String> getParameters() {
    return Collections.unmodifiableMap(parameters);
  }

  @Override
  public int hashCode() {
    // final int prime = 31;
    // int result = 1;
    //
    // Set<Entry<String, String>> entries = parameters.entrySet();
    // for (Entry<String, String> entry : entries) {
    // String key = entry.getKey();
    // String value = entry.getValue();
    // result = prime * result + ((key == null) ? 0 : key.hashCode());
    // result = prime * result + ((value == null) ? 0 : value.hashCode());
    // }
    //
    // result = prime * result + ((subtype == null) ? 0 : subtype.hashCode());
    // result = prime * result + ((type == null) ? 0 : type.hashCode());
    // return result;
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
    
    // subtype checks
    if (subtype == null) {
      if (other.subtype != null) {
        return false;
      }
    } else if (!subtype.equals(other.subtype)) {
      if (!subtype.equals(MEDIA_TYPE_WILDCARD) && !other.subtype.equals(MEDIA_TYPE_WILDCARD)) {
        return false;
      }
    }
    
    // type checks
    if (type == null) {
      if (other.type != null) {
        return false;
      }
    } else if (!type.equals(other.type)) {
      if (!type.equals(MEDIA_TYPE_WILDCARD) && !other.type.equals(MEDIA_TYPE_WILDCARD)) {
        return false;
      }
    }

    //
    if(countWildcars() > 0 || other.countWildcars() > 0) {
      return true;
    }
    
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
    } else {
      return false;
    }
    
    // all tests passed
    return true;
  }

  /**
   * Get {@link ContentType} as string as defined in RFC 2616 (http://www.ietf.org/rfc/rfc2616.txt - chapter 14.17: Content-Type)
   * 
   * @return
   */
  public String toContentTypeString() {
    StringBuilder sb = new StringBuilder();
    sb.append(type).append(TYPE_SUBTYPE_SEPARATOR).append(subtype);
    for (String key : parameters.keySet()) {
      if(isParameterAllowed(key)) {
        String value = parameters.get(key);
        sb.append("; ").append(key).append("=").append(value);
      }
    }
    return sb.toString();
  }
  
  @Override
  public String toString() {
    return toContentTypeString();
  }

  public ODataFormat getODataFormat() {
    return odataFormat;
  }

  /**
   * Find best match between this {@link ContentType} and the {@link ContentType} in the list.
   * If a match (this {@link ContentType} is equal to a {@link ContentType} in list) is found either this or the {@link ContentType}
   * from the list is returned based on which {@link ContentType} has less {@value #WILDCARD} characters set 
   * (checked with {@link #compareWildcardCounts(ContentType)}.
   * If no match (none {@link ContentType} in list is equal to this {@link ContentType}) is found <code>NULL</code> is returned.
   * 
   * @param toMatchContentTypes
   * @return
   */
  public ContentType match(List<ContentType> toMatchContentTypes) {
    for (ContentType supportedContentType : toMatchContentTypes) {
      if(equals(supportedContentType)) {
        if(compareWildcardCounts(supportedContentType) < 0) {
          return this;
        } else {
          return supportedContentType;
        }
      }
    }
    return null;
  }

  /**
   * Compare wildcards counts/weights of both {@link ContentType}.
   * 
   * The smaller {@link ContentType} has lesser weighted wildcards then the bigger {@link ContentType}.
   * As result this method returns this object weighted wildcards minus the given parameter object weighted wildcards.
   * 
   * A type wildcard is weighted with <code>2</code> and a subtype wildcard is weighted with <code>1</code>.
   * 
   * @param otherContentType {@link ContentType} to be compared to
   * @return this object weighted wildcards minus the given parameter object weighted wildcards.
   */
  public int compareWildcardCounts(ContentType otherContentType) {
    return countWildcars() - otherContentType.countWildcars();
  }

  private int countWildcars() {
    int count = 0;
    if(MEDIA_TYPE_WILDCARD.equals(type)) {
      count += 2;
    }
    if(MEDIA_TYPE_WILDCARD.equals(subtype)) {
      count++;
    }
    return count;
  }
  
  public boolean isWildcard() {
    return (MEDIA_TYPE_WILDCARD.equals(type) && MEDIA_TYPE_WILDCARD.equals(subtype));
  }
}
