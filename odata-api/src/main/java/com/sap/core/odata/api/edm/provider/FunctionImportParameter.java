package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.FullQualifiedName;

public class FunctionImportParameter {

  private String name;
  private String mode;
  private FullQualifiedName qualifiedName;
  private EdmFacets facets;
  private Mapping mapping;
  private Documentation documentation;
  private Annotations annotations;

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

  public FunctionImportParameter setName(String name) {
    this.name = name;
    return this;
  }

  public FunctionImportParameter setMode(String mode) {
    this.mode = mode;
    return this;
  }

  public FunctionImportParameter setQualifiedName(FullQualifiedName qualifiedName) {
    this.qualifiedName = qualifiedName;
    return this;
  }

  public FunctionImportParameter setFacets(EdmFacets facets) {
    this.facets = facets;
    return this;
  }

  public FunctionImportParameter setMapping(Mapping mapping) {
    this.mapping = mapping;
    return this;
  }

  public FunctionImportParameter setDocumentation(Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  public FunctionImportParameter setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }

}