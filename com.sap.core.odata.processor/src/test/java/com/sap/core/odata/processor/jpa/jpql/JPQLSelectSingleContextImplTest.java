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
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.SelectItem;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContext;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContextType;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.jpa.jpql.JPQLSelectSingleContext.JPQLSelectSingleContextBuilder;

public class JPQLSelectSingleContextImplTest {

	private static String entityTypeName = "MockEntity";
	private static String[] fields = { "Field1", "Field2" };
	private static List<KeyPredicate> keyPredicates = new ArrayList<KeyPredicate>();
	
	private static JPQLSelectSingleContextBuilder builder;
	private static JPQLSelectSingleContext selectContext;

	@BeforeClass
	public static void setup() {
		GetEntityUriInfo resultsView = EasyMock
				.createMock(GetEntityUriInfo.class);
		
		EdmEntitySet entitySet = EasyMock.createMock(EdmEntitySet.class);
		EdmEntityType entityType = EasyMock.createMock(EdmEntityType.class);

		/*int i = 0;
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

		} while (++i < 2);*/

		KeyPredicate keyPredicate = EasyMock
				.createMock(KeyPredicate.class);
		EdmProperty kpProperty = EasyMock
				.createMock(EdmProperty.class);
		EdmType edmType = EasyMock
				.createMock(EdmType.class);
		EasyMock.expect(keyPredicate.getLiteral()).andStubReturn("1");
		try {
			EasyMock.expect(kpProperty.getName()).andStubReturn("Field1");
			EasyMock.expect(kpProperty.getType()).andStubReturn(edmType);
			
		} catch (EdmException e2) {
			fail("this should not happen");
		}
		EasyMock.expect(keyPredicate.getProperty()).andStubReturn(kpProperty);
		EasyMock.replay(edmType,kpProperty,keyPredicate);
		keyPredicates.add(keyPredicate);
		int i = 0;
		try {
			
			List<SelectItem> selectItemList = new ArrayList<SelectItem>(2);
			do {
				EdmMapping edmMapping = EasyMock.createMock(EdmMapping.class);
				EasyMock.expect(edmMapping.getInternalName()).andStubReturn(fields[i]);
				EdmProperty edmProperty = EasyMock.createMock(EdmProperty.class);
				EasyMock.expect(edmProperty.getMapping()).andReturn(edmMapping);
				EasyMock.replay(edmMapping, edmProperty);

				SelectItem selectItem = EasyMock.createMock(SelectItem.class);
				EasyMock.expect(selectItem.getProperty()).andStubReturn(
						edmProperty);
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
			EasyMock.expect(resultsView.getSelect()).andStubReturn(selectItemList);
			ArrayList<KeyPredicate> arrayList = new ArrayList<KeyPredicate>();
			arrayList.add(keyPredicate);
			EasyMock.expect(resultsView.getKeyPredicates()).andStubReturn(
					arrayList);
			EasyMock.replay(resultsView);

		} catch (EdmException e1) {
			fail("Exception not Expected");
		}
		builder = (JPQLSelectSingleContextBuilder) JPQLContext.createBuilder(
				JPQLContextType.SELECT_SINGLE, resultsView);
		try {
			selectContext = (JPQLSelectSingleContext) builder.build();
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
	public void getKeyPredicates(){
		assertEquals(keyPredicates.size(), selectContext.getKeyPredicates().size());
		assertEquals(keyPredicates, selectContext.getKeyPredicates());
	}
	
	@Test
	public void testGetJPAEntityName() {
		assertEquals(JPQLSelectSingleContextImplTest.entityTypeName,
				selectContext.getJPAEntityName());
	}

	@Test
	public void testGetType() {
		assertEquals(JPQLContextType.SELECT_SINGLE, selectContext.getType());
	}

	@Test
	public void testCreateBuilder() {
		assertEquals(JPQLSelectSingleContextBuilder.class.toString(), builder
				.getClass().toString());
	}

}
