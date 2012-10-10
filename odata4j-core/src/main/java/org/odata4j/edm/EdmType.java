package org.odata4j.edm;

import java.util.Map;

import org.core4j.Enumerable;
import org.core4j.Func1;
import org.odata4j.core.ImmutableList;
import org.odata4j.core.OCollection;
import org.odata4j.core.OComplexObject;
import org.odata4j.core.OEntity;
import org.odata4j.core.OObject;
import org.odata4j.exceptions.NotImplementedException;

/**
 * A type in the EDM type system.
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/bb399591.aspx">[msdn] Types (Metadata)</a>
 */
public abstract class EdmType extends EdmItem {

  private static class LazyInit {

    private static final Map<String, EdmSimpleType<?>> POOL = Enumerable.create(EdmSimpleType.ALL).toMap(
        new Func1<EdmSimpleType<?>, String>() {
          @Override
          public String apply(EdmSimpleType<?> t) {
            return t.getFullyQualifiedTypeName();
          }
        });
  }

  private final String fullyQualifiedTypeName;

  protected EdmType(String fullyQualifiedTypeName) {
    this(fullyQualifiedTypeName, null, null, null);
  }

  protected EdmType(String fullyQualifiedTypeName, EdmDocumentation documentation, ImmutableList<EdmAnnotation<?>> annotations,
      ImmutableList<EdmAnnotation<?>> annotationElements) {
    super(documentation, annotations, annotationElements);
    this.fullyQualifiedTypeName = fullyQualifiedTypeName;
  }

  /**
   * Gets an edm-type for a given type name.
   *
   * @param fullyQualifiedTypeName  the fully-qualified type name
   * @return the edm-type
   */
  public static EdmSimpleType<?> getSimple(String fullyQualifiedTypeName) {
    if (fullyQualifiedTypeName == null)
      return null;
    EdmSimpleType<?> simpleType = LazyInit.POOL.get(fullyQualifiedTypeName);
    if (simpleType == null && !fullyQualifiedTypeName.contains(".")) // allow "string, Int32" for old dallas service functions
      for (EdmSimpleType<?> simpleTypeInPool : LazyInit.POOL.values())
        if (simpleTypeInPool.getFullyQualifiedTypeName().equalsIgnoreCase("Edm." + fullyQualifiedTypeName))
          return simpleTypeInPool;
    return simpleType;
  }

  /** Gets the corresponding instance type of a given edm type. e.g. {@code OEntity} for {@code EdmEntityType} */
  public static Class<? extends OObject> getInstanceType(EdmType edmType) {
    if (edmType instanceof EdmComplexType) {
      return OComplexObject.class;
    } else if (edmType instanceof EdmCollectionType) {
      return OCollection.class;
    } else if (edmType instanceof EdmEntityType) {
      return OEntity.class;
    } else {
      throw new NotImplementedException("Unable to determine instance type for edm type: " + edmType.getFullyQualifiedTypeName());
    }
  }

  /**
   * Gets the fully-qualified type name for this edm-type.
   */
  public String getFullyQualifiedTypeName() {
    return this.fullyQualifiedTypeName;
  }

  @Override
  public String toString() {
    return String.format("%s[%s]", getClass().getSimpleName(), getFullyQualifiedTypeName());
  }

  @Override
  public int hashCode() {
    return this.fullyQualifiedTypeName.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof EdmType && ((EdmType) other).fullyQualifiedTypeName.equals(this.fullyQualifiedTypeName);
  }

  public abstract boolean isSimple();

  /** Mutable builder for {@link EdmType} objects. */
  public abstract static class Builder<T, TBuilder> extends EdmItem.Builder<T, TBuilder> {

    private EdmType builtType = null;

    public Builder() {}

    public Builder(EdmType type) {
      this.builtType = type;
    }

    public abstract EdmType build();

    protected final EdmType _build() {
      if (builtType == null) {
        builtType = buildImpl();
      }
      return builtType;
    }

    protected abstract EdmType buildImpl();

  }

  @SuppressWarnings("rawtypes")
  public static DeferredBuilder<?, ?> newDeferredBuilder(String fqTypeName, EdmDataServices.Builder dataServices) {
    return new DeferredBuilder(fqTypeName, dataServices);
  }

  /** Mutable builder for {@link EdmType} objects with deferred resolution. */
  public static class DeferredBuilder<T, TBuilder> extends Builder<T, TBuilder> {

    private final String fqTypeName;
    private final EdmDataServices.Builder dataServices;

    private DeferredBuilder(String fqTypeName, EdmDataServices.Builder dataServices) {
      this.fqTypeName = fqTypeName;
      this.dataServices = dataServices;
    }

    @Override
    public EdmType build() {
      return _build();
    }

    @Override
    protected EdmType buildImpl() {
      Builder<?, ?> b = dataServices.resolveType(fqTypeName);
      if (b == null) {
        throw new RuntimeException("Edm-type not found: " + fqTypeName);
      }
      return b.build();
    }

    @Override
    TBuilder newBuilder(Object item, BuilderContext context) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

  }

}
