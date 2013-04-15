/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.edm.provider;

import java.util.List;

/**
 * Objects of this class represent an entity container including its child elements
 * @author SAP AG
 */
public class EntityContainer extends EntityContainerInfo {

  private List<EntitySet> entitySets;
  private List<AssociationSet> associationSets;
  private List<FunctionImport> functionImports;
  private Documentation documentation;

  /**
   * @return <b>List</b> of all entity sets of the entity container
   */
  public List<EntitySet> getEntitySets() {
    return entitySets;
  }

  /**
   * Sets the entity sets of this {@link EntityContainer}
   * @param entitySets
   * @return {@link EntityContainerInfo} for method chaining
   */
  public EntityContainer setEntitySets(final List<EntitySet> entitySets) {
    this.entitySets = entitySets;
    return this;
  }

  /**
   * @return <b>List</b> of all association sets of the entity container
   */
  public List<AssociationSet> getAssociationSets() {
    return associationSets;
  }

  /**
   * Sets the association sets of this {@link EntityContainer}
   * @param associationSets
   * @return {@link EntityContainerInfo} for method chaining
   */
  public EntityContainer setAssociationSets(final List<AssociationSet> associationSets) {
    this.associationSets = associationSets;
    return this;
  }

  /**
   * @return <b>List</b> of all function imports of the entity container
   */
  public List<FunctionImport> getFunctionImports() {
    return functionImports;
  }

  /**
   * Sets the function imports of this {@link EntityContainer}
   * @param functionImports
   * @return {@link EntityContainerInfo} for method chaining
   */
  public EntityContainer setFunctionImports(final List<FunctionImport> functionImports) {
    this.functionImports = functionImports;
    return this;
  }

  /**
   * Sets the name of this {@link EntityContainer}
   * @param name
   * @return {@link EntityContainer} for method chaining
   */
  @Override
  public EntityContainer setName(final String name) {
    super.setName(name);
    return this;
  }

  /**
   * Sets the entity container which is the parent of this {@link EntityContainer}
   * @param extendz
   * @return {@link EntityContainer} for method chaining
   */
  @Override
  public EntityContainer setExtendz(final String extendz) {
    super.setExtendz(extendz);
    return this;
  }

  /**
   * Sets if this is the default {@link EntityContainer}
   * @param isDefaultEntityContainer
   * @return {@link EntityContainer} for method chaining
   */
  @Override
  public EntityContainer setDefaultEntityContainer(final boolean isDefaultEntityContainer) {
    super.setDefaultEntityContainer(isDefaultEntityContainer);
    return this;
  }

  /**
   * @return {@link Documentation} documentation
   */
  public Documentation getDocumentation() {
    return documentation;
  }

  /**
   * Sets the {@link Documentation}
   * @param documentation
   * @return {@link EntityContainer} for method chaining
   */
  public EntityContainer setDocumentation(final Documentation documentation) {
    this.documentation = documentation;
    return this;
  }
}