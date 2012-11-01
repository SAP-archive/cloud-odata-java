package com.sap.core.odata.api.edm.provider;

public class NavigationProperty {

  private String name;
  private FullQualifiedName relationship;
  private String fromRole;
  private String toRole;
  private Documentation documentation;
  private Annotations annotations;

  public NavigationProperty(String name, FullQualifiedName relationship, String fromRole, String toRole, Documentation documentation, Annotations annotations) {
    this.name = name;
    this.relationship = relationship;
    this.fromRole = fromRole;
    this.toRole = toRole;
    this.documentation = documentation;
    this.annotations = annotations;
  }

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
}