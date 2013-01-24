package com.sap.core.odata.processor.jpa.api.model;

import java.util.List;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.Property;

public interface JPAEdmComplexTypeView extends JPAEdmBaseView {
	public ComplexType getEdmComplexType();

	public javax.persistence.metamodel.EmbeddableType<?> getJPAEmbeddableType();

	public List<ComplexType> getConsistentEdmComplexTypes();

	public ComplexType searchComplexType(String embeddableTypeName);

	public void addCompleTypeView(JPAEdmComplexTypeView view);

	public ComplexType searchComplexType(FullQualifiedName type);
	
	public void expandEdmComplexType(ComplexType complexType,List<Property> expandedPropertyList);

}
