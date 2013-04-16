/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.core.commons;

import java.util.ArrayList;
import java.util.Arrays;
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
 * Internally used {@link ContentType} for OData library.
 * For more details on format and content of a {@link ContentType} see    
 * <code>Media Type</code> format as defined in <code>RFC 2616 chapter 3.7</code>.
 * 
 * Once created a {@link ContentType} is IMMUTABLE.
 * 
 * @author SAP AG
 */
public class ContentType {

  public enum ODataFormat {
    ATOM, XML, JSON, CUSTOM
  }

  private static final String PARAMETER_SEPARATOR = ";";
  private static final String TYPE_SUBTYPE_SEPARATOR = "/";
  private static final String MEDIA_TYPE_WILDCARD = "*";

  public static final String PARAMETER_CHARSET = "charset";
  public static final String PARAMETER_Q = "q";
  public static final String CHARSET_UTF_8 = "utf-8";

  public static final ContentType WILDCARD = new ContentType(MEDIA_TYPE_WILDCARD, MEDIA_TYPE_WILDCARD);

  public static final ContentType APPLICATION_XML = new ContentType("application", "xml", ODataFormat.XML);
  public static final ContentType APPLICATION_XML_CS_UTF_8 = ContentType.create(APPLICATION_XML, PARAMETER_CHARSET, CHARSET_UTF_8);
  public static final ContentType APPLICATION_ATOM_XML = new ContentType("application", "atom+xml", ODataFormat.ATOM);
  public static final ContentType APPLICATION_ATOM_XML_CS_UTF_8 = ContentType.create(APPLICATION_ATOM_XML, PARAMETER_CHARSET, CHARSET_UTF_8);
  public static final ContentType APPLICATION_ATOM_XML_ENTRY = new ContentType("application", "atom+xml", ODataFormat.ATOM, parameterMap("type", "entry"));
  public static final ContentType APPLICATION_ATOM_XML_ENTRY_CS_UTF_8 = ContentType.create(APPLICATION_ATOM_XML_ENTRY, PARAMETER_CHARSET, CHARSET_UTF_8);
  public static final ContentType APPLICATION_ATOM_XML_FEED = new ContentType("application", "atom+xml", ODataFormat.ATOM, parameterMap("type", "feed"));
  public static final ContentType APPLICATION_ATOM_XML_FEED_CS_UTF_8 = ContentType.create(APPLICATION_ATOM_XML_FEED, PARAMETER_CHARSET, CHARSET_UTF_8);
  public static final ContentType APPLICATION_ATOM_SVC = new ContentType("application", "atomsvc+xml", ODataFormat.ATOM);
  public static final ContentType APPLICATION_ATOM_SVC_CS_UTF_8 = ContentType.create(APPLICATION_ATOM_SVC, PARAMETER_CHARSET, CHARSET_UTF_8);
  public static final ContentType APPLICATION_JSON = new ContentType("application", "json", ODataFormat.JSON);
  public static final ContentType APPLICATION_JSON_CS_UTF_8 = ContentType.create(APPLICATION_JSON, PARAMETER_CHARSET, CHARSET_UTF_8);
  public static final ContentType APPLICATION_OCTET_STREAM = new ContentType("application", "octet-stream");
  public static final ContentType TEXT_PLAIN = new ContentType("text", "plain");
  public static final ContentType TEXT_PLAIN_CS_UTF_8 = ContentType.create(TEXT_PLAIN, PARAMETER_CHARSET, CHARSET_UTF_8);
  public static final ContentType MULTIPART_MIXED = new ContentType("multipart", "mixed");

  private String type;
  private String subtype;
  private Map<String, String> parameters;
  private ODataFormat odataFormat;

  private ContentType(final String type, final String subtype) {
    this(type, subtype, ODataFormat.CUSTOM, null);
  }

  private ContentType(final String type, final String subtype, final Map<String, String> parameters) {
    this(type, subtype, mapToODataFormat(subtype), parameters);
  }

  private ContentType(final String type, final String subtype, final ODataFormat odataFormat) {
    this(type, subtype, odataFormat, null);
  }

  private ContentType(final String type, final String subtype, final ODataFormat odataFormat, final Map<String, String> parameters) {
    if ((type == null || MEDIA_TYPE_WILDCARD.equals(type)) && !MEDIA_TYPE_WILDCARD.equals(subtype)) {
      throw new IllegalArgumentException("Illegal combination of WILDCARD type with NONE WILDCARD subtype.");
    }
    this.odataFormat = odataFormat;
    this.type = type == null ? MEDIA_TYPE_WILDCARD : type;
    this.subtype = subtype == null ? MEDIA_TYPE_WILDCARD : subtype;

    if (parameters == null) {
      this.parameters = Collections.emptyMap();
    } else {
      this.parameters = new TreeMap<String, String>(new Comparator<String>() {
        @Override
        public int compare(final String o1, final String o2) {
          return o1.compareToIgnoreCase(o2);
        }
      });
      this.parameters.putAll(parameters);
      this.parameters.remove(PARAMETER_Q);
    }
  }

