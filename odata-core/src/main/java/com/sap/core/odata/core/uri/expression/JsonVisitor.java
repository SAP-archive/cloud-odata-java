package com.sap.core.odata.core.uri.expression;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmLiteral;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.uri.expression.BinaryExpression;
import com.sap.core.odata.api.uri.expression.BinaryOperator;
import com.sap.core.odata.api.uri.expression.CommonExpression;
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
import com.sap.core.odata.core.ep.util.JsonStreamWriter;

/**
 * @author SAP AG
 */
public class JsonVisitor implements ExpressionVisitor {

  @Override
  public Object visitFilterExpression(final FilterExpression filterExpression, final String expressionString, final Object expression) {
    return expression;
  }

  @Override
  public Object visitBinary(final BinaryExpression binaryExpression, final BinaryOperator operator, final Object leftSide, final Object rightSide) {
    try {
      StringWriter writer = new StringWriter();
      JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);
      jsonStreamWriter.beginObject();
      jsonStreamWriter.namedStringValueRaw("nodeType", binaryExpression.getKind().toString());
      jsonStreamWriter.separator();
      jsonStreamWriter.namedStringValue("operator", operator.toUriLiteral());
      jsonStreamWriter.separator();
      jsonStreamWriter.namedStringValueRaw("type", getType(binaryExpression));
      jsonStreamWriter.separator();
      jsonStreamWriter.name("left");
      jsonStreamWriter.unquotedValue(leftSide.toString());
      jsonStreamWriter.separator();
      jsonStreamWriter.name("right");
      jsonStreamWriter.unquotedValue(rightSide.toString());
      jsonStreamWriter.endObject();
      writer.flush();
      return writer.toString();
    } catch (final IOException e) {
      return null;
    }
  }

  @Override
  public Object visitOrderByExpression(final OrderByExpression orderByExpression, final String expressionString, final List<Object> orders) {
    try {
      StringWriter writer = new StringWriter();
      JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);
      jsonStreamWriter.beginObject();
      jsonStreamWriter.namedStringValueRaw("nodeType", "order collection");
      jsonStreamWriter.separator();
      jsonStreamWriter.name("orders");
      jsonStreamWriter.beginArray();
      boolean first = true;
      for (final Object order : orders) {
        if (first) {
          first = false;
        } else {
          jsonStreamWriter.separator();
        }
        jsonStreamWriter.unquotedValue(order.toString());
      }
      jsonStreamWriter.endArray();
      jsonStreamWriter.endObject();
      writer.flush();
      return writer.toString();
    } catch (final IOException e) {
      return null;
    }
  }

  @Override
  public Object visitOrder(final OrderExpression orderExpression, final Object filterResult, final SortOrder sortOrder) {
    try {
      StringWriter writer = new StringWriter();
      JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);
      jsonStreamWriter.beginObject();
      jsonStreamWriter.namedStringValueRaw("nodeType", orderExpression.getKind().toString());
      jsonStreamWriter.separator();
      jsonStreamWriter.namedStringValueRaw("sortorder", sortOrder.toString());
      jsonStreamWriter.separator();
      jsonStreamWriter.name("expression");
      jsonStreamWriter.unquotedValue(filterResult.toString());
      jsonStreamWriter.endObject();
      writer.flush();
      return writer.toString();
    } catch (final IOException e) {
      return null;
    }
  }

  @Override
  public Object visitLiteral(final LiteralExpression literal, final EdmLiteral edmLiteral) {
    try {
      StringWriter writer = new StringWriter();
      JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);
      jsonStreamWriter.beginObject();
      jsonStreamWriter.namedStringValueRaw("nodeType", literal.getKind().toString());
      jsonStreamWriter.separator();
      jsonStreamWriter.namedStringValueRaw("type", getType(literal));
      jsonStreamWriter.separator();
      jsonStreamWriter.namedStringValue("value", edmLiteral.getLiteral());
      jsonStreamWriter.endObject();
      writer.flush();
      return writer.toString();
    } catch (final IOException e) {
      return null;
    }
  }

  @Override
  public Object visitMethod(final MethodExpression methodExpression, final MethodOperator method, final List<Object> parameters) {
    try {
      StringWriter writer = new StringWriter();
      JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);
      jsonStreamWriter.beginObject();
      jsonStreamWriter.namedStringValueRaw("nodeType", methodExpression.getKind().toString());
      jsonStreamWriter.separator();
      jsonStreamWriter.namedStringValueRaw("operator", method.toUriLiteral());
      jsonStreamWriter.separator();
      jsonStreamWriter.namedStringValueRaw("type", getType(methodExpression));
      jsonStreamWriter.separator();
      jsonStreamWriter.name("parameters");
      jsonStreamWriter.beginArray();
      boolean first = true;
      for (Object parameter : parameters) {
        if (first) {
          first = false;
        } else {
          jsonStreamWriter.separator();
        }
        jsonStreamWriter.unquotedValue(parameter.toString());
      }
      jsonStreamWriter.endArray();
      jsonStreamWriter.endObject();
      writer.flush();
      return writer.toString();
    } catch (final IOException e) {
      return null;
    }
  }

  @Override
  public Object visitMember(final MemberExpression memberExpression, final Object path, final Object property) {
    try {
      StringWriter writer = new StringWriter();
      JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);
      jsonStreamWriter.beginObject();
      jsonStreamWriter.namedStringValueRaw("nodeType", memberExpression.getKind().toString());
      jsonStreamWriter.separator();
      jsonStreamWriter.namedStringValueRaw("type", getType(memberExpression));
      jsonStreamWriter.separator();
      jsonStreamWriter.name("source");
      jsonStreamWriter.unquotedValue(path.toString());
      jsonStreamWriter.separator();
      jsonStreamWriter.name("path");
      jsonStreamWriter.unquotedValue(property.toString());
      jsonStreamWriter.endObject();
      writer.flush();
      return writer.toString();
    } catch (final IOException e) {
      return null;
    }
  }

  @Override
  public Object visitProperty(final PropertyExpression propertyExpression, final String uriLiteral, final EdmTyped edmProperty) {
    try {
      StringWriter writer = new StringWriter();
      JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);
      jsonStreamWriter.beginObject();
      jsonStreamWriter.namedStringValueRaw("nodeType", propertyExpression.getKind().toString());
      jsonStreamWriter.separator();
      jsonStreamWriter.namedStringValue("name", uriLiteral);
      jsonStreamWriter.separator();
      jsonStreamWriter.namedStringValueRaw("type", getType(propertyExpression));
      jsonStreamWriter.endObject();
      writer.flush();
      return writer.toString();
    } catch (final IOException e) {
      return null;
    }
  }

  @Override
  public Object visitUnary(final UnaryExpression unaryExpression, final UnaryOperator operator, final Object operand) {
    try {
      StringWriter writer = new StringWriter();
      JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);
      jsonStreamWriter.beginObject();
      jsonStreamWriter.namedStringValueRaw("nodeType", unaryExpression.getKind().toString());
      jsonStreamWriter.separator();
      jsonStreamWriter.namedStringValueRaw("operator", operator.toUriLiteral());
      jsonStreamWriter.separator();
      jsonStreamWriter.namedStringValueRaw("type", getType(unaryExpression));
      jsonStreamWriter.separator();
      jsonStreamWriter.name("operand");
      jsonStreamWriter.unquotedValue(operand.toString());
      jsonStreamWriter.endObject();
      writer.flush();
      return writer.toString();
    } catch (final IOException e) {
      return null;
    }
  }

  private static String getType(final CommonExpression expression) {
    try {
      final EdmType type = expression.getEdmType();
      return type == null ? null : type.getNamespace() + Edm.DELIMITER + type.getName();
    } catch (final EdmException e) {
      return "EdmException occurred: " + e.getMessage();
    }
  }
}
