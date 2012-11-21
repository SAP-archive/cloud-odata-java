package com.sap.core.odata.api.edm.provider;

/**
 * @author SAP AG
 * <p>
 * Objects of this class represent an entity container in the EDM
 */
public class EntityContainer {

  private String name;
  private String extendz;
  private boolean isDefaultEntityContainer;

  /**
   * @return <b>String</b> name
   */
  public String getName() {
    return name;
  }

  /**
   * @return <b>String</b> name of the container which this container extends
   */
  public String getExtendz() {
    return extendz;
  }

  /**
   * @return<b>boolean</b> if this container is the default container
   */
  public boolean isDefaultEntityContainer() {
    return isDefaultEntityContainer;
  }

  /**
   * Sets the name of this {@link EntityContainer}
   * @param name
   * @return {@link EntityContainer} for method chaining
   */
  public EntityContainer setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Sets the entity container which is the parent of this {@link EntityContainer}
   * @param extendz
   * @return {@link EntityContainer} for method chaining
   */
  public EntityContainer setExtendz(String extendz) {
    this.extendz = extendz;
    return this;
  }

  /**
   * Sets if this is the default {@link EntityContainer}
   * @param isDefaultEntityContainer
   * @return {@link EntityContainer} for method chaining
   */
  public EntityContainer setDefaultEntityContainer(boolean isDefaultEntityContainer) {
    this.isDefaultEntityContainer = isDefaultEntityContainer;
    return this;
  }

}