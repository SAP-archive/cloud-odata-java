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
 * Objects of this class represent documentation
 * @author SAP AG
 */
public class Documentation {

  private String summary;
  private String longDescription;
  private List<AnnotationAttribute> annotationAttributes;
  private List<AnnotationElement> annotationElements;

  /**
   * @return <b>String</b> summary
   */
  public String getSummary() {
    return summary;
  }

  /**
   * @return <b>String</b> the long description
   */
  public String getLongDescription() {
    return longDescription;
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
   * Sets the summary for this {@link Documentation}
   * @param summary
   * @return {@link Documentation} for method chaining
   */
  public Documentation setSummary(final String summary) {
    this.summary = summary;
    return this;
  }

  /**
   * Sets the long description for this {@link Documentation}
   * @param longDescription
   * @return {@link Documentation} for method chaining
   */
  public Documentation setLongDescription(final String longDescription) {
    this.longDescription = longDescription;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationAttribute} for this {@link Documentation}
   * @param annotationAttributes
   * @return {@link Documentation} for method chaining
   */
  public Documentation setAnnotationAttributes(final List<AnnotationAttribute> annotationAttributes) {
    this.annotationAttributes = annotationAttributes;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationElement} for this {@link Documentation}
   * @param annotationElements
   * @return {@link Documentation} for method chaining
   */
  public Documentation setAnnotationElements(final List<AnnotationElement> annotationElements) {
    this.annotationElements = annotationElements;
    return this;
  }
}
