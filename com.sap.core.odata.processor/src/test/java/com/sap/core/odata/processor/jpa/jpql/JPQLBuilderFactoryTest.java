package com.sap.core.odata.processor.jpa.jpql;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.easymock.EasyMock;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContext;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContext.JPQLContextBuilder;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContextType;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLStatement.JPQLStatementBuilder;
import com.sap.core.odata.processor.jpa.factory.ODataJPAFactoryImpl;
import com.sap.core.odata.processor.jpa.jpql.JPQLSelectContextImpl.JPQLSelectContextBuilder;

public class JPQLBuilderFactoryTest {
	
	@Test
	public void testGetStatementBuilderFactoryforSelect() throws ODataException{
		
		GetEntitySetUriInfo getEntitySetView = getUriInfo();
		
		// Build JPQL Context
		JPQLContext selectContext = JPQLContext.createBuilder(
				JPQLContextType.SELECT, getEntitySetView).build();
		JPQLStatementBuilder statementBuilder = new ODataJPAFactoryImpl().getJPQLBuilderFactory().getStatementBuilder(selectContext);
		
		assertTrue(statementBuilder instanceof JPQLSelectStatementBuilder);
				
	}
	
		
	@Test
	public void testGetContextBuilderforDelete() throws ODataException{
				
		// Build JPQL ContextBuilder
		JPQLContextBuilder  contextBuilder = new ODataJPAFactoryImpl().getJPQLBuilderFactory().getContextBuilder(JPQLContextType.DELETE);
				
		assertNull(contextBuilder);
				
	}
	
	@Test
	public void testGetContextBuilderforSelect() throws ODataException{
				
		// Build JPQL ContextBuilder
		JPQLContextBuilder  contextBuilder = new ODataJPAFactoryImpl().getJPQLBuilderFactory().getContextBuilder(JPQLContextType.SELECT);
				
		assertNotNull(contextBuilder);
		assertTrue(contextBuilder instanceof JPQLSelectContextBuilder);
				
	}


	private GetEntitySetUriInfo getUriInfo() throws EdmException {
		GetEntitySetUriInfo getEntitySetView = EasyMock.createMock(GetEntitySetUriInfo.class);
		EdmEntitySet edmEntitySet = EasyMock.createMock(EdmEntitySet.class);
		EdmEntityType edmEntityType = EasyMock.createMock(EdmEntityType.class);
		OrderByExpression orderByExpression = EasyMock.createMock(OrderByExpression.class);
		EasyMock.expect(getEntitySetView.getTargetEntitySet()).andStubReturn(edmEntitySet);
		EasyMock.expect(getEntitySetView.getOrderBy()).andStubReturn(orderByExpression);
		EasyMock.expect(getEntitySetView.getSelect()).andStubReturn(null);
		EasyMock.expect(getEntitySetView.getFilter()).andStubReturn(null);
		EasyMock.replay(getEntitySetView);
		EasyMock.expect(edmEntitySet.getEntityType()).andStubReturn(edmEntityType);
		EasyMock.replay(edmEntitySet);
		return getEntitySetView;
	}

}
