package com.sap.core.odata.processor.jpa.access.model;

import java.lang.reflect.AnnotatedElement;

import javax.persistence.Column;
import javax.persistence.metamodel.Attribute;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.Mapping;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmAssociationEndView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmComplexPropertyView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntityContainerView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntitySetView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntityTypeView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmMapping;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmPropertyView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmSchemaView;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.model.JPAEdmComplexType;
import com.sap.core.odata.processor.jpa.model.JPAEdmMappingImpl;

public class JPAEdmNameBuilder {
	private static final String ENTITY_CONTAINER_SUFFIX = "Container";
	private static final String ENTITY_SET_SUFFIX = "s";

	/*
	 * ************************************************************
	 *					EDM EntityType Name - RULES 
	 * ************************************************************
	 * JPA Entity Type Name is set as EDM Entity Type Name.
	 * ************************************************************
	 * 					EDM Entity Type Name - RULES 
	 * ************************************************************
	 */
	public static void build(JPAEdmEntityTypeView view) {

		EntityType edmEntityType = view.getEdmEntityType();
		String jpaEntityName = view.getJPAEntityType().getName();

		edmEntityType.setName(jpaEntityName);
		edmEntityType.setMapping(new Mapping().setInternalName(jpaEntityName));
	}

	/*
	 * ************************************************************
	 *					EDM Schema Name - RULES 
	 * ************************************************************
	 * Java Persistence Unit name is set as Schema's Namespace
	 * ************************************************************
	 * 					EDM Schema Name - RULES 
	 * ************************************************************
	 */
	public static void build(JPAEdmSchemaView view)
			throws ODataJPAModelException {
		view.getEdmSchema().setNamespace(buildNamespace(view.getpUnitName()));
	}

	/*
	 * ************************************************************
	 *					EDM Property Name - RULES 
	 * ************************************************************ 
	 * OData Property Names are represented in Camel Case. 
	 * The first character of JPA Attribute Name is converted to an 
	 * UpperCase Character and set as OData Property Name. 
	 * JPA Attribute Name is set as Internal Name for OData Property.
	 * The Column name (annotated as @Column(name="x")) is set as
	 * column name in the mapping object.
	 * ************************************************************
	 * 					EDM Property Name - RULES 
	 * ************************************************************ 
	 */
	public static void build(JPAEdmPropertyView view) {
		Attribute<?, ?> jpaAttribute = view.getJPAAttribute();
		String jpaAttributeName = jpaAttribute.getName();
		String propertyName = Character.toUpperCase(jpaAttributeName.charAt(0))
				+ jpaAttributeName.substring(1);
		view.getSimpleProperty().setName(propertyName);
		
		JPAEdmMapping mapping = new JPAEdmMappingImpl();
		
		AnnotatedElement annotatedElement = (AnnotatedElement) jpaAttribute.getJavaMember();
		if(annotatedElement != null){
			Column column = annotatedElement.getAnnotation(Column.class);
		if(column != null)
			mapping.setColumnName(column.name());
		}
		((Mapping) mapping).setInternalName(jpaAttributeName);
		view.getSimpleProperty().setMapping((Mapping) mapping);
	}
	
	/*
	 * ************************************************************
 	 *				EDM EntityContainer Name - RULES 
	 * ************************************************************
	 * Java Persistence Unit name + Literal "Container" is appended
	 * to Entity Container Name.
	 * ************************************************************
	 *				EDM EntityContainer Name - RULES 
	 * ************************************************************
	 */
	public static void build(JPAEdmEntityContainerView view) {
		view.getEdmEntityContainer().setName(
				buildNamespace(view.getpUnitName()) + ENTITY_CONTAINER_SUFFIX);
	}
	
	/*
	 * ************************************************************
 	 *					EDM EntitySet Name - RULES 
	 * ************************************************************
	 * JPA Entity Type Name + Literal "s" is appended to Entity Set
	 * Name.
	 * ************************************************************
	 *					EDM EntitySet Name - RULES 
	 * ************************************************************
	 */
	public static void build(JPAEdmEntitySetView view) {
		FullQualifiedName fQname = view.getEdmEntitySet().getEntityType();
		view.getEdmEntitySet().setName(fQname.getName() + ENTITY_SET_SUFFIX);
	}

	public static void build(JPAEdmComplexType view) {
		// TODO Auto-generated method stub
		
	}

	public static void build(JPAEdmComplexPropertyView view) {
		// TODO Auto-generated method stub
		
	}
	
	public static void build(JPAEdmAssociationEndView assocaitionEndView,
			JPAEdmEntityTypeView entityTypeView, JPAEdmPropertyView propertyView) {
		
		String namespace = buildNamespace(assocaitionEndView.getpUnitName());

		String name = entityTypeView.getEdmEntityType().getName();
		FullQualifiedName fQName = new FullQualifiedName(namespace, name);
		assocaitionEndView.getAssociationEnd1().setType(fQName);
		
		name = propertyView.getJPAAttribute().getJavaType().getSimpleName();
		fQName = new FullQualifiedName(namespace, name);
		assocaitionEndView.getAssociationEnd2().setType(fQName);
		
	}
	
	private static String buildNamespace(String name){
		return name;
	}
	
}	

