package com.sap.core.odata.processor.jpa.jpql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.uri.SelectItem;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.processor.jpa.access.data.ODataExpressionParser;
import com.sap.core.odata.processor.jpa.api.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.api.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContext;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContextType;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLSelectContextView;

public class JPQLSelectContext extends JPQLContext implements
		JPQLSelectContextView {


	protected ArrayList<String> selectedFields;
	protected HashMap<String, String> orderByCollection;
	protected String whereCondition;

	protected final void setSelectedFields(ArrayList<String> selectedFields) {
		this.selectedFields = selectedFields;
	}

	protected final void setOrderByCollection(
			HashMap<String, String> orderByCollection) {
		this.orderByCollection = orderByCollection;
	}

	protected final void setWhereExpression(String filterExpression) {
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
	public String getWhereExpression() {
		return this.whereCondition;
	}

	public class JPQLSelectContextBuilder
			extends
			com.sap.core.odata.processor.jpa.api.jpql.JPQLContext.JPQLContextBuilder {

		protected GetEntitySetUriInfo entitySetView;

		@Override
		public JPQLContext build() throws ODataJPAModelException,
				ODataJPARuntimeException {
			if (entitySetView != null) {

				try {

					JPQLSelectContext.this.setType(JPQLContextType.SELECT);

					JPQLSelectContext.this.setJPAEntityName(entitySetView
							.getTargetEntitySet().getEntityType().getName());
					
					JPQLSelectContext.this.setJPAEntityAlias(generateJPAEntityAlias());

					JPQLSelectContext.this
							.setOrderByCollection(generateOrderByFileds());

					JPQLSelectContext.this
							.setSelectedFields(generateSelectFields());

					JPQLSelectContext.this
							.setWhereExpression(generateWhereExpression());
				} catch (ODataException e) {
					throw ODataJPARuntimeException.throwException(ODataJPARuntimeException.GENERAL, e);
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
		 * Generate Select Clause Fields
		 */
		protected ArrayList<String> generateSelectFields() throws EdmException {

			List<SelectItem> selectItemList = entitySetView.getSelect();

			if (selectItemList != null) {
				ArrayList<String> selectedFields = new ArrayList<String>(
						selectItemList.size());
				for (SelectItem item : selectItemList) {
					selectedFields.add(((EdmProperty)item.getProperty()).getMapping().getInternalName());
				}

				return selectedFields;
			}
			return null;
		}

		/*
		 * Generate Order By Clause Fields
		 */
		protected HashMap<String, String> generateOrderByFileds()
				throws ODataJPARuntimeException {

			if (entitySetView.getOrderBy() != null) {

				return ODataExpressionParser
						.parseToJPAOrderByExpression(entitySetView.getOrderBy(),getJPAEntityAlias());

			} else if (entitySetView.getTop() != null
					|| entitySetView.getSkip() != null) {

				return ODataExpressionParser
						.parseKeyPredicatesToJPAOrderByExpression(entitySetView
								.getKeyPredicates(),getJPAEntityAlias());
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
