package com.sap.core.odata.processor.jpa.access;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotImplementedException;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.expression.BinaryExpression;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.LiteralExpression;
import com.sap.core.odata.api.uri.expression.MemberExpression;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.api.uri.expression.OrderExpression;
import com.sap.core.odata.api.uri.expression.PropertyExpression;
import com.sap.core.odata.api.uri.expression.SortOrder;
import com.sap.core.odata.api.uri.expression.UnaryExpression;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLStatement;
import com.sap.core.odata.processor.jpa.exception.ODataJPARuntimeException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * This class contains utility methods for parsing the filter expressions built by core library from user OData Query.
 * 
 * @author SAP AG
 *
 */
public class ODataExpressionParser {
	
	public static final String SPACE = " ";	//$NON-NLS-1$
	public static final String EMPTY = "";	//$NON-NLS-1$
//	public static final String TABLE_ALIAS = "gwt1";	//$NON-NLS-1$
	private static final String DOT = ".";	//$NON-NLS-1$
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ODataExpressionParser.class);
	
	/**
	 * This method returns the parsed where condition corresponding to the filter input in the user query.
	 * 
	 * @param whereExpression
	 * 
	 * @return Parsed where condition String
	 * @throws ODataException
	 */
	public static String parseToJPAWhereExpression(final CommonExpression whereExpression, String tableAlias) throws ODataException {
	    switch (whereExpression.getKind()) {
	    case UNARY:
	      final UnaryExpression unaryExpression = (UnaryExpression) whereExpression;
	      final String operand = parseToJPAWhereExpression(unaryExpression.getOperand(), tableAlias);

	      switch (unaryExpression.getOperator()) {
	      case NOT:
	          return JPQLStatement.Operator.NOT + "("+  operand +")";	//$NON-NLS-1$ //$NON-NLS-2$
	      case MINUS:
	        if (operand.startsWith("-"))	//$NON-NLS-1$
	          return operand.substring(1);
	        else
	          return "-" + operand;	//$NON-NLS-1$
	      default:
	    	  LOGGER.error("Unary Expression other then - and ! not supported");
	    	  throw new ODataNotImplementedException();
	      }
	      
	    case FILTER:
	    	return parseToJPAWhereExpression(((FilterExpression)whereExpression).getExpression(), tableAlias);
	    case BINARY:
	      final BinaryExpression binaryExpression = (BinaryExpression) whereExpression;
	      final String left = parseToJPAWhereExpression(binaryExpression.getLeftOperand(), tableAlias);
	      final String right = parseToJPAWhereExpression(binaryExpression.getRightOperand(), tableAlias);

	      switch (binaryExpression.getOperator()) {
		      case AND:
		    	  return left + SPACE + JPQLStatement.Operator.AND + SPACE + right;
		      case OR:
		          return left + SPACE + JPQLStatement.Operator.OR + SPACE + right;
		      case EQ:
		          return left + SPACE + JPQLStatement.Operator.EQ+ SPACE + right;
		      case NE:
		          return left + SPACE + JPQLStatement.Operator.NE + SPACE + right;
		      case LT:
		    	  return left + SPACE + JPQLStatement.Operator.LT + SPACE + right;
		      case LE:
		    	  return left + SPACE + JPQLStatement.Operator.LE + SPACE + right;
		      case GT:
		    	  return left + SPACE + JPQLStatement.Operator.GT + SPACE + right;
		      case GE:
		    	  return left + SPACE + JPQLStatement.Operator.GE + SPACE + right;
		      case PROPERTY_ACCESS:
		    	  LOGGER.error("Binary Expression of Propert Access type not supported");
		    	  throw new ODataNotImplementedException();
		      default:
		    	  LOGGER.error("Binary Expression "+binaryExpression.getOperator().name()+" type not supported");
		    	  throw new ODataNotImplementedException();
	      }

	    case PROPERTY:
	    	String returnStr = tableAlias+DOT+((PropertyExpression) whereExpression).getPropertyName();	
	    	return returnStr;

	    case MEMBER:
	    	final MemberExpression memberExpression = (MemberExpression) whereExpression;
	        final PropertyExpression propertyExpressionPath = (PropertyExpression) memberExpression.getPath();	        
	    	return tableAlias+DOT+propertyExpressionPath.getUriLiteral()+DOT+memberExpression.getProperty().getUriLiteral();	

	    case LITERAL:
	    	final LiteralExpression literal = (LiteralExpression) whereExpression;
		    final EdmSimpleType literalType = (EdmSimpleType) literal.getEdmType();
		    String value = literalType.valueToString(literalType.valueOfString(literal.getUriLiteral(), EdmLiteralKind.URI, null), EdmLiteralKind.DEFAULT, null);
		    return evaluateComparingExpression(value, literalType);

	    
	    default:
	    	LOGGER.error("Where Expression "+whereExpression.getKind().name()+" type not supported");
	    	throw new ODataNotImplementedException();
	    }
	  }
	
	/**
	 * This method parses the order by condition in the query.
	 * 
	 * @param orderByExpression
	 * @return
	 * @throws ODataJPARuntimeException
	 */
	public static HashMap<String, String> parseToJPAOrderByExpression(OrderByExpression orderByExpression, String tableAlias) throws ODataJPARuntimeException{
		HashMap<String, String> orderByMap = new HashMap<String, String>();
		if(orderByExpression != null && orderByExpression.getOrders() != null ){
			List<OrderExpression> orderBys= orderByExpression.getOrders();
			String orderByField = null;
			String orderByDirection = null;
			for(OrderExpression orderBy : orderBys){
				
				try {
					orderByField = orderBy.getExpression().getEdmType().getName();
					orderByDirection = (orderBy.getSortOrder() == SortOrder.asc)? EMPTY : "DESC";	//$NON-NLS-1$
					orderByMap.put(tableAlias+DOT+orderByField, orderByDirection);
				} catch (EdmException e) {
					LOGGER.error(e.getMessage(), e);
					throw ODataJPARuntimeException.throwException(
							ODataJPARuntimeException.RUNTIME_EXCEPTION.addContent(e
									.getMessage()), e);
				}				
			}			
		} 
		return orderByMap;		
	}
	
	/**
	 * This method evaluated the where expression for read of an entity based on the keys specified in the query.
	 * 
	 * @param keyPredicates
	 * @return the evaluated where expression
	 */
	
	public static String parseKeyPredicates(List<KeyPredicate> keyPredicates, String tableAlias) throws ODataJPARuntimeException{
		String literal = null;
		String propertyName = null;
		EdmSimpleType edmSimpleType = null;
		StringBuilder keyFilters = new StringBuilder();
		int i = 0;
		for(KeyPredicate keyPredicate : keyPredicates){
			if(i > 0){
				keyFilters.append(SPACE + JPQLStatement.Operator.AND + SPACE);
			}
			i++;
			literal = keyPredicate.getLiteral();
			try {
				propertyName = keyPredicate.getProperty().getName();
				edmSimpleType = (EdmSimpleType)keyPredicate.getProperty().getType();
			} catch (EdmException e) {
				throw ODataJPARuntimeException.throwException(
						ODataJPARuntimeException.RUNTIME_EXCEPTION.addContent(e
								.getMessage()), e);
			}
			
			literal = evaluateComparingExpression(literal, edmSimpleType);
			keyFilters.append(tableAlias+DOT+propertyName + SPACE + JPQLStatement.Operator.EQ+ SPACE + literal);
		}
		return keyFilters.toString();
	}
	
	/**
	 * This method evaluates the expression based on the type instance. Used for adding escape characters where necessary.
	 * 
	 * @param value
	 * @param edmSimpleType
	 * @return the evaluated expression
	 */
	private static String evaluateComparingExpression(String value, EdmSimpleType edmSimpleType){
		if (edmSimpleType == EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()
	            || edmSimpleType == EdmSimpleTypeKind.Guid.getEdmSimpleTypeInstance())
		{
			value = "\'"+value+"\'";	//$NON-NLS-1$	//$NON-NLS-2$
		}else if(edmSimpleType == EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance()
	            || edmSimpleType == EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance()	)
		{
			value = "{d \'"+value+"\'}";	//$NON-NLS-1$ 	//$NON-NLS-2$
		}else if(edmSimpleType == EdmSimpleTypeKind.Time.getEdmSimpleTypeInstance()){
			value = "{t \'"+value+"\'}";	//$NON-NLS-1$	//$NON-NLS-2$
		}
		return value;
	}

	public static HashMap<String, String> parseKeyPredicatesToJPAOrderByExpression(List<KeyPredicate> keyPredicates, String tableAlias) throws ODataJPARuntimeException {
		HashMap<String, String> orderByMap = new HashMap<String, String>();
		String propertyName = null;		
		for(KeyPredicate keyPredicate : keyPredicates){
			try {
				propertyName = keyPredicate.getProperty().getName();				
			} catch (EdmException e) {
				throw ODataJPARuntimeException.throwException(
						ODataJPARuntimeException.RUNTIME_EXCEPTION.addContent(e
								.getMessage()), e);
			}
			orderByMap.put(tableAlias+DOT+propertyName, EMPTY);
		}
		return orderByMap;
	}

}
