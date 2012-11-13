package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.FullQualifiedName;

public class EntitySet {

  private String name;
  private FullQualifiedName entityType;
  private Documentation documentation;
  private Annotations annotations;

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

  public EntitySet setName(String name) {
    this.name = name;
    return this;
  }

  public EntitySet setEntityType(FullQualifiedName entityType) {
    this.entityType = entityType;
    return this;
  }

  public EntitySet setDocumentation(Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  public EntitySet setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }
  
  
}