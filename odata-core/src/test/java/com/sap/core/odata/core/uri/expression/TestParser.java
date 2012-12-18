package com.sap.core.odata.core.uri.expression;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.rt.RuntimeDelegate;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.FilterParserException;
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
import com.sap.core.odata.core.uri.expression.FilterParserImpl;
import com.sap.core.odata.core.uri.expression.FilterParserInternalError;
import com.sap.core.odata.testutils.mocks.TecEdmInfo;
import com.sap.core.odata.testutils.mocks.TechnicalScenarioEdmProvider;

public class TestParser {

  Edm edm = null;
  TecEdmInfo edmInfo = null;

  public TestParser()
  {
    edm = RuntimeDelegate.createEdm(new TechnicalScenarioEdmProvider());
    edmInfo = new TecEdmInfo(edm);
  }

  @Test
  public void TestProperties()
  {
    GetPTF("sven").aSerialized("sven").aKind(ExpressionKind.PROPERTY);
    GetPTF("sven1 add sven2").aSerialized("{sven1 add sven2}")
        .aKind(ExpressionKind.BINARY)
        .root().left().aKind(ExpressionKind.PROPERTY).aUriLiteral("sven1")
        .root().right().aKind(ExpressionKind.PROPERTY).aUriLiteral("sven2");
  }

  @Test
  public void TestDeepProperties()
  {
    GetPTF("a/b").aSerialized("{a/b}").aKind(ExpressionKind.MEMBER);
    GetPTF("a/b/c").aSerialized("{{a/b}/c}").aKind(ExpressionKind.MEMBER);
  }

  @Test
  public void TestPropertiesWithEdm()
  {
    EdmEntityType edmType = edmInfo.getEtAllTypes();

    GetPTF(edm, edmType, "String");
    GetPTF(edm, edmType, "Complex/String");
    //GetPTF(edm, edmType,"Complex/Address/City");
  }

  @Test
  public void TestSimpleMethod()
  {
    GetPTF("startswith('Test','Te')").aSerialized("{startswith('Test','Te')}");
    //add test for concat
    GetPTF("startswith('Test','Te')").aSerialized("{startswith('Test','Te')}");
  }

  @Test
  public void TestSimpleSameBinary()
  {
    GetPTF("1d add 2d").aSerialized("{1d add 2d}");
    GetPTF("1d div 2d").aSerialized("{1d div 2d}");

    GetPTF("1d add 2d").aSerialized("{1d add 2d}")
        .aKind(ExpressionKind.BINARY)
        .root().left().aKind(ExpressionKind.LITERAL)
        .root().right().aKind(ExpressionKind.LITERAL);

  }

  @Test
  public void TestSimpleSameBinaryBinary()
  {
    GetPTF("1d add 2d add 3d").aSerialized("{{1d add 2d} add 3d}");
    GetPTF("1d div 2d div 3d").aSerialized("{{1d div 2d} div 3d}");
  }

  @Test
  public void TestSimpleSameBinaryBinaryPriority()
  {
    GetPTF("1d add 2d div 3d").aSerialized("{1d add {2d div 3d}}");
    GetPTF("1d div 2d add 3d").aSerialized("{{1d div 2d} add 3d}");
  }

  @Test
  public void TestSimpleSameBinaryBinaryBinaryPriority()
  {
    GetPTF("1d add 2d add 3d add 4d").aSerialized("{{{1d add 2d} add 3d} add 4d}");
    GetPTF("1d add 2d add 3d div 4d").aSerialized("{{1d add 2d} add {3d div 4d}}");
    GetPTF("1d add 2d div 3d add 4d").aSerialized("{{1d add {2d div 3d}} add 4d}");
    GetPTF("1d add 2d div 3d div 4d").aSerialized("{1d add {{2d div 3d} div 4d}}");
    GetPTF("1d div 2d add 3d add 4d").aSerialized("{{{1d div 2d} add 3d} add 4d}");
    GetPTF("1d div 2d add 3d div 4d").aSerialized("{{1d div 2d} add {3d div 4d}}");
    GetPTF("1d div 2d div 3d add 4d").aSerialized("{{{1d div 2d} div 3d} add 4d}");
    GetPTF("1d div 2d div 3d div 4d").aSerialized("{{{1d div 2d} div 3d} div 4d}");
    //XXX maybe add generator for more deepness
  }

