package com.sap.core.odata.processor.jpa.api.model;

import java.util.List;

import javax.persistence.metamodel.EmbeddableType;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.ComplexType;

public interface JPAEdmComplexTypeView extends JPAEdmBaseView {
	public ComplexType getEdmComplexType();

	public javax.persistence.metamodel.EmbeddableType<?> getJPAEmbeddableType();

	public List<ComplexType> getConsistentEdmComplexTypes();

	public ComplexType searchComplexType(EmbeddableType<?> jpaEmbeddableType);

	public void addCompleTypeView(JPAEdmComplexTypeView view);

	public ComplexType searchComplexType(FullQualifiedName type);

}
