package com.sap.core.odata.api.edm.provider;

public class EntityContainer {

  private String name;
  private String extendz;
  private boolean isDefaultEntityContainer;

  public EntityContainer(String name, String extendz, boolean isDefaultEntityContainer) {
    this.name = name;
    this.extendz = extendz;
    this.isDefaultEntityContainer = isDefaultEntityContainer;
  }

  public String getName() {
    return name;
  }

  public String getExtendz() {
    return extendz;
  }

  public boolean isDefaultEntityContainer() {
    return isDefaultEntityContainer;
  }
}