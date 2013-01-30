package com.sap.core.odata.processor.jpa.exception;

import com.sap.core.odata.api.exception.MessageReference;

public class ODataJPARuntimeException extends ODataJPAException{
	
	public static final MessageReference ENTITY_MANAGER_NOT_INITIALIZED = createMessageReference(ODataJPARuntimeException.class,"ENTITY_MANAGER_NOT_INITIALIZED");
	public static final MessageReference JOIN_CLAUSE_EXPECTED = createMessageReference(ODataJPARuntimeException.class, "JOIN_CLAUSE_EXPECTED");
	
	private ODataJPARuntimeException(String localizedMessage, Throwable e){
		super(localizedMessage,e);
	}


	public static ODataJPARuntimeException throwException(MessageReference messageReference, Throwable e){
		String message = MessageService.getLocalizedMessage(DEFAULT_LOCALE, messageReference);
		return new ODataJPARuntimeException(message,e);
	}

	/**
	 * 
	 */
	
	private static final long serialVersionUID = -5230976355642443012L;
	public static final MessageReference GENERAL = createMessageReference(ODataJPARuntimeException.class, "GENERAL");


}
