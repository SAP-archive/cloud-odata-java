package com.sap.core.odata.api.edm.provider;

import java.util.Collection;

import com.sap.core.odata.api.edm.EdmAnnotationAttribute;
import com.sap.core.odata.api.edm.EdmAnnotationElement;

public class Annotations {

  Collection<EdmAnnotationElement> annotationElements;
  Collection<EdmAnnotationAttribute> annotationAttributes;

  public Collection<EdmAnnotationElement> getAnnotationElements() {
    return annotationElements;
  }

  public Collection<EdmAnnotationAttribute> getAnnotationAttributes() {
    return annotationAttributes;
  }

  public Annotations setAnnotationElements(Collection<EdmAnnotationElement> annotationElements) {
    this.annotationElements = annotationElements;
    return this;
  }

  public Annotations setAnnotationAttributes(Collection<EdmAnnotationAttribute> annotationAttributes) {
    this.annotationAttributes = annotationAttributes;
    return this;
  }

}