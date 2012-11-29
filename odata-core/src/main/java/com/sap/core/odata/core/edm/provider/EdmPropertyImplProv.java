package com.sap.core.odata.core.edm.provider;

import com.sap.core.odata.api.edm.EdmCustomizableFeedMappings;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.provider.Property;

public class EdmPropertyImplProv extends EdmElementImplProv implements EdmProperty {

  private Property property;

  public EdmPropertyImplProv(EdmImplProv edm, Property property) throws EdmException {
    super(edm, property.getName(), property.getType(), property.getFacets(), property.getMapping());
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
}