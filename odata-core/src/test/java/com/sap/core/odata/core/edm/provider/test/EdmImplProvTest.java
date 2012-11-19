package com.sap.core.odata.core.edm.provider.test;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.core.edm.provider.EdmImplProv;

public class EdmImplProvTest {

  private static EdmImplProv edm;

  @BeforeClass
  public static void getEdmImpl() throws Exception {
    edm = new EdmImplProv(new ScenarioEdmProvider());
  }

  @Test
  public void testDefaultEntityContainer() throws EdmException {
    assertEquals(edm.getEntityContainer("Container1"), edm.getDefaultEntityContainer());
  }
}