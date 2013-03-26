package com.sap.core.odata.processor.api.jpa.access;

import java.io.InputStream;
import java.util.List;

import com.sap.core.odata.api.uri.info.DeleteUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.api.uri.info.PostUriInfo;
import com.sap.core.odata.api.uri.info.PutMergePatchUriInfo;
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
			throws ODataJPAModelException, ODataJPARuntimeException;

	/**
	 * Processes OData request for fetching Entity count. The method returns an
	 * Object of type representing JPA Entity count
	 * 
	 * @param requestView
	 *            OData request for counting an entity set
	 * @return long value representing count of JPA entity set
	 * 
	 * @throws ODataJPAModelException
	 * @throws ODataJPARuntimeException
	 */
	
	public long process(GetEntitySetCountUriInfo requestView)
			throws ODataJPAModelException, ODataJPARuntimeException;
	/**
	 * Processes OData request for creating Entity. The method returns an
	 * Object which is created. Null means object was not created.
	 * 
	 * @param createView
	 * @param content
	 * @param requestContentType
	 * @param contentType
	 * @return Created Object
	 * 
	 * @throws ODataJPAModelException
	 * @throws ODataJPARuntimeException
	 */
	
	public <T> Object process(PostUriInfo createView, InputStream content, String requestContentType)
			throws ODataJPAModelException, ODataJPARuntimeException;
	/**
	 * Processes OData request for updating Entity. The method returns an
	 * Object which is updated. Null means object was not found or updated.
	 * 
	 * @param deleteuriInfo
	 * @param contentType
	 * @return Deleted Object
	 * 
	 * @throws ODataJPAModelException
	 * @throws ODataJPARuntimeException
	 */
	public <T> Object process(PutMergePatchUriInfo updateView, InputStream content, String requestContentType)
			throws ODataJPAModelException, ODataJPARuntimeException;

	/**
	 * Processes OData request for deleting Entity. The method returns an
	 * Object which is deleted. Null means object was not found.
	 * 
	 * @param deleteuriInfo
	 * @param contentType
	 * @return Deleted Object
	 * 
	 * @throws ODataJPAModelException
	 * @throws ODataJPARuntimeException
	 */
	public Object process(DeleteUriInfo deleteuriInfo, String contentType) 
			throws ODataJPAModelException, ODataJPARuntimeException;
}
