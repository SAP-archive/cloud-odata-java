package com.sap.core.odata.processor.jpa.jpql;

import java.util.HashMap;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetView;
import com.sap.core.odata.processor.jpa.access.ExpressionParsingUtility;
import com.sap.core.odata.processor.jpa.jpql.api.JPQLContext;
import com.sap.core.odata.processor.jpa.jpql.api.JPQLContextType;
import com.sap.core.odata.processor.jpa.jpql.api.JPQLSelectContext;

public class JPQLSelectContextImpl extends JPQLSelectContext {

	private String[] selectedFields;
	private HashMap<String, String> orderByCollection;
	private FilterExpression whereCondition;

	@Override
	protected final void setSelectedFields(String[] selectedFields) {
		this.selectedFields = selectedFields;
	}

	@Override
	protected final void setOrderByCollection(
			HashMap<String, String> orderByCollection) {
		this.orderByCollection = orderByCollection;
	}

	@Override
	protected final void setWhereExpression(FilterExpression filterExpression) {
		this.whereCondition = filterExpression;
	}

	@Override
	public String[] getSelectedFields() {
		return this.selectedFields;
	}

	@Override
	public HashMap<String, String> getOrderByCollection() {
		return this.orderByCollection;
	}

	@Override
	public FilterExpression getWhereExpression() {
		return this.whereCondition;
	}

	public class JPQLSelectContextBuilder
			extends
			com.sap.core.odata.processor.jpa.jpql.api.JPQLContext.JPQLContextBuilder {

		private GetEntitySetView entitySetView;

		@Override
		public JPQLContext build() {
			if (entitySetView != null) {

				try {
					
					JPQLSelectContextImpl.this.setType(JPQLContextType.SELECT);
					
					JPQLSelectContextImpl.this.setJPAEntityName(entitySetView
							.getTargetEntitySet().getEntityType().getName());
					JPQLSelectContextImpl.this
							.setOrderByCollection(ExpressionParsingUtility
									.parseOrderByExpression(entitySetView
											.getOrderBy()));
				} catch (EdmException e) {
					e.printStackTrace();
				}

			}

			return JPQLSelectContextImpl.this;

		}

		@Override
		protected void setResultsView(Object resultsView) {
			if (resultsView instanceof GetEntitySetView) {
				this.entitySetView = (GetEntitySetView) resultsView;
			}

		}

	}
}
