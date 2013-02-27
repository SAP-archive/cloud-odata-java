package com.sap.core.odata.processor.core.jpa.access.model;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.PluralAttribute;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.api.edm.provider.ComplexProperty;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.Mapping;
import com.sap.core.odata.api.edm.provider.NavigationProperty;
import com.sap.core.odata.processor.api.jpa.access.JPAEdmMappingModelAccess;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationEndView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationSetView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmBaseView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmComplexPropertyView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityContainerView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntitySetView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityTypeView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmMapping;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmNavigationPropertyView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmPropertyView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmSchemaView;
import com.sap.core.odata.processor.core.jpa.model.JPAEdmComplexType;
import com.sap.core.odata.processor.core.jpa.model.JPAEdmMappingImpl;

public class JPAEdmNameBuilder {
	private static final String ENTITY_CONTAINER_SUFFIX = "Container";
	private static final String ENTITY_SET_SUFFIX = "s";
	private static final String ASSOCIATIONSET_SUFFIX = "Set";
	private static final String NAVIGATION_NAME = "Details";
	private static final String UNDERSCORE = "_";

	/*
	 * ************************************************************************
	 * EDM EntityType Name - RULES
	 * ************************************************************************
	 * EDM Entity Type Name = JPA Entity Name EDM Entity Type Internal Name =
	 * JPA Entity Name
	 * ************************************************************************
	 * EDM Entity Type Name - RULES
	 * ************************************************************************
	 */
	public static void build(JPAEdmEntityTypeView view) {

		EntityType edmEntityType = view.getEdmEntityType();
		String jpaEntityName = view.getJPAEntityType().getName();
		JPAEdmMappingModelAccess mappingModelAccess = view
				.getJPAEdmMappingModelAccess();
		String edmEntityTypeName = null;
		if (mappingModelAccess != null
				&& mappingModelAccess.isMappingModelExists())
			edmEntityTypeName = mappingModelAccess
					.mapJPAEntityType(jpaEntityName);

		if (edmEntityTypeName == null)
			edmEntityTypeName = jpaEntityName;
		else
			edmEntityType.setMapping(new Mapping()
					.setInternalName(jpaEntityName));

		edmEntityType.setName(edmEntityTypeName);

	}

	/*
	 * ************************************************************************
	 * EDM Schema Name - RULES
	 * ************************************************************************
	 * Java Persistence Unit name is set as Schema's Namespace
	 * ************************************************************************
	 * EDM Schema Name - RULES
	 * ************************************************************************
	 */
	public static void build(JPAEdmSchemaView view)
			throws ODataJPAModelException {
		view.getEdmSchema().setNamespace(buildNamespace(view));
	}

