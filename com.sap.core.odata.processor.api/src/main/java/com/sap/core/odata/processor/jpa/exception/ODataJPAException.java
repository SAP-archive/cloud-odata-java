package com.sap.core.odata.processor.jpa.exception;

import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingFormatArgumentException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataException;


/**
 * @author SAP AG
 *
 */
public abstract class ODataJPAException extends ODataException {

	private static final long serialVersionUID = -6884673558124441214L;
	protected static final Locale DEFAULT_LOCALE = Locale.ENGLISH; 

	protected ODataJPAException(String localizedMessage, Throwable e){
		super(localizedMessage,e);
	}
	
	protected static MessageReference createMessageReference(
			Class<? extends ODataJPAException> clazz, String messageReferenceKey) {
		return MessageReference.create(clazz, messageReferenceKey);
	}
	
	
	protected static class MessageService{
		private static final String BUNDLE_NAME = "jpaprocessor_msg"; //$NON-NLS-1$

		private final ResourceBundle resourceBundle;
		private static final Map<Locale, MessageService> LOCALE_2_MESSAGE_SERVICE = new HashMap<Locale, MessageService>();

		private MessageService(Locale locale) {
			resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
		}

		public static MessageService getInstance(Locale locale) {
			MessageService messagesInstance = LOCALE_2_MESSAGE_SERVICE.get(locale);
			if (messagesInstance == null) {
				messagesInstance = new MessageService(locale);
				LOCALE_2_MESSAGE_SERVICE.put(locale, messagesInstance);
			}
			return messagesInstance;
		}

		public static String getLocalizedMessage(Locale language, MessageReference context) {
			
			MessageService messageService = MessageService.getInstance(DEFAULT_LOCALE);
			
			Object[] contentAsArray = context.getContent().toArray(new Object[0]);
			String value = null;
			String key = context.getKey();

			try {
				value = messageService.getMessage(key);
				StringBuilder builder = new StringBuilder();
				Formatter f = new Formatter(builder, language);
				f.format(value, contentAsArray);
				f.close();
				return builder.toString();

			} catch (MissingResourceException e) {
				return  "Missing message for key '"
						+ key + "'!";
			} catch (MissingFormatArgumentException e) {
				return "Missing replacement for place holder in value '" + value
								+ "' for following arguments '"
								+ Arrays.toString(contentAsArray) + "'!";
			}
			
			
		}

		private String getMessage(String key) {
			return resourceBundle.getString(key);
		}
	}
}
