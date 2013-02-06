package com.sap.core.odata.processor.api.exception;

import java.util.Locale;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataException;

/**
 * @author SAP AG
 * 
 */
public abstract class ODataJPAException extends ODataException {

	private static final long serialVersionUID = -6884673558124441214L;
	protected static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

	protected ODataJPAException(String localizedMessage, Throwable e) {
		super(localizedMessage, e);
	}

	protected static MessageReference createMessageReference(
			Class<? extends ODataJPAException> clazz, String messageReferenceKey) {
		return MessageReference.create(clazz, messageReferenceKey);
	}

}
