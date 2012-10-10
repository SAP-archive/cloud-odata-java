package org.odata4j.test.integration.producer.jpa.northwind;

import org.junit.Before;
import org.junit.Test;

public class QueryOption50Test extends NorthwindJpaProducerTest {

  public QueryOption50Test(RuntimeFacadeType type) {
    super(type);
  }

  @Before
  public void setUpClass() throws Exception {
    super.setUp(50);
  }

  @Test
  public void systemQueryOptionFilterNotEqualTest() {
    String inp = "SystemQueryOptionFilterNotEqualTest";
    String uri = "Suppliers?$filter=Country ne 'UK'";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void resourcePathComplexFilterEqualTest() {
    String inp = "ResourcePathComplexFilterEqualTest";
    String uri = "Categories(1)/Products?$filter=Supplier/Address eq '49 Gilbert St.'";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }
}
