package com.sap.core.odata.processor.jpa.jpql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.uri.SelectItem;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.processor.jpa.access.ODataExpressionParser;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContext;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContextType;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLSelectContext;

public class JPQLSelectContextImpl extends JPQLSelectContext {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JPQLSelectContextImpl.class);

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
			com.sap.core.odata.processor.jpa.api.jpql.JPQLContext.JPQLContextBuilder {

		private GetEntitySetUriInfo entitySetView;

		@Override
		public JPQLContext build() throws ODataJPAModelException, ODataJPARuntimeException {
			if (entitySetView != null) {

				try {

					JPQLSelectContextImpl.this.setType(JPQLContextType.SELECT);

					JPQLSelectContextImpl.this.setJPAEntityName(entitySetView
							.getTargetEntitySet().getEntityType().getName());
					JPQLSelectContextImpl.this
							.setOrderByCollection(ODataExpressionParser
									.parseToJPAOrderByExpression(entitySetView
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
					LOGGER.error(e.getMessage(), e);
					throw ODataJPARuntimeException.throwException(
							ODataJPARuntimeException.RUNTIME_EXCEPTION.addContent(e
									.getMessage()), e);
				} catch (ODataJPARuntimeException e) {
					LOGGER.error(e.getMessage(), e);
					throw ODataJPARuntimeException.throwException(
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