	/*
	 * ************************************************************************
	 * EDM Property Name - RULES
	 * ************************************************************************
	 * OData Property Names are represented in Camel Case. The first character
	 * of JPA Attribute Name is converted to an UpperCase Character and set as
	 * OData Property Name. JPA Attribute Name is set as Internal Name for OData
	 * Property. The Column name (annotated as @Column(name="x")) is set as
	 * column name in the mapping object.
	 * ************************************************************************
	 * EDM Property Name - RULES
	 * ************************************************************************
	 */
	public static void build(JPAEdmPropertyView view, boolean isComplexMode) {
		Attribute<?, ?> jpaAttribute = view.getJPAAttribute();
		String jpaAttributeName = jpaAttribute.getName();
		String propertyName = null;

		JPAEdmMappingModelAccess mappingModelAccess = view
				.getJPAEdmMappingModelAccess();
		if (mappingModelAccess != null
				&& mappingModelAccess.isMappingModelExists()) {
			if (isComplexMode)
				propertyName = mappingModelAccess
						.mapJPAEmbeddableTypeAttribute(view
								.getJPAEdmComplexTypeView()
								.getJPAEmbeddableType().getJavaType()
								.getSimpleName(), jpaAttributeName);
			else
				propertyName = mappingModelAccess.mapJPAAttribute(
						view.getJPAEdmEntityTypeView().getJPAEntityType()
								.getName(), jpaAttributeName);
		}
		if (propertyName == null)
			propertyName = Character.toUpperCase(jpaAttributeName.charAt(0))
					+ jpaAttributeName.substring(1);

		view.getEdmSimpleProperty().setName(propertyName);

		JPAEdmMapping mapping = new JPAEdmMappingImpl();
		((Mapping) mapping).setInternalName(jpaAttributeName);

		AnnotatedElement annotatedElement = (AnnotatedElement) jpaAttribute
				.getJavaMember();
		if (annotatedElement != null) {
			Column column = annotatedElement.getAnnotation(Column.class);
			if (column != null)
				mapping.setJPAColumnName(column.name());
		} else {
			ManagedType<?> managedType = jpaAttribute.getDeclaringType();
			if (managedType != null) {
				Class<?> clazz = managedType.getJavaType();
				try {
					Field field = clazz.getDeclaredField(jpaAttributeName);
					Column column = field.getAnnotation(Column.class);
					if (column != null) {
						mapping.setJPAColumnName(column.name());
					}
				} catch (SecurityException e) {

				} catch (NoSuchFieldException e) {

				}
			}

		}
		view.getEdmSimpleProperty().setMapping((Mapping) mapping);
	}

	/*
	 * ************************************************************************
	 * EDM EntityContainer Name - RULES
	 * ************************************************************************
	 * Entity Container Name = EDM Namespace + Literal "Container"
	 * ************************************************************************
	 * EDM EntityContainer Name - RULES
	 * ************************************************************************
	 */
	public static void build(JPAEdmEntityContainerView view) {
		view.getEdmEntityContainer().setName(
				buildNamespace(view) + ENTITY_CONTAINER_SUFFIX);
	}

	/*
	 * ************************************************************************
	 * EDM EntitySet Name - RULES
	 * ************************************************************************
	 * Entity Set Name = JPA Entity Type Name + Literal "s"
	 * ************************************************************************
	 * EDM EntitySet Name - RULES
	 * ************************************************************************
	 */
	public static void build(JPAEdmEntitySetView view,
			JPAEdmEntityTypeView entityTypeView) {
		FullQualifiedName fQname = view.getEdmEntitySet().getEntityType();
		JPAEdmMappingModelAccess mappingModelAccess = view
				.getJPAEdmMappingModelAccess();
		String entitySetName = null;
		if (mappingModelAccess != null
				&& mappingModelAccess.isMappingModelExists()) {
			Mapping mapping = entityTypeView.getEdmEntityType().getMapping();
			if (mapping != null)
				entitySetName = mappingModelAccess.mapJPAEntitySet(mapping
						.getInternalName());
		}

		if (entitySetName == null)
			entitySetName = fQname.getName() + ENTITY_SET_SUFFIX;

		view.getEdmEntitySet().setName(entitySetName);
	}

	/*
	 * ************************************************************************
	 * EDM Complex Type Name - RULES
	 * ************************************************************************
	 * Complex Type Name = JPA Embeddable Type Simple Name.
	 * ************************************************************************
	 * EDM Complex Type Name - RULES
	 * ************************************************************************
	 */
	public static void build(JPAEdmComplexType view) {

		JPAEdmMappingModelAccess mappingModelAccess = view
				.getJPAEdmMappingModelAccess();
		String jpaEmbeddableTypeName = view.getJPAEmbeddableType()
				.getJavaType().getSimpleName();
		String edmComplexTypeName = null;
		if (mappingModelAccess != null
				&& mappingModelAccess.isMappingModelExists())
			edmComplexTypeName = mappingModelAccess
					.mapJPAEmbeddableType(jpaEmbeddableTypeName);

		if (edmComplexTypeName == null)
			edmComplexTypeName = jpaEmbeddableTypeName;

		view.getEdmComplexType().setName(edmComplexTypeName);

	}

