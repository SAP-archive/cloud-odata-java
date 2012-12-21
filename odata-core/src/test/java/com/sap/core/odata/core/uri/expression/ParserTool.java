package com.sap.core.odata.core.uri.expression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExceptionVisitExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.ExpressionVisitor;
import com.sap.core.odata.api.uri.expression.FilterExpression;

public class ParserTool
{
  private String expression;
  private CommonExpression tree;
  private CommonExpression curNode;

  public ParserTool(String expression, FilterExpression root)
  {
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

  public ParserTool aUriLiteral(String uriLiteral)
  {
    String info = "GetUriLiteral(" + expression + ")-->";
    System.out.println("  " + info + "Expected: " + uriLiteral + " Actual: " + curNode.getUriLiteral());

    assertEquals(info, curNode.getUriLiteral(), uriLiteral);
    return this;
  }

  public ParserTool aEdmType(EdmType type) 
  {
    String info = "GetEdmType(" + expression + ")-->";
    try {
      System.out.println("  " + info + "Expected: " + type.getName() + " Actual: " + curNode.getEdmType().getName());
    } catch (EdmException e) {
      fail("Error in aEdmType:" + e.getLocalizedMessage());
    }

    assertEquals(info, curNode.getEdmType(), type);
    return this;
  }

  public ParserTool aEdmProperty(EdmProperty string)
  {
    String info = "GetEdmProperty(" + expression + ")-->";
    
    if (curNode.getKind() !=ExpressionKind.PROPERTY)
    {
      String out = info + "Expected: " + ExpressionKind.PROPERTY + " Actual: " + curNode.getKind().toString();
      System.out.println("  " + out);
      fail(out);
    }
    
    PropertyExpressionImpl propertyExpression = (PropertyExpressionImpl)curNode;
    try {
      System.out.println("  " + info + "Expected: Property'" + string.getName() + "' Actual: " + propertyExpression.getEdmProperty().getName());
    } catch (EdmException e) {
      fail("Error in aEdmProperty:" + e.getLocalizedMessage());
    }

    assertEquals(info, string,  propertyExpression.getEdmProperty());
    return this;
  }
  
  
  public ParserTool aSerialized(String expected)
  {
    String actual = null;
    ExpressionVisitor visitor = new VisitorTool();
    try {
      actual = tree.accept(visitor).toString();
    } catch (ExceptionVisitExpression e) {
      fail("Error in visitor:" + e.getLocalizedMessage());
    } catch (ODataApplicationException e) {
      fail("Error in visitor:" + e.getLocalizedMessage());
    }

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
    case MEMBER:
      curNode = ((MemberExpressionImpl) curNode).getPath();
      break;
    case LITERAL:
    case METHOD:
    case PROPERTY:
      String info = "param(" + expression + ")-->";
      info = "  " + info + "Expected: " + ExpressionKind.BINARY.toString() + " or " + ExpressionKind.MEMBER.toString() + " Actual: " + curNode.getKind();
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
    case MEMBER:
      curNode = ((MemberExpressionImpl) curNode).getProperty();
      break;
    case LITERAL:
    case METHOD:
    case PROPERTY:
      String info = "param(" + expression + ")-->";
      info = "  " + info + "Expected: " + ExpressionKind.BINARY.toString() + " or " + ExpressionKind.MEMBER.toString() + " Actual: " + curNode.getKind();
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
