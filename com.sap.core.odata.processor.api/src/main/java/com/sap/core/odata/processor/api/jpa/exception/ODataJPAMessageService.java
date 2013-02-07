package com.sap.core.odata.processor.api.jpa.exception;

import com.sap.core.odata.api.exception.MessageReference;

/**
 * The interface is used to access language dependent message texts. Default
 * language is "English - EN". <br>
 * The default implementation of the interface shipped with the library loads
 * message texts from language dependent property files. If the message text is
 * not found for the given language then the default language -EN is used for
 * the message texts.
 * 
 * @author SAP AG
 * 
 */
public interface ODataJPAMessageService {
	/**
	 * The method returns a language dependent message texts for the given
	 * {@link MessageReference}.
	 * 
	 * @param context
	 *            is a Message Reference
	 * @return a language dependent message text
	 */
	public String getLocalizedMessage(MessageReference context);
}
