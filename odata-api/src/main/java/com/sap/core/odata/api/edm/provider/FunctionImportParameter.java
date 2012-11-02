package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmFacets;

public class FunctionImportParameter {

  private String name;
  private String mode;
  private FullQualifiedName qualifiedName;
  private EdmFacets facets;
  private Mapping mapping;
  private Documentation documentation;
  private Annotations annotations;

  public FunctionImportParameter(String name, String mode, FullQualifiedName qualifiedName, EdmFacets facets, Mapping mapping, Documentation documentation, Annotations annotations) {
    this.name = name;
    this.mode = mode;
    this.qualifiedName = qualifiedName;
    this.facets = facets;
    this.mapping = mapping;
    this.documentation = documentation;
    this.annotations = annotations;
  }

  public String getName() {
    return name;
  }

  public String getMode() {
    return mode;
  }

  public FullQualifiedName getQualifiedName() {
    return qualifiedName;
  }

  public EdmFacets getFacets() {
    return facets;
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
}