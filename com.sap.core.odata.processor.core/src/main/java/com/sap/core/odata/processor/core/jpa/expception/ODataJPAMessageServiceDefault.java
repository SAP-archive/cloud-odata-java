package com.sap.core.odata.processor.core.jpa.expception;

import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingFormatArgumentException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAMessageService;
import com.sap.core.odata.processor.core.jpa.ODataJPAContextImpl;

public class ODataJPAMessageServiceDefault implements ODataJPAMessageService {

	private static final String BUNDLE_NAME = "jpaprocessor_msg"; //$NON-NLS-1$
	private static final Map<Locale, ODataJPAMessageService> LOCALE_2_MESSAGE_SERVICE = new HashMap<Locale, ODataJPAMessageService>();
	private static final ResourceBundle defaultResourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);
	private final ResourceBundle resourceBundle;
	private final Locale lanLocale;

	@Override
	public String getLocalizedMessage(MessageReference context) {

		Object[] contentAsArray = context.getContent().toArray(new Object[0]);
		String value = null;
		String key = context.getKey();

		try {
			value = getMessage(key);
			StringBuilder builder = new StringBuilder();
			Formatter f = null;
			if (lanLocale == null)
				f = new Formatter();
			else
				f = new Formatter(builder, lanLocale);
			f.format(value, contentAsArray);
			f.close();
			return builder.toString();

		} catch (MissingResourceException e) {
			return "Missing message for key '" + key + "'!";
		} catch (MissingFormatArgumentException e) {
			return "Missing replacement for place holder in value '" + value
					+ "' for following arguments '"
					+ Arrays.toString(contentAsArray) + "'!";
		}
	}

	private ODataJPAMessageServiceDefault(ResourceBundle resourceBundle,
			Locale locale) {
		this.resourceBundle = resourceBundle;
		lanLocale = locale;
	}

	public static ODataJPAMessageService getInstance(Locale locale) {
		
		Locale acceptedLocale = Locale.ENGLISH;
		if(ODataJPAContextImpl.getContextInThreadLocal()!=null) {
		List<Locale> acceptedLanguages = ODataJPAContextImpl.getContextInThreadLocal().getAcceptableLanguages();			
		if(acceptedLanguages!=null) {		
		Iterator<Locale> itr = acceptedLanguages.iterator();
		
		while(itr.hasNext()) {
			
			Locale tempLocale = itr.next();
			if(ResourceBundle.getBundle(BUNDLE_NAME, tempLocale).getLocale().equals(tempLocale)) {
				acceptedLocale = tempLocale;
				break;
			}
		}
		}
		
		else {		
			acceptedLocale = Locale.ENGLISH;
		}
		
		}
				
		else {		
			acceptedLocale = Locale.ENGLISH;
		}
		
		ODataJPAMessageService messagesInstance = LOCALE_2_MESSAGE_SERVICE
				.get(acceptedLocale);
		if (messagesInstance == null) {
			ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, acceptedLocale);
			
			if (resourceBundle != null) {
				messagesInstance = new ODataJPAMessageServiceDefault(
						resourceBundle, acceptedLocale);
				LOCALE_2_MESSAGE_SERVICE.put(acceptedLocale, messagesInstance);
			} else if (defaultResourceBundle != null) {
				messagesInstance = new ODataJPAMessageServiceDefault(
						defaultResourceBundle, null);
			}

		}
		return messagesInstance;
	}

	private String getMessage(String key) {
		return resourceBundle.getString(key);
	}	
}
