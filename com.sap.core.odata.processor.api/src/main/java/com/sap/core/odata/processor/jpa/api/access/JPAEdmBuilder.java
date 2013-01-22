package com.sap.core.odata.processor.jpa.api.access;

import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntityContainerView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntitySetView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntityTypeView;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;

/**
 * Interface provides methods for building an Entity Data Model from a JPA Model
 * 
 * @author SAP AG
 * 
 */
public interface JPAEdmBuilder {
	/**
	 * Method builds EDM Elements by transforming JPA MetaModel. The method processes EDM Views
	 * like 
	 * <li>{@link JPAEdmShcemaView}</li>
	 * <li>{@link JPAEdmEntityContainerView}</li>
	 * <li>{@link JPAEdmAssociationSetView}</li>
	 * <li>{@link JPAEdmEntityTypeView}</li>
	 * <li>{@link JPAEdmEntitySetView}</li>
	 * <p>
	 * <br>
	 * 
	 * The built EDM elements can be accessed using corresponding EDM Views.
	 * </p>
	 * <br>
	 **/
	public void build() throws ODataJPAModelException;
}
