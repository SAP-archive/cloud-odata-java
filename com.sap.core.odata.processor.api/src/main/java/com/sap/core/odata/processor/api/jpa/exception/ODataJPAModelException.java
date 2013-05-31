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
import com.sap.core.odata.processor.api.jpa.factory.ODataJPAFactory;

/**
 * The exception is thrown for any unexpected errors raising while
 * accessing/transforming Java Persistence Models.
 * 
 * @author SAP AG
 * 
 */
public class ODataJPAModelException extends ODataJPAException {

	public static final MessageReference INVALID_ENTITY_TYPE = createMessageReference(
			ODataJPAModelException.class, "INVALID_ENTITY_TYPE");
	public static final MessageReference INVALID_COMPLEX_TYPE = createMessageReference(
			ODataJPAModelException.class, "INVLAID_COMPLEX_TYPE");
	public static final MessageReference INVALID_ASSOCIATION = createMessageReference(
			ODataJPAModelException.class, "INVALID_ASSOCIATION");
	public static final MessageReference INVALID_ENTITYSET = createMessageReference(
			ODataJPAModelException.class, "INVALID_ENTITYSET");
	public static final MessageReference INVALID_ENTITYCONTAINER = createMessageReference(
			ODataJPAModelException.class, "INVALID_ENTITYCONTAINER");
	public static final MessageReference INVALID_ASSOCIATION_SET = createMessageReference(
			ODataJPAModelException.class, "INVALID_ASSOCIATION_SET");
	public static final MessageReference INVALID_FUNC_IMPORT = createMessageReference(
			ODataJPAModelException.class, "INVALID_FUNC_IMPORT");

	public static final MessageReference BUILDER_NULL = createMessageReference(
			ODataJPAModelException.class, "BUILDER_NULL");
	public static final MessageReference TYPE_NOT_SUPPORTED = createMessageReference(
			ODataJPAModelException.class, "TYPE_NOT_SUPPORTED");
	public static final MessageReference FUNC_ENTITYSET_EXP = createMessageReference(
			ODataJPAModelException.class, "FUNC_ENTITYSET_EXP");
	public static final MessageReference FUNC_RETURN_TYPE_EXP = createMessageReference(
			ODataJPAModelException.class, "FUNC_RETURN_TYPE_EXP");
	public static final MessageReference FUNC_RETURN_TYPE_ENTITY_NOT_FOUND = createMessageReference(
			ODataJPAModelException.class, "FUNC_RETURN_TYPE_ENTITY_NOT_FOUND");
	public static final MessageReference GENERAL = createMessageReference(
			ODataJPAModelException.class, "GENERAL");
	public static final MessageReference INNER_EXCEPTION = createMessageReference(
			ODataJPAModelException.class, "INNER_EXCEPTION");
	public static final MessageReference FUNC_PARAM_NAME_EXP = createMessageReference(
			ODataJPAModelException.class, "FUNC_PARAM_NAME_EXP");

	private ODataJPAModelException(String localizedMessage, Throwable e,
			MessageReference msgRef) {
		super(localizedMessage, e, msgRef);
	}

	/**
	 * The method creates an exception object of type ODataJPAModelException
	 * with localized error texts.
	 * 
	 * @param messageReference
	 *            is a <b>mandatory</b> parameter referring to a literal that
	 *            could be translated to localized error texts.
	 * @param e
	 *            is an optional parameter representing the previous exception
	 *            in the call stack
	 * @return an instance of ODataJPAModelException which can be then raised.
	 * @throws ODataJPARuntimeException
	 */
	public static ODataJPAModelException throwException(
			MessageReference messageReference, Throwable e) {

		ODataJPAMessageService messageService;
		messageService = ODataJPAFactory.createFactory()
				.getODataJPAAccessFactory()
				.getODataJPAMessageService(DEFAULT_LOCALE);
		String message = messageService
				.getLocalizedMessage(messageReference, e);
		return new ODataJPAModelException(message, e, messageReference);
	}

	private static final long serialVersionUID = 7940106375606950703L;

}
