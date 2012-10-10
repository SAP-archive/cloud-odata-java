package org.odata4j.test.integration.producer.jpa.northwind;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class QueryOptionTest extends NorthwindJpaProducerTest {

  public QueryOptionTest(RuntimeFacadeType type) {
    super(type);
  }

  @Before
  public void setUp() {
    super.setUp(20);
  }

  @After
  public void tearDown() throws Exception {
    if (server != null) {
      server.stop();
    }

    if (emf != null) {
      emf.close();
    }
  }

  @Test
  public void SystemQueryOptionOrderByTest() {
    String inp = "SystemQueryOptionOrderByTest";
    String uri = "Products?$orderby=ProductID";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionOrderByDescTest() {
    String inp = "SystemQueryOptionOrderByDescTest";
    String uri = "Products?$orderby=ProductID desc";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionTopTest() {
    String inp = "SystemQueryOptionTopTest";
    String uri = "Products?$top=5";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionOrderByTopTest() {
    String inp = "SystemQueryOptionOrderByTopTest";
    String uri = "Products?$top=5&$orderby=ProductName desc";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionSkipTest() {
    String inp = "SystemQueryOptionSkipTest";
    String uri = "Categories(1)/Products?$skip=2";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionOrderBySkipTest() {
    String inp = "SystemQueryOptionOrderBySkipTest";
    String uri = "Products?$skip=2&$top=2&$orderby=ProductName";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  // @Test
  // public void SystemQueryOptionTop5000Test() {
  // String inp = "SystemQueryOptionTop5000Test";
  // String uri = "Products?$top=5000";
  // TestUtils.testJSONResult(endpointUri, uri, inp);
  // }

  @Test
  public void SystemQueryOptionSkipTokenTest() {
    String inp = "SystemQueryOptionSkipTokenTest";
    String uri = "Customers?$top=5&$skiptoken='ANATR'";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionSkipTokenComplexKeyTest() {
    String inp = "SystemQueryOptionSkipTokenComplexKeyTest";
    String uri = "Order_Details?$top=5&$skiptoken=OrderID=10248,ProductID=11";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionSkipTokenWithOrderByTest() {
    String inp = "SystemQueryOptionSkipTokenWithOrderByTest";
    String uri = "Products?$orderby=SupplierID desc, ProductName&$skiptoken=20,'Gula Malacca',44";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionFilterEqualNullTest() {
    String inp = "SystemQueryOptionFilterEqualNullTest";
    String uri = "Suppliers?$filter=Region eq null";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionFilterNotEqualNullTest() {
    String inp = "SystemQueryOptionFilterNotEqualNullTest";
    String uri = "Suppliers?$filter=Region ne null";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionFilterEqualTest() {
    String inp = "SystemQueryOptionFilterEqualTest";
    String uri = "Suppliers?$filter=Country eq 'Brazil'";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionFilterGreaterThanTest() {
    String inp = "SystemQueryOptionFilterGreaterThanTest";
    String uri = "Products?$top=20&$filter=UnitPrice gt 20";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionFilterGreaterThanOrEqualTest() {
    String inp = "SystemQueryOptionFilterGreaterThanOrEqualTest";
    String uri = "Products?$filter=UnitPrice ge 10";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionFilterLessThanOrEqualTest() {
    String inp = "SystemQueryOptionFilterLessThanOrEqualTest";
    String uri = "Products?$top=20&$filter=UnitPrice le 100";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionFilterLessThanTest() {
    String inp = "SystemQueryOptionFilterLessThanTest";
    String uri = "Products?$filter=UnitPrice lt 20";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionFilterLogicalAndTest() {
    String inp = "SystemQueryOptionFilterLogicalAndTest";
    String uri =
        "Products?$top=20&$filter=UnitPrice le 200 and UnitPrice gt 3.5f";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionFilterLogicalOrTest() {
    String inp = "SystemQueryOptionFilterLogicalOrTest";
    String uri = "Products?$filter=UnitPrice le 3.5f or UnitPrice gt 200";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionFilterGroupingLogicalAndTest() {
    String inp = "SystemQueryOptionFilterGroupingLogicalAndTest";
    String uri =
        "Products?$top=10&$filter=%28UnitPrice%20gt%205%29%20and%20%28UnitPrice%20lt%2020%29";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionFilterAdditionTest() {
    String inp = "SystemQueryOptionFilterAdditionTest";
    String uri = "Products?$filter=UnitPrice add 5 gt 10";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionFilterSubtractionTest() {
    String inp = "SystemQueryOptionFilterSubtractionTest";
    String uri = "Products?$filter=UnitPrice sub 5 gt 10";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionFilterMultiplicationTest() {
    String inp = "SystemQueryOptionFilterMultiplicationTest";
    String uri = "Products?$filter=UnitPrice mul 2 gt 2000";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionFilterDivisionTest() {
    String inp = "SystemQueryOptionFilterDivisionTest";
    String uri = "Products?$filter=UnitPrice div 2 gt 4";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionFilterModuloTest() {
    String inp = "SystemQueryOptionFilterModuloTest";
    String uri = "Products?$filter=ProductID mod 8 eq 2";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionFilterGroupingArithmeticSubTest() {
    String inp = "SystemQueryOptionFilterGroupingArithmeticSubTest";
    String uri = "Products?$filter=(UnitPrice sub 5) gt 10";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionFilterLogicalNotTest() {
    String inp = "SystemQueryOptionFilterLogicalNotTest";
    String uri = "Products?$filter=not endswith(QuantityPerUnit,'bags')";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionFilterLogicalNotTest2() {
    String inp = "SystemQueryOptionFilterLogicalNotTest2";
    String uri = "Products?$filter=not (Discontinued eq 1 or UnitsOnOrder le 70)";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionFilterBoolSubstringOfTest() {
    String inp = "SystemQueryOptionFilterBoolSubstringOfTest";
    String uri =
        "Customers?$filter=substringof('Alfreds', CompanyName) eq true";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionFilterBoolEndswithTest() {
    String inp = "SystemQueryOptionFilterBoolEndswithTest";
    String uri =
        "Customers?$filter=endswith(CompanyName, 'Futterkiste') eq true";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionFilterBoolStartswithTest() {
    String inp = "SystemQueryOptionFilterBoolStartswithTest";
    String uri = "Customers?$filter=startswith(CompanyName, 'Alfr') eq true";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionFilterIntLengthTest() {
    String inp = "SystemQueryOptionFilterIntLengthTest";
    String uri = "Customers?$filter=length(CompanyName) eq 19";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionFilterIntIndexofTest() {
    String inp = "SystemQueryOptionFilterIntIndexofTest";
    String uri = "Customers?$filter=indexof(CompanyName, 'lfreds') eq 1";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionFilterStringReplaceTest() {
    String inp = "SystemQueryOptionFilterStringReplaceTest";
    String uri =
        "Customers?$filter=replace(CompanyName, ' ', '') eq 'AlfredsFutterkiste'";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionFilterStringSubstringTest() {
    String inp = "SystemQueryOptionFilterStringSubstringTest";
    String uri =
        "Customers?$filter=substring(CompanyName, 1) eq 'lfreds Futterkiste'";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionFilterStringSubstring2Test() {
    String inp = "SystemQueryOptionFilterStringSubstring2Test";
    String uri = "Customers?$filter=substring(CompanyName, 1, 2) eq 'lf'";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionFilterStringToLowerTest() {
    String inp = "SystemQueryOptionFilterStringToLowerTest";
    String uri =
        "Customers?$filter=tolower(CompanyName) eq 'alfreds futterkiste'";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionFilterStringToupperTest() {
    String inp = "SystemQueryOptionFilterStringToupperTest";
    String uri =
        "Customers?$filter=toupper(CompanyName) eq 'ALFREDS FUTTERKISTE'";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionFilterStringTrimTest() {
    String inp = "SystemQueryOptionFilterStringTrimTest";
    String uri =
        "Customers?$filter=trim(CompanyName) eq 'Alfreds Futterkiste'";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionFilterStringConcatTest() {
    String inp = "SystemQueryOptionFilterStringConcatTest";
    String uri =
        "Customers?$filter=concat(concat(City, ', '), Country) eq 'Berlin, Germany'";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  /*
   * Note on the All/Any tests:
   * - They are just placeholders until someone implements this in JPA producer.
   * - The expected results files are checked in.  I got them by hosting a
   *   V3 OData service built using the June11 EF CTP
   */

  @Ignore("TODO")
  @SuppressWarnings("unused")
  @Test
  public void SystemQueryOptionFilterAnyTest() {
    // get all Customers who have 1 or more Orders
    // some SQL to run against Northwind:
    // select count(*) from Customers -- 91 customers
    // select distinct c.CustomerID  from Customers c left outer join Orders o on c.CustomerID = o.CustomerID
    //    where o.OrderID is not null -- 'FISSA', 'PARIS' are the two customers w/out Orders
    String uri =
        "Customers?$filter=Orders/any()&$select=CustomerID";
  }

  @Ignore("TODO")
  @SuppressWarnings("unused")
  @Test
  public void SystemQueryOptionFilterAllTest() {
    // all Orders whose every associated Order_Details has a Quantity >= 60
    /*
     * select o.OrderID, count(od.Quantity) as thecount,  min(od.Quantity) as themin, avg(od.Quantity) as theavg, max(od.Quantity) as themax
       from Orders as o inner join [Order Details] od on o.OrderID = od.OrderID
       group by o.OrderID
       having min(od.Quantity) >= 60
     *
     * Interesting: that query shows 7 Orders that meet the criteria.  However, the .NET
     * data service returns 8, OrderID=11078 is included even though it has 0 Order_Details!  Bug!
     */
    String uri =
        "Orders?$filter=Order_Details/all(od:od/Quantity ge 60)&$select=OrderID";
  }

  @Ignore("TODO")
  @SuppressWarnings("unused")
  @Test
  public void SystemQueryOptionFilterAnyPredicateTest() {
    // all Orders who have one or more associated Order_Details objects with Quantity < 3

    /*
     * select o.OrderID, count(od.Quantity) as thecount,  min(od.Quantity) as themin, avg(od.Quantity) as theavg, max(od.Quantity) as themax
       from Orders as o inner join [Order Details] od on o.OrderID = od.OrderID
       group by o.OrderID
       having min(od.Quantity) < 3
     */
    String uri =
        "Orders?$filter=Order_Details/any(od:od/Quantity lt 3)&$select=OrderID";
  }

  @Ignore("TODO")
  @SuppressWarnings("unused")
  @Test
  public void SystemQueryOptionFilterAnyAllTest() {

    // all Customers who have Orders whose every associated Order_Details has a Quantity >= 60
    /*
     * select distinct c.CustomerID  from Customers c left outer join Orders o on c.CustomerID = o.CustomerID
       where o.OrderID in (
           select o2.OrderID
           from Orders as o2 inner join [Order Details] od on o2.OrderID = od.OrderID
           group by o2.OrderID
           having min(od.Quantity) >= 60 )
       order by c.CustomerID
     */
    String uri =
        "Customers?$filter=Orders/any(o:o/Order_Details/all(od:od/Quantity ge 60))&$select=CustomerID";
  }

  // TODO: date time

  // @Test
  // public void SystemQueryOptionFilterIntDayTest() {
  // String inp = "SystemQueryOptionFilterIntDayTest";
  // String uri = "Employees?$filter=day(BirthDate) eq 8";
  // NorthwindTestUtils.testJSONResult(endpointUri, uri, inp);
  // }

  // @Test
  // public void SystemQueryOptionFilterIntHourTest() {
  // String inp = "SystemQueryOptionFilterIntHourTest";
  // String uri = "Employees?$filter=hour(BirthDate) eq 0";
  // NorthwindTestUtils.testJSONResult(endpointUri, uri, inp);
  // }
  //
  // @Test
  // public void SystemQueryOptionFilterIntMinuteTest() {
  // String inp = "SystemQueryOptionFilterIntMinuteTest";
  // String uri = "Employees?$filter=minute(BirthDate) eq 0";
  // NorthwindTestUtils.testJSONResult(endpointUri, uri, inp);
  // }
  //
  // @Test
  // public void SystemQueryOptionFilterIntMonthTest() {
  // String inp = "SystemQueryOptionFilterIntMonthTest";
  // String uri = "Employees?$filter=month(BirthDate) eq 12";
  // NorthwindTestUtils.testJSONResult(endpointUri, uri, inp);
  // }
  //
  // @Test
  // public void SystemQueryOptionFilterIntSecondTest() {
  // String inp = "SystemQueryOptionFilterIntSecondTest";
  // String uri = "Employees?$filter=second(BirthDate) eq 0";
  // NorthwindTestUtils.testJSONResult(endpointUri, uri, inp);
  // }
  //
  // @Test
  // public void SystemQueryOptionFilterIntYearTest() {
  // String inp = "SystemQueryOptionFilterIntYearTest";
  // String uri = "Employees?$filter=year(BirthDate) eq 1948";
  // NorthwindTestUtils.testJSONResult(endpointUri, uri, inp);
  // }

  // TODO: numeric

  // @Test
  // public void SystemQueryOptionFilterRoundTest() {
  // String inp = "SystemQueryOptionFilterRoundTest";
  // String uri = "Orders?$filter=round(Freight) eq 32";
  // NorthwindTestUtils.testJSONResult(endpointUri, uri, inp);
  // }

  // @Test
  // public void SystemQueryOptionFilterDecimalFloorTest() {
  // String inp = "SystemQueryOptionFilterDecimalFloorTest";
  // String uri = "Orders?$filter=floor(Freight) eq 32";
  // NorthwindTestUtils.testJSONResult(endpointUri, uri, inp);
  // }
  //
  // @Test
  // public void SystemQueryOptionFilterDoubleCeilingTest() {
  // String inp = "SystemQueryOptionFilterDoubleCeilingTest";
  // String uri = "Orders?$filter=ceiling(Freight) eq 33";
  // NorthwindTestUtils.testJSONResult(endpointUri, uri, inp);
  // }
  //
  // @Test
  // public void SystemQueryOptionFilterDecimalCeilingTest() {
  // String inp = "SystemQueryOptionFilterDecimalCeilingTest";
  // String uri = "Orders?$filter=floor(Freight) eq 33";
  // NorthwindTestUtils.testJSONResult(endpointUri, uri, inp);
  // }

  // TODO: type test

  // @Test
  // public void SystemQueryOptionFilterBoolIsOfTest() {
  // String inp = "SystemQueryOptionFilterBoolIsOfTest";
  // String uri = "Orders?$filter=isof('NorthwindModel.Order')";
  // NorthwindTestUtils.testJSONResult(endpointUri, uri, inp);
  // }

  // @Test
  // public void SystemQueryOptionFilterBoolIsOf2Test() {
  // String inp = "SystemQueryOptionFilterBoolIsOf2Test";
  // String uri = "Orders?$filter=isof(ShipCountry, 'Edm.String')";
  // NorthwindTestUtils.testJSONResult(endpointUri, uri, inp);
  // }

  @Test
  public void SystemQueryOptionExpand1Test() {
    String inp = "SystemQueryOptionExpand1Test";
    String uri = "Categories?$expand=Products";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  // @Test
  // public void SystemQueryOptionExpand2Test() {
  // String inp = "SystemQueryOptionExpand2Test";
  // String uri = "Categories?$expand=Products/Supplier";
  // NorthwindTestUtils.testJSONResult(endpointUri, uri, inp);
  // }

  @Test
  public void SystemQueryOptionExpand3Test() {
    String inp = "SystemQueryOptionExpand3Test";
    String uri = "Products?$expand=Category,Supplier";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  // @Test
  // public void SystemQueryOptionFormatAtomTest() {
  // String inp = "SystemQueryOptionFormatAtomTest";
  // String uri = "Products?$format=atom";
  // NorthwindTestUtils.testJSONResult(endpointUri, uri, inp);
  // }

  // @Test
  // public void SystemQueryOptionFormatJsonTest() {
  // String inp = "SystemQueryOptionFormatJsonTest";
  // String uri = "Products?$top=20&$format=json";
  // NorthwindTestUtils.testJSONResult(endpointUri, uri, inp);
  // }

  // TODO: select+$expand
  // @Test
  // public void SystemQueryOptionSelect3Test() {
  //  String inp = "SystemQueryOptionSelect3Test";
  //  String uri = "Categories?$select=CategoryName,Products&$expand=Products/Supplier";
  //  NorthwindTestUtils.testJSONResult(endpointUri, uri, inp);
  // }

  @Test
  public void SystemQueryOptionSelect1Test() {
    String inp = "SystemQueryOptionSelect1Test";
    String uri = "Products?$select=UnitPrice,ProductName";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionSelect2Test() {
    String inp = "SystemQueryOptionSelect2Test";
    String uri = "Products?$select=ProductName,Category";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionInlinecountTest() {
    String inp = "SystemQueryOptionInlinecountTest";
    String uri = "Products?$inlinecount=allpages";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionInlinecountTopTest() {
    String inp = "SystemQueryOptionInlinecountTopTest";
    String uri = "Products?$top=5&$inlinecount=allpages&Price gt 200";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SystemQueryOptionComplexKeyNavTest() {
    String inp = "SystemQueryOptionComplexKeyNavTest";
    String uri = "Order_Details(OrderID=10248,ProductID=11)/Product";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SelectOnSingleEntityTest() {
    String inp = "SelectOnSingleEntityTest";
    String uri = "Products(1)?$select=ProductName";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void MultiSelectOnSingleEntityTest() {
    String inp = "MultiSelectOnSingleEntityTest";
    String uri = "Products(1)?$select=ProductName,UnitPrice";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void ExpandOnSingleEntityTest() {
    String inp = "ExpandOnSingleEntityTest";
    String uri = "Products(1)?$expand=Category";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }

  @Test
  public void SelectTopZeroEntitiesTest() {
    String inp = "SelectTopZeroEntitiesTest";
    String uri = "Products?$top=0";
    this.utils.testAtomResult(endpointUri, uri, inp);
  }

  @Test
  public void SelectExpandOnSingleEntityTest() {
    String inp = "SelectExpandOnSingleEntityTest";
    String uri = "Products(1)?$select=ProductName,Category&$expand=Category";
    this.utils.testJSONResult(endpointUri, uri, inp);
  }
}
