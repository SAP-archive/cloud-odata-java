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
package com.sap.core.odata.processor.api.jpa.jpql;

/**
 * Enumerated list of JPQL context Types.
 * 
 * @author SAP AG
 * 
 */
public enum JPQLContextType {
	/**
	 * indicates that the JPQL context can be used for building JPQL select
	 * statements
	 */
	SELECT,
	/**
	 * indicates that the JPQL context can be used for building JPQL modify
	 * statements
	 */
	MODIFY,
	/**
	 * indicates that the JPQL context can be used for building JPQL delete
	 * statements
	 */
	DELETE,
	/**
	 * indicates that the JPQL context can be used for building JPQL select
	 * statement that fetches single record
	 */
	SELECT_SINGLE,
	/**
	 * indicates that the JPQL context can be used for building JPQL join
	 * statement
	 */
	JOIN,
	/**
	 * indicates that the JPQL context can be used for building JPQL join
	 * statement that fetches single record
	 */
	JOIN_SINGLE,
	/**
	 * indicates that the JPQL context can be used for building JPQL select
	 * statement that fetches record counts
	 */
	SELECT_COUNT,
	/**
	 * indicates that the JPQL context can be used for building JPQL join
	 * statement that fetches single record
	 */
	JOIN_COUNT,
	/**
	 * indicates that the JPQL context can be used for building JPA Method
	 * context that can be used for invoking custom functions
	 */
	FUNCTION
}
