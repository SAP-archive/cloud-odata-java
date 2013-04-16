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
import com.sap.core.odata.api.edm.FullQualifiedName;

/**
 * Objects of this class represent a complex property
 * @author SAP AG
 */
public class ComplexProperty extends Property {

  private FullQualifiedName type;

  /**
   * @return {@link FullQualifiedName} of this property
   */
  public FullQualifiedName getType() {
    return type;
  }

  /**
   * Sets the {@link FullQualifiedName} for this {@link Property}
   * @param type
   * @return {@link Property} for method chaining
   */
  public ComplexProperty setType(final FullQualifiedName type) {
    this.type = type;
    return this;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.provider.Property#setName(java.lang.String)
   */
  @Override
  public ComplexProperty setName(final String name) {
    super.setName(name);
    return this;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.provider.Property#setFacets(com.sap.core.odata.api.edm.EdmFacets)
   */
  @Override
  public ComplexProperty setFacets(final EdmFacets facets) {
    super.setFacets(facets);
    return this;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.provider.Property#setCustomizableFeedMappings(com.sap.core.odata.api.edm.provider.CustomizableFeedMappings)
   */
  @Override
  public ComplexProperty setCustomizableFeedMappings(final CustomizableFeedMappings customizableFeedMappings) {
    super.setCustomizableFeedMappings(customizableFeedMappings);
    return this;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.provider.Property#setMimeType(java.lang.String)
   */
  @Override
  public ComplexProperty setMimeType(final String mimeType) {
    super.setMimeType(mimeType);
    return this;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.provider.Property#setMapping(com.sap.core.odata.api.edm.provider.Mapping)
   */
  @Override
  public ComplexProperty setMapping(final Mapping mapping) {
    super.setMapping(mapping);
    return this;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.provider.Property#setDocumentation(com.sap.core.odata.api.edm.provider.Documentation)
   */
  @Override
  public ComplexProperty setDocumentation(final Documentation documentation) {
    super.setDocumentation(documentation);
    return this;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.provider.Property#setAnnotationAttributes(java.util.List)
   */
  @Override
  public ComplexProperty setAnnotationAttributes(final List<AnnotationAttribute> annotationAttributes) {
    super.setAnnotationAttributes(annotationAttributes);
    return this;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.provider.Property#setAnnotationElements(java.util.List)
   */
  @Override
  public ComplexProperty setAnnotationElements(final List<AnnotationElement> annotationElements) {
    super.setAnnotationElements(annotationElements);
    return this;
  }
}
