package com.sap.core.odata.core.ep.aggregator;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmCustomizableFeedMappings;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmType;

/**
 * author SAP AG
 */
public class EntityComplexPropertyInfo extends EntityPropertyInfo {

  protected Map<String, EntityPropertyInfo> internalName2EntityPropertyInfo;

  public EntityComplexPropertyInfo(final String name, final EdmType type, final EdmFacets facets, final EdmCustomizableFeedMappings customizableFeedMapping, final Map<String, EntityPropertyInfo> childEntityInfos) {
    super(name, type, facets, customizableFeedMapping);
    internalName2EntityPropertyInfo = childEntityInfos;
  }

  static EntityComplexPropertyInfo create(EdmProperty property, Map<String, EntityPropertyInfo> childEntityInfos) throws EdmException {
    EntityComplexPropertyInfo info = new EntityComplexPropertyInfo(
        property.getName(),
        property.getType(),
        property.getFacets(),
        property.getCustomizableFeedMappings(),
        childEntityInfos);
    return info;
  }

  @Override
  public boolean isComplex() {
    return true;
  }

  public Collection<EntityPropertyInfo> getPropertyInfos() {
    return Collections.unmodifiableCollection(internalName2EntityPropertyInfo.values());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (EntityPropertyInfo info : internalName2EntityPropertyInfo.values()) {
      if (sb.length() == 0) {
        sb.append(super.toString()).append("=>[").append(info.toString());
      } else {
        sb.append(", ").append(info.toString());
      }
    }
    sb.append("]");
    return sb.toString();
  }
}
