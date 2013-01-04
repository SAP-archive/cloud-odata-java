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
import com.sap.core.odata.api.uri.resultviews.GetEntitySetView;
import com.sap.core.odata.processor.jpa.api.ODataJPAContext;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.jpql.JPQLSelectContextImpl;
import com.sap.core.odata.processor.jpa.jpql.JPQLSelectStatementBuilder;
import com.sap.core.odata.processor.jpa.jpql.api.JPQLContext;
import com.sap.core.odata.processor.jpa.jpql.api.JPQLContext.JPQLContextBuilder;
import com.sap.core.odata.processor.jpa.jpql.api.JPQLContextType;
import com.sap.core.odata.processor.jpa.jpql.api.JPQLSelectContext;
import com.sap.core.odata.processor.jpa.jpql.api.JPQLStatement;

/**
 * @author I072732
 *
 */
public class JPQLSelectStatementBuilderTest {

	/**
	 * @throws java.lang.Exception
	 */
	
	private JPQLSelectStatementBuilder jpqlSelectStatementBuilder; 
	private JPQLSelectContextImpl jpqlSelectContextImpl;
	private JPQLContextBuilder contextBuilder;
	private GetEntitySetView getEntitySetView;
	private ODataJPAContext odataJPAContext;
			
	@Before
	public void setUp() throws Exception {

		jpqlSelectContextImpl = new JPQLSelectContextImpl();
		contextBuilder = jpqlSelectContextImpl.new JPQLSelectContextBuilder();
				
		getEntitySetView = EasyMock.createMock(GetEntitySetView.class);
		odataJPAContext = EasyMock.createMock(ODataJPAContext.class);
				
	}
	
		/**
	 * Test method for {@link com.sap.core.odata.processor.jpa.jpql.JPQLSelectStatementBuilder#build)}.
	 * @throws EdmException 
	 */
	
	@SuppressWarnings("static-access")
	@Test
	public void testBuild() throws EdmException {
		EdmEntitySet edmEntitySet = EasyMock.createMock(EdmEntitySet.class);
		EdmEntityType edmEntityType = EasyMock.createMock(EdmEntityType.class);
		OrderByExpression orderByExpression = EasyMock.createMock(OrderByExpression.class);
		List<SelectItem> selectItemList = null;
		EasyMock.expect(getEntitySetView.getTargetEntitySet()).andReturn(edmEntitySet);
		EasyMock.expect(getEntitySetView.getOrderBy()).andReturn(orderByExpression);
		EasyMock.expect(getEntitySetView.getSelect()).andReturn(selectItemList);
		EasyMock.replay(getEntitySetView);
		EasyMock.expect(edmEntitySet.getEntityType()).andReturn(edmEntityType);
		EasyMock.replay(edmEntitySet);
		//Setting up the expected value
		EasyMock.expect(edmEntityType.getName()).andReturn("SalesOrderHeader");
		EasyMock.replay(edmEntityType);
		EasyMock.replay(odataJPAContext);
		JPQLContextBuilder contextBuilder1 = jpqlSelectContextImpl.createBuilder(JPQLContextType.SELECT, getEntitySetView, odataJPAContext);
		try {
			jpqlSelectContextImpl = (JPQLSelectContextImpl) contextBuilder1.build();
		} catch (ODataJPAModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		jpqlSelectStatementBuilder = new JPQLSelectStatementBuilder(jpqlSelectContextImpl);

		assertEquals("SELECT gwt1 FROM SalesOrderHeader gwt1",jpqlSelectStatementBuilder.build().toString());
	}
	
}
