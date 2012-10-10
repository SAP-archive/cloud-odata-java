package org.odata4j.producer;

import java.util.List;
import java.util.Locale;

import javax.ws.rs.core.HttpHeaders;

/**
 * An (probably the only ever) implementation of ODataHeadersContext 
 * 
 */
public class ODataHeadersImpl implements ODataHeadersContext {

  public ODataHeadersImpl(HttpHeaders headers) {
    this.headers = headers;
  }

  @Override
  public Iterable<String> getRequestHeaderFieldNames() {
    return this.headers.getRequestHeaders().keySet();
  }

  @Override
  public Iterable<String> getRequestHeaderValues(String fieldName) {
    return this.headers.getRequestHeader(fieldName);
  }

  @Override
  public String getRequestHeaderValue(String fieldName) {
    List<String> l = this.headers.getRequestHeader(fieldName);
    return l == null || l.isEmpty() ? null : l.get(0);
  }

  @Override
  public List<Locale> getAcceptableLanguages() {
    return headers.getAcceptableLanguages();
  }

  private HttpHeaders headers;
}
