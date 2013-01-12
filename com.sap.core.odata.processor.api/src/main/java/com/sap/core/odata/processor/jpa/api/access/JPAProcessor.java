package com.sap.core.odata.processor.jpa.api.access;

import java.util.List;

import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.exception.ODataJPARuntimeException;

/**
 * Interface provides methods for processing OData Requests.
 **/
public interface JPAProcessor {
	/**
	 * Processes OData request for querying an Entity Set. The method returns
	 * list of Objects of type representing JPA Entity Types.
	 * 
	 * @param requestView OData request for querying an entity set
	 * 
	 * @return list of objects representing JPA entity types
	 **/
	public List<Object> process(GetEntitySetUriInfo requestView)
			throws ODataJPAModelException, ODataJPARuntimeException;

	/**
	 * Processes OData request for reading an Entity. The method returns an
	 * Object of type representing JPA Entity Type.
	 * 
	 * @param requestView OData request for reading an entity
	 * 
	 * @return object representing JPA entity type
	 **/
	public Object process(GetEntityUriInfo requestView)
			throws ODataJPAModelException, ODataJPARuntimeException;;
}
