package com.sap.core.odata.processor.api.jpa.jpql;

/**
 * The interface provides a view on JPQL Context. The view can be used to access
 * different JPQL context type implementations.
 * 
 * @author SAP AG
 * @see com.sap.core.odata.processor.api.jpa.jpql.JPQLContextType
 * @see com.sap.core.odata.processor.api.jpa.jpql.JPQLContextType
 */
public interface JPQLContextView {
	/**
	 * The method returns a JPA entity name for which the JPQL context is
	 * relevant.
	 * 
	 * @return JPA entity name
	 */
	public String getJPAEntityName();

	/**
	 * The method returns a JPA entity alias name for which the JPQL context is
	 * relevant.
	 * 
	 * @return JPA entity alias name
	 */

	public String getJPAEntityAlias();

	/**
	 * The method returns a JPQL context type
	 * 
	 * @return an instance of type
	 *         {@link com.sap.core.odata.processor.api.jpa.jpql.JPQLContextType}
	 */
	public JPQLContextType getType();
}
