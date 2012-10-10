package org.odata4j.edm;

import java.util.List;

import org.odata4j.core.NamespacedAnnotation;

/**
 * A CSDL Annotation element.
 *
 * <p>Annotation elements in conceptual schema definition language (CSDL) are custom XML elements
 * in the conceptual model. Annotation elements can be used to provide extra metadata about
 * the elements in a conceptual model.
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/ee473443.aspx">[msdn] Annotation Elements (CSDL)</a>
 */
public class EdmAnnotationElement<T> extends EdmAnnotation<T> {

  private List<EdmAnnotation<?>> annotations;
  private List<EdmAnnotation<?>> annotationElements;

  public EdmAnnotationElement(String namespaceUri, String namespacePrefix, String name, Class<T> valueType, T value) {
    super(namespaceUri, namespacePrefix, name, valueType, value);
  }

  public void setAnnotations(List<EdmAnnotation<?>> annotations) {
    this.annotations = annotations;
  }

  public void setAnnotationElements(List<EdmAnnotation<?>> annotationElements) {
    this.annotationElements = annotationElements;
  }

  public Iterable<? extends NamespacedAnnotation<?>> getAnnotations() {
    return annotations;
  }

  public Iterable<? extends NamespacedAnnotation<?>> getAnnotationElements() {
    return annotationElements;
  }

  public EdmAnnotation<?> findAnnotationElement(String namespaceUri, String name) {
    if (annotationElements != null) {
      for (EdmAnnotation<?> element : annotationElements) {
        if (element.getNamespace().getUri().equals(namespaceUri) && element.getName().equals(name))
          return element;

      }
    }
    return null;
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

}
