package com.sap.core.odata.core.ep.aggregator;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmException;

public class EntityComplexPropertyInfo extends EntityPropertyInfo {
  protected Map<String, EntityPropertyInfo> internalName2EntityPropertyInfo;
  
  static EntityComplexPropertyInfo create(EdmComplexType property, Map<String, EntityPropertyInfo> childEntityInfos) throws EdmException {
    EntityComplexPropertyInfo info = new EntityComplexPropertyInfo();
    info.name = property.getName();
    info.internalName2EntityPropertyInfo = childEntityInfos;
    return info;
  }
  
  @Override
  boolean isComplex() {
    return true;
  }
  
  public Collection<EntityPropertyInfo> getPropertyInfos() {
    return Collections.unmodifiableCollection(internalName2EntityPropertyInfo.values());
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (EntityPropertyInfo info: internalName2EntityPropertyInfo.values()) {
      if(sb.length() == 0) {
        sb.append(super.toString()).append("=>[").append(info.toString());
      } else {
        sb.append(", ").append(info.toString());
      }
    }
    sb.append("]");
    return sb.toString();
  }
}
