package com.sap.core.odata.api.edm.provider;

import java.util.Collection;

/**
 * @author SAP AG
 *
 */
public class FunctionImport {

  private String name;
  private ReturnType returnType;
  private String entitySet;
  private String httpMethod;
  private Collection<FunctionImportParameter> parameters;
  private Documentation documentation;
  private Annotations annotations;

  /**
   * @return <b>String</b> name of this function import
   */
  public String getName() {
    return name;
  }

  /**
   * @return {@link ReturnType} of this function import
   */
  public ReturnType getReturnType() {
    return returnType;
  }

  /**
   * @return <b>String</b> name of the entity set
   */
  public String getEntitySet() {
    return entitySet;
  }

  /**
   * @return <b>String</b> name of  the used HTTP method
   */
  public String getHttpMethod() {
    return httpMethod;
  }

  /**
   * @return Collection<{@link FunctionImportParameter}>s of this function import
   */
  public Collection<FunctionImportParameter> getParameters() {
    return parameters;
  }

  /**
   * @return {@link Documentation} documentation
   */
  public Documentation getDocumentation() {
    return documentation;
  }

  /**
   * @return {@link Annotations} annotations
   */
  public Annotations getAnnotations() {
    return annotations;
  }

  /**
   * MANDATORY
   * <p>Sets the name of this {@link FunctionImport}
   * @param name
   * @return {@link FunctionImport} for method chaining
   */
  public FunctionImport setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * MANDATORY
   * <p>Sets the {@link ReturnType} of this {@link FunctionImport}
   * @param returnType
   * @return {@link FunctionImport} for method chaining
   */
  public FunctionImport setReturnType(ReturnType returnType) {
    this.returnType = returnType;
    return this;
  }

  /**
   * MANDATORY
   * <p>Sets the {@link EntitySet} of this {@link FunctionImport}
   * @param entitySet
   * @return {@link FunctionImport} for method chaining
   */
  public FunctionImport setEntitySet(String entitySet) {
    this.entitySet = entitySet;
    return this;
  }

  /**
   * MANDATORY
   * <p>Sets the HTTP method of this {@link FunctionImport}
   * @param httpMethod
   * @return {@link FunctionImport} for method chaining
   */
  public FunctionImport setHttpMethod(String httpMethod) {
    this.httpMethod = httpMethod;
    return this;
  }

  /**
   * MANDATORY
   * <p>Sets the {@link FunctionImportParameter}s of this {@link FunctionImport}
   * @param parameters
   * @return {@link FunctionImport} for method chaining
   */
  public FunctionImport setParameters(Collection<FunctionImportParameter> parameters) {
    this.parameters = parameters;
    return this;
  }

  /**
   * Sets the {@link Documentation} of this {@link FunctionImport}
   * @param documentation
   * @return {@link FunctionImport} for method chaining
   */
  public FunctionImport setDocumentation(Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  /**
   * Sets the {@link Annotations} of this {@link FunctionImport}
   * @param annotations
   * @return {@link FunctionImport} for method chaining
   */
  public FunctionImport setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }

}