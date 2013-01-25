package com.sap.core.odata.api.commons;

/**
 * Constants for <code>Http Content Type</code> definitions as specified in 
 * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html">RFC 2616 Section 14</a>.
 * @author SAP AG
 */
public interface HttpContentType {

  String APPLICATION_XML_UTF8 = "application/xml; charset=utf-8";
  String APPLICATION_ATOM_XML_UTF8 = "application/atom+xml; charset=utf-8";
  String APPLICATION_ATOM_XML_ENTRY_UTF8 = "application/atom+xml; type=entry; charset=utf-8";
  String APPLICATION_ATOM_XML_FEED_UTF8 = "application/atom+xml; type=feed; charset=utf-8";
  String APPLICATION_ATOM_SVC_UTF8 = "application/atomsvc+xml; charset=utf-8";
  String APPLICATION_JSON_UTF8 = "application/json; charset=utf-8";
  String TEXT_PLAIN_UTF8 = "text/plain; charset=utf-8";
  String APPLICATION_OCTET_STREAM = "application/octet-stream";
  String MULTIPART_MIXED = "multipart/mixed";
  String WILDCARD = "*/*";
}
