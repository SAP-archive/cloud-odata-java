package org.odata4j.edm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.odata4j.core.ImmutableList;

public class EdmReferentialConstraint extends EdmItem {

  private final String principalRole;
  private final String dependentRole;
  private final ImmutableList<String> principalReferences;
  private final ImmutableList<String> dependentReferences;

  protected EdmReferentialConstraint(String principalRole, String dependentRole,
      ImmutableList<String> principalReferences, ImmutableList<String> dependentReferences,
      EdmDocumentation documentation, ImmutableList<EdmAnnotation<?>> annotations,
      ImmutableList<EdmAnnotation<?>> annotationElements) {
    super(documentation, annotations, annotationElements);
    this.principalRole = principalRole;
    this.dependentRole = dependentRole;
    this.principalReferences = principalReferences;
    this.dependentReferences = dependentReferences;
    // TODO Auto-generated constructor stub
  }

  public String getPrincipalRole() {
    return principalRole;
  }

  public String getDependentRole() {
    return dependentRole;
  }

  public List<String> getPrincipalReferences() {
    return principalReferences;
  }

  public List<String> getDependentReferences() {
    return dependentReferences;
  }

  @Override
  public String toString() {
    return String.format("EdmReferentialConstraint[%s,%s]", principalRole, dependentRole);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  static Builder newBuilder(EdmReferentialConstraint refConstraint, BuilderContext context) {
    return context.newBuilder(refConstraint, new Builder());
  }

  /** Mutable builder for {@link EdmReferentialConstraint} objects. */
  public static class Builder extends EdmItem.Builder<EdmReferentialConstraint, Builder> {

    private String principalRole;
    private String dependentRole;
    private final List<String> principalReferences = new ArrayList<String>();
    private final List<String> dependentReferences = new ArrayList<String>();

    @Override
    Builder newBuilder(EdmReferentialConstraint refConstraint, BuilderContext context) {
      this.principalRole = refConstraint.principalRole;
      this.dependentRole = refConstraint.dependentRole;
      if (refConstraint.principalReferences != null) {
        this.principalReferences.addAll(refConstraint.principalReferences);
      }
      if (refConstraint.principalReferences != null) {
        this.dependentReferences.addAll(refConstraint.dependentReferences);
      }
      return this;
    }

    public EdmReferentialConstraint build() {
      return new EdmReferentialConstraint(principalRole, dependentRole,
          ImmutableList.copyOf(principalReferences), ImmutableList.copyOf(dependentReferences),
          getDocumentation(), ImmutableList.copyOf(getAnnotations()),
          ImmutableList.copyOf(getAnnotationElements()));
    }

    public Builder setPrincipalRole(String principalRole) {
      this.principalRole = principalRole;
      return this;
    }

    public Builder setDependentRole(String dependentRole) {
      this.dependentRole = dependentRole;
      return this;
    }

    public Builder addPrincipalReferences(List<String> pReferences) {
      this.principalReferences.addAll(pReferences);
      return this;
    }

    public Builder addPrincipalReferences(String... pReferences) {
      return addPrincipalReferences(Arrays.asList(pReferences));
    }

    public Builder addDependentReferences(List<String> depReferences) {
      this.dependentReferences.addAll(depReferences);
      return this;
    }

    public Builder addDependentReferences(String... depReferences) {
      return addDependentReferences(Arrays.asList(depReferences));
    }

    public String getPrincipalRole() {
      return principalRole;
    }

    public String getDependentRole() {
      return dependentRole;
    }

  }

}
