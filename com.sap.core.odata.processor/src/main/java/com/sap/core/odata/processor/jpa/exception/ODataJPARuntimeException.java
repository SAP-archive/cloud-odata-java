package com.sap.core.odata.processor.jpa.exception;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataMessageException;

public class ODataJPARuntimeException extends ODataMessageException{
	
	public static final MessageReference ENTITY_MANAGER_NOT_INITIALIZED = createMessageReference(ODataJPARuntimeException.class,"ENTITY_MANAGER_NOT_INITIALIZED");
	public static final MessageReference RUNTIME_EXCEPTION = createMessageReference(ODataJPARuntimeException.class, "RUNTIME_EXCEPTION");
	
	public ODataJPARuntimeException(MessageReference messageReference) {
		super(messageReference);
	}
	
	public ODataJPARuntimeException(MessageReference messageReference, Throwable cause){
		super(messageReference,cause);
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -5230976355642443012L;

}
