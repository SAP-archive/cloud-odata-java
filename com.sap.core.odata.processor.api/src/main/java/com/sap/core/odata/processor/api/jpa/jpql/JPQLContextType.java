package com.sap.core.odata.processor.api.jpa.jpql;

/**
 * Enumerated list of JPQL context Types.
 * 
 * @author SAP AG
 * 
 */
public enum JPQLContextType {
	/**
	 * indicates that the JPQL context can be used for building JPQL select
	 * statements
	 */
	SELECT,
	/**
	 * indicates that the JPQL context can be used for building JPQL modify
	 * statements
	 */
	MODIFY,
	/**
	 * indicates that the JPQL context can be used for building JPQL delete
	 * statements
	 */
	DELETE,
	/**
	 * indicates that the JPQL context can be used for building JPQL select
	 * statement that fetches single record
	 */
	SELECT_SINGLE,
	/**
	 * indicates that the JPQL context can be used for building JPQL join
	 * statement
	 */
	JOIN,
	/**
	 * indicates that the JPQL context can be used for building JPQL join
	 * statement that fetches single record
	 */
	JOIN_SINGLE,
	/**
	 * indicates that the JPQL context can be used for building JPQL select
	 * statement that fetches record counts
	 */
	SELECT_COUNT,
	/**
	 * indicates that the JPQL context can be used for building JPQL join
	 * statement that fetches single record
	 */
	JOIN_COUNT,
	/**
	 * indicates that the JPQL context can be used for building JPA Method
	 * context that can be used for invoking custom functions
	 */
	FUNCTION
}
