package com.sap.core.odata.core.uri.expression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Locale;

import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.exception.ODataMessageException;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExceptionVisitExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.ExpressionParserException;
import com.sap.core.odata.api.uri.expression.ExpressionVisitor;
import com.sap.core.odata.api.uri.expression.SortOrder;
import com.sap.core.odata.core.exception.MessageService;
import com.sap.core.odata.core.exception.MessageService.Message;

/**
 * @author SAP AG
 */
public class ParserTool
{

  static {
    DOMConfigurator.configureAndWatch("log4j.xml");
  }

  private static final Logger log = LoggerFactory.getLogger(ParserTool.class);

  private static boolean debug = false;

  private String expression;
  private CommonExpression tree;
  private CommonExpression curNode;
  private Exception curException;
  private Exception exception;
  private static final Locale DEFAULT_LANGUAGE = new Locale("test", "SAP");

  public static void dout(String out)
  {
    if (debug) ParserTool.log.debug(out);
  }

  public ParserTool(String expression, boolean isOrder, boolean addTestfunctions)
  {
    dout("ParserTool - Testing: " + expression);
    this.expression = expression;

    try {
      if (!isOrder)
      {
        TestFilterParserImpl parser = new TestFilterParserImpl(null, null);
        if (addTestfunctions) parser.addTestfunctions();
        this.tree = parser.parseFilterString(expression).getExpression();
      }
      else
      {
        OrderByParserImpl parser = new OrderByParserImpl(null, null);
        this.tree = parser.parseOrderByString(expression);
      }
    } catch (ExpressionParserException e) {
      this.exception = e;
    } catch (ExpressionParserInternalError e) {
      this.exception = e;
    }

    this.curNode = this.tree;
    this.curException = this.exception;
  }

