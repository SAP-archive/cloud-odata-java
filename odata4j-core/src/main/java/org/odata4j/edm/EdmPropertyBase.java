package org.odata4j.edm;

import org.odata4j.core.ImmutableList;
import org.odata4j.core.Named;

/** Shared base class for {@link EdmProperty} and {@link EdmNavigationProperty} */
public abstract class EdmPropertyBase extends EdmItem implements Named {

  private final String name;

  protected EdmPropertyBase(EdmDocumentation documentation, ImmutableList<EdmAnnotation<?>> annotations,
      ImmutableList<EdmAnnotation<?>> annotationElements, String name) {
    super(documentation, annotations, annotationElements);
    this.name = name;
  }

  public String getName() {
    return name;
  }

  /** Mutable builder for {@link EdmPropertyBase} objects. */
  public abstract static class Builder<T, TBuilder> extends EdmItem.Builder<T, TBuilder> implements Named {

    private String name;

    Builder(String name) {
      this.name = name;
    }

    @Override
    public String getName() {
      return name;
    }

    @SuppressWarnings("unchecked")
    public TBuilder setName(String name) {
      this.name = name;
      return (TBuilder) this;
    }

  }

}
