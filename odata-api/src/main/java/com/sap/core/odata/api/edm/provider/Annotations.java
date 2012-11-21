package com.sap.core.odata.api.edm.provider;

import java.util.Collection;

import com.sap.core.odata.api.edm.EdmAnnotationAttribute;
import com.sap.core.odata.api.edm.EdmAnnotationElement;

/**
 * @author SAP AG
  * <p>
 * Objects of this class represent an annotation in the EDM
 */
public class Annotations {

  //TODO: Is EdmAnnotation right here? It should be AnnotationAttribute
  Collection<EdmAnnotationElement> annotationElements;
  Collection<EdmAnnotationAttribute> annotationAttributes;

  /**
   * @return Collection<{@link EdmAnnotationElement}> of this {@link Annotations}
   */
  public Collection<EdmAnnotationElement> getAnnotationElements() {
    return annotationElements;
  }

  /**
   * @return Collection<{@link EdmAnnotationAttribute}> of this {@link Annotations}
   */
  public Collection<EdmAnnotationAttribute> getAnnotationAttributes() {
    return annotationAttributes;
  }

  /**
   * Sets all {@link EdmAnnotationElement}s for this {@link Annotations}
   * @param annotationElements
   * @return {@link Annotations} for method chaining
   */
  public Annotations setAnnotationElements(Collection<EdmAnnotationElement> annotationElements) {
    this.annotationElements = annotationElements;
    return this;
  }

  /**
   * Sets all {@link EdmAnnotationAttribute}s for this {@link Annotations}
   * @param annotationAttributes
   * @return {@link Annotations} for method chaining
   */
  public Annotations setAnnotationAttributes(Collection<EdmAnnotationAttribute> annotationAttributes) {
    this.annotationAttributes = annotationAttributes;
    return this;
  }

}