package com.sap.core.odata.processor.core.jpa.access.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.easymock.EasyMock;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.expression.BinaryExpression;
import com.sap.core.odata.api.uri.expression.BinaryOperator;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.LiteralExpression;
import com.sap.core.odata.api.uri.expression.MemberExpression;
import com.sap.core.odata.api.uri.expression.PropertyExpression;
import com.sap.core.odata.api.uri.expression.UnaryExpression;
import com.sap.core.odata.api.uri.expression.UnaryOperator;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.core.jpa.common.JPATestConstants;

public class ODataExpressionParserTest {
	
	private final String EXPECTED_STR_1 = "gwt1.SalesOrder = 1234";
	private final String EXPECTED_STR_2 = "gwt1.SalesOrder >= 1234 AND gwt1.SalesABC <> XYZ";
	private final String EXPECTED_STR_3 = "gwt1.SalesOrder >= 1234 OR gwt1.SalesABC <> XYZ";
	private final String EXPECTED_STR_4 = "gwt1.SalesOrder < 1234 AND gwt1.SalesABC <= XYZ";
	private final String EXPECTED_STR_5 = "gwt1.LineItems > 2345 AND gwt1.SalesOrder >= Amazon";
	private final String EXPECTED_STR_6 = "gwt1.Address.city = \'City_3\'";
	private final String EXPECTED_STR_7 = "gwt1.Address.city.area = \'BTM\'";
	private final String EXPECTED_STR_8 = "gwt1.field1 = 1 AND gwt1.field2 = 'abc'";
	private final String EXPECTED_STR_9 = "gwt1.BuyerAddress, gwt1.BuyerName, gwt1.BuyerId";
	private final String EXPECTED_STR_10 = "gwt1.SalesOrder";
	private final String EXPECTED_STR_11 = "NOT(gwt1.deliveryStatus)";
	
	private final String ADDRESS = "Address";
	private final String CITY = "city";
	private final String AREA = "area";
	private final String SALES_ORDER = "SalesOrder";
	private final String SALES_ABC = "SalesABC";
	private final String SAMPLE_DATA_1 = "1234";
	private final String SAMPLE_DATA_2 = "2345";
	private final String SAMPLE_DATA_XYZ = "XYZ";
	private final String SAMPLE_DATA_BTM = "\'BTM\'";
	private final String SAMPLE_DATA_CITY_3 = "\'City_3\'";
	private final String SAMPLE_DATA_LINE_ITEMS = "LineItems";
	private final String SAMPLE_DATA_AMAZON = "Amazon";
	private final String SAMPLE_DATA_FIELD1 = "field1";
	private final String SAMPLE_DATA_FIELD2 = "field2";
	
	private final String TABLE_ALIAS = "gwt1"; //$NON-NLS-1$
	
	
	@Test
	public void testParseWhereExpression() {
		try {
			String parsedStr = JPATestConstants.EMPTY_STRING;
			// Simple Binary query -
			parsedStr = ODataExpressionParser.parseToJPAWhereExpression(
					getBinaryExpressionMockedObj(BinaryOperator.EQ, ExpressionKind.PROPERTY, 
							SALES_ORDER, SAMPLE_DATA_1), TABLE_ALIAS);

			assertEquals(EXPECTED_STR_1, parsedStr);
			// complex query -
			parsedStr = JPATestConstants.EMPTY_STRING;

			CommonExpression exp1 = getBinaryExpressionMockedObj(
					BinaryOperator.GE, ExpressionKind.PROPERTY, SALES_ORDER, SAMPLE_DATA_1);
			CommonExpression exp2 = getBinaryExpressionMockedObj(
					BinaryOperator.NE, ExpressionKind.PROPERTY, SALES_ABC,	SAMPLE_DATA_XYZ);
			parsedStr = ODataExpressionParser.parseToJPAWhereExpression(
								getBinaryExpression(exp1, BinaryOperator.AND, exp2), TABLE_ALIAS);
			assertEquals(EXPECTED_STR_2, parsedStr);
		} catch (EdmException e) {
			fail(JPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ JPATestConstants.EXCEPTION_MSG_PART_2);
		} catch (ODataException e) {
			fail(JPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ JPATestConstants.EXCEPTION_MSG_PART_2);
		}
	}

