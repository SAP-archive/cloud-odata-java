package com.sap.core.odata.processor.core.jpa.mock.model;

import java.util.List;

import com.sap.core.odata.api.annotation.edm.FunctionImport;
import com.sap.core.odata.api.annotation.edm.FunctionImport.Multiplicity;
import com.sap.core.odata.api.annotation.edm.FunctionImport.ReturnType;
import com.sap.core.odata.api.annotation.edm.Parameter;

public class JPACustomProcessorNegativeMock {

	@FunctionImport(returnType = ReturnType.ENTITY_TYPE, multiplicity = Multiplicity.MANY)
	public List<JPACustomProcessorNegativeMock> method5() {
		return null;
	}

	@FunctionImport(returnType = ReturnType.ENTITY_TYPE, entitySet = "MockSet", multiplicity = Multiplicity.MANY)
	public void method6() {
		return;
	}

	@FunctionImport(returnType = ReturnType.ENTITY_TYPE, entitySet = "MockSet", multiplicity = Multiplicity.MANY)
	public JPACustomProcessorNegativeMock method8() {
		return null;
	}

	@FunctionImport(returnType = ReturnType.COMPLEX_TYPE, multiplicity = Multiplicity.ONE)
	public JPACustomProcessorNegativeMock method11() {
		return null;
	}

	@FunctionImport(returnType = ReturnType.SCALAR, multiplicity = Multiplicity.ONE)
	public JPACustomProcessorMock method12() {
		return null;
	}

	@FunctionImport(returnType = ReturnType.SCALAR, multiplicity = Multiplicity.ONE)
	public int method13(@Parameter(name = "") int y) {
		return 0;
	}
}
