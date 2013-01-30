package com.sap.core.odata.core.uri.expression;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
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

/**
 * 
 * @author SAP AG
 */
public class TestSpec extends TestBase
{
  final static String aBoolean = "true";
  final static String aBinary = "binary'FA12AAA1'";
  final static String aBit = "1";
  final static String aByte = "255";
  final static String aUint7 = "123";
  final static String aDatetime = "datetime'2009-12-26T21:23:38'";
  final static String aDatetimeOffset = "datetimeoffset'2009-12-26T21:23:38Z'";
  final static String aDecimal = "4.5m";
  final static String aDouble = "4.5d";
  final static String aGuid = "guid'1225c695-cfb8-4ebb-aaaa-80da344efa6a'";
  final static String aInt16 = "-32768";
  final static String aInt32 = "327686";
  final static String aInt64 = "64L";
  final static String aIntSByte = "-123";
  final static String aSingle = "4.5f";
  final static String aString = "'abc'";
  final static String aTime = "time'PT120S'";

  final EdmBoolean boolInst = EdmBoolean.getInstance();
  final EdmBinary binaryInst = EdmBinary.getInstance();
  final Bit bitInst = Bit.getInstance();
  final EdmByte byteInst = EdmByte.getInstance();
  final Uint7 Uint7Inst = Uint7.getInstance();
  final EdmDateTime datetimeInst = EdmDateTime.getInstance();
  final EdmDateTimeOffset datetimeOffsetInst = EdmDateTimeOffset.getInstance();
  final EdmDecimal decimalInst = EdmDecimal.getInstance();
  final EdmDouble doubleInst = EdmDouble.getInstance();
  final EdmGuid guidInst = EdmGuid.getInstance();
  final EdmInt16 int16Inst = EdmInt16.getInstance();
  final EdmInt32 int32Inst = EdmInt32.getInstance();
  final EdmInt64 int64Inst = EdmInt64.getInstance();
  final EdmSByte intSByte = EdmSByte.getInstance();
  final EdmSingle singleInst = EdmSingle.getInstance();
  final EdmString stringInst = EdmString.getInstance();
  final EdmTime timeInst = EdmTime.getInstance();

