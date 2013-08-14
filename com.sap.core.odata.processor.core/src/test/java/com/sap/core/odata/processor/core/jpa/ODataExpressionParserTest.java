package com.sap.core.odata.processor.core.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

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
import com.sap.core.odata.api.uri.expression.MethodExpression;
import com.sap.core.odata.api.uri.expression.MethodOperator;
import com.sap.core.odata.api.uri.expression.PropertyExpression;
import com.sap.core.odata.api.uri.expression.UnaryExpression;
import com.sap.core.odata.api.uri.expression.UnaryOperator;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.core.jpa.ODataExpressionParser;
import com.sap.core.odata.processor.core.jpa.common.ODataJPATestConstants;

public class ODataExpressionParserTest {

  private static final String EXPECTED_STR_1 = "gwt1.SalesOrder = 1234";
  private static final String EXPECTED_STR_2 = "gwt1.SalesOrder >= 1234 AND gwt1.SalesABC <> XYZ";
  private static final String EXPECTED_STR_3 = "gwt1.SalesOrder >= 1234 OR gwt1.SalesABC <> XYZ";
  private static final String EXPECTED_STR_4 = "gwt1.SalesOrder < 1234 AND gwt1.SalesABC <= XYZ";
  private static final String EXPECTED_STR_5 = "gwt1.LineItems > 2345 AND gwt1.SalesOrder >= Amazon";
  private static final String EXPECTED_STR_6 = "gwt1.Address.city = \'City_3\'";
  private static final String EXPECTED_STR_7 = "gwt1.Address.city.area = \'BTM\'";
  private static final String EXPECTED_STR_8 = "gwt1.field1 = 1 AND gwt1.field2 = 'abc'";
  private static final String EXPECTED_STR_9 = "gwt1.BuyerAddress, gwt1.BuyerName, gwt1.BuyerId";
  private static final String EXPECTED_STR_10 = "gwt1.SalesOrder";
  private static final String EXPECTED_STR_11 = "NOT(gwt1.deliveryStatus)";
  private static final String EXPECTED_STR_12 = "(CASE WHEN (gwt1.currencyCode LIKE '%Ru%') THEN TRUE ELSE FALSE END) = true";
  private static final String EXPECTED_STR_13 = "SUBSTRING(gwt1.currencyCode, 1 + 1 , 2) = 'NR'";
  private static final String EXPECTED_STR_14 = "LOWER(gwt1.currencyCode) = 'inr rupees'";
  private static final String EXPECTED_STR_15 = "(CASE WHEN (LOWER(gwt1.currencyCode) LIKE '%nr rupees%') THEN TRUE ELSE FALSE END) = true";
  private static final String EXPECTED_STR_16 = "(CASE WHEN (gwt1.currencyCode LIKE '%INR%') THEN TRUE ELSE FALSE END) = true";
  private static final String EXPECTED_STR_17 = "(CASE WHEN (LOWER(gwt1.currencyCode) LIKE '%nr rupees%') THEN TRUE ELSE FALSE END) = true";

  private static final String ADDRESS = "Address";
  private static final String CITY = "city";
  private static final String AREA = "area";
  private static final String SALES_ORDER = "SalesOrder";
  private static final String SALES_ABC = "SalesABC";
  private static final String SAMPLE_DATA_1 = "1234";
  private static final String SAMPLE_DATA_2 = "2345";
  private static final String SAMPLE_DATA_XYZ = "XYZ";
  private static final String SAMPLE_DATA_BTM = "\'BTM\'";
  private static final String SAMPLE_DATA_CITY_3 = "\'City_3\'";
  private static final String SAMPLE_DATA_LINE_ITEMS = "LineItems";
  private static final String SAMPLE_DATA_AMAZON = "Amazon";
  private static final String SAMPLE_DATA_FIELD1 = "field1";
  private static final String SAMPLE_DATA_FIELD2 = "field2";

  private static final String TABLE_ALIAS = "gwt1"; //$NON-NLS-1$

