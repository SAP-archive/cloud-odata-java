/**
 * Entity Provider<p>
 * 
 * The <b>com.sap.core.odata.api.ep</b> package contains all classes related and necessary to provide an {@link com.sap.core.odata.api.ep.EntityProvider}.
 * <p>
 * An {@link com.sap.core.odata.api.ep.EntityProvider} provides all necessary <b>read</b> and <b>write</b> methods for accessing 
 * the entities defined in an <code>Entity Data Model</code>.
 * Therefore this library provides (in its <code>core</code> packages) as convenience basic {@link com.sap.core.odata.api.ep.EntityProvider} 
 * for accessing entities in the <b>XML</b> and <b>JSON</b> format.
 * <p>
 * For support of additional formats it is recommended to handle those directly within an implementation of a 
 * <code>ODataProcessor</code> (it is possible but <b>not recommended</b> to implement an own 
 * {@link com.sap.core.odata.api.ep.EntityProvider} for support of additional formats).
 */
package com.sap.core.odata.api.ep;