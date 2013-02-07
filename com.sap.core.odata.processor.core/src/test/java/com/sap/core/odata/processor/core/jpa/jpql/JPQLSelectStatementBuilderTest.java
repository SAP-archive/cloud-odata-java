/**
 * 
 */
package com.sap.core.odata.processor.core.jpa.jpql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.uri.SelectItem;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContextType;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContext.JPQLContextBuilder;
import com.sap.core.odata.processor.core.jpa.jpql.JPQLSelectContext;
import com.sap.core.odata.processor.core.jpa.jpql.JPQLSelectStatementBuilder;

public class JPQLSelectStatementBuilderTest {

	/**
	 * @throws java.lang.Exception
	 */	
	private JPQLSelectStatementBuilder jpqlSelectStatementBuilder; 
	
	@Before
	public void setUp() throws Exception {
					
	}
	
	private JPQLSelectContext createSelectContext(OrderByExpression orderByExpression, FilterExpression filterExpression) throws ODataJPARuntimeException, EdmException{
		//Object Instantiation
		
				JPQLSelectContext jpqlSelectContextImpl = null;
				GetEntitySetUriInfo getEntitySetView = EasyMock.createMock(GetEntitySetUriInfo.class);
				
				EdmEntitySet edmEntitySet = EasyMock.createMock(EdmEntitySet.class);
				EdmEntityType edmEntityType = EasyMock.createMock(EdmEntityType.class);
				List<SelectItem> selectItemList = null;

				//Setting up the expected value
				
				EasyMock.expect(getEntitySetView.getTargetEntitySet()).andStubReturn(edmEntitySet);
				EasyMock.expect(getEntitySetView.getOrderBy()).andStubReturn(orderByExpression);
				EasyMock.expect(getEntitySetView.getSelect()).andStubReturn(selectItemList);
				EasyMock.expect(getEntitySetView.getFilter()).andStubReturn(filterExpression);
				EasyMock.replay(getEntitySetView);
				EasyMock.expect(edmEntitySet.getEntityType()).andStubReturn(edmEntityType);
				EasyMock.replay(edmEntitySet);
				EasyMock.expect(edmEntityType.getName()).andStubReturn("SalesOrderHeader");
				EasyMock.replay(edmEntityType);
				
				JPQLContextBuilder contextBuilder1 = JPQLSelectContext.createBuilder(JPQLContextType.SELECT, getEntitySetView);
				try {
					jpqlSelectContextImpl = (JPQLSelectContext) contextBuilder1.build();
				} catch (ODataJPAModelException e) {
					fail("Model Exception thrown");
				}
				
				return jpqlSelectContextImpl;
	}
	
	/**
	 * Test method for {@link com.sap.core.odata.processor.jpa.jpql.JPQLSelectStatementBuilder#build)}.
	 * @throws EdmException 
	 * @throws ODataJPARuntimeException 
	 */
	
	@Test
	public void testBuildSimpleQuery() throws EdmException, ODataJPARuntimeException {
		OrderByExpression orderByExpression = EasyMock.createMock(OrderByExpression.class);
		JPQLSelectContext jpqlSelectContextImpl = createSelectContext(orderByExpression, null);
		jpqlSelectStatementBuilder = new JPQLSelectStatementBuilder(jpqlSelectContextImpl);
		
		assertEquals("SELECT E1 FROM SalesOrderHeader E1",jpqlSelectStatementBuilder.build().toString());
	}
	
	@Test
	public void testBuildQueryWithOrderBy() throws EdmException, ODataJPARuntimeException {
		OrderByExpression orderByExpression = EasyMock.createMock(OrderByExpression.class);
		
		JPQLSelectContext jpqlSelectContextImpl = createSelectContext(orderByExpression, null);
		HashMap<String, String> orderByCollection = new HashMap<String, String>();
		orderByCollection.put("E1.soID", "ASC");
		orderByCollection.put("E1.buyerId", "DESC");
		jpqlSelectContextImpl.setOrderByCollection(orderByCollection);
		jpqlSelectStatementBuilder = new JPQLSelectStatementBuilder(jpqlSelectContextImpl);
		
		assertEquals("SELECT E1 FROM SalesOrderHeader E1 ORDER BY E1.soID ASC , E1.buyerId DESC",jpqlSelectStatementBuilder.build().toString());
	}
	
	@Test
	public void testBuildQueryWithFilter() throws EdmException, ODataJPARuntimeException {
		OrderByExpression orderByExpression = EasyMock.createMock(OrderByExpression.class);
		FilterExpression filterExpression = null;//getFilterExpressionMockedObj();
		JPQLSelectContext jpqlSelectContextImpl = createSelectContext(orderByExpression, filterExpression);
		jpqlSelectContextImpl.setWhereExpression("E1.soID >= 1234");
		
		jpqlSelectStatementBuilder = new JPQLSelectStatementBuilder(jpqlSelectContextImpl);
		
		assertEquals("SELECT E1 FROM SalesOrderHeader E1 WHERE E1.soID >= 1234",jpqlSelectStatementBuilder.build().toString());
	}	

}
