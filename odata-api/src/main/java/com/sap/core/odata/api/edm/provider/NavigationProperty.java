package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.FullQualifiedName;

/**
 * @author SAP AG
 *
 */
public class NavigationProperty {

  private String name;
  private FullQualifiedName relationship;
  private String fromRole;
  private String toRole;
  private Documentation documentation;
  private Annotations annotations;

  /**
   * @return <b>String</b> name of this navigation property
   */
  public String getName() {
    return name;
  }

  /**
   * @return {@link FullQualifiedName} of the relationship
   */
  public FullQualifiedName getRelationship() {
    return relationship;
  }

  /**
   * @return <b>String</b> name of the role this navigation is comming from
   */
  public String getFromRole() {
    return fromRole;
  }

  /**
   * @return <b>String</b> name of the role this navigation is going to
   */
  public String getToRole() {
    return toRole;
  }

  /**
   * @return {@link Documentation} documentation
   */
  public Documentation getDocumentation() {
    return documentation;
  }

  /**
   * @return {@link Annotations} annotations
   */
  public Annotations getAnnotations() {
    return annotations;
  }

  /**
   * MANDATORY
   * <p>Sets the name of this {@link NavigationProperty}
   * @param name
   * @return {@link NavigationProperty} for method chaining
   */
  public NavigationProperty setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * MANDATORY
   * <p>Sets the {@link FullQualifiedName} for the relationship of this {@link NavigationProperty}
   * @param relationship
   * @return {@link NavigationProperty} for method chaining
   */
  public NavigationProperty setRelationship(FullQualifiedName relationship) {
    this.relationship = relationship;
    return this;
  }

  /**
   * MANDATORY
   * <p>Sets the role this {@link NavigationProperty} is comming from
   * @param fromRole
   * @return {@link NavigationProperty} for method chaining
   */
  public NavigationProperty setFromRole(String fromRole) {
    this.fromRole = fromRole;
    return this;
  }

  /**
   * MANDATORY
   * <p>Sets the role this {@link NavigationProperty} is going to
   * @param toRole
   * @return {@link NavigationProperty} for method chaining
   */
  public NavigationProperty setToRole(String toRole) {
    this.toRole = toRole;
    return this;
  }

  /**
   * Sets the {@link Documentation} for this {@link NavigationProperty}
   * @param documentation
   * @return {@link NavigationProperty} for method chaining
   */
  public NavigationProperty setDocumentation(Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  /**
   * Sets the {@link Annotations} for this {@link NavigationProperty}
   * @param annotations
   * @return {@link NavigationProperty} for method chaining
   */
  public NavigationProperty setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }
}