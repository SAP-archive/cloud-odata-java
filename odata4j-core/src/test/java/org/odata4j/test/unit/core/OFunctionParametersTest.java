package org.odata4j.test.unit.core;

import junit.framework.Assert;

import org.junit.Test;
import org.odata4j.core.OFunctionParameter;
import org.odata4j.core.OFunctionParameters;

public class OFunctionParametersTest {

  private static final String NAME = "name";
  private static final String VALUE = "value";
  private static final String HEX_VALUE = "0x76616c7565";

  @Test
  public void stringParameterToStringTest() {
    OFunctionParameter functionParameter = OFunctionParameters.create(NAME, VALUE);
    String toString = functionParameter.toString();
    Assert.assertTrue(toString.contains(NAME));
    Assert.assertTrue(toString.contains(VALUE));
  }

  @Test
  public void binaryParameterToStringTest() {
    OFunctionParameter functionParameter = OFunctionParameters.create(NAME, VALUE.getBytes());
    String toString = functionParameter.toString();
    Assert.assertTrue(toString.contains(NAME));
    Assert.assertTrue(toString.contains(HEX_VALUE));
  }
}
