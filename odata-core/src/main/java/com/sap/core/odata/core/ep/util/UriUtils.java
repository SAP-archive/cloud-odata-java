package com.sap.core.odata.core.ep.util;

import java.net.URI;
import java.net.URISyntaxException;

public class UriUtils {

  /**
   * Encoding for uri path element (ATTENTION: not for encoding of complete urls)
   * 
   * @param path
   * @return
   * @throws URISyntaxException
   */
  public static String encodeUriPath(String path) throws URISyntaxException {
    String toEncodePath = path;
    if(path != null && !(path.startsWith("/") || path.startsWith("./"))) {
      toEncodePath = "/" + path;
    }
    URI uri = new URI(null, null, null, -1, toEncodePath, null, null);

    if(path != toEncodePath) {
      return uri.toASCIIString().substring(1);
    }
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
