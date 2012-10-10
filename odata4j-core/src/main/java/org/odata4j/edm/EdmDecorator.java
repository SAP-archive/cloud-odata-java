package org.odata4j.edm;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.odata4j.core.NamespacedAnnotation;
import org.odata4j.core.OProperty;
import org.odata4j.core.PrefixedNamespace;
import org.odata4j.producer.PropertyPath;

/**
 * Application specific EDM customizations such as Documentation and Annotations.
 */
public interface EdmDecorator {

  /** Gets custom prefixed namespaces for this EDM. */
  List<PrefixedNamespace> getNamespaces();

  /** Gets custom documentation for a given EDM schema. */
  EdmDocumentation getDocumentationForSchema(String namespace);

  /** Gets custom annotations for a given EDM schema. */
  List<EdmAnnotation<?>> getAnnotationsForSchema(String namespace);

  /** Gets custom documentation for a given EDM entity type. */
  EdmDocumentation getDocumentationForEntityType(String namespace, String typeName);

  /** Gets custom annotations for a given EDM schema. */
  List<EdmAnnotation<?>> getAnnotationsForEntityType(String namespace, String typeName);

  /**
   * Resolves a custom property (i.e. Annotation) on a structural type.
   *
   * @param structuralType  the type
   * @param path  the path to the property
   * @return a property value (may be null) for the requested property if it exists.
   * @throws IllegalArgumentException if the property does not exist.
   */
  Object resolveStructuralTypeProperty(EdmStructuralType structuralType, PropertyPath path) throws IllegalArgumentException;

  /** Gets custom documentation for a given EDM property. */
  EdmDocumentation getDocumentationForProperty(String namespace, String typeName, String propName);

  /** Gets custom annotations for a given EDM property. */
  List<EdmAnnotation<?>> getAnnotationsForProperty(String namespace, String typeName, String propName);

  /**
   * Resolves a custom property (i.e. Annotation) on a property type.
   *
   * @param st  the type
   * @param path  the path to the property
   * @return a property value (may be null) for the requested property if it exists.
   * @throws IllegalArgumentException if the property does not exist.
   */
  Object resolvePropertyProperty(EdmProperty st, PropertyPath path) throws IllegalArgumentException;

  // TODO: other EdmItem types here.

  /**
   * Gets an annotation value that overrides the original annotation value.
   *
   * <p>This is an experiment that allows one to localize queryable metadata.
   * Say you have an annotation called LocalizedName on your item.  When
   * the metadata is queried, the caller can supply a custom locale parameter
   * in options and this method can override the original LocalizedName with
   * the one for the given locale.
   *
   * @param item  the annotated item
   * @param annot  the annotation
   * @param options  from query
   * @return null if no override, an object with the value if there is one.
   */
  Object getAnnotationValueOverride(EdmItem item, NamespacedAnnotation<?> annot, boolean flatten, Locale locale, Map<String, String> options);

  /** Modifies outgoing EDM items. */
  void decorateEntity(EdmEntitySet entitySet, EdmItem item, EdmItem originalQueryItem,
      List<OProperty<?>> props, boolean flatten, Locale locale, Map<String, String> options);

}
