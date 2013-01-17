package com.sap.core.odata.processor.jpa.jpql;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.SelectItem;
import com.sap.core.odata.api.uri.expression.BinaryExpression;
import com.sap.core.odata.api.uri.expression.BinaryOperator;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.LiteralExpression;
import com.sap.core.odata.api.uri.expression.MethodExpression;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.api.uri.expression.PropertyExpression;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContext;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContext.JPQLContextBuilder;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLContextType;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.exception.ODataJPARuntimeException;

public class JPQLSelectSingleStatementBuilderTest {

	/**
	 * @throws java.lang.Exception
	 */	
	private JPQLSelectSingleStatementBuilder JPQLSelectSingleStatementBuilder; 
	
	@Before
	public void setUp() throws Exception {
				
	}
	
	private JPQLSelectSingleContextImpl createSelectContext() throws ODataJPARuntimeException, EdmException{
		//Object Instantiation
		
				JPQLSelectSingleContextImpl JPQLSelectSingleContextImpl = null;// new JPQLSelectSingleContextImpl();
				GetEntityUriInfo getEntityView = EasyMock.createMock(GetEntityUriInfo.class);
				
				EdmEntitySet edmEntitySet = EasyMock.createMock(EdmEntitySet.class);
				EdmEntityType edmEntityType = EasyMock.createMock(EdmEntityType.class);
//				OrderByExpression orderByExpression = EasyMock.createMock(OrderByExpression.class);
				List<SelectItem> selectItemList = null;

				//Setting up the expected value
				KeyPredicate keyPredicate = EasyMock
						.createMock(KeyPredicate.class);
				EdmProperty kpProperty = EasyMock
						.createMock(EdmProperty.class);
				EdmSimpleType edmType = EasyMock
						.createMock(EdmSimpleType.class);
				EasyMock.expect(keyPredicate.getLiteral()).andStubReturn("1");
				try {
					EasyMock.expect(kpProperty.getName()).andStubReturn("Field1");
					EasyMock.expect(kpProperty.getType()).andStubReturn(edmType);
					
				} catch (EdmException e2) {
					fail("this should not happen");
				}
				EasyMock.expect(keyPredicate.getProperty()).andStubReturn(kpProperty);
				EasyMock.replay(edmType,kpProperty,keyPredicate);
				EasyMock.expect(getEntityView.getTargetEntitySet()).andStubReturn(edmEntitySet);
				EasyMock.expect(getEntityView.getSelect()).andStubReturn(selectItemList);
				
				EasyMock.expect(edmEntitySet.getEntityType()).andStubReturn(edmEntityType);
				EasyMock.replay(edmEntitySet);
				EasyMock.expect(edmEntityType.getName()).andStubReturn("SalesOrderHeader");
				EasyMock.replay(edmEntityType);
				ArrayList<KeyPredicate> arrayList = new ArrayList<KeyPredicate>();
				arrayList.add(keyPredicate);
				EasyMock.expect(getEntityView.getKeyPredicates()).andStubReturn(
						arrayList);
				EasyMock.replay(getEntityView);
				
				JPQLContextBuilder contextBuilder1 = JPQLContext.createBuilder(JPQLContextType.SELECT_SINGLE, getEntityView);
				try {
					JPQLSelectSingleContextImpl = (JPQLSelectSingleContextImpl) contextBuilder1.build();
				} catch (ODataJPAModelException e) {
					fail("Model Exception thrown");
				}
				
				return JPQLSelectSingleContextImpl;
	}
	
		/**
	 * Test method for {@link com.sap.core.odata.processor.jpa.jpql.JPQLSelectSingleStatementBuilder#build)}.
	 * @throws EdmException 
		 * @throws ODataJPARuntimeException 
	 */
	
	@Test
	public void testBuildSimpleQuery() throws EdmException, ODataJPARuntimeException {
		JPQLSelectSingleContextImpl JPQLSelectSingleContextImpl = createSelectContext();
		JPQLSelectSingleStatementBuilder = new JPQLSelectSingleStatementBuilder(JPQLSelectSingleContextImpl);
		
		assertEquals("SELECT gwt1 FROM SalesOrderHeader gwt1 WHERE gwt1.Field1 = 1",JPQLSelectSingleStatementBuilder.build().toString());
	}
	
	
	
	
	
}
