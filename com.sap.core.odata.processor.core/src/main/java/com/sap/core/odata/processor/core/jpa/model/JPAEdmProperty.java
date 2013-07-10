package com.sap.core.odata.processor.core.jpa.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.SingularAttribute;

import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.ComplexProperty;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.Property;
import com.sap.core.odata.api.edm.provider.SimpleProperty;
import com.sap.core.odata.processor.api.jpa.access.JPAEdmBuilder;
import com.sap.core.odata.processor.api.jpa.access.JPAEdmMappingModelAccess;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationEndView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmComplexPropertyView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmComplexTypeView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityTypeView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmKeyView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmNavigationPropertyView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmPropertyView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmReferentialConstraintView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmSchemaView;
import com.sap.core.odata.processor.core.jpa.access.model.JPAEdmNameBuilder;
import com.sap.core.odata.processor.core.jpa.access.model.JPATypeConvertor;

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
  private Map<String, Integer> associationCount;

  public JPAEdmProperty(final JPAEdmSchemaView view) {
    super(view);
    schemaView = view;
    entityTypeView = schemaView.getJPAEdmEntityContainerView()
        .getJPAEdmEntitySetView().getJPAEdmEntityTypeView();
    complexTypeView = schemaView.getJPAEdmComplexTypeView();
    navigationPropertyView = new JPAEdmNavigationProperty(schemaView);
    isBuildModeComplexType = false;
    associationCount = new HashMap<String, Integer>();
  }

  public JPAEdmProperty(final JPAEdmSchemaView schemaView,
      final JPAEdmComplexTypeView view) {
    super(view);
    this.schemaView = schemaView;
    complexTypeView = view;
    isBuildModeComplexType = true;
  }

  @Override
  public JPAEdmBuilder getBuilder() {
    if (builder == null) {
      builder = new JPAEdmPropertyBuilder();
    }

    return builder;
  }

  @Override
  public List<Property> getEdmPropertyList() {
    return properties;
  }

  @Override
  public JPAEdmKeyView getJPAEdmKeyView() {
    return keyView;
  }

  @Override
  public SimpleProperty getEdmSimpleProperty() {
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
    public void build() throws ODataJPAModelException, ODataJPARuntimeException {

      JPAEdmBuilder keyViewBuilder = null;

      properties = new ArrayList<Property>();

      List<Attribute<?, ?>> jpaAttributes = null;
      String currentEntityName = null;
      String targetEntityName = null;
      String entityTypeName = null;
      if (isBuildModeComplexType) {
        jpaAttributes = sortInAscendingOrder(complexTypeView.getJPAEmbeddableType()
            .getAttributes());
        entityTypeName = complexTypeView.getJPAEmbeddableType().getJavaType()
            .getSimpleName();
      } else {
        jpaAttributes = sortInAscendingOrder(entityTypeView.getJPAEntityType()
            .getAttributes());
        entityTypeName = entityTypeView.getJPAEntityType().getName();
      }

      for (Object jpaAttribute : jpaAttributes) {
        currentAttribute = (Attribute<?, ?>) jpaAttribute;

        // Check for need to Exclude 
        if (isExcluded((JPAEdmPropertyView) JPAEdmProperty.this, entityTypeName, currentAttribute.getName())) {
          continue;
        }

        PersistentAttributeType attributeType = currentAttribute
            .getPersistentAttributeType();

        switch (attributeType) {
        case BASIC:

          currentSimpleProperty = new SimpleProperty();
          JPAEdmNameBuilder
              .build((JPAEdmPropertyView) JPAEdmProperty.this, isBuildModeComplexType);

          EdmSimpleTypeKind simpleTypeKind = JPATypeConvertor
              .convertToEdmSimpleType(currentAttribute
                  .getJavaType(), currentAttribute);

          currentSimpleProperty.setType(simpleTypeKind);
          JPAEdmFacets.setFacets(currentAttribute, currentSimpleProperty);

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
              .searchEdmComplexType(currentAttribute.getJavaType().getName());

          if (complexType == null) {
            JPAEdmComplexTypeView complexTypeViewLocal = new JPAEdmComplexType(
                schemaView, currentAttribute);
            complexTypeViewLocal.getBuilder().build();
            complexType = complexTypeViewLocal.getEdmComplexType();
            complexTypeView.addJPAEdmCompleTypeView(complexTypeViewLocal);

          }

          if (isBuildModeComplexType == false
              && entityTypeView.getJPAEntityType().getIdType()
                  .getJavaType()
                  .equals(currentAttribute.getJavaType())) {

            if (keyView == null) {
              keyView = new JPAEdmKey(complexTypeView,
                  JPAEdmProperty.this);
            }
            keyView.getBuilder().build();
            complexTypeView.expandEdmComplexType(complexType, properties, currentAttribute.getName());
          }
          else {
            currentComplexProperty = new ComplexProperty();
            if (isBuildModeComplexType) {
              JPAEdmNameBuilder
                  .build((JPAEdmComplexPropertyView) JPAEdmProperty.this,
                      complexTypeView.getJPAEmbeddableType().getJavaType().getSimpleName());
            } else {
              JPAEdmNameBuilder
                  .build((JPAEdmComplexPropertyView) JPAEdmProperty.this,
                      JPAEdmProperty.this);
            }
            currentComplexProperty.setType(new FullQualifiedName(
                schemaView.getEdmSchema().getNamespace(),
                complexType.getName()));
            //            currentComplexProperty
            //                .setFacets(setFacets(currentAttribute));
            properties.add(currentComplexProperty);
            List<String> nonKeyComplexTypes = schemaView.getNonKeyComplexTypeList();
            if (!nonKeyComplexTypes.contains(currentComplexProperty.getType().getName()))
            {
              schemaView.addNonKeyComplexName(currentComplexProperty.getType().getName());
            }
          }

          break;
        case MANY_TO_MANY:
        case ONE_TO_MANY:
        case ONE_TO_ONE:
        case MANY_TO_ONE:

          JPAEdmAssociationEndView associationEndView = new JPAEdmAssociationEnd(entityTypeView, JPAEdmProperty.this);
          associationEndView.getBuilder().build();
          JPAEdmAssociationView associationView = schemaView.getJPAEdmAssociationView();
          if (associationView.searchAssociation(associationEndView) == null) {
            int count = associationView.getNumberOfAssociationsWithSimilarEndPoints(associationEndView);
            JPAEdmAssociationView associationViewLocal = new JPAEdmAssociation(associationEndView, entityTypeView, JPAEdmProperty.this, count);
            associationViewLocal.getBuilder().build();
            associationView.addJPAEdmAssociationView(associationViewLocal, associationEndView);
          }

          JPAEdmReferentialConstraintView refConstraintView = new JPAEdmReferentialConstraint(
              associationView, entityTypeView, JPAEdmProperty.this);
          refConstraintView.getBuilder().build();

          if (refConstraintView.isExists()) {
            associationView.addJPAEdmRefConstraintView(refConstraintView);
          }

          if (navigationPropertyView == null)
          {
            navigationPropertyView = new JPAEdmNavigationProperty(schemaView);
          }
          currentEntityName = entityTypeView.getJPAEntityType().getName();
          targetEntityName = currentAttribute.getJavaType().getSimpleName();
          Integer sequenceNumber = associationCount.get(currentEntityName + targetEntityName);
          if (sequenceNumber == null) {
            sequenceNumber = new Integer(1);
          } else {
            sequenceNumber = new Integer(sequenceNumber.intValue() + 1);
          }
          associationCount.put(currentEntityName + targetEntityName, sequenceNumber);
          JPAEdmNavigationPropertyView localNavigationPropertyView = new JPAEdmNavigationProperty(associationView, JPAEdmProperty.this, sequenceNumber.intValue());
          localNavigationPropertyView.getBuilder().build();
          navigationPropertyView.addJPAEdmNavigationPropertyView(localNavigationPropertyView);
          break;
        default:
          break;
        }
      }

    }

    @SuppressWarnings("rawtypes")
    private List<Attribute<?, ?>> sortInAscendingOrder(final Set<?> jpaAttributes) {
      List<Attribute<?, ?>> jpaAttributeList = new ArrayList<Attribute<?, ?>>();
      Iterator itr = null;
      Attribute<?, ?> smallestJpaAttribute;
      Attribute<?, ?> currentJpaAttribute;
      while (!jpaAttributes.isEmpty()) {
        itr = jpaAttributes.iterator();
        smallestJpaAttribute = (Attribute<?, ?>) itr.next();
        while (itr.hasNext()) {
          currentJpaAttribute = (Attribute<?, ?>) itr.next();
          if (smallestJpaAttribute.getName().compareTo(currentJpaAttribute.getName()) > 0) {
            smallestJpaAttribute = currentJpaAttribute;
          }
        }
        jpaAttributeList.add(smallestJpaAttribute);
        jpaAttributes.remove(smallestJpaAttribute);
      }
      return jpaAttributeList;
    }
  }

  @Override
  public JPAEdmEntityTypeView getJPAEdmEntityTypeView() {
    return entityTypeView;
  }

  @Override
  public JPAEdmComplexTypeView getJPAEdmComplexTypeView() {
    return complexTypeView;
  }

  private boolean isExcluded(final JPAEdmPropertyView jpaEdmPropertyView, final String jpaEntityTypeName, final String jpaAttributeName) {
    JPAEdmMappingModelAccess mappingModelAccess = jpaEdmPropertyView
        .getJPAEdmMappingModelAccess();
    boolean isExcluded = false;
    if (mappingModelAccess != null && mappingModelAccess.isMappingModelExists()) {
      // Exclusion of a simple property in a complex type
      if (isBuildModeComplexType && mappingModelAccess.checkExclusionOfJPAEmbeddableAttributeType(jpaEntityTypeName, jpaAttributeName)
          // Exclusion of a simple property of an Entity Type
          || (!isBuildModeComplexType && mappingModelAccess.checkExclusionOfJPAAttributeType(jpaEntityTypeName, jpaAttributeName))) {
        isExcluded = true;
      }
    }
    return isExcluded;
  }
}
