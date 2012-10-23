package com.sap.core.odata.core.experimental.edm.adapter;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmTypeKind;

public class EdmComplexTypeAdapter extends EdmNamedAdapter implements EdmComplexType {

  private org.odata4j.edm.EdmComplexType edmComplexType;

  public EdmComplexTypeAdapter(org.odata4j.edm.EdmComplexType edmComplexType) {
    super(edmComplexType.getName());
    this.edmComplexType = edmComplexType;
  }

  @Override
  public EdmProperty getProperty(String name) {
    org.odata4j.edm.EdmProperty edmProperty = this.edmComplexType.findProperty(name);
    if (edmProperty != null) {
      return new EdmPropertyAdapter(edmProperty);
    }
    return null;
  }

  @Override
  public List<String> getPropertyNames() {
    List<String> propertyNames = new ArrayList<String>();
    for (org.odata4j.edm.EdmProperty edmProperty : this.edmComplexType.getProperties()) {
      propertyNames.add(edmProperty.getName());
    }
    return propertyNames;
  }

  @Override
  public String getNamespace() {
    return this.edmComplexType.getNamespace();
  }

  @Override
  public EdmComplexType getBaseType() {
    //TODO is this an error in the edm implementation that for a complex type an Entity Type is returned as BaseType?
    //new EdmComplexTypeAdapter(this.edmComplexType.getBaseType());
    return null;
  }

  @Override
  public EdmTypeKind getKind() {
    return EdmTypeKind.COMPLEX;
  }

}
