package com.sap.core.odata.processor.jpa.jpql;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.SelectItem;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContext;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContextType;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLSelectSingleContextView;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.exception.ODataJPARuntimeException;

public class JPQLSelectSingleContext extends JPQLContext implements JPQLSelectSingleContextView {
	
	private ArrayList<String> selectedFields;
	private List<KeyPredicate> keyPredicates;
	
	protected void setKeyPredicates(List<KeyPredicate> keyPredicates) {
		this.keyPredicates = keyPredicates;		
	}


	public List<KeyPredicate> getKeyPredicates() {
		return this.keyPredicates;
	}
	
	protected void setSelectedFields(ArrayList<String> selectedFields) {
		this.selectedFields = selectedFields;		
	}


	public ArrayList<String> getSelectedFields() {
		return this.selectedFields;
	}
	
	public class JPQLSelectSingleContextBuilder
			extends
			com.sap.core.odata.processor.jpa.api.jpql.JPQLContext.JPQLContextBuilder {

		protected GetEntityUriInfo entityView;

		@Override
		public JPQLContext build() throws ODataJPAModelException, ODataJPARuntimeException {
			if (entityView != null) {

				try {

					JPQLSelectSingleContext.this.setType(JPQLContextType.SELECT_SINGLE);

					JPQLSelectSingleContext.this.setJPAEntityName(entityView
							.getTargetEntitySet().getEntityType().getName());
					
					JPQLSelectSingleContext.this.setJPAEntityAlias(generateJPAEntityAlias());
					
					JPQLSelectSingleContext.this.setKeyPredicates(entityView.getKeyPredicates());
					
					JPQLSelectSingleContext.this.setSelectedFields(generateSelectFields());

				} catch (EdmException e) {
					throw ODataJPARuntimeException.throwException(
							ODataJPARuntimeException.GENERAL.addContent(e
									.getMessage()), e);
				}

			}

			return JPQLSelectSingleContext.this;

		}

		@Override
		protected void setResultsView(Object resultsView) {
			if (resultsView instanceof GetEntityUriInfo) {
				this.entityView = (GetEntityUriInfo) resultsView;
			}

		}
		
		/*
		 * Generate Select Clause Fields
		 */
		protected ArrayList<String> generateSelectFields() throws EdmException {

			List<SelectItem> selectItemList = entityView.getSelect();

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


	}

	


	
}
