package com.sap.core.odata.processor.api.jpa.model;

import java.util.List;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.Property;

/**
 * This class provides functionality to search, add, expand and fetch EDM Complex Types.
 * @author AG
 *
 */
public interface JPAEdmComplexTypeView extends JPAEdmBaseView {
	public ComplexType getEdmComplexType();

	public javax.persistence.metamodel.EmbeddableType<?> getJPAEmbeddableType();

	public List<ComplexType> getConsistentEdmComplexTypes();

	public ComplexType searchEdmComplexType(String embeddableTypeName);

	public void addJPAEdmCompleTypeView(JPAEdmComplexTypeView view);

	public ComplexType searchEdmComplexType(FullQualifiedName type);
	
	public void expandEdmComplexType(ComplexType complexType,List<Property> expandedPropertyList,String embeddablePropertyName);

}