  @Test
  public void TestDeepParenthesis()
  {
    GetPTF("2d").aSerialized("2d");
    GetPTF("(2d)").aSerialized("2d");
    GetPTF("((2d))").aSerialized("2d");
    GetPTF("(((2d)))").aSerialized("2d");
  }

  @Test
  public void TestParenthesisWithBinaryBinary()
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
  public void TestSimpleUnaryOperator()
  {
    GetPTF("not true").aSerialized("{not true}");
    GetPTF("- 2d").aSerialized("{- 2d}");
  }

  @Test
  public void TestDeepUnaryOperator()
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
  public void TestMixedUnaryOperators()
  {
    GetPTF("not - true").aSerialized("{not {- true}}");
    GetPTF("- not true").aSerialized("{- {not true}}");
  }

  @Test
  public void TestSinglePlainLiterals()
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

    GetPTF("datetime'2009-12-26T21%3A23%3A38'").aUriLiteral("datetime'2009-12-26T21%3A23%3A38'").aKind(ExpressionKind.LITERAL).aEdmType(datetimeInst);
    GetPTF("datetime'2009-12-26T21%3A23%3A38'").aUriLiteral("datetime'2009-12-26T21%3A23%3A38'").aKind(ExpressionKind.LITERAL).aEdmType(datetimeInst);

    GetPTF("datetimeoffset'2009-12-26T21%3A23%3A38Z'").aUriLiteral("datetimeoffset'2009-12-26T21%3A23%3A38Z'").aKind(ExpressionKind.LITERAL).aEdmType(datetimeOffsetInst);
    GetPTF("datetimeoffset'2002-10-10T12%3A00%3A00-05%3A00'").aUriLiteral("datetimeoffset'2002-10-10T12%3A00%3A00-05%3A00'").aKind(ExpressionKind.LITERAL).aEdmType(datetimeOffsetInst);

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
    GetPTF("time'P120D'").aUriLiteral("time'P120D'").aKind(ExpressionKind.LITERAL).aEdmType(timeInst);

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

