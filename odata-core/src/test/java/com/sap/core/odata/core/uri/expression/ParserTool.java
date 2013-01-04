package com.sap.core.odata.core.uri.expression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExceptionVisitExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.ExpressionVisitor;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.api.uri.expression.SortOrder;

public class ParserTool
{
  private static boolean debug = true;
  private String expression;
  private CommonExpression tree;
  private CommonExpression curNode;
  
  public static void dout(String out)
  {
    if (debug) System.out.println(out);
  }

  public ParserTool(String expression, FilterExpression root)
  {
    dout("ParserTool - Testing: " + expression);
    this.expression = expression;
    this.tree = root.getExpression();
    this.curNode = this.tree;
  }

  public ParserTool(String expression, OrderByExpression root) {
    dout("ParserTool - Testing: " + expression);
    this.expression = expression;
    this.tree = root;
    this.curNode = this.tree;
  }

  ParserTool aKind(ExpressionKind kind)
  {
    String info = "GetInfoKind(" + expression + ")-->";
    dout("  " + info + "Expected: " + kind.toString() + " Actual: " + curNode.getKind().toString());

    assertEquals(info, kind, curNode.getKind());
    return this;
  }

  public ParserTool aUriLiteral(String uriLiteral)
  {
    String info = "GetUriLiteral(" + expression + ")-->";
    dout("  " + info + "Expected: " + uriLiteral + " Actual: " + curNode.getUriLiteral());

    assertEquals(info, uriLiteral, curNode.getUriLiteral());
    return this;
  }

  public ParserTool aEdmType(EdmType type)
  {
    String info = "GetEdmType(" + expression + ")-->";
    try {
      dout("  " + info + "Expected: " + type.getName() + " Actual: " + curNode.getEdmType().getName());
    } catch (EdmException e) {
      fail("Error in aEdmType:" + e.getLocalizedMessage());
    }

    assertEquals(info, type, curNode.getEdmType());
    return this;
  }
  
  public ParserTool aSortOrder(SortOrder orderType)  
  {
    String info = "GetSortOrder(" + expression + ")-->";
    
    if (curNode.getKind() != ExpressionKind.ORDER)
    {
      String out = info + "Expected: " + ExpressionKind.ORDER + " Actual: " + curNode.getKind().toString();
      dout("  " + out);
      fail(out);
    }
   
    OrderExpressionImpl orderExpression = (OrderExpressionImpl) curNode;
    dout("  " + info + "Expected: " + orderType.toString() + " Actual: " + orderExpression.getSortOrder().toString());

    assertEquals(info, orderType, orderExpression.getSortOrder());
    return this;
  }
  
  public ParserTool aExpr()
  {
    String info = "GetSortOrder(" + expression + ")-->";
    
    if ((curNode.getKind() != ExpressionKind.ORDER) && (curNode.getKind() != ExpressionKind.FILTER) )
    {
      String out = info + "Expected: " + ExpressionKind.ORDER + " or " + ExpressionKind.FILTER + " Actual: " + curNode.getKind().toString();
      dout("  " + out);
      fail(out);
    }
   
    
    if (curNode.getKind() == ExpressionKind.FILTER)
    {
      FilterExpressionImpl filterExpression = (FilterExpressionImpl) curNode;
      curNode = filterExpression.getExpression();
    }
    else
    {
      OrderExpressionImpl orderByExpression = (OrderExpressionImpl) curNode;
      curNode = orderByExpression.getExpression();
    }
        
    return this;
  }
  
  

  public ParserTool aEdmProperty(EdmProperty string)
  {
    String info = "GetEdmProperty(" + expression + ")-->";

    if (curNode.getKind() != ExpressionKind.PROPERTY)
    {
      String out = info + "Expected: " + ExpressionKind.PROPERTY + " Actual: " + curNode.getKind().toString();
      dout("  " + out);
      fail(out);
    }

    PropertyExpressionImpl propertyExpression = (PropertyExpressionImpl) curNode;
    try {
      dout("  " + info + "Expected: Property'" + string.getName() + "' Actual: " + propertyExpression.getEdmProperty().getName());
    } catch (EdmException e) {
      fail("Error in aEdmProperty:" + e.getLocalizedMessage());
    }

    assertEquals(info, string, propertyExpression.getEdmProperty());
    return this;
  }
  
