package com.sap.core.odata.processor.jpa.access.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotImplementedException;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.expression.BinaryExpression;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.LiteralExpression;
import com.sap.core.odata.api.uri.expression.MemberExpression;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.api.uri.expression.OrderExpression;
import com.sap.core.odata.api.uri.expression.PropertyExpression;
import com.sap.core.odata.api.uri.expression.SortOrder;
import com.sap.core.odata.api.uri.expression.UnaryExpression;
import com.sap.core.odata.processor.jpa.api.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.jpa.api.jpql.JPQLStatement;

/**
 * This class contains utility methods for parsing the filter expressions built by core library from user OData Query.
 * 
 * @author SAP AG
 *
 */
public class ODataExpressionParser {
	
	public static final String EMPTY = "";	//$NON-NLS-1$
	
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
		    	  return left + JPQLStatement.DELIMITER.SPACE + JPQLStatement.Operator.AND + JPQLStatement.DELIMITER.SPACE + right;
		      case OR:
		          return left + JPQLStatement.DELIMITER.SPACE + JPQLStatement.Operator.OR + JPQLStatement.DELIMITER.SPACE + right;
		      case EQ:
		          return left + JPQLStatement.DELIMITER.SPACE + JPQLStatement.Operator.EQ+ JPQLStatement.DELIMITER.SPACE + right;
		      case NE:
		          return left + JPQLStatement.DELIMITER.SPACE + JPQLStatement.Operator.NE + JPQLStatement.DELIMITER.SPACE + right;
		      case LT:
		    	  return left + JPQLStatement.DELIMITER.SPACE + JPQLStatement.Operator.LT + JPQLStatement.DELIMITER.SPACE + right;
		      case LE:
		    	  return left + JPQLStatement.DELIMITER.SPACE + JPQLStatement.Operator.LE + JPQLStatement.DELIMITER.SPACE + right;
		      case GT:
		    	  return left + JPQLStatement.DELIMITER.SPACE + JPQLStatement.Operator.GT + JPQLStatement.DELIMITER.SPACE + right;
		      case GE:
		    	  return left + JPQLStatement.DELIMITER.SPACE + JPQLStatement.Operator.GE + JPQLStatement.DELIMITER.SPACE + right;
		      case PROPERTY_ACCESS:
		    	  throw new ODataNotImplementedException();
		      default:
		    	  throw new ODataNotImplementedException();
	      }

	    case PROPERTY:
	    	String returnStr = tableAlias+JPQLStatement.DELIMITER.PERIOD+((EdmProperty) ((PropertyExpression) whereExpression).getEdmProperty()).getMapping().getInternalName();	
	    	return returnStr;

	    case MEMBER:
	        String memberExpStr = EMPTY;
	        int i=0;
	        MemberExpression member = null;
	        CommonExpression tempExp = whereExpression;
	        while (tempExp != null && tempExp.getKind() == ExpressionKind.MEMBER) {
	          member = (MemberExpression) tempExp;
	          if(i>0){
	        	  memberExpStr = JPQLStatement.DELIMITER.PERIOD + memberExpStr;
	          }
	          i++;
	          memberExpStr = ((EdmProperty) ((PropertyExpression) member.getProperty()).getEdmProperty()).getMapping().getInternalName() + memberExpStr;	          
	          tempExp = member.getPath();
	        }
	        memberExpStr = ((EdmProperty) ((PropertyExpression) tempExp).getEdmProperty()).getMapping().getInternalName() + JPQLStatement.DELIMITER.PERIOD + memberExpStr;
	        return tableAlias+JPQLStatement.DELIMITER.PERIOD+memberExpStr;

	    case LITERAL:
	    	final LiteralExpression literal = (LiteralExpression) whereExpression;
		    final EdmSimpleType literalType = (EdmSimpleType) literal.getEdmType();
		    String value = literalType.valueToString(literalType.valueOfString(literal.getUriLiteral(), EdmLiteralKind.URI, null, literalType.getDefaultType()), EdmLiteralKind.DEFAULT, null);
		    return evaluateComparingExpression(value, literalType);

	    
	    default:
	    	throw new ODataNotImplementedException();
	    }
	  }
	
	/**
	 * This method parses the select clause
	 * 
	 * @param tableAlias
	 * @param selectedFields
	 * @return
	 */	
	public static String parseToJPASelectExpression(String tableAlias,ArrayList<String> selectedFields) {
		
		String selectClause = EMPTY;
		Iterator<String> itr = selectedFields.iterator();
		int count = 0;
		
		while(itr.hasNext()) {
			selectClause = selectClause + tableAlias + JPQLStatement.DELIMITER.PERIOD + itr.next();
			count++;			
			
			if(count<selectedFields.size()) {
				selectClause = selectClause + JPQLStatement.DELIMITER.COMMA + JPQLStatement.DELIMITER.SPACE;
			}			
		}		
		return selectClause;		
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
					orderByField =((EdmProperty) ((PropertyExpression) orderBy.getExpression()).getEdmProperty()).getMapping().getInternalName();
					orderByDirection = (orderBy.getSortOrder() == SortOrder.asc)? EMPTY : "DESC";	//$NON-NLS-1$
					orderByMap.put(tableAlias+JPQLStatement.DELIMITER.PERIOD+orderByField, orderByDirection);
				} catch (EdmException e) {
					throw ODataJPARuntimeException.throwException(
							ODataJPARuntimeException.GENERAL.addContent(e
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
				keyFilters.append(JPQLStatement.DELIMITER.SPACE + JPQLStatement.Operator.AND + JPQLStatement.DELIMITER.SPACE);
			}
			i++;
			literal = keyPredicate.getLiteral();
			try {
				propertyName = keyPredicate.getProperty().getMapping().getInternalName();
				edmSimpleType = (EdmSimpleType)keyPredicate.getProperty().getType();
			} catch (EdmException e) {
				throw ODataJPARuntimeException.throwException(
						ODataJPARuntimeException.GENERAL.addContent(e
								.getMessage()), e);
			}
			
			literal = evaluateComparingExpression(literal, edmSimpleType);
			keyFilters.append(tableAlias+JPQLStatement.DELIMITER.PERIOD+propertyName + JPQLStatement.DELIMITER.SPACE + JPQLStatement.Operator.EQ+ JPQLStatement.DELIMITER.SPACE + literal);
		}
		if(keyFilters.length()>0)
			return keyFilters.toString();
		else
			return null;
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
				propertyName = keyPredicate.getProperty().getMapping().getInternalName();				
			} catch (EdmException e) {
				throw ODataJPARuntimeException.throwException(
						ODataJPARuntimeException.GENERAL.addContent(e
								.getMessage()), e);
			}
			orderByMap.put(tableAlias+JPQLStatement.DELIMITER.PERIOD+propertyName, EMPTY);
		}
		return orderByMap;
	}

}
