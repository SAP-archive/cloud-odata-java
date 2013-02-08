/**
 * Data Processor
 * 
 * A data processor implements all create, read, update and delete (CRUD) methods of an OData service. A processor as 
 * part of a OData service implementation is created by the service factory and then called during request handling. 
 * In dependency of the http context (http method, requestheaders ...) and the parsed uri semantic the OData Library 
 * will call an appropriate processor method. Within this method a service can perform operations on data. In a final
 * step the data result can be transformed using a {@link EntityProvider} (for Json, Atom and XML) and is returned as 
 * a {@see ODataResponse}.
 * <p>
 * A processor gets access to context information either via method parameters or a {@link ODataContext} which is attached 
 * to the processor object.
 * <p>
 * A processor can support optional features {@see com.sap.core.odata.processor.feature} and implement 
 * parts {@see com.sap.core.odata.processor.part} which is more or less a grouping for different OData CRUD operations.
 * <p>
 * {@see ODataSingleProcessor} is a convenience abstract class that implements all interface parts and has default implementations 
 * for handling OData service document and metadata. Usually the {@see ODataSingleProcessor} is used together with the 
 * {@see ODataSingleService} default implementation.
 *  
 */
package com.sap.core.odata.api.processor;