package com.sap.core.odata.processor.jpa.access;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.uri.expression.BinaryExpression;
import com.sap.core.odata.api.uri.expression.BinaryOperator;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.LiteralExpression;
import com.sap.core.odata.api.uri.expression.MemberExpression;
import com.sap.core.odata.api.uri.expression.PropertyExpression;
//import com.sap.core.odata.api.uri.expression.OrderType;

public class ExpressionParsingUtilityTest {

	@Test
	public void testParseWhereExpression() throws ODataException {
		
		String parsedStr = "";
		
		// Simple Binary query - 
		parsedStr = ExpressionParsingUtility.parseWhereExpression(getBinaryExpressionMockedObj(BinaryOperator.EQ, ExpressionKind.PROPERTY, "SalesOrder", "1234"));
		assertEquals(getAliasedProperty("SalesOrder")+ " = 1234", parsedStr);
		
		// complex query - 
		parsedStr = "";
		
		CommonExpression exp1 = getBinaryExpressionMockedObj(BinaryOperator.GE, ExpressionKind.PROPERTY, "SalesOrder", "1234");
		CommonExpression exp2 = getBinaryExpressionMockedObj(BinaryOperator.NE, ExpressionKind.PROPERTY, "SalesABC", "XYZ");

		parsedStr = parsedStr.concat(ExpressionParsingUtility.parseWhereExpression(getBinaryExpression(exp1, BinaryOperator.AND, exp2)));
		assertEquals(getAliasedProperty("SalesOrder")+ " >= 1234 AND "+getAliasedProperty("SalesABC")+ " <> XYZ", parsedStr);
	}
	
	@Test
	public void testMoreThanOneBinaryExpression() throws ODataException {
		// complex query - 
		String parsedStr = "";
		CommonExpression exp1 = getBinaryExpressionMockedObj(BinaryOperator.GE, ExpressionKind.PROPERTY, "SalesOrder", "1234");
		CommonExpression exp2 = getBinaryExpressionMockedObj(BinaryOperator.NE, ExpressionKind.PROPERTY, "SalesABC", "XYZ");

		parsedStr = ExpressionParsingUtility.parseWhereExpression(getBinaryExpression(exp1, BinaryOperator.AND, exp2));
		assertEquals(getAliasedProperty("SalesOrder")+ " >= 1234 AND "+getAliasedProperty("SalesABC")+ " <> XYZ", parsedStr);
		
		parsedStr = ExpressionParsingUtility.parseWhereExpression(getBinaryExpression(exp1, BinaryOperator.OR, exp2));
		assertEquals(getAliasedProperty("SalesOrder")+ " >= 1234 OR "+getAliasedProperty("SalesABC")+ " <> XYZ", parsedStr);
	}
	
	@Test
	public void testParseFilterExpression() throws ODataException {
		assertEquals(getAliasedProperty("SalesOrder"), 
				ExpressionParsingUtility.parseWhereExpression(getFilterExpressionMockedObj(ExpressionKind.PROPERTY, "SalesOrder")));
	}
	
	@Test
	public void testAllBinaryOperators() throws ODataException { //Test for all Binary Operators 
		// complex query - 
		String parsedStr1 = "";
		String parsedStr2 = "";
		
//		String finalParsedStr = "";
		
		CommonExpression exp1 = getBinaryExpressionMockedObj(BinaryOperator.LT, ExpressionKind.PROPERTY, "SalesOrder", "1234");
		CommonExpression exp2 = getBinaryExpressionMockedObj(BinaryOperator.LE, ExpressionKind.PROPERTY, "SalesABC", "XYZ");
		
		parsedStr1 = ExpressionParsingUtility.parseWhereExpression((BinaryExpression) getBinaryExpression(exp1, BinaryOperator.AND, exp2));
//		BinaryExpression be1 = (BinaryExpression) getBinaryExpression(exp1, BinaryOperator.AND, exp2);
		
		assertEquals(getAliasedProperty("SalesOrder")+ " < 1234 AND "+getAliasedProperty("SalesABC")+ " <= XYZ", parsedStr1);
		
		CommonExpression exp3 = getBinaryExpressionMockedObj(BinaryOperator.GT, ExpressionKind.PROPERTY, "LineItems", "2345");
		CommonExpression exp4 = getBinaryExpressionMockedObj(BinaryOperator.GE, ExpressionKind.PROPERTY, "SalesOrder", "Amazon");

		parsedStr2 = ExpressionParsingUtility.parseWhereExpression(getBinaryExpression(exp3, BinaryOperator.AND, exp4));
//		BinaryExpression be2 = (BinaryExpression) getBinaryExpression(exp3, BinaryOperator.AND, exp4);
//		finalParsedStr = ExpressionParsingUtility.parseWhereExpression((BinaryExpression) getBinaryExpression(be1, BinaryOperator.OR, be2));
		
		assertEquals(getAliasedProperty("LineItems")+ " > 2345 AND "+getAliasedProperty("SalesOrder")+ " >= Amazon", parsedStr2);
		
//		assertEquals(getAliasedProperty("SalesOrder")+ " < 1234 AND "+getAliasedProperty("SalesABC")+ " <= XYZ" + " OR "
//					+getAliasedProperty("LineItems")+ " > 2345 AND "+getAliasedProperty("SalesOrder")+ " >= Amazon"
//					, finalParsedStr);
		
		
	}
	