	/*
	 * ************************************************************************
	 * EDM Complex Property Name - RULES
	 * ************************************************************************
	 * The first character of JPA complex attribute name is converted to
	 * uppercase. The modified JPA complex attribute name is assigned as EDM
	 * complex property name. The unmodified JPA complex attribute name is
	 * assigned as internal name.
	 * ************************************************************************
	 * EDM Complex Property Name - RULES
	 * ************************************************************************
	 */
	public static void build(JPAEdmComplexPropertyView complexView,
			JPAEdmPropertyView propertyView) {

		ComplexProperty complexProperty = complexView.getEdmComplexProperty();

		String jpaAttributeName = propertyView.getJPAAttribute().getName();
		String jpaEntityTypeName = propertyView.getJPAEdmEntityTypeView()
				.getJPAEntityType().getName();

		JPAEdmMappingModelAccess mappingModelAccess = complexView
				.getJPAEdmMappingModelAccess();
		String propertyName = null;

		if (mappingModelAccess != null
				&& mappingModelAccess.isMappingModelExists())
			propertyName = mappingModelAccess.mapJPAAttribute(
					jpaEntityTypeName, jpaAttributeName);

		if (propertyName == null)
			propertyName = Character.toUpperCase(jpaAttributeName.charAt(0))
					+ jpaAttributeName.substring(1);
		else
			complexProperty.setMapping(((Mapping) new JPAEdmMappingImpl())
					.setInternalName(jpaAttributeName));

		complexProperty.setName(propertyName);

	}

	/*
	 * ************************************************************************
	 * EDM Association End Name - RULES
	 * ************************************************************************
	 * Association End name = Namespace + Entity Type Name
	 * ************************************************************************
	 * EDM Association End Name - RULES
	 * ************************************************************************
	 */
	public static void build(JPAEdmAssociationEndView assocaitionEndView,
			JPAEdmEntityTypeView entityTypeView, JPAEdmPropertyView propertyView) {

		String namespace = buildNamespace(assocaitionEndView);

		String name = entityTypeView.getEdmEntityType().getName();
		FullQualifiedName fQName = new FullQualifiedName(namespace, name);
		assocaitionEndView.getEdmAssociationEnd1().setType(fQName);
		
		name = null;
		String jpaEntityTypeName = null;
		try {

			PluralAttribute<?, ?, ?> jpattr = (PluralAttribute<?, ?, ?>) propertyView
					.getJPAAttribute();

			jpaEntityTypeName = jpattr.getElementType().getJavaType()
					.getSimpleName();

		} catch (Exception e) {
			jpaEntityTypeName = propertyView.getJPAAttribute().getJavaType()
					.getSimpleName();
		}

		JPAEdmMappingModelAccess mappingModelAccess = assocaitionEndView
				.getJPAEdmMappingModelAccess();

		if (mappingModelAccess != null
				&& mappingModelAccess.isMappingModelExists())
			name = mappingModelAccess.mapJPAEntityType(jpaEntityTypeName);

		if (name == null)
			name = jpaEntityTypeName;

		fQName = new FullQualifiedName(namespace, name);
		assocaitionEndView.getEdmAssociationEnd2().setType(fQName);

	}

	private static String buildNamespace(JPAEdmBaseView view) {
		JPAEdmMappingModelAccess mappingModelAccess = view
				.getJPAEdmMappingModelAccess();
		String namespace = null;
		if (mappingModelAccess != null
				&& mappingModelAccess.isMappingModelExists())
			namespace = mappingModelAccess.mapJPAPersistenceUnit(view
					.getpUnitName());
		if (namespace == null)
			namespace = view.getpUnitName();

		return namespace;
	}