	@Test
	public void testMoreThanOneBinaryExpression() {
		// complex query -
		String parsedStr = JPATestConstants.EMPTY_STRING;
		CommonExpression exp1 = getBinaryExpressionMockedObj(BinaryOperator.GE,
				ExpressionKind.PROPERTY, SALES_ORDER, SAMPLE_DATA_1);
		CommonExpression exp2 = getBinaryExpressionMockedObj(BinaryOperator.NE,
				ExpressionKind.PROPERTY, SALES_ABC, SAMPLE_DATA_XYZ);
		try {
			parsedStr = ODataExpressionParser.parseToJPAWhereExpression(
					getBinaryExpression(exp1, BinaryOperator.AND, exp2),
					TABLE_ALIAS);
			assertEquals(EXPECTED_STR_2, parsedStr);
			parsedStr = ODataExpressionParser
						.parseToJPAWhereExpression(
								getBinaryExpression(exp1, BinaryOperator.OR, exp2),
								TABLE_ALIAS);
		} catch (ODataException e) {
			fail(JPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()+ JPATestConstants.EXCEPTION_MSG_PART_2);
		}
		assertEquals(EXPECTED_STR_3, parsedStr);
	}

	@Test
	public void testParseFilterExpression() {
		try {
			assertEquals(EXPECTED_STR_10,
					ODataExpressionParser.parseToJPAWhereExpression(
							getFilterExpressionMockedObj(ExpressionKind.PROPERTY,
									SALES_ORDER), TABLE_ALIAS));
		} catch (ODataException e) {
			fail(JPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()+ JPATestConstants.EXCEPTION_MSG_PART_2);
		}
	}

	@Test
	public void testAllBinaryOperators() { // Test for all Binary Operators
		// complex query -
		String parsedStr1 = JPATestConstants.EMPTY_STRING;
		String parsedStr2 = JPATestConstants.EMPTY_STRING;

		CommonExpression exp1 = getBinaryExpressionMockedObj(BinaryOperator.LT,
				ExpressionKind.PROPERTY, SALES_ORDER, SAMPLE_DATA_1);
		CommonExpression exp2 = getBinaryExpressionMockedObj(BinaryOperator.LE,
				ExpressionKind.PROPERTY, SALES_ABC, SAMPLE_DATA_XYZ);

		try {
			parsedStr1 = ODataExpressionParser.parseToJPAWhereExpression(
					(BinaryExpression) getBinaryExpression(exp1,
							BinaryOperator.AND, exp2), TABLE_ALIAS);
			assertEquals(EXPECTED_STR_4, parsedStr1);
	
			CommonExpression exp3 = getBinaryExpressionMockedObj(BinaryOperator.GT,
					ExpressionKind.PROPERTY, SAMPLE_DATA_LINE_ITEMS, SAMPLE_DATA_2);
			CommonExpression exp4 = getBinaryExpressionMockedObj(BinaryOperator.GE,
					ExpressionKind.PROPERTY, SALES_ORDER, SAMPLE_DATA_AMAZON);
	
			parsedStr2 = ODataExpressionParser.parseToJPAWhereExpression(
					getBinaryExpression(exp3, BinaryOperator.AND, exp4),
					TABLE_ALIAS);

		} catch (ODataException e) {
			fail(JPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()+ JPATestConstants.EXCEPTION_MSG_PART_2);
		}
		assertEquals(EXPECTED_STR_5, parsedStr2);
	}

	@Test
	public void testParseMemberExpression() {
		try {
			assertEquals(EXPECTED_STR_6,
					ODataExpressionParser
							.parseToJPAWhereExpression(
									getBinaryExpression(
											getMemberExpressionMockedObj(ADDRESS,
													CITY),
											BinaryOperator.EQ,
											getLiteralExpressionMockedObj(SAMPLE_DATA_CITY_3)),
									TABLE_ALIAS));
			assertEquals(EXPECTED_STR_7,
					ODataExpressionParser
						.parseToJPAWhereExpression(
								getBinaryExpression(
										getMultipleMemberExpressionMockedObj(ADDRESS,CITY,AREA),
										BinaryOperator.EQ,
										getLiteralExpressionMockedObj(SAMPLE_DATA_BTM)),
								TABLE_ALIAS));
		} catch (ODataException e) {
			fail(JPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()+ JPATestConstants.EXCEPTION_MSG_PART_2);
		}
	}
	
