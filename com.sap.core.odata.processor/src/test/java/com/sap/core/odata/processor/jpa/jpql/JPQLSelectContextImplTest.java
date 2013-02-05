package com.sap.core.odata.processor.jpa.jpql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.SelectItem;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.api.uri.expression.OrderExpression;
import com.sap.core.odata.api.uri.expression.PropertyExpression;
import com.sap.core.odata.api.uri.expression.SortOrder;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.processor.jpa.api.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.api.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContext;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContextType;
import com.sap.core.odata.processor.jpa.jpql.JPQLSelectContext.JPQLSelectContextBuilder;

public class JPQLSelectContextImplTest {

	private static String entityTypeName = "MockEntity";
	private static String[] fields = { "Field1", "Field2" };
	private static List<KeyPredicate> keyPredicates;
	private static SortOrder[] orderType = { SortOrder.asc, SortOrder.desc };

	private static JPQLSelectContextBuilder builder;
	private static JPQLSelectContext selectContext;

	@BeforeClass
	public static void setup() {
		
	}

	private void buildSelectContext(boolean orderByIsNull, boolean selectFieldsIsNull, boolean filterIsNull, boolean isTopNull, boolean isSkipNull) {
		builder = null;
		selectContext = null;
		keyPredicates = new ArrayList<KeyPredicate>();
		GetEntitySetUriInfo resultsView = EasyMock
				.createMock(GetEntitySetUriInfo.class);

		EdmEntitySet entitySet = EasyMock.createMock(EdmEntitySet.class);
		EdmEntityType entityType = EasyMock.createMock(EdmEntityType.class);
		KeyPredicate keyPredicate = EasyMock
				.createMock(KeyPredicate.class);
		EdmProperty kpProperty = EasyMock
				.createMock(EdmProperty.class);
		EdmType edmType1 = EasyMock
				.createMock(EdmType.class);
		EdmMapping mapping = EasyMock.createMock(EdmMapping.class);
		EasyMock.expect(keyPredicate.getLiteral()).andStubReturn("1");
		try {
			EasyMock.expect(mapping.getInternalName()).andStubReturn("Field1");
			EasyMock.expect(kpProperty.getMapping()).andStubReturn(mapping);
			EasyMock.expect(kpProperty.getType()).andStubReturn(edmType1);
			
		} catch (EdmException e2) {
			fail("this should not happen");
		}
		EasyMock.expect(keyPredicate.getProperty()).andStubReturn(kpProperty);
		EasyMock.replay(mapping, edmType1,kpProperty,keyPredicate);
		keyPredicates.add(keyPredicate);
		int i = 0;
		List<OrderExpression> orderList = new ArrayList<OrderExpression>(2);
		do {

			EdmType edmType = EasyMock.createMock(EdmType.class);
			try {
				EasyMock.expect(edmType.getName()).andStubReturn(fields[i]);
				EasyMock.replay(edmType);
			} catch (EdmException e2) {
				fail("Exception not Expected");
			}

			PropertyExpression commonExpression = EasyMock
					.createMock(PropertyExpression.class);
			EasyMock.expect(commonExpression.getEdmType()).andStubReturn(
					edmType);
			
			
			EdmProperty edmTyped = EasyMock
					.createMock(EdmProperty.class);
			EdmMapping edmMapping = EasyMock.createMock(EdmMapping.class);
			EasyMock.expect(edmMapping.getInternalName()).andStubReturn(fields[i]);
			try {
				EasyMock.expect(edmTyped.getMapping()).andStubReturn(edmMapping);
			} catch (EdmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			EasyMock.expect(commonExpression.getEdmProperty()).andStubReturn(edmTyped);
			OrderExpression order = EasyMock.createMock(OrderExpression.class);
			EasyMock.expect(order.getExpression()).andStubReturn(
					commonExpression);
			EasyMock.expect(order.getSortOrder()).andStubReturn(orderType[i]);
			EasyMock.replay(edmMapping, edmTyped, commonExpression);
			EasyMock.replay(order);

			orderList.add(order);

		} while (++i < 2);

		OrderByExpression orderBy = EasyMock
				.createMock(OrderByExpression.class);
		EasyMock.expect(orderBy.getOrders()).andStubReturn(orderList);
		EasyMock.replay(orderBy);

		try {
			i = 0;
			List<SelectItem> selectItemList = new ArrayList<SelectItem>(2);
			do {
				EdmMapping edmMapping = EasyMock.createMock(EdmMapping.class);
				EasyMock.expect(edmMapping.getInternalName()).andStubReturn(fields[i]);
				EdmProperty edmProperty = EasyMock.createMock(EdmProperty.class);
				EasyMock.expect(edmProperty.getMapping()).andStubReturn(edmMapping);
				EasyMock.replay(edmMapping, edmProperty);

				SelectItem selectItem = EasyMock.createMock(SelectItem.class);
				EasyMock.expect(selectItem.getProperty()).andStubReturn(
						edmProperty);
				EasyMock.replay(selectItem);

				selectItemList.add(selectItem);

			} while (++i < 2);
			
			EasyMock.expect(entityType.getName()).andStubReturn(entityTypeName);
			EasyMock.replay(entityType);
			EasyMock.expect(entitySet.getEntityType())
					.andStubReturn(entityType);
			EasyMock.replay(entitySet);
			
			EasyMock.expect(resultsView.getTargetEntitySet()).andStubReturn(
					entitySet);
			if(orderByIsNull) EasyMock.expect(resultsView.getOrderBy()).andStubReturn(null);
			else EasyMock.expect(resultsView.getOrderBy()).andStubReturn(orderBy);
			if(selectFieldsIsNull) EasyMock.expect(resultsView.getSelect()).andStubReturn(null);
			else EasyMock.expect(resultsView.getSelect()).andStubReturn(selectItemList);			
//			FilterExpression filterExpression = EasyMock.createMock(FilterExpression.class);
//			EasyMock.expect(filterExpression.g)
			if(filterIsNull) EasyMock.expect(resultsView.getFilter()).andStubReturn(null);
			else EasyMock.expect(resultsView.getFilter()).andStubReturn(getFilterExpressionMockedObj(ExpressionKind.PROPERTY, "SalesOrder"));
			if(isTopNull)
				EasyMock.expect(resultsView.getTop()).andStubReturn(null);
			else EasyMock.expect(resultsView.getTop()).andStubReturn(10);
			if(isSkipNull)
				EasyMock.expect(resultsView.getSkip()).andStubReturn(null);
			else EasyMock.expect(resultsView.getSkip()).andStubReturn(0);
			EasyMock.expect(resultsView.getKeyPredicates()).andStubReturn(keyPredicates);
			EasyMock.replay(resultsView);

		} catch (EdmException e1) {
			fail("Exception not Expected");
		}
		builder = (JPQLSelectContextBuilder) JPQLContext.createBuilder(
				JPQLContextType.SELECT, resultsView);
		try {
			selectContext = (JPQLSelectContext) builder.build();
		} catch (ODataJPAModelException e) {
			fail("Exception not Expected");
		} catch (ODataJPARuntimeException e) {
			fail("Runtime Exception thrown");
		}
	}
	
	@Test
	public void testEntityNameThrowingException() {
//		buildSelectContext(false, false, false);
		GetEntitySetUriInfo resultsView = EasyMock
				.createMock(GetEntitySetUriInfo.class);

		EdmEntitySet entitySet = EasyMock.createMock(EdmEntitySet.class);
		EdmEntityType entityType = EasyMock.createMock(EdmEntityType.class);
	
		try {
			EasyMock.expect(entityType.getName()).andStubThrow(new EdmException(null));
			EasyMock.expect(entitySet.getEntityType())
			.andStubThrow(new EdmException(null));
		} catch (EdmException e1) {
//			throw new ODataException();
		}
			
		EasyMock.replay(entityType);
		EasyMock.replay(entitySet);
		
		EasyMock.expect(resultsView.getTargetEntitySet()).andStubReturn(
				entitySet);
		EasyMock.expect(resultsView.getOrderBy()).andStubReturn(null);
		EasyMock.expect(resultsView.getSelect()).andStubReturn(null);
		EasyMock.expect(resultsView.getFilter()).andStubReturn(null);
		EasyMock.replay(resultsView);
		JPQLSelectContextBuilder builder1 = (JPQLSelectContextBuilder) JPQLContext.createBuilder(
				JPQLContextType.SELECT, resultsView);
		try {
			/*JPQLSelectContext selectContext1 = (JPQLSelectContext)*/ builder1.build();
			fail("Should not come here");
		} catch (ODataJPAModelException e) {
			fail();
		}
		catch(ODataJPARuntimeException e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testSelectFieldsAsNull() {
		buildSelectContext(false, true, true, true, true);
		
		try {
			selectContext = (JPQLSelectContext) builder.build();
			assertEquals("E2", selectContext.getSelectExpression());
		} catch (ODataJPAModelException e) {
			fail();
		}
		catch(ODataJPARuntimeException e) {
			fail();
		}
	}

	@Test
	public void testGetSelectedFields() {
		buildSelectContext(false, false, true, true, true);
		assertTrue(selectContext.getSelectExpression().contains(fields[0]));
		assertTrue(selectContext.getSelectExpression().contains(fields[1]));
	}

	@Test
	public void testGetOrderByCollection() {
		buildSelectContext(false, false, true, true, true);
		assertEquals(
				true,
				selectContext.getOrderByCollection().containsKey("E1."+
						JPQLSelectContextImplTest.fields[0]));
		assertEquals(
				"",
				selectContext.getOrderByCollection().get("E1."+
						JPQLSelectContextImplTest.fields[0]));

		assertEquals(
				true,
				selectContext.getOrderByCollection().containsKey("E1."+
						JPQLSelectContextImplTest.fields[1]));
		assertEquals(
				"DESC",
				selectContext.getOrderByCollection().get("E1."+
						JPQLSelectContextImplTest.fields[1]));
	}

	@Test
	public void testGetWhereExpression() {
		buildSelectContext(false, false, true, true, true);
		//fail("Not yet implemented");
	}

	@Test
	public void testGetJPAEntityName() {
		buildSelectContext(false, false, true, true, true);
		assertEquals(JPQLSelectContextImplTest.entityTypeName,
				selectContext.getJPAEntityName());
	}

	@Test
	public void testGetType() {
		buildSelectContext(false, false, true, true, true);
		assertEquals(JPQLContextType.SELECT, selectContext.getType());
	}

	@Test
	public void testCreateBuilder() {
		buildSelectContext(false, false, true, true, true);
		assertEquals(JPQLSelectContextBuilder.class.toString(), builder
				.getClass().toString());
	}
	
	@Test
	public void testEntitySetAsNull(){
		buildSelectContext(false, false, true, true, true);
		JPQLSelectContextBuilder builder = (JPQLSelectContextBuilder) JPQLContext.createBuilder(
				JPQLContextType.SELECT, null);
		try {
			JPQLSelectContext selectContext1 = (JPQLSelectContext) builder.build();
			assertNull(selectContext1.getJPAEntityAlias());
			assertNull(selectContext1.getJPAEntityName());
			assertNull(selectContext1.getOrderByCollection());
			assertNull(selectContext1.getSelectExpression());
			assertNull(selectContext1.getType());
			assertNull(selectContext1.getWhereExpression());
		} catch (ODataJPAModelException e) {
			fail("Exception not Expected");
		} catch (ODataJPARuntimeException e) {
			fail("Runtime Exception thrown");
		}
	}
	
	@Test
	public void testOrderingWithSkip(){
		buildSelectContext(true, false, true, true, false);
		assertEquals(
				true,
				selectContext.getOrderByCollection().containsKey("E1."+
						JPQLSelectContextImplTest.fields[0]));
		assertEquals(
				"",
				selectContext.getOrderByCollection().get("E1."+
						JPQLSelectContextImplTest.fields[0]));

		assertEquals(
				false,
				selectContext.getOrderByCollection().containsKey("E1."+
						JPQLSelectContextImplTest.fields[1]));
		
	}
	
	@Test
	public void testOrderingWithTop(){
		buildSelectContext(true, false, true, false, true);
		assertEquals(
				true,
				selectContext.getOrderByCollection().containsKey("E1."+
						JPQLSelectContextImplTest.fields[0]));
		assertEquals(
				"",
				selectContext.getOrderByCollection().get("E1."+
						JPQLSelectContextImplTest.fields[0]));

		assertEquals(
				false,
				selectContext.getOrderByCollection().containsKey("E1."+
						JPQLSelectContextImplTest.fields[1]));
		
	}
	
	@Test
	public void testOrderByTopSkipAsNull(){
		buildSelectContext(true, true, true, true, true);
		assertNull(selectContext.getOrderByCollection());
	}
	
	@Test
	public void testFilter(){
		buildSelectContext(true, false, false, false, false);
		assertEquals("E1.field",selectContext.whereCondition);
		
	}
	
	
	private FilterExpression getFilterExpressionMockedObj(ExpressionKind leftOperandExpKind, String propertyName) throws EdmException{
		FilterExpression filterExpression = EasyMock.createMock(FilterExpression.class);
		EasyMock.expect(filterExpression.getKind()).andReturn(ExpressionKind.FILTER).times(10);
		EasyMock.expect(filterExpression.getExpression()).andReturn(getPropertyExpressionMockedObj(leftOperandExpKind, propertyName)).times(10);		
		EasyMock.replay(filterExpression);
		return filterExpression;		
	}
	
	private PropertyExpression getPropertyExpressionMockedObj(ExpressionKind expKind, String propertyName){
		PropertyExpression  leftOperandPropertyExpresion  = EasyMock.createMock(PropertyExpression.class);
		EasyMock.expect(leftOperandPropertyExpresion.getKind()).andReturn(ExpressionKind.PROPERTY);
		EasyMock.expect(leftOperandPropertyExpresion.getPropertyName()).andReturn(propertyName);
		EdmProperty edmtTyped = EasyMock.createMock(EdmProperty.class);
		EdmMapping mapping = EasyMock.createMock(EdmMapping.class);
		EasyMock.expect(mapping.getInternalName()).andStubReturn("field");
		try {
			EasyMock.expect(edmtTyped.getMapping()).andStubReturn(mapping);
		} catch (EdmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		EasyMock.expect(leftOperandPropertyExpresion.getEdmProperty()).andReturn(edmtTyped);
		EasyMock.replay(mapping, edmtTyped,leftOperandPropertyExpresion);
		return leftOperandPropertyExpresion;
	}

}