  @Test
  public void testParseWhereExpression() {
    try {
      String parsedStr = ODataJPATestConstants.EMPTY_STRING;
      // Simple Binary query -
      parsedStr = ODataExpressionParser.parseToJPAWhereExpression(
          getBinaryExpressionMockedObj(BinaryOperator.EQ, ExpressionKind.PROPERTY,
              SALES_ORDER, SAMPLE_DATA_1), TABLE_ALIAS);

      assertEquals(EXPECTED_STR_1, parsedStr);
      // complex query -
      parsedStr = ODataJPATestConstants.EMPTY_STRING;

      CommonExpression exp1 = getBinaryExpressionMockedObj(
          BinaryOperator.GE, ExpressionKind.PROPERTY, SALES_ORDER, SAMPLE_DATA_1);
      CommonExpression exp2 = getBinaryExpressionMockedObj(
          BinaryOperator.NE, ExpressionKind.PROPERTY, SALES_ABC, SAMPLE_DATA_XYZ);
      parsedStr = ODataExpressionParser.parseToJPAWhereExpression(
          getBinaryExpression(exp1, BinaryOperator.AND, exp2), TABLE_ALIAS);
      assertEquals(EXPECTED_STR_2, parsedStr);
    } catch (EdmException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (ODataException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
  }

  @Test
  public void testMoreThanOneBinaryExpression() {
    // complex query -
    String parsedStr = ODataJPATestConstants.EMPTY_STRING;
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
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage() + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
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
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage() + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
  }

  @Test
  public void testAllBinaryOperators() { // Test for all Binary Operators
    // complex query -
    String parsedStr1 = ODataJPATestConstants.EMPTY_STRING;
    String parsedStr2 = ODataJPATestConstants.EMPTY_STRING;

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
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage() + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
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
                      getMultipleMemberExpressionMockedObj(ADDRESS, CITY, AREA),
                      BinaryOperator.EQ,
                      getLiteralExpressionMockedObj(SAMPLE_DATA_BTM)),
                  TABLE_ALIAS));
    } catch (ODataException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage() + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
  }

  @Test
  public void testParseMethodExpression() {
    try {
      assertEquals(EXPECTED_STR_12,
          ODataExpressionParser
              .parseToJPAWhereExpression(
                  getBinaryExpression(
                      getMethodExpressionMockedObj(MethodOperator.SUBSTRINGOF, "'Ru'", "currencyCode", null, 2),
                      BinaryOperator.EQ,
                      getLiteralExpressionMockedObj("true")),
                  TABLE_ALIAS));
      assertEquals(EXPECTED_STR_13,
          ODataExpressionParser
              .parseToJPAWhereExpression(
                  getBinaryExpression(
                      getMethodExpressionMockedObj(MethodOperator.SUBSTRING, "currencyCode", "1", "2", 3),
                      BinaryOperator.EQ,
                      getLiteralExpressionMockedObj("'NR'")),
                  TABLE_ALIAS));
      assertEquals(EXPECTED_STR_14,
          ODataExpressionParser
              .parseToJPAWhereExpression(
                  getBinaryExpression(
                      getMethodExpressionMockedObj(MethodOperator.TOLOWER, "currencyCode", null, null, 1),
                      BinaryOperator.EQ,
                      getLiteralExpressionMockedObj("'inr rupees'")),
                  TABLE_ALIAS));
      assertEquals(EXPECTED_STR_15,
          ODataExpressionParser
              .parseToJPAWhereExpression(
                  getBinaryExpression(
                      getMultipleMethodExpressionMockedObj(MethodOperator.SUBSTRINGOF, "'nr rupees'", MethodOperator.TOLOWER, "currencyCode", 2, 1),
                      BinaryOperator.EQ,
                      getLiteralExpressionMockedObj("true")),
                  TABLE_ALIAS));
      assertEquals(EXPECTED_STR_16,
          ODataExpressionParser
              .parseToJPAWhereExpression(getFilterExpressionForFunctionsMockedObj(MethodOperator.SUBSTRINGOF, "'INR'", null, "currencyCode", 2, null)
                  /*getBinaryExpression(
                  		getMemberExpressionMockedObj(ADDRESS,
                  				CITY),
                  		BinaryOperator.EQ,
                  		getLiteralExpressionMockedObj(SAMPLE_DATA_CITY_3))*/,
                  TABLE_ALIAS));
      assertEquals(EXPECTED_STR_17,
          ODataExpressionParser
              .parseToJPAWhereExpression(getFilterExpressionForFunctionsMockedObj(MethodOperator.SUBSTRINGOF, "'nr rupees'", MethodOperator.TOLOWER, "currencyCode", 2, 1)
                  /*getBinaryExpression(
                  		getMemberExpressionMockedObj(ADDRESS,
                  				CITY),
                  		BinaryOperator.EQ,
                  		getLiteralExpressionMockedObj(SAMPLE_DATA_CITY_3))*/,
                  TABLE_ALIAS));

    } catch (ODataException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage() + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
  }

  private CommonExpression getMethodExpressionMockedObj(final MethodOperator methodOperator, final String firstName, final String secondName, final String thirdName, final Integer parameterCount) {

    List<CommonExpression> parameters = new ArrayList<CommonExpression>();

    if (methodOperator == MethodOperator.SUBSTRINGOF) {
      parameters.add(getLiteralExpressionMockedObj(firstName));
      parameters.add(getPropertyExpressionMockedObj(ExpressionKind.PROPERTY, secondName));
    }

    else if (methodOperator == MethodOperator.SUBSTRING) {
      parameters.add(getPropertyExpressionMockedObj(ExpressionKind.PROPERTY, firstName));
      parameters.add(getLiteralExpressionMockedObj(secondName));
      parameters.add(getLiteralExpressionMockedObj(thirdName));
    }
    else if (methodOperator == MethodOperator.TOLOWER) {
      parameters.add(getPropertyExpressionMockedObj(ExpressionKind.PROPERTY, firstName));
    }

    MethodExpression methodExpression = EasyMock.createMock(MethodExpression.class);

    EasyMock.expect(methodExpression.getKind()).andStubReturn(ExpressionKind.METHOD);
    EasyMock.expect(methodExpression.getMethod()).andStubReturn(methodOperator);
    EasyMock.expect(methodExpression.getParameterCount()).andStubReturn(parameterCount);
    EasyMock.expect(methodExpression.getParameters()).andStubReturn(parameters);
    EasyMock.replay(methodExpression);

    return methodExpression;
  }

  private CommonExpression getMultipleMethodExpressionMockedObj(final MethodOperator methodOperator1, final String firstName, final MethodOperator methodOperator2, final String secondName, final Integer parameterCount1, final Integer parameterCount2) {

    //complex query
    List<CommonExpression> parameters = new ArrayList<CommonExpression>();

    parameters.add(getLiteralExpressionMockedObj(firstName));
    parameters.add(getMethodExpressionMockedObj(methodOperator2, secondName, null, null, 1));

    MethodExpression methodExpression = EasyMock.createMock(MethodExpression.class);

    EasyMock.expect(methodExpression.getKind()).andStubReturn(ExpressionKind.METHOD);
    EasyMock.expect(methodExpression.getMethod()).andStubReturn(methodOperator1);
    EasyMock.expect(methodExpression.getParameterCount()).andStubReturn(parameterCount1);
    EasyMock.expect(methodExpression.getParameters()).andStubReturn(parameters);
    EasyMock.replay(methodExpression);

    return methodExpression;
  }

  private CommonExpression getMultipleMemberExpressionMockedObj(final String string1, final String string2, final String string3) {

    MemberExpression memberExpression = EasyMock.createMock(MemberExpression.class);

    EasyMock.expect(memberExpression.getPath()).andStubReturn(getMemberExpressionMockedObj(string1, string2));
    EasyMock.expect(memberExpression.getProperty()).andStubReturn(getPropertyExpressionMockedObj(ExpressionKind.PROPERTY, string3));
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
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage() + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }

  }

  private UnaryExpression getUnaryExpressionMockedObj(
      final CommonExpression operand, final UnaryOperator unaryOperator) {
    UnaryExpression unaryExpression = EasyMock
        .createMock(UnaryExpression.class);
    EasyMock.expect(unaryExpression.getKind())
        .andStubReturn(ExpressionKind.UNARY);
    EasyMock.expect(unaryExpression.getOperand()).andStubReturn(operand);
    EasyMock.expect(unaryExpression.getOperator()).andStubReturn(unaryOperator);

    EasyMock.replay(unaryExpression);
    return unaryExpression;
  }

  private CommonExpression getMemberExpressionMockedObj(
      final String pathUriLiteral, final String propertyUriLiteral) {
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

  private LiteralExpression getLiteralExpressionMockedObj(final String uriLiteral) {
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

  private EdmSimpleType getEdmSimpleTypeMockedObj(final String value) {
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
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage() + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
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
      final ExpressionKind expKind, final String propertyName) {
    PropertyExpression leftOperandPropertyExpresion = EasyMock.createMock(PropertyExpression.class);
    EasyMock.expect(leftOperandPropertyExpresion.getKind()).andStubReturn(ExpressionKind.PROPERTY);
    EasyMock.expect(leftOperandPropertyExpresion.getPropertyName()).andStubReturn(propertyName);
    EasyMock.expect(leftOperandPropertyExpresion.getEdmProperty()).andStubReturn(getEdmTypedMockedObj(propertyName));
    EasyMock.replay(leftOperandPropertyExpresion);
    return leftOperandPropertyExpresion;
  }

  private EdmTyped getEdmTypedMockedObj(final String propertyName) {
    EdmProperty mockedEdmProperty = EasyMock.createMock(EdmProperty.class);
    try {
      EasyMock.expect(mockedEdmProperty.getMapping()).andStubReturn(getEdmMappingMockedObj(propertyName));
      EasyMock.replay(mockedEdmProperty);
    } catch (EdmException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage() + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    return mockedEdmProperty;
  }

  private EdmMapping getEdmMappingMockedObj(final String propertyName) {
    EdmMapping mockedEdmMapping = EasyMock.createMock(EdmMapping.class);
    EasyMock.expect(mockedEdmMapping.getInternalName())
        .andStubReturn(propertyName);
    EasyMock.replay(mockedEdmMapping);
    return mockedEdmMapping;
  }

  private BinaryExpression getBinaryExpressionMockedObj(
      final BinaryOperator operator, final ExpressionKind leftOperandExpKind,
      final String propertyName, final String literalStr) {
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
      final ExpressionKind leftOperandExpKind, final String propertyName) {
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

  private FilterExpression getFilterExpressionForFunctionsMockedObj(
      final MethodOperator methodOperator1, final String firstName, final MethodOperator methodOperator2, final String secondName, final Integer parameterCount1, final Integer parameterCount2) {
    //default value handling of SUBSTRINGOF
    FilterExpression filterExpression = EasyMock
        .createMock(FilterExpression.class);
    EasyMock.expect(filterExpression.getKind())
        .andStubReturn(ExpressionKind.FILTER);
    if ((methodOperator2 != null) && (parameterCount2 != null)) {
      EasyMock.expect(filterExpression.getExpression())
          .andStubReturn(
              getMultipleMethodExpressionMockedObj(methodOperator1, firstName, methodOperator2, secondName, parameterCount1, parameterCount2));
    }
    else {
      EasyMock.expect(filterExpression.getExpression())
          .andStubReturn(
              getMethodExpressionMockedObj(methodOperator1, firstName, secondName, null, parameterCount1));
    }

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
      EasyMock.expect(kpProperty1.getMapping()).andStubReturn(edmMapping);
      EasyMock.expect(edmMapping.getInternalName()).andReturn(SAMPLE_DATA_FIELD1);
      EasyMock.expect(keyPredicate2.getProperty()).andStubReturn(kpProperty2);
      EasyMock.expect(kpProperty2.getMapping()).andStubReturn(edmMapping);
    } catch (EdmException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage() + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
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
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage() + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
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
