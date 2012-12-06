package com.sap.core.odata.api.edm.provider;

import java.util.Collection;

import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;

public class SimpleProperty extends Property {

  private EdmSimpleTypeKind type;

  /**
   * @return {@link EdmSimpleTypeKind} of this property
   */
  public EdmSimpleTypeKind getType() {
    return type;
  }

  /**
   * MANDATORY
   * <p>Sets the {@link EdmSimpleTypeKind} for this {@link Property}
   * @param type
   * @return {@link Property} for method chaining
   */
  public SimpleProperty setType(EdmSimpleTypeKind type) {
    this.type = type;
    return this;
  }
  
  @Override
  public SimpleProperty setName(String name) {
    super.setName(name);
    return this;
  }

  @Override
  public SimpleProperty setFacets(EdmFacets facets) {
    super.setFacets(facets);
    return this;
  }

  @Override
  public SimpleProperty setCustomizableFeedMappings(CustomizableFeedMappings customizableFeedMappings) {
    super.setCustomizableFeedMappings(customizableFeedMappings);
    return this;
  }

  @Override
  public SimpleProperty setMimeType(String mimeType) {
    super.setMimeType(mimeType);
    return this;
  }

  @Override
  public SimpleProperty setMapping(Mapping mapping) {
    super.setMapping(mapping);
    return this;
  }

  @Override
  public SimpleProperty setDocumentation(Documentation documentation) {
    super.setDocumentation(documentation);
    return this;
  }

  @Override
  public SimpleProperty setAnnotationAttributes(Collection<AnnotationAttribute> annotationAttributes) {
    super.setAnnotationAttributes(annotationAttributes);
    return this;
  }

  @Override
  public SimpleProperty setAnnotationElements(Collection<AnnotationElement> annotationElements) {
    super.setAnnotationElements(annotationElements);
    return this;
  }
}
