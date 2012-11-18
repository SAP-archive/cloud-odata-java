package com.sap.core.odata.api.edm.provider;

import java.util.Collection;

public class FunctionImport {

  private String name;
  private ReturnType returnType;
  private String entitySet;
  private String httpMethod;
  private Collection<FunctionImportParameter> parameters;
  private Documentation documentation;
  private Annotations annotations;

  public String getName() {
    return name;
  }

  public ReturnType getReturnType() {
    return returnType;
  }

  public String getEntitySet() {
    return entitySet;
  }

  public String getHttpMethod() {
    return httpMethod;
  }

  public Collection<FunctionImportParameter> getParameters() {
    return parameters;
  }

  public Documentation getDocumentation() {
    return documentation;
  }

  public Annotations getAnnotations() {
    return annotations;
  }

  public FunctionImport setName(String name) {
    this.name = name;
    return this;
  }

  public FunctionImport setReturnType(ReturnType returnType) {
    this.returnType = returnType;
    return this;
  }

  public FunctionImport setEntitySet(String entitySet) {
    this.entitySet = entitySet;
    return this;
  }

  public FunctionImport setHttpMethod(String httpMethod) {
    this.httpMethod = httpMethod;
    return this;
  }

  public FunctionImport setParameters(Collection<FunctionImportParameter> parameters) {
    this.parameters = parameters;
    return this;
  }

  public FunctionImport setDocumentation(Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  public FunctionImport setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }

}