package com.sap.core.odata.core.uri.expression;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.ExpressionParserException;
import com.sap.core.odata.api.uri.expression.SortOrder;
import com.sap.core.odata.core.edm.Bit;
import com.sap.core.odata.core.edm.EdmBinary;
import com.sap.core.odata.core.edm.EdmBoolean;
import com.sap.core.odata.core.edm.EdmByte;
import com.sap.core.odata.core.edm.EdmDateTime;
import com.sap.core.odata.core.edm.EdmDateTimeOffset;
import com.sap.core.odata.core.edm.EdmDecimal;
import com.sap.core.odata.core.edm.EdmDouble;
import com.sap.core.odata.core.edm.EdmGuid;
import com.sap.core.odata.core.edm.EdmInt16;
import com.sap.core.odata.core.edm.EdmInt32;
import com.sap.core.odata.core.edm.EdmInt64;
import com.sap.core.odata.core.edm.EdmSByte;
import com.sap.core.odata.core.edm.EdmSingle;
import com.sap.core.odata.core.edm.EdmString;
import com.sap.core.odata.core.edm.EdmTime;
import com.sap.core.odata.core.edm.Uint7;
import com.sap.core.odata.core.edm.provider.EdmComplexPropertyImplProv;

public class TestParser extends TestBase
{

  @Test
  public void testQuick()
  {
    GetPTF("substring('Test', 1 add 2)").aSerialized("{substring('Test',{1 add 2})}");
  }

  @Test
  public void testOrderBy()
  {

    GetPTO("sven").aSerialized("{oc({o(sven, asc)})}");
    GetPTO("sven asc").aSerialized("{oc({o(sven, asc)})}");
    GetPTO("sven desc").aSerialized("{oc({o(sven, desc)})}");
    GetPTO("sven     asc").aSerialized("{oc({o(sven, asc)})}");
    GetPTO("sven     desc").aSerialized("{oc({o(sven, desc)})}");

    GetPTO("sven, test").aSerialized("{oc({o(sven, asc)},{o(test, asc)})}");
    GetPTO("sven   ,    test").aSerialized("{oc({o(sven, asc)},{o(test, asc)})}");

    GetPTO("sven, test asc").aSerialized("{oc({o(sven, asc)},{o(test, asc)})}");

    GetPTO("sven asc, test").aSerialized("{oc({o(sven, asc)},{o(test, asc)})}");

    GetPTO("sven asc, test asc").aSerialized("{oc({o(sven, asc)},{o(test, asc)})}");
    GetPTO("sven, test desc").aSerialized("{oc({o(sven, asc)},{o(test, desc)})}");
    GetPTO("sven desc, test").aSerialized("{oc({o(sven, desc)},{o(test, asc)})}");
    GetPTO("sven desc, test desc").aSerialized("{oc({o(sven, desc)},{o(test, desc)})}");

    GetPTO("'sven', 77").order(1).aSortOrder(SortOrder.asc);
    GetPTO("'sven', 77 desc")
        .root().order(0).aSortOrder(SortOrder.asc).aExpr().aEdmType(EdmString.getInstance())
        .root().order(1).aSortOrder(SortOrder.desc).aExpr().aEdmType(Uint7.getInstance());

  }

