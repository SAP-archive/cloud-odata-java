package com.sap.core.odata.api.edm.provider;

import java.util.List;

/**
 * @author SAP AG
 * <p>
 * Objects of this class represent a schema in the EDM
 */
public class Schema {

  private String namespace;
  private String alias;
  private List<Using> usings;
  private List<EntityType> entityTypes;
  private List<ComplexType> complexTypes;
  private List<Association> associations;
  private List<EntityContainer> entityContainers;
  private Annotations annotations;

  /**
   * @param namespace
   */
  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  /**
   * @param alias
   */
  public void setAlias(String alias) {
    this.alias = alias;
  }

  /**
   * @param usings
   */
  public void setUsings(List<Using> usings) {
    this.usings = usings;
  }

  /**
   * @param entityTypes
   */
  public void setEntityTypes(List<EntityType> entityTypes) {
    this.entityTypes = entityTypes;
  }

  /**
   * @param complexTypes
   */
  public void setComplexTypes(List<ComplexType> complexTypes) {
    this.complexTypes = complexTypes;
  }

  /**
   * @param associations
   */
  public void setAssociations(List<Association> associations) {
    this.associations = associations;
  }

  /**
   * @param entityContainers
   */
  public void setEntityContainers(List<EntityContainer> entityContainers) {
    this.entityContainers = entityContainers;
  }

  /**
   * @param annotations
   */
  public void setAnnotations(Annotations annotations) {
    this.annotations = annotations;
  }

  /**
   * @return
   */
  public String getNamespace() {
    return namespace;
  }

  /**
   * @return
   */
  public String getAlias() {
    return alias;
  }

  /**
   * @return
   */
  public List<Using> getUsings() {
    return usings;
  }

  /**
   * @return
   */
  public List<EntityType> getEntityTypes() {
    return entityTypes;
  }

  /**
   * @return
   */
  public List<ComplexType> getComplexTypes() {
    return complexTypes;
  }

  /**
   * @return
   */
  public List<Association> getAssociations() {
    return associations;
  }

  /**
   * @return
   */
  public List<EntityContainer> getEntityContainers() {
    return entityContainers;
  }

  /**
   * @return
   */
  public Annotations getAnnotations() {
    return annotations;
  }
}