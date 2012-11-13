package com.sap.core.odata.api.edm.provider;

public class EntityContainer {

  private String name;
  private String extendz;
  private boolean isDefaultEntityContainer;

  public String getName() {
    return name;
  }

  public String getExtendz() {
    return extendz;
  }

  public boolean isDefaultEntityContainer() {
    return isDefaultEntityContainer;
  }

  public EntityContainer setName(String name) {
    this.name = name;
    return this;
  }

  public EntityContainer setExtendz(String extendz) {
    this.extendz = extendz;
    return this;
  }

  public EntityContainer setDefaultEntityContainer(boolean isDefaultEntityContainer) {
    this.isDefaultEntityContainer = isDefaultEntityContainer;
    return this;
  }

}