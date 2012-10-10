package org.odata4j.edm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.core4j.Enumerable;
import org.core4j.Func;
import org.core4j.Func1;
import org.core4j.Predicate1;
import org.odata4j.core.ImmutableList;
import org.odata4j.core.Named;
import org.odata4j.core.OPredicates;

/**
 * A CSDL EntityType element.
 *
 * <p>The EntityType element represents the structure of a top-level concept, such as a customer or order,
 * in a conceptual model. An entity type is a template for instances of entity types in an application.
 * Each template contains the following information:
 * <li>A unique name. (Required.)
 * <li>An entity key that is defined by one or more properties. (Required.)
 * <li>Properties for containing data. (Optional.)
 * <li>Navigation properties that allow for navigation from one end of an association to the other end. (Optional.)
 *
 * <p>In an application, an instance of an entity type represents a specific object (such as a specific customer or order).
 * Each instance of an entity type must have a unique entity key within an entity set.
 *
 * <p>Two entity type instances are considered equal only if they are of the same type and the values of their entity keys are the same.
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/bb399206.aspx">[msdn] EntityType Element (CSDL)</a>
 */
public class EdmEntityType extends EdmStructuralType {

  private final String alias;
  private final Boolean hasStream;
  private final Boolean openType;
  private final List<String> keys;
  private final List<EdmNavigationProperty> navigationProperties;

  private EdmEntityType(String namespace, String alias, String name, Boolean hasStream, Boolean openType,
      ImmutableList<String> keys, EdmEntityType baseType, List<EdmProperty.Builder> properties,
      ImmutableList<EdmNavigationProperty> navigationProperties, EdmDocumentation doc,
      ImmutableList<EdmAnnotation<?>> annotations, ImmutableList<EdmAnnotation<?>> annotElements, Boolean isAbstract) {
    super(baseType, namespace, name, properties, doc, annotations, annotElements, isAbstract);
    this.alias = alias;
    this.hasStream = hasStream;
    this.openType = openType;
    this.keys = (keys == null || keys.isEmpty()) ?
        (baseType == null ? findConventionalKeys() : null) :
        keys;

    if (baseType == null && this.keys == null)
      throw new IllegalArgumentException("Root types must have keys");
    if (baseType != null && this.keys != null)
      throw new IllegalArgumentException("Keys on root types only");

    this.navigationProperties = navigationProperties;
  }

  private List<String> findConventionalKeys() {
    for (EdmProperty prop : getProperties()) {
      if (prop.getName().equalsIgnoreCase("Id") && prop.getType().isSimple() && !prop.isNullable()) {
        return Enumerable.create(prop.getName()).toList();
      }
    }
    return null;
  }

  public String getAlias() {
    return alias;
  }

  public Boolean getHasStream() {
    return hasStream;
  }

  public Boolean getOpenType() {
    return openType;
  }

  public String getFQAliasName() {
    return alias == null ? null : (alias + "." + getName());
  }

  @Override
  public String toString() {
    return String.format("EdmEntityType[%s.%s,alias=%s]", getNamespace(), getName(), alias);
  }

  /**
   * Finds a navigation property by name, searching up the type hierarchy if necessary.
   */
  public EdmNavigationProperty findNavigationProperty(String name) {
    return getNavigationProperties().firstOrNull(OPredicates.nameEquals(EdmNavigationProperty.class, name));
  }

  /**
   * Gets the navigation properties defined for this entity type <i>not including</i> inherited properties.
   */
  public Enumerable<EdmNavigationProperty> getDeclaredNavigationProperties() {
    return Enumerable.create(navigationProperties);
  }

  /**
   * Finds a navigation property by name on this entity type <i>not including</i> inherited properties.
   */
  public EdmNavigationProperty findDeclaredNavigationProperty(String name) {
    return getDeclaredNavigationProperties().firstOrNull(OPredicates.nameEquals(EdmNavigationProperty.class, name));
  }

  /**
   * Gets the navigation properties defined for this entity type <i>including</i> inherited properties.
   */
  public Enumerable<EdmNavigationProperty> getNavigationProperties() {
    return isRootType()
        ? getDeclaredNavigationProperties()
        : getBaseType().getNavigationProperties().union(getDeclaredNavigationProperties());
  }

