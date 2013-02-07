package com.sap.core.odata.processor.api.jpa.model;

import javax.persistence.metamodel.Metamodel;

import com.sap.core.odata.processor.api.jpa.access.JPAEdmBuilder;

/**
 * <p>
 * A base view on Java Persistence Model and Entity Data Model.
 * </p>
 * <p>
 * The implementation of the view acts as a base container for containers of
 * Java Persistence Model and Entity Data Model elements.
 * 
 * @author SAP AG
 *         <p>
 * @DoNotImplement
 * 
 */
public interface JPAEdmBaseView {
	/**
	 * 
	 * @return Java Persistence Unit Name
	 */
	public String getpUnitName();

	/**
	 * The method returns the Java Persistence MetaModel
	 * 
	 * @return a meta model of type {@link Metamodel}
	 */
	public Metamodel getJPAMetaModel();

	/**
	 * The method returns a builder for building Entity Data Model elements from
	 * Java Persistence Model Elements
	 * 
	 * @return a builder of type {@link JPAEdmBuilder}
	 */
	public JPAEdmBuilder getBuilder();
	
	/**
	 * The method returns the if the container is consistent without any errors
	 * @return <ul>
	 * <li>true - if the container is consistent without errors</li>
	 * <li>false - if the container is inconsistent with errors</li>
	 * 
	 */
	public boolean isConsistent();
	
	/**
	 * The method cleans the container.
	 */
	public void clean();
}
