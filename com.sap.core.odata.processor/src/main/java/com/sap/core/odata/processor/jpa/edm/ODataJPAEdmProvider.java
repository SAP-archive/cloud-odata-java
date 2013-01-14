package com.sap.core.odata.processor.jpa.edm;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.edm.provider.EntityContainer;
import com.sap.core.odata.api.edm.provider.EntityContainerInfo;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.FunctionImport;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.processor.jpa.api.ODataJPAContext;
import com.sap.core.odata.processor.jpa.api.access.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.api.factory.ODataJPAFactory;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;

public class ODataJPAEdmProvider extends EdmProvider {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ODataJPAEdmProvider.class);

	private ODataJPAContext oDataJPAContext;
	private JPAEdmBuilder builder;

	private List<Schema> schemas;
	private HashMap<String, EntityType> entityTypes;
	private HashMap<String, EntityContainerInfo> entityContainerInfos;
	private HashMap<String, ComplexType> complexTypes;

	public ODataJPAEdmProvider() {
		entityTypes = new HashMap<String, EntityType>();
		entityContainerInfos = new HashMap<String, EntityContainerInfo>();
		complexTypes = new HashMap<String, ComplexType>();
	}

	public ODataJPAEdmProvider(ODataJPAContext oDataJPAContext) {
		entityTypes = new HashMap<String, EntityType>();
		entityContainerInfos = new HashMap<String, EntityContainerInfo>();
		complexTypes = new HashMap<String, ComplexType>();
		builder = ODataJPAFactory.createFactory().getJPAAccessFactory().getJPAEdmBuilder(oDataJPAContext);
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

		if (entityContainerInfos.containsKey(name))
			return entityContainerInfos.get(name);

		else {

			if (schemas == null)
				getSchemas();

			for (EntityContainer container : schemas.get(0)
					.getEntityContainers()) {
				if (name == null && container.isDefaultEntityContainer()) {
					entityContainerInfos.put(name, container);
					return container;
				} else if (name != null && name.equals(container.getName())) {
					return container;
				}
			}
		}
		LOGGER.error("Invalid Entity Container : "+name);
		throw ODataJPAModelException
				.throwException(ODataJPAModelException.INVALID_ENTITYCONTAINER
						.addContent(name), null);
	}

	@Override
	public EntityType getEntityType(FullQualifiedName edmFQName)
			throws ODataException {
		if (edmFQName != null) {
			// Load from Buffer
			if (edmFQName != null
					&& entityTypes.containsKey(edmFQName.toString()))
				return entityTypes.get(edmFQName.toString());
			else if (schemas == null)
				getSchemas();

			if (edmFQName != null) {
				for (Schema schema : schemas) {
					if (schema.getNamespace().equals(edmFQName.getNamespace())) {
						for (EntityType et : schema.getEntityTypes()) {
							if (et.getName().equals(edmFQName.getName())) {
								entityTypes.put(edmFQName.toString(), et);
								return et;
							}
						}
					}
				}
			}
			LOGGER.error("Invalid Entity Type : "+edmFQName.toString());
			throw ODataJPAModelException.throwException(
					ODataJPAModelException.INVALID_ENTITY_TYPE
							.addContent(edmFQName.toString()), null);
		}
		return null;
	}

	@Override
	public ComplexType getComplexType(FullQualifiedName edmFQName)
			throws ODataException {
		if (edmFQName != null) {
			if (edmFQName != null
					&& complexTypes.containsKey(edmFQName.toString()))
				return complexTypes.get(edmFQName.toString());
			else if (schemas == null)
				getSchemas();

			if (edmFQName != null)
				for (Schema schema : schemas) {
					if (schema.getNamespace().equals(edmFQName.getNamespace())) {
						for (ComplexType ct : schema.getComplexTypes()) {
							if (ct.getName().equals(edmFQName.getName())) {
								complexTypes.put(edmFQName.toString(), ct);
								return ct;
							}
						}
					}
				}
			LOGGER.error("Invalid Complex Type : "+edmFQName.toString());
			throw ODataJPAModelException.throwException(
					ODataJPAModelException.INVALID_COMPLEX_TYPE
							.addContent(edmFQName.toString()), null);
		}
		return null;
	}

	@Override
	public Association getAssociation(FullQualifiedName edmFQName)
			throws ODataException {
		LOGGER.error("Invalid Association : "+edmFQName.toString());
		throw ODataJPAModelException.throwException(
				ODataJPAModelException.INVALID_ASSOCIATION.addContent(edmFQName
						.toString()), null);
	}

	@Override
	public EntitySet getEntitySet(String entityContainer, String name)
			throws ODataException {

		EntityContainer container = null;
		if (!entityContainerInfos.containsKey(entityContainer))
			container = (EntityContainer) getEntityContainerInfo(entityContainer);
		else
			container = (EntityContainer) entityContainerInfos
					.get(entityContainer);

		if (container != null && name != null)
			for (EntitySet es : container.getEntitySets())
				if (name.equals(es.getName()))
					return es;
		LOGGER.error("Invalid Entity Set : "+name);
		throw ODataJPAModelException
				.throwException(ODataJPAModelException.INVALID_ENTITYSET
						.addContent(name), null);
	}

	@Override
	public AssociationSet getAssociationSet(String entityContainer,
			FullQualifiedName association, String sourceEntitySetName,
			String sourceEntitySetRole) throws ODataException {
		LOGGER.error("Invalid Association Set : "+association.toString());
		throw ODataJPAModelException.throwException(
				ODataJPAModelException.INVALID_ENTITYSET.addContent(association
						.toString()), null);
	}

	@Override
	public FunctionImport getFunctionImport(String entityContainer, String name)
			throws ODataException {
		LOGGER.error("Invalid Entity Set : "+name);
		throw ODataJPAModelException
				.throwException(ODataJPAModelException.INVALID_ENTITYSET
						.addContent(name), null);
	}

	@Override
	public List<Schema> getSchemas() throws ODataException {
		if (schemas == null && builder != null)
			schemas = builder.getSchemas();
		if (builder == null){
			LOGGER.error("Builder is NULL");
			throw ODataJPAModelException.throwException(
					ODataJPAModelException.BUILDER_NULL, null);
		}
			
		return schemas;

	}

}
