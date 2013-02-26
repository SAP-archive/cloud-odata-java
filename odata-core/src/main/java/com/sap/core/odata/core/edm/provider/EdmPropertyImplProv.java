package com.sap.core.odata.core.edm.provider;

import com.sap.core.odata.api.edm.EdmAnnotatable;
import com.sap.core.odata.api.edm.EdmAnnotations;
import com.sap.core.odata.api.edm.EdmCustomizableFeedMappings;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Property;

public abstract class EdmPropertyImplProv extends EdmElementImplProv implements EdmProperty, EdmAnnotatable {

  private Property property;

  public EdmPropertyImplProv(final EdmImplProv edm, final FullQualifiedName propertyName, final Property property) throws EdmException {
    super(edm, property.getName(), propertyName, property.getFacets(), property.getMapping());
    this.property = property;
  }

  @Override
  public EdmCustomizableFeedMappings getCustomizableFeedMappings() throws EdmException {
    return property.getCustomizableFeedMappings();
  }

  @Override
  public String getMimeType() throws EdmException {
    return property.getMimeType();
  }

  @Override
  public EdmAnnotations getAnnotations() throws EdmException {
    return new EdmAnnotationsImplProv(property.getAnnotationAttributes(), property.getAnnotationElements());
  }
}