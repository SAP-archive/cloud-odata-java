package org.odata4j.edm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.odata4j.core.ImmutableList;

/**
 * A CSDL Schema element.
 *
 * <p>The Schema element is the root element of a conceptual model definition. It contains definitions
 * for the objects, functions, and containers that make up a conceptual model.
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/bb399276.aspx">[msdn] Schema Element (CSDL)</a>
 */
public class EdmSchema extends EdmItem {

  private final String namespace;
  private final String alias;
  private final ImmutableList<EdmEntityType> entityTypes;
  private final ImmutableList<EdmComplexType> complexTypes;
  private final ImmutableList<EdmAssociation> associations;
  private final ImmutableList<EdmEntityContainer> entityContainers;

  private EdmSchema(String namespace, String alias, ImmutableList<EdmEntityType> entityTypes,
      ImmutableList<EdmComplexType> complexTypes, ImmutableList<EdmAssociation> associations,
      ImmutableList<EdmEntityContainer> entityContainers, EdmDocumentation doc,
      ImmutableList<EdmAnnotation<?>> annots, ImmutableList<EdmAnnotation<?>> annotElements) {
    super(doc, annots, annotElements);
    this.namespace = namespace;
    this.alias = alias;
    this.entityTypes = entityTypes;
    this.complexTypes = complexTypes;
    this.associations = associations;
    this.entityContainers = entityContainers;
  }

  public String getNamespace() {
    return namespace;
  }

  public String getAlias() {
    return alias;
  }

  public List<EdmEntityType> getEntityTypes() {
    return entityTypes;
  }

  public List<EdmComplexType> getComplexTypes() {
    return complexTypes;
  }

  public List<EdmAssociation> getAssociations() {
    return associations;
  }

  public List<EdmEntityContainer> getEntityContainers() {
    return entityContainers;
  }

  public EdmEntityContainer findEntityContainer(String name) {
    for (EdmEntityContainer ec : entityContainers) {
      if (ec.getName().equals(name)) {
        return ec;
      }
    }
    return null;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  static Builder newBuilder(EdmSchema schema, BuilderContext context) {
    return context.newBuilder(schema, new Builder());
  }

  /** Mutable builder for {@link EdmSchema} objects. */
  public static class Builder extends EdmItem.Builder<EdmSchema, Builder> {

    private String namespace;
    private String alias;
    private final List<EdmEntityType.Builder> entityTypes = new ArrayList<EdmEntityType.Builder>();
    private final List<EdmComplexType.Builder> complexTypes = new ArrayList<EdmComplexType.Builder>();
    private final List<EdmAssociation.Builder> associations = new ArrayList<EdmAssociation.Builder>();
    private final List<EdmEntityContainer.Builder> entityContainers = new ArrayList<EdmEntityContainer.Builder>();

    @Override
    Builder newBuilder(EdmSchema schema, BuilderContext context) {
      List<EdmEntityContainer.Builder> entityContainers = new ArrayList<EdmEntityContainer.Builder>();
      for (EdmEntityContainer entityContainer : schema.entityContainers)
        entityContainers.add(EdmEntityContainer.newBuilder(entityContainer, context));

      List<EdmComplexType.Builder> complexTypes = new ArrayList<EdmComplexType.Builder>();
      for (EdmComplexType complexType : schema.complexTypes)
        complexTypes.add(EdmComplexType.newBuilder(complexType, context));

      List<EdmEntityType.Builder> entityTypes = new ArrayList<EdmEntityType.Builder>();
      for (EdmEntityType entityType : schema.entityTypes)
        entityTypes.add(EdmEntityType.newBuilder(entityType, context));

      List<EdmAssociation.Builder> associations = new ArrayList<EdmAssociation.Builder>();
      for (EdmAssociation association : schema.associations)
        associations.add(EdmAssociation.newBuilder(association, context));

      return new Builder()
          .setNamespace(schema.namespace)
          .setAlias(schema.alias)
          .addEntityTypes(entityTypes)
          .addComplexTypes(complexTypes)
          .addAssociations(associations)
          .addEntityContainers(entityContainers);

    }

    public EdmSchema build() {
      List<EdmEntityContainer> entityContainers = new ArrayList<EdmEntityContainer>(this.entityContainers.size());
      for (EdmEntityContainer.Builder entityContainer : this.entityContainers)
        entityContainers.add(entityContainer.build());

      List<EdmComplexType> complexTypes = new ArrayList<EdmComplexType>(this.complexTypes.size());
      for (EdmComplexType.Builder complexType : this.complexTypes)
        complexTypes.add(complexType.build());

      List<EdmEntityType> entityTypes = new ArrayList<EdmEntityType>(this.entityTypes.size());
      for (EdmEntityType.Builder entityType : this.entityTypes)
        entityTypes.add(entityType.build());

      List<EdmAssociation> associations = new ArrayList<EdmAssociation>(this.associations.size());
      for (EdmAssociation.Builder association : this.associations)
        associations.add(association.build());

      return new EdmSchema(namespace, alias,
          ImmutableList.copyOf(entityTypes),
          ImmutableList.copyOf(complexTypes),
          ImmutableList.copyOf(associations),
          ImmutableList.copyOf(entityContainers),
          getDocumentation(),
          ImmutableList.copyOf(getAnnotations()),
          ImmutableList.copyOf(getAnnotationElements()));
    }

    public Builder setNamespace(String namespace) {
      this.namespace = namespace;
      return this;
    }

    public Builder setAlias(String alias) {
      this.alias = alias;
      return this;
    }

    public Builder addEntityTypes(Collection<EdmEntityType.Builder> entityTypes) {
      this.entityTypes.addAll(entityTypes);
      return this;
    }

    public Builder addEntityTypes(EdmEntityType.Builder... entityTypes) {
      this.entityTypes.addAll(Arrays.asList(entityTypes));
      return this;
    }

    public Builder addComplexTypes(Collection<EdmComplexType.Builder> complexTypes) {
      this.complexTypes.addAll(complexTypes);
      return this;
    }

    public Builder addAssociations(Collection<EdmAssociation.Builder> associations) {
      this.associations.addAll(associations);
      return this;
    }

    public Builder addEntityContainers(EdmEntityContainer.Builder... entityContainers) {
      this.entityContainers.addAll(Arrays.asList(entityContainers));
      return this;
    }

    public Builder addEntityContainers(Collection<EdmEntityContainer.Builder> entityContainers) {
      this.entityContainers.addAll(entityContainers);
      return this;
    }

    public Iterable<EdmComplexType.Builder> getComplexTypes() {
      return complexTypes;
    }

    public List<EdmEntityType.Builder> getEntityTypes() {
      return entityTypes;
    }

    public List<EdmAssociation.Builder> getAssociations() {
      return associations;
    }

    public List<EdmEntityContainer.Builder> getEntityContainers() {
      return entityContainers;
    }

    public String getNamespace() {
      return namespace;
    }

    public EdmEntityContainer.Builder findEntityContainer(String name) {
      // TODO share or remove
      for (EdmEntityContainer.Builder ec : entityContainers) {
        if (ec.getName().equals(name)) {
          return ec;
        }
      }
      return null;
    }
  }

}
