package com.sap.core.odata.core.ep.aggregator;

import com.sap.core.odata.api.edm.EdmCustomizableFeedMappings;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmType;

/**
 * author SAP AG
 */
public class EntityPropertyInfo {
  protected String name;
  protected EdmType type;
  protected EdmFacets facets;
  protected EdmCustomizableFeedMappings customMapping;

  public EntityPropertyInfo(final String name, final EdmType type, final EdmFacets facets, final EdmCustomizableFeedMappings customizableFeedMapping) {
    this.name = name;
    this.type = type;
    this.facets = facets;
    this.customMapping = customizableFeedMapping;
  }

  static EntityPropertyInfo create(final EdmProperty property) throws EdmException {
    return new EntityPropertyInfo(
        property.getName(),
        property.getType(),
        property.getFacets(),
        property.getCustomizableFeedMappings());
  }

  public boolean isComplex() {
    return false;
  }

  public String getName() {
    return name;
  }

  public EdmType getType() {
    return type;
  }

  public EdmFacets getFacets() {
    return facets;
  }

  public EdmCustomizableFeedMappings getCustomMapping() {
    return customMapping;
  }

  @Override
  public String toString() {
    return name;
  }
}
