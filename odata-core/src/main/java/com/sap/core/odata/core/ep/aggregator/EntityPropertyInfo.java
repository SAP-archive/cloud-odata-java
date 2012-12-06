package com.sap.core.odata.core.ep.aggregator;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmType;

public class EntityPropertyInfo {
  protected String name;
  protected EdmType type;
  protected EdmFacets facets;
  
  static EntityPropertyInfo create(EdmProperty property) throws EdmException {
    EntityPropertyInfo info = new EntityPropertyInfo();
    info.name = property.getName();
    info.type = property.getType();
    info.facets = property.getFacets();
    return info;
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
  
  @Override
  public String toString() {
    return name;
  }
}
