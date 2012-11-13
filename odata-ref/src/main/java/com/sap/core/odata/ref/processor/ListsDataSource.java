package com.sap.core.odata.ref.processor;

import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.exception.ODataNotFoundException;
import com.sap.core.odata.api.exception.ODataNotImplementedException;

/**
 * <p>This interface is intended to make it easier to implement an OData
 * service in cases where all data for each entity set can be provided as a
 * {@link List} of objects from which all properties described in the
 * Entity Data Model can be retrieved and set.</p>
 * <p>By obeying these restrictions, data-source implementations get the
 * following advantages: 
 * <ul>
 * <li>All system query options can be handled centrally.</li>
 * <li>Following navigation paths must only be done step by step.</li>
 * </ul>
 * </p>
 * @author SAP AG
 */
public interface ListsDataSource {

  /**
   * <p>Retrieves the whole data list for the specified entity set.</p>
   * @param entitySet  the requested {@link EdmEntitySet}
   * @return the requested data list
   */
  List<?> readData(EdmEntitySet entitySet) throws ODataNotImplementedException, ODataNotFoundException, EdmException, ODataApplicationException;

  /**
   * <p>Retrieves a single data object for the specified entity set and key.</p>
   * @param entitySet  the requested {@link EdmEntitySet}
   * @param keys  the entity key as map of key names to key values
   * @return the requested data object
   */
  Object readData(EdmEntitySet entitySet, Map<String, Object> keys) throws ODataNotImplementedException, ODataNotFoundException, EdmException, ODataApplicationException;

  /**
   * <p>Retrieves data for the specified function import and key.</p>
   * <p>This method is called also for function imports that have defined in
   * their metadata an other HTTP method than <code>GET</code>.</p>  
   * @param function  the requested {@link EdmFunctionImport}
   * @param parameters  the parameters of the function import
   *                    as map of parameter names to parameter values
   * @param keys  the key of the returned entity set, as map of key names to key values,
   *              if the return type of the function import is a collection of entities
   *              (optional)
   * @return the requested data object, either a list or a single object
   */
  Object readData(EdmFunctionImport function, Map<String, Object> parameters, Map<String, Object> keys) throws ODataNotImplementedException, ODataNotFoundException, EdmException, ODataApplicationException;

  /**
   * <p>Retrieves related data for the specified source data, entity set, and key.</p>
   * <p>If the underlying association of the EDM is specified to have target
   * multiplicity '*' and no target key is given, this method returns a list of
   * related data, otherwise it returns a single data object.</p>
   * @param sourceEntitySet  the {@link EdmEntitySet} of the source entity
   * @param sourceData  the data object of the source entity
   * @param targetEntitySet  the requested target {@link EdmEntitySet}
   * @param targetKeys  the key of the target entity as map of key names to key values
   *                    (optional)
   * @return the requested releated data object, either a list or a single object
   */
  Object readRelatedData(EdmEntitySet sourceEntitySet, Object sourceData, EdmEntitySet targetEntitySet, Map<String, Object> targetKeys) throws ODataNotImplementedException, ODataNotFoundException, EdmException, ODataApplicationException;

  /**
   * <p>Creates and returns a new instance of the requested data-object type.</p>
   * <p>This instance must not be part of the corresponding list and should
   * have empty content.</p>
   * @param entitySet  the {@link EdmEntitySet} the object must correspond to
   * @return the new data object
   */
  Object newDataObject(EdmEntitySet entitySet) throws ODataNotImplementedException, EdmException, ODataApplicationException;
}
