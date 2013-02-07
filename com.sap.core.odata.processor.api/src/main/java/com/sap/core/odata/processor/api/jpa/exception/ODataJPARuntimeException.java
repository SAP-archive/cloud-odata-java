package com.sap.core.odata.processor.api.jpa.exception;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.processor.api.jpa.factory.ODataJPAFactory;

public class ODataJPARuntimeException extends ODataJPAException {

	public static final MessageReference ENTITY_MANAGER_NOT_INITIALIZED = createMessageReference(
			ODataJPARuntimeException.class, "ENTITY_MANAGER_NOT_INITIALIZED");
	public static final MessageReference RUNTIME_EXCEPTION = createMessageReference(
			ODataJPARuntimeException.class, "RUNTIME_EXCEPTION");
	public static final MessageReference RESOURCE_NOT_FOUND = createMessageReference(
			ODataJPARuntimeException.class, "RESOURCE_NOT_FOUND");
	public static final MessageReference GENERAL = createMessageReference(
			ODataJPARuntimeException.class, "GENERAL");
	public static final MessageReference JOIN_CLAUSE_EXPECTED = createMessageReference(
			ODataJPARuntimeException.class, "JOIN_CLAUSE_EXPECTED");

	private ODataJPARuntimeException(String localizedMessage, Throwable e) {
		super(localizedMessage, e);
	}

	public static ODataJPARuntimeException throwException(
			MessageReference messageReference, Throwable e) throws ODataJPARuntimeException {
		ODataJPAMessageService messageService = ODataJPAFactory.createFactory()
				.getODataJPAAccessFactory()
				.getODataJPAMessageService(DEFAULT_LOCALE);
		String message = messageService.getLocalizedMessage(messageReference);
		return new ODataJPARuntimeException(message, e);
	}

	/**
	 * 
	 */

	private static final long serialVersionUID = -5230976355642443012L;

}