  public void TestSinglePlainLiteralsABAP()
  {
    //---
    //Checks from ABAP
    //---
    //GetPTF("X'1234567890ABCDEF'").aKind(ExpressionKind.LITERAL);

    /*    
        lo_simple_type = /iwcor/cl_ds_edm_simple_type=>binary( ).
        lcl_helper=>veri_type( iv_expression = `X'1234567890ABCDEF'` io_expected_type = lo_simple_type ).

        lo_simple_type = /iwcor/cl_ds_edm_simple_type=>boolean( ).
        lcl_helper=>veri_type( iv_expression = `true` io_expected_type = lo_simple_type ).

        lo_simple_type = /iwcor/cl_ds_edm_simple_type=>get_instance( iv_name = 'Bit' ).
        lcl_helper=>veri_type( iv_expression = `1` io_expected_type = lo_simple_type ).

        lo_simple_type = /iwcor/cl_ds_edm_simple_type=>get_instance( iv_name = 'Bit' ).
        lcl_helper=>veri_type( iv_expression = `0` io_expected_type = lo_simple_type ).

        lo_simple_type = /iwcor/cl_ds_edm_simple_type=>get_instance( iv_name = 'UInt7' ).
        lcl_helper=>veri_type( iv_expression = `123` io_expected_type = lo_simple_type ).

        lo_simple_type = /iwcor/cl_ds_edm_simple_type=>byte( ).
        lcl_helper=>veri_type( iv_expression = `130` io_expected_type = lo_simple_type ).

        lo_simple_type = /iwcor/cl_ds_edm_simple_type=>datetime( ).
        lcl_helper=>veri_type( iv_expression = `datetime'2011-07-31T23:30:59'` io_expected_type = lo_simple_type ).

        lo_simple_type = /iwcor/cl_ds_edm_simple_type=>datetimeoffset( ).
        lcl_helper=>veri_type( iv_expression = `datetimeoffset'2002-10-10T12:00:00-05:00'` io_expected_type = lo_simple_type ).

        lo_simple_type = /iwcor/cl_ds_edm_simple_type=>decimal( ).
        lcl_helper=>veri_type( iv_expression = `1.1M` io_expected_type = lo_simple_type ).

        lo_simple_type = /iwcor/cl_ds_edm_simple_type=>double( ).
        lcl_helper=>veri_type( iv_expression = `1.1D` io_expected_type = lo_simple_type ).

        lo_simple_type = /iwcor/cl_ds_edm_simple_type=>double( ).
        lcl_helper=>veri_type( iv_expression = `1.1E+02D` io_expected_type = lo_simple_type ).

        lo_simple_type = /iwcor/cl_ds_edm_simple_type=>guid( ).
        lcl_helper=>veri_type( iv_expression = `guid'12345678-1234-1234-1234-123456789012'` io_expected_type = lo_simple_type ).

        lo_simple_type = /iwcor/cl_ds_edm_simple_type=>float( ).
        lcl_helper=>veri_type( iv_expression = `1.1F` io_expected_type = lo_simple_type ).

        lo_simple_type = /iwcor/cl_ds_edm_simple_type=>int16( ).
        lcl_helper=>veri_type( iv_expression = `12345` io_expected_type = lo_simple_type ).

        lo_simple_type = /iwcor/cl_ds_edm_simple_type=>int32( ).
        lcl_helper=>veri_type( iv_expression = `1234512345` io_expected_type = lo_simple_type ).

        lo_simple_type = /iwcor/cl_ds_edm_simple_type=>int64( ).
        lcl_helper=>veri_type( iv_expression = `12345L` io_expected_type = lo_simple_type ).

        lo_simple_type = /iwcor/cl_ds_edm_simple_type=>sbyte( ).
        lcl_helper=>veri_type( iv_expression = `-12` io_expected_type = lo_simple_type ).

        lo_simple_type = /iwcor/cl_ds_edm_simple_type=>single( ).
        lcl_helper=>veri_type( iv_expression = `1.1F` io_expected_type = lo_simple_type ).

        lo_simple_type = /iwcor/cl_ds_edm_simple_type=>string( ).
        lcl_helper=>veri_type( iv_expression = `'TEST'` io_expected_type = lo_simple_type ).

        lo_simple_type = /iwcor/cl_ds_edm_simple_type=>time( ).
        lcl_helper=>veri_type( iv_expression = `time'P1998Y02M01D'` io_expected_type = lo_simple_type ).

        lo_simple_type = /iwcor/cl_ds_edm_simple_type=>time( ).
        lcl_helper=>veri_type( iv_expression = `time'P1998Y02M01DT23H14M33S'` io_expected_type = lo_simple_type ).
    */

  }

  void Errors()
  {
    //GetPTF("-(-(- 2d)))").aSerialized("{-{-{- 2d}}}");
  }

  FilterParserException GetException()
  {
    FilterParserException ex = new FilterParserException(FilterParserException.COMMON_ERROR);
    List<StackTraceElement> stack = new ArrayList<StackTraceElement>(Arrays.asList(ex.getStackTrace()));
    stack.remove(0);
    ex.setStackTrace(stack.toArray(new StackTraceElement[stack.size()]));
    return ex;
  }

  void LevelB() throws FilterParserException
  {
    FilterParserException ex = GetException();
    throw ex;
  }

  void LevelA() throws FilterParserException
  {
    try {
      LevelB();
    } catch (FilterParserException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      throw e;
      //throw new ExceptionParseExpression(ExceptionParseExpression.COMMON);
    }
  }

  //@Test
  public void DynTest()
  {
    try {
      LevelA();
    } catch (FilterParserException e) {
      e.printStackTrace();
    }
  }

  static public ParserTool GetPTF(String expression)
  {
    try {
      FilterParserImpl parser = new FilterParserImpl(null, null);
      FilterExpression root = parser.parseExpression(expression);
      return new ParserTool(expression, root);
    } catch (FilterParserInternalError e) {
      fail("Error in parser" + e.getLocalizedMessage());
    } catch (FilterParserException e) {
      fail("Error in parser" + e.getLocalizedMessage());
    }
    return null;
  }

  static public ParserTool GetPTF(Edm edm, EdmEntityType resourceEntityType, String expression) {
    try {
      FilterParserImpl parser = new FilterParserImpl(edm, resourceEntityType);
      FilterExpression root = parser.parseExpression(expression);
      return new ParserTool(expression, root);
    } catch (FilterParserInternalError e) {
      fail("Error in parser" + e.getLocalizedMessage());
    } catch (FilterParserException e) {
      fail("Error in parser" + e.getLocalizedMessage());
    }
    return null;

  }

}