	/*
	 * ************************************************************************
	 * EDM Association Name - RULES
	 * ************************************************************************
	 * Association name = Association + End1 Name + End2 Name
	 * ************************************************************************
	 * EDM Association Name - RULES
	 * ************************************************************************
	 */

	public static void build(JPAEdmAssociationView view) {
		Association association = view.getEdmAssociation();

		String end1Name = association.getEnd1().getType().getName();
		String end2Name = association.getEnd2().getType().getName();

		if (end1Name.compareToIgnoreCase(end2Name) > 0) {
			association.setName(end2Name + UNDERSCORE + end1Name);
		} else {
			association.setName(end1Name + UNDERSCORE + end2Name);
		}

	}

	/*
	 * ************************************************************************
	 * EDM Association Set Name - RULES
	 * ************************************************************************
	 * Association Set name = Association Name + "Set"
	 * ************************************************************************
	 * EDM Association Set Name - RULES
	 * ************************************************************************
	 */
	public static void build(JPAEdmAssociationSetView view) {
		AssociationSet associationSet = view.getEdmAssociationSet();

		String name = view.getEdmAssociation().getName();
		associationSet.setName(name + ASSOCIATIONSET_SUFFIX);

	}

	public static void build(JPAEdmAssociationView associationView,
			JPAEdmPropertyView propertyView,
			JPAEdmNavigationPropertyView navPropertyView) {

		NavigationProperty navProp = navPropertyView.getEdmNavigationProperty();
		String namespace = buildNamespace(associationView);

		Association association = associationView.getEdmAssociation();
		navProp.setRelationship(new FullQualifiedName(namespace, association
				.getName()));

		FullQualifiedName associationEndTypeOne = association.getEnd1()
				.getType();

		Attribute<?, ?> jpaAttribute = propertyView.getJPAAttribute();
		navProp.setMapping(new Mapping().setInternalName(jpaAttribute.getName()));

		String jpaEntityTypeName = propertyView.getJPAEdmEntityTypeView()
				.getJPAEntityType().getName();
		String navPropName = null;

		JPAEdmMappingModelAccess mappingModelAccess = navPropertyView
				.getJPAEdmMappingModelAccess();

		try {
			PluralAttribute<?, ?, ?> jpattr = (PluralAttribute<?, ?, ?>) propertyView
					.getJPAAttribute();

			if (mappingModelAccess != null
					&& mappingModelAccess.isMappingModelExists())
				navPropName = mappingModelAccess.mapJPARelationship(
						jpaEntityTypeName, jpattr.getName());
			if (navPropName == null)
				navPropName = jpattr.getElementType().getJavaType()
						.getSimpleName().concat(NAVIGATION_NAME);

			navProp.setName(navPropName);

			if (jpattr.getElementType().getJavaType().getSimpleName()
					.equals(associationEndTypeOne.getName())) {
				navProp.setFromRole(association.getEnd2().getRole());
				navProp.setToRole(association.getEnd1().getRole());
			} else {
				navProp.setToRole(association.getEnd2().getRole());
				navProp.setFromRole(association.getEnd1().getRole());
			}

		} catch (Exception e) {
			if (mappingModelAccess != null
					&& mappingModelAccess.isMappingModelExists())
				navPropName = mappingModelAccess.mapJPARelationship(
						jpaEntityTypeName, jpaAttribute.getName());

			if (navPropName == null)
				navPropName = jpaAttribute.getJavaType().getSimpleName()
						.concat(NAVIGATION_NAME);

			navProp.setName(navPropName);

			if (jpaAttribute.getJavaType().getSimpleName()
					.equals(associationEndTypeOne.getName())) {
				navProp.setFromRole(association.getEnd2().getRole());
				navProp.setToRole(association.getEnd1().getRole());
			} else {

				navProp.setToRole(association.getEnd2().getRole());
				navProp.setFromRole(association.getEnd1().getRole());
			}
		}

	}
}
