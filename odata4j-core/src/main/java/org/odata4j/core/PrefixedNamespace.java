package org.odata4j.core;

/**
 * A namespace - consists of a uri and local prefix.
 */
public class PrefixedNamespace {

  private final String uri;
  private final String prefix;

  public PrefixedNamespace(String uri, String prefix) {
    this.uri = uri;
    this.prefix = prefix;
  }

  public String getUri() {
    return uri;
  }

  public String getPrefix() {
    return prefix;
  }

  @Override
  public String toString() {
    return uri + " " + prefix;
  }
}
