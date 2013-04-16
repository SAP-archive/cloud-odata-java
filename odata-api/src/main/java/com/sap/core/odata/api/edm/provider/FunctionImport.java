/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
   * Sets the name of this {@link FunctionImport}
   * @param name
   * @return {@link FunctionImport} for method chaining
   */
  public FunctionImport setName(final String name) {
    this.name = name;
    return this;
  }

  /**
   * Sets the {@link ReturnType} of this {@link FunctionImport}
   * @param returnType
   * @return {@link FunctionImport} for method chaining
   */
  public FunctionImport setReturnType(final ReturnType returnType) {
    this.returnType = returnType;
    return this;
  }

  /**
   * Sets the {@link EntitySet} of this {@link FunctionImport}
   * @param entitySet
   * @return {@link FunctionImport} for method chaining
   */
  public FunctionImport setEntitySet(final String entitySet) {
    this.entitySet = entitySet;
    return this;
  }

  /**
   * Sets the HTTP method of this {@link FunctionImport}
   * @param httpMethod
   * @return {@link FunctionImport} for method chaining
   */
  public FunctionImport setHttpMethod(final String httpMethod) {
    this.httpMethod = httpMethod;
    return this;
  }

  /**
   * Sets the {@link FunctionImportParameter}s of this {@link FunctionImport}
   * @param parameters
   * @return {@link FunctionImport} for method chaining
   */
  public FunctionImport setParameters(final List<FunctionImportParameter> parameters) {
    this.parameters = parameters;
    return this;
  }

  /**
   * Sets the {@link Mapping}
   * @param mapping
   * @return {@link FunctionImport} for method chaining
   */
  public FunctionImport setMapping(final Mapping mapping) {
    this.mapping = mapping;
    return this;
  }

  /**
   * Sets the {@link Documentation}
   * @param documentation
   * @return {@link FunctionImport} for method chaining
   */
  public FunctionImport setDocumentation(final Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationAttribute} for this {@link FunctionImport}
   * @param annotationAttributes
   * @return {@link FunctionImport} for method chaining
   */
  public FunctionImport setAnnotationAttributes(final List<AnnotationAttribute> annotationAttributes) {
    this.annotationAttributes = annotationAttributes;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationElement} for this {@link FunctionImport}
   * @param annotationElements
   * @return {@link FunctionImport} for method chaining
   */
  public FunctionImport setAnnotationElements(final List<AnnotationElement> annotationElements) {
    this.annotationElements = annotationElements;
    return this;
  }
}