  @Test
  public void testPromotion()
  {
    //SByte <--> SByte
    GetPTF("-10").aEdmType(EdmSByte.getInstance());
    GetPTF("-10 add -10").aEdmType(EdmSByte.getInstance());

    //Byte <--> Byte
    GetPTF("200").aEdmType(EdmByte.getInstance());
    GetPTF("200 add 200").aEdmType(EdmByte.getInstance());

    //SByte <--> Byte
    GetPTF("-10 add 200").aEdmType(EdmInt16.getInstance());
    //Byte <--> SByte
    GetPTF("200 add -10").aEdmType(EdmInt16.getInstance());

    //Uint7 <--> Uint7
    GetPTF("100").aEdmType(Uint7.getInstance());
    GetPTF("100 add 100").aEdmType(EdmSByte.getInstance());

    //Uint7 <--> Byte
    GetPTF("100 add 200").aEdmType(EdmByte.getInstance());

    //Byte <--> Uint7
    GetPTF("200 add 100").aEdmType(EdmByte.getInstance());

    //Uint7 <--> SByte
    GetPTF("100 add -10").aEdmType(EdmSByte.getInstance());

    //SByte <--> Uint7 
    GetPTF("-10 add 100").aEdmType(EdmSByte.getInstance());

    GetPTF("1000").aEdmType(EdmInt16.getInstance());
    GetPTF("1000 add 1000").aEdmType(EdmInt16.getInstance());

    GetPTF("concat('a','b')").aEdmType(EdmString.getInstance());
    GetPTF("concat('a','b','c')").aEdmType(EdmString.getInstance());
  }

  @Test
  public void testProperties()
  {
    //GetPTF("sven").aSerialized("sven").aKind(ExpressionKind.PROPERTY);
    GetPTF("sven1 add sven2").aSerialized("{sven1 add sven2}")
        .aKind(ExpressionKind.BINARY)
        .root().left().aKind(ExpressionKind.PROPERTY).aUriLiteral("sven1")
        .root().right().aKind(ExpressionKind.PROPERTY).aUriLiteral("sven2");
  }

  @Test
  public void testDeepProperties()
  {
    GetPTF("a/b").aSerialized("{a/b}").aKind(ExpressionKind.MEMBER);
    GetPTF("a/b/c").aSerialized("{{a/b}/c}")
        .root().aKind(ExpressionKind.MEMBER)
        .root().left().aKind(ExpressionKind.MEMBER)
        .root().left().left().aKind(ExpressionKind.PROPERTY).aUriLiteral("a")
        .root().left().right().aKind(ExpressionKind.PROPERTY).aUriLiteral("b")
        .root().right().aKind(ExpressionKind.PROPERTY).aUriLiteral("c");
  }

  @Test
  public void testPropertiesWithEdm()
  {
    try {
      EdmEntityType edmEtAllTypes = edmInfo.getTypeEtAllTypes();
      EdmProperty string = (EdmProperty) edmEtAllTypes.getProperty("String");
      EdmSimpleType stringType = (EdmSimpleType) string.getType();
      EdmComplexPropertyImplProv complex = (EdmComplexPropertyImplProv) edmEtAllTypes.getProperty("Complex");
      EdmComplexType complexType = (EdmComplexType) complex.getType();
      EdmProperty complexString = (EdmProperty) complexType.getProperty("String");
      EdmSimpleType complexStringType = (EdmSimpleType) complexString.getType();
      EdmComplexPropertyImplProv complexAddress = (EdmComplexPropertyImplProv) complexType.getProperty("Address");
      EdmComplexType complexAddressType = (EdmComplexType) complexAddress.getType();
      EdmProperty complexAddressCity = (EdmProperty) complexAddressType.getProperty("City");
      EdmSimpleType complexAddressCityType = (EdmSimpleType) complexAddressCity.getType();

      GetPTF(edmEtAllTypes, "String").aEdmProperty(string).aEdmType(stringType);

      GetPTF(edmEtAllTypes, "'text' eq String")
          .root().aKind(ExpressionKind.BINARY);

      GetPTF(edmEtAllTypes, "Complex/String")
          .root().left().aEdmProperty(complex).aEdmType(complexType)
          .root().right().aEdmProperty(complexString).aEdmType(complexStringType)
          .root().aKind(ExpressionKind.MEMBER).aEdmType(complexStringType);

      GetPTF(edmEtAllTypes, "Complex/Address/City")
          .root().aKind(ExpressionKind.MEMBER)
          .root().left().aKind(ExpressionKind.MEMBER)
          .root().left().left().aKind(ExpressionKind.PROPERTY).aEdmProperty(complex).aEdmType(complexType)
          .root().left().right().aKind(ExpressionKind.PROPERTY).aEdmProperty(complexAddress).aEdmType(complexAddressType)
          .root().left().aEdmType(complexAddressType)
          .root().right().aKind(ExpressionKind.PROPERTY).aEdmProperty(complexAddressCity).aEdmType(complexAddressCityType)
          .root().aEdmType(complexAddressCityType);

      EdmProperty boolean_ = (EdmProperty) edmEtAllTypes.getProperty("Boolean");
      EdmSimpleType boolean_Type = (EdmSimpleType) boolean_.getType();

      GetPTF(edmEtAllTypes, "not Boolean")
          .aKind(ExpressionKind.UNARY)
          .aEdmType(boolean_Type)
          .right().aEdmProperty(boolean_).aEdmType(boolean_Type);

    } catch (EdmException e) {
      fail("Error in testPropertiesWithEdm:" + e.getLocalizedMessage());
      e.printStackTrace();
    }
  }

