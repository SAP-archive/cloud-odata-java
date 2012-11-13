package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.FullQualifiedName;

public class NavigationProperty {

  private String name;
  private FullQualifiedName relationship;
  private String fromRole;
  private String toRole;
  private Documentation documentation;
  private Annotations annotations;

  public String getName() {
    return name;
  }

  public FullQualifiedName getRelationship() {
    return relationship;
  }

  public String getFromRole() {
    return fromRole;
  }

  public String getToRole() {
    return toRole;
  }

  public Documentation getDocumentation() {
    return documentation;
  }

  public Annotations getAnnotations() {
    return annotations;
  }

  public NavigationProperty setName(String name) {
    this.name = name;
    return this;
  }

  public NavigationProperty setRelationship(FullQualifiedName relationship) {
    this.relationship = relationship;
    return this;
  }

  public NavigationProperty setFromRole(String fromRole) {
    this.fromRole = fromRole;
    return this;
  }

  public NavigationProperty setToRole(String toRole) {
    this.toRole = toRole;
    return this;
  }

  public NavigationProperty setDocumentation(Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  public NavigationProperty setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }
}