package com.sap.core.odata.processor.jpa.access.api;

import java.util.List;

import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;

public interface JPAEdmBuilder {
	public List<Schema> getSchemas( ) throws ODataJPAModelException;
	/*public List<EntityType> getEntityTypes( ) throws ODataJPAModelException;
	public EntityType getEntityType(FullQualifiedName fqName) throws ODataJPAModelException;
	public EntitySet getEntitySet(FullQualifiedName fqName) throws ODataJPAModelException;*/
	
}
