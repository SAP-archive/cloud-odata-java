package com.sap.core.odata.core.ep.aggregator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

  protected List<EntityPropertyInfo> entityPropertyInfo;

  EntityComplexPropertyInfo(final String name, final EdmType type, final EdmFacets facets, final EdmCustomizableFeedMappings customizableFeedMapping, final List<EntityPropertyInfo> childEntityInfos) {
    super(name, type, facets, customizableFeedMapping, null, null);
    entityPropertyInfo = childEntityInfos;
  }

  static EntityComplexPropertyInfo create(EdmProperty property, List<String> propertyNames, Map<String, EntityPropertyInfo> childEntityInfos) throws EdmException {
    List<EntityPropertyInfo> childEntityInfoList = new ArrayList<EntityPropertyInfo>(childEntityInfos.size());
    for (String name : propertyNames) {
      childEntityInfoList.add(childEntityInfos.get(name));
    }

    EntityComplexPropertyInfo info = new EntityComplexPropertyInfo(
        property.getName(),
        property.getType(),
        property.getFacets(),
        property.getCustomizableFeedMappings(),
        childEntityInfoList);
    return info;
  }

  @Override
  public boolean isComplex() {
    return true;
  }

  public List<EntityPropertyInfo> getPropertyInfos() {
    return Collections.unmodifiableList(entityPropertyInfo);
  }
  
  public EntityPropertyInfo getPropertyInfo(String name) {
    for (EntityPropertyInfo info : entityPropertyInfo) {
      if(info.getName().equals(name)) {
        return info;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (EntityPropertyInfo info : entityPropertyInfo) {
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
