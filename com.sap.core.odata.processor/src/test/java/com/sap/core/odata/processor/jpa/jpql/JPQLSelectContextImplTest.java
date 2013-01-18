package com.sap.core.odata.processor.jpa.jpql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.uri.SelectItem;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.api.uri.expression.OrderExpression;
import com.sap.core.odata.api.uri.expression.SortOrder;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContext;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContextType;
import com.sap.core.odata.processor.jpa.jpql.JPQLSelectContext;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.jpa.jpql.JPQLSelectContext.JPQLSelectContextBuilder;

public class JPQLSelectContextImplTest {

	private static String entityTypeName = "MockEntity";
	private static String[] fields = { "Field1", "Field2" };
	private static SortOrder[] orderType = { SortOrder.asc, SortOrder.desc };

	private static JPQLSelectContextBuilder builder;
	private static JPQLSelectContext selectContext;

	@BeforeClass
	public static void setup() {
		GetEntitySetUriInfo resultsView = EasyMock
				.createMock(GetEntitySetUriInfo.class);

		EdmEntitySet entitySet = EasyMock.createMock(EdmEntitySet.class);
		EdmEntityType entityType = EasyMock.createMock(EdmEntityType.class);

		int i = 0;
		List<OrderExpression> orderList = new ArrayList<OrderExpression>(2);
		do {

			EdmType edmType = EasyMock.createMock(EdmType.class);
			try {
				EasyMock.expect(edmType.getName()).andReturn(fields[i]);
				EasyMock.replay(edmType);
			} catch (EdmException e2) {
				fail("Exception not Expected");
			}

			CommonExpression commonExpression = EasyMock
					.createMock(CommonExpression.class);
			EasyMock.expect(commonExpression.getEdmType()).andStubReturn(
					edmType);
			EasyMock.replay(commonExpression);

			OrderExpression order = EasyMock.createMock(OrderExpression.class);
			EasyMock.expect(order.getExpression()).andStubReturn(
					commonExpression);
			EasyMock.expect(order.getSortOrder()).andStubReturn(orderType[i]);
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
				EdmTyped edmTyped = EasyMock.createMock(EdmTyped.class);
				EasyMock.expect(edmTyped.getName()).andReturn(fields[i]);
				EasyMock.replay(edmTyped);

				SelectItem selectItem = EasyMock.createMock(SelectItem.class);
				EasyMock.expect(selectItem.getProperty()).andStubReturn(
						edmTyped);
				EasyMock.replay(selectItem);

				selectItemList.add(selectItem);

			} while (++i < 2);
			
			EasyMock.expect(entityType.getName()).andReturn(entityTypeName);
			EasyMock.replay(entityType);
			EasyMock.expect(entitySet.getEntityType())
					.andStubReturn(entityType);
			EasyMock.replay(entitySet);
			
			EasyMock.expect(resultsView.getTargetEntitySet()).andStubReturn(
					entitySet);
			EasyMock.expect(resultsView.getOrderBy()).andStubReturn(orderBy);
			EasyMock.expect(resultsView.getSelect()).andStubReturn(selectItemList);
			EasyMock.expect(resultsView.getFilter()).andReturn(null);
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
	public void testGetSelectedFields() {
		assertEquals(fields[0], selectContext.getSelectedFields().get(0));
		assertEquals(fields[1], selectContext.getSelectedFields().get(1));
	}

	@Test
	public void testGetOrderByCollection() {

		assertEquals(
				true,
				selectContext.getOrderByCollection().containsKey(
						JPQLSelectContextImplTest.fields[0]));
		assertEquals(
				"",
				selectContext.getOrderByCollection().get(
						JPQLSelectContextImplTest.fields[0]));

		assertEquals(
				true,
				selectContext.getOrderByCollection().containsKey(
						JPQLSelectContextImplTest.fields[1]));
		assertEquals(
				"DESC",
				selectContext.getOrderByCollection().get(
						JPQLSelectContextImplTest.fields[1]));
	}

	@Test
	public void testGetWhereExpression() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetJPAEntityName() {
		assertEquals(JPQLSelectContextImplTest.entityTypeName,
				selectContext.getJPAEntityName());
	}

	@Test
	public void testGetType() {
		assertEquals(JPQLContextType.SELECT, selectContext.getType());
	}

	@Test
	public void testCreateBuilder() {
		assertEquals(JPQLSelectContextBuilder.class.toString(), builder
				.getClass().toString());
	}

}
