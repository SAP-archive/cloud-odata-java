package com.sap.core.odata.processor.jpa.model;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.SingularAttribute;

import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.ComplexProperty;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.Facets;
import com.sap.core.odata.api.edm.provider.Property;
import com.sap.core.odata.api.edm.provider.SimpleProperty;
import com.sap.core.odata.processor.jpa.access.model.JPAEdmNameBuilder;
import com.sap.core.odata.processor.jpa.access.model.JPATypeConvertor;
import com.sap.core.odata.processor.jpa.api.access.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmAssociationEndView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmAssociationView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmComplexPropertyView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmComplexTypeView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntityTypeView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmKeyView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmNavigationPropertyView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmPropertyView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmReferentialContraintView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmSchemaView;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;

public class JPAEdmProperty extends JPAEdmBaseViewImpl implements
		JPAEdmPropertyView, JPAEdmComplexPropertyView {

	private JPAEdmSchemaView schemaView;
	private JPAEdmEntityTypeView entityTypeView;
	private JPAEdmComplexTypeView complexTypeView;
	private JPAEdmNavigationPropertyView navigationPropertyView = null;

	private JPAEdmKeyView keyView;
	private List<Property> properties;
	private SimpleProperty currentSimpleProperty = null;
	private ComplexProperty currentComplexProperty = null;
	private Attribute<?, ?> currentAttribute;
	private boolean isBuildModeComplexType;

	public JPAEdmProperty(JPAEdmSchemaView view) {
		super(view);
		this.schemaView = view;
		this.entityTypeView = this.schemaView.getJPAEdmEntityContainerView()
				.getJPAEdmEntitySetView().getJPAEdmEntityTypeView();
		this.complexTypeView = this.schemaView.getJPAEdmComplexTypeView();
		navigationPropertyView = new JPAEdmNavigationProperty(schemaView);
		isBuildModeComplexType = false;
	}

	public JPAEdmProperty(JPAEdmSchemaView schemaView,
			JPAEdmComplexTypeView view) {
		super(view);
		this.schemaView = schemaView;
		this.complexTypeView = view;
		this.isBuildModeComplexType = true;
	}

	@Override
	public JPAEdmBuilder getBuilder() {
		return new JPAEdmPropertyBuilder();
	}

	@Override
	public List<Property> getPropertyList() {
		return properties;
	}

	@Override
	public JPAEdmKeyView getJPAEdmKeyView() {
		return keyView;
	}

	@Override
	public SimpleProperty getSimpleProperty() {
		return currentSimpleProperty;
	}

	@Override
	public Attribute<?, ?> getJPAAttribute() {
		return currentAttribute;
	}

	@Override
	public ComplexProperty getEdmComplexProperty() {
		return currentComplexProperty;
	}
	
	@Override
	public JPAEdmNavigationPropertyView getJPAEdmNavigationPropertyView()
	{
		return navigationPropertyView;
	}

	private class JPAEdmPropertyBuilder implements JPAEdmBuilder {
		/*
		 * 
		 * Each call to build method creates a new EDM Property List. 
		 * The Property List can be created either by an Entity type or
		 * ComplexType. The flag isBuildModeComplexType tells if the
		 * Properties are built for complex type or for Entity Type.
		 * 
		 * While Building Properties Associations are built. However
		 * the associations thus built does not contain Referential
		 * constraint. Associations thus built only contains
		 * information about Referential constraints. Adding of
		 * referential constraints to Associations is the taken care
		 * by Schema.
		 * 
		 * Building Properties is divided into four parts
		 * 	A) Building Simple Properties
		 * 	B) Building Complex Properties
		 * 	C) Building Associations
		 * 	D) Building Navigation Properties
		 *  
		 * ************************************************************
		 * 					Build EDM Schema - STEPS
		 * ************************************************************
		 * A) 	Building Simple Properties:
		 * 
		 * 	1) 	Fetch JPA Attribute List from 
		 * 			A) Complex Type
		 * 			B) Entity Type
		 * 	  	depending on isBuildModeComplexType.
		 * B)	Building Complex Properties
		 * C)	Building Associations
		 * D)	Building Navigation Properties
			
		 * ************************************************************
		 * 					Build EDM Schema - STEPS
		 * ************************************************************
		 *
		 */
		@Override
		public void build() throws ODataJPAModelException {

			JPAEdmBuilder keyViewBuilder = null;

			properties = new ArrayList<Property>();

			Set<?> jpaAttributes = null;

			if (isBuildModeComplexType) {
				jpaAttributes = complexTypeView.getJPAEmbeddableType()
						.getAttributes();
			} else {

				jpaAttributes = entityTypeView.getJPAEntityType()
						.getAttributes();
			}

			for (Object jpaAttribute : jpaAttributes) {
				currentAttribute = (Attribute<?, ?>) jpaAttribute;

				PersistentAttributeType attributeType = currentAttribute
						.getPersistentAttributeType();

				switch (attributeType) {
				case BASIC:
					
					currentSimpleProperty = new SimpleProperty();
					JPAEdmNameBuilder
							.build((JPAEdmPropertyView) JPAEdmProperty.this);

					EdmSimpleTypeKind simpleTypeKind = JPATypeConvertor
							.convertToEdmSimpleType(currentAttribute
									.getJavaType());

					currentSimpleProperty.setType(simpleTypeKind);
					currentSimpleProperty
							.setFacets(setFacets(currentAttribute));

					properties.add(currentSimpleProperty);
					
					if (((SingularAttribute<?, ?>) currentAttribute).isId()) {
						if (keyView == null) {
							keyView = new JPAEdmKey(JPAEdmProperty.this);
							keyViewBuilder = keyView.getBuilder();
						}

						keyViewBuilder.build();
					}
					
					break;
				case EMBEDDED:
					ComplexType complexType = complexTypeView
							.searchComplexType(currentAttribute.getJavaType().getName());

					if (complexType == null) {
						JPAEdmComplexTypeView complexTypeViewLocal = new JPAEdmComplexType(
								schemaView);

						complexTypeViewLocal.getBuilder().build();
						complexType = complexTypeViewLocal.getEdmComplexType();
						complexTypeView.addCompleTypeView(complexTypeViewLocal);

					}

					if (isBuildModeComplexType == false
							&& entityTypeView.getJPAEntityType().getIdType()
									.getJavaType()
									.equals(currentAttribute.getJavaType())) {

						if (keyView == null)
							keyView = new JPAEdmKey(complexTypeView,
									JPAEdmProperty.this);
						keyView.getBuilder().build();
					}
					else{
						currentComplexProperty = new ComplexProperty();
						JPAEdmNameBuilder
								.build((JPAEdmComplexPropertyView) JPAEdmProperty.this,JPAEdmProperty.this);
						currentComplexProperty.setType(new FullQualifiedName(
								schemaView.getEdmSchema().getNamespace(),
								complexType.getName()));
						currentComplexProperty
								.setFacets(setFacets(currentAttribute));
						properties.add(currentComplexProperty);
					}

					break;
				case MANY_TO_MANY :
				case ONE_TO_MANY:
				case ONE_TO_ONE :
				case MANY_TO_ONE:
					
					JPAEdmAssociationEndView associationEndView = new JPAEdmAssociationEnd(entityTypeView,JPAEdmProperty.this);
					associationEndView.getBuilder().build( );
					
					JPAEdmAssociationView associationView = schemaView.getJPAEdmAssociationView();
					if(associationView.searchAssociation(associationEndView) == null){
						JPAEdmAssociationView associationViewLocal = new JPAEdmAssociation(associationEndView);
						associationViewLocal.getBuilder().build();
						associationView.addJPAEdmAssociationView(associationViewLocal);
					}
					
					JPAEdmReferentialContraintView refView = new JPAEdmReferentialConstraint(associationView,JPAEdmProperty.this);
//					refView.getBuilder().build( );
//					associationView.addJPAEdmRefConstraintView(refView);
					if(navigationPropertyView == null)
					{
						navigationPropertyView = new JPAEdmNavigationProperty(schemaView);
					}
					JPAEdmNavigationPropertyView localNavigationPropertyView = new JPAEdmNavigationProperty(schemaView,associationView,JPAEdmProperty.this);
					localNavigationPropertyView.getBuilder().build();
					navigationPropertyView.addJPAEdmNavigationPropertyView(localNavigationPropertyView);
					break;
				default:
					break;
				}
			}

		}
		
		private EdmFacets setFacets(Attribute<?, ?> jpaAttribute)
				throws ODataJPAModelException {

			Facets facets = new Facets();
			if (jpaAttribute.getJavaMember() instanceof AnnotatedElement) {
				Column column = ((AnnotatedElement) jpaAttribute
						.getJavaMember()).getAnnotation(Column.class);
				if (column != null) {
					EdmSimpleTypeKind attrEmdType = JPATypeConvertor
							.convertToEdmSimpleType(jpaAttribute.getJavaType());
					if (column.nullable()) {
						facets.setNullable(true);
					}
					if (column.length() != 0
							&& attrEmdType.equals(EdmSimpleTypeKind.String)) {
						facets.setMaxLength(column.length());
					}
					if (column.precision() != 0
							&& attrEmdType.equals(EdmSimpleTypeKind.Double)) {
						facets.setPrecision(column.precision());
					}
				}
				return facets;
			}
			return facets;
		}
	}

}
