package com.sap.core.odata.core.uri.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sap.core.odata.core.uri.enums.SystemQueryOption;
import com.sap.core.odata.core.uri.enums.UriType;

public class QueryOptionsEnumTest {

  @Test
  public void uriType0() {

    assertTrue(UriType.URI0.isCompatible(SystemQueryOption.$format));

    assertFalse(UriType.URI0.isCompatible(SystemQueryOption.$expand));
    assertFalse(UriType.URI0.isCompatible(SystemQueryOption.$filter));
    assertFalse(UriType.URI0.isCompatible(SystemQueryOption.$orderby));
    assertFalse(UriType.URI0.isCompatible(SystemQueryOption.$skip));
    assertFalse(UriType.URI0.isCompatible(SystemQueryOption.$top));
    assertFalse(UriType.URI0.isCompatible(SystemQueryOption.$skiptoken));
    assertFalse(UriType.URI0.isCompatible(SystemQueryOption.$inlinecount));
    assertFalse(UriType.URI0.isCompatible(SystemQueryOption.$select));
  }

  @Test
  public void uriType1() {

    assertTrue(UriType.URI1.isCompatible(SystemQueryOption.$format));
    assertTrue(UriType.URI1.isCompatible(SystemQueryOption.$expand));
    assertTrue(UriType.URI1.isCompatible(SystemQueryOption.$filter));
    assertTrue(UriType.URI1.isCompatible(SystemQueryOption.$orderby));
    assertTrue(UriType.URI1.isCompatible(SystemQueryOption.$skip));
    assertTrue(UriType.URI1.isCompatible(SystemQueryOption.$top));
    assertTrue(UriType.URI1.isCompatible(SystemQueryOption.$skiptoken));
    assertTrue(UriType.URI1.isCompatible(SystemQueryOption.$inlinecount));
    assertTrue(UriType.URI1.isCompatible(SystemQueryOption.$select));
  }

  @Test
  public void uriType2() {

    assertTrue(UriType.URI2.isCompatible(SystemQueryOption.$format));
    assertTrue(UriType.URI2.isCompatible(SystemQueryOption.$expand));
    assertTrue(UriType.URI2.isCompatible(SystemQueryOption.$filter));
    assertTrue(UriType.URI2.isCompatible(SystemQueryOption.$select));

    assertFalse(UriType.URI2.isCompatible(SystemQueryOption.$orderby));
    assertFalse(UriType.URI2.isCompatible(SystemQueryOption.$skip));
    assertFalse(UriType.URI2.isCompatible(SystemQueryOption.$top));
    assertFalse(UriType.URI2.isCompatible(SystemQueryOption.$skiptoken));
    assertFalse(UriType.URI2.isCompatible(SystemQueryOption.$inlinecount));
  }

  @Test
  public void uriType3() {

    assertTrue(UriType.URI3.isCompatible(SystemQueryOption.$format));

    assertFalse(UriType.URI3.isCompatible(SystemQueryOption.$expand));
    assertFalse(UriType.URI3.isCompatible(SystemQueryOption.$filter));
    assertFalse(UriType.URI3.isCompatible(SystemQueryOption.$orderby));
    assertFalse(UriType.URI3.isCompatible(SystemQueryOption.$skip));
    assertFalse(UriType.URI3.isCompatible(SystemQueryOption.$top));
    assertFalse(UriType.URI3.isCompatible(SystemQueryOption.$skiptoken));
    assertFalse(UriType.URI3.isCompatible(SystemQueryOption.$inlinecount));
    assertFalse(UriType.URI3.isCompatible(SystemQueryOption.$select));
  }

  @Test
  public void uriType4() {

    assertTrue(UriType.URI4.isCompatible(SystemQueryOption.$format));

    assertFalse(UriType.URI4.isCompatible(SystemQueryOption.$expand));
    assertFalse(UriType.URI4.isCompatible(SystemQueryOption.$filter));
    assertFalse(UriType.URI4.isCompatible(SystemQueryOption.$orderby));
    assertFalse(UriType.URI4.isCompatible(SystemQueryOption.$skip));
    assertFalse(UriType.URI4.isCompatible(SystemQueryOption.$top));
    assertFalse(UriType.URI4.isCompatible(SystemQueryOption.$skiptoken));
    assertFalse(UriType.URI4.isCompatible(SystemQueryOption.$inlinecount));
    assertFalse(UriType.URI4.isCompatible(SystemQueryOption.$select));
  }

