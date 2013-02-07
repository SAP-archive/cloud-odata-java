package com.sap.core.odata.processor.api.jpa.factory;

import com.sap.core.odata.processor.api.jpa.jpql.JPQLContextType;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContextView;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContext.JPQLContextBuilder;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLStatement.JPQLStatementBuilder;

public interface JPQLBuilderFactory {
	public JPQLStatementBuilder getStatementBuilder(JPQLContextView context);
	public JPQLContextBuilder getContextBuilder(JPQLContextType contextType);
}
