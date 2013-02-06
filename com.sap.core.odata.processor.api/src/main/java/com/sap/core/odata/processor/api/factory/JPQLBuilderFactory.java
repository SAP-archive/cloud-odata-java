package com.sap.core.odata.processor.api.factory;

import com.sap.core.odata.processor.api.jpql.JPQLContextType;
import com.sap.core.odata.processor.api.jpql.JPQLContextView;
import com.sap.core.odata.processor.api.jpql.JPQLContext.JPQLContextBuilder;
import com.sap.core.odata.processor.api.jpql.JPQLStatement.JPQLStatementBuilder;

public interface JPQLBuilderFactory {
	public JPQLStatementBuilder getStatementBuilder(JPQLContextView context);
	public JPQLContextBuilder getContextBuilder(JPQLContextType contextType);
}
