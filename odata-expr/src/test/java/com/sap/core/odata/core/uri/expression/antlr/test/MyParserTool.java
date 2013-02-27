package com.sap.core.odata.core.uri.expression.antlr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExceptionVisitExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.ExpressionVisitor;

public class MyParserTool {
  private final String expression;
  private final CommonExpression tree;
  private final CommonExpression curNode;

  public MyParserTool(final String expression, final CommonExpression tree) {
    this.expression = expression;
    this.tree = tree;
    curNode = this.tree;

  }

  public MyParserTool aSerialized(final String expected) {
    String actual = null;
    final ExpressionVisitor visitor = new MyVisitorTool();
    try {
      actual = tree.accept(visitor).toString();
    } catch (final ExceptionVisitExpression e) {
      fail("Error in visitor:" + e.getLocalizedMessage());
    } catch (final ODataApplicationException e) {
      fail("Error in visitor:" + e.getLocalizedMessage());
    }

    final String info = "GetSerialized(" + expression + ")-->";
    //    System.out.println("  " + info + "Expected: " + expected + " Actual: " + actual);

    assertEquals(info, expected, actual);
    return this;

  }

  public MyParserTool aSerializedCompr(final String expected) {
    aSerialized(compress(expected));
    return this;
  }

  static public String compress(final String expression) {
    String ret = "";
    final char[] charArray = expression.trim().toCharArray();
    Character oldChar = null;
    for (final char x : charArray) {
      if ((x != ' ') || (oldChar == null) || (oldChar != ' ')) {
        ret += x;
      }

      oldChar = x;
    }
    ret = ret.replace("{ ", "{");
    ret = ret.replace(" }", "}");
    return ret;
  }

  MyParserTool aKind(final ExpressionKind kind) {
    final String info = "GetInfoKind(" + expression + ")-->";
    // dout("  " + info + "Expected: " + kind.toString() + " Actual: " + curNode.getKind().toString());

    assertEquals(info, kind, curNode.getKind());
    return this;
  }

}
