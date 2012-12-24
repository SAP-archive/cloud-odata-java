package com.sap.core.odata.processor.jpa.access.api;

import java.util.List;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.Schema;

public interface JPAEdmBuilder {
	public List<Schema> getSchemas( );
	public List<EntityType> getEntityTypes( );
	public EntityType getEntityType(FullQualifiedName fqName);
	EntitySet getEntitySet(FullQualifiedName fqName);
}
