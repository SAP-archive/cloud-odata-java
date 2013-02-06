package com.sap.core.odata.processor.core.jpa.jpql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.SelectItem;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.processor.api.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpql.JPQLContext;
import com.sap.core.odata.processor.api.jpql.JPQLContextType;
import com.sap.core.odata.processor.core.jpa.jpql.JPQLSelectSingleContext;
import com.sap.core.odata.processor.core.jpa.jpql.JPQLSelectSingleContext.JPQLSelectSingleContextBuilder;

public class JPQLSelectSingleContextImplTest {

	private static String entityTypeName = "MockEntity";
	private static String[] fields = { "Field1", "Field2" };
	private static List<KeyPredicate> keyPredicates;

	private static JPQLSelectSingleContextBuilder builder;
	private static JPQLSelectSingleContext selectContext;

	private void buildContextBuilder(boolean isSelectNull) {
		builder = null;
		selectContext = null;
		keyPredicates = new ArrayList<KeyPredicate>();
		GetEntityUriInfo resultsView = EasyMock
				.createMock(GetEntityUriInfo.class);

		EdmEntitySet entitySet = EasyMock.createMock(EdmEntitySet.class);
		EdmEntityType entityType = EasyMock.createMock(EdmEntityType.class);

		KeyPredicate keyPredicate = EasyMock.createMock(KeyPredicate.class);
		EdmProperty kpProperty = EasyMock.createMock(EdmProperty.class);
		EdmType edmType = EasyMock.createMock(EdmType.class);
		EasyMock.expect(keyPredicate.getLiteral()).andStubReturn("1");
		try {
			EasyMock.expect(kpProperty.getName()).andStubReturn("Field1");
			EasyMock.expect(kpProperty.getType()).andStubReturn(edmType);

		} catch (EdmException e2) {
			fail("this should not happen");
		}
		EasyMock.expect(keyPredicate.getProperty()).andStubReturn(kpProperty);
		EasyMock.replay(edmType, kpProperty, keyPredicate);
		keyPredicates.add(keyPredicate);
		int i = 0;
		try {

			List<SelectItem> selectItemList = new ArrayList<SelectItem>(2);
			do {
				EdmMapping edmMapping = EasyMock.createMock(EdmMapping.class);
				EasyMock.expect(edmMapping.getInternalName()).andStubReturn(
						fields[i]);
				EdmProperty edmProperty = EasyMock
						.createMock(EdmProperty.class);
				EasyMock.expect(edmProperty.getMapping()).andStubReturn(
						edmMapping);
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
			if (isSelectNull)
				selectItemList = null;
			EasyMock.expect(resultsView.getSelect()).andStubReturn(
					selectItemList);
			ArrayList<KeyPredicate> arrayList = new ArrayList<KeyPredicate>();
			arrayList.add(keyPredicate);
			EasyMock.expect(resultsView.getKeyPredicates()).andStubReturn(
					arrayList);
			EasyMock.replay(resultsView);

		} catch (EdmException e1) {
			fail("Exception not Expected");
		}
		try {
			builder = (JPQLSelectSingleContextBuilder) JPQLContext
					.createBuilder(JPQLContextType.SELECT_SINGLE, resultsView);

			selectContext = (JPQLSelectSingleContext) builder.build();
		} catch (ODataJPAModelException e) {
			fail("Exception not Expected");
		} catch (ODataJPARuntimeException e) {
			fail("Runtime Exception thrown");
		}
	}

	@Test
	public void testEntityNameThrowingException() {
		// buildSelectContext(false, false, false);
		GetEntityUriInfo resultsView = EasyMock
				.createMock(GetEntityUriInfo.class);

		EdmEntitySet entitySet = EasyMock.createMock(EdmEntitySet.class);
		EdmEntityType entityType = EasyMock.createMock(EdmEntityType.class);

		try {
			EasyMock.expect(entityType.getName()).andStubThrow(
					new EdmException(null));
			EasyMock.expect(entitySet.getEntityType()).andStubThrow(
					new EdmException(null));
		} catch (EdmException e1) {
			// throw new ODataException();
		}

		EasyMock.replay(entityType);
		EasyMock.replay(entitySet);

		EasyMock.expect(resultsView.getTargetEntitySet()).andStubReturn(
				entitySet);
		EasyMock.expect(resultsView.getSelect()).andStubReturn(null);
		EasyMock.expect(resultsView.getFilter()).andStubReturn(null);
		EasyMock.replay(resultsView);
		try {
			JPQLSelectSingleContextBuilder builder1 = (JPQLSelectSingleContextBuilder) JPQLContext
					.createBuilder(JPQLContextType.SELECT_SINGLE, resultsView);
			builder1.build();
			fail("Should not come here");
		} catch (ODataJPAModelException e) {
			fail();
		} catch (ODataJPARuntimeException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testSlectedFieldsAsNull() {
		buildContextBuilder(true);
		try {
			selectContext = (JPQLSelectSingleContext) builder.build();
			assertEquals("E2", selectContext.getSelectExpression());
		} catch (ODataJPAModelException e) {
			fail();
		} catch (ODataJPARuntimeException e) {
			fail();
		}
	}

	@Test
	public void testGetSelectedFields() {
		buildContextBuilder(false);
		assertTrue(selectContext.getSelectExpression().contains(fields[0]));
		assertTrue(selectContext.getSelectExpression().contains(fields[1]));
	}

	@Test
	public void getKeyPredicates() {
		buildContextBuilder(false);
		assertEquals(keyPredicates.size(), selectContext.getKeyPredicates()
				.size());
		assertEquals(keyPredicates, selectContext.getKeyPredicates());
	}

	@Test
	public void testGetJPAEntityName() {
		buildContextBuilder(false);
		assertEquals(JPQLSelectSingleContextImplTest.entityTypeName,
				selectContext.getJPAEntityName());
	}

	@Test
	public void testGetType() {
		buildContextBuilder(false);
		assertEquals(JPQLContextType.SELECT_SINGLE, selectContext.getType());
	}

	@Test
	public void testCreateBuilder() {
		buildContextBuilder(false);
		assertEquals(JPQLSelectSingleContextBuilder.class.toString(), builder
				.getClass().toString());
	}

}