  @Test
  public void uriType5() {

    assertTrue(UriType.URI5.isCompatible(SystemQueryOption.$format));

    assertFalse(UriType.URI5.isCompatible(SystemQueryOption.$expand));
    assertFalse(UriType.URI5.isCompatible(SystemQueryOption.$filter));
    assertFalse(UriType.URI5.isCompatible(SystemQueryOption.$orderby));
    assertFalse(UriType.URI5.isCompatible(SystemQueryOption.$skip));
    assertFalse(UriType.URI5.isCompatible(SystemQueryOption.$top));
    assertFalse(UriType.URI5.isCompatible(SystemQueryOption.$skiptoken));
    assertFalse(UriType.URI5.isCompatible(SystemQueryOption.$inlinecount));
    assertFalse(UriType.URI5.isCompatible(SystemQueryOption.$select));
  }

  @Test
  public void uriType6A() {

    assertTrue(UriType.URI6A.isCompatible(SystemQueryOption.$format));
    assertTrue(UriType.URI6A.isCompatible(SystemQueryOption.$expand));
    assertTrue(UriType.URI6A.isCompatible(SystemQueryOption.$filter));
    assertTrue(UriType.URI6A.isCompatible(SystemQueryOption.$select));

    assertFalse(UriType.URI6A.isCompatible(SystemQueryOption.$orderby));
    assertFalse(UriType.URI6A.isCompatible(SystemQueryOption.$skip));
    assertFalse(UriType.URI6A.isCompatible(SystemQueryOption.$top));
    assertFalse(UriType.URI6A.isCompatible(SystemQueryOption.$skiptoken));
    assertFalse(UriType.URI6A.isCompatible(SystemQueryOption.$inlinecount));
  }

  @Test
  public void uriType6B() {

    assertTrue(UriType.URI6B.isCompatible(SystemQueryOption.$format));
    assertTrue(UriType.URI6B.isCompatible(SystemQueryOption.$expand));
    assertTrue(UriType.URI6B.isCompatible(SystemQueryOption.$filter));
    assertTrue(UriType.URI6B.isCompatible(SystemQueryOption.$orderby));
    assertTrue(UriType.URI6B.isCompatible(SystemQueryOption.$skip));
    assertTrue(UriType.URI6B.isCompatible(SystemQueryOption.$top));
    assertTrue(UriType.URI6B.isCompatible(SystemQueryOption.$skiptoken));
    assertTrue(UriType.URI6B.isCompatible(SystemQueryOption.$inlinecount));
    assertTrue(UriType.URI6B.isCompatible(SystemQueryOption.$select));
  }

  @Test
  public void uriType7A() {

    assertTrue(UriType.URI7A.isCompatible(SystemQueryOption.$format));
    assertTrue(UriType.URI7A.isCompatible(SystemQueryOption.$filter));
    assertTrue(UriType.URI7A.isCompatible(SystemQueryOption.$select));

    assertFalse(UriType.URI7A.isCompatible(SystemQueryOption.$expand));
    assertFalse(UriType.URI7A.isCompatible(SystemQueryOption.$orderby));
    assertFalse(UriType.URI7A.isCompatible(SystemQueryOption.$skip));
    assertFalse(UriType.URI7A.isCompatible(SystemQueryOption.$top));
    assertFalse(UriType.URI7A.isCompatible(SystemQueryOption.$skiptoken));
    assertFalse(UriType.URI7A.isCompatible(SystemQueryOption.$inlinecount));
  }

  @Test
  public void uriType7B() {

    assertTrue(UriType.URI7B.isCompatible(SystemQueryOption.$format));
    assertTrue(UriType.URI7B.isCompatible(SystemQueryOption.$expand));
    assertTrue(UriType.URI7B.isCompatible(SystemQueryOption.$filter));
    assertTrue(UriType.URI7B.isCompatible(SystemQueryOption.$skip));
    assertTrue(UriType.URI7B.isCompatible(SystemQueryOption.$top));
    assertTrue(UriType.URI7B.isCompatible(SystemQueryOption.$skiptoken));
    assertTrue(UriType.URI7B.isCompatible(SystemQueryOption.$inlinecount));

    assertFalse(UriType.URI7B.isCompatible(SystemQueryOption.$orderby));
    assertFalse(UriType.URI7B.isCompatible(SystemQueryOption.$select));

  }

  @Test
  public void uriType8() {

    assertFalse(UriType.URI8.isCompatible(SystemQueryOption.$format));
    assertFalse(UriType.URI8.isCompatible(SystemQueryOption.$expand));
    assertFalse(UriType.URI8.isCompatible(SystemQueryOption.$filter));
    assertFalse(UriType.URI8.isCompatible(SystemQueryOption.$orderby));
    assertFalse(UriType.URI8.isCompatible(SystemQueryOption.$skip));
    assertFalse(UriType.URI8.isCompatible(SystemQueryOption.$top));
    assertFalse(UriType.URI8.isCompatible(SystemQueryOption.$skiptoken));
    assertFalse(UriType.URI8.isCompatible(SystemQueryOption.$inlinecount));
    assertFalse(UriType.URI8.isCompatible(SystemQueryOption.$select));

  }

