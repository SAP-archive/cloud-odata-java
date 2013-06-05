package com.sap.core.odata.processor.core.jpa.model;

import java.util.HashMap;
import java.util.List;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EmbeddableType;
import javax.persistence.metamodel.Metamodel;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationEnd;
import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.api.edm.provider.ComplexProperty;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.EntityContainer;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.Key;
import com.sap.core.odata.api.edm.provider.NavigationProperty;
import com.sap.core.odata.api.edm.provider.Property;
import com.sap.core.odata.api.edm.provider.ReferentialConstraint;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.api.edm.provider.SimpleProperty;
import com.sap.core.odata.processor.api.jpa.access.JPAEdmBuilder;
import com.sap.core.odata.processor.api.jpa.access.JPAEdmMappingModelAccess;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationEndView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationSetView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmBaseView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmComplexPropertyView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmComplexTypeView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityContainerView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntitySetView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityTypeView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmExtension;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmKeyView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmModelView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmNavigationPropertyView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmPropertyView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmReferentialConstraintView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmSchemaView;

public class JPAEdmTestModelView implements JPAEdmAssociationEndView,
    JPAEdmAssociationSetView, JPAEdmAssociationView, JPAEdmBaseView,
    JPAEdmComplexPropertyView, JPAEdmComplexTypeView,
    JPAEdmEntityContainerView, JPAEdmEntitySetView, JPAEdmEntityTypeView,
    JPAEdmKeyView, JPAEdmModelView, JPAEdmNavigationPropertyView,
    JPAEdmPropertyView, JPAEdmReferentialConstraintView, JPAEdmSchemaView {

  protected JPAEdmMappingModelAccess mappingModelAccess;

  @Override
  public Schema getEdmSchema() {
    return null;
  }

  @Override
  public JPAEdmAssociationView getJPAEdmAssociationView() {
    return null;
  }

  @Override
  public JPAEdmComplexTypeView getJPAEdmComplexTypeView() {
    return null;
  }

  @Override
  public JPAEdmEntityContainerView getJPAEdmEntityContainerView() {
    return null;
  }

  @Override
  public Attribute<?, ?> getJPAAttribute() {
    return null;
  }

  @Override
  public JPAEdmKeyView getJPAEdmKeyView() {
    return null;
  }

  @Override
  public List<Property> getEdmPropertyList() {
    return null;
  }

  @Override
  public SimpleProperty getEdmSimpleProperty() {
    return null;
  }

  @Override
  public JPAEdmSchemaView getEdmSchemaView() {
    return null;
  }

  @Override
  public Key getEdmKey() {
    return null;
  }

  @Override
  public List<EntityType> getConsistentEdmEntityTypes() {
    return null;
  }

  @Override
  public EntityType getEdmEntityType() {
    return null;
  }

  @Override
  public javax.persistence.metamodel.EntityType<?> getJPAEntityType() {
    return null;
  }

  @Override
  public List<EntitySet> getConsistentEdmEntitySetList() {
    return null;
  }

  @Override
  public EntitySet getEdmEntitySet() {
    return null;
  }

  @Override
  public JPAEdmEntityTypeView getJPAEdmEntityTypeView() {
    return null;
  }

  @Override
  public List<EntityContainer> getConsistentEdmEntityContainerList() {
    return null;
  }

  @Override
  public JPAEdmAssociationSetView getEdmAssociationSetView() {
    return null;
  }

  @Override
  public EntityContainer getEdmEntityContainer() {
    return null;
  }

  @Override
  public JPAEdmEntitySetView getJPAEdmEntitySetView() {
    return null;
  }

  @Override
  public void addJPAEdmCompleTypeView(final JPAEdmComplexTypeView arg0) {

  }

  @Override
  public List<ComplexType> getConsistentEdmComplexTypes() {
    return null;
  }

  @Override
  public ComplexType getEdmComplexType() {
    return null;
  }

  @Override
  public EmbeddableType<?> getJPAEmbeddableType() {
    return null;
  }

  @Override
  public ComplexType searchEdmComplexType(final String arg0) {
    return null;
  }

  @Override
  public ComplexType searchEdmComplexType(final FullQualifiedName arg0) {
    return null;
  }

  @Override
  public ComplexProperty getEdmComplexProperty() {
    return null;
  }

  @Override
  public void clean() {

  }

  @Override
  public JPAEdmBuilder getBuilder() {
    return null;
  }

  @Override
  public Metamodel getJPAMetaModel() {
    return null;
  }

  @Override
  public String getpUnitName() {
    return null;
  }

  @Override
  public boolean isConsistent() {
    return false;
  }

  @Override
  public void addJPAEdmRefConstraintView(final JPAEdmReferentialConstraintView arg0) {

  }

  @Override
  public ReferentialConstraint getEdmReferentialConstraint() {
    return null;
  }

  @Override
  public String getEdmRelationShipName() {
    return null;
  }

  @Override
  public boolean isExists() {
    return false;
  }

  @Override
  public EntityType searchEdmEntityType(final String arg0) {
    return null;
  }

  @Override
  public JPAEdmReferentialConstraintView getJPAEdmReferentialConstraintView() {
    return null;
  }

  @Override
  public List<Association> getConsistentEdmAssociationList() {
    return null;
  }

  @Override
  public Association searchAssociation(final JPAEdmAssociationEndView arg0) {
    return null;
  }

  @Override
  public List<AssociationSet> getConsistentEdmAssociationSetList() {
    return null;
  }

  @Override
  public Association getEdmAssociation() {
    return null;
  }

  @Override
  public AssociationSet getEdmAssociationSet() {
    return null;
  }

  @Override
  public boolean compare(final AssociationEnd arg0, final AssociationEnd arg1) {
    return false;
  }

  @Override
  public AssociationEnd getEdmAssociationEnd1() {
    return null;
  }

  @Override
  public AssociationEnd getEdmAssociationEnd2() {
    return null;
  }

  @Override
  public JPAEdmNavigationPropertyView getJPAEdmNavigationPropertyView() {
    return null;
  }

  @Override
  public void addJPAEdmNavigationPropertyView(
      final JPAEdmNavigationPropertyView view) {

  }

  @Override
  public List<NavigationProperty> getConsistentEdmNavigationProperties() {
    return null;
  }

  @Override
  public NavigationProperty getEdmNavigationProperty() {
    return null;
  }

  @Override
  public void expandEdmComplexType(final ComplexType complexType,
      final List<Property> expandedPropertyList, final String embeddablePropertyName) {

  }

  @Override
  public List<String> getNonKeyComplexTypeList() {
    return null;
  }

  @Override
  public void addNonKeyComplexName(final String complexTypeName) {}

  @Override
  public JPAEdmMappingModelAccess getJPAEdmMappingModelAccess() {
    return null;
  }

  @Override
  public void registerOperations(final Class<?> customClass, final String[] methodNames) {
    // Do nothing
  }

  @Override
  public HashMap<Class<?>, String[]> getRegisteredOperations() {
    return null;
  }

  @Override
  public JPAEdmExtension getJPAEdmExtension() {
    return null;
  }

  @Override
  public void addJPAEdmAssociationView(final JPAEdmAssociationView associationView,
      final JPAEdmAssociationEndView associationEndView) {
    // TODO Auto-generated method stub

  }

  @Override
  public int getNumberOfAssociationsWithSimilarEndPoints(
      final JPAEdmAssociationEndView view) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public String getJoinColumnName() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getJoinColumnReferenceColumnName() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getMappedByName() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getOwningPropertyName() {
    // TODO Auto-generated method stub
    return null;
  }

}
