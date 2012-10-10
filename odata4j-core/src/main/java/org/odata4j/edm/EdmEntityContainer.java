package org.odata4j.edm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.odata4j.core.ImmutableList;

/**
 * A CSDL EntityContainer element.
 *
 * <p>The EntityContainer element in conceptual schema definition language (CSDL) is a logical container for
 * entity sets, association sets, and function imports.
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/bb399169.aspx">[msdn] EntityContainer Element (CSDL)</a>
 */
public class EdmEntityContainer extends EdmItem {

  private final String name;
  private final boolean isDefault;
  private final Boolean lazyLoadingEnabled;
  private final String extendz;
  private final ImmutableList<EdmEntitySet> entitySets;
  private final ImmutableList<EdmAssociationSet> associationSets;
  private final ImmutableList<EdmFunctionImport> functionImports;

  private EdmEntityContainer(String name, boolean isDefault, Boolean lazyLoadingEnabled, String extendz,
      ImmutableList<EdmEntitySet> entitySets, ImmutableList<EdmAssociationSet> associationSets,
      ImmutableList<EdmFunctionImport> functionImports, EdmDocumentation doc, ImmutableList<EdmAnnotation<?>> annots,
      ImmutableList<EdmAnnotation<?>> annotElements) {
    super(doc, annots, annotElements);
    this.name = name;
    this.isDefault = isDefault;
    this.lazyLoadingEnabled = lazyLoadingEnabled;
    this.extendz = extendz;
    this.entitySets = entitySets;
    this.associationSets = associationSets;
    this.functionImports = functionImports;
  }

  public String getName() {
    return name;
  }

  public boolean isDefault() {
    return isDefault;
  }

  public Boolean getLazyLoadingEnabled() {
    return lazyLoadingEnabled;
  }

  public String getExtendz() {
    return extendz;
  }

  public List<EdmEntitySet> getEntitySets() {
    return entitySets;
  }

  public List<EdmAssociationSet> getAssociationSets() {
    return associationSets;
  }

  public List<EdmFunctionImport> getFunctionImports() {
    return functionImports;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  static Builder newBuilder(EdmEntityContainer entityContainer, BuilderContext context) {
    return context.newBuilder(entityContainer, new Builder());
  }

  /** Mutable builder for {@link EdmEntityContainer} objects. */
  public static class Builder extends EdmItem.Builder<EdmEntityContainer, Builder> {

    private String name;
    private boolean isDefault;
    private Boolean lazyLoadingEnabled;
    private String extendz;
    private final List<EdmEntitySet.Builder> entitySets = new ArrayList<EdmEntitySet.Builder>();
    private final List<EdmAssociationSet.Builder> associationSets = new ArrayList<EdmAssociationSet.Builder>();
    private final List<EdmFunctionImport.Builder> functionImports = new ArrayList<EdmFunctionImport.Builder>();

    @Override
    Builder newBuilder(EdmEntityContainer entityContainer, BuilderContext context) {
      List<EdmFunctionImport.Builder> functionImports = new ArrayList<EdmFunctionImport.Builder>();
      for (EdmFunctionImport functionImport : entityContainer.functionImports)
        functionImports.add(EdmFunctionImport.newBuilder(functionImport, context));

      List<EdmEntitySet.Builder> entitySets = new ArrayList<EdmEntitySet.Builder>();
      for (EdmEntitySet entitySet : entityContainer.entitySets)
        entitySets.add(EdmEntitySet.newBuilder(entitySet, context));

      List<EdmAssociationSet.Builder> associationSets = new ArrayList<EdmAssociationSet.Builder>();
      for (EdmAssociationSet associationSet : entityContainer.associationSets)
        associationSets.add(EdmAssociationSet.newBuilder(associationSet, context));
      return new Builder().setName(entityContainer.name).setIsDefault(entityContainer.isDefault).setLazyLoadingEnabled(entityContainer.lazyLoadingEnabled)
          .addEntitySets(entitySets).addAssociationSets(associationSets).addFunctionImports(functionImports);
    }

    public EdmEntityContainer build() {
      List<EdmFunctionImport> functionImports = new ArrayList<EdmFunctionImport>();
      for (EdmFunctionImport.Builder functionImport : this.functionImports)
        functionImports.add(functionImport.build());

      List<EdmEntitySet> entitySets = new ArrayList<EdmEntitySet>();
      for (EdmEntitySet.Builder entitySet : this.entitySets)
        entitySets.add(entitySet.build());

      List<EdmAssociationSet> associationSets = new ArrayList<EdmAssociationSet>();
      for (EdmAssociationSet.Builder associationSet : this.associationSets)
        associationSets.add(associationSet.build());

      return new EdmEntityContainer(name, isDefault, lazyLoadingEnabled, extendz,
          ImmutableList.copyOf(entitySets),
          ImmutableList.copyOf(associationSets),
          ImmutableList.copyOf(functionImports),
          getDocumentation(), ImmutableList.copyOf(getAnnotations()), ImmutableList.copyOf(getAnnotationElements()));
    }

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setIsDefault(boolean isDefault) {
      this.isDefault = isDefault;
      return this;
    }

    public Builder setLazyLoadingEnabled(Boolean lazyLoadingEnabled) {
      this.lazyLoadingEnabled = lazyLoadingEnabled;
      return this;
    }

    public Builder setExtendz(String extendz) {
      this.extendz = extendz;
      return this;
    }

    public Builder addEntitySets(EdmEntitySet.Builder... entitySets) {
      this.entitySets.addAll(Arrays.asList(entitySets));
      return this;
    }

    public Builder addEntitySets(Collection<EdmEntitySet.Builder> entitySets) {
      this.entitySets.addAll(entitySets);
      return this;
    }

    public Builder addAssociationSets(Collection<EdmAssociationSet.Builder> associationSets) {
      this.associationSets.addAll(associationSets);
      return this;
    }

    public Builder addFunctionImports(Collection<EdmFunctionImport.Builder> functionImports) {
      this.functionImports.addAll(functionImports);
      return this;
    }

    public Builder addFunctionImports(EdmFunctionImport.Builder... functionImports) {
      this.functionImports.addAll(Arrays.asList(functionImports));
      return this;
    }

    public List<EdmEntitySet.Builder> getEntitySets() {
      return entitySets;
    }

    public List<EdmAssociationSet.Builder> getAssociationSets() {
      return associationSets;
    }

    public List<EdmFunctionImport.Builder> getFunctionImports() {
      return functionImports;
    }

    public String getName() {
      return name;
    }

  }

}
