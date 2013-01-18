package com.sap.core.odata.processor.jpa.api.factory;

import com.sap.core.odata.processor.jpa.api.jpql.JPQLContext.JPQLContextBuilder;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContextType;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContextView;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLStatement.JPQLStatementBuilder;

public interface JPQLBuilderFactory {
	public JPQLStatementBuilder getStatementBuilder(JPQLContextView context);
	public JPQLContextBuilder getContextBuilder(JPQLContextType contextType);
}
