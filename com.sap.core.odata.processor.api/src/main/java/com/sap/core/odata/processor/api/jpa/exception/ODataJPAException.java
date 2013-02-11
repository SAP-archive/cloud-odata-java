package com.sap.core.odata.processor.api.jpa.exception;

import java.util.Locale;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataException;

/**
 * The exception class is the base of OData JPA exceptions. The class also
 * provides non localized error texts that can be used for raising OData JPA
 * exceptions with non localized error texts.
 * 
 * @author SAP AG
 * 
 */
public abstract class ODataJPAException extends ODataException {

	public static final String ODATA_JPACTX_NULL = "OData JPA Context cannot be null";

	private static final long serialVersionUID = -6884673558124441214L;
	protected static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

	protected ODataJPAException(String localizedMessage, Throwable e) {
		super(localizedMessage, e);
	}

	/**
	 * The method creates a Reference to Message Object
	 * {@link com.sap.core.odata.api.exception.MessageReference} . The message
	 * text key is derived out of parameters clazz.messageReferenceKey.
	 * 
	 * @param clazz
	 *            is name of the class extending
	 *            {@link com.sap.core.odata.processor.api.jpa.exception.ODataJPAException}
	 * @param messageReferenceKey
	 *            is the key of the message
	 * @return an instance of type
	 *         {@link com.sap.core.odata.api.exception.MessageReference}
	 */
	protected static MessageReference createMessageReference(
			Class<? extends ODataJPAException> clazz, String messageReferenceKey) {
		return MessageReference.create(clazz, messageReferenceKey);
	}

}
