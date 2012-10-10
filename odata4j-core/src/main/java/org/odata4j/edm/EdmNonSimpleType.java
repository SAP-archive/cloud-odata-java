package org.odata4j.edm;

import org.odata4j.core.ImmutableList;

/**
 * Non-primitive type in the EDM type system.
 */
public abstract class EdmNonSimpleType extends EdmType {

  public EdmNonSimpleType(String fullyQualifiedTypeName) {
    this(fullyQualifiedTypeName, null, null, null);
  }

  public EdmNonSimpleType(String fullyQualifiedTypeName, EdmDocumentation documentation,
      ImmutableList<EdmAnnotation<?>> annotations, ImmutableList<EdmAnnotation<?>> annotationElements) {
    super(fullyQualifiedTypeName, documentation, annotations, annotationElements);
  }

  @Override
  public boolean isSimple() {
    return false;
  }

}
