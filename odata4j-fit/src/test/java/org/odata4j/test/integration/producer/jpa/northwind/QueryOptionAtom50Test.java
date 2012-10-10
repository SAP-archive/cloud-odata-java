package org.odata4j.test.integration.producer.jpa.northwind;

import org.junit.Before;
import org.junit.Test;

public class QueryOptionAtom50Test extends NorthwindJpaProducerTest {

  public QueryOptionAtom50Test(RuntimeFacadeType type) {
    super(type);
  }

  @Before
  public void setUp() {
    super.setUp(50);
  }

  @Test
  // http://services.odata.org/northwind/Northwind.svc/ is using maxResult > 20 here?
  public void systemQueryOptionFilterNotEqualTest() {
    String inp = "SystemQueryOptionFilterNotEqualTest";
    String uri = "Suppliers?$filter=Country ne 'UK'";
    this.utils.testAtomResult(endpointUri, uri, inp);
  }

}
