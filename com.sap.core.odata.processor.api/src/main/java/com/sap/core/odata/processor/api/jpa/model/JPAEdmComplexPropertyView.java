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
package com.sap.core.odata.processor.api.jpa.model;

import com.sap.core.odata.api.edm.provider.ComplexProperty;

/**
 * <p>
 * A view on properties of Java Persistence embeddable type and EDM complex
 * type. Properties of JPA embeddable types are converted into EDM properties of
 * EDM complex type.
 * </p>
 * <p>
 * The implementation of the view provides access to properties of EDM complex
 * type created for a given JPA EDM complex type. The implementation acts as a
 * container for the properties of EDM complex type.
 * </p>
 * 
 * @author SAP AG
 * @see com.sap.core.odata.processor.api.jpa.model.JPAEdmComplexTypeView
 */
public interface JPAEdmComplexPropertyView extends JPAEdmBaseView {
	/**
	 * The method returns a complex property for a complex type.
	 * 
	 * @return an instance of
	 *         {@link com.sap.core.odata.api.edm.provider.ComplexProperty}
	 */
	ComplexProperty getEdmComplexProperty();
}
