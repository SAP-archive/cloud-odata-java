package com.sap.core.odata.processor.api.jpa.access;

import java.util.List;

import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;

/**
 * The interface provides methods for processing OData Requests.
 *
 * @author SAP AG
*/
public interface JPAProcessor {
	/**
	 * Processes OData request for querying an Entity Set. The method returns
	 * list of Objects of type representing JPA Entity Types.
	 * 
	 * @param <T>
	 *            Template parameter representing Java Persistence Entity Type.
	 *            <p>
	 *            <b>Note:-</b> Default parameter is Object.
	 *            </p>
	 * 
	 * @param requestView
	 *            is an OData request for querying an entity set
	 *            <p>
	 * @return list of objects representing JPA entity types
	 **/
	public <T> List<T> process(GetEntitySetUriInfo requestView)
			throws ODataJPAModelException, ODataJPARuntimeException;

	/**
	 * Processes OData request for reading an Entity. The method returns an
	 * Object of type representing JPA Entity Type.
	 * 
	 * @param <T>
	 *            Template parameter representing Java Persistence Entity Type.
	 *            <p>
	 *            <b>Note:-</b> Default parameter is Object.
	 *            </p>
	 * 
	 * @param requestView
	 *            OData request for reading an entity
	 * 
	 *            <p>
	 * @return object representing JPA entity type
	 **/
	public <T> Object process(GetEntityUriInfo requestView)
			throws ODataJPAModelException, ODataJPARuntimeException;;
}
