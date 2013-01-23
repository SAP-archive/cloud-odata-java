/**
 * 
 */
package com.sap.core.odata.processor.jpa.jpql;

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
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.uri.SelectItem;
import com.sap.core.odata.api.uri.expression.BinaryExpression;
import com.sap.core.odata.api.uri.expression.BinaryOperator;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.LiteralExpression;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.api.uri.expression.PropertyExpression;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContext.JPQLContextBuilder;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContextType;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.exception.ODataJPARuntimeException;

public class JPQLSelectStatementBuilderTest {

	/**
	 * @throws java.lang.Exception
	 */	
	private JPQLSelectStatementBuilder jpqlSelectStatementBuilder; 
	
	@Before
	public void setUp() throws Exception {

		/*//Object Instantiation
		
		JPQLSelectContextImpl jpqlSelectContextImpl = new JPQLSelectContextImpl();
		GetEntitySetUriInfo getEntitySetView = EasyMock.createMock(GetEntitySetUriInfo.class);
		ODataJPAContext odataJPAContext = EasyMock.createMock(ODataJPAContext.class);
		EdmEntitySet edmEntitySet = EasyMock.createMock(EdmEntitySet.class);
		EdmEntityType edmEntityType = EasyMock.createMock(EdmEntityType.class);
		OrderByExpression orderByExpression = EasyMock.createMock(OrderByExpression.class);
		List<SelectItem> selectItemList = null;

		//Setting up the expected value
		
		EasyMock.expect(getEntitySetView.getTargetEntitySet()).andStubReturn(edmEntitySet);
		EasyMock.expect(getEntitySetView.getOrderBy()).andStubReturn(orderByExpression);
		EasyMock.expect(getEntitySetView.getSelect()).andStubReturn(selectItemList);
		EasyMock.expect(getEntitySetView.getFilter()).andStubReturn(null);
		EasyMock.replay(getEntitySetView);
		EasyMock.expect(edmEntitySet.getEntityType()).andStubReturn(edmEntityType);
		EasyMock.replay(edmEntitySet);
		EasyMock.expect(edmEntityType.getName()).andStubReturn("SalesOrderHeader");
		EasyMock.replay(edmEntityType);
		EasyMock.replay(odataJPAContext);
		
		JPQLContextBuilder contextBuilder1 = JPQLSelectContextImpl.createBuilder(JPQLContextType.SELECT, getEntitySetView, odataJPAContext);
		try {
			jpqlSelectContextImpl = (JPQLSelectContextImpl) contextBuilder1.build();
		} catch (ODataJPAModelException e) {
			fail("Model Exception thrown");
		}
		
		jpqlSelectStatementBuilder = new JPQLSelectStatementBuilder(jpqlSelectContextImpl);*/
				
	}
	
	private JPQLSelectContext createSelectContext(OrderByExpression orderByExpression, FilterExpression filterExpression) throws ODataJPARuntimeException, EdmException{
		//Object Instantiation
		
				JPQLSelectContext jpqlSelectContextImpl = null;// new JPQLSelectContextImpl();
				GetEntitySetUriInfo getEntitySetView = EasyMock.createMock(GetEntitySetUriInfo.class);
				
				EdmEntitySet edmEntitySet = EasyMock.createMock(EdmEntitySet.class);
				EdmEntityType edmEntityType = EasyMock.createMock(EdmEntityType.class);
//				OrderByExpression orderByExpression = EasyMock.createMock(OrderByExpression.class);
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
		orderByCollection.put("soID", "ASC");
		orderByCollection.put("buyerId", "DESC");
		jpqlSelectContextImpl.setOrderByCollection(orderByCollection);
		jpqlSelectStatementBuilder = new JPQLSelectStatementBuilder(jpqlSelectContextImpl);
		
		assertEquals("SELECT E1 FROM SalesOrderHeader E1 ORDER BY E1.buyerId DESC , E1.soID ASC",jpqlSelectStatementBuilder.build().toString());
	}
	
	@Test
	public void testBuildQueryWithFilter() throws EdmException, ODataJPARuntimeException {
		OrderByExpression orderByExpression = EasyMock.createMock(OrderByExpression.class);
		FilterExpression filterExpression = getFilterExpressionMockedObj();
		JPQLSelectContext jpqlSelectContextImpl = createSelectContext(orderByExpression, filterExpression);
		
		jpqlSelectStatementBuilder = new JPQLSelectStatementBuilder(jpqlSelectContextImpl);
		
		assertEquals("SELECT E1 FROM SalesOrderHeader E1 WHERE E1.soID >= 1234",jpqlSelectStatementBuilder.build().toString());
	}
	
	/*@Test
	public void testBuildQueryWithMethodExpression() throws EdmException, ODataJPARuntimeException {
		OrderByExpression orderByExpression = EasyMock.createMock(OrderByExpression.class);
		FilterExpression filterExpression = getFilterExpressionwithMethodMock();
		JPQLSelectContext jpqlSelectContextImpl = createSelectContext(orderByExpression, filterExpression);
		
		jpqlSelectStatementBuilder = new JPQLSelectStatementBuilder(jpqlSelectContextImpl);
		try
		{
		jpqlSelectStatementBuilder.build();
		}catch(ODataJPARuntimeException e){
			assertEquals(e.getMessage(), "\"OData - JPA Runtime: Internal error [null]\"");
		}
	}*/
	