  public ParserTool aSerializedCompr(String expected)
  {
    aSerialized(compress(expected));
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
    dout("  " + info + "Expected: " + expected + " Actual: " + actual);

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
      dout(info);
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
      dout(info);
      fail(info);
      break;
    case UNARY:
      curNode = ((UnaryExpressionImpl) curNode).getOperand();
      break;
    }
    return this;
  }
  
  public ParserTool order(int i)
  {
    if (curNode.getKind() != ExpressionKind.ORDERBY)
    {
      String info = "param(" + expression + ")-->";
      info = "  " + info + "Expected: " + ExpressionKind.ORDERBY.toString() + " Actual: " + curNode.getKind();
      dout(info);
      fail(info);
    } 
    
    OrderByExpressionImpl orderByExpressionImpl = (OrderByExpressionImpl) curNode;
    if (i >= orderByExpressionImpl.getOrdersCount())
    {
      String info = "param(" + expression + ")-->";
      info = "  " + info + "Too wrong index! Expected max: " + orderByExpressionImpl.getOrdersCount() + " Actual: " + i;
      dout(info);
      fail(info);
    }
    
    curNode = orderByExpressionImpl.getOrders().get(i);
    return this;
  
  }

  public ParserTool param(int i)
  {
    if (curNode.getKind() != ExpressionKind.METHOD)
    {
      String info = "param(" + expression + ")-->";
      info = "  " + info + "Expected: " + ExpressionKind.METHOD.toString() + " Actual: " + curNode.getKind();
      dout(info);
      fail(info);
    }

    MethodExpressionImpl methodExpressionImpl = (MethodExpressionImpl) curNode;
    if (i >= methodExpressionImpl.getParameterCount())
    {
      String info = "param(" + expression + ")-->";
      info = "  " + info + "Too wrong index! Expected max: " + methodExpressionImpl.getParameterCount() + " Actual: " + i;
      dout(info);
      fail(info);
    }

    curNode = methodExpressionImpl.getParameters().get(i);
    return this;
  }

  public ParserTool root() {
    curNode = this.tree;
    return this;
  }
  
  static public String compress(String expression)
  {
    String ret = "";
    char [] charArray = expression.trim().toCharArray();
    Character oldChar = null;
    for (char x : charArray)
    {
      if (( x != ' ') || (oldChar == null) || (oldChar !=' '))
        ret += x;
      
      oldChar = x;
    }
    ret = ret.replace("{ ", "{");
    ret = ret.replace(" }", "}");
    return ret;
  }
  
  public static class testParserTool
  {
  @Test
  public void testCompr()
  {
    //leading and trainling spaces
    assertEquals("Error in parsertool" , compress("   a"), "a");
    assertEquals("Error in parsertool" , compress("a    "), "a");
    assertEquals("Error in parsertool" , compress("   a    "), "a");
    
    
    assertEquals("Error in parsertool" , compress("{ a}"), "{a}");
    assertEquals("Error in parsertool" , compress("{   a}"), "{a}");
    assertEquals("Error in parsertool" , compress("{a   }"), "{a}");
    assertEquals("Error in parsertool" , compress("{   a   }"), "{a}");
    
    assertEquals("Error in parsertool" , compress("{ a }"), "{a}");
    assertEquals("Error in parsertool" , compress("{ a a }"), "{a a}");
    assertEquals("Error in parsertool" , compress("{ a   a }"), "{a a}");
    
    assertEquals("Error in parsertool" , compress("   {   a   a   }   "), "{a a}");
    
    assertEquals("Error in parsertool" , compress("   {   a { }  a   }   "), "{a {} a}");
  }
  }
}
