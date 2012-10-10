package org.odata4j.edm;

import org.odata4j.core.ImmutableList;

/**
 * A CSDL End element (as a child of the AssociationSet element)
 *
 * <p>The End element specifies one end of an association set. The AssociationSet element must contain two
 * End elements. The information contained in an End element is used in mapping an association set to a data
 * source.
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/bb896235.aspx">[msdn] End Element (CSDL)</a>
 */
public class EdmAssociationSetEnd extends EdmItem {

  private final EdmAssociationEnd role;
  private final EdmEntitySet entitySet;

  private EdmAssociationSetEnd(EdmAssociationEnd role, EdmEntitySet entitySet,
      EdmDocumentation doc, ImmutableList<EdmAnnotation<?>> annots, ImmutableList<EdmAnnotation<?>> annotElements) {
    super(doc, annots, annotElements);
    this.role = role;
    this.entitySet = entitySet;
  }

  public EdmAssociationEnd getRole() {
    return role;
  }

  public EdmEntitySet getEntitySet() {
    return entitySet;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  static Builder newBuilder(EdmAssociationSetEnd associationSetEnd, BuilderContext context) {
    return context.newBuilder(associationSetEnd, new Builder());
  }

  /** Mutable builder for {@link EdmAssociationEnd} objects. */
  public static class Builder extends EdmItem.Builder<EdmAssociationSetEnd, Builder> {

    private EdmAssociationEnd.Builder role;
    private String roleName;
    private EdmEntitySet.Builder entitySet;
    private String entitySetName;

    @Override
    Builder newBuilder(EdmAssociationSetEnd associationSetEnd, BuilderContext context) {
      this.role = EdmAssociationEnd.newBuilder(associationSetEnd.role, context);
      this.entitySet = EdmEntitySet.newBuilder(associationSetEnd.entitySet, context);
      return this;
    }

    public Builder setRole(EdmAssociationEnd.Builder role) {
      this.role = role;
      return this;
    }

    public Builder setEntitySet(EdmEntitySet.Builder entitySet) {
      this.entitySet = entitySet;
      return this;
    }

    public EdmAssociationSetEnd build() {
      return new EdmAssociationSetEnd(role.build(), entitySet.build(), getDocumentation(),
          ImmutableList.copyOf(getAnnotations()), ImmutableList.copyOf(getAnnotationElements()));
    }

    public String getRoleName() {
      return roleName;
    }

    public Builder setRoleName(String roleName) {
      this.roleName = roleName;
      return this;
    }

    public Builder setEntitySetName(String entitySetName) {
      this.entitySetName = entitySetName;
      return this;
    }

    public String getEntitySetName() {
      return entitySetName;
    }

  }

}
