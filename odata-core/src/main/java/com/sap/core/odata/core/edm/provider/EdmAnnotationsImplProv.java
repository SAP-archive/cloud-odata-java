package com.sap.core.odata.core.edm.provider;

import java.util.Collection;
import java.util.Iterator;

import com.sap.core.odata.api.edm.EdmAnnotationAttribute;
import com.sap.core.odata.api.edm.EdmAnnotationElement;
import com.sap.core.odata.api.edm.EdmAnnotations;
import com.sap.core.odata.api.edm.provider.AnnotationAttribute;
import com.sap.core.odata.api.edm.provider.AnnotationElement;

public class EdmAnnotationsImplProv implements EdmAnnotations {

  private Collection<AnnotationAttribute> annotationAttributes;
  private Collection<? extends EdmAnnotationElement> annotationElements;

  public EdmAnnotationsImplProv(Collection<AnnotationAttribute> annotationAttributes, Collection<AnnotationElement> annotationElements) {
    this.annotationAttributes = annotationAttributes;
    this.annotationElements = annotationElements;
  }

  @Override
  public Collection<? extends EdmAnnotationElement> getAnnotationElements() {
    return annotationElements;
  }

  @Override
  public EdmAnnotationElement getAnnotationElement(String name, String namespace) {
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
  public Collection<? extends EdmAnnotationAttribute> getAnnotationAttributes() {
    return annotationAttributes;
  }

  @Override
  public EdmAnnotationAttribute getAnnotationAttribute(String name, String namespace) {
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
