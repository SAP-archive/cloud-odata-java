package com.sap.core.odata.processor.core.jpa.jpql;

import java.util.HashMap;

import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContext;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContextType;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLSelectContextView;
import com.sap.core.odata.processor.core.jpa.access.data.ODataExpressionParser;

public class JPQLSelectContext extends JPQLContext implements
		JPQLSelectContextView {


	protected String selectExpression;
	protected HashMap<String, String> orderByCollection;
	protected String whereCondition;

	protected boolean isCountOnly = false;//Support for $count
	
	public JPQLSelectContext(boolean isCountOnly){
		this.isCountOnly = isCountOnly; 
	}

	protected final void setOrderByCollection(
			HashMap<String, String> orderByCollection) {
		this.orderByCollection = orderByCollection;
	}

	protected final void setWhereExpression(String filterExpression) {
		this.whereCondition = filterExpression;
	}
	
	protected final void setSelectExpression(String selectExpression) {
		this.selectExpression = selectExpression;
	}

	@Override
	public String getSelectExpression() {
		return selectExpression;
	}
	
	@Override
	public HashMap<String, String> getOrderByCollection() {
		return this.orderByCollection;
	}

	@Override
	public String getWhereExpression() {
		return this.whereCondition;
	}

	public class JPQLSelectContextBuilder
			extends
			com.sap.core.odata.processor.api.jpa.jpql.JPQLContext.JPQLContextBuilder {

		protected GetEntitySetUriInfo entitySetView;

		@Override
		public JPQLContext build() throws ODataJPAModelException,
				ODataJPARuntimeException {
			if (entitySetView != null) {

				try {

					if(JPQLSelectContext.this.isCountOnly)
						JPQLSelectContext.this.setType(JPQLContextType.SELECT_COUNT);
					else
						JPQLSelectContext.this.setType(JPQLContextType.SELECT);
					EdmEntityType entityType = entitySetView
							.getTargetEntitySet().getEntityType();
					EdmMapping mapping = entityType.getMapping();
					if(mapping != null)
						JPQLSelectContext.this.setJPAEntityName(mapping.getInternalName());
					else
						JPQLSelectContext.this.setJPAEntityName(entityType.getName());
					
					JPQLSelectContext.this.setJPAEntityAlias(generateJPAEntityAlias());

					JPQLSelectContext.this
							.setOrderByCollection(generateOrderByFileds());

					JPQLSelectContext.this
							.setSelectExpression(generateSelectExpression());

					JPQLSelectContext.this
							.setWhereExpression(generateWhereExpression());
				} catch (ODataException e) {
					throw ODataJPARuntimeException.throwException(ODataJPARuntimeException.INNER_EXCEPTION, e);
				}
			}

			return JPQLSelectContext.this;

		}

		@Override
		protected void setResultsView(Object resultsView) {
			if (resultsView instanceof GetEntitySetUriInfo) {
				this.entitySetView = (GetEntitySetUriInfo) resultsView;
			}

		}
		
		/*
		 * Generate Select Clause 
		 */
		protected String generateSelectExpression() throws EdmException {
			return getJPAEntityAlias();			
		}

		/*
		 * Generate Order By Clause Fields
		 */
		protected HashMap<String, String> generateOrderByFileds()
				throws ODataJPARuntimeException, EdmException {

			if (entitySetView.getOrderBy() != null) {

				return ODataExpressionParser
						.parseToJPAOrderByExpression(entitySetView.getOrderBy(),getJPAEntityAlias());

			} else if (entitySetView.getTop() != null
					|| entitySetView.getSkip() != null) {

					return ODataExpressionParser
							.parseKeyPropertiesToJPAOrderByExpression(entitySetView.getTargetEntitySet()
									.getEntityType().getKeyProperties(),getJPAEntityAlias());
			} else
				return null;
			
		}

		/*
		 * Generate Where Clause Expression
		 */
		protected String generateWhereExpression() throws ODataException {
			if(entitySetView.getFilter() != null){ 
			return ODataExpressionParser
					.parseToJPAWhereExpression(entitySetView.getFilter(),getJPAEntityAlias());
			}
			return null;
		}
	}

	
}
