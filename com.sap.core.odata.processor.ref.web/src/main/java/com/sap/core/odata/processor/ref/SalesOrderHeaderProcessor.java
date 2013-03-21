package com.sap.core.odata.processor.ref;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.annotation.edm.Facets;
import com.sap.core.odata.api.annotation.edm.FunctionImport;
import com.sap.core.odata.api.annotation.edm.FunctionImport.Multiplicity;
import com.sap.core.odata.api.annotation.edm.FunctionImport.ReturnType;
import com.sap.core.odata.api.annotation.edm.Parameter;
import com.sap.core.odata.processor.ref.jpa.Address;
import com.sap.core.odata.processor.ref.jpa.SalesOrderHeader;

public class SalesOrderHeaderProcessor {

	@FunctionImport(name = "FindAllSalesOrders", entitySet = "SalesOrders", returnType = ReturnType.ENTITY_TYPE, multiplicity = Multiplicity.MANY)
	public List<SalesOrderHeader> findAllSalesOrders(
			@Parameter(name = "LifeCycleStatusCode", facets = @Facets(maxLength = 2)) String status) {
		return new ArrayList<SalesOrderHeader>();
	}

	@FunctionImport(name = "CheckATP", returnType = ReturnType.SCALAR, multiplicity = Multiplicity.ONE)
	public boolean checkATP(
			@Parameter(name = "SoID", facets = @Facets(nullable = false)) long soID,
			@Parameter(name = "LiId", facets = @Facets(nullable = false)) long lineItemID) {
		return false;
	}

	@FunctionImport(returnType = ReturnType.ENTITY_TYPE)
	public SalesOrderHeader calculateNetAmount() {
		return null;
	}

	@FunctionImport(returnType = ReturnType.COMPLEX_TYPE)
	public Address getAddress(
			@Parameter(name = "SoID", facets = @Facets(nullable = false)) long soID) {
		return null;
	}

}
