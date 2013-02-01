package com.sap.core.odata.processor.jpa.access.model;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.api.edm.provider.ComplexProperty;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.Mapping;
import com.sap.core.odata.api.edm.provider.NavigationProperty;
import com.sap.core.odata.processor.jpa.api.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmAssociationEndView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmAssociationSetView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmAssociationView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmComplexPropertyView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntityContainerView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntitySetView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntityTypeView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmMapping;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmPropertyView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmSchemaView;
import com.sap.core.odata.processor.jpa.model.JPAEdmComplexType;
import com.sap.core.odata.processor.jpa.model.JPAEdmMappingImpl;
import com.sap.core.odata.processor.jpa.model.JPAEdmNavigationProperty;

public class JPAEdmNameBuilder {
	private static final String ENTITY_CONTAINER_SUFFIX = "Container";
	private static final String ENTITY_SET_SUFFIX = "s";
	private static final String ASSOCIATION_PREFIX = "Association_";
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

		edmEntityType.setName(jpaEntityName);
		edmEntityType.setMapping(new Mapping().setInternalName(jpaEntityName));
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
		view.getEdmSchema().setNamespace(buildNamespace(view.getpUnitName()));
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
	public static void build(JPAEdmPropertyView view) {
		Attribute<?, ?> jpaAttribute = view.getJPAAttribute();
		String jpaAttributeName = jpaAttribute.getName();
		String propertyName = Character.toUpperCase(jpaAttributeName.charAt(0))
				+ jpaAttributeName.substring(1);
		view.getSimpleProperty().setName(propertyName);

		JPAEdmMapping mapping = new JPAEdmMappingImpl();

		AnnotatedElement annotatedElement = (AnnotatedElement) jpaAttribute
				.getJavaMember();
		if (annotatedElement != null) {
			Column column = annotatedElement.getAnnotation(Column.class);
			if (column != null)
				mapping.setColumnName(column.name());
		} else {
			ManagedType<?> managedType = jpaAttribute.getDeclaringType();
			if (managedType != null) {
				Class<?> clazz = managedType.getJavaType();
				try {
					Field field = clazz.getDeclaredField(jpaAttributeName);
					Column column = field.getAnnotation(Column.class);
					if (column != null) {
						mapping.setColumnName(column.name());
					}
				} catch (SecurityException e) {

				} catch (NoSuchFieldException e) {

				}
			}

		}
		((Mapping) mapping).setInternalName(jpaAttributeName);
		view.getSimpleProperty().setMapping((Mapping) mapping);
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
				buildNamespace(view.getpUnitName()) + ENTITY_CONTAINER_SUFFIX);
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
	public static void build(JPAEdmEntitySetView view) {
		FullQualifiedName fQname = view.getEdmEntitySet().getEntityType();
		view.getEdmEntitySet().setName(fQname.getName() + ENTITY_SET_SUFFIX);
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
		view.getEdmComplexType().setName(
				view.getJPAEmbeddableType().getJavaType().getSimpleName());

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
		String name = propertyView.getJPAAttribute().getName();
		String propertyName = Character.toUpperCase(name.charAt(0))
				+ name.substring(1);
		ComplexProperty complexProperty = complexView.getEdmComplexProperty();
		complexProperty.setName(propertyName);
		complexProperty.setMapping(((Mapping) new JPAEdmMappingImpl())
				.setInternalName(name));

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

		String namespace = buildNamespace(assocaitionEndView.getpUnitName());

		String name = entityTypeView.getEdmEntityType().getName();
		FullQualifiedName fQName = new FullQualifiedName(namespace, name);
		assocaitionEndView.getAssociationEnd1().setType(fQName);

		name = propertyView.getJPAAttribute().getJavaType().getSimpleName();
		fQName = new FullQualifiedName(namespace, name);
		assocaitionEndView.getAssociationEnd2().setType(fQName);

	}

	private static String buildNamespace(String name) {
		return name;
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
			association.setName(ASSOCIATION_PREFIX + end2Name + UNDERSCORE
					+ end1Name);
		} else {
			association.setName(ASSOCIATION_PREFIX + end1Name + UNDERSCORE
					+ end2Name);
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
			JPAEdmNavigationProperty jpaEdmNavigationProperty) {

		NavigationProperty navProp = jpaEdmNavigationProperty
				.getEdmNavigationProperty();
		String namespace = buildNamespace(associationView.getpUnitName());

		Association association = associationView.getEdmAssociation();
		navProp.setRelationship(new FullQualifiedName(namespace, association
				.getName()));

		FullQualifiedName associationEndTypeOne = association.getEnd1()
				.getType();

		if (propertyView.getJPAAttribute().getJavaType().getSimpleName()
				.equals(associationEndTypeOne.getName())) {
			navProp.setFromRole(association.getEnd2().getRole());
			navProp.setToRole(association.getEnd1().getRole());
		} else {

			navProp.setToRole(association.getEnd2().getRole());
			navProp.setFromRole(association.getEnd1().getRole());
		}
		Attribute<?, ?> jpaAttribute = propertyView.getJPAAttribute();
		navProp.setMapping(new Mapping().setInternalName(jpaAttribute.getName()));
		navProp.setName(jpaAttribute.getJavaType().getSimpleName()
				.concat(NAVIGATION_NAME));

	}
}
