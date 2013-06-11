package com.sap.core.odata.api.edm.provider;

import java.util.List;

import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.FullQualifiedName;

/**
 * Objects of this class represent a complex property.
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

  @Override
  public ComplexProperty setName(final String name) {
    super.setName(name);
    return this;
  }

  @Override
  public ComplexProperty setFacets(final EdmFacets facets) {
    super.setFacets(facets);
    return this;
  }

  @Override
  public ComplexProperty setCustomizableFeedMappings(final CustomizableFeedMappings customizableFeedMappings) {
    super.setCustomizableFeedMappings(customizableFeedMappings);
    return this;
  }

  @Override
  public ComplexProperty setMimeType(final String mimeType) {
    super.setMimeType(mimeType);
    return this;
  }

  @Override
  public ComplexProperty setMapping(final Mapping mapping) {
    super.setMapping(mapping);
    return this;
  }

  @Override
  public ComplexProperty setDocumentation(final Documentation documentation) {
    super.setDocumentation(documentation);
    return this;
  }

  @Override
  public ComplexProperty setAnnotationAttributes(final List<AnnotationAttribute> annotationAttributes) {
    super.setAnnotationAttributes(annotationAttributes);
    return this;
  }

  @Override
  public ComplexProperty setAnnotationElements(final List<AnnotationElement> annotationElements) {
    super.setAnnotationElements(annotationElements);
    return this;
  }
}