	private CommonExpression getMultipleMemberExpressionMockedObj(String string1,String string2, String string3) {
		
		MemberExpression memberExpression = EasyMock.createMock(MemberExpression.class);
		
		EasyMock.expect(memberExpression.getPath()).andStubReturn(getMemberExpressionMockedObj(string1, string2));
		EasyMock.expect(memberExpression.getProperty()).andStubReturn(getPropertyExpressionMockedObj(ExpressionKind.PROPERTY,string3));
		EasyMock.expect(memberExpression.getKind()).andStubReturn(ExpressionKind.MEMBER);
		EasyMock.replay(memberExpression);
		
		return memberExpression;
	}

	@Test
	public void testParseUnaryExpression() {

		UnaryExpression unaryExpression = getUnaryExpressionMockedObj(
				getPropertyExpressionMockedObj(ExpressionKind.PROPERTY,
						"deliveryStatus"),
				com.sap.core.odata.api.uri.expression.UnaryOperator.NOT);
		try {
			assertEquals(EXPECTED_STR_11, ODataExpressionParser.parseToJPAWhereExpression(
					unaryExpression, TABLE_ALIAS));
		} catch (ODataException e) {
			fail(JPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()+ JPATestConstants.EXCEPTION_MSG_PART_2);
		}

	}

	private UnaryExpression getUnaryExpressionMockedObj(
			CommonExpression operand, UnaryOperator unaryOperator) {
		UnaryExpression unaryExpression = EasyMock
				.createMock(UnaryExpression.class);
		EasyMock.expect(unaryExpression.getKind())
				.andStubReturn(ExpressionKind.UNARY);
		EasyMock.expect(unaryExpression.getOperand()).andStubReturn(operand)
				;
		EasyMock.expect(unaryExpression.getOperator()).andStubReturn(unaryOperator)
				;

		EasyMock.replay(unaryExpression);
		return unaryExpression;
	}

	private CommonExpression getMemberExpressionMockedObj(
			String pathUriLiteral, String propertyUriLiteral){
		MemberExpression memberExpression = EasyMock
				.createMock(MemberExpression.class);
		EasyMock.expect(memberExpression.getPath())
				.andStubReturn(
						getPropertyExpressionMockedObj(ExpressionKind.PROPERTY,
								pathUriLiteral));
		EasyMock.expect(memberExpression.getProperty())
				.andStubReturn(
						getPropertyExpressionMockedObj(ExpressionKind.PROPERTY,
								propertyUriLiteral));
		EasyMock.expect(memberExpression.getKind())
				.andStubReturn(ExpressionKind.MEMBER);

		EasyMock.replay(memberExpression);
		return memberExpression;
	}

	private LiteralExpression getLiteralExpressionMockedObj(String uriLiteral) {
		LiteralExpression rightOperandLiteralExpresion = EasyMock
				.createMock(LiteralExpression.class);
		EasyMock.expect(rightOperandLiteralExpresion.getKind())
				.andStubReturn(ExpressionKind.LITERAL);
		EasyMock.expect(rightOperandLiteralExpresion.getUriLiteral())
				.andStubReturn(uriLiteral);// SAMPLE_DATA
		EasyMock.expect(rightOperandLiteralExpresion.getEdmType())
				.andStubReturn(getEdmSimpleTypeMockedObj(uriLiteral));
		EasyMock.replay(rightOperandLiteralExpresion);
		return rightOperandLiteralExpresion;

	}

