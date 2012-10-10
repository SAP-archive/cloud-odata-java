package org.odata4j.core;

/**
 * Useful constants.
 */
public class ODataConstants {

  private ODataConstants() {}

  public static final String TEXT_PLAIN = "text/plain";
  public static final String TEXT_PLAIN_CHARSET_UTF8 = TEXT_PLAIN + ";charset=" + Charsets.Lower.UTF_8;

  public static final String APPLICATION_ATOM_XML = "application/atom+xml";
  public static final String APPLICATION_ATOM_XML_CHARSET_UTF8 = APPLICATION_ATOM_XML + ";charset=" + Charsets.Lower.UTF_8;

  public static final String APPLICATION_ATOMSVC_XML = "application/atomsvc+xml";
  public static final String APPLICATION_ATOMSVC_XML_CHARSET_UTF8 = APPLICATION_ATOMSVC_XML + ";charset=" + Charsets.Lower.UTF_8;

  public static final String APPLICATION_XML_CHARSET_UTF8 = "application/xml;charset=" + Charsets.Lower.UTF_8;
  public static final String TEXT_JAVASCRIPT_CHARSET_UTF8 = "text/javascript;charset=" + Charsets.Lower.UTF_8;
  public static final String APPLICATION_JAVASCRIPT = "application/json";
  public static final String APPLICATION_JAVASCRIPT_CHARSET_UTF8 = APPLICATION_JAVASCRIPT + ";charset=" + Charsets.Lower.UTF_8;

  public static final ODataVersion DATA_SERVICE_VERSION = ODataVersion.V1;
  public static final String DATA_SERVICE_VERSION_HEADER = DATA_SERVICE_VERSION.asString;

  /** Common http header names. */
  public static class Headers {
    public static final String X_HTTP_METHOD = "X-HTTP-METHOD";
    public static final String DATA_SERVICE_VERSION = "DataServiceVersion";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String USER_AGENT = "User-Agent";
    public static final String IF_MATCH = "If-Match";
  }

  /** Common character sets. */
  public static class Charsets {
    /** Common character sets. (UPPER-CASE) */
    public static class Upper {
      public static final String UTF_8 = "UTF-8";
      public static final String ISO_8859_1 = "ISO-8859-1"; // latin1
      public static final String ISO_8859_15 = "ISO-8859-15"; // latin9
    }

    /** Common character sets. (lower-case) */
    public static class Lower {
      public static final String UTF_8 = "utf-8";
      public static final String ISO_8859_1 = "iso-8859-1";
      public static final String ISO_8859_15 = "iso-8859-15";
    }
  }
}
