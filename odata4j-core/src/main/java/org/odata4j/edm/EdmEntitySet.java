package org.odata4j.edm;

import org.core4j.Func;
import org.odata4j.core.ImmutableList;
import org.odata4j.core.Named;

/**
 * A CSDL EntitySet element.
 *
 * <p>The EntitySet element in conceptual schema definition language is a logical container for instances
 * of an entity type and instances of any type that is derived from that entity type. The relationship
 * between an entity type and an entity set is analogous to the relationship between a row and a table in
 * a relational database. Like a row, an entity type defines a set of related data, and, like a table,
 * an entity set contains instances of that definition. An entity set provides a construct for grouping
 * entity type instances so that they can be mapped to related data structures in a data source.
 *
 * <p>More than one entity set for a particular entity type may be defined.
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/bb387139.aspx">[msdn] EntitySet Element (CSDL)</a>
 */
public class EdmEntitySet extends EdmItem implements Named {

  private final String name;
  private final Func<EdmEntityType> type;

  private EdmEntitySet(String name, Func<EdmEntityType> type, EdmDocumentation doc,
      ImmutableList<EdmAnnotation<?>> annots, ImmutableList<EdmAnnotation<?>> annotElements) {
    super(doc, annots, annotElements);
    this.name = name;
    this.type = type;
  }

  /**
   * The name of the entity set.
   */
  public String getName() {
    return name;
  }

  /**
   * The entity type for which the entity set contains instances.
   */
  public EdmEntityType getType() {
    return type == null ? null : type.apply();
  }

  @Override
  public String toString() {
    return String.format("EdmEntitySet[%s,%s]", name, type);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  static Builder newBuilder(EdmEntitySet entitySet, BuilderContext context) {
    return context.newBuilder(entitySet, new Builder());
  }

  /** Mutable builder for {@link EdmEntitySet} objects. */
  public static class Builder extends EdmItem.Builder<EdmEntitySet, Builder> implements Named {

    protected String name;
    protected String entityTypeName;
    protected EdmEntityType.Builder entityType;
    private EdmEntitySet builtEntitySet;

    @Override
    Builder newBuilder(EdmEntitySet entitySet, BuilderContext context) {
      this.name = entitySet.name;
      EdmEntityType et = entitySet.type.apply();
      this.entityType = EdmEntityType.newBuilder(et, context);
      return this;
    }

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setEntityType(EdmEntityType.Builder entityType) {
      this.entityType = entityType;
      return this;
    }

    public EdmEntitySet build() {
      if (builtEntitySet == null) {
        builtEntitySet = new EdmEntitySet(name,
            entityType == null ? null : entityType.builtFunc(), getDocumentation(),
            ImmutableList.copyOf(getAnnotations()), ImmutableList.copyOf(getAnnotationElements()));
      }
      return builtEntitySet;
    }

    @Override
    public String getName() {
      return name;
    }

    public String getEntityTypeName() {
      return entityTypeName;
    }

    public Builder setEntityTypeName(String entityTypeName) {
      this.entityTypeName = entityTypeName;
      return this;
    }

  }
}
