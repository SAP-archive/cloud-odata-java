package com.sap.core.odata.processor.api.jpa.jpql;

/**
 * The interface provides a view on JPQL Context. The view can be used to access
 * different JPQL context type implementations.
 * 
 * @author SAP AG
 * @see {@link JPQLContextType}, {@link JPQLContext}
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
	 * @return an instance of type {@link JPQLContextType}
	 */
	public JPQLContextType getType();
}
