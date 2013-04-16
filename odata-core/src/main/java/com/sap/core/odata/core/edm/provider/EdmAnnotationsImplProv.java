package com.sap.core.odata.core.edm.provider;

import java.util.Iterator;
import java.util.List;

import com.sap.core.odata.api.edm.EdmAnnotationAttribute;
import com.sap.core.odata.api.edm.EdmAnnotationElement;
import com.sap.core.odata.api.edm.EdmAnnotations;
import com.sap.core.odata.api.edm.provider.AnnotationAttribute;
import com.sap.core.odata.api.edm.provider.AnnotationElement;

public class EdmAnnotationsImplProv implements EdmAnnotations {

  private List<AnnotationAttribute> annotationAttributes;
  private List<? extends EdmAnnotationElement> annotationElements;

  public EdmAnnotationsImplProv(final List<AnnotationAttribute> annotationAttributes, final List<AnnotationElement> annotationElements) {
    this.annotationAttributes = annotationAttributes;
    this.annotationElements = annotationElements;
  }

  @Override
  public List<? extends EdmAnnotationElement> getAnnotationElements() {
    return annotationElements;
  }

  @Override
  public EdmAnnotationElement getAnnotationElement(final String name, final String namespace) {
    if (annotationElements != null) {
      Iterator<? extends EdmAnnotationElement> annotationElementIterator = annotationElements.iterator();

      while (annotationElementIterator.hasNext()) {
        EdmAnnotationElement annotationElement = annotationElementIterator.next();
        if (annotationElement.getName().equals(name) && annotationElement.getNamespace().equals(namespace)) {
          return annotationElement;
        }
      }
    }
    return null;
  }

  @Override
  public List<? extends EdmAnnotationAttribute> getAnnotationAttributes() {
    return annotationAttributes;
  }

  @Override
  public EdmAnnotationAttribute getAnnotationAttribute(final String name, final String namespace) {
    if (annotationElements != null) {
      Iterator<? extends EdmAnnotationAttribute> annotationAttributesIterator = annotationAttributes.iterator();

      while (annotationAttributesIterator.hasNext()) {
        EdmAnnotationAttribute annotationAttribute = annotationAttributesIterator.next();
        if (annotationAttribute.getName().equals(name) && annotationAttribute.getNamespace().equals(namespace)) {
          return annotationAttribute;
        }
      }
    }
    return null;
  }

}
