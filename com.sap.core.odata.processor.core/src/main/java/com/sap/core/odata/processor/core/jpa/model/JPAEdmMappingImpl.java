package com.sap.core.odata.processor.core.jpa.model;

import java.util.ArrayList;

import com.sap.core.odata.api.edm.provider.Mapping;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmMapping;

public class JPAEdmMappingImpl extends Mapping implements JPAEdmMapping {

  private String columnName = null;
  private Class<?> type = null;
  private ArrayList<Class<?>> typeHierarchy = null;

  @Override
  public void setJPAColumnName(final String name) {
    columnName = name;

  }

  @Override
  public String getJPAColumnName() {
    return columnName;
  }

  @Override
  public void setJPAType(final Class<?> type) {
    this.type = type;

  }

  @Override
  public Class<?> getJPAType() {
    return type;
  }

  @Override
  public Class<?>[] getJPATypeHierachy() {
    if (typeHierarchy != null)
      return (Class<?>[]) typeHierarchy.toArray();
    return null;
  }

  @Override
  public int addToJPATypeHierachy(Class<?> type) {
    if (typeHierarchy == null)
      typeHierarchy = new ArrayList<Class<?>>();

    typeHierarchy.add(type);
    return typeHierarchy.size();
  }

}
