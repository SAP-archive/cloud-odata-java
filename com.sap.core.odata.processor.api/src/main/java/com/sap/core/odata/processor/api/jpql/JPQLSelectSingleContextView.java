package com.sap.core.odata.processor.api.jpql;

import java.util.List;

import com.sap.core.odata.api.uri.KeyPredicate;

public interface JPQLSelectSingleContextView extends JPQLContextView {
	public String getSelectExpression();
	public List<KeyPredicate> getKeyPredicates();
}
