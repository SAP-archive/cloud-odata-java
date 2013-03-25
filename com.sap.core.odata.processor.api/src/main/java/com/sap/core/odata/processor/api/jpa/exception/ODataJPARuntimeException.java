package com.sap.core.odata.processor.api.jpa.exception;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.processor.api.jpa.factory.ODataJPAFactory;

/**
 * The exception is thrown for any unexpected errors raising while accessing
 * data from Java Persistence Models.
 * 
 * The exception object is created with localized error texts provided error
 * texts are maintained in localized languages.
 * 
 * @author SAP AG
 * 
 */
public class ODataJPARuntimeException extends ODataJPAException {

	public static final MessageReference ENTITY_MANAGER_NOT_INITIALIZED = createMessageReference(
			ODataJPARuntimeException.class, "ENTITY_MANAGER_NOT_INITIALIZED");
	public static final MessageReference RESOURCE_NOT_FOUND = createMessageReference(
			ODataJPARuntimeException.class, "RESOURCE_NOT_FOUND");
	public static final MessageReference GENERAL = createMessageReference(
			ODataJPARuntimeException.class, "GENERAL");
	public static final MessageReference INNER_EXCEPTION = createMessageReference(
			ODataJPARuntimeException.class, "INNER_EXCEPTION");
	public static final MessageReference JOIN_CLAUSE_EXPECTED = createMessageReference(
			ODataJPARuntimeException.class, "JOIN_CLAUSE_EXPECTED");
	public static final MessageReference ERROR_JPQLCTXBLDR_CREATE = createMessageReference(
			ODataJPARuntimeException.class, "ERROR_JPQLCTXBLDR_CREATE");
	public static final MessageReference ERROR_ODATA_FILTER_CONDITION = createMessageReference(
			ODataJPARuntimeException.class, "ERROR_ODATA_FILTER_CONDITION");
	public static final MessageReference ERROR_JPQL_QUERY_CREATE = createMessageReference(
			ODataJPARuntimeException.class, "ERROR_JPQL_QUERY_CREATE");
	public static final MessageReference ERROR_JPQL_CREATE_CREATE = createMessageReference(
			ODataJPARuntimeException.class, "ERROR_JPQL_CREATE_CREATE");


	private ODataJPARuntimeException(String localizedMessage, Throwable e,
			MessageReference msgRef) {
		super(localizedMessage, e, msgRef);
	}

	/**
	 * The method creates an exception object of type ODataJPARuntimeException
	 * with localized error texts.
	 * 
	 * @param messageReference
	 *            is a <b>mandatory</b> parameter referring to a literal that
	 *            could be translated to localized error texts.
	 * @param e
	 *            is an optional parameter representing the previous exception
	 *            in the call stack
	 * @return an instance of ODataJPARuntimeException which can be then raised.
	 * @throws ODataJPARuntimeException
	 */
	public static ODataJPARuntimeException throwException(
			MessageReference messageReference, Throwable e) {
		ODataJPAMessageService messageService;
		messageService = ODataJPAFactory.createFactory()
				.getODataJPAAccessFactory()
				.getODataJPAMessageService(DEFAULT_LOCALE);
		String message = messageService
				.getLocalizedMessage(messageReference, e);
		return new ODataJPARuntimeException(message, e, messageReference);
	}

	private static final long serialVersionUID = -5230976355642443012L;

}
