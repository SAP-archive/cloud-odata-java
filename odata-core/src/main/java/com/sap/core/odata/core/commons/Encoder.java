package com.sap.core.odata.core.commons;

import java.util.Map;

/**
 * @author SAP AG
 */
public class Encoder {

  /*
   * encoding rules:
   * 
   *  http://www.ietf.org/rfc/rfc3986.txt
   *
   *  private final static String RFC3986_UNRESERVED = "-._~"; // + ALPHA + DIGIT
   *  private final static String RFC3986_GEN_DELIMS = ":/?#[]@"; 
   *  private final static String RFC3986_SUB_DELIMS = "!$&'()*+,;="; 
   *  private final static String RFC3986_RESERVED = RFC3986_GEN_DELIMS + RFC3986_SUB_DELIMS; 
   */

  private Encoder(String unsafe, String unreserved, Map<Character, String> map) {
    this.unsafe = unsafe;
    this.unreserved = unreserved;
    this.map = map;
  }

  public static String encodePathPart(String value) {
    Encoder encoder = new Encoder(UNSAFE, UNRESERVED, null);
    return encoder.encodeInternal(value);
  }

  public static String encodeQueryPart(String value) {

    return encodePathPart(value);

    // TODO SKL This needs evaluation! RFC3986

    //    Map<Character, String> map = new HashMap<Character, String>();
    //    map.put(' ', "+");
    //    Encoder encoder = new Encoder(UNSAFE_NOSPACE, UNRESERVED, map);
    //    return encoder.encodeInternal(value);
  }

  private final static String RFC3986_UNRESERVED = "-._~"; // + ALPHA + DIGIT
  private final static String RFC3986_GEN_DELIMS = ":/?#[]@";
  private final static String RFC3986_SUB_DELIMS = "!$&'()*+,;=";
  private final static String RFC3986_RESERVED = RFC3986_GEN_DELIMS + RFC3986_SUB_DELIMS;

  /*
   * this is due to compatibility reasons for URI decoding behavior which is RFC2396 (deprecated by RFC3986)
   */
  private final static String UNSAFE_NOSPACE = RFC3986_GEN_DELIMS + "<>%";
  private final static String UNSAFE = UNSAFE_NOSPACE + " ";
  private final static String UNRESERVED = RFC3986_UNRESERVED;

  private String unsafe;
  private String unreserved;
  private Map<Character, String> map;

  final static String[] hex = {
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

  private String encodeInternal(String input) {
    StringBuilder resultStr = new StringBuilder();

    for (int ch : input.toCharArray()) {
      if (isUnsafe(ch)) { // case unsafe
        resultStr.append(hex[ch]);
      } else if (map != null && map.containsKey((char) ch)) { // case mapping
        resultStr.append(map.get((char) ch));
      } else if (isUnreserved(ch)) { // case unreserved
        resultStr.append((char) ch);
      } else if ('A' <= ch && ch <= 'Z') { // case A..Z
        resultStr.append((char) ch);
      } else if ('a' <= ch && ch <= 'z') { // case a..z
        resultStr.append((char) ch);
      } else if ('0' <= ch && ch <= '9') { // case 0..9
        resultStr.append((char) ch);
      } else if (ch <= 0x007F) { // case other ASCII
        resultStr.append((char) ch);
      } else if (ch <= 0x07FF) { // case non ASCII <= 0x07FF
        resultStr.append(hex[0xc0 | (ch >> 6)]);
        resultStr.append(hex[0x80 | (ch & 0x3F)]);
      } else { // case 0x7FF < ch <= 0xFFFF
        resultStr.append(hex[0xe0 | (ch >> 12)]);
        resultStr.append(hex[0x80 | ((ch >> 6) & 0x3F)]);
        resultStr.append(hex[0x80 | (ch & 0x3F)]);
      }
    }

    return resultStr.toString();
  }

  private boolean isUnsafe(int ch) {
    return unsafe.indexOf(ch) >= 0;
  }

  private boolean isUnreserved(int ch) {
    return unreserved.indexOf(ch) >= 0;
  }

}
