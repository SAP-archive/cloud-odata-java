package com.sap.core.odata.processor.jpa.jpql;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.SelectItem;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContext;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContextType;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLSelectSingleContext;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.exception.ODataJPARuntimeException;

public class JPQLSelectSingleContextImpl extends JPQLSelectSingleContext {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JPQLSelectSingleContextImpl.class);
	
	private ArrayList<String> selectedFields;
	private List<KeyPredicate> keyPredicates;
	
	@Override
	protected void setKeyPredicates(List<KeyPredicate> keyPredicates) {
		this.keyPredicates = keyPredicates;		
	}


	@Override
	public List<KeyPredicate> getKeyPredicates() {
		return this.keyPredicates;
	}
	
	@Override
	protected void setSelectedFields(ArrayList<String> selectedFields) {
		this.selectedFields = selectedFields;		
	}


	@Override
	public ArrayList<String> getSelectedFields() {
		return this.selectedFields;
	}
	
	public class JPQLSelectSingleContextBuilder
			extends
			com.sap.core.odata.processor.jpa.api.jpql.JPQLContext.JPQLContextBuilder {

		private GetEntityUriInfo entityView;

		@Override
		public JPQLContext build() throws ODataJPAModelException, ODataJPARuntimeException {
			if (entityView != null) {

				try {

					JPQLSelectSingleContextImpl.this.setType(JPQLContextType.SELECT_SINGLE);

					JPQLSelectSingleContextImpl.this.setJPAEntityName(entityView
							.getTargetEntitySet().getEntityType().getName());
					
					JPQLSelectSingleContextImpl.this.setKeyPredicates(entityView.getKeyPredicates());
					
					List<SelectItem> selectItemList = entityView.getSelect();
					if (selectItemList != null) {
						ArrayList<String> selectedFields = new ArrayList<String>(
								selectItemList.size());
						for (SelectItem item : selectItemList) {
							selectedFields.add(item. getProperty(). getName());
						}
						JPQLSelectSingleContextImpl.this.setSelectedFields(selectedFields);
					}

				} catch (EdmException e) {
					LOGGER.error(e.getMessage(), e);
					throw ODataJPARuntimeException.throwException(
							ODataJPARuntimeException.RUNTIME_EXCEPTION.addContent(e
									.getMessage()), e);
				}

			}

			return JPQLSelectSingleContextImpl.this;

		}

		@Override
		protected void setResultsView(Object resultsView) {
			if (resultsView instanceof GetEntityUriInfo) {
				this.entityView = (GetEntityUriInfo) resultsView;
			}

		}

	}

	


	
}