	private FilterExpression getFilterExpressionMockedObj() throws EdmException{
		FilterExpression filterExpression = EasyMock.createMock(FilterExpression.class);
		EasyMock.expect(filterExpression.getKind()).andStubReturn(ExpressionKind.FILTER);
		EasyMock.expect(filterExpression.getExpression()).andStubReturn(getBinaryExpressionMockedObj(BinaryOperator.GE, ExpressionKind.PROPERTY, "soID", "1234"));
		
		EasyMock.replay(filterExpression);
		return filterExpression;		
	}
	/*private FilterExpression getFilterExpressionwithMethodMock() throws EdmException{
		FilterExpression filterExpression = EasyMock.createMock(FilterExpression.class);
		EasyMock.expect(filterExpression.getKind()).andStubReturn(ExpressionKind.FILTER);
		EasyMock.expect(filterExpression.getExpression()).andStubReturn(getMethodExpressionMockedObj());
		
		EasyMock.replay(filterExpression);
		return filterExpression;		
	}*/
	
	/*private MethodExpression getMethodExpressionMockedObj() throws EdmException{
		MethodExpression methodExpression = EasyMock.createMock(MethodExpression.class);
		EasyMock.expect(methodExpression.getKind()).andStubReturn(ExpressionKind.METHOD);
//		EasyMock.expect(methodExpression.getExpression()).andStubReturn(getBinaryExpressionMockedObj(BinaryOperator.GE, ExpressionKind.PROPERTY, "soID", "1234"));
		
		EasyMock.replay(methodExpression);
		return methodExpression;		
	}*/
	
	private PropertyExpression getPropertyExpressionMockedObj(ExpressionKind expKind, String propertyName){
		PropertyExpression  leftOperandPropertyExpresion  = EasyMock.createMock(PropertyExpression.class);
		EasyMock.expect(leftOperandPropertyExpresion.getKind()).andStubReturn(ExpressionKind.PROPERTY);
		EasyMock.expect(leftOperandPropertyExpresion.getPropertyName()).andStubReturn(propertyName);
		EasyMock.replay(leftOperandPropertyExpresion);
		return leftOperandPropertyExpresion;
	}
	
	private BinaryExpression getBinaryExpressionMockedObj(BinaryOperator operator, ExpressionKind leftOperandExpKind, String propertyName,  String literalStr) throws EdmException{
		BinaryExpression binaryExpression = EasyMock.createMock(BinaryExpression.class);
		EasyMock.expect(binaryExpression.getKind()).andStubReturn(ExpressionKind.BINARY);
		EasyMock.expect(binaryExpression.getLeftOperand()).andStubReturn(getPropertyExpressionMockedObj(leftOperandExpKind, propertyName));
		EasyMock.expect(binaryExpression.getOperator()).andStubReturn(operator);
		EasyMock.expect(binaryExpression.getRightOperand()).andStubReturn(getLiteralExpressionMockedObj(literalStr));
		
		EasyMock.replay(binaryExpression);
		return binaryExpression;		
	}
	
	private LiteralExpression getLiteralExpressionMockedObj(String uriLiteral) throws EdmException{
		LiteralExpression rightOperandLiteralExpresion = EasyMock.createMock(LiteralExpression.class);
		EasyMock.expect(rightOperandLiteralExpresion.getKind()).andStubReturn(ExpressionKind.LITERAL);
		EasyMock.expect(rightOperandLiteralExpresion.getUriLiteral()).andStubReturn(uriLiteral);// "1234"
		EasyMock.expect(rightOperandLiteralExpresion.getEdmType()).andStubReturn(getEdmSimpleTypeMockedObj(uriLiteral));
		EasyMock.replay(rightOperandLiteralExpresion);
		return rightOperandLiteralExpresion;
		
	}
	
	private EdmSimpleType getEdmSimpleTypeMockedObj(String value) throws EdmException{
		EdmSimpleType edmSimpleType = EasyMock.createMock(EdmSimpleType.class);
		EasyMock.expect(edmSimpleType.getName()).andStubReturn(value);
		EasyMock.expect(edmSimpleType.getKind()).andStubReturn(EdmTypeKind.SIMPLE);
		EasyMock.expect(edmSimpleType.valueOfString(value, EdmLiteralKind.URI, getEdmFacetsMockedObj(), null)).andStubReturn(value);
		EasyMock.expect(edmSimpleType.valueOfString(value, EdmLiteralKind.URI, null, null)).andStubReturn(value);
		EasyMock.expect(edmSimpleType.valueToString(value, EdmLiteralKind.DEFAULT, getEdmFacetsMockedObj())).andStubReturn(value);
		EasyMock.expect(edmSimpleType.valueToString(value, EdmLiteralKind.DEFAULT, null)).andStubReturn(value);
		EasyMock.replay(edmSimpleType);
		return edmSimpleType;
	}
	
	private EdmFacets getEdmFacetsMockedObj(){
		EdmFacets facets = EasyMock.createMock(EdmFacets.class);
		
		EasyMock.replay(facets);
		return facets;
	}

}
