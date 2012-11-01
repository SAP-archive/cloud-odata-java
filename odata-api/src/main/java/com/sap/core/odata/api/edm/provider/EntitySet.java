package com.sap.core.odata.api.edm.provider;

public class EntitySet {

  private String name;
  private FullQualifiedName entityType;
  private Documentation documentation;
  private Annotations annotations;

  public EntitySet(String name, FullQualifiedName entityType, Documentation documentation, Annotations annotations) {
    this.name = name;
    this.entityType = entityType;
    this.documentation = documentation;
    this.annotations = annotations;
  }

  public String getName() {
    return name;
  }

  public FullQualifiedName getEntityType() {
    return entityType;
  }

  public Documentation getDocumentation() {
    return documentation;
  }

  public Annotations getAnnotations() {
    return annotations;
  }
}