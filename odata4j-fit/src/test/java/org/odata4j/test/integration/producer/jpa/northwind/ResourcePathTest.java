package org.odata4j.test.integration.producer.jpa.northwind;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class ResourcePathTest extends NorthwindJpaProducerTest {

  public ResourcePathTest(RuntimeFacadeType type) {
    super(type);
  }

  @Before
  public void setUpClass() throws Exception {
    super.setUp(50);
  }

  @Test
  public void ResourcePathCollectionTest() {
    String inp = "ResourcePathCollectionTest";
    String uri = "Categories";
    this.utils.testJSONResult(endpointUri, uri, inp);
    this.utils.testAtomResult(endpointUri, uri, inp);
  }

  @Test
  public void ResourcePathKeyPredicateTest() {
    String inp = "ResourcePathKeyPredicateTest";
    String uri = "Categories(1)";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void ResourcePathNavPropSingleTest() {
    String inp = "ResourcePathNavPropSingleTest";
    String uri = "Categories(1)/CategoryName";
    this.utils.testJSONResult(endpointUri, uri, inp);
    this.utils.testAtomResult(endpointUri, uri, inp);
  }

  @Test
  public void ResourcePathNavPropCollectionTest() {
    String inp = "ResourcePathNavPropCollectionTest";
    String uri = "Categories(1)/Products?$filter=ProductID gt 0";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void ResourcePathComplexTypeTest() {
    String inp = "ResourcePathComplexTypeTest";
    String uri = "Categories(1)/Products(1)/Supplier/Address";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void ResourcePathCollectionCountTest() {
    String uri = "Categories/$count";
    String result = this.utils.getCount(endpointUri, uri);
    Assert.assertEquals("8", result);

    uri = "Categories/$count/";
    result = this.utils.getCount(endpointUri, uri);
    Assert.assertEquals("8", result);
  }

  @Test
  public void ResourcePathCollectionCountFilteredTest() {
    String uri = "Categories/$count?$filter=CategoryID gt 2";
    String result = this.utils.getCount(endpointUri, uri);
    Assert.assertEquals("6", result);
  }

  @Test
  public void ResourcePathCollectionCountTopTest() {
    String uri = "Categories/$count?$top=5";
    String result = this.utils.getCount(endpointUri, uri);
    Assert.assertEquals("5", result);

    uri = "Categories/$count/?$top=0";
    result = this.utils.getCount(endpointUri, uri);
    Assert.assertEquals("0", result);

    uri = "Categories/$count/?$top=100";
    result = this.utils.getCount(endpointUri, uri);
    Assert.assertEquals("8", result);
  }

  @Test
  public void ResourcePathCollectionCountSkipTest() {
    String uri = "Categories/$count?$skip=3";
    String result = this.utils.getCount(endpointUri, uri);
    Assert.assertEquals("5", result);

    uri = "Categories/$count/?$skip=100";
    result = this.utils.getCount(endpointUri, uri);
    Assert.assertEquals("0", result);

    uri = "Categories/$count/?$skip=0";
    result = this.utils.getCount(endpointUri, uri);
    Assert.assertEquals("8", result);
  }

  @Test
  public void ResourcePathNavPropCollectionCountTest() {
    String uri = "Categories(1)/Products/$count?$filter=ProductID gt 0";
    String result = this.utils.getCount(endpointUri, uri);
    Assert.assertEquals("12", result);
  }

  @Test
  public void ResourcePathNavPropCollectionCountTopTest() {
    String uri = "Categories(1)/Products/$count?$top=10&$filter=ProductID gt 0";
    String result = this.utils.getCount(endpointUri, uri);
    Assert.assertEquals("10", result);

    uri = "Categories(1)/Products/$count?$top=100&$filter=ProductID gt 0";
    ;
    result = this.utils.getCount(endpointUri, uri);
    Assert.assertEquals("12", result);

    uri = "Categories(1)/Products/$count?$top=0&$filter=ProductID gt 0";
    ;
    result = this.utils.getCount(endpointUri, uri);
    Assert.assertEquals("0", result);
  }

  @Test
  public void ResourcePathNavPropCollectionCountSkipTest() {
    String uri = "Categories(1)/Products/$count?$skip=10&$filter=ProductID gt 0";
    String result = this.utils.getCount(endpointUri, uri);
    Assert.assertEquals("2", result);

    uri = "Categories(1)/Products/$count?$skip=100&$filter=ProductID gt 0";
    ;
    result = this.utils.getCount(endpointUri, uri);
    Assert.assertEquals("0", result);

    uri = "Categories(1)/Products/$count?$skip=0&$filter=ProductID gt 0";
    ;
    result = this.utils.getCount(endpointUri, uri);
    Assert.assertEquals("12", result);
  }

}