	private EdmSimpleType getEdmSimpleTypeMockedObj(String value) {
		EdmSimpleType edmSimpleType = EasyMock.createMock(EdmSimpleType.class);
		try {
			EasyMock.expect(edmSimpleType.getName()).andReturn(value);
			EasyMock.expect(edmSimpleType.getKind()).andStubReturn(EdmTypeKind.SIMPLE);
			EasyMock.expect(edmSimpleType.valueOfString(value, EdmLiteralKind.URI,
						getEdmFacetsMockedObj(), null)).andStubReturn(value);
			EasyMock.expect(edmSimpleType.valueOfString(value, EdmLiteralKind.URI, null, null)).andStubReturn(value);
			EasyMock.expect(edmSimpleType.valueToString(value, EdmLiteralKind.DEFAULT,
						getEdmFacetsMockedObj())).andStubReturn(value);
			EasyMock.expect(edmSimpleType.valueToString(value, EdmLiteralKind.DEFAULT, null)).andStubReturn(value);
		} catch (EdmException e) {
			fail(JPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()+ JPATestConstants.EXCEPTION_MSG_PART_2);
		}
		EasyMock.expect(edmSimpleType.getDefaultType()).andStubReturn(null);
		EasyMock.replay(edmSimpleType);
		return edmSimpleType;
	}

	private EdmFacets getEdmFacetsMockedObj() {
		EdmFacets facets = EasyMock.createMock(EdmFacets.class);

		EasyMock.replay(facets);
		return facets;
	}

	private PropertyExpression getPropertyExpressionMockedObj(
			ExpressionKind expKind, String propertyName) {
		PropertyExpression leftOperandPropertyExpresion = EasyMock.createMock(PropertyExpression.class);
		EasyMock.expect(leftOperandPropertyExpresion.getKind()).andStubReturn(ExpressionKind.PROPERTY);
		EasyMock.expect(leftOperandPropertyExpresion.getPropertyName()).andStubReturn(propertyName);
		EasyMock.expect(leftOperandPropertyExpresion.getEdmProperty()).andStubReturn(getEdmTypedMockedObj(propertyName));
		EasyMock.replay(leftOperandPropertyExpresion);
		return leftOperandPropertyExpresion;
	}

	private EdmTyped getEdmTypedMockedObj(String propertyName) {
		EdmProperty mockedEdmProperty = EasyMock.createMock(EdmProperty.class);
		try {
			EasyMock.expect(mockedEdmProperty.getMapping()).andStubReturn(getEdmMappingMockedObj(propertyName));
			EasyMock.replay(mockedEdmProperty);
		} catch (EdmException e) {
			fail(JPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()+ JPATestConstants.EXCEPTION_MSG_PART_2);
		}
		return mockedEdmProperty;
	}

	private EdmMapping getEdmMappingMockedObj(String propertyName) {
		EdmMapping mockedEdmMapping = EasyMock.createMock(EdmMapping.class);
		EasyMock.expect(mockedEdmMapping.getInternalName())
				.andStubReturn(propertyName);
		EasyMock.replay(mockedEdmMapping);
		return mockedEdmMapping;
	}

	private BinaryExpression getBinaryExpressionMockedObj(
			BinaryOperator operator, ExpressionKind leftOperandExpKind,
			String propertyName, String literalStr) {
		BinaryExpression binaryExpression = EasyMock
				.createMock(BinaryExpression.class);
		EasyMock.expect(binaryExpression.getKind())
				.andStubReturn(ExpressionKind.BINARY);
		EasyMock.expect(binaryExpression.getLeftOperand())
				.andStubReturn(
						getPropertyExpressionMockedObj(leftOperandExpKind,
								propertyName));
		EasyMock.expect(binaryExpression.getOperator()).andStubReturn(operator);
		EasyMock.expect(binaryExpression.getRightOperand())
				.andStubReturn(getLiteralExpressionMockedObj(literalStr));

		EasyMock.replay(binaryExpression);
		return binaryExpression;
	}

	private FilterExpression getFilterExpressionMockedObj(
			ExpressionKind leftOperandExpKind, String propertyName) {
		FilterExpression filterExpression = EasyMock
				.createMock(FilterExpression.class);
		EasyMock.expect(filterExpression.getKind())
				.andStubReturn(ExpressionKind.FILTER);
		EasyMock.expect(filterExpression.getExpression())
				.andStubReturn(
						getPropertyExpressionMockedObj(leftOperandExpKind,
								propertyName));

		EasyMock.replay(filterExpression);
		return filterExpression;
	}

