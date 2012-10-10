package org.odata4j.edm;

/**
 * A CSDL Annotation attribute.
 *
 * <p>Annotation attributes in conceptual schema definition language (CSDL) are custom XML attributes
 * in the conceptual model. Annotation attributes can be used to provide extra metadata about
 * the elements in a conceptual model.
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/ee473438.aspx">[msdn] Annotation Attributes (CSDL)</a>
 */
public class EdmAnnotationAttribute extends EdmAnnotation<String> {

  public EdmAnnotationAttribute(String namespaceUri, String namespacePrefix, String name, String value) {
    super(namespaceUri, namespacePrefix, name, String.class, value);
  }

}