  @Test
  public void testSimpleMethod()
  {
    GetPTF("startswith('Test','Te')").aSerialized("{startswith('Test','Te')}");

    GetPTF("startswith('Test','Te')").aSerialized("{startswith('Test','Te')}");

    GetPTF("startswith('Test', concat('A','B'))").aSerialized("{startswith('Test',{concat('A','B')})}");

    GetPTF("substring('Test', 1 add 2)").aSerialized("{substring('Test',{1 add 2})}");
  }

  @Test
  public void testMethodVariableParameters()
  {
    GetPTF("concat('Test', 'A' )").aSerialized("{concat('Test','A')}");
    GetPTF("concat('Test', 'A', 'B' )").aSerialized("{concat('Test','A','B')}");
    GetPTF("concat('Test', 'A', 'B', 'C' )").aSerialized("{concat('Test','A','B','C')}");
  }

  @Test
  public void testSimpleSameBinary()
  {
    GetPTF("1d add 2d").aSerialized("{1d add 2d}");
    GetPTF("1d div 2d").aSerialized("{1d div 2d}");

    GetPTF("1d add 2d").aSerialized("{1d add 2d}")
        .aKind(ExpressionKind.BINARY)
        .root().left().aKind(ExpressionKind.LITERAL)
        .root().right().aKind(ExpressionKind.LITERAL);

  }

  @Test
  public void testSimpleSameBinaryBinary()
  {
    GetPTF("1d add 2d add 3d").aSerialized("{{1d add 2d} add 3d}");
    GetPTF("1d div 2d div 3d").aSerialized("{{1d div 2d} div 3d}");
  }

  @Test
  public void testSimpleSameBinaryBinaryPriority()
  {
    GetPTF("1d add 2d div 3d").aSerialized("{1d add {2d div 3d}}");
    GetPTF("1d div 2d add 3d").aSerialized("{{1d div 2d} add 3d}");
  }

  @Test
  public void testSimpleSameBinaryBinaryBinaryPriority()
  {
    GetPTF("1d add 2d add 3d add 4d").aSerialized("{{{1d add 2d} add 3d} add 4d}");
    GetPTF("1d add 2d add 3d div 4d").aSerialized("{{1d add 2d} add {3d div 4d}}");
    GetPTF("1d add 2d div 3d add 4d").aSerialized("{{1d add {2d div 3d}} add 4d}");
    GetPTF("1d add 2d div 3d div 4d").aSerialized("{1d add {{2d div 3d} div 4d}}");
    GetPTF("1d div 2d add 3d add 4d").aSerialized("{{{1d div 2d} add 3d} add 4d}");
    GetPTF("1d div 2d add 3d div 4d").aSerialized("{{1d div 2d} add {3d div 4d}}");
    GetPTF("1d div 2d div 3d add 4d").aSerialized("{{{1d div 2d} div 3d} add 4d}");
    GetPTF("1d div 2d div 3d div 4d").aSerialized("{{{1d div 2d} div 3d} div 4d}");
  }

