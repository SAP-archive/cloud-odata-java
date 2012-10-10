package org.odata4j.test.integration.producer.jpa.links;

import org.junit.Test;
import org.odata4j.format.FormatType;

public class LinksJPATest extends LinksTest {

  public LinksJPATest(RuntimeFacadeType type) {
    super(type);
  }

  static {
    useJpaProducer = true;
  }

  @Test
  public void testReadDeferredAtom() throws Exception {
    testReadDeferred(FormatType.ATOM);
  }

  @Test
  public void testReadDeferredJSON() throws Exception {
    testReadDeferred(FormatType.JSON);
  }

  @Test
  public void testReadEmptyAtom() throws Exception {
    testReadEmpty(FormatType.ATOM);
  }

  @Test
  public void testReadEmptyJSON() throws Exception {
    testReadEmpty(FormatType.JSON);
  }

  @Test
  public void testReadPopulatedAtom() throws Exception {
    testReadPopulated(FormatType.ATOM);
  }

  @Test
  public void testReadPopulatedJSON() throws Exception {
    testReadPopulated(FormatType.JSON);
  }

}
