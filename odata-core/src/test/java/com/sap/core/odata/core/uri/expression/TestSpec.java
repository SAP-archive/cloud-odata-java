/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
  public void testMinimumSpecReq()
  {

    //ADD
    GetPTF(aDecimal + " add " + aDecimal).aEdmType(decimalInst).aSerialized("{" + aDecimal + " add " + aDecimal + "}");
    GetPTF(aDouble + " add " + aDouble).aEdmType(doubleInst).aSerialized("{" + aDouble + " add " + aDouble + "}");
    ;
    GetPTF(aSingle + " add " + aSingle).aEdmType(singleInst).aSerialized("{" + aSingle + " add " + aSingle + "}");
    GetPTF(aInt64 + " add " + aInt64).aEdmType(int64Inst).aSerialized("{" + aInt64 + " add " + aInt64 + "}");
    GetPTF(aInt32 + " add " + aInt32).aEdmType(int32Inst).aSerialized("{" + aInt32 + " add " + aInt32 + "}");

    //ADD
    GetPTF(aDecimal + " sub " + aDecimal).aEdmType(decimalInst).aSerialized("{" + aDecimal + " sub " + aDecimal + "}");
    GetPTF(aDouble + " sub " + aDouble).aEdmType(doubleInst).aSerialized("{" + aDouble + " sub " + aDouble + "}");
    GetPTF(aSingle + " sub " + aSingle).aEdmType(singleInst).aSerialized("{" + aSingle + " sub " + aSingle + "}");
    GetPTF(aInt64 + " sub " + aInt64).aEdmType(int64Inst).aSerialized("{" + aInt64 + " sub " + aInt64 + "}");
    GetPTF(aInt32 + " sub " + aInt32).aEdmType(int32Inst).aSerialized("{" + aInt32 + " sub " + aInt32 + "}");

    //MUL
    GetPTF(aDecimal + " mul " + aDecimal).aEdmType(decimalInst).aSerialized("{" + aDecimal + " mul " + aDecimal + "}");
    GetPTF(aDouble + " mul " + aDouble).aEdmType(doubleInst).aSerialized("{" + aDouble + " mul " + aDouble + "}");
    GetPTF(aSingle + " mul " + aSingle).aEdmType(singleInst).aSerialized("{" + aSingle + " mul " + aSingle + "}");
    GetPTF(aInt64 + " mul " + aInt64).aEdmType(int64Inst).aSerialized("{" + aInt64 + " mul " + aInt64 + "}");
    GetPTF(aInt32 + " mul " + aInt32).aEdmType(int32Inst).aSerialized("{" + aInt32 + " mul " + aInt32 + "}");

    //DIV
    GetPTF(aDecimal + " div " + aDecimal).aEdmType(decimalInst).aSerialized("{" + aDecimal + " div " + aDecimal + "}");
    GetPTF(aDouble + " div " + aDouble).aEdmType(doubleInst).aSerialized("{" + aDouble + " div " + aDouble + "}");
    GetPTF(aSingle + " div " + aSingle).aEdmType(singleInst).aSerialized("{" + aSingle + " div " + aSingle + "}");
    GetPTF(aInt64 + " div " + aInt64).aEdmType(int64Inst).aSerialized("{" + aInt64 + " div " + aInt64 + "}");
    GetPTF(aInt32 + " div " + aInt32).aEdmType(int32Inst).aSerialized("{" + aInt32 + " div " + aInt32 + "}");

    //MOD
    GetPTF(aDecimal + " mod " + aDecimal).aEdmType(decimalInst).aSerialized("{" + aDecimal + " mod " + aDecimal + "}");
    GetPTF(aDouble + " mod " + aDouble).aEdmType(doubleInst).aSerialized("{" + aDouble + " mod " + aDouble + "}");
    GetPTF(aSingle + " mod " + aSingle).aEdmType(singleInst).aSerialized("{" + aSingle + " mod " + aSingle + "}");
    GetPTF(aInt64 + " mod " + aInt64).aEdmType(int64Inst).aSerialized("{" + aInt64 + " mod " + aInt64 + "}");
    GetPTF(aInt32 + " mod " + aInt32).aEdmType(int32Inst).aSerialized("{" + aInt32 + " mod " + aInt32 + "}");

    //NEGATE
    GetPTF(" - " + aDecimal).aEdmType(decimalInst).aSerialized("{- " + aDecimal + "}");
    GetPTF(" - " + aDouble).aEdmType(doubleInst).aSerialized("{- " + aDouble + "}");
    GetPTF(" - " + aSingle).aEdmType(singleInst).aSerialized("{- " + aSingle + "}");
    GetPTF(" - " + aInt64).aEdmType(int64Inst).aSerialized("{- " + aInt64 + "}");
    GetPTF(" - " + aInt32).aEdmType(int32Inst).aSerialized("{- " + aInt32 + "}");

    //AND
    GetPTF(aBoolean + " and " + aBoolean).aEdmType(boolInst).aSerialized("{" + aBoolean + " and " + aBoolean + "}");

    //OR
    GetPTF(aBoolean + " or " + aBoolean).aEdmType(boolInst).aSerialized("{" + aBoolean + " or " + aBoolean + "}");

    //EQ
    GetPTF(aDecimal + " eq " + aDecimal).aEdmType(boolInst).aSerialized("{" + aDecimal + " eq " + aDecimal + "}");
    GetPTF(aDouble + " eq " + aDouble).aEdmType(boolInst).aSerialized("{" + aDouble + " eq " + aDouble + "}");
    GetPTF(aSingle + " eq " + aSingle).aEdmType(boolInst).aSerialized("{" + aSingle + " eq " + aSingle + "}");
    GetPTF(aInt64 + " eq " + aInt64).aEdmType(boolInst).aSerialized("{" + aInt64 + " eq " + aInt64 + "}");
    GetPTF(aInt32 + " eq " + aInt32).aEdmType(boolInst).aSerialized("{" + aInt32 + " eq " + aInt32 + "}");
    GetPTF(aString + " eq " + aString).aEdmType(boolInst).aSerialized("{" + aString + " eq " + aString + "}");
    GetPTF(aDatetime + " eq " + aDatetime).aEdmType(boolInst).aSerialized("{" + aDatetime + " eq " + aDatetime + "}");
    GetPTF(aGuid + " eq " + aGuid).aEdmType(boolInst).aSerialized("{" + aGuid + " eq " + aGuid + "}");
    GetPTF(aBinary + " eq " + aBinary).aEdmType(boolInst).aSerialized("{" + aBinary + " eq " + aBinary + "}");

    //NE
    GetPTF(aDecimal + " ne " + aDecimal).aEdmType(boolInst).aSerialized("{" + aDecimal + " ne " + aDecimal + "}");
    GetPTF(aDouble + " ne " + aDouble).aEdmType(boolInst).aSerialized("{" + aDouble + " ne " + aDouble + "}");
    GetPTF(aSingle + " ne " + aSingle).aEdmType(boolInst).aSerialized("{" + aSingle + " ne " + aSingle + "}");
    GetPTF(aInt64 + " ne " + aInt64).aEdmType(boolInst).aSerialized("{" + aInt64 + " ne " + aInt64 + "}");
    GetPTF(aInt32 + " ne " + aInt32).aEdmType(boolInst).aSerialized("{" + aInt32 + " ne " + aInt32 + "}");
    GetPTF(aString + " ne " + aString).aEdmType(boolInst).aSerialized("{" + aString + " ne " + aString + "}");
    GetPTF(aDatetime + " ne " + aDatetime).aEdmType(boolInst).aSerialized("{" + aDatetime + " ne " + aDatetime + "}");
    GetPTF(aGuid + " ne " + aGuid).aEdmType(boolInst).aSerialized("{" + aGuid + " ne " + aGuid + "}");
    GetPTF(aBinary + " ne " + aBinary).aEdmType(boolInst).aSerialized("{" + aBinary + " ne " + aBinary + "}");

    //LT
    GetPTF(aDecimal + " lt " + aDecimal).aEdmType(boolInst).aSerialized("{" + aDecimal + " lt " + aDecimal + "}");
    GetPTF(aDouble + " lt " + aDouble).aEdmType(boolInst).aSerialized("{" + aDouble + " lt " + aDouble + "}");
    GetPTF(aSingle + " lt " + aSingle).aEdmType(boolInst).aSerialized("{" + aSingle + " lt " + aSingle + "}");
    GetPTF(aInt64 + " lt " + aInt64).aEdmType(boolInst).aSerialized("{" + aInt64 + " lt " + aInt64 + "}");
    GetPTF(aInt32 + " lt " + aInt32).aEdmType(boolInst).aSerialized("{" + aInt32 + " lt " + aInt32 + "}");
    GetPTF(aString + " lt " + aString).aEdmType(boolInst).aSerialized("{" + aString + " lt " + aString + "}");
    GetPTF(aDatetime + " lt " + aDatetime).aEdmType(boolInst).aSerialized("{" + aDatetime + " lt " + aDatetime + "}");
    GetPTF(aGuid + " lt " + aGuid).aEdmType(boolInst).aSerialized("{" + aGuid + " lt " + aGuid + "}");
    GetPTF(aBinary + " lt " + aBinary).aEdmType(boolInst).aSerialized("{" + aBinary + " lt " + aBinary + "}");

    //LE
    GetPTF(aDecimal + " le " + aDecimal).aEdmType(boolInst).aSerialized("{" + aDecimal + " le " + aDecimal + "}");
    GetPTF(aDouble + " le " + aDouble).aEdmType(boolInst).aSerialized("{" + aDouble + " le " + aDouble + "}");
    GetPTF(aSingle + " le " + aSingle).aEdmType(boolInst).aSerialized("{" + aSingle + " le " + aSingle + "}");
    GetPTF(aInt64 + " le " + aInt64).aEdmType(boolInst).aSerialized("{" + aInt64 + " le " + aInt64 + "}");
    GetPTF(aInt32 + " le " + aInt32).aEdmType(boolInst).aSerialized("{" + aInt32 + " le " + aInt32 + "}");
    GetPTF(aString + " le " + aString).aEdmType(boolInst).aSerialized("{" + aString + " le " + aString + "}");
    GetPTF(aDatetime + " le " + aDatetime).aEdmType(boolInst).aSerialized("{" + aDatetime + " le " + aDatetime + "}");
    GetPTF(aGuid + " le " + aGuid).aEdmType(boolInst).aSerialized("{" + aGuid + " le " + aGuid + "}");
    GetPTF(aBinary + " le " + aBinary).aEdmType(boolInst).aSerialized("{" + aBinary + " le " + aBinary + "}");

    //GT
    GetPTF(aDecimal + " gt " + aDecimal).aEdmType(boolInst).aSerialized("{" + aDecimal + " gt " + aDecimal + "}");
    GetPTF(aDouble + " gt " + aDouble).aEdmType(boolInst).aSerialized("{" + aDouble + " gt " + aDouble + "}");
    GetPTF(aSingle + " gt " + aSingle).aEdmType(boolInst).aSerialized("{" + aSingle + " gt " + aSingle + "}");
    GetPTF(aInt64 + " gt " + aInt64).aEdmType(boolInst).aSerialized("{" + aInt64 + " gt " + aInt64 + "}");
    GetPTF(aInt32 + " gt " + aInt32).aEdmType(boolInst).aSerialized("{" + aInt32 + " gt " + aInt32 + "}");
    GetPTF(aString + " gt " + aString).aEdmType(boolInst).aSerialized("{" + aString + " gt " + aString + "}");
    GetPTF(aDatetime + " gt " + aDatetime).aEdmType(boolInst).aSerialized("{" + aDatetime + " gt " + aDatetime + "}");
    GetPTF(aGuid + " gt " + aGuid).aEdmType(boolInst).aSerialized("{" + aGuid + " gt " + aGuid + "}");
    GetPTF(aBinary + " gt " + aBinary).aEdmType(boolInst).aSerialized("{" + aBinary + " gt " + aBinary + "}");

    //GE
    GetPTF(aDecimal + " ge " + aDecimal).aEdmType(boolInst).aSerialized("{" + aDecimal + " ge " + aDecimal + "}");
    GetPTF(aDouble + " ge " + aDouble).aEdmType(boolInst).aSerialized("{" + aDouble + " ge " + aDouble + "}");
    GetPTF(aSingle + " ge " + aSingle).aEdmType(boolInst).aSerialized("{" + aSingle + " ge " + aSingle + "}");
    GetPTF(aInt64 + " ge " + aInt64).aEdmType(boolInst).aSerialized("{" + aInt64 + " ge " + aInt64 + "}");
    GetPTF(aInt32 + " ge " + aInt32).aEdmType(boolInst).aSerialized("{" + aInt32 + " ge " + aInt32 + "}");
    GetPTF(aString + " ge " + aString).aEdmType(boolInst).aSerialized("{" + aString + " ge " + aString + "}");
    GetPTF(aDatetime + " ge " + aDatetime).aEdmType(boolInst).aSerialized("{" + aDatetime + " ge " + aDatetime + "}");
    GetPTF(aGuid + " ge " + aGuid).aEdmType(boolInst).aSerialized("{" + aGuid + " ge " + aGuid + "}");
    GetPTF(aBinary + " ge " + aBinary).aEdmType(boolInst).aSerialized("{" + aBinary + " ge " + aBinary + "}");

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
  public void testDeepParenthesis()
  {
    GetPTF("2d").aSerialized("2d");
    GetPTF("(2d)").aSerialized("2d");
    GetPTF("((2d))").aSerialized("2d");
    GetPTF("(((2d)))").aSerialized("2d");
  }

}
