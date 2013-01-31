package com.sap.core.odata.processor.jpa.testdata;

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
import com.sap.core.odata.processor.jpa.api.access.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmAssociationEndView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmAssociationSetView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmAssociationView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmBaseView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmComplexPropertyView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmComplexTypeView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntityContainerView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntitySetView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntityTypeView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmKeyView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmModelView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmNavigationPropertyView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmPropertyView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmReferentialConstraintView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmSchemaView;

public abstract class JPAEdmTestModelView implements
JPAEdmAssociationEndView,
JPAEdmAssociationSetView,
JPAEdmAssociationView,
JPAEdmBaseView,
JPAEdmComplexPropertyView,
JPAEdmComplexTypeView,
JPAEdmEntityContainerView,
JPAEdmEntitySetView,
JPAEdmEntityTypeView,
JPAEdmKeyView,
JPAEdmModelView,
JPAEdmNavigationPropertyView,
JPAEdmPropertyView,
JPAEdmReferentialConstraintView,
JPAEdmSchemaView
{

	@Override
	public ReferentialConstraint getEdmReferentialConstraint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRelationShipName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isExists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EntityType searchEdmEntityType(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPAEdmReferentialConstraintView getJPAEdmReferentialConstraintView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Schema getEdmSchema() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPAEdmAssociationView getJPAEdmAssociationView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPAEdmComplexTypeView getJPAEdmComplexTypeView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPAEdmEntityContainerView getJPAEdmEntityContainerView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attribute<?, ?> getJPAAttribute() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPAEdmKeyView getJPAEdmKeyView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Property> getPropertyList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SimpleProperty getSimpleProperty() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPAEdmSchemaView getSchemaView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Key getEdmKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EntityType> getConsistentEdmEntityTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityType getEdmEntityType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public javax.persistence.metamodel.EntityType<?> getJPAEntityType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EntitySet> getConsistentEntitySetList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntitySet getEdmEntitySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPAEdmEntityTypeView getJPAEdmEntityTypeView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EntityContainer> getConsistentEdmEntityContainerList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPAEdmAssociationSetView getEdmAssociationSetView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityContainer getEdmEntityContainer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPAEdmEntitySetView getJPAEdmEntitySetView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addCompleTypeView(JPAEdmComplexTypeView arg0) {
		// TODO Auto-generated method stub
		
	}

	/*@Override
	public void expandEdmComplexType(ComplexType arg0, List<Property> arg1) {
		// TODO Auto-generated method stub
		
	}*/

	@Override
	public List<ComplexType> getConsistentEdmComplexTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ComplexType getEdmComplexType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EmbeddableType<?> getJPAEmbeddableType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ComplexType searchComplexType(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ComplexType searchComplexType(FullQualifiedName arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ComplexProperty getEdmComplexProperty() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clean() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JPAEdmBuilder getBuilder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Metamodel getJPAMetaModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getpUnitName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isConsistent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addJPAEdmAssociationView(JPAEdmAssociationView arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addJPAEdmRefConstraintView(JPAEdmReferentialConstraintView arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Association> getConsistentEdmAssociationList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Association searchAssociation(JPAEdmAssociationEndView arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AssociationSet> getConsistentEdmAssociationSetList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Association getEdmAssociation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AssociationSet getEdmAssociationSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean compare(AssociationEnd arg0, AssociationEnd arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public AssociationEnd getAssociationEnd1() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AssociationEnd getAssociationEnd2() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void addJPAEdmNavigationPropertyView(
			JPAEdmNavigationPropertyView view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<NavigationProperty> getConsistentEdmNavigationProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NavigationProperty getEdmNavigationProperty() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPAEdmNavigationPropertyView getJPAEdmNavigationPropertyView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void expandEdmComplexType(ComplexType complexType,
			List<Property> expandedPropertyList, String embeddablePropertyName) {
		// TODO Auto-generated method stub
		
	}
}
