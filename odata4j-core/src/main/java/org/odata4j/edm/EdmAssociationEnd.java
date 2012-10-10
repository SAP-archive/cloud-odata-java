package org.odata4j.edm;

import org.core4j.Func;
import org.odata4j.core.ImmutableList;

/**
 * A CSDL End element (as a child of the Association element)
 *
 * <p>An End element (as a child of the Association element) identifies the entity type on one end of
 * an association and the number of entity type instances that can exist at that end of an association.
 * Association ends are defined as part of an association; an association must have exactly two association
 * ends. Entity type instances at one end of an association can be accessed through navigation properties
 * or foreign keys if they are exposed on an entity type.
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/bb896235.aspx">[msdn] End Element (CSDL)</a>
 */
public class EdmAssociationEnd extends EdmItem {

  private final String role;
  private final Func<EdmEntityType> type;
  private final EdmMultiplicity multiplicity;
  private final EdmOnDeleteAction onDeleteAction;

  private EdmAssociationEnd(String role, Func<EdmEntityType> type, EdmMultiplicity multiplicity,
      EdmOnDeleteAction onDeleteAction, EdmDocumentation doc, ImmutableList<EdmAnnotation<?>> annots,
      ImmutableList<EdmAnnotation<?>> annotElements) {
    super(doc, annots, annotElements);
    this.role = role;
    this.type = type;
    this.multiplicity = multiplicity;
    this.onDeleteAction = onDeleteAction;
  }

  public String getRole() {
    return role;
  }

  public EdmEntityType getType() {
    return type.apply();
  }

  public EdmMultiplicity getMultiplicity() {
    return multiplicity;
  }

  public EdmOnDeleteAction getOnDeleteAction() {
    return onDeleteAction;
  }

  @Override
  public String toString() {
    return String.format("EdmAssociationEnd[%s,%s,%s,%s]", role, type, multiplicity, onDeleteAction);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  static Builder newBuilder(EdmAssociationEnd associationEnd, BuilderContext context) {
    return context.newBuilder(associationEnd, new Builder());
  }

  /** Mutable builder for {@link EdmAssociationEnd} objects. */
  public static class Builder extends EdmItem.Builder<EdmAssociationEnd, Builder> {

    private String role;
    private EdmEntityType.Builder type;
    private String typeName;
    private EdmMultiplicity multiplicity;
    private EdmOnDeleteAction onDeleteAction;

    @Override
    Builder newBuilder(EdmAssociationEnd associationEnd, BuilderContext context) {
      this.role = associationEnd.role;
      this.type = EdmEntityType.newBuilder(associationEnd.getType(), context);
      this.multiplicity = associationEnd.multiplicity;
      this.onDeleteAction = associationEnd.onDeleteAction;
      return this;
    }

    public EdmAssociationEnd build() {
      return new EdmAssociationEnd(role, type == null ? null : type.builtFunc(), multiplicity,
          onDeleteAction, getDocumentation(), ImmutableList.copyOf(getAnnotations()),
          ImmutableList.copyOf(getAnnotationElements()));
    }

    public Builder setRole(String role) {
      this.role = role;
      return this;
    }

    public Builder setType(EdmEntityType.Builder type) {
      this.type = type;
      return this;
    }

    public Builder setTypeName(String typeName) {
      this.typeName = typeName;
      return this;
    }

    public Builder setMultiplicity(EdmMultiplicity multiplicity) {
      this.multiplicity = multiplicity;
      return this;
    }

    public Builder setOnDeleteAction(EdmOnDeleteAction onDeleteAction) {
      this.onDeleteAction = onDeleteAction;
      return this;
    }

    public EdmEntityType.Builder getType() {
      return type;
    }

    public String getTypeName() {
      return typeName;
    }

    public String getRole() {
      return role;
    }

  }

}