  public ParserTool(String expression, boolean isOrder, boolean addTestfunctions, Edm edm, EdmEntityType resourceEntityType)
  {
    dout("ParserTool - Testing: " + expression);
    this.expression = expression;

    try {
      if (!isOrder)
      {
        TestFilterParserImpl parser = new TestFilterParserImpl(edm, resourceEntityType);
        if (addTestfunctions) parser.addTestfunctions();
        this.tree = parser.parseFilterString(expression).getExpression();
      }
      else
      {
        OrderByParserImpl parser = new OrderByParserImpl(edm, resourceEntityType);
        this.tree = parser.parseOrderByString(expression);
      }
    } catch (ExpressionParserException e) {
      this.curException = e;
    } catch (ExpressionParserInternalError e) {
      this.curException = e;
    }

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

  /**
   * Verifies that the thrown exception is of {@paramref expected}
   * @param expected
   *   Expected Exception class 
   * @return
   */
  public ParserTool aExType(Class<? extends Exception> expected)
  {
    String info = "GetExceptionType(" + expression + ")-->";

    if (curException == null)
    {
      fail("Error in aExType: Expected exception " + expected.getName());
    }

    dout("  " + info + "Expected: " + expected.getName() + " Actual: " + curException.getClass().getName());

    if (expected != curException.getClass())
    {
      fail("  " + info + "Expected: " + expected.getName() + " Actual: " + curException.getClass().getName());
    }
    return this;
  }

  /**
   * Verifies that the message text of the thrown exception serialized is {@paramref messageText}
   * @param messageText  
   *   Expected message text 
   * @return  this
   */
  public ParserTool aExMsgText(String messageText)
  {
    String info = "aExMessageText(" + expression + ")-->";
    if (curException == null)
    {
      fail("Error in aExMessageText: Expected exception.");
    }

    ODataMessageException messageException;

    try
    {
      messageException = (ODataMessageException) curException;
    } catch (ClassCastException ex)
    {
      fail("Error in aExNext: curException not an ODataMessageException");
      return this;
    }

    Message ms = MessageService.getMessage(DEFAULT_LANGUAGE, messageException.getMessageReference());
    info = "  " + info + "Expected: '" + messageText + "' Actual: '" + ms.getText() + "'";

    dout(info);
    assertEquals(info, messageText, ms.getText());

    return this;
  }

  /**
   * Verifies that all place holders in the message text definition of the thrown exception are provided with content
   * @return
   */
  public ParserTool aExMsgContentAllSet()
  {
    String info = "aExMessageTextNoEmptyTag(" + expression + ")-->";
    if (curException == null)
    {
      fail("Error in aExMessageText: Expected exception.");
    }

    ODataMessageException messageException;

    try
    {
      messageException = (ODataMessageException) curException;
    } catch (ClassCastException ex)
    {
      fail("Error in aExNext: curException not an ODataMessageException");
      return this;
    }

    Message ms = MessageService.getMessage(DEFAULT_LANGUAGE, messageException.getMessageReference());
    info = "  " + info + "Messagetext: '" + ms.getText() + "contains [%";

    dout(info);

    if (ms.getText().contains("[%"))
    {
      fail(info);
    }
    return this;
  }

  /**
   * Verifies that the message text of the thrown exception is not empty
   * @return
   */
  public ParserTool aExMsgNotEmpty()
  {
    String info = "aExTextNotEmpty(" + expression + ")-->";
    if (curException == null)
    {
      fail("Error in aExMessageText: Expected exception.");
    }

    ODataMessageException messageException;

    try
    {
      messageException = (ODataMessageException) curException;
    } catch (ClassCastException ex)
    {
      fail("Error in aExNext: curException not an ODataMessageException");
      return this;
    }

    Message ms = MessageService.getMessage(DEFAULT_LANGUAGE, messageException.getMessageReference());
    info = "  " + info + "check if Messagetext is empty";
    dout(info);

    if (ms.getText().length() == 0)
    {
      fail(info);
    }
    return this;
  }

  public ParserTool aExKey(MessageReference expressionExpectedAtPos) {
    String expectedKey = expressionExpectedAtPos.getKey();
    ODataMessageException messageException;

    String info = "GetExceptionType(" + expression + ")-->";

    if (curException == null)
    {
      fail("Error in aExType: Expected exception");
    }

    try
    {
      messageException = (ODataMessageException) curException;
    } catch (ClassCastException ex)
    {
      fail("Error in aExNext: curException not an ODataMessageException");
      return this;
    }

    String actualKey = messageException.getMessageReference().getKey();
    dout("  " + info + "Expected key: " + expectedKey + " Actual: " + actualKey);

    if (expectedKey != actualKey)
    {
      fail("  " + info + "Expected: " + expectedKey + " Actual: " + actualKey);
    }
    return this;
  }

  public ParserTool printExMessage() {
    ODataMessageException messageException;

    if (curException == null)
    {
      fail("Error in aExMsgPrint: Expected exception");
    }

    try
    {
      messageException = (ODataMessageException) curException;
    } catch (ClassCastException ex)
    {
      fail("Error in aExNext: curException not an ODataMessageException");
      return this;
    }

    Message ms = MessageService.getMessage(DEFAULT_LANGUAGE, messageException.getMessageReference());
    dout("Messge --> ");
    dout("  " + ms.getText());
    dout("Messge <-- ");

    return this;
  }

  public ParserTool printSerialized()
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

    dout("Messge --> ");
    dout("  " + actual);
    dout("Messge <-- ");
    return this;
  }

  public ParserTool exPrintStack()
  {
    curException.printStackTrace();
    return this;
  }

  public ParserTool exNext()
  {
    try
    {
      curException = (Exception) curException.getCause();
    } catch (ClassCastException ex)
    {
      fail("Error in aExNext: Cause not an Exception");
    }
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

    if ((curNode.getKind() != ExpressionKind.ORDER) && (curNode.getKind() != ExpressionKind.FILTER))
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
    default:
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
    default:
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
    char[] charArray = expression.trim().toCharArray();
    Character oldChar = null;
    for (char x : charArray)
    {
      if ((x != ' ') || (oldChar == null) || (oldChar != ' '))
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
      assertEquals("Error in parsertool", compress("   a"), "a");
      assertEquals("Error in parsertool", compress("a    "), "a");
      assertEquals("Error in parsertool", compress("   a    "), "a");

      assertEquals("Error in parsertool", compress("{ a}"), "{a}");
      assertEquals("Error in parsertool", compress("{   a}"), "{a}");
      assertEquals("Error in parsertool", compress("{a   }"), "{a}");
      assertEquals("Error in parsertool", compress("{   a   }"), "{a}");

      assertEquals("Error in parsertool", compress("{ a }"), "{a}");
      assertEquals("Error in parsertool", compress("{ a a }"), "{a a}");
      assertEquals("Error in parsertool", compress("{ a   a }"), "{a a}");

      assertEquals("Error in parsertool", compress("   {   a   a   }   "), "{a a}");

      assertEquals("Error in parsertool", compress("   {   a { }  a   }   "), "{a {} a}");
    }
  }

  public ParserTool exRoot() {
    curException = exception;
    return this;
  }

}
