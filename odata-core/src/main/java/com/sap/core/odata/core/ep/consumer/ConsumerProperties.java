package com.sap.core.odata.core.ep.consumer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConsumerProperties {
  private ConsumerCallback callback;
  final private boolean merge;
  final private Map<String, Object> typeMappings;
  final private Map<String, String> validatedPrefix2NamespaceUri;

  public ConsumerProperties() {
    this(false);
  }

  public ConsumerProperties(final boolean merge) {
    typeMappings = new HashMap<String, Object>();
    validatedPrefix2NamespaceUri = new HashMap<String, String>();
    this.merge = merge;
  }

  public void addValidatedPrefixNamespaceUris(final Map<String, String> prefix2NamespaceUri) {
    if (prefix2NamespaceUri != null) {
      validatedPrefix2NamespaceUri.putAll(prefix2NamespaceUri);
    }
  }

  public Map<String, String> getValidatedPrefixNamespaceUris() {
    return Collections.unmodifiableMap(validatedPrefix2NamespaceUri);
  }

  public void addTypeMappings(final Map<String, Object> typeMappings) {
    if (typeMappings != null) {
      this.typeMappings.putAll(typeMappings);
    }
  }

  public Map<String, Object> getTypeMappings() {
    return Collections.unmodifiableMap(typeMappings);
  }

  public ConsumerCallback getCallback() {
    return callback;
  }

  public void setCallback(final ConsumerCallback callback) {
    this.callback = callback;
  }

  public boolean getMergeSemantic() {
    return merge;
  }
}