  @Test
  public void uriType9() {

    assertFalse(UriType.URI9.isCompatible(SystemQueryOption.$format));
    assertFalse(UriType.URI9.isCompatible(SystemQueryOption.$expand));
    assertFalse(UriType.URI9.isCompatible(SystemQueryOption.$filter));
    assertFalse(UriType.URI9.isCompatible(SystemQueryOption.$orderby));
    assertFalse(UriType.URI9.isCompatible(SystemQueryOption.$skip));
    assertFalse(UriType.URI9.isCompatible(SystemQueryOption.$top));
    assertFalse(UriType.URI9.isCompatible(SystemQueryOption.$skiptoken));
    assertFalse(UriType.URI9.isCompatible(SystemQueryOption.$inlinecount));
    assertFalse(UriType.URI9.isCompatible(SystemQueryOption.$select));

  }

  @Test
  public void uriType10() {

    assertTrue(UriType.URI10.isCompatible(SystemQueryOption.$format));

    assertFalse(UriType.URI10.isCompatible(SystemQueryOption.$expand));
    assertFalse(UriType.URI10.isCompatible(SystemQueryOption.$filter));
    assertFalse(UriType.URI10.isCompatible(SystemQueryOption.$orderby));
    assertFalse(UriType.URI10.isCompatible(SystemQueryOption.$skip));
    assertFalse(UriType.URI10.isCompatible(SystemQueryOption.$top));
    assertFalse(UriType.URI10.isCompatible(SystemQueryOption.$skiptoken));
    assertFalse(UriType.URI10.isCompatible(SystemQueryOption.$inlinecount));
    assertFalse(UriType.URI10.isCompatible(SystemQueryOption.$select));

  }

  @Test
  public void uriType11() {

    assertTrue(UriType.URI11.isCompatible(SystemQueryOption.$format));

    assertFalse(UriType.URI11.isCompatible(SystemQueryOption.$expand));
    assertFalse(UriType.URI11.isCompatible(SystemQueryOption.$filter));
    assertFalse(UriType.URI11.isCompatible(SystemQueryOption.$orderby));
    assertFalse(UriType.URI11.isCompatible(SystemQueryOption.$skip));
    assertFalse(UriType.URI11.isCompatible(SystemQueryOption.$top));
    assertFalse(UriType.URI11.isCompatible(SystemQueryOption.$skiptoken));
    assertFalse(UriType.URI11.isCompatible(SystemQueryOption.$inlinecount));
    assertFalse(UriType.URI11.isCompatible(SystemQueryOption.$select));

  }

  @Test
  public void uriType12() {

    assertTrue(UriType.URI12.isCompatible(SystemQueryOption.$format));

    assertFalse(UriType.URI12.isCompatible(SystemQueryOption.$expand));
    assertFalse(UriType.URI12.isCompatible(SystemQueryOption.$filter));
    assertFalse(UriType.URI12.isCompatible(SystemQueryOption.$orderby));
    assertFalse(UriType.URI12.isCompatible(SystemQueryOption.$skip));
    assertFalse(UriType.URI12.isCompatible(SystemQueryOption.$top));
    assertFalse(UriType.URI12.isCompatible(SystemQueryOption.$skiptoken));
    assertFalse(UriType.URI12.isCompatible(SystemQueryOption.$inlinecount));
    assertFalse(UriType.URI12.isCompatible(SystemQueryOption.$select));
  }

  @Test
  public void uriType13() {

    assertTrue(UriType.URI13.isCompatible(SystemQueryOption.$format));

    assertFalse(UriType.URI13.isCompatible(SystemQueryOption.$expand));
    assertFalse(UriType.URI13.isCompatible(SystemQueryOption.$filter));
    assertFalse(UriType.URI13.isCompatible(SystemQueryOption.$orderby));
    assertFalse(UriType.URI13.isCompatible(SystemQueryOption.$skip));
    assertFalse(UriType.URI13.isCompatible(SystemQueryOption.$top));
    assertFalse(UriType.URI13.isCompatible(SystemQueryOption.$skiptoken));
    assertFalse(UriType.URI13.isCompatible(SystemQueryOption.$inlinecount));
    assertFalse(UriType.URI13.isCompatible(SystemQueryOption.$select));

  }

