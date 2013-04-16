package com.sap.core.odata.core.uri.expression.antlr.test;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.junit.Test;

import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.core.uri.expression.antlr.ODataFilter4Lexer;
import com.sap.core.odata.core.uri.expression.antlr.ODataFilter4Parser;
import com.sap.core.odata.core.uri.expression.antlr.ODataTreeWalker;

public class ParserTest {
  @Test
  public void testQuick() throws RecognitionException {
    getParserFilter("substring('Test',1 add 2)").aSerialized("{substring('Test',{1 add 2})}");
  }

  @Test
  public void testSimpleSameBinary() throws RecognitionException {
    getParserFilter("1 add 2").aSerialized("{1 add 2}");

    getParserFilter("1 add 2").aSerialized("{1 add 2}").aKind(ExpressionKind.BINARY);

  }

  @Test
  public void testSimpleSameBinaryBinaryBinaryPriority() throws RecognitionException {
    getParserFilter("1 add 2 add 3 add 4").aSerialized("{{{1 add 2} add 3} add 4}");
    getParserFilter("1 add 2 add 3 div 4").aSerialized("{{1 add 2} add {3 div 4}}");
    getParserFilter("1 add 2 div 3 add 4").aSerialized("{{1 add {2 div 3}} add 4}");
    getParserFilter("1 add 2 div 3 div 4").aSerialized("{1 add {{2 div 3} div 4}}");
    getParserFilter("1 div 2 add 3 add 4").aSerialized("{{{1 div 2} add 3} add 4}");
    getParserFilter("1 div 2 add 3 div 4").aSerialized("{{1 div 2} add {3 div 4}}");
    getParserFilter("1 div 2 div 3 add 4").aSerialized("{{{1 div 2} div 3} add 4}");
    getParserFilter("1 div 2 div 3 div 4").aSerialized("{{{1 div 2} div 3} div 4}");
  }

  @Test
  public void testParenthesisWithBinaryBinary() throws RecognitionException {
    getParserFilter("1 add 2 add 3").aSerialized("{{1 add 2} add 3}");
    getParserFilter("1 add (2 add 3)").aSerialized("{1 add {2 add 3}}");
    getParserFilter("(1 add 2) add 3").aSerialized("{{1 add 2} add 3}");
    getParserFilter("(1 add 2 add 3)").aSerialized("{{1 add 2} add 3}");

    getParserFilter("1 add 2 div 3").aSerialized("{1 add {2 div 3}}");
    getParserFilter("1 add (2 div 3)").aSerialized("{1 add {2 div 3}}");
    getParserFilter("(1 add 2) div 3").aSerialized("{{1 add 2} div 3}");
    getParserFilter("(1 add 2 div 3)").aSerialized("{1 add {2 div 3}}");

    getParserFilter("1 div 2 div 3").aSerialized("{{1 div 2} div 3}");
    getParserFilter("1 div (2 div 3)").aSerialized("{1 div {2 div 3}}");
    getParserFilter("(1 div 2) div 3").aSerialized("{{1 div 2} div 3}");
    getParserFilter("(1 div 2 div 3)").aSerialized("{{1 div 2} div 3}");
  }

  @Test
  public void testDeepParenthesis() throws RecognitionException {
    getParserFilter("2").aSerialized("2");
    getParserFilter("(2)").aSerialized("2");
    getParserFilter("((2))").aSerialized("2");
    getParserFilter("(((2)))").aSerialized("2");
  }

  @Test
  public void testComplexMixedPriority() throws RecognitionException {
    getParserFilter("1      or 3      and 5     ").aSerializedCompr("{ 1       or { 3       and  5      }}");
    getParserFilter("1      or 3      and 5 eq 6").aSerializedCompr("{ 1       or { 3       and {5 eq 6}}}");
    getParserFilter("1      or 3 eq 4 and 5     ").aSerializedCompr("{ 1       or {{3 eq 4} and  5      }}");
    getParserFilter("1      or 3 eq 4 and 5 eq 6").aSerializedCompr("{ 1       or {{3 eq 4} and {5 eq 6}}}");
    getParserFilter("1 eq 2 or 3      and 5     ").aSerializedCompr("{{1 eq 2} or { 3       and  5      }}");
    getParserFilter("1 eq 2 or 3      and 5 eq 6").aSerializedCompr("{{1 eq 2} or { 3       and {5 eq 6}}}");
    getParserFilter("1 eq 2 or 3 eq 4 and 5     ").aSerializedCompr("{{1 eq 2} or {{3 eq 4} and  5      }}");
    getParserFilter("1 eq 2 or 3 eq 4 and 5 eq 6").aSerializedCompr("{{1 eq 2} or {{3 eq 4} and {5 eq 6}}}");

    getParserFilter("(1 eq 2) or (3 eq 4) and (5 eq 6)").aSerialized("{{1 eq 2} or {{3 eq 4} and {5 eq 6}}}");
    getParserFilter("(a eq b) or (c eq d) and (e eq f)").aSerialized("{{a eq b} or {{c eq d} and {e eq f}}}");
  }

