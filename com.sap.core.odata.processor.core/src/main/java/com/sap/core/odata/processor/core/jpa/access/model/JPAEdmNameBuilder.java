/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
import com.sap.core.odata.api.edm.provider.ComplexType;
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

  public static FullQualifiedName build(final JPAEdmBaseView view, final String name) {
    FullQualifiedName fqName = new FullQualifiedName(buildNamespace(view),
        name);
    return fqName;
  }

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
  public static void build(final JPAEdmEntityTypeView view) {

    EntityType edmEntityType = view.getEdmEntityType();
    String jpaEntityName = view.getJPAEntityType().getName();
    JPAEdmMappingModelAccess mappingModelAccess = view
        .getJPAEdmMappingModelAccess();
    String edmEntityTypeName = null;
    if (mappingModelAccess != null
        && mappingModelAccess.isMappingModelExists()) {
      edmEntityTypeName = mappingModelAccess
          .mapJPAEntityType(jpaEntityName);
    }

    JPAEdmMapping mapping = new JPAEdmMappingImpl();
    mapping.setJPAType(view.getJPAEntityType().getJavaType());

    if (edmEntityTypeName == null) {
      edmEntityTypeName = jpaEntityName;
    }
    //Setting the mapping object
    edmEntityType.setMapping(((Mapping) mapping)
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
  public static void build(final JPAEdmSchemaView view)
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
  public static void build(final JPAEdmPropertyView view, final boolean isComplexMode) {
    Attribute<?, ?> jpaAttribute = view.getJPAAttribute();
    String jpaAttributeName = jpaAttribute.getName();
    String propertyName = null;

    JPAEdmMappingModelAccess mappingModelAccess = view
        .getJPAEdmMappingModelAccess();
    if (mappingModelAccess != null
        && mappingModelAccess.isMappingModelExists()) {
      if (isComplexMode) {
        propertyName = mappingModelAccess
            .mapJPAEmbeddableTypeAttribute(view
                .getJPAEdmComplexTypeView()
                .getJPAEmbeddableType().getJavaType()
                .getSimpleName(), jpaAttributeName);
      } else {
        propertyName = mappingModelAccess.mapJPAAttribute(
            view.getJPAEdmEntityTypeView().getJPAEntityType()
                .getName(), jpaAttributeName);
      }
    }
    if (propertyName == null) {
      propertyName = Character.toUpperCase(jpaAttributeName.charAt(0))
          + jpaAttributeName.substring(1);
    }

    view.getEdmSimpleProperty().setName(propertyName);

    JPAEdmMapping mapping = new JPAEdmMappingImpl();
    ((Mapping) mapping).setInternalName(jpaAttributeName);
    mapping.setJPAType(jpaAttribute.getJavaType());

    AnnotatedElement annotatedElement = (AnnotatedElement) jpaAttribute
        .getJavaMember();
    if (annotatedElement != null) {
      Column column = annotatedElement.getAnnotation(Column.class);
      if (column != null) {
        mapping.setJPAColumnName(column.name());
      }
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
  public static void build(final JPAEdmEntityContainerView view) {
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
  public static void build(final JPAEdmEntitySetView view,
      final JPAEdmEntityTypeView entityTypeView) {
    FullQualifiedName fQname = view.getEdmEntitySet().getEntityType();
    JPAEdmMappingModelAccess mappingModelAccess = view
        .getJPAEdmMappingModelAccess();
    String entitySetName = null;
    if (mappingModelAccess != null
        && mappingModelAccess.isMappingModelExists()) {
      Mapping mapping = entityTypeView.getEdmEntityType().getMapping();
      if (mapping != null) {
        entitySetName = mappingModelAccess.mapJPAEntitySet(mapping
            .getInternalName());
      }
    }

    if (entitySetName == null) {
      entitySetName = fQname.getName() + ENTITY_SET_SUFFIX;
    }

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
  public static void build(final JPAEdmComplexType view) {

    JPAEdmMappingModelAccess mappingModelAccess = view
        .getJPAEdmMappingModelAccess();
    String jpaEmbeddableTypeName = view.getJPAEmbeddableType()
        .getJavaType().getSimpleName();
    String edmComplexTypeName = null;
    if (mappingModelAccess != null
        && mappingModelAccess.isMappingModelExists()) {
      edmComplexTypeName = mappingModelAccess
          .mapJPAEmbeddableType(jpaEmbeddableTypeName);
    }

    if (edmComplexTypeName == null) {
      edmComplexTypeName = jpaEmbeddableTypeName;
    }

    ComplexType complexType = view.getEdmComplexType();
    complexType.setName(edmComplexTypeName);
    JPAEdmMapping mapping = new JPAEdmMappingImpl();
    mapping.setJPAType(view.getJPAEmbeddableType().getJavaType());
    complexType.setMapping((Mapping) mapping);

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
  public static void build(final JPAEdmComplexPropertyView complexView,
      final JPAEdmPropertyView propertyView) {

    ComplexProperty complexProperty = complexView.getEdmComplexProperty();

    String jpaAttributeName = propertyView.getJPAAttribute().getName();
    String jpaEntityTypeName = propertyView.getJPAEdmEntityTypeView()
        .getJPAEntityType().getName();

    JPAEdmMappingModelAccess mappingModelAccess = complexView
        .getJPAEdmMappingModelAccess();
    String propertyName = null;

    if (mappingModelAccess != null
        && mappingModelAccess.isMappingModelExists()) {
      propertyName = mappingModelAccess.mapJPAAttribute(
          jpaEntityTypeName, jpaAttributeName);
    }

    if (propertyName == null) {
      propertyName = Character.toUpperCase(jpaAttributeName.charAt(0))
          + jpaAttributeName.substring(1);
    }
    // change for navigation property issue
    JPAEdmMapping mapping = new JPAEdmMappingImpl();
    ((Mapping) mapping).setInternalName(jpaAttributeName);
    mapping.setJPAType(propertyView.getJPAAttribute().getJavaType());
    complexProperty.setMapping((Mapping) mapping);

    complexProperty.setName(propertyName);

  }

  public static void build(final JPAEdmComplexPropertyView complexView, final String parentComplexTypeName)
  {
    ComplexProperty complexProperty = complexView.getEdmComplexProperty();

    JPAEdmMappingModelAccess mappingModelAccess = complexView.getJPAEdmMappingModelAccess();
    JPAEdmPropertyView propertyView = ((JPAEdmPropertyView) complexView);
    String jpaAttributeName = propertyView.getJPAAttribute().getName();
    String propertyName = null;
    if (mappingModelAccess != null
        && mappingModelAccess.isMappingModelExists()) {
      propertyName = mappingModelAccess.mapJPAEmbeddableTypeAttribute(
          parentComplexTypeName, jpaAttributeName);
    }
    if (propertyName == null) {
      propertyName = Character.toUpperCase(jpaAttributeName.charAt(0))
          + jpaAttributeName.substring(1);
    }
    JPAEdmMapping mapping = new JPAEdmMappingImpl();
    ((Mapping) mapping).setInternalName(jpaAttributeName);
    mapping.setJPAType(propertyView.getJPAAttribute().getJavaType());
    complexProperty.setMapping((Mapping) mapping);
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
  public static void build(final JPAEdmAssociationEndView assocaitionEndView,
      final JPAEdmEntityTypeView entityTypeView, final JPAEdmPropertyView propertyView) {

    String namespace = buildNamespace(assocaitionEndView);

    String name = entityTypeView.getEdmEntityType().getName();
    FullQualifiedName fQName = new FullQualifiedName(namespace, name);
    assocaitionEndView.getEdmAssociationEnd1().setType(fQName);

    name = null;
    String jpaEntityTypeName = null;
    Attribute<?, ?> jpaAttribute = propertyView.getJPAAttribute();
    if (jpaAttribute.isCollection())
      jpaEntityTypeName = ((PluralAttribute<?, ?, ?>) jpaAttribute).getElementType().getJavaType()
          .getSimpleName();
    else
      jpaEntityTypeName = propertyView.getJPAAttribute().getJavaType()
          .getSimpleName();

    JPAEdmMappingModelAccess mappingModelAccess = assocaitionEndView
        .getJPAEdmMappingModelAccess();

    if (mappingModelAccess != null
        && mappingModelAccess.isMappingModelExists()) {
      name = mappingModelAccess.mapJPAEntityType(jpaEntityTypeName);
    }

    if (name == null) {
      name = jpaEntityTypeName;
    }

    fQName = new FullQualifiedName(namespace, name);
    assocaitionEndView.getEdmAssociationEnd2().setType(fQName);

  }

  private static String buildNamespace(final JPAEdmBaseView view) {
    JPAEdmMappingModelAccess mappingModelAccess = view
        .getJPAEdmMappingModelAccess();
    String namespace = null;
    if (mappingModelAccess != null
        && mappingModelAccess.isMappingModelExists()) {
      namespace = mappingModelAccess.mapJPAPersistenceUnit(view
          .getpUnitName());
    }
    if (namespace == null) {
      namespace = view.getpUnitName();
    }

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

  public static void build(final JPAEdmAssociationView view, final int count) {
    Association association = view.getEdmAssociation();
    String associationName = null;
    String end1Name = association.getEnd1().getType().getName();
    String end2Name = association.getEnd2().getType().getName();

    if (end1Name.compareToIgnoreCase(end2Name) > 0) {
      associationName = end2Name + UNDERSCORE + end1Name;
    } else {
      associationName = end1Name + UNDERSCORE + end2Name;
    }
    if (count > 1) {
      associationName = associationName + Integer.toString(count - 1);
    }
    association.setName(associationName);

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
  public static void build(final JPAEdmAssociationSetView view) {
    AssociationSet associationSet = view.getEdmAssociationSet();

    String name = view.getEdmAssociation().getName();
    associationSet.setName(name + ASSOCIATIONSET_SUFFIX);

  }

  public static void build(final JPAEdmAssociationView associationView,
      final JPAEdmPropertyView propertyView,
      final JPAEdmNavigationPropertyView navPropertyView, final int count) {

    String toName = null;
    String fromName = null;
    String navPropName = null;
    NavigationProperty navProp = navPropertyView.getEdmNavigationProperty();
    String namespace = buildNamespace(associationView);

    Association association = associationView.getEdmAssociation();
    navProp.setRelationship(new FullQualifiedName(namespace, association
        .getName()));

    FullQualifiedName associationEndTypeOne = association.getEnd1()
        .getType();
    FullQualifiedName associationEndTypeTwo = association.getEnd2()
        .getType();

    Attribute<?, ?> jpaAttribute = propertyView.getJPAAttribute();
    navProp.setMapping(new Mapping().setInternalName(jpaAttribute.getName()));

    String jpaEntityTypeName = propertyView.getJPAEdmEntityTypeView()
        .getJPAEntityType().getName();
    JPAEdmMappingModelAccess mappingModelAccess = navPropertyView
        .getJPAEdmMappingModelAccess();

    String targetEntityTypeName = null;
    if (jpaAttribute.isCollection())
       targetEntityTypeName = ((PluralAttribute<?, ?, ?>) jpaAttribute).getElementType().getJavaType().getSimpleName();
    else
      targetEntityTypeName = jpaAttribute.getJavaType().getSimpleName();

    if (mappingModelAccess != null
        && mappingModelAccess.isMappingModelExists()) {
      navPropName = mappingModelAccess.mapJPARelationship(
          jpaEntityTypeName, jpaAttribute.getName());
      toName = mappingModelAccess.mapJPAEntityType(targetEntityTypeName);
      fromName = mappingModelAccess
          .mapJPAEntityType(jpaEntityTypeName);
    }
    if (toName == null) {
      toName = targetEntityTypeName;
    }

    if (fromName == null) {
      fromName = jpaEntityTypeName;
    }

    if (navPropName == null) {
      navPropName = toName.concat(NAVIGATION_NAME);
    }
    if (count > 1) {
      navPropName = navPropName + Integer.toString(count - 1);
    }
    navProp.setName(navPropName);

    if (toName.equals(associationEndTypeOne.getName())) {
      navProp.setFromRole(association.getEnd2().getRole());
      navProp.setToRole(association.getEnd1().getRole());
    } else if (toName.equals(associationEndTypeTwo.getName())) {

      navProp.setToRole(association.getEnd2().getRole());
      navProp.setFromRole(association.getEnd1().getRole());
    }
  }

}
