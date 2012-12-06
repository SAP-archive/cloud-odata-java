package com.sap.core.odata.api.edm.provider;

import java.util.Collection;

import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.FullQualifiedName;

public class ComplexProperty extends Property {

  private FullQualifiedName type;

  /**
   * @return {@link FullQualifiedName} of this property
   */
  public FullQualifiedName getType() {
    return type;
  }

  /**
   * MANDATORY
   * <p>Sets the {@link FullQualifiedName} for this {@link Property}
   * @param type
   * @return {@link Property} for method chaining
   */
  public ComplexProperty setType(FullQualifiedName type) {
    this.type = type;
    return this;
  }
  
  @Override
  public ComplexProperty setName(String name) {
    super.setName(name);
    return this;
  }

  @Override
  public ComplexProperty setFacets(EdmFacets facets) {
    super.setFacets(facets);
    return this;
  }

  @Override
  public ComplexProperty setCustomizableFeedMappings(CustomizableFeedMappings customizableFeedMappings) {
    super.setCustomizableFeedMappings(customizableFeedMappings);
    return this;
  }

  @Override
  public ComplexProperty setMimeType(String mimeType) {
    super.setMimeType(mimeType);
    return this;
  }

  @Override
  public ComplexProperty setMapping(Mapping mapping) {
    super.setMapping(mapping);
    return this;
  }

  @Override
  public ComplexProperty setDocumentation(Documentation documentation) {
    super.setDocumentation(documentation);
    return this;
  }

  @Override
  public ComplexProperty setAnnotationAttributes(Collection<AnnotationAttribute> annotationAttributes) {
    super.setAnnotationAttributes(annotationAttributes);
    return this;
  }

  @Override
  public ComplexProperty setAnnotationElements(Collection<AnnotationElement> annotationElements) {
    super.setAnnotationElements(annotationElements);
    return this;
  }
}
