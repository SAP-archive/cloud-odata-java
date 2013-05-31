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
package com.sap.core.odata.processor.api.jpa;

import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.processor.api.jpa.access.JPAProcessor;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAException;
import com.sap.core.odata.processor.api.jpa.factory.ODataJPAFactory;

/**
 * Extend this class and implement an OData JPA processor if the default
 * behavior of OData JPA Processor library has to be overwritten.
 * 
 * @author SAP AG
 * 
 * 
 */
public abstract class ODataJPAProcessor extends ODataSingleProcessor {

	/**
	 * An instance of
	 * {@link com.sap.core.odata.processor.api.jpa.ODataJPAContext} object
	 */
	protected ODataJPAContext oDataJPAContext;

	/**
	 * An instance of
	 * {@link com.sap.core.odata.processor.api.jpa.access.JPAProcessor}. The
	 * instance is created using
	 * {@link com.sap.core.odata.processor.api.jpa.factory.JPAAccessFactory}.
	 */
	protected JPAProcessor jpaProcessor;

	public ODataJPAContext getOdataJPAContext() {
		return oDataJPAContext;
	}

	public void setOdataJPAContext(ODataJPAContext odataJPAContext) {
		this.oDataJPAContext = odataJPAContext;
	}

	/**
	 * Constructor
	 * 
	 * @param oDataJPAContext
	 *            non null OData JPA Context object
	 */
	public ODataJPAProcessor(ODataJPAContext oDataJPAContext) {
		if (oDataJPAContext == null) {
			throw new IllegalArgumentException(
					ODataJPAException.ODATA_JPACTX_NULL);
		}
		this.oDataJPAContext = oDataJPAContext;
		jpaProcessor = ODataJPAFactory.createFactory().getJPAAccessFactory()
				.getJPAProcessor(this.oDataJPAContext);
	}

}
