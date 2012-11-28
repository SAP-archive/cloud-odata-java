package com.sap.core.odata.core.uri.expression.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.ExpressionVisitor;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.core.uri.expression.BinaryExpressionImpl;
import com.sap.core.odata.core.uri.expression.MethodExpressionImpl;
import com.sap.core.odata.core.uri.expression.UnaryExpressionImpl;


public class ParserTool
{
  private String expression;
  private CommonExpression tree;
  private CommonExpression curNode;

  public ParserTool(String expression, FilterExpression root) {
    System.out.println("ParserTool - Testing: " + expression);
    this.expression = expression;
    this.tree = root.getExpression();
    this.curNode = this.tree;
  }

  ParserTool aKind(ExpressionKind kind)
  {
    String info = "GetInfoKind(" + expression + ")-->";
    System.out.println("  " + info + "Expected: " + kind.toString() + " Actual: " + curNode.getKind().toString());

    assertEquals(info, curNode.getKind(), kind);
    return this;
  }

  public ParserTool aUriLiteral(String uriLiteral) {
    String info = "GetUriLiteral(" + expression + ")-->";
    System.out.println("  " + info + "Expected: " + uriLiteral + " Actual: " + curNode.toUriLiteral());

    assertEquals(info, curNode.toUriLiteral(), uriLiteral);
    return this;
  }

  public ParserTool aEdmType(EdmSimpleType boolInst) {
    String info = "GetEdmType(" + expression + ")-->";
    System.out.println("  " + info + "Expected: " + boolInst.toString() + " Actual: " + curNode.getEdmType().toString());

    assertEquals(info, curNode.getEdmType().equals(boolInst), true);
    return this;
  }

  public ParserTool aSerialized(String expected) {
    String actual;
    ExpressionVisitor visitor = new TestVisitor();
    actual = tree.accept(visitor).toString();

    String info = "GetSerialized(" + expression + ")-->";
    System.out.println("  " + info + "Expected: " + expected + " Actual: " + actual);

    assertEquals(info, expected, actual);
    return this;
  }

  public ParserTool left() {
    switch (curNode.getKind())
    {
    case BINARY:
      curNode = ((BinaryExpressionImpl) curNode).getLeftOperand();
      break;
    case LITERAL:
    case MEMBER:
    case METHOD:
    case PROPERTY:
      String info = "param(" + expression + ")-->";
      info = "  " + info + "Expected: " + ExpressionKind.METHOD.toString() + " or " + ExpressionKind.METHOD.toString() + " Actual: " + curNode.getKind();
      System.out.println(info);
      fail(info);
      break;
    case UNARY:
      curNode = ((UnaryExpressionImpl) curNode).getOperand();
      break;
    }
    return this;
  }

  public ParserTool right() {
    switch (curNode.getKind())
    {
    case BINARY:
      curNode = ((BinaryExpressionImpl) curNode).getRightOperand();
      break;
    case LITERAL:
    case MEMBER:
    case METHOD:
    case PROPERTY:
      String info = "param(" + expression + ")-->";
      info = "  " + info + "Expected: " + ExpressionKind.METHOD.toString() + " or " + ExpressionKind.METHOD.toString() + " Actual: " + curNode.getKind();
      System.out.println(info);
      fail(info);
      break;
    case UNARY:
      curNode = ((UnaryExpressionImpl) curNode).getOperand();
      break;
    }
    return this;
  }

  public ParserTool param(int i)
  {
    if (curNode.getKind() != ExpressionKind.METHOD)
    {
      String info = "param(" + expression + ")-->";
      info = "  " + info + "Expected: " + ExpressionKind.METHOD.toString() + " Actual: " + curNode.getKind();
      System.out.println(info);
      fail(info);
    }

    MethodExpressionImpl methodExpressionImpl = (MethodExpressionImpl) curNode;
    if (i >= methodExpressionImpl.getParameterCount())
    {
      String info = "param(" + expression + ")-->";
      info = "  " + info + "Too wrong index! Expected max: " + methodExpressionImpl.getParameterCount() + " Actual: " + i;
      System.out.println(info);
      fail(info);
    }

    curNode = methodExpressionImpl.getParameters().elementAt(i);
    return this;
  }

  public ParserTool root() {
    curNode = this.tree;
    return this;
  }

}
