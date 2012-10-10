package org.odata4j.edm;

import org.odata4j.edm.EdmProperty.CollectionKind;

/**
 * A CSDL CollectionType element.
 *
 * <p>The CollectionType element in conceptual schema definition language (CSDL) specifies that a
 * function parameter or function return type is a collection.
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/ee476606.aspx">[msdn] CollectionType Element (CSDL)</a>
 */
public class EdmCollectionType extends EdmNonSimpleType {

  private final EdmType itemType;
  private final CollectionKind collectionKind;

  public EdmCollectionType(CollectionKind kind, EdmType itemType) {
    super(getCollectionTypeString(kind, itemType));
    if (itemType == null) throw new IllegalArgumentException("itemType cannot be null");
    this.itemType = itemType;
    this.collectionKind = kind;
  }

  public CollectionKind getCollectionKind() {
    return this.collectionKind;
  }

  public EdmType getItemType() {
    return itemType;
  }

  public static String getCollectionTypeString(CollectionKind kind, EdmType itemType) {
    return kind.toString() + "(" + itemType.getFullyQualifiedTypeName() + ")";
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  /** Mutable builder for {@link EdmCollectionType} objects. */
  public static class Builder extends EdmType.Builder<EdmCollectionType, Builder> {

    private CollectionKind kind;
    private EdmType.Builder<?, ?> collectionType;

    @Override
    Builder newBuilder(EdmCollectionType type, BuilderContext context) {
      this.kind = type.getCollectionKind();
      return this;
    }

    public Builder setKind(CollectionKind kind) {
      this.kind = kind;
      return this;
    }

    public Builder setCollectionType(EdmType.Builder<?, ?> collectionType) {
      this.collectionType = collectionType;
      return this;
    }

    @Override
    public EdmCollectionType build() {
      return (EdmCollectionType) _build();
    }

    @Override
    protected EdmType buildImpl() {
      return new EdmCollectionType(kind, collectionType.build());
    }

  }

}