  @Test
  public void testComplexMixedPriority()
  {
    GetPTF("a      or c      and e     ").aSerializedCompr("{ a       or { c       and  e      }}");
    GetPTF("a      or c      and e eq f").aSerializedCompr("{ a       or { c       and {e eq f}}}");
    GetPTF("a      or c eq d and e     ").aSerializedCompr("{ a       or {{c eq d} and  e      }}");
    GetPTF("a      or c eq d and e eq f").aSerializedCompr("{ a       or {{c eq d} and {e eq f}}}");
    GetPTF("a eq b or c      and e     ").aSerializedCompr("{{a eq b} or { c       and  e      }}");
    GetPTF("a eq b or c      and e eq f").aSerializedCompr("{{a eq b} or { c       and {e eq f}}}");
    GetPTF("a eq b or c eq d and e     ").aSerializedCompr("{{a eq b} or {{c eq d} and  e      }}");
    GetPTF("a eq b or c eq d and e eq f").aSerializedCompr("{{a eq b} or {{c eq d} and {e eq f}}}");

    //helper
    GetPTF("(a eq b) or (c eq d) and (e eq f)").aSerialized("{{a eq b} or {{c eq d} and {e eq f}}}");
  }

  @Test
  public void testDeepParenthesis()
  {
    GetPTF("2d").aSerialized("2d");
    GetPTF("(2d)").aSerialized("2d");
    GetPTF("((2d))").aSerialized("2d");
    GetPTF("(((2d)))").aSerialized("2d");
  }

  @Test
  public void testParenthesisWithBinaryBinary()
  {
    GetPTF("1d add 2d add 3d").aSerialized("{{1d add 2d} add 3d}");
    GetPTF("1d add (2d add 3d)").aSerialized("{1d add {2d add 3d}}");
    GetPTF("(1d add 2d) add 3d").aSerialized("{{1d add 2d} add 3d}");
    GetPTF("(1d add 2d add 3d)").aSerialized("{{1d add 2d} add 3d}");

    GetPTF("1d add 2d div 3d").aSerialized("{1d add {2d div 3d}}");
    GetPTF("1d add (2d div 3d)").aSerialized("{1d add {2d div 3d}}");
    GetPTF("(1d add 2d) div 3d").aSerialized("{{1d add 2d} div 3d}");
    GetPTF("(1d add 2d div 3d)").aSerialized("{1d add {2d div 3d}}");

    GetPTF("1d div 2d div 3d").aSerialized("{{1d div 2d} div 3d}");
    GetPTF("1d div (2d div 3d)").aSerialized("{1d div {2d div 3d}}");
    GetPTF("(1d div 2d) div 3d").aSerialized("{{1d div 2d} div 3d}");
    GetPTF("(1d div 2d div 3d)").aSerialized("{{1d div 2d} div 3d}");
  }

  @Test
  public void testSimpleUnaryOperator()
  {
    GetPTF("not true").aSerialized("{not true}");
    GetPTF("- 2d").aSerialized("{- 2d}");
  }

  @Test
  public void testDeepUnaryOperator()
  {
    GetPTF("not not true").aSerialized("{not {not true}}");
    GetPTF("not not not true").aSerialized("{not {not {not true}}}");
    GetPTF("-- 2d").aSerialized("{- {- 2d}}");
    GetPTF("- - 2d").aSerialized("{- {- 2d}}");
    GetPTF("--- 2d").aSerialized("{- {- {- 2d}}}");
    GetPTF("- - - 2d").aSerialized("{- {- {- 2d}}}");

    GetPTF("-(-(- 2d))").aSerialized("{- {- {- 2d}}}");
    GetPTF("not(not(not 2d))").aSerialized("{not {not {not 2d}}}");
  }

  @Test
  public void testMixedUnaryOperators()
  {
    GetPTF("not - true").aSerialized("{not {- true}}");
    GetPTF("- not true").aSerialized("{- {not true}}");
  }