  @Test
  public void testAdd()
  {

    //ADD
    GetPTF(aDecimal + " add " + aDecimal).aEdmType(decimalInst);
    GetPTF(aDouble + " add " + aDouble).aEdmType(doubleInst);
    GetPTF(aSingle + " add " + aSingle).aEdmType(singleInst);
    GetPTF(aInt64 + " add " + aInt64).aEdmType(int64Inst);
    GetPTF(aInt32 + " add " + aInt32).aEdmType(int32Inst);

    //ADD
    GetPTF(aDecimal + " sub " + aDecimal).aEdmType(decimalInst);
    GetPTF(aDouble + " sub " + aDouble).aEdmType(doubleInst);
    GetPTF(aSingle + " sub " + aSingle).aEdmType(singleInst);
    GetPTF(aInt64 + " sub " + aInt64).aEdmType(int64Inst);
    GetPTF(aInt32 + " sub " + aInt32).aEdmType(int32Inst);

    //MUL
    GetPTF(aDecimal + " mul " + aDecimal).aEdmType(decimalInst);
    GetPTF(aDouble + " mul " + aDouble).aEdmType(doubleInst);
    GetPTF(aSingle + " mul " + aSingle).aEdmType(singleInst);
    GetPTF(aInt64 + " mul " + aInt64).aEdmType(int64Inst);
    GetPTF(aInt32 + " mul " + aInt32).aEdmType(int32Inst);

    //DIV
    GetPTF(aDecimal + " div " + aDecimal).aEdmType(decimalInst);
    GetPTF(aDouble + " div " + aDouble).aEdmType(doubleInst);
    GetPTF(aSingle + " div " + aSingle).aEdmType(singleInst);
    GetPTF(aInt64 + " div " + aInt64).aEdmType(int64Inst);
    GetPTF(aInt32 + " div " + aInt32).aEdmType(int32Inst);

    //MOD
    GetPTF(aDecimal + " mod " + aDecimal).aEdmType(decimalInst);
    GetPTF(aDouble + " mod " + aDouble).aEdmType(doubleInst);
    GetPTF(aSingle + " mod " + aSingle).aEdmType(singleInst);
    GetPTF(aInt64 + " mod " + aInt64).aEdmType(int64Inst);
    GetPTF(aInt32 + " mod " + aInt32).aEdmType(int32Inst);

    //NEGATE
    GetPTF(" - " + aDecimal).aEdmType(decimalInst);
    GetPTF(" - " + aDouble).aEdmType(doubleInst);
    GetPTF(" - " + aSingle).aEdmType(singleInst);
    GetPTF(" - " + aInt64).aEdmType(int64Inst);
    GetPTF(" - " + aInt32).aEdmType(int32Inst);

    //AND
    GetPTF(aBoolean + " and " + aBoolean).aEdmType(boolInst);

    //OR
    GetPTF(aBoolean + " or " + aBoolean).aEdmType(boolInst);

    //EQ
    GetPTF(aDecimal + " eq " + aDecimal).aEdmType(boolInst);
    GetPTF(aDouble + " eq " + aDouble).aEdmType(boolInst);
    GetPTF(aSingle + " eq " + aSingle).aEdmType(boolInst);
    GetPTF(aInt64 + " eq " + aInt64).aEdmType(boolInst);
    GetPTF(aInt32 + " eq " + aInt32).aEdmType(boolInst);
    GetPTF(aString + " eq " + aString).aEdmType(boolInst);
    GetPTF(aDatetime + " eq " + aDatetime).aEdmType(boolInst);
    GetPTF(aGuid + " eq " + aGuid).aEdmType(boolInst);
    GetPTF(aGuid + " eq " + aGuid).aEdmType(boolInst);

    //NE
    GetPTF(aDecimal + " ne " + aDecimal).aEdmType(boolInst);
    GetPTF(aDouble + " ne " + aDouble).aEdmType(boolInst);
    GetPTF(aSingle + " ne " + aSingle).aEdmType(boolInst);
    GetPTF(aInt64 + " ne " + aInt64).aEdmType(boolInst);
    GetPTF(aInt32 + " ne " + aInt32).aEdmType(boolInst);
    GetPTF(aString + " ne " + aString).aEdmType(boolInst);
    GetPTF(aDatetime + " ne " + aDatetime).aEdmType(boolInst);
    GetPTF(aGuid + " ne " + aGuid).aEdmType(boolInst);
    GetPTF(aGuid + " ne " + aGuid).aEdmType(boolInst);

    //LT
    GetPTF(aDecimal + " lt " + aDecimal).aEdmType(boolInst);
    GetPTF(aDouble + " lt " + aDouble).aEdmType(boolInst);
    GetPTF(aSingle + " lt " + aSingle).aEdmType(boolInst);
    GetPTF(aInt64 + " lt " + aInt64).aEdmType(boolInst);
    GetPTF(aInt32 + " lt " + aInt32).aEdmType(boolInst);
    GetPTF(aString + " lt " + aString).aEdmType(boolInst);
    GetPTF(aDatetime + " lt " + aDatetime).aEdmType(boolInst);
    GetPTF(aGuid + " lt " + aGuid).aEdmType(boolInst);
    GetPTF(aGuid + " lt " + aGuid).aEdmType(boolInst);

    //LE
    GetPTF(aDecimal + " le " + aDecimal).aEdmType(boolInst);
    GetPTF(aDouble + " le " + aDouble).aEdmType(boolInst);
    GetPTF(aSingle + " le " + aSingle).aEdmType(boolInst);
    GetPTF(aInt64 + " le " + aInt64).aEdmType(boolInst);
    GetPTF(aInt32 + " le " + aInt32).aEdmType(boolInst);
    GetPTF(aString + " le " + aString).aEdmType(boolInst);
    GetPTF(aDatetime + " le " + aDatetime).aEdmType(boolInst);
    GetPTF(aGuid + " le " + aGuid).aEdmType(boolInst);
    GetPTF(aGuid + " le " + aGuid).aEdmType(boolInst);

    //GT
    GetPTF(aDecimal + " gt " + aDecimal).aEdmType(boolInst);
    GetPTF(aDouble + " gt " + aDouble).aEdmType(boolInst);
    GetPTF(aSingle + " gt " + aSingle).aEdmType(boolInst);
    GetPTF(aInt64 + " gt " + aInt64).aEdmType(boolInst);
    GetPTF(aInt32 + " gt " + aInt32).aEdmType(boolInst);
    GetPTF(aString + " gt " + aString).aEdmType(boolInst);
    GetPTF(aDatetime + " gt " + aDatetime).aEdmType(boolInst);
    GetPTF(aGuid + " gt " + aGuid).aEdmType(boolInst);
    GetPTF(aGuid + " gt " + aGuid).aEdmType(boolInst);

    //GE
    GetPTF(aDecimal + " ge " + aDecimal).aEdmType(boolInst);
    GetPTF(aDouble + " ge " + aDouble).aEdmType(boolInst);
    GetPTF(aSingle + " ge " + aSingle).aEdmType(boolInst);
    GetPTF(aInt64 + " ge " + aInt64).aEdmType(boolInst);
    GetPTF(aInt32 + " ge " + aInt32).aEdmType(boolInst);
    GetPTF(aString + " ge " + aString).aEdmType(boolInst);
    GetPTF(aDatetime + " ge " + aDatetime).aEdmType(boolInst);
    GetPTF(aGuid + " ge " + aGuid).aEdmType(boolInst);
    GetPTF(aGuid + " ge " + aGuid).aEdmType(boolInst);

    //NOT
    GetPTF(" not " + aBoolean).aEdmType(boolInst);

    //ISOF is not supported

    //CAST is not supported

    //BOOL lit
    GetPTF("true").aEdmType(boolInst);
    GetPTF("false").aEdmType(boolInst);

    //endsWith
    GetPTF("endswith('ABC','C')").aEdmType(boolInst);

    //indexOf
    GetPTF("indexof('ABC','B')").aEdmType(int32Inst);

    //replace not supported

    //startsWith
    GetPTF("startswith('ABC','C')").aEdmType(boolInst);

    //toLower
    GetPTF("tolower('ABC')").aEdmType(stringInst);

    //toUpper
    GetPTF("toupper('ABC')").aEdmType(stringInst);

    //trim
    GetPTF("trim('ABC')").aEdmType(stringInst);

    //substring
    GetPTF("substring('ABC'," + aInt32 + ',' + aInt32 + ")").aEdmType(stringInst);

    //subStringOf
    GetPTF("substringof('ABC','BC')").aEdmType(boolInst);

    //concat
    GetPTF("concat('ABC','BC')").aEdmType(stringInst);
    //tested more in deep in other testcases

    //length
    GetPTF("length('ABC')").aEdmType(int32Inst);

    //year
    GetPTF("year(" + aDatetime + ")").aEdmType(int32Inst);

    //month
    GetPTF("month(" + aDatetime + ")").aEdmType(int32Inst);

    //day
    GetPTF("day(" + aDatetime + ")").aEdmType(int32Inst);

    //hour
    GetPTF("hour(" + aDatetime + ")").aEdmType(int32Inst);

    //minute
    GetPTF("minute(" + aDatetime + ")").aEdmType(int32Inst);

    //second
    GetPTF("second(" + aDatetime + ")").aEdmType(int32Inst);

    //round
    GetPTF("round(" + aDecimal + ")").aEdmType(decimalInst);
    GetPTF("round(" + aDouble + ")").aEdmType(doubleInst);

    //floor
    GetPTF("floor(" + aDecimal + ")").aEdmType(decimalInst);
    GetPTF("floor(" + aDouble + ")").aEdmType(doubleInst);

    //ceiling
    GetPTF("ceiling(" + aDecimal + ")").aEdmType(decimalInst);
    GetPTF("ceiling(" + aDouble + ")").aEdmType(doubleInst);

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

      GetPTF(edm, edmEtAllTypes, "String").aEdmProperty(string).aEdmType(stringType);

      GetPTF(edm, edmEtAllTypes, "'text' eq String")
          .root().aKind(ExpressionKind.BINARY);

      GetPTF(edm, edmEtAllTypes, "Complex/String")
          .root().left().aEdmProperty(complex).aEdmType(complexType)
          .root().right().aEdmProperty(complexString).aEdmType(complexStringType)
          .root().aKind(ExpressionKind.MEMBER).aEdmType(complexStringType);

      GetPTF(edm, edmEtAllTypes, "Complex/Address/City")
          .root().aKind(ExpressionKind.MEMBER)
          .root().left().aKind(ExpressionKind.MEMBER)
          .root().left().left().aKind(ExpressionKind.PROPERTY).aEdmProperty(complex).aEdmType(complexType)
          .root().left().right().aKind(ExpressionKind.PROPERTY).aEdmProperty(complexAddress).aEdmType(complexAddressType)
          .root().left().aEdmType(complexAddressType)
          .root().right().aKind(ExpressionKind.PROPERTY).aEdmProperty(complexAddressCity).aEdmType(complexAddressCityType)
          .root().aEdmType(complexAddressCityType);

      EdmProperty boolean_ = (EdmProperty) edmEtAllTypes.getProperty("Boolean");
      EdmSimpleType boolean_Type = (EdmSimpleType) boolean_.getType();

      GetPTF(edm, edmEtAllTypes, "not Boolean")
          .aKind(ExpressionKind.UNARY)
          .aEdmType(boolean_Type)
          .right().aEdmProperty(boolean_).aEdmType(boolean_Type);

    } catch (EdmException e) {
      fail("Error in testPropertiesWithEdm:" + e.getLocalizedMessage());
      e.printStackTrace();
    }
  }

  @Test
  public void testDeepParenthesis()
  {
    GetPTF("2d").aSerialized("2d");
    GetPTF("(2d)").aSerialized("2d");
    GetPTF("((2d))").aSerialized("2d");
    GetPTF("(((2d)))").aSerialized("2d");
  }

}