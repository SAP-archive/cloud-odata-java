package com.sap.core.odata.processor.jpa.api.jpql;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.uri.KeyPredicate;

public interface JPQLSelectSingleContextView {
	public ArrayList<String> getSelectedFields();
	public List<KeyPredicate> getKeyPredicates();
}
