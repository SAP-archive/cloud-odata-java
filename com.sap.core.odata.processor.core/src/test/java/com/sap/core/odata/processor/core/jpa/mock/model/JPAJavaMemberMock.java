package com.sap.core.odata.processor.core.jpa.mock.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;

import javax.persistence.JoinColumns;

public class JPAJavaMemberMock implements Member, AnnotatedElement, Annotation {

  @Override
  public Class<?> getDeclaringClass() {
    return null;
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public int getModifiers() {
    return 0;
  }

  @Override
  public boolean isSynthetic() {
    return false;
  }

  @Override
  public boolean isAnnotationPresent(
      final Class<? extends Annotation> annotationClass) {
    return false;
  }

  @Override
  public Annotation[] getAnnotations() {
    return null;
  }

  @Override
  public Annotation[] getDeclaredAnnotations() {
    return null;
  }

  @Override
  public Class<? extends Annotation> annotationType() {
    return JoinColumns.class;
  }

  @Override
  public <T extends Annotation> T getAnnotation(final Class<T> annotationClass) {
    return null;
  }

}
