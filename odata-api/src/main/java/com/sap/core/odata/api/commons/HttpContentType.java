package com.sap.core.odata.api.commons;


/**
 * Constants for <code>Http Content Type</code> definitions as specified in 
 * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html">RFC 2616 Section 14</a>.
 */
public interface HttpContentType {

  String APPLICATION_XML = "application/xml";
  String APPLICATION_ATOM_XML = "application/atom+xml";
  String APPLICATION_ATOM_XML_ENTRY = "application/atom+xml; type=entry";
  String APPLICATION_ATOM_XML_FEED = "application/atom+xml; type=feed";
  String APPLICATION_ATOM_SVC = "application/atomsvc+xml";
  String APPLICATION_JSON = "application/json";
  String APPLICATION_OCTET_STREAM = "application/octet-stream";
  String TEXT_PLAIN = "text/plain";
  String MULTIPART_MIXED = "multipart/mixed";
}
