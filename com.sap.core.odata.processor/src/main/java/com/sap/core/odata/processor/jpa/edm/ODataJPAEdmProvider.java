package com.sap.core.odata.processor.jpa.edm;

import java.util.HashMap;
import java.util.List;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.edm.provider.EntityContainerInfo;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.FunctionImport;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.processor.jpa.access.JPAController;
import com.sap.core.odata.processor.jpa.access.api.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.api.ODataJPAContext;

public class ODataJPAEdmProvider implements EdmProvider {

	private ODataJPAContext oDataJPAContext;
	private JPAEdmBuilder builder;

	private List<Schema> schemas;
	private HashMap<String, EntityType> entityTypes;
	private HashMap<String,EntityContainerInfo> entityContainerInfos;

	public ODataJPAEdmProvider() {
		builder = JPAController.getJPAEdmBuilder(oDataJPAContext);
		entityTypes = new HashMap<String, EntityType>();
	}

	public ODataJPAContext getODataJPAContext() {
		return oDataJPAContext;
	}

	public void setODataJPAContext(ODataJPAContext jpaContext) {
		this.oDataJPAContext = jpaContext;
	}

	@Override
	public EntityContainerInfo getEntityContainerInfo(String name)
			throws ODataException {
		EntityContainerInfo entityContainer = entityContainerInfos.get(name);
		if ( entityContainer == null )
		{
		
		}
		return entityContainer;
	}

	@Override
	public EntityType getEntityType(FullQualifiedName edmFQName)
			throws ODataException {
		
		EntityType entityType = entityTypes.get(edmFQName.toString());
		// Build the EntityType 
		// if EntityType is not buffered and EntityType Name is not Null
		if (entityType == null && edmFQName != null) {
			entityType = builder.getEntityType(edmFQName);
			entityTypes.put(edmFQName.toString(), entityType);
		}
		
		return entityType;
	}

	@Override
	public ComplexType getComplexType(FullQualifiedName edmFQName)
			throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Association getAssociation(FullQualifiedName edmFQName)
			throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntitySet getEntitySet(String entityContainer, String name)
			throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AssociationSet getAssociationSet(String entityContainer,
			FullQualifiedName association, String sourceEntitySetName,
			String sourceEntitySetRole) throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FunctionImport getFunctionImport(String entityContainer, String name)
			throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Schema> getSchemas() throws ODataException {
		if (schemas == null)
			schemas = builder.getSchemas();

		return schemas;

	}

}
