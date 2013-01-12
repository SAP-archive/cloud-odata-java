package com.sap.core.odata.processor.jpa.api.access;

import java.util.List;

import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;

/**
 * Interface provides methods for building an Entity Data Model from a JPA Model
 * 
 * @author SAP AG
 * 
 */
public interface JPAEdmBuilder {
	/**
	 * Method returns a list of {@link Schema} objects.
	 **/
	public List<Schema> getSchemas() throws ODataJPAModelException;
}
