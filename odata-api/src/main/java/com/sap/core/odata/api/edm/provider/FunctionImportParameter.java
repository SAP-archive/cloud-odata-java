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

import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;

/**
 * Objects of this class represent function import parameters
 * @author SAP AG
 */
public class FunctionImportParameter {

  private String name;
  private String mode;
  private EdmSimpleTypeKind type;
  private EdmFacets facets;
  private Mapping mapping;
  private Documentation documentation;
  private List<AnnotationAttribute> annotationAttributes;
  private List<AnnotationElement> annotationElements;

  /**
   * @return <b>String</b> name of the parameter
   */
  public String getName() {
    return name;
  }

  /**
   * @return <b>String</b> mode of this parameter
   */
  public String getMode() {
    return mode;
  }

  /**
   * @return {@link EdmSimpleTypeKind} of this parameter
   */
  public EdmSimpleTypeKind getType() {
    return type;
  }

  /**
   * @return {@link EdmFacets} of this parameter
   */
  public EdmFacets getFacets() {
    return facets;
  }

  /**
   * @return {@link Mapping} of this parameter
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
   * Sets the name of this {@link FunctionImportParameter}
   * @param name
   * @return {@link FunctionImportParameter} for method chaining
   */
  public FunctionImportParameter setName(final String name) {
    this.name = name;
    return this;
  }

  /**
   * Sets the mode of this {@link FunctionImportParameter}
   * @param mode
   * @return {@link FunctionImportParameter} for method chaining
   */
  public FunctionImportParameter setMode(final String mode) {
    this.mode = mode;
    return this;
  }

  /**
   * Sets the {@link EdmSimpleTypeKind} of this {@link FunctionImportParameter}
   * @param type
   * @return {@link FunctionImportParameter} for method chaining
   */
  public FunctionImportParameter setType(final EdmSimpleTypeKind type) {
    this.type = type;
    return this;
  }

  /**
   * Sets the {@link EdmFacets} of this {@link FunctionImportParameter}
   * @param facets
   * @return {@link FunctionImportParameter} for method chaining
   */
  public FunctionImportParameter setFacets(final EdmFacets facets) {
    this.facets = facets;
    return this;
  }

  /**
   * Sets the {@link Mapping} of this {@link FunctionImportParameter}
   * @param mapping
   * @return {@link FunctionImportParameter} for method chaining
   */
  public FunctionImportParameter setMapping(final Mapping mapping) {
    this.mapping = mapping;
    return this;
  }

  /**
   * Sets the {@link Documentation} of this {@link FunctionImportParameter}
   * @param documentation
   * @return {@link FunctionImportParameter} for method chaining
   */
  public FunctionImportParameter setDocumentation(final Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationAttribute} for this {@link FunctionImportParameter}
   * @param annotationAttributes
   * @return {@link FunctionImportParameter} for method chaining
   */
  public FunctionImportParameter setAnnotationAttributes(final List<AnnotationAttribute> annotationAttributes) {
    this.annotationAttributes = annotationAttributes;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationElement} for this {@link FunctionImportParameter}
   * @param annotationElements
   * @return {@link FunctionImportParameter} for method chaining
   */
  public FunctionImportParameter setAnnotationElements(final List<AnnotationElement> annotationElements) {
    this.annotationElements = annotationElements;
    return this;
  }
}
