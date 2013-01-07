package com.sap.core.odata.processor.jpa.jpql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.uri.SelectItem;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.processor.jpa.access.ExpressionParsingUtility;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.jpa.jpql.api.JPQLContext;
import com.sap.core.odata.processor.jpa.jpql.api.JPQLContextType;
import com.sap.core.odata.processor.jpa.jpql.api.JPQLSelectContext;

public class JPQLSelectContextImpl extends JPQLSelectContext {

	private ArrayList<String> selectedFields;
	private HashMap<String, String> orderByCollection;
	private FilterExpression whereCondition;

	@Override
	protected final void setSelectedFields(ArrayList<String> selectedFields) {
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
	public ArrayList<String> getSelectedFields() {
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

		private GetEntitySetUriInfo entitySetView;

		@Override
		public JPQLContext build() throws ODataJPAModelException {
			if (entitySetView != null) {

				try {

					JPQLSelectContextImpl.this.setType(JPQLContextType.SELECT);

					JPQLSelectContextImpl.this.setJPAEntityName(entitySetView
							.getTargetEntitySet().getEntityType().getName());
					JPQLSelectContextImpl.this
							.setOrderByCollection(ExpressionParsingUtility
									.parseOrderByExpression(entitySetView
											.getOrderBy()));
					JPQLSelectContextImpl.this.setWhereExpression(entitySetView.getFilter());

					List<SelectItem> selectItemList = entitySetView.getSelect();
					if (selectItemList != null) {
						ArrayList<String> selectedFields = new ArrayList<String>(
								selectItemList.size());
						for (SelectItem item : selectItemList) {
							selectedFields.add(item. getProperty(). getName());
						}
						JPQLSelectContextImpl.this.setSelectedFields(selectedFields);
					}

				} catch (EdmException e) {
					throw ODataJPAModelException.throwException(
							ODataJPARuntimeException.RUNTIME_EXCEPTION.addContent(e
									.getMessage()), e);
				}

			}

			return JPQLSelectContextImpl.this;

		}

		@Override
		protected void setResultsView(Object resultsView) {
			if (resultsView instanceof GetEntitySetUriInfo) {
				this.entitySetView = (GetEntitySetUriInfo) resultsView;
			}

		}

	}
}
