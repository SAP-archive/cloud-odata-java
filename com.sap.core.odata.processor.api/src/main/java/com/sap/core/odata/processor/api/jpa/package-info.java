/**
 * <h3>OData JPA Processor API Library</h3>
 * The library provides a way for the developers to create an OData Service from a Java Persistence Model.
 * The library supports Java Persistence 2.0 and is dependent on OData library.
 * 
 * To create an OData service from JPA models
 * <ol><li>extend the service factory class {@link com.sap.core.odata.processor.api.jpa.ODataJPAServiceFactory}
 * and implement the methods</li>
 * <li>define a JAX-RS servlet in web.xml and configure the service factory as servlet init parameter. 
 * <p><b>See Also:</b>{@link com.sap.core.odata.processor.api.jpa.ODataJPAServiceFactory}</li></ol>
 * 
 * @author SAP AG
 */
package com.sap.core.odata.processor.api.jpa;