  @Test
  public void testDeepMixedUnaryOperators()
  {
    GetPTF("- not - true").aSerialized("{- {not {- true}}}");
    GetPTF("not - not true").aSerialized("{not {- {not true}}}");
  }

  @Test
  public void testString()
  {
    GetPTF("'TEST'").aSerialized("'TEST'");
    GetPTF("'TE''ST'").aSerialized("'TE'ST'");
  }

  @Test
  public void testSinglePlainLiterals()
  {
    //assertEquals("Hier", 44, 33);
    //---
    //Checks from EdmSimpleType test
    //---
    EdmBoolean boolInst = EdmBoolean.getInstance();
    EdmBinary binaryInst = EdmBinary.getInstance();
    Bit bitInst = Bit.getInstance();
    EdmByte byteInst = EdmByte.getInstance();
    Uint7 Uint7Inst = Uint7.getInstance();
    EdmDateTime datetimeInst = EdmDateTime.getInstance();
    EdmDateTimeOffset datetimeOffsetInst = EdmDateTimeOffset.getInstance();
    EdmDecimal decimalInst = EdmDecimal.getInstance();
    EdmDouble doubleInst = EdmDouble.getInstance();
    EdmGuid guidInst = EdmGuid.getInstance();
    EdmInt16 int16Inst = EdmInt16.getInstance();
    EdmInt32 int32Inst = EdmInt32.getInstance();
    EdmInt64 int64Inst = EdmInt64.getInstance();
    EdmSByte intSByte = EdmSByte.getInstance();
    EdmSingle singleInst = EdmSingle.getInstance();
    EdmString stringInst = EdmString.getInstance();
    EdmTime timeInst = EdmTime.getInstance();

    GetPTF("X'Fa12aAA1'").aUriLiteral("X'Fa12aAA1'").aKind(ExpressionKind.LITERAL).aEdmType(binaryInst);
    GetPTF("binary'FA12AAA1'").aUriLiteral("binary'FA12AAA1'").aKind(ExpressionKind.LITERAL).aEdmType(binaryInst);

    GetPTF("true").aUriLiteral("true").aKind(ExpressionKind.LITERAL).aEdmType(boolInst);
    GetPTF("false").aUriLiteral("false").aKind(ExpressionKind.LITERAL).aEdmType(boolInst);

    GetPTF("1").aUriLiteral("1").aKind(ExpressionKind.LITERAL).aEdmType(bitInst);
    GetPTF("0").aUriLiteral("0").aKind(ExpressionKind.LITERAL).aEdmType(bitInst);

    GetPTF("255").aUriLiteral("255").aKind(ExpressionKind.LITERAL).aEdmType(byteInst);

    GetPTF("123").aUriLiteral("123").aKind(ExpressionKind.LITERAL).aEdmType(Uint7Inst);

    GetPTF("datetime'2009-12-26T21:23:38'").aUriLiteral("datetime'2009-12-26T21:23:38'").aKind(ExpressionKind.LITERAL).aEdmType(datetimeInst);
    GetPTF("datetime'2009-12-26T21:23:38'").aUriLiteral("datetime'2009-12-26T21:23:38'").aKind(ExpressionKind.LITERAL).aEdmType(datetimeInst);

    GetPTF("datetimeoffset'2009-12-26T21:23:38Z'").aUriLiteral("datetimeoffset'2009-12-26T21:23:38Z'").aKind(ExpressionKind.LITERAL).aEdmType(datetimeOffsetInst);
    GetPTF("datetimeoffset'2002-10-10T12:00:00-05:00'").aUriLiteral("datetimeoffset'2002-10-10T12:00:00-05:00'").aKind(ExpressionKind.LITERAL).aEdmType(datetimeOffsetInst);

    GetPTF("4.5m").aUriLiteral("4.5m").aKind(ExpressionKind.LITERAL).aEdmType(decimalInst);
    GetPTF("4.5M").aUriLiteral("4.5M").aKind(ExpressionKind.LITERAL).aEdmType(decimalInst);

    GetPTF("4.5d").aUriLiteral("4.5d").aKind(ExpressionKind.LITERAL).aEdmType(doubleInst);
    GetPTF("4.5D").aUriLiteral("4.5D").aKind(ExpressionKind.LITERAL).aEdmType(doubleInst);

    GetPTF("guid'1225c695-cfb8-4ebb-aaaa-80da344efa6a'").aUriLiteral("guid'1225c695-cfb8-4ebb-aaaa-80da344efa6a'").aKind(ExpressionKind.LITERAL).aEdmType(guidInst);

    GetPTF("-32768").aUriLiteral("-32768").aKind(ExpressionKind.LITERAL).aEdmType(int16Inst);
    GetPTF("3276").aUriLiteral("3276").aKind(ExpressionKind.LITERAL).aEdmType(int16Inst);
    GetPTF("32767").aUriLiteral("32767").aKind(ExpressionKind.LITERAL).aEdmType(int16Inst);

    GetPTF("-327687").aUriLiteral("-327687").aKind(ExpressionKind.LITERAL).aEdmType(int32Inst);
    GetPTF("32768").aUriLiteral("32768").aKind(ExpressionKind.LITERAL).aEdmType(int32Inst);
    GetPTF("327686").aUriLiteral("327686").aKind(ExpressionKind.LITERAL).aEdmType(int32Inst);

    GetPTF("64L").aUriLiteral("64L").aKind(ExpressionKind.LITERAL).aEdmType(int64Inst);
    GetPTF("64l").aUriLiteral("64l").aKind(ExpressionKind.LITERAL).aEdmType(int64Inst);

    GetPTF("-123").aUriLiteral("-123").aKind(ExpressionKind.LITERAL).aEdmType(intSByte);
    GetPTF("-128").aUriLiteral("-128").aKind(ExpressionKind.LITERAL).aEdmType(intSByte);

    GetPTF("4.5f").aUriLiteral("4.5f").aKind(ExpressionKind.LITERAL).aEdmType(singleInst);

    GetPTF("'abc'").aUriLiteral("'abc'").aKind(ExpressionKind.LITERAL).aEdmType(stringInst);
    GetPTF("time'PT120S'").aUriLiteral("time'PT120S'").aKind(ExpressionKind.LITERAL).aEdmType(timeInst);

    //The EdmSimpleTypeSamples contains a well formatted list of all possible 
    //UriLiterals for SimpleTypes, instances for their Type classes and their Values in java notation
    /*
    for ( EdmSimpleTypeSamples.UriTypeValueSet [] utvSetSet : EdmSimpleTypeSamples.someAll )
    {
      for( EdmSimpleTypeSamples.UriTypeValueSet utvSet : utvSetSet)
      {
        GetPTF(utvSet.uri).aKind(ExpressionKind.LITERAL).aEdmType(utvSet.type).aUriLiteral(utvSet.uri);
      }
    }*/

  }

  ExpressionParserException GetException()
  {
    ExpressionParserException ex = new ExpressionParserException(ExpressionParserException.COMMON_ERROR);
    List<StackTraceElement> stack = new ArrayList<StackTraceElement>(Arrays.asList(ex.getStackTrace()));
    stack.remove(0);
    ex.setStackTrace(stack.toArray(new StackTraceElement[stack.size()]));
    return ex;
  }

  void LevelB() throws ExpressionParserException
  {
    ExpressionParserException ex = GetException();
    throw ex;
  }

  void LevelA() throws ExpressionParserException
  {
    try {
      LevelB();
    } catch (ExpressionParserException e) {
      e.printStackTrace();
      throw e;
      //throw new ExceptionParseExpression(ExceptionParseExpression.COMMON);
    }
  }

  //@Test
  public void testExceptionStack()
  {
    try {
      LevelA();
    } catch (ExpressionParserException e) {
      e.printStackTrace();
    }
  }

}