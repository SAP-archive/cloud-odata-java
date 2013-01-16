package com.sap.core.odata.processor.jpa.api.jpql;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.uri.KeyPredicate;

public abstract class JPQLSelectSingleContext extends JPQLContext {
	protected abstract void setSelectedFields(ArrayList<String> selectedFields);
	public abstract ArrayList<String> getSelectedFields();
	protected abstract void setKeyPredicates(List<KeyPredicate> keyPredicates);
	public abstract List<KeyPredicate> getKeyPredicates();
}