	@Test
	public void testParseMemberExpression() throws ODataException {
		assertEquals(getAliasedProperty("Address")+"."+ "city"+" = "+"\'City_3\'", ExpressionParsingUtility.parseWhereExpression(getBinaryExpression(getMemberExpressionMockedObj("Address", "city"), 
				BinaryOperator.EQ, getLiteralExpressionMockedObj("\'City_3\'"))));
		
	}
	
	private CommonExpression getMemberExpressionMockedObj(String pathUriLiteral, String propertyUriLiteral) {
		MemberExpression memberExpression = EasyMock.createMock(MemberExpression.class);
		EasyMock.expect(memberExpression.getPath()).andReturn(getPropertyExpressionPath(pathUriLiteral)).times(10);
		EasyMock.expect(memberExpression.getProperty()).andReturn(getPropertyExpressionPath(propertyUriLiteral)).times(10);
		EasyMock.expect(memberExpression.getKind()).andReturn(ExpressionKind.MEMBER).times(10);
		
		EasyMock.replay(memberExpression);
		return memberExpression;
	}

	private CommonExpression getPropertyExpressionPath(String uriLiteral) {
		PropertyExpression 	propertyExpression = EasyMock.createMock(PropertyExpression.class);
		EasyMock.expect(propertyExpression.getUriLiteral()).andReturn(uriLiteral).times(10);
		
		EasyMock.replay(propertyExpression);
		return propertyExpression;
	}

/*	@Test
	public void testParseOrderByExpression() throws EdmException {
		//fail("Not yet implemented");
		HashMap<String, String> orderByMap = ExpressionParsingUtility.parseOrderByExpression(getOrderByExpressionMockedObj());
		
		String orderByDirection = orderByMap.get("Name 0");
		assertEquals("DESC", orderByDirection);
		
	}*/

	private String getAliasedProperty(String property){
		return ExpressionParsingUtility.TABLE_ALIAS+"."+property;
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
		EasyMock.expect(edmSimpleType.valueOfString(value, EdmLiteralKind.URI, getEdmFacetsMockedObj())).andStubReturn(value);
		EasyMock.expect(edmSimpleType.valueOfString(value, EdmLiteralKind.URI, null)).andStubReturn(value);
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
	
	private FilterExpression getFilterExpressionMockedObj(ExpressionKind leftOperandExpKind, String propertyName) throws EdmException{
		FilterExpression filterExpression = EasyMock.createMock(FilterExpression.class);
		EasyMock.expect(filterExpression.getKind()).andStubReturn(ExpressionKind.FILTER);
		EasyMock.expect(filterExpression.getExpression()).andStubReturn(getPropertyExpressionMockedObj(leftOperandExpKind, propertyName));
		
		EasyMock.replay(filterExpression);
		return filterExpression;		
	}
	
	private CommonExpression getBinaryExpression(final CommonExpression leftOperand, final BinaryOperator operator, final CommonExpression rightOperand) {
		BinaryExpression binaryExpression = EasyMock.createMock(BinaryExpression.class);
		EasyMock.expect(binaryExpression.getKind()).andStubReturn(ExpressionKind.BINARY);
		EasyMock.expect(binaryExpression.getLeftOperand()).andStubReturn(leftOperand);
		EasyMock.expect(binaryExpression.getRightOperand()).andStubReturn(rightOperand);
		EasyMock.expect(binaryExpression.getOperator()).andStubReturn(operator);
		
		EasyMock.replay(binaryExpression);
		return binaryExpression;
	}
	

/*	private OrderByExpression getOrderByExpressionMockedObj() throws EdmException {
		OrderByExpression orderByExpression = EasyMock.createMock(OrderByExpression.class);
		EasyMock.expect(orderByExpression.getOrders()).andStubReturn(getOrdersMockedObj());
		EasyMock.replay(orderByExpression);
		return orderByExpression;
	}

	private List<OrderExpression> getOrdersMockedObj() throws EdmException {
		List<OrderExpression> orderBys= new ArrayList<OrderExpression>();
		for (int i = 0; i < 1; i++) {
			orderBys.add(getOrderExpressionMockedObj(i));
		}
		return orderBys;
	}

	private OrderExpression getOrderExpressionMockedObj(int index) throws EdmException {
		OrderExpression orderExpression = EasyMock.createMock(OrderExpression.class);
		EasyMock.expect(orderExpression.getSortOrder()).andStubReturn(OrderType.desc);
		EasyMock.expect(orderExpression.getExpression()).andStubReturn(getLiteralExpressionMockedObj("Name "+index)).times(10);
		//EasyMock.expect(orderExpression.getExpression().getEdmType().getName()).andStubReturn("Name "+index);
		
		EasyMock.replay(orderExpression);
		return null;
	}*/
	

}
