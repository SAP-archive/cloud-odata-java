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
import com.sap.core.odata.api.uri.expression.LiteralExpression;
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
		EasyMock.expect(rightOperandLiteralExpresion.getKind()).andReturn(ExpressionKind.LITERAL).times(10);
		EasyMock.expect(rightOperandLiteralExpresion.getUriLiteral()).andReturn(uriLiteral).times(10);// "1234"
		EasyMock.expect(rightOperandLiteralExpresion.getEdmType()).andReturn(getEdmSimpleTypeMockedObj(uriLiteral)).times(10);
		EasyMock.replay(rightOperandLiteralExpresion);
		return rightOperandLiteralExpresion;
		
	}
	
	private EdmSimpleType getEdmSimpleTypeMockedObj(String value) throws EdmException{
		EdmSimpleType edmSimpleType = EasyMock.createMock(EdmSimpleType.class);
		EasyMock.expect(edmSimpleType.getName()).andReturn(value).times(15);
		EasyMock.expect(edmSimpleType.getKind()).andReturn(EdmTypeKind.SIMPLE);
		EasyMock.expect(edmSimpleType.valueOfString(value, EdmLiteralKind.URI, getEdmFacetsMockedObj())).andReturn(value).times(10);
		EasyMock.expect(edmSimpleType.valueOfString(value, EdmLiteralKind.URI, null)).andReturn(value).times(10);
		EasyMock.expect(edmSimpleType.valueToString(value, EdmLiteralKind.DEFAULT, getEdmFacetsMockedObj())).andReturn(value).times(10);
		EasyMock.expect(edmSimpleType.valueToString(value, EdmLiteralKind.DEFAULT, null)).andReturn(value).times(10);
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
		EasyMock.expect(leftOperandPropertyExpresion.getKind()).andReturn(ExpressionKind.PROPERTY);
		EasyMock.expect(leftOperandPropertyExpresion.getPropertyName()).andReturn(propertyName);
		EasyMock.replay(leftOperandPropertyExpresion);
		return leftOperandPropertyExpresion;
	}
	
	private BinaryExpression getBinaryExpressionMockedObj(BinaryOperator operator, ExpressionKind leftOperandExpKind, String propertyName,  String literalStr) throws EdmException{
		BinaryExpression binaryExpression = EasyMock.createMock(BinaryExpression.class);
		EasyMock.expect(binaryExpression.getKind()).andReturn(ExpressionKind.BINARY).times(10);
		EasyMock.expect(binaryExpression.getLeftOperand()).andReturn(getPropertyExpressionMockedObj(leftOperandExpKind, propertyName));
		EasyMock.expect(binaryExpression.getOperator()).andReturn(operator).times(10);
		EasyMock.expect(binaryExpression.getRightOperand()).andReturn(getLiteralExpressionMockedObj(literalStr));
		
		EasyMock.replay(binaryExpression);
		return binaryExpression;		
	}
	
	private CommonExpression getBinaryExpression(final CommonExpression leftOperand, final BinaryOperator operator, final CommonExpression rightOperand) {
		BinaryExpression binaryExpression = EasyMock.createMock(BinaryExpression.class);
		EasyMock.expect(binaryExpression.getKind()).andReturn(ExpressionKind.BINARY);
		EasyMock.expect(binaryExpression.getLeftOperand()).andReturn(leftOperand);
		EasyMock.expect(binaryExpression.getRightOperand()).andReturn(rightOperand);
		EasyMock.expect(binaryExpression.getOperator()).andReturn(operator);
		
		EasyMock.replay(binaryExpression);
		return binaryExpression;
	}
	

/*	private OrderByExpression getOrderByExpressionMockedObj() throws EdmException {
		OrderByExpression orderByExpression = EasyMock.createMock(OrderByExpression.class);
		EasyMock.expect(orderByExpression.getOrders()).andReturn(getOrdersMockedObj());
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
		EasyMock.expect(orderExpression.getSortOrder()).andReturn(OrderType.desc);
		EasyMock.expect(orderExpression.getExpression()).andReturn(getLiteralExpressionMockedObj("Name "+index)).times(10);
		//EasyMock.expect(orderExpression.getExpression().getEdmType().getName()).andReturn("Name "+index);
		
		EasyMock.replay(orderExpression);
		return null;
	}*/
	

}