  /**
   * Validates if given <code>format</code> is parseable and can be used as input for {@link #create(String)} method.
   * 
   * @param format to be validated string
   * @return <code>true</code> if format is parseable otherwise <code>false</code>
   */
  public static boolean isParseable(final String format) {
    try {
      ContentType ct = ContentType.create(format);
      return ct != null;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Creates a content type from type and subtype
   * @param type
   * @param subtype
   * @return a new <code>ContentType</code> object
   */
  public static ContentType create(final String type, final String subtype) {
    return new ContentType(type, subtype, mapToODataFormat(subtype), null);
  }

  /**
   * 
   * @param type
   * @param subtype
   * @param parameters
   * @return a new <code>ContentType</code> object
   */
  public static ContentType create(final String type, final String subtype, final Map<String, String> parameters) {
    return new ContentType(type, subtype, mapToODataFormat(subtype), parameters);
  }

  /**
   * 
   * @param contentType
   * @param parameterKey
   * @param parameterValue
   * @return a new <code>ContentType</code> object
   */
  public static ContentType create(final ContentType contentType, final String parameterKey, final String parameterValue) {
    ContentType ct = new ContentType(contentType.type, contentType.subtype, contentType.odataFormat, contentType.parameters);
    ct.parameters.put(parameterKey, parameterValue);
    return ct;
  }

  /**
   * Create a {@link ContentType} based on given input string (<code>format</code>).
   * 
   * Supported format is <code>Media Type</code> format as defined in <code>RFC 2616 chapter 3.7</code>.
   * This format is used as
   * <code>HTTP Accept HEADER</code> format as defined in <code>RFC 2616 chapter 14.1</code>
   * and 
   * <code>HTTP Content-Type HEADER</code> format as defined in <code>RFC 2616 chapter 14.17</code>
   * 
   * @param format a string in format as defined in <code>RFC 2616 section 3.7</code>
   * @return a new <code>ContentType</code> object
   * @throws IllegalArgumentException if input string is not parseable
   */
  public static ContentType create(String format) {
    if (format == null) {
      throw new IllegalArgumentException("Parameter format MUST NOT be NULL.");
    }

    // split 'types' and 'parameters'
    String[] typesAndParameters = format.split(PARAMETER_SEPARATOR, 2);
    String types = typesAndParameters[0];
    String parameters = (typesAndParameters.length > 1 ? typesAndParameters[1] : null);
    //
    Map<String, String> parametersMap = parseParameters(parameters);
    //
    if (types.contains(TYPE_SUBTYPE_SEPARATOR)) {
      String[] tokens = types.split(TYPE_SUBTYPE_SEPARATOR);
      if (tokens.length == 2) {
        return create(tokens[0], tokens[1], parametersMap);
      } else {
        throw new IllegalArgumentException("Too many '" + TYPE_SUBTYPE_SEPARATOR + "' in format '" + format + "'.");
      }
    } else {
      return create(types, MEDIA_TYPE_WILDCARD, parametersMap);
    }
  }

  /**
   * Parse given input string (<code>format</code>) and return created {@link ContentType} if input was valid 
   * or return <code>NULL</code> if input was not parseable.
   * 
   * For definition of the supported format see {@link #create(String)}
   * 
   * @param format a string in format as defined in <code>RFC 2616 section 3.7</code>
   * @return a new <code>ContentType</code> object
   */
  public static ContentType parse(final String format) {
    try {
      return ContentType.create(format);
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 
   * @param subtype
   * @return
   */
  private static ODataFormat mapToODataFormat(final String subtype) {
    ODataFormat odataFormat = null;
    if (subtype.contains("atom")) {
      odataFormat = ODataFormat.ATOM;
    } else if (subtype.contains("xml")) {
      odataFormat = ODataFormat.XML;
    } else if (subtype.contains("json")) {
      odataFormat = ODataFormat.JSON;
    } else {
      odataFormat = ODataFormat.CUSTOM;
    }
    return odataFormat;
  }

  /**
   * 
   * @param content
   * @return a new <code>ContentType</code> object
   */
  private static Map<String, String> parameterMap(final String... content) {
    Map<String, String> map = new HashMap<String, String>();
    for (int i = 0; i < content.length - 1; i += 2) {
      String key = content[i];
      String value = content[i + 1];
      map.put(key, value);
    }
    return map;
  }

  /**
   * Valid input are <code>;</code> separated <code>key = value</code> pairs.
   * 
   * @param parameters
   * @return Map with keys mapped to values
   */
  private static Map<String, String> parseParameters(final String parameters) {
    Map<String, String> parameterMap = new HashMap<String, String>();
    if (parameters != null) {
      String[] splittedParameters = parameters.split(PARAMETER_SEPARATOR);
      for (String parameter : splittedParameters) {
        String[] keyValue = parameter.split("=");
        String key = keyValue[0].trim().toLowerCase(Locale.ENGLISH);
        if (isParameterAllowed(key)) {
          String value = ((keyValue != null && keyValue.length > 1) ? keyValue[1].trim() : null);
          parameterMap.put(key, value);
        }
      }
    }
    return parameterMap;
  }

  private static boolean isParameterAllowed(String key) {
    if (key == null) {
      return false;
    } else if (PARAMETER_Q.equals(key.toLowerCase(Locale.US))) {
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

  /**
   * 
   * @return parameters of this {@link ContentType} as unmodifiable map.
   */
  public Map<String, String> getParameters() {
    return Collections.unmodifiableMap(parameters);
  }

  @Override
  public int hashCode() {
    return 1;
  }

  /**
   * {@link ContentType}s are equal 
   * <ul>
   * <li>if <code>type</code>, <code>subtype</code> and all <code>parameters</code> have the same value.</li>
   * <li>if <code>type</code> and/or <code>subtype</code> is set to "*" (in such a case the <code>parameters</code> are ignored).</li>
   * </ul>
   * 
   * @return <code>true</code> if both instances are equal (see definition above), otherwise <code>false</code>.
   */
  @Override
  public boolean equals(final Object obj) {
    // basic checks
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }

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

    // if wildcards are set, content types are defined as 'equal'
    if (countWildcards() > 0 || other.countWildcards() > 0) {
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

        if (!areEqual(e.getKey(), oe.getKey())) {
          return false;
        }
        if (!areEqual(e.getValue(), oe.getValue())) {
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
   * Check whether both string are equal ignoring the case of the strings.
   * 
   * @param first
   * @param second
   * @return
   */
  private static boolean areEqual(final String first, final String second) {
    if (first == null) {
      if (second != null) {
        return false;
      }
    } else if (!first.equalsIgnoreCase(second)) {
      return false;
    }
    return true;
  }

  /**
   * Get {@link ContentType} as string as defined in RFC 2616 (http://www.ietf.org/rfc/rfc2616.txt - chapter 14.17: Content-Type)
   * 
   * @return string representation of <code>ContentType</code> object
   */
  public String toContentTypeString() {
    StringBuilder sb = new StringBuilder();
    sb.append(type).append(TYPE_SUBTYPE_SEPARATOR).append(subtype);
    for (String key : parameters.keySet()) {
      if (isParameterAllowed(key)) {
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
   * from the list is returned based on which {@link ContentType} has less "**" characters set 
   * (checked with {@link #compareWildcardCounts(ContentType)}.
   * If no match (none {@link ContentType} in list is equal to this {@link ContentType}) is found <code>NULL</code> is returned.
   * 
   * @param toMatchContentTypes list of {@link ContentType}s which are matches against this {@link ContentType}
   * @return best matched content type in list or <code>NULL</code> if none content type match to this content type instance
   */
  public ContentType match(final List<ContentType> toMatchContentTypes) {
    for (ContentType supportedContentType : toMatchContentTypes) {
      if (equals(supportedContentType)) {
        if (compareWildcardCounts(supportedContentType) < 0) {
          return this;
        } else {
          return supportedContentType;
        }
      }
    }
    return null;
  }

  /**
   * Check if a valid match for this {@link ContentType} exists in given list.
   * For more detail what a valid match is see {@link #match(List)}.
   * 
   * @param toMatchContentTypes list of {@link ContentType}s which are matches against this {@link ContentType}
   * @return <code>true</code> if a matching content type was found in given list 
   *          or <code>false</code> if none matching content type match was found
   */
  public boolean hasMatch(final List<ContentType> toMatchContentTypes) {
    return match(toMatchContentTypes) != null;
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
  public int compareWildcardCounts(final ContentType otherContentType) {
    return countWildcards() - otherContentType.countWildcards();
  }

  private int countWildcards() {
    int count = 0;
    if (MEDIA_TYPE_WILDCARD.equals(type)) {
      count += 2;
    }
    if (MEDIA_TYPE_WILDCARD.equals(subtype)) {
      count++;
    }
    return count;
  }

  /**
   * 
   * @return <code>true</code> if both <code>type</code> and <code>subtype</code> of this instance are a "*".
   */
  public boolean isWildcard() {
    return (MEDIA_TYPE_WILDCARD.equals(type) && MEDIA_TYPE_WILDCARD.equals(subtype));
  }

  public static List<ContentType> convert(final List<String> types) {
    List<ContentType> results = new ArrayList<ContentType>();
    for (String contentType : types) {
      results.add(ContentType.create(contentType));
    }
    return results;
  }

  /**
   * Check if a valid match for given content type formated string (<code>toMatch</code>) exists in given list.
   * Therefore the given content type formated string (<code>toMatch</code>) is converted into a {@link ContentType}
   * with a simple {@link #create(String)} call (during which an exception can occur).
   * 
   * For more detail in general see {@link #hasMatch(List)} and for what a valid match is see {@link #match(List)}.
   * 
   * @param toMatch content type formated string (<code>toMatch</code>) for which is checked if a match exists in given list
   * @param matchExamples list of {@link ContentType}s which are matches against content type formated string (<code>toMatch</code>)
   * @return <code>true</code> if a matching content type was found in given list 
   *          or <code>false</code> if none matching content type match was found
   */
  public static boolean match(final String toMatch, final ContentType... matchExamples) {
    ContentType toMatchContentType = ContentType.create(toMatch);

    return toMatchContentType.hasMatch(Arrays.asList(matchExamples));
  }
}
