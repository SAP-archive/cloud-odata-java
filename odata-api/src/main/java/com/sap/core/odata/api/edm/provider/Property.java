package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.FullQualifiedName;

public class Property {

  private String name;
  private FullQualifiedName type;
  private EdmFacets facets;
  private CustomizableFeedMappings customizableFeedMappings;
  private String mimeType;
  private Mapping mapping;
  private Documentation documentation;
  private Annotations annotations;

  public String getName() {
    return name;
  }

  public FullQualifiedName getType() {
    return type;
  }

  public EdmFacets getFacets() {
    return facets;
  }

  public CustomizableFeedMappings getCustomizableFeedMappings() {
    return customizableFeedMappings;
  }

  public String getMimeType() {
    return mimeType;
  }

  public Mapping getMapping() {
    return mapping;
  }

  public Documentation getDocumentation() {
    return documentation;
  }

  public Annotations getAnnotations() {
    return annotations;
  }

  public Property setName(String name) {
    this.name = name;
    return this;
  }

  public Property setType(FullQualifiedName type) {
    this.type = type;
    return this;
  }

  public Property setFacets(EdmFacets facets) {
    this.facets = facets;
    return this;
  }

  public Property setCustomizableFeedMappings(CustomizableFeedMappings customizableFeedMappings) {
    this.customizableFeedMappings = customizableFeedMappings;
    return this;
  }

  public Property setMimeType(String mimeType) {
    this.mimeType = mimeType;
    return this;
  }

  public Property setMapping(Mapping mapping) {
    this.mapping = mapping;
    return this;
  }

  public Property setDocumentation(Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  public Property setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }
}