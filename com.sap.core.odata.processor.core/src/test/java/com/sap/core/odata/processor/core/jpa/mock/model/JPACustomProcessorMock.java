package com.sap.core.odata.processor.core.jpa.mock.model;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.annotation.edm.Facets;
import com.sap.core.odata.api.annotation.edm.FunctionImport;
import com.sap.core.odata.api.annotation.edm.FunctionImport.Multiplicity;
import com.sap.core.odata.api.annotation.edm.Parameter;
import com.sap.core.odata.api.annotation.edm.FunctionImport.ReturnType;
import com.sap.core.odata.api.annotation.edm.Parameter.Mode;

public class JPACustomProcessorMock {

	public static final String className = "JPACustomProcessorMock";
	public static final String edmName = "JPACustomProcessor";

	@FunctionImport(name = "Method1", entitySet = "MockSet", returnType = ReturnType.ENTITY_TYPE, multiplicity = Multiplicity.MANY)
	public List<JPACustomProcessorMock> method1(
			@Parameter(name = "Param1", facets = @Facets(nullable = true, maxLength = 2), mode = Mode.IN) String param1,
			int param2,
			@Parameter(name = "Param3", facets = @Facets(precision = 10, scale = 2), mode = Mode.IN) double param3) {
		return new ArrayList<JPACustomProcessorMock>();
	}

	@FunctionImport(name = "Method2", entitySet = "MockSet", returnType = ReturnType.ENTITY_TYPE, multiplicity = Multiplicity.MANY)
	public List<JPACustomProcessorMock> method2(
			@Parameter(facets = @Facets(maxLength = 2), name = "Param2") String param2) {
		return new ArrayList<JPACustomProcessorMock>();
	}

	@FunctionImport(returnType = ReturnType.SCALAR)
	public int method3(@Parameter(name = "Param3") String param3) {
		return 0;
	}

	@FunctionImport(returnType = ReturnType.NONE)
	public void method4() {
		return;
	}

	@FunctionImport(returnType = ReturnType.ENTITY_TYPE, entitySet="MockSet", multiplicity = Multiplicity.ONE)
	public JPACustomProcessorMock method7() {
		return null;
	}

	@FunctionImport(returnType = ReturnType.COMPLEX_TYPE, multiplicity = Multiplicity.ONE)
	public JPACustomProcessorMock method9() {
		return null;
	}

	@FunctionImport(returnType = ReturnType.COMPLEX_TYPE, multiplicity = Multiplicity.MANY)
	public List<JPACustomProcessorMock> method10() {
		return null;
	}

}
