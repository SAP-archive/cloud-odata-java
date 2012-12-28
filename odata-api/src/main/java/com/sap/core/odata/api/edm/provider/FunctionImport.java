package com.sap.core.odata.api.edm.provider;

import java.util.List;

/**
 * Objects of this class represent a function import
 * @author SAP AG
 */
public class FunctionImport {

  private String name;
  private ReturnType returnType;
  private String entitySet;
  private String httpMethod;
  private List<FunctionImportParameter> parameters;
  private Mapping mapping;
  private Documentation documentation;
  private List<AnnotationAttribute> annotationAttributes;
  private List<AnnotationElement> annotationElements;

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
   * @return List<{@link FunctionImportParameter}>s of this function import
   */
  public List<FunctionImportParameter> getParameters() {
    return parameters;
  }

  /**
   * @return {@link Mapping} for this type
   */
  public Mapping getMapping() {
    return mapping;
  }

  /**
   * @return {@link Documentation} documentation
   */
  public Documentation getDocumentation() {
    return documentation;
  }

  /**
   * @return collection of {@link AnnotationAttribute} annotation attributes
   */
  public List<AnnotationAttribute> getAnnotationAttributes() {
    return annotationAttributes;
  }

  /**
   * @return collection of {@link AnnotationElement} annotation elements
   */
  public List<AnnotationElement> getAnnotationElements() {
    return annotationElements;
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
  public FunctionImport setParameters(List<FunctionImportParameter> parameters) {
    this.parameters = parameters;
    return this;
  }

  /**
   * Sets the {@link Mapping}
   * @param mapping
   * @return {@link FunctionImport} for method chaining
   */
  public FunctionImport setMapping(Mapping mapping) {
    this.mapping = mapping;
    return this;
  }

  /**
   * Sets the {@link Documentation}
   * @param documentation
   * @return {@link FunctionImport} for method chaining
   */
  public FunctionImport setDocumentation(Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationAttribute} for this {@link FunctionImport}
   * @param annotationAttributes
   * @return {@link FunctionImport} for method chaining
   */
  public FunctionImport setAnnotationAttributes(List<AnnotationAttribute> annotationAttributes) {
    this.annotationAttributes = annotationAttributes;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationElement} for this {@link FunctionImport}
   * @param annotationElements
   * @return {@link FunctionImport} for method chaining
   */
  public FunctionImport setAnnotationElements(List<AnnotationElement> annotationElements) {
    this.annotationElements = annotationElements;
    return this;
  }
}