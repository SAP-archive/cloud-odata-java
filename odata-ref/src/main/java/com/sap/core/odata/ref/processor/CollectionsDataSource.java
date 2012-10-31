package com.sap.core.odata.ref.processor;

import java.util.Collection;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.exception.ODataError;

/**
 * <p>This interface is intended to make it easier to implement an OData
 * service in cases where all data for each entity set can be provided as a
 * {@link Collection} of objects from which all properties described in the
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
public interface CollectionsDataSource {

  /**
   * <p>Retrieves the whole data collection for the specified entity set.</p>
   * <p>Implementations should return a copy of the original data since this
   * collection will be handled destructively, e.g., to process filtering
   * or paging.</p>
   * @param entitySet  the requested {@link EdmEntitySet}
   * @return the requested data collection
   * @throws ODataError
   */
  Collection<?> readData(EdmEntitySet entitySet) throws ODataError;

  /**
   * <p>Retrieves a single data object for the specified entity set and key.</p>
   * @param entitySet  the requested {@link EdmEntitySet}
   * @param keys  the entity key as map of key names to key values
   * @return the requested data object
   * @throws ODataError
   */
  Object readData(EdmEntitySet entitySet, Map<String, Object> keys) throws ODataError;

  /**
   * <p>Creates and returns a new instance of the requested data-object type.</p>
   * <p>This instance must not be part of the corresponding collection and should
   * have empty content.</p>
   * @param entitySet  the {@link EdmEntitySet} the object must correspond to
   * @return the new data object
   * @throws ODataError
   */
  Object newDataObject(EdmEntitySet entitySet) throws ODataError;
}
