package com.sap.core.odata.api.edm.provider;

import java.util.Collection;

/**
 * @author SAP AG
 * <p>
 * Objects of this class represent an entity container including child elemenst in the EDM
 */
public class EntityContainer extends EntityContainerInfo {

  private Collection<EntitySet> entitySets;
  private Collection<AssociationSet> associationSets;
  private Collection<FunctionImport> functionImports;
  private Documentation documentation;

  /**
   * @return <b>Collection</b> of all entity sets of the entity container
   */
  public Collection<EntitySet> getEntitySets() {
    return entitySets;
  }

  /**
   * Sets the entity sets of this {@link EntityContainer}
   * @param name
   * @return {@link EntityContainerInfo} for method chaining
   */
  public EntityContainer setEntitySets(Collection<EntitySet> entitySets) {
    this.entitySets = entitySets;
    return this;
  }

  /**
   * @return <b>Collection</b> of all association sets of the entity container
   */
  public Collection<AssociationSet> getAssociationSets() {
    return associationSets;
  }

  /**
   * Sets the association sets of this {@link EntityContainer}
   * @param name
   * @return {@link EntityContainerInfo} for method chaining
   */
  public EntityContainer setAssociationSets(Collection<AssociationSet> associationStets) {
    this.associationSets = associationStets;
    return this;
  }

  /**
   * @return <b>Collection</b> of all function imports of the entity container
   */
  public Collection<FunctionImport> getFunctionImports() {
    return functionImports;
  }

  /**
   * Sets the function imports of this {@link EntityContainer}
   * @param name
   * @return {@link EntityContainerInfo} for method chaining
   */
  public EntityContainer setFunctionImports(Collection<FunctionImport> functionImports) {
    this.functionImports = functionImports;
    return this;
  }

  /**
   * Sets the name of this {@link EntityContainer}
   * @param name
   * @return {@link EntityContainer} for method chaining
   */
  @Override
  public EntityContainer setName(String name) {
    super.setName(name);
    return this;
  }

  /**
   * Sets the entity container which is the parent of this {@link EntityContainer}
   * @param extendz
   * @return {@link EntityContainer} for method chaining
   */
  @Override
  public EntityContainer setExtendz(String extendz) {
    super.setExtendz(extendz);
    return this;
  }

  /**
   * Sets if this is the default {@link EntityContainer}
   * @param isDefaultEntityContainer
   * @return {@link EntityContainer} for method chaining
   */
  @Override
  public EntityContainer setDefaultEntityContainer(boolean isDefaultEntityContainer) {
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
  public EntityContainer setDocumentation(Documentation documentation) {
    this.documentation = documentation;
    return this;
  }
}