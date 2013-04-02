package com.sap.core.odata.core.ep.consumer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EntityProviderReadProperties {
  private OnReadEntryContent callback;
  private boolean merge;
  final private Map<String, Object> typeMappings;
  final private Map<String, String> validatedPrefix2NamespaceUri;

  private EntityProviderReadProperties() {
    typeMappings = new HashMap<String, Object>();
    validatedPrefix2NamespaceUri = new HashMap<String, String>();
  }

  public static EntityProviderReadPropertiesBuilder init() {
    return new EntityProviderReadPropertiesBuilder();
  }

  public static EntityProviderReadPropertiesBuilder initFrom(EntityProviderReadProperties properties) {
    return new EntityProviderReadPropertiesBuilder(properties);
  }

  public Map<String, String> getValidatedPrefixNamespaceUris() {
    return Collections.unmodifiableMap(validatedPrefix2NamespaceUri);
  }

  public Map<String, Object> getTypeMappings() {
    return Collections.unmodifiableMap(typeMappings);
  }

  public OnReadEntryContent getCallback() {
    return callback;
  }

  public boolean getMergeSemantic() {
    return merge;
  }

  /**
   * 
   * 
   * @author SAP AG
   */
  public static class EntityProviderReadPropertiesBuilder {
    private final EntityProviderReadProperties properties = new EntityProviderReadProperties();
    
    public EntityProviderReadPropertiesBuilder() {}
    
    public EntityProviderReadPropertiesBuilder(EntityProviderReadProperties propertiesFrom) {
      properties.merge = propertiesFrom.merge;
      properties.callback = propertiesFrom.callback;
      addValidatedPrefixes(propertiesFrom.validatedPrefix2NamespaceUri);
      addTypeMappings(propertiesFrom.typeMappings);
    }

    public EntityProviderReadPropertiesBuilder mergeSemantic(boolean mergeSemantic) {
      properties.merge = mergeSemantic;
      return this;
    }

    public EntityProviderReadPropertiesBuilder callback(OnReadEntryContent callback) {
      properties.callback = callback;
      return this;
    }
    
    public EntityProviderReadPropertiesBuilder addValidatedPrefixes(Map<String, String> prefix2NamespaceUri) {
      if (prefix2NamespaceUri != null) {
        properties.validatedPrefix2NamespaceUri.putAll(prefix2NamespaceUri);
      }
      return this;
    }
    
    public EntityProviderReadPropertiesBuilder addTypeMappings(final Map<String, Object> typeMappings) {
      if (typeMappings != null) {
        properties.typeMappings.putAll(typeMappings);
      }
      return this;
    }
    
    public EntityProviderReadProperties build() {
      return properties;
    }
  }
}