  /**
   * Gets the keys for this EdmEntityType.  Keys are defined only in a root types.
   */
  public List<String> getKeys() {
    return isRootType() ? keys : getBaseType().getKeys();
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static Builder newBuilder(EdmEntityType entityType, BuilderContext context) {
    return context.newBuilder(entityType, new Builder());
  }

  /** Mutable builder for {@link EdmEntityType} objects. */
  public static class Builder extends EdmStructuralType.Builder<EdmEntityType, Builder> implements Named {

    private String alias;
    private Boolean hasStream;
    private Boolean openType;
    private final List<String> keys = new ArrayList<String>();
    private final List<EdmNavigationProperty.Builder> navigationProperties = new ArrayList<EdmNavigationProperty.Builder>();
    private EdmEntityType.Builder baseTypeBuilder;
    private String baseTypeNameFQ;

    @Override
    Builder newBuilder(EdmEntityType entityType, BuilderContext context) {
      fillBuilder(entityType, context);
      context.register(entityType, this);
      this.alias = entityType.alias;
      this.hasStream = entityType.hasStream;
      this.openType = entityType.openType;
      if (entityType.keys != null) {
        // subtypes don't have keys!
        this.keys.addAll(entityType.keys);
      }

      if (entityType.getBaseType() != null) {
        baseTypeBuilder = EdmEntityType.newBuilder(entityType.getBaseType(), context);
      }

      for (EdmNavigationProperty navigationProperty : entityType.navigationProperties)
        this.navigationProperties.add(EdmNavigationProperty.newBuilder(navigationProperty, context));
      return this;
    }

    @Override
    public EdmEntityType build() {
      return (EdmEntityType) _build();
    }

    @Override
    protected EdmEntityType buildImpl() {
      List<EdmNavigationProperty> builtNavProps = new ArrayList<EdmNavigationProperty>();
      for (EdmNavigationProperty.Builder navigationProperty : this.navigationProperties) {
        builtNavProps.add(navigationProperty.build());
      }
      return new EdmEntityType(namespace, alias, name, hasStream, openType, ImmutableList.copyOf(keys),
          (EdmEntityType) (this.baseTypeBuilder != null ? this.baseTypeBuilder.build() : null),
          properties, ImmutableList.copyOf(builtNavProps), getDocumentation(),
          ImmutableList.copyOf(getAnnotations()), ImmutableList.copyOf(getAnnotationElements()), isAbstract);
    }

    public Builder addNavigationProperties(EdmNavigationProperty.Builder... navigationProperties) {
      return addNavigationProperties(Arrays.asList(navigationProperties));
    }

    public Builder addNavigationProperties(List<EdmNavigationProperty.Builder> navProperties) {
      this.navigationProperties.addAll(navProperties);
      return this;
    }

    public Builder addKeys(List<String> keys) {
      this.keys.addAll(keys);
      return this;
    }

    public Builder addKeys(String... keys) {
      return addKeys(Arrays.asList(keys));
    }

    @Override
    public String getName() {
      return name;
    }

    public Builder setBaseType(EdmEntityType.Builder baseType) {
      this.baseTypeBuilder = baseType;
      return this;
    }

    public Builder setBaseType(String baseTypeName) {
      this.baseTypeNameFQ = baseTypeName;
      return this;
    }

    public Builder setAlias(String alias) {
      this.alias = alias;
      return this;
    }

    public Builder setHasStream(Boolean hasStream) {
      this.hasStream = hasStream;
      return this;
    }

    public Builder setOpenType(Boolean openType) {
      this.openType = openType;
      return this;
    }

    public String getAlias() {
      return alias;
    }

    public String getFQAliasName() {
      // TODO share or remove
      return alias == null ? null : (alias + "." + getName());
    }

    public String getFQBaseTypeName() {
      return baseTypeNameFQ != null
          ? baseTypeNameFQ
          : (baseType != null ? baseType.getFullyQualifiedTypeName() : null);
    }

    public List<EdmNavigationProperty.Builder> getNavigationProperties() {
      return navigationProperties;
    }

    public Func<EdmEntityType> builtFunc() {
      return new Func<EdmEntityType>() {
        @Override
        public EdmEntityType apply() {
          return (EdmEntityType) build();
        }
      };
    }

    public static Func1<EdmEntityType.Builder, String> func1_getFullyQualifiedTypeName() {
      return new Func1<EdmEntityType.Builder, String>() {
        @Override
        public String apply(Builder input) {
          return input.getFullyQualifiedTypeName();
        }
      };
    }

    public static Func1<EdmEntityType.Builder, String> func1_getFQAliasName() {
      return new Func1<EdmEntityType.Builder, String>() {
        @Override
        public String apply(Builder input) {
          return input.getFQAliasName();
        }
      };
    }

    public static Predicate1<Builder> pred1_hasAlias() {
      return new Predicate1<EdmEntityType.Builder>() {
        @Override
        public boolean apply(Builder input) {
          return input.getAlias() != null;
        }
      };
    }

  }

}
