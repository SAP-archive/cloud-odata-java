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

import com.sap.core.odata.api.edm.EdmAnnotationElement;

/**
 * Objects of this class represent an annotation element.
 * @author SAP AG
 */
public class AnnotationElement implements EdmAnnotationElement {

  private String namespace;
  private String prefix;
  private String name;
  private String text;
  private List<AnnotationElement> childElements;
  private List<AnnotationAttribute> attributes;

  @Override
  public String getNamespace() {
    return namespace;
  }

  @Override
  public String getPrefix() {
    return prefix;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getText() {
    return text;
  }

  @Override
  public List<AnnotationElement> getChildElements() {
    return childElements;
  }

  @Override
  public List<AnnotationAttribute> getAttributes() {
    return attributes;
  }

  /**
   * Sets the attributes for this {@link AnnotationElement}.
   * @param attributes
   * @return {@link AnnotationElement} for method chaining
   */
  public AnnotationElement setAttributes(final List<AnnotationAttribute> attributes) {
    this.attributes = attributes;
    return this;
  }

  /**
   * Sets the child elements for this {@link AnnotationElement}.
   * Does not set child elements and characterData for one element.
   * @param childElements
   * @return {@link AnnotationElement} for method chaining
   */
  public AnnotationElement setChildElements(final List<AnnotationElement> childElements) {
    this.childElements = childElements;
    return this;
  }

  /**
   * Sets the namespace for this {@link AnnotationElement}.
   * @param namespace
   * @return {@link AnnotationElement} for method chaining
   */
  public AnnotationElement setNamespace(final String namespace) {
    this.namespace = namespace;
    return this;
  }

  /**
   * Sets the prefix for this {@link AnnotationElement}.
   * @param prefix
   * @return {@link AnnotationElement} for method chaining
   */
  public AnnotationElement setPrefix(final String prefix) {
    this.prefix = prefix;
    return this;
  }

  /**
   * Sets the name for this {@link AnnotationElement}.
   * @param name
   * @return {@link AnnotationElement} for method chaining
   */
  public AnnotationElement setName(final String name) {
    this.name = name;
    return this;
  }

  /**
   * Sets the text for this {@link AnnotationElement} which will be displayed inside the tags.
   * Must NOT be set if child elements are set!
   * @param text
   * @return {@link AnnotationElement} for method chaining
   */
  public AnnotationElement setText(final String text) {
    this.text = text;
    return this;
  }

}
