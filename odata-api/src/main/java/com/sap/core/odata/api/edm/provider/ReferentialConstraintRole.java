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
 * Objects of this Class represent a referential constraint role
 * @author SAP AG
 */
public class ReferentialConstraintRole {

  private String role;
  private List<PropertyRef> propertyRefs;
  private List<AnnotationAttribute> annotationAttributes;
  private List<AnnotationElement> annotationElements;

  /**
   * @return <b>String</b> role of this {@link ReferentialConstraintRole}
   */
  public String getRole() {
    return role;
  }

  /**
   * @return List<{@link PropertyRef}> for this {@link ReferentialConstraintRole}
   */
  public List<PropertyRef> getPropertyRefs() {
    return propertyRefs;
  }

  /**
   * @return List of {@link AnnotationAttribute} annotation attributes
   */
  public List<AnnotationAttribute> getAnnotationAttributes() {
    return annotationAttributes;
  }

  /**
   * @return List of {@link AnnotationElement} annotation elements
   */
  public List<AnnotationElement> getAnnotationElements() {
    return annotationElements;
  }

  /**
   * Sets the role of this {@link ReferentialConstraintRole}
   * @param role
   * @return {@link ReferentialConstraintRole} for method chaining
   */
  public ReferentialConstraintRole setRole(final String role) {
    this.role = role;
    return this;
  }

  /**
   * Sets the {@link PropertyRef}s of this {@link ReferentialConstraintRole}
   * @param propertyRef
   * @return {@link ReferentialConstraintRole} for method chaining
   */
  public ReferentialConstraintRole setPropertyRefs(final List<PropertyRef> propertyRef) {
    propertyRefs = propertyRef;
    return this;
  }

  /**
   * Sets the List of {@link AnnotationAttribute} for this {@link ReferentialConstraintRole}
   * @param annotationAttributes
   * @return {@link ReferentialConstraintRole} for method chaining
   */
  public ReferentialConstraintRole setAnnotationAttributes(final List<AnnotationAttribute> annotationAttributes) {
    this.annotationAttributes = annotationAttributes;
    return this;
  }

  /**
   * Sets the List of {@link AnnotationElement} for this {@link ReferentialConstraintRole}
   * @param annotationElements
   * @return {@link ReferentialConstraintRole} for method chaining
   */
  public ReferentialConstraintRole setAnnotationElements(final List<AnnotationElement> annotationElements) {
    this.annotationElements = annotationElements;
    return this;
  }
}
