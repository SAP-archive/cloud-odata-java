package com.sap.core.odata.core.ep.aggregator;

import com.sap.core.odata.api.edm.EdmCustomizableFeedMappings;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmType;

/**
 * Collects informations about a property of an entity.
 * @author SAP AG
 */
public class EntityPropertyInfo {
  protected String name;
  protected EdmType type;
  protected EdmFacets facets;
  protected EdmCustomizableFeedMappings customMapping;
  private String mimeType;
  protected EdmMapping mapping;

  EntityPropertyInfo(final String name, final EdmType type, final EdmFacets facets, final EdmCustomizableFeedMappings customizableFeedMapping, final String mimeType, final EdmMapping mapping) {
    this.name = name;
    this.type = type;
    this.facets = facets;
    this.customMapping = customizableFeedMapping;
    this.mimeType = mimeType;
    this.mapping = mapping;
  }

  static EntityPropertyInfo create(final EdmProperty property) throws EdmException {
    return new EntityPropertyInfo(
        property.getName(),
        property.getType(),
        property.getFacets(),
        property.getCustomizableFeedMappings(),
        property.getMimeType(),
        property.getMapping());
  }

  public boolean isMandatory() {
    if(facets == null) {
      return false;
    } else if(facets.isNullable() == null) {
      return false;
    } else {
      return !facets.isNullable();
    }
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

  public String getMimeType() {
    return mimeType;
  }

  public EdmMapping getMapping() {
    return mapping;
  }

  @Override
  public String toString() {
    return name;
  }
}
