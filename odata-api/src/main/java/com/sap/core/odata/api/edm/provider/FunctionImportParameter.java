package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmFacets;

public class FunctionImportParameter {

  private String name;
  private String mode;
  private FullQualifiedName qualifiedName;
  private EdmFacets facets;
  // TODO Mapping
  private Documentation documentation;
  private Annotations annotations;

  public FunctionImportParameter(String name, String mode, FullQualifiedName qualifiedName, EdmFacets facets, Documentation documentation, Annotations annotations) {
    this.name = name;
    this.mode = mode;
    this.qualifiedName = qualifiedName;
    this.facets = facets;
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

  public Documentation getDocumentation() {
    return documentation;
  }

  public Annotations getAnnotations() {
    return annotations;
  }
}