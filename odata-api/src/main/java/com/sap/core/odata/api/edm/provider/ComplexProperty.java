package com.sap.core.odata.api.edm.provider;

import java.util.Collection;

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
   * MANDATORY
   * <p>Sets the {@link FullQualifiedName} for this {@link Property}
   * @param type
   * @return {@link Property} for method chaining
   */
  public ComplexProperty setType(FullQualifiedName type) {
    this.type = type;
    return this;
  }
  
  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.provider.Property#setName(java.lang.String)
   */
  @Override
  public ComplexProperty setName(String name) {
    super.setName(name);
    return this;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.provider.Property#setFacets(com.sap.core.odata.api.edm.EdmFacets)
   */
  @Override
  public ComplexProperty setFacets(EdmFacets facets) {
    super.setFacets(facets);
    return this;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.provider.Property#setCustomizableFeedMappings(com.sap.core.odata.api.edm.provider.CustomizableFeedMappings)
   */
  @Override
  public ComplexProperty setCustomizableFeedMappings(CustomizableFeedMappings customizableFeedMappings) {
    super.setCustomizableFeedMappings(customizableFeedMappings);
    return this;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.provider.Property#setMimeType(java.lang.String)
   */
  @Override
  public ComplexProperty setMimeType(String mimeType) {
    super.setMimeType(mimeType);
    return this;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.provider.Property#setMapping(com.sap.core.odata.api.edm.provider.Mapping)
   */
  @Override
  public ComplexProperty setMapping(Mapping mapping) {
    super.setMapping(mapping);
    return this;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.provider.Property#setDocumentation(com.sap.core.odata.api.edm.provider.Documentation)
   */
  @Override
  public ComplexProperty setDocumentation(Documentation documentation) {
    super.setDocumentation(documentation);
    return this;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.provider.Property#setAnnotationAttributes(java.util.Collection)
   */
  @Override
  public ComplexProperty setAnnotationAttributes(Collection<AnnotationAttribute> annotationAttributes) {
    super.setAnnotationAttributes(annotationAttributes);
    return this;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.edm.provider.Property#setAnnotationElements(java.util.Collection)
   */
  @Override
  public ComplexProperty setAnnotationElements(Collection<AnnotationElement> annotationElements) {
    super.setAnnotationElements(annotationElements);
    return this;
  }
}
