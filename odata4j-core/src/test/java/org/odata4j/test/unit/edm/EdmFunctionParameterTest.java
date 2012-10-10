package org.odata4j.test.unit.edm;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.odata4j.edm.EdmFunctionParameter;
import org.odata4j.edm.EdmSimpleType;

public class EdmFunctionParameterTest {
  private static final Boolean NULLABLE = true;
  private static final Integer MAX_LENGTH = 2;
  private static final Integer PRECISION = 3;
  private static final Integer SCALE = 2;

  @Test
  public void edmFunctionParameterNullable() {
    EdmFunctionParameter.Builder builder = EdmFunctionParameter.newBuilder();
    EdmFunctionParameter parameter = builder.setNullable(NULLABLE).setType(EdmSimpleType.STRING).build();
    assertEquals(NULLABLE, parameter.isNullable());
  }

  @Test
  public void edmFunctionParameterMaxLength() {
    EdmFunctionParameter.Builder builder = EdmFunctionParameter.newBuilder();
    EdmFunctionParameter parameter = builder.setType(EdmSimpleType.INT32).setMaxLength(MAX_LENGTH).build();
    assertEquals(MAX_LENGTH, parameter.getMaxLength());
  }

  @Test
  public void edmFunctionParameterPrecision() {
    EdmFunctionParameter.Builder builder = EdmFunctionParameter.newBuilder();
    EdmFunctionParameter parameter = builder.setType(EdmSimpleType.DECIMAL).setPrecision(PRECISION).build();
    assertEquals(PRECISION, parameter.getPrecision());
  }

  @Test
  public void edmFunctionParameterScale() {
    EdmFunctionParameter.Builder builder = EdmFunctionParameter.newBuilder();
    EdmFunctionParameter parameter = builder.setType(EdmSimpleType.DECIMAL).setScale(SCALE).build();
    assertEquals(SCALE, parameter.getScale());
  }
}