  @Test
  public void uriType14() {

    assertTrue(UriType.URI14.isCompatible(SystemQueryOption.$format));

    assertFalse(UriType.URI14.isCompatible(SystemQueryOption.$expand));
    assertFalse(UriType.URI14.isCompatible(SystemQueryOption.$filter));
    assertFalse(UriType.URI14.isCompatible(SystemQueryOption.$orderby));
    assertFalse(UriType.URI14.isCompatible(SystemQueryOption.$skip));
    assertFalse(UriType.URI14.isCompatible(SystemQueryOption.$top));
    assertFalse(UriType.URI14.isCompatible(SystemQueryOption.$skiptoken));
    assertFalse(UriType.URI14.isCompatible(SystemQueryOption.$inlinecount));
    assertFalse(UriType.URI14.isCompatible(SystemQueryOption.$select));
  }

  @Test
  public void uriType15() {

    assertTrue(UriType.URI15.isCompatible(SystemQueryOption.$expand));
    assertTrue(UriType.URI15.isCompatible(SystemQueryOption.$filter));
    assertTrue(UriType.URI15.isCompatible(SystemQueryOption.$orderby));
    assertTrue(UriType.URI15.isCompatible(SystemQueryOption.$skip));
    assertTrue(UriType.URI15.isCompatible(SystemQueryOption.$top));

    assertFalse(UriType.URI15.isCompatible(SystemQueryOption.$format));
    assertFalse(UriType.URI15.isCompatible(SystemQueryOption.$skiptoken));
    assertFalse(UriType.URI15.isCompatible(SystemQueryOption.$inlinecount));
    assertFalse(UriType.URI15.isCompatible(SystemQueryOption.$select));

  }

  @Test
  public void uriType16() {

    assertTrue(UriType.URI6B.isCompatible(SystemQueryOption.$expand));
    assertTrue(UriType.URI6B.isCompatible(SystemQueryOption.$filter));

    assertFalse(UriType.URI16.isCompatible(SystemQueryOption.$format));
    assertFalse(UriType.URI16.isCompatible(SystemQueryOption.$orderby));
    assertFalse(UriType.URI16.isCompatible(SystemQueryOption.$skip));
    assertFalse(UriType.URI16.isCompatible(SystemQueryOption.$top));
    assertFalse(UriType.URI16.isCompatible(SystemQueryOption.$skiptoken));
    assertFalse(UriType.URI16.isCompatible(SystemQueryOption.$inlinecount));
    assertFalse(UriType.URI16.isCompatible(SystemQueryOption.$select));

  }

  @Test
  public void uriType17() {

    assertTrue(UriType.URI17.isCompatible(SystemQueryOption.$format));
    assertTrue(UriType.URI17.isCompatible(SystemQueryOption.$filter));

    assertFalse(UriType.URI17.isCompatible(SystemQueryOption.$expand));
    assertFalse(UriType.URI17.isCompatible(SystemQueryOption.$orderby));
    assertFalse(UriType.URI17.isCompatible(SystemQueryOption.$skip));
    assertFalse(UriType.URI17.isCompatible(SystemQueryOption.$top));
    assertFalse(UriType.URI17.isCompatible(SystemQueryOption.$skiptoken));
    assertFalse(UriType.URI17.isCompatible(SystemQueryOption.$inlinecount));
    assertFalse(UriType.URI17.isCompatible(SystemQueryOption.$select));
  }

  @Test
  public void uriType50A() {

    assertTrue(UriType.URI50A.isCompatible(SystemQueryOption.$format));
    assertTrue(UriType.URI50A.isCompatible(SystemQueryOption.$select));

    assertFalse(UriType.URI50A.isCompatible(SystemQueryOption.$expand));
    assertFalse(UriType.URI50A.isCompatible(SystemQueryOption.$filter));
    assertFalse(UriType.URI50A.isCompatible(SystemQueryOption.$orderby));
    assertFalse(UriType.URI50A.isCompatible(SystemQueryOption.$skip));
    assertFalse(UriType.URI50A.isCompatible(SystemQueryOption.$top));
    assertFalse(UriType.URI50A.isCompatible(SystemQueryOption.$skiptoken));
    assertFalse(UriType.URI50A.isCompatible(SystemQueryOption.$inlinecount));
  }

  @Test
  public void uriType50B() {

    assertTrue(UriType.URI50B.isCompatible(SystemQueryOption.$format));
    assertTrue(UriType.URI50B.isCompatible(SystemQueryOption.$skip));
    assertTrue(UriType.URI50B.isCompatible(SystemQueryOption.$top));
    assertTrue(UriType.URI50B.isCompatible(SystemQueryOption.$skiptoken));
    assertTrue(UriType.URI50B.isCompatible(SystemQueryOption.$inlinecount));

    assertFalse(UriType.URI50B.isCompatible(SystemQueryOption.$expand));
    assertFalse(UriType.URI50B.isCompatible(SystemQueryOption.$filter));
    assertFalse(UriType.URI50B.isCompatible(SystemQueryOption.$orderby));
    assertFalse(UriType.URI50B.isCompatible(SystemQueryOption.$select));
  }
}
