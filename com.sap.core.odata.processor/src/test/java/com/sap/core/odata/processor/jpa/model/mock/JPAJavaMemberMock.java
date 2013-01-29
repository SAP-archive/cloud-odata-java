package com.sap.core.odata.processor.jpa.model.mock;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;

import javax.persistence.JoinColumns;

public class JPAJavaMemberMock implements Member, AnnotatedElement, Annotation {

	@Override
	public Class<?> getDeclaringClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getModifiers() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isSynthetic() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAnnotationPresent(
			Class<? extends Annotation> annotationClass) {
		// TODO Auto-generated method stub
		return false;
	}

		@Override
	public Annotation[] getAnnotations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Annotation[] getDeclaredAnnotations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return JoinColumns.class;
	}

	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		// TODO Auto-generated method stub
		return null;
	}

}
