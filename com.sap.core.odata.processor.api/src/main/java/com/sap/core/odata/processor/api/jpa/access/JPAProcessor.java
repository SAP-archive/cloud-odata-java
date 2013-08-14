package com.sap.core.odata.processor.api.jpa.access;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.uri.info.DeleteUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityLinkUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetLinksUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.api.uri.info.GetFunctionImportUriInfo;
import com.sap.core.odata.api.uri.info.PostUriInfo;
import com.sap.core.odata.api.uri.info.PutMergePatchUriInfo;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;

/**
 * The interface provides methods for processing OData Requests for Create, Read, Update, Delete operations. 
 * Pass the OData request or parsed OData request (Map of properties) as request. 
 * A JPA entity is returned as a response. 
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
   * Processes OData request for fetching Entity count. The method returns JPA Entity count
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
   * Processes OData request for fetching Entity count. The method returns count of target entity.
   * This is specific to situation where cardinality is 1:1
   * 
   * @param resultsView 
   * 			OData request for counting target entity.
   * @return long value representing count of JPA entity
   * 
   * @throws ODataJPAModelException
   * @throws ODataJPARuntimeException
   */
  public long process(GetEntityCountUriInfo resultsView)
      throws ODataJPAModelException, ODataJPARuntimeException;

  /**
   * Processes OData request for executing custom operations. The method
   * returns a List of Object. The list contains one entry if the the custom
   * operations return type has multiplicity of ONE.
   * 
   * @param requestView
   *            OData request for executing function import
   * @return result of executing function import
   * @throws ODataJPAModelException
   * @throws ODataJPARuntimeException
   */
  public List<Object> process(GetFunctionImportUriInfo requestView)
      throws ODataJPAModelException, ODataJPARuntimeException;

  /**
   * Processes OData request for executing $links OData command for N:1 relation. 
   * The method returns an Object of type representing OData entity.
   * 
   * @param uriParserResultView
   *          OData request for Entity Link URI
   * @return an object representing JPA entity
   * @throws ODataJPAModelException
   * @throws ODataJPARuntimeException
   */
  public Object process(GetEntityLinkUriInfo uriParserResultView)
      throws ODataJPAModelException, ODataJPARuntimeException;

  /**
   * Processes OData request for executing $links OData command for N:1 relation. 
   * The method returns an Object of type representing OData entity.
   * 
   * @param uriParserResultView
   *          OData request for Entity Set Link URI
   * @return a list of object representing JPA entities
   * @throws ODataJPAModelException
   * @throws ODataJPARuntimeException
   */
  public <T> List<T> process(GetEntitySetLinksUriInfo uriParserResultView)
      throws ODataJPAModelException, ODataJPARuntimeException;

  /**
   * Processes OData request for creating Entity. The method returns an Object
   * which is created.  A Null reference implies object was not created.
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

  public <T> List<T> process(PostUriInfo createView, InputStream content,
      String requestContentType) throws ODataJPAModelException,
      ODataJPARuntimeException;

  /**
   * Processes OData request for creating Entity. The method expects a parsed OData request which is a Map of properties.
   * The method returns an Object that is created. A Null reference implies object was not created.
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

  public <T> List<T> process(PostUriInfo createView, Map<String, Object> content) throws ODataJPAModelException,
      ODataJPARuntimeException;

  /**
   * Processes OData request for updating Entity. The method returns an Object
   * which is updated.  A Null reference implies object was not created.
   * 
   * @param deleteuriInfo
   * @param contentType
   * @return Deleted Object
   * 
   * @throws ODataJPAModelException
   * @throws ODataJPARuntimeException
   */
  public <T> Object process(PutMergePatchUriInfo updateView,
      InputStream content, String requestContentType)
      throws ODataJPAModelException, ODataJPARuntimeException;

  /**
   * Processes OData request for updating Entity. The method returns an Object
   * which is updated.  A Null reference implies object was not created.
   * 
   * @param deleteuriInfo
   * @param contentType
   * @return Deleted Object
   * 
   * @throws ODataJPAModelException
   * @throws ODataJPARuntimeException
   */
  public <T> Object process(PutMergePatchUriInfo updateView, Map<String, Object> content)
      throws ODataJPAModelException, ODataJPARuntimeException;

  /**
   * Processes OData request for deleting Entity. The method returns an Object
   * which is deleted.  A Null reference implies object was not created.
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

  /**
   * Process OData request for creating Links. The OData request should contain
   * $links OData command.
   * 
   * @param uriParserResultView
   *          OData request for creating Links
   * @param content
   * @param requestContentType
   * @param contentType
   * 
   * @throws ODataJPARuntimeException
   * @throws ODataJPAModelException
   */
  public void process(PostUriInfo uriParserResultView,
      InputStream content, String requestContentType, String contentType)
      throws ODataJPARuntimeException, ODataJPAModelException;

  /**
   * Process OData request for updating Links. The OData request should contain
   * $links OData command.
   * 
   * @param uriParserResultView
   *          OData request for updating Links
   * @param content
   * @param requestContentType
   * @param contentType
   * 
   * @throws ODataJPARuntimeException
   * @throws ODataJPAModelException
   */
  public void process(PutMergePatchUriInfo uriParserResultView,
      InputStream content, String requestContentType, String contentType)
      throws ODataJPARuntimeException, ODataJPAModelException;
}
