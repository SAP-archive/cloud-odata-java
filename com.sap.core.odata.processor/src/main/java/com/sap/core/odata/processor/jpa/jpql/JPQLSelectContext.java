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
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContext;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContextType;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLSelectContextView;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.exception.ODataJPARuntimeException;

public class JPQLSelectContext extends JPQLContext implements
		JPQLSelectContextView {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(JPQLSelectContext.class);

	private ArrayList<String> selectedFields;
	private HashMap<String, String> orderByCollection;
	private FilterExpression whereCondition;

	protected final void setSelectedFields(ArrayList<String> selectedFields) {
		this.selectedFields = selectedFields;
	}

	protected final void setOrderByCollection(
			HashMap<String, String> orderByCollection) {
		this.orderByCollection = orderByCollection;
	}

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
		public JPQLContext build() throws ODataJPAModelException,
				ODataJPARuntimeException {
			if (entitySetView != null) {

				try {

					JPQLSelectContext.this.setType(JPQLContextType.SELECT);

					JPQLSelectContext.this.setJPAEntityName(entitySetView
							.getTargetEntitySet().getEntityType().getName());

					if (entitySetView.getOrderBy() != null) {

						JPQLSelectContext.this
						.setOrderByCollection(ODataExpressionParser
						.parseToJPAOrderByExpression(entitySetView
						.getOrderBy()));

					} else if (entitySetView.getTop() != null
							|| entitySetView.getSkip() != null) {

						JPQLSelectContext.this
						.setOrderByCollection(ODataExpressionParser
						.parseKeyPredicatesToJPAOrderByExpression(entitySetView
								.getKeyPredicates()));
					}

					JPQLSelectContext.this.setWhereExpression(entitySetView
							.getFilter());

					List<SelectItem> selectItemList = entitySetView.getSelect();
					if (selectItemList != null) {
						ArrayList<String> selectedFields = new ArrayList<String>(
								selectItemList.size());
						for (SelectItem item : selectItemList) {
							selectedFields.add(item.getProperty().getName());
						}
						JPQLSelectContext.this
								.setSelectedFields(selectedFields);
					}

				} catch (EdmException e) {
					LOGGER.error(e.getMessage(), e);
					throw ODataJPARuntimeException.throwException(
							ODataJPARuntimeException.RUNTIME_EXCEPTION
									.addContent(e.getMessage()), e);
				} catch (ODataJPARuntimeException e) {
					LOGGER.error(e.getMessage(), e);
					throw ODataJPARuntimeException.throwException(
							ODataJPARuntimeException.RUNTIME_EXCEPTION
									.addContent(e.getMessage()), e);
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

	}
}
