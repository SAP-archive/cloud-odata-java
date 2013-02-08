package com.sap.core.odata.core.commons;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Encodes a Java String (in its internal UTF-16 encoding) into its
 * percent-encoded UTF-8 representation according to
 * <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>
 * (with consideration of its predecessor RFC 2396).
 * @author SAP AG
 */
public class Encoder {

  /**
   * Encodes a Java String (in its internal UTF-16 encoding) into its
   * percent-encoded UTF-8 representation according to
   * <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>
   * (with consideration of its predecessor RFC 2396).
   * @param value the Java String
   * @return the encoded String
   */
  public static String encode(String value) {
    Encoder encoder = new Encoder(UNSAFE, UNRESERVED, null);
    return encoder.encodeInternal(value);
  }

  private final String unsafe;
  private final String unreserved;
  private final Map<Character, String> map;

  private Encoder(String unsafe, String unreserved, Map<Character, String> map) {
    this.unsafe = unsafe;
    this.unreserved = unreserved;
    this.map = map;
  }

  private final static String RFC3986_UNRESERVED = "-._~"; // + ALPHA + DIGIT
  private final static String RFC3986_GEN_DELIMS = ":/?#[]@";
  private final static String RFC3986_SUB_DELIMS = "!$&'()*+,;=";
  @SuppressWarnings("unused")
  private final static String RFC3986_RESERVED = RFC3986_GEN_DELIMS + RFC3986_SUB_DELIMS;

  /*
   * this is due to compatibility reasons for java.net.URI decoding behavior
   * which follows RFC2396 (deprecated by RFC3986)
   */
  private final static String UNSAFE_NOSPACE = RFC3986_GEN_DELIMS + "<>%&";
  private final static String UNSAFE = UNSAFE_NOSPACE + " ";
  private final static String UNRESERVED = RFC3986_UNRESERVED;

  private final static String[] hex = {
      "%00", "%01", "%02", "%03", "%04", "%05", "%06", "%07",
      "%08", "%09", "%0a", "%0b", "%0c", "%0d", "%0e", "%0f",
      "%10", "%11", "%12", "%13", "%14", "%15", "%16", "%17",
      "%18", "%19", "%1a", "%1b", "%1c", "%1d", "%1e", "%1f",
      "%20", "%21", "%22", "%23", "%24", "%25", "%26", "%27",
      "%28", "%29", "%2a", "%2b", "%2c", "%2d", "%2e", "%2f",
      "%30", "%31", "%32", "%33", "%34", "%35", "%36", "%37",
      "%38", "%39", "%3a", "%3b", "%3c", "%3d", "%3e", "%3f",
      "%40", "%41", "%42", "%43", "%44", "%45", "%46", "%47",
      "%48", "%49", "%4a", "%4b", "%4c", "%4d", "%4e", "%4f",
      "%50", "%51", "%52", "%53", "%54", "%55", "%56", "%57",
      "%58", "%59", "%5a", "%5b", "%5c", "%5d", "%5e", "%5f",
      "%60", "%61", "%62", "%63", "%64", "%65", "%66", "%67",
      "%68", "%69", "%6a", "%6b", "%6c", "%6d", "%6e", "%6f",
      "%70", "%71", "%72", "%73", "%74", "%75", "%76", "%77",
      "%78", "%79", "%7a", "%7b", "%7c", "%7d", "%7e", "%7f",
      "%80", "%81", "%82", "%83", "%84", "%85", "%86", "%87",
      "%88", "%89", "%8a", "%8b", "%8c", "%8d", "%8e", "%8f",
      "%90", "%91", "%92", "%93", "%94", "%95", "%96", "%97",
      "%98", "%99", "%9a", "%9b", "%9c", "%9d", "%9e", "%9f",
      "%a0", "%a1", "%a2", "%a3", "%a4", "%a5", "%a6", "%a7",
      "%a8", "%a9", "%aa", "%ab", "%ac", "%ad", "%ae", "%af",
      "%b0", "%b1", "%b2", "%b3", "%b4", "%b5", "%b6", "%b7",
      "%b8", "%b9", "%ba", "%bb", "%bc", "%bd", "%be", "%bf",
      "%c0", "%c1", "%c2", "%c3", "%c4", "%c5", "%c6", "%c7",
      "%c8", "%c9", "%ca", "%cb", "%cc", "%cd", "%ce", "%cf",
      "%d0", "%d1", "%d2", "%d3", "%d4", "%d5", "%d6", "%d7",
      "%d8", "%d9", "%da", "%db", "%dc", "%dd", "%de", "%df",
      "%e0", "%e1", "%e2", "%e3", "%e4", "%e5", "%e6", "%e7",
      "%e8", "%e9", "%ea", "%eb", "%ec", "%ed", "%ee", "%ef",
      "%f0", "%f1", "%f2", "%f3", "%f4", "%f5", "%f6", "%f7",
      "%f8", "%f9", "%fa", "%fb", "%fc", "%fd", "%fe", "%ff"
  };

  /**
   * <p>Returns the percent-encoded UTF-8 representation of a String.</p>
   * <p>In order to avoid producing percent-encoded CESU-8 (as described in
   * the Unicode Consortium's <a href="http://www.unicode.org/reports/tr26/">
   * Technical Report #26</a>), this is done in two steps:
   * <ol>
   * <li>Re-encode the characters from their Java-internal UTF-16 representations
   * into their UTF-8 representations.</li>
   * <li>Percent-encode each of the bytes in the UTF-8 representation.
   * This is possible on byte level because all characters that do not have
   * a <code>%xx</code> representation are represented in one byte in UTF-8.</li>
   * </ol></p>
   * @param input input String
   * @return encoded representation
   */
  private String encodeInternal(String input) {
    StringBuilder resultStr = new StringBuilder();

    try {
      for (byte utf8Byte : input.getBytes("UTF-8")) {
        if (isUnsafe(utf8Byte)) { // case unsafe
          resultStr.append(hex[utf8Byte]);
        } else if (map != null && map.containsKey((char) utf8Byte)) { // case mapping
          resultStr.append(map.get((char) utf8Byte));
        } else if (isUnreserved(utf8Byte)) { // case unreserved
          resultStr.append((char) utf8Byte);
        } else if (utf8Byte >= 0) { // case other ASCII
          if (utf8Byte < ' ') // case ASCII control character
            resultStr.append(hex[utf8Byte]);
          else
            resultStr.append((char) utf8Byte);
        } else { // case UTF-8 continuation byte
          resultStr.append(hex[256 + utf8Byte]); // index adjusted for the usage of signed bytes
        }
      }
    } catch (UnsupportedEncodingException e) { // should never happen; UTF-8 is always there
      return null;
    }
    return resultStr.toString();
  }

  private boolean isUnsafe(final byte utf8Byte) {
    return unsafe.indexOf(utf8Byte) >= 0;
  }

  private boolean isUnreserved(final byte utf8Byte) {
    return unreserved.indexOf(utf8Byte) >= 0
        || 'A' <= utf8Byte && utf8Byte <= 'Z' // case A..Z
        || 'a' <= utf8Byte && utf8Byte <= 'z' // case a..z
        || '0' <= utf8Byte && utf8Byte <= '9'; // case 0..9
  }
}
