package org.odata4j.edm;

import org.odata4j.core.ImmutableList;

/**
 * A CSDL NavigationProperty element.
 *
 * <p>A NavigationProperty element defines a navigation property, which provides a reference to the other end of an
 * association. Unlike properties defined with the Property element, navigation properties do not define the shape
 * and characteristics of data. They provide a way to navigate an association between two entity types.
 *
 * <p>Note that navigation properties are optional on both entity types at the ends of an association. If you define
 * a navigation property on one entity type at the end of an association, you do not have to define a navigation property
 * on the entity type at the other end of the association.
 *
 * <p>The data type returned by a navigation property is determined by the multiplicity of its remote association end.
 * For example, suppose a navigation property, OrdersNavProp, exists on a Customer entity type and navigates a one-to-many
 * association between Customer and Order. Because the remote association end for the navigation property has multiplicity
 * many (*), its data type is a collection (of Order). Similarly, if a navigation property, CustomerNavProp, exists on the
 * Order entity type, its data type would be Customer since the multiplicity of the remote end is one (1).
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/bb387104.aspx">[msdn] NavigationProperty Element (CSDL)</a>
 */
public class EdmNavigationProperty extends EdmPropertyBase {

  private final EdmAssociation relationship;
  private final EdmAssociationEnd fromRole;
  private final EdmAssociationEnd toRole;

  private EdmNavigationProperty(EdmDocumentation documentation, ImmutableList<EdmAnnotation<?>> annotations, ImmutableList<EdmAnnotation<?>> annotElements,
      String name,
      EdmAssociation relationship,
      EdmAssociationEnd fromRole,
      EdmAssociationEnd toRole) {
    super(documentation, annotations, annotElements, name);
    this.relationship = relationship;
    this.fromRole = fromRole;
    this.toRole = toRole;
  }

  public EdmAssociation getRelationship() {
    return relationship;
  }

  public EdmAssociationEnd getFromRole() {
    return fromRole;
  }

  public EdmAssociationEnd getToRole() {
    return toRole;
  }

  @Override
  public String toString() {
    return String.format("EdmNavigationProperty[%s,rel=%s,from=%s,to=%s]", getName(), relationship, fromRole, toRole);
  }

  public static Builder newBuilder(String name) {
    return new Builder(name);
  }

  static Builder newBuilder(EdmNavigationProperty navigationProperty, BuilderContext context) {
    return context.newBuilder(navigationProperty, new Builder(navigationProperty.getName()));
  }

  /** Mutable builder for {@link EdmNavigationProperty} objects. */
  public static class Builder extends EdmPropertyBase.Builder<EdmNavigationProperty, Builder> {

    private EdmAssociation.Builder relationship;
    private String relationshipName;
    private EdmAssociationEnd.Builder fromRole;
    private String fromRoleName;
    private EdmAssociationEnd.Builder toRole;
    private String toRoleName;

    private Builder(String name) {
      super(name);
    }

    @Override
    Builder newBuilder(EdmNavigationProperty navigationProperty, BuilderContext context) {
      this.relationship = EdmAssociation.newBuilder(navigationProperty.relationship, context);
      this.fromRole = EdmAssociationEnd.newBuilder(navigationProperty.fromRole, context);
      this.toRole = EdmAssociationEnd.newBuilder(navigationProperty.toRole, context);
      return this;
    }

    public Builder setRelationship(EdmAssociation.Builder relationship) {
      this.relationship = relationship;
      return this;
    }

    public Builder setFromTo(EdmAssociationEnd.Builder fromRole, EdmAssociationEnd.Builder toRole) {
      this.fromRole = fromRole;
      this.toRole = toRole;
      return this;
    }

    public EdmNavigationProperty build() {
      return new EdmNavigationProperty(getDocumentation(), ImmutableList.copyOf(getAnnotations()),
          ImmutableList.copyOf(getAnnotationElements()), getName(), relationship.build(), fromRole.build(), toRole.build());
    }

    public String getRelationshipName() {
      return relationshipName;
    }

    public String getFromRoleName() {
      return fromRoleName;
    }

    public String getToRoleName() {
      return toRoleName;
    }

    public Builder setRelationshipName(String relationshipName) {
      this.relationshipName = relationshipName;
      return this;
    }

    public Builder setFromToName(String fromRoleName, String toRoleName) {
      this.fromRoleName = fromRoleName;
      this.toRoleName = toRoleName;
      return this;
    }

  }

}
