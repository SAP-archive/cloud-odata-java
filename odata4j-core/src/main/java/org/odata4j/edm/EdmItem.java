package org.odata4j.edm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.odata4j.core.ImmutableList;
import org.odata4j.core.NamespacedAnnotation;

/**
 * Constructs in the CSDL that we model in the org.odata4j.edm package
 * share some common functionality:
 * <li>Documentation (see {@link EdmDocumentation})
 * <li>Annotation (attributes and elements, see {@link EdmAnnotation})
 */
public class EdmItem {

  private final EdmDocumentation documentation;
  private final ImmutableList<? extends NamespacedAnnotation<?>> annotations;
  private final ImmutableList<? extends NamespacedAnnotation<?>> annotationElements;

  protected EdmItem(EdmDocumentation documentation, ImmutableList<EdmAnnotation<?>> annotations,
      ImmutableList<EdmAnnotation<?>> annotationElements) {
    this.documentation = documentation;
    this.annotations = annotations;
    this.annotationElements = annotationElements;
  }

  public EdmDocumentation getDocumentation() {
    return documentation;
  }

  public Iterable<? extends NamespacedAnnotation<?>> getAnnotations() {
    return annotations;
  }

  public Iterable<? extends NamespacedAnnotation<?>> getAnnotationElements() {
    return annotationElements;
  }

  public NamespacedAnnotation<?> findAnnotation(String namespaceUri, String localName) {
    if (annotations != null) {
      for (NamespacedAnnotation<?> annotation : annotations) {
        if (annotation.getNamespace().getUri().equals(namespaceUri) && annotation.getName().equals(localName))
          return annotation;
      }
    }
    return null;
  }

  public NamespacedAnnotation<?> findAnnotationElement(String namespaceUri, String name) {
    if (annotationElements != null) {
      for (NamespacedAnnotation<?> element : annotationElements) {
        if (element.getNamespace().getUri().equals(namespaceUri) && element.getName().equals(name))
          return element;

      }
    }
    return null;
  }

  static class BuilderContext {

    private final Map<Object, Builder<?, ?>> newBuilders = new HashMap<Object, Builder<?, ?>>();
    private final EdmDataServices.Builder dataServices;

    public BuilderContext(EdmDataServices.Builder ds) {
      this.dataServices = ds;
    }

    public EdmDataServices.Builder getDataServices() {
      return this.dataServices;
    }

    @SuppressWarnings("unchecked")
    public <T, TBuilder> TBuilder newBuilder(T item, Builder<T, TBuilder> builder) {
      if (!newBuilders.containsKey(item)) {
        newBuilders.put(item, (Builder<?, ?>) builder.newBuilder(item, this));
      }
      return (TBuilder) newBuilders.get(item);
    }

    public <T, TBuilder extends Builder<?, ?>> void register(T item, TBuilder builder) {
      newBuilders.put(item, builder);
    }

  }

  /** Mutable builder for {@link EdmItem} objects. */
  public abstract static class Builder<T, TBuilder> {

    private EdmDocumentation documentation;
    private List<EdmAnnotation<?>> annotations;
    private List<EdmAnnotation<?>> annotationElements;

    abstract TBuilder newBuilder(T item, BuilderContext context);

    public EdmDocumentation getDocumentation() {
      return documentation;
    }

    public List<EdmAnnotation<?>> getAnnotations() {
      return annotations;
    }

    public List<EdmAnnotation<?>> getAnnotationElements() {
      return annotationElements;
    }

    @SuppressWarnings("unchecked")
    public TBuilder setDocumentation(EdmDocumentation documentation) {
      this.documentation = documentation;
      return (TBuilder) this;
    }

    @SuppressWarnings("unchecked")
    public TBuilder setAnnotations(List<EdmAnnotation<?>> annotations) {
      this.annotations = annotations;
      return (TBuilder) this;
    }

    @SuppressWarnings("unchecked")
    public TBuilder setAnnotationElements(List<EdmAnnotation<?>> annotationElements) {
      this.annotationElements = annotationElements;
      return (TBuilder) this;
    }

  }

}
