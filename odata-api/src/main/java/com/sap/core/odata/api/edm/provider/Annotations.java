package com.sap.core.odata.api.edm.provider;

import java.util.Collection;

import com.sap.core.odata.api.edm.EdmAnnotationAttribute;
import com.sap.core.odata.api.edm.EdmAnnotationElement;

public class Annotations {

  Collection<EdmAnnotationElement> annotationElements;
  Collection<EdmAnnotationAttribute> annotationAttributes;

  public Annotations(Collection<EdmAnnotationElement> annotationElements, Collection<EdmAnnotationAttribute> annotationAttributes) {
    this.annotationElements = annotationElements;
    this.annotationAttributes = annotationAttributes;
  }

  public Collection<EdmAnnotationElement> getAnnotationElements() {
    return annotationElements;
  }

  public Collection<EdmAnnotationAttribute> getAnnotationAttributes() {
    return annotationAttributes;
  }
}