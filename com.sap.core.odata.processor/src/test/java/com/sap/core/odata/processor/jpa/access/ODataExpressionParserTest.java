package com.sap.core.odata.processor.jpa.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.expression.BinaryExpression;
import com.sap.core.odata.api.uri.expression.BinaryOperator;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.LiteralExpression;
import com.sap.core.odata.api.uri.expression.MemberExpression;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.api.uri.expression.OrderExpression;
import com.sap.core.odata.api.uri.expression.PropertyExpression;
import com.sap.core.odata.api.uri.expression.SortOrder;
import com.sap.core.odata.api.uri.expression.UnaryExpression;
import com.sap.core.odata.api.uri.expression.UnaryOperator;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLStatement.Operator;
import com.sap.core.odata.processor.jpa.exception.ODataJPARuntimeException;

public class ODataExpressionParserTest {

	@Test
	public void testParseWhereExpression() throws ODataException {
		
		String parsedStr = "";
		
		// Simple Binary query - 
		parsedStr = ODataExpressionParser.parseToJPAWhereExpression(getBinaryExpressionMockedObj(BinaryOperator.EQ, ExpressionKind.PROPERTY, "SalesOrder", "1234"));
		assertEquals(getAliasedProperty("SalesOrder")+ " = 1234", parsedStr);
		
		// complex query - 
		parsedStr = "";
		
		CommonExpression exp1 = getBinaryExpressionMockedObj(BinaryOperator.GE, ExpressionKind.PROPERTY, "SalesOrder", "1234");
		CommonExpression exp2 = getBinaryExpressionMockedObj(BinaryOperator.NE, ExpressionKind.PROPERTY, "SalesABC", "XYZ");

		parsedStr = parsedStr.concat(ODataExpressionParser.parseToJPAWhereExpression(getBinaryExpression(exp1, BinaryOperator.AND, exp2)));
		assertEquals(getAliasedProperty("SalesOrder")+ " >= 1234 AND "+getAliasedProperty("SalesABC")+ " <> XYZ", parsedStr);
	}
	
	@Test
	public void testMoreThanOneBinaryExpression() throws ODataException {
		// complex query - 
		String parsedStr = "";
		CommonExpression exp1 = getBinaryExpressionMockedObj(BinaryOperator.GE, ExpressionKind.PROPERTY, "SalesOrder", "1234");
		CommonExpression exp2 = getBinaryExpressionMockedObj(BinaryOperator.NE, ExpressionKind.PROPERTY, "SalesABC", "XYZ");

		parsedStr = ODataExpressionParser.parseToJPAWhereExpression(getBinaryExpression(exp1, BinaryOperator.AND, exp2));
		assertEquals(getAliasedProperty("SalesOrder")+ " >= 1234 AND "+getAliasedProperty("SalesABC")+ " <> XYZ", parsedStr);
		
		parsedStr = ODataExpressionParser.parseToJPAWhereExpression(getBinaryExpression(exp1, BinaryOperator.OR, exp2));
		assertEquals(getAliasedProperty("SalesOrder")+ " >= 1234 OR "+getAliasedProperty("SalesABC")+ " <> XYZ", parsedStr);
	}
	
	@Test
	public void testParseFilterExpression() throws ODataException {
		assertEquals(getAliasedProperty("SalesOrder"), 
				ODataExpressionParser.parseToJPAWhereExpression(getFilterExpressionMockedObj(ExpressionKind.PROPERTY, "SalesOrder")));
	}
	
	@Test
	public void testAllBinaryOperators() throws ODataException { //Test for all Binary Operators 
		// complex query - 
		String parsedStr1 = "";
		String parsedStr2 = "";
		
		CommonExpression exp1 = getBinaryExpressionMockedObj(BinaryOperator.LT, ExpressionKind.PROPERTY, "SalesOrder", "1234");
		CommonExpression exp2 = getBinaryExpressionMockedObj(BinaryOperator.LE, ExpressionKind.PROPERTY, "SalesABC", "XYZ");
		
		parsedStr1 = ODataExpressionParser.parseToJPAWhereExpression((BinaryExpression) getBinaryExpression(exp1, BinaryOperator.AND, exp2));
		assertEquals(getAliasedProperty("SalesOrder")+ " < 1234 AND "+getAliasedProperty("SalesABC")+ " <= XYZ", parsedStr1);
		
		CommonExpression exp3 = getBinaryExpressionMockedObj(BinaryOperator.GT, ExpressionKind.PROPERTY, "LineItems", "2345");
		CommonExpression exp4 = getBinaryExpressionMockedObj(BinaryOperator.GE, ExpressionKind.PROPERTY, "SalesOrder", "Amazon");

		parsedStr2 = ODataExpressionParser.parseToJPAWhereExpression(getBinaryExpression(exp3, BinaryOperator.AND, exp4));
		
		assertEquals(getAliasedProperty("LineItems")+ " > 2345 AND "+getAliasedProperty("SalesOrder")+ " >= Amazon", parsedStr2);
	}
	
	@Test
	public void testParseMemberExpression() throws ODataException {
		assertEquals(getAliasedProperty("Address")+"."+ "city"+" = "+"\'City_3\'", ODataExpressionParser.parseToJPAWhereExpression(getBinaryExpression(getMemberExpressionMockedObj("Address", "city"), 
				BinaryOperator.EQ, getLiteralExpressionMockedObj("\'City_3\'"))));
		
	}
	
	@Test
	public void testParseUnaryExpression() throws ODataException {
		
		UnaryExpression unaryExpression = getUnaryExpressionMockedObj(getPropertyExpressionMockedObj(ExpressionKind.PROPERTY, "deliveryStatus"), 
				com.sap.core.odata.api.uri.expression.UnaryOperator.NOT);
		assertEquals(Operator.NOT+"("+getAliasedProperty("deliveryStatus")+")", ODataExpressionParser.parseToJPAWhereExpression(unaryExpression));
		
	}
	

	private UnaryExpression getUnaryExpressionMockedObj(CommonExpression operand, UnaryOperator unaryOperator) {
		UnaryExpression unaryExpression = EasyMock.createMock(UnaryExpression.class);
		EasyMock.expect(unaryExpression.getKind()).andReturn(ExpressionKind.UNARY).times(10);
		EasyMock.expect(unaryExpression.getOperand()).andReturn(operand).times(10);
		EasyMock.expect(unaryExpression.getOperator()).andReturn(unaryOperator).times(10);
		
		EasyMock.replay(unaryExpression);
		return unaryExpression;
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

	@Test
	public void testParseOrderByExpression() throws EdmException, ODataJPARuntimeException {
		//fail("Not yet implemented");
		HashMap<String, String> orderByMap = ODataExpressionParser.parseToJPAOrderByExpression(getOrderByExpressionMockedObj());
		
		String orderByDirection = orderByMap.get("Name 0");
		assertEquals("DESC", orderByDirection);
		
	}

	private String getAliasedProperty(String property){
		return ODataExpressionParser.TABLE_ALIAS+"."+property;
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
		EasyMock.expect(edmSimpleType.getName()).andReturn(value);
		EasyMock.expect(edmSimpleType.getKind()).andReturn(EdmTypeKind.SIMPLE).times(10);
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
		EasyMock.expect(leftOperandPropertyExpresion.getKind()).andReturn(ExpressionKind.PROPERTY).times(10);
		EasyMock.expect(leftOperandPropertyExpresion.getPropertyName()).andReturn(propertyName).times(10);
		EasyMock.replay(leftOperandPropertyExpresion);
		return leftOperandPropertyExpresion;
	}
	
	private BinaryExpression getBinaryExpressionMockedObj(BinaryOperator operator, ExpressionKind leftOperandExpKind, String propertyName,  String literalStr) throws EdmException{
		BinaryExpression binaryExpression = EasyMock.createMock(BinaryExpression.class);
		EasyMock.expect(binaryExpression.getKind()).andReturn(ExpressionKind.BINARY).times(10);
		EasyMock.expect(binaryExpression.getLeftOperand()).andReturn(getPropertyExpressionMockedObj(leftOperandExpKind, propertyName)).times(10);
		EasyMock.expect(binaryExpression.getOperator()).andReturn(operator).times(10);
		EasyMock.expect(binaryExpression.getRightOperand()).andReturn(getLiteralExpressionMockedObj(literalStr)).times(10);
		
		EasyMock.replay(binaryExpression);
		return binaryExpression;		
	}
	
	private FilterExpression getFilterExpressionMockedObj(ExpressionKind leftOperandExpKind, String propertyName) throws EdmException{
		FilterExpression filterExpression = EasyMock.createMock(FilterExpression.class);
		EasyMock.expect(filterExpression.getKind()).andReturn(ExpressionKind.FILTER).times(10);
		EasyMock.expect(filterExpression.getExpression()).andReturn(getPropertyExpressionMockedObj(leftOperandExpKind, propertyName)).times(10);
		
		EasyMock.replay(filterExpression);
		return filterExpression;		
	}
	
	private CommonExpression getBinaryExpression(final CommonExpression leftOperand, final BinaryOperator operator, final CommonExpression rightOperand) {
		BinaryExpression binaryExpression = EasyMock.createMock(BinaryExpression.class);
		EasyMock.expect(binaryExpression.getKind()).andReturn(ExpressionKind.BINARY).times(10);
		EasyMock.expect(binaryExpression.getLeftOperand()).andReturn(leftOperand).times(10);
		EasyMock.expect(binaryExpression.getRightOperand()).andReturn(rightOperand).times(10);
		EasyMock.expect(binaryExpression.getOperator()).andReturn(operator).times(10);
		
		EasyMock.replay(binaryExpression);
		return binaryExpression;
	}
	

	private OrderByExpression getOrderByExpressionMockedObj() throws EdmException {
		OrderByExpression orderByExpression = EasyMock.createMock(OrderByExpression.class);
		EasyMock.expect(orderByExpression.getOrders()).andReturn(getOrdersMockedObj()).times(10);
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
		EasyMock.expect(orderExpression.getSortOrder()).andReturn(SortOrder.desc).times(10);
		EasyMock.expect(orderExpression.getEdmType()).andReturn(getEdmSimpleTypeMockedObj("Name "+index)).times(10);
		//EasyMock.expect(orderExpression.getEdmType().getName()).andReturn("Name "+index).times(10);
		EasyMock.expect(orderExpression.getExpression()).andReturn(getLiteralExpressionMockedObj("Name "+index)).times(10);
		//EasyMock.expect(orderExpression.getExpression().getEdmType().getName()).andReturn("Name "+index);
		
		EasyMock.replay(orderExpression);
		return orderExpression;
	}
	
	@Test
	public void testParseKeyPredicates(){
		//Setting up the expected value
		KeyPredicate keyPredicate1 = EasyMock
				.createMock(KeyPredicate.class);
		EdmProperty kpProperty1 = EasyMock
				.createMock(EdmProperty.class);
		EasyMock.expect(keyPredicate1.getLiteral()).andStubReturn("1");
		KeyPredicate keyPredicate2 = EasyMock
				.createMock(KeyPredicate.class);
		EdmProperty kpProperty2 = EasyMock
				.createMock(EdmProperty.class);
		EasyMock.expect(keyPredicate2.getLiteral()).andStubReturn("abc");
		try {
			EasyMock.expect(kpProperty1.getName()).andStubReturn("field1");
			EasyMock.expect(kpProperty1.getType()).andStubReturn(EdmSimpleTypeKind.Int32.getEdmSimpleTypeInstance());
			EasyMock.expect(kpProperty2.getName()).andStubReturn("field2");
			EasyMock.expect(kpProperty2.getType()).andStubReturn(EdmSimpleTypeKind.String.getEdmSimpleTypeInstance());			
		} catch (EdmException e2) {
			fail("this should not happen");
		}
		EasyMock.expect(keyPredicate1.getProperty()).andStubReturn(kpProperty1);
		EasyMock.expect(keyPredicate2.getProperty()).andStubReturn(kpProperty2);
		EasyMock.replay(kpProperty1,keyPredicate1,kpProperty2,keyPredicate2);
		
		ArrayList<KeyPredicate> keyPredicates = new ArrayList<KeyPredicate>();
		keyPredicates.add(keyPredicate1);
		keyPredicates.add(keyPredicate2);
		String str = null;
		
		try {
			str = ODataExpressionParser.parseKeyPredicates(keyPredicates);
		} catch (ODataJPARuntimeException e) {
			fail("this should not happen");
		}
		
		assertEquals("gwt1.field1 = 1 AND gwt1.field2 = 'abc'", str);
	}
	

}
