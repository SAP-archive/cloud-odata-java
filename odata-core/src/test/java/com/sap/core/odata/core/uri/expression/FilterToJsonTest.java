package com.sap.core.odata.core.uri.expression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmLiteral;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.uri.UriParser;
import com.sap.core.odata.api.uri.expression.BinaryExpression;
import com.sap.core.odata.api.uri.expression.BinaryOperator;
import com.sap.core.odata.api.uri.expression.ExceptionVisitExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.ExpressionVisitor;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.LiteralExpression;
import com.sap.core.odata.api.uri.expression.MemberExpression;
import com.sap.core.odata.api.uri.expression.MethodExpression;
import com.sap.core.odata.api.uri.expression.MethodOperator;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.api.uri.expression.OrderExpression;
import com.sap.core.odata.api.uri.expression.PropertyExpression;
import com.sap.core.odata.api.uri.expression.SortOrder;
import com.sap.core.odata.api.uri.expression.UnaryExpression;
import com.sap.core.odata.api.uri.expression.UnaryOperator;

public class FilterToJsonTest {

  private static final String PARAMETER = "parameter";
  private static final String NODETYPE = "nodeType";
  private static final String OPERATOR = "operator";
  private static final String LEFT = "left";
  private static final String RIGHT = "right";
  private static final String TYPE = "type";
  private static final String VALUE = "value";
  private static final Object OPERAND = "operand";
  private static final Object SOURCE = "source";
  private static final Object PATH = "path";

