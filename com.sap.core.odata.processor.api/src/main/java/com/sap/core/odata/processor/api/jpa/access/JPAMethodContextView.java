package com.sap.core.odata.processor.api.jpa.access;

import java.util.List;

/**
 * The interface provides view on JPA Method Context. JPA Method context can be
 * used to access custom operations or JPA Entity property access methods.
 * 
 * @author SAP AG
 * 
 */
public interface JPAMethodContextView {
	/**
	 * The method returns an instance of Object on which the methods/custom
	 * operations can be executed.
	 * 
	 * @return instance of enclosing object for the method
	 */
	public Object getEnclosingObject();

	/**
	 * The method returns list of JPA functions that can be executed on the
	 * enclosing object.
	 * 
	 * @return an instance of list of JPA Function
	 */
	public List<JPAFunction> getJPAFunctionList();
}
