package com.sap.core.odata.processor.api.jpa.model;

/**
 * The interface acts a container for storing Java persistence column name. The
 * JPA EDM mapping instance can be associated with any EDM simple, EDM complex
 * property to denote the properties Java persistence column name.
 * 
 * @author SAP AG
 * 
 */
public interface JPAEdmMapping {
	/**
	 * The method sets the Java persistence column name into the mapping
	 * container.
	 * 
	 * @param name
	 *            is the Java persistence column name
	 */
	public void setJPAColumnName(String name);

	/**
	 * The method gets the Java persistence column name from the mapping
	 * container.
	 * 
	 * @return a String representing the Java persistence column name set into
	 *         the container
	 */
	public String getJPAColumnName();

	/**
	 * The method sets the Java persistence entity/property type.
	 * 
	 * @param type
	 *            is an instance of type Class<?>
	 */
	public void setJPAType(Class<?> type);

	/**
	 * The method returns the Java persistence entity/property type.
	 * 
	 * @return type
	 */
	public Class<?> getJPAType();

}
