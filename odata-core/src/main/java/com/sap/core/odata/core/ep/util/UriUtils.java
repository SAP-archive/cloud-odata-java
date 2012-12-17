package com.sap.core.odata.core.ep.util;

import java.net.URI;
import java.net.URISyntaxException;

public class UriUtils {

  public static String encodeUri(String toEncode) throws URISyntaxException {
    URI uri = new URI(null, null, null, -1, toEncode, null, null);
    return uri.toASCIIString();
  }

  public static String encodeUri(String path, String query) throws URISyntaxException {
    URI uri = new URI(null, null, null, -1, path, query, null);
    return uri.toASCIIString();
  }

  public static String encodeUri(URI baseUri, String pathExtension) throws URISyntaxException {
    String scheme = baseUri.getScheme();
    String userInfo = baseUri.getUserInfo();
    String host = baseUri.getHost();
    int port = baseUri.getPort();
    String path = baseUri.getPath() + pathExtension;
    String query = baseUri.getQuery();
    String fragment = baseUri.getFragment();
    URI uri = new URI(scheme, userInfo, host, port, path, query, fragment);

    return uri.toASCIIString();
  }
}
