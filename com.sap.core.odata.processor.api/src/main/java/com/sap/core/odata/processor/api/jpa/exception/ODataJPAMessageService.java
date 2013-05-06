/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
 * @see com.sap.core.odata.processor.api.jpa.exception.ODataJPAException
 * @see com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException
 * @see com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException
 * @see com.sap.core.odata.api.exception.MessageReference
 */
public interface ODataJPAMessageService {
	/**
	 * The method returns a language dependent message texts for the given
	 * {@link com.sap.core.odata.api.exception.MessageReference}.
	 * 
	 * @param context
	 *            is a Message Reference
	 *        exception
	 *        	  is a Throwable Exception
	 * @return a language dependent message text
	 */
	public String getLocalizedMessage(MessageReference context,Throwable exception);
}