  @SuppressWarnings("unchecked")
  @Test
  public void testToJsonBinaryProperty() throws Exception {
    FilterExpression expression = UriParser.parseFilter(null, null, "a eq b");
    String jsonString = new FilterToJsonConverter().toJson(expression);
    Gson gsonConverter = new Gson();

    StringMap<Object> jsonMap = gsonConverter.fromJson(jsonString, StringMap.class);
    checkBinary(jsonMap, "eq", null);

    StringMap<Object> left = (StringMap<Object>) jsonMap.get(LEFT);
    checkProperty(left, null, "a");

    StringMap<Object> right = (StringMap<Object>) jsonMap.get(RIGHT);
    checkProperty(right, null, "b");
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testToJsonBinaryLiteral() throws Exception {
    FilterExpression expression = UriParser.parseFilter(null, null, "'a' eq 'b'");
    String jsonString = new FilterToJsonConverter().toJson(expression);
    Gson gsonConverter = new Gson();

    StringMap<Object> jsonMap = gsonConverter.fromJson(jsonString, StringMap.class);
    checkBinary(jsonMap, "eq", "Edm.Boolean");

    StringMap<Object> left = (StringMap<Object>) jsonMap.get(LEFT);
    checkLiteral(left, "Edm.String", "a");

    StringMap<Object> right = (StringMap<Object>) jsonMap.get(RIGHT);
    checkLiteral(right, "Edm.String", "b");
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testToJsonBinaryAdd() throws Exception {
    FilterExpression expression = UriParser.parseFilter(null, null, "1d add 2d add 3d add 4d");
    String jsonString = new FilterToJsonConverter().toJson(expression);
    Gson gsonConverter = new Gson();

    StringMap<Object> jsonMap = gsonConverter.fromJson(jsonString, StringMap.class);
    checkBinary(jsonMap, "add", "Edm.Double");

    StringMap<Object> left1 = (StringMap<Object>) jsonMap.get(LEFT);
    checkBinary(left1, "add", "Edm.Double");

    StringMap<Object> left2 = (StringMap<Object>) left1.get(LEFT);
    checkBinary(left2, "add", "Edm.Double");

    StringMap<Object> literal1 = (StringMap<Object>) left2.get(LEFT);
    checkLiteral(literal1, "Edm.Double", new Double(1));

    StringMap<Object> literal2 = (StringMap<Object>) left2.get(RIGHT);
    checkLiteral(literal2, "Edm.Double", new Double(2));

    StringMap<Object> literal3 = (StringMap<Object>) left1.get(RIGHT);
    checkLiteral(literal3, "Edm.Double", new Double(3));

    StringMap<Object> right1 = (StringMap<Object>) jsonMap.get(RIGHT);
    checkLiteral(right1, "Edm.Double", new Double(4));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testToJsonMethod() throws Exception {
    FilterExpression expression = UriParser.parseFilter(null, null, "concat('aa','b')");
    String jsonString = new FilterToJsonConverter().toJson(expression);
    Gson gsonConverter = new Gson();

    StringMap<Object> jsonMap = gsonConverter.fromJson(jsonString, StringMap.class);
    checkMethod(jsonMap, MethodOperator.CONCAT, "Edm.String");

    List<Object> parameter = (List<Object>) jsonMap.get(PARAMETER);
    checkLiteral((StringMap<Object>) parameter.get(0), "Edm.String", "aa");
    checkLiteral((StringMap<Object>) parameter.get(1), "Edm.String", "b");
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testToJsonUnary() throws Exception {
    FilterExpression expression = UriParser.parseFilter(null, null, "not 'a'");
    String jsonString = new FilterToJsonConverter().toJson(expression);
    Gson gsonConverter = new Gson();

    StringMap<Object> jsonMap = gsonConverter.fromJson(jsonString, StringMap.class);
    checkUnary(jsonMap, UnaryOperator.NOT, null);

    StringMap<Object> operand = (StringMap<Object>) jsonMap.get(OPERAND);
    checkLiteral(operand, "Edm.String", "a");
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testToJsonMember() throws Exception {
    FilterExpression expression = UriParser.parseFilter(null, null, "Location/Country");
    String jsonString = new FilterToJsonConverter().toJson(expression);
    Gson gsonConverter = new Gson();

    StringMap<Object> jsonMap = gsonConverter.fromJson(jsonString, StringMap.class);
    checkMember(jsonMap, null);

    StringMap<Object> source = (StringMap<Object>) jsonMap.get(SOURCE);
    checkProperty(source, null, "Location");

    StringMap<Object> path = (StringMap<Object>) jsonMap.get(PATH);
    checkProperty(path, null, "Country");
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testToJsonMember2() throws Exception {
    FilterExpression expression = UriParser.parseFilter(null, null, "Location/Country/PostalCode");
    String jsonString = new FilterToJsonConverter().toJson(expression);
    Gson gsonConverter = new Gson();

    StringMap<Object> jsonMap = gsonConverter.fromJson(jsonString, StringMap.class);
    checkMember(jsonMap, null);

    StringMap<Object> source1 = (StringMap<Object>) jsonMap.get(SOURCE);
    checkMember(source1, null);

    StringMap<Object> source2 = (StringMap<Object>) source1.get(SOURCE);
    checkProperty(source2, null, "Location");

    StringMap<Object> path1 = (StringMap<Object>) source1.get(PATH);
    checkProperty(path1, null, "Country");

    StringMap<Object> path = (StringMap<Object>) jsonMap.get(PATH);
    checkProperty(path, null, "PostalCode");
  }

  private void checkUnary(final StringMap<Object> unary, final UnaryOperator expectedOperator, final String expectedType) {
    String nodeType = (String) unary.get(NODETYPE);
    assertEquals(ExpressionKind.UNARY.toString(), nodeType);

    String operator = (String) unary.get(OPERATOR);
    assertEquals(expectedOperator.toString(), operator);

    String type = (String) unary.get(TYPE);
    assertEquals(expectedType, type);

    Object operand = unary.get(OPERAND);
    assertNotNull(operand);
  }

  private void checkMember(final StringMap<Object> member, final String expectedType) {
    String nodeType = (String) member.get(NODETYPE);
    assertEquals(ExpressionKind.MEMBER.toString(), nodeType);

    String type = (String) member.get(TYPE);
    assertEquals(expectedType, type);

    Object source = member.get(SOURCE);
    assertNotNull(source);

    Object path = member.get(PATH);
    assertNotNull(path);
  }

  private void checkMethod(final StringMap<Object> method, final MethodOperator expectedOperator, final String expectedType) {
    String nodeType = (String) method.get(NODETYPE);
    assertEquals(ExpressionKind.METHOD.toString(), nodeType);

    String operator = (String) method.get(OPERATOR);
    assertEquals(expectedOperator.toString(), operator);

    String type = (String) method.get(TYPE);
    assertEquals(expectedType, type);

    assertNotNull(method.get(PARAMETER));

  }

  private void checkProperty(final StringMap<Object> property, final String expectedType, final Object expectedValue) {
    String nodeType = (String) property.get(NODETYPE);
    assertEquals(ExpressionKind.PROPERTY.toString(), nodeType);

    String type = (String) property.get(TYPE);
    assertEquals(expectedType, type);

    Object value = property.get(VALUE);
    assertEquals(expectedValue, value);
  }

  private void checkLiteral(final StringMap<Object> literal, final String expectedType, final Object expectedValue) {
    String nodeType = (String) literal.get(NODETYPE);
    assertEquals(ExpressionKind.LITERAL.toString(), nodeType);

    String type = (String) literal.get(TYPE);
    assertEquals(expectedType, type);

    Object value = literal.get(VALUE);
    assertEquals(expectedValue, value);
  }

  private void checkBinary(final StringMap<Object> binary, final String expectedOperator, final String expectedType) throws Exception {
    String nodeType = (String) binary.get(NODETYPE);
    assertEquals(ExpressionKind.BINARY.toString(), nodeType);

    String operator = (String) binary.get(OPERATOR);
    assertEquals(expectedOperator, operator);

    String type = (String) binary.get(TYPE);
    assertEquals(expectedType, type);

    Object left = binary.get(LEFT);
    assertNotNull(left);

    Object right = binary.get(RIGHT);
    assertNotNull(right);
  }

  public class FilterToJsonConverter {

    String toJson(final FilterExpression expression) throws ExceptionVisitExpression, ODataApplicationException {
      ExpressionVisitor visitor = new JsonVisitor();

      return (String) expression.accept(visitor);
    }
  }

  public class JsonVisitor implements ExpressionVisitor {

    @Override
    public Object visitFilterExpression(final FilterExpression filterExpression, final String expressionString, final Object expression) {
      return expression;
    }

    @Override
    public Object visitBinary(final BinaryExpression binaryExpression, final BinaryOperator operator, final Object leftSide, final Object rightSide) {
      String binaryType = "null";

      try {
        if (binaryExpression.getEdmType() != null) {
          binaryType = binaryExpression.getEdmType().getNamespace() + "." + binaryExpression.getEdmType().getName();
        }
      } catch (EdmException e) {
        binaryType = "EdmException occoured: " + e.getMessage();
      }

      return "{ nodeType: " + binaryExpression.getKind() + ", operator: " + operator.toUriLiteral() + ", type: " + binaryType + ", left: " + (String) leftSide + ", right: " + rightSide.toString() + "}";
    }

    @Override
    public Object visitOrderByExpression(final OrderByExpression orderByExpression, final String expressionString, final List<Object> orders) {
      String expression = "";

      for (Object order : orders) {
        expression = expression + " " + (String) order;
      }

      return expression;
    }

    @Override
    public Object visitOrder(final OrderExpression orderExpression, final Object filterResult, final SortOrder sortOrder) {
      return filterResult;
    }

    @Override
    public Object visitLiteral(final LiteralExpression literal, final EdmLiteral edmLiteral) {
      String literalType = "null";

      try {
        literalType = edmLiteral.getType().getNamespace() + "." + edmLiteral.getType().getName();
      } catch (EdmException e) {
        literalType = "EdmException occoured: " + e.getMessage();
      }

      return "{ nodeType: " + literal.getKind() + ", type: " + literalType + ", value: " + edmLiteral.getLiteral() + "}";
    }

    @Override
    public Object visitMethod(final MethodExpression methodExpression, final MethodOperator method, final List<Object> parameters) {

      String methodType = "null";

      try {
        if (methodExpression.getEdmType() != null) {
          methodType = methodExpression.getEdmType().getNamespace() + "." + methodExpression.getEdmType().getName();
        }
      } catch (EdmException e) {
        methodType = "EdmException occoured: " + e.getMessage();
      }

      String parametersString = "";

      for (Object parameter : parameters) {
        parametersString = parametersString + parameter + ",";
      }

      parametersString = parametersString.substring(0, parametersString.length() - 1);

      return "{ nodeType: " + methodExpression.getKind() + ", operator: " + method.toUriLiteral() + ", type: " + methodType + ", parameter: [" + parametersString + "]}";
    }

    @Override
    public Object visitMember(final MemberExpression memberExpression, final Object path, final Object property) {
      String memberType = "null";
      try {
        if (memberExpression.getEdmType() != null) {
          memberType = memberExpression.getEdmType().getNamespace() + "." + memberExpression.getEdmType().getName();
        }
      } catch (EdmException e) {
        memberType = "EdmException occoured: " + e.getMessage();
      }

      return "{ nodeType: " + memberExpression.getKind() + ", type: " + memberType + ", source: " + path + ", path: " + property + "}";
    }

    @Override
    public Object visitProperty(final PropertyExpression propertyExpression, final String uriLiteral, final EdmTyped edmProperty) {
      String propertyType = "null";

      if (edmProperty != null) {
        try {
          propertyType = edmProperty.getType().getNamespace() + "." + edmProperty.getType().getName();
        } catch (EdmException e) {
          propertyType = "EdmException occoured: " + e.getMessage();
        }
      }

      return "{ nodeType: " + propertyExpression.getKind() + ", type: " + propertyType + ", value: " + uriLiteral + "}";
    }

    @Override
    public Object visitUnary(final UnaryExpression unaryExpression, final UnaryOperator operator, final Object operand) {
      String unaryType = "null";

      try {
        if (unaryExpression.getEdmType() != null) {
          unaryType = unaryExpression.getEdmType().getNamespace() + "." + unaryExpression.getEdmType().getName();
        }
      } catch (EdmException e) {
        unaryType = "EdmException occoured: " + e.getMessage();
      }

      return "{ nodeType: " + unaryExpression.getKind() + ", operator: " + operator.toUriLiteral() + ", type: " + unaryType + ", operand: " + operand + "}";
    }

  }

}