  @Test
  public void testSimpleUnaryOperator() throws RecognitionException {
    getParserFilter("not true").aSerialized("{not true}").aKind(ExpressionKind.UNARY);
    //   getParserFilter("- 2").aSerialized("{- 2}");
  }

  @Test
  public void testDeepUnaryOperator() throws RecognitionException {
    getParserFilter("not not true").aSerialized("{not {not true}}");
    getParserFilter("not not not true").aSerialized("{not {not {not true}}}");
    /*getParserFilter("-- 2d").aSerialized("{- {- 2d}}");
    getParserFilter("- - 2d").aSerialized("{- {- 2d}}");
    getParserFilter("--- 2d").aSerialized("{- {- {- 2d}}}");
    getParserFilter("- - - 2d").aSerialized("{- {- {- 2d}}}");

    getParserFilter("-(-(- 2d))").aSerialized("{- {- {- 2d}}}");*/
    getParserFilter("not(not(not true))").aSerialized("{not {not {not true}}}");
  }

  @Test
  public void testSimpleMethod() throws RecognitionException {

    getParserFilter("startswith('Test','Te')").aSerialized("{startswith('Test','Te')}").aKind(ExpressionKind.METHOD);

    getParserFilter("startswith('Test',concat('A','B'))").aSerialized("{startswith('Test',{concat('A','B')})}");

    getParserFilter("substring('Test',1 add 2)").aSerialized("{substring('Test',{1 add 2})}");
  }

  @Test
  public void testProperties() throws RecognitionException {
    getParserFilter("property1 add property2").aKind(ExpressionKind.BINARY);
    getParserFilter("property1 add property2").aSerialized("{property1 add property2}");
  }

  @Test
  public void testDeepProperties() throws RecognitionException {
    getParserFilter("a.adress/city").aKind(ExpressionKind.MEMBER);
    getParserFilter("a.adress/city").aSerialized("{a.adress/city}");
    getParserFilter("c").aSerialized("c").aKind(ExpressionKind.PROPERTY);
    ;
  }

  @Test
  public void testString() throws RecognitionException {
    getParserFilter("'TEST'").aKind(ExpressionKind.LITERAL);
    getParserFilter("'TEST'").aSerialized("'TEST'");
    getParserFilter("'TE''ST'").aSerialized("'TE'ST'");
  }

  @Test
  public void test() throws RecognitionException {
    getParserFilter("(true and (id eq (6 div 2)) or substringof('te',concat('t','est')))").aSerialized("{{true and {id eq {6 div 2}}} or {substringof('te',{concat('t','est')})}}");
  }

  static public MyParserTool getParserFilter(final String expression) throws RecognitionException {
    final ANTLRStringStream input = new ANTLRStringStream(expression);
    final ODataFilter4Lexer lexer = new ODataFilter4Lexer(input);
    final CommonTokenStream tokens = new CommonTokenStream(lexer);
    final ODataFilter4Parser parser = new ODataFilter4Parser(tokens);
    // launch the parser starting at rule boolCommonExpr, get return object
    final ODataFilter4Parser.boolCommonExpr_return r = parser.boolCommonExpr();

    // get tree from parser
    final CommonTree tree = (CommonTree) r.getTree();

    //create a tree node stream from resulting tree
    final CommonTreeNodeStream nodes = new CommonTreeNodeStream(tree);

    //create a tree parser
    final ODataTreeWalker walker = new ODataTreeWalker(nodes);

    // launch at start rule expr
    final CommonExpression expr = walker.expr();
    return new MyParserTool(expression, expr);

  }
}
