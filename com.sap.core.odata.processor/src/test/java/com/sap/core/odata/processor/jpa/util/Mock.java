package com.sap.core.odata.processor.jpa.util;
import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.EntityContainer;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.processor.jpa.access.api.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.api.ODataJPAContext;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.util.MockData;
public class Mock {
	
	public static JPAEdmBuilder mockjpaEdmBuilder() throws ODataJPAModelException
	{
		JPAEdmBuilder builder = EasyMock.createMock(JPAEdmBuilder.class);
		EasyMock.expect(builder.getSchemas()).andReturn(mockSchemas()).times(10);
		EasyMock.replay(builder);
		return builder;
		
	}

	private static List<Schema> mockSchemas() {
		List<Schema> schemas = new ArrayList<Schema>();
		schemas.add(mockSchema());
		return schemas;
	}

	private static Schema mockSchema() {
		Schema schema = new Schema();
		schema.setNamespace(MockData.NAME_SPACE);
		schema.setEntityTypes(mockEntityTypes());
		schema.setEntityContainers(mockEnityContainers());
		return schema;
	}

	private static List<EntityContainer> mockEnityContainers() {
		List<EntityContainer> entityContainers = new ArrayList<EntityContainer>();
		entityContainers.add(mockEntityContainer(MockData.ENTITY_CONTAINER_NAME));
		return entityContainers;
	}

	private static EntityContainer mockEntityContainer(
			String entityContainerName) {
		EntityContainer entityContainer = new EntityContainer();
		entityContainer.setName(entityContainerName);
		entityContainer.setDefaultEntityContainer(true);
		entityContainer.setEntitySets(mockEntitySets());
		return entityContainer;
	}

	private static List<EntitySet> mockEntitySets() {
		List<EntitySet> entitySets = new ArrayList<EntitySet>();
		entitySets.add(mockEntitySet(MockData.ENTITY_NAME_1));
		entitySets.add(mockEntitySet(MockData.ENTITY_NAME_2));
		return entitySets;
	}

	private static EntitySet mockEntitySet(String entityName) {
		EntitySet entitySet = new EntitySet();
		entitySet.setName(entityName+"s");
		entitySet.setEntityType(new FullQualifiedName(MockData.NAME_SPACE, entityName));
		return entitySet;
	}

	private static List<EntityType> mockEntityTypes() {
		List<EntityType> entities = new ArrayList<EntityType>();
		entities.add(mockEntity(MockData.ENTITY_NAME_1));
		entities.add(mockEntity(MockData.ENTITY_NAME_2));
		return entities;
	}

	private static EntityType mockEntity(String entityName) {
		EntityType entity = new EntityType();
		entity.setName(entityName);
		return entity;
	}
	public static ODataJPAContext mockODataJPAContext()
	{
		ODataJPAContext odataJPAContext = EasyMock.createMock(ODataJPAContext.class);
		EasyMock.expect(odataJPAContext.getPersistenceUnitName()).andReturn(MockData.NAME_SPACE).times(2);
		EasyMock.replay(odataJPAContext);
		return odataJPAContext;
	}

}
