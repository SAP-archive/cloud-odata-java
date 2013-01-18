package com.sap.core.odata.processor.jpa.jpql;

import java.util.List;

import com.sap.core.odata.processor.jpa.api.access.JPAReferenceEntityKey;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLJoinContextView;

public abstract class JPQLJoinContext extends JPQLSelectContext implements JPQLJoinContextView {
	protected abstract void setReferenceEntityKeys(List<JPAReferenceEntityKey> referenceEntityKey);
	
}
