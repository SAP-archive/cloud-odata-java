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

import com.sap.core.odata.api.edm.FullQualifiedName;

/**
 * Objects of this class represent an entity set
 * @author SAP AG
 */
public class EntitySet {

  private String name;
  private FullQualifiedName entityType;
  private Mapping mapping;
  private Documentation documentation;
  private List<AnnotationAttribute> annotationAttributes;
  private List<AnnotationElement> annotationElements;

  /**
   * @return <b>String> name of this entity set
   */
  public String getName() {
    return name;
  }

  /**
   * @return {@link FullQualifiedName} of the entity type of this entity set
   */
  public FullQualifiedName getEntityType() {
    return entityType;
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
   * Sets the name of this {@link EntitySet}
   * @param name
   * @return {@link EntitySet} for method chaining
   */
  public EntitySet setName(final String name) {
    this.name = name;
    return this;
  }

  /**
   * Sets the {@link FullQualifiedName} of the {@link EntityType} of this {@link EntitySet}
   * @param entityType
   * @return {@link EntitySet} for method chaining
   */
  public EntitySet setEntityType(final FullQualifiedName entityType) {
    this.entityType = entityType;
    return this;
  }

  /**
   * Sets the {@link Mapping}
   * @param mapping
   * @return {@link EntitySet} for method chaining
   */
  public EntitySet setMapping(final Mapping mapping) {
    this.mapping = mapping;
    return this;
  }

  /**
   * Sets the {@link Documentation}
   * @param documentation
   * @return {@link EntitySet} for method chaining
   */
  public EntitySet setDocumentation(final Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationAttribute} for this {@link EntitySet}
   * @param annotationAttributes
   * @return {@link EntitySet} for method chaining
   */
  public EntitySet setAnnotationAttributes(final List<AnnotationAttribute> annotationAttributes) {
    this.annotationAttributes = annotationAttributes;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationElement} for this {@link EntitySet}
   * @param annotationElements
   * @return {@link EntitySet} for method chaining
   */
  public EntitySet setAnnotationElements(final List<AnnotationElement> annotationElements) {
    this.annotationElements = annotationElements;
    return this;
  }
}
