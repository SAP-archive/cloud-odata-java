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

import java.util.HashMap;

/**
 * The interface provide a view on JPQL select context.The interface provides
 * methods for accessing the clauses of a JPQL SELECT statement like "SELECT",
 * "ORDERBY", "WHERE". The clauses are built from OData read entity set request
 * views. The clauses thus built can be used for building JPQL Statements.
 * 
 * @author SAP AG
 * @see com.sap.core.odata.processor.api.jpa.jpql.JPQLStatement
 * 
 */
public interface JPQLSelectContextView extends JPQLContextView {
	/**
	 * The method returns a JPQL SELECT clause. The SELECT clause is built from
	 * $select OData system Query option.
	 * 
	 * @return a String representing a SELECT clause in JPQL
	 */
	public String getSelectExpression();

	/**
	 * The method returns a Hash Map of JPQL ORDERBY clause. The ORDERBY clause
	 * is built from $orderby OData system query option. The hash map contains
	 * <ol>
	 * <li>Key - JPA Entity Property name to be ordered</li>
	 * <li>Value - Sort Order in JPQL (desc,asc)</li>
	 * </ol>
	 * 
	 * @return a hash map of (JPA Property Name,Sort Order)
	 */
	public HashMap<String, String> getOrderByCollection();

	/**
	 * The method returns a JPQL WHERE condition as string. The WHERE condition
	 * can be built from $filter OData System Query Option and/or Key predicates
	 * of an OData Request.
	 * 
	 * @return a String representing a WHERE condition in JPQL
	 */
	public String getWhereExpression();
}
