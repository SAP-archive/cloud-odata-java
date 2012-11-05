package com.sap.core.odata.api.edm.provider;

import java.util.Map;

public class FunctionImport {

  private String name;
  private ReturnType returnType;
  private String entitySet;
  private String httpMethod;
  private Map<String, FunctionImportParameter> parameters;
  private Documentation documentation;
  private Annotations annotations;

  public FunctionImport(String name, ReturnType returnType, String entitySet, String httpMethod, Map<String, FunctionImportParameter> parameters, Documentation documentation, Annotations annotations) {
    this.name = name;
    this.returnType = returnType;
    this.entitySet = entitySet;
    this.httpMethod = httpMethod;
    this.parameters = parameters;
    this.documentation = documentation;
    this.annotations = annotations;
  }

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

  public Map<String, FunctionImportParameter> getParameters() {
    return parameters;
  }

  public Documentation getDocumentation() {
    return documentation;
  }

  public Annotations getAnnotations() {
    return annotations;
  }
}