/**
 * 
 */
package com.sap.core.odata.processor.jpa.jpql;

import static org.junit.Assert.*;
import java.util.List;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.uri.SelectItem;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.processor.jpa.api.ODataJPAContext;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.jpql.JPQLSelectContextImpl;
import com.sap.core.odata.processor.jpa.jpql.JPQLSelectStatementBuilder;
import com.sap.core.odata.processor.jpa.jpql.api.JPQLContext.JPQLContextBuilder;
import com.sap.core.odata.processor.jpa.jpql.api.JPQLContextType;

public class JPQLSelectStatementBuilderTest {

	/**
	 * @throws java.lang.Exception
	 */	
	private JPQLSelectStatementBuilder jpqlSelectStatementBuilder; 
	
	@Before
	public void setUp() throws Exception {

		//Object Instantiation
		
		JPQLSelectContextImpl jpqlSelectContextImpl = new JPQLSelectContextImpl();
		GetEntitySetUriInfo getEntitySetView = EasyMock.createMock(GetEntitySetUriInfo.class);
		ODataJPAContext odataJPAContext = EasyMock.createMock(ODataJPAContext.class);
		EdmEntitySet edmEntitySet = EasyMock.createMock(EdmEntitySet.class);
		EdmEntityType edmEntityType = EasyMock.createMock(EdmEntityType.class);
		OrderByExpression orderByExpression = EasyMock.createMock(OrderByExpression.class);
		List<SelectItem> selectItemList = null;

		//Setting up the expected value
		
		EasyMock.expect(getEntitySetView.getTargetEntitySet()).andReturn(edmEntitySet);
		EasyMock.expect(getEntitySetView.getOrderBy()).andReturn(orderByExpression);
		EasyMock.expect(getEntitySetView.getSelect()).andReturn(selectItemList);
		EasyMock.expect(getEntitySetView.getFilter()).andReturn(null);
		EasyMock.replay(getEntitySetView);
		EasyMock.expect(edmEntitySet.getEntityType()).andReturn(edmEntityType);
		EasyMock.replay(edmEntitySet);
		EasyMock.expect(edmEntityType.getName()).andReturn("SalesOrderHeader");
		EasyMock.replay(edmEntityType);
		EasyMock.replay(odataJPAContext);
		
		JPQLContextBuilder contextBuilder1 = JPQLSelectContextImpl.createBuilder(JPQLContextType.SELECT, getEntitySetView, odataJPAContext);
		try {
			jpqlSelectContextImpl = (JPQLSelectContextImpl) contextBuilder1.build();
		} catch (ODataJPAModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		jpqlSelectStatementBuilder = new JPQLSelectStatementBuilder(jpqlSelectContextImpl);
				
	}
	
		/**
	 * Test method for {@link com.sap.core.odata.processor.jpa.jpql.JPQLSelectStatementBuilder#build)}.
	 * @throws EdmException 
	 */
	
	@Test
	public void testBuild() throws EdmException {
		
		assertEquals("SELECT gwt1 FROM SalesOrderHeader gwt1",jpqlSelectStatementBuilder.build().toString());
	}
	
}