	private CommonExpression getBinaryExpression(
			final CommonExpression leftOperand, final BinaryOperator operator,
			final CommonExpression rightOperand) {
		BinaryExpression binaryExpression = EasyMock
				.createMock(BinaryExpression.class);
		EasyMock.expect(binaryExpression.getKind())
				.andStubReturn(ExpressionKind.BINARY);
		EasyMock.expect(binaryExpression.getLeftOperand())
				.andStubReturn(leftOperand);
		EasyMock.expect(binaryExpression.getRightOperand())
				.andStubReturn(rightOperand);
		EasyMock.expect(binaryExpression.getOperator()).andStubReturn(operator);

		EasyMock.replay(binaryExpression);
		return binaryExpression;
	}

	@Test
	public void testParseKeyPredicates() {
		// Setting up the expected value
		KeyPredicate keyPredicate1 = EasyMock.createMock(KeyPredicate.class);
		EdmProperty kpProperty1 = EasyMock.createMock(EdmProperty.class);
		EasyMock.expect(keyPredicate1.getLiteral()).andStubReturn("1");
		KeyPredicate keyPredicate2 = EasyMock.createMock(KeyPredicate.class);
		EdmProperty kpProperty2 = EasyMock.createMock(EdmProperty.class);
		EasyMock.expect(keyPredicate2.getLiteral()).andStubReturn("abc");
		EdmMapping edmMapping = EasyMock.createMock(EdmMapping.class);
		try {
			EasyMock.expect(kpProperty1.getName()).andStubReturn(SAMPLE_DATA_FIELD1);
			EasyMock.expect(kpProperty1.getType()).andStubReturn(
					EdmSimpleTypeKind.Int32.getEdmSimpleTypeInstance());
			EasyMock.expect(kpProperty2.getName()).andStubReturn(SAMPLE_DATA_FIELD2);
			EasyMock.expect(kpProperty2.getType()).andStubReturn(
					EdmSimpleTypeKind.String.getEdmSimpleTypeInstance());
			EasyMock.expect(keyPredicate1.getProperty()).andStubReturn(kpProperty1);
			EasyMock.expect(kpProperty1.getMapping()).andReturn(edmMapping);
			EasyMock.expect(edmMapping.getInternalName()).andReturn(SAMPLE_DATA_FIELD1);
			EasyMock.expect(keyPredicate2.getProperty()).andStubReturn(kpProperty2);
			EasyMock.expect(kpProperty2.getMapping()).andReturn(edmMapping);
		} catch (EdmException e) {
			fail(JPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()+ JPATestConstants.EXCEPTION_MSG_PART_2);
		}
		EasyMock.expect(edmMapping.getInternalName()).andReturn(SAMPLE_DATA_FIELD2);
		EasyMock.replay(edmMapping);
		EasyMock.replay(kpProperty1, keyPredicate1, kpProperty2, keyPredicate2);

		ArrayList<KeyPredicate> keyPredicates = new ArrayList<KeyPredicate>();
		keyPredicates.add(keyPredicate1);
		keyPredicates.add(keyPredicate2);
		String str = null;

		try {
			str = ODataExpressionParser.parseKeyPredicates(keyPredicates,
					TABLE_ALIAS);
		} catch (ODataJPARuntimeException e) {
			fail(JPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()+ JPATestConstants.EXCEPTION_MSG_PART_2);
		}

		assertEquals(EXPECTED_STR_8, str);
	}

	@Test
	public void testParseToJPASelectExpression() {

		ArrayList<String> selectedFields = new ArrayList<String>();
		selectedFields.add("BuyerAddress");
		selectedFields.add("BuyerName");
		selectedFields.add("BuyerId");

		assertEquals(EXPECTED_STR_9,
				ODataExpressionParser.parseToJPASelectExpression(TABLE_ALIAS,
						selectedFields));
		assertEquals(TABLE_ALIAS, ODataExpressionParser.parseToJPASelectExpression(
				TABLE_ALIAS, null));

		selectedFields.clear();
		assertEquals(TABLE_ALIAS, ODataExpressionParser.parseToJPASelectExpression(
				TABLE_ALIAS, selectedFields));
	}
}
