package com.sap.core.odata.processor.core.jpa.access.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotImplementedException;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.expression.BinaryExpression;
import com.sap.core.odata.api.uri.expression.BinaryOperator;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.LiteralExpression;
import com.sap.core.odata.api.uri.expression.MemberExpression;
import com.sap.core.odata.api.uri.expression.MethodExpression;
import com.sap.core.odata.api.uri.expression.MethodOperator;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.api.uri.expression.OrderExpression;
import com.sap.core.odata.api.uri.expression.PropertyExpression;
import com.sap.core.odata.api.uri.expression.SortOrder;
import com.sap.core.odata.api.uri.expression.UnaryExpression;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLStatement;

/**
 * This class contains utility methods for parsing the filter expressions built by core library from user OData Query.
 * 
 * @author SAP AG
 *
 */
public class ODataExpressionParser {
	
	public static final String EMPTY = "";	//$NON-NLS-1$
	public static Integer methodFlag = 0;
		
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
	      if((binaryExpression.getLeftOperand().getKind()==ExpressionKind.METHOD)&&((binaryExpression.getOperator()==BinaryOperator.EQ)||(binaryExpression.getOperator()==BinaryOperator.NE))&&(((MethodExpression)binaryExpression.getLeftOperand()).getMethod()==MethodOperator.SUBSTRINGOF)) {
	    	  methodFlag = 1;
	      }
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
		    
	    case METHOD:
	    	final MethodExpression methodExpression = (MethodExpression) whereExpression;
	    	String first = parseToJPAWhereExpression(methodExpression.getParameters().get(0),tableAlias);
	    	final String second = methodExpression.getParameterCount() > 1 ?
	        		parseToJPAWhereExpression(methodExpression.getParameters().get(1),tableAlias) : null;
	        String third = methodExpression.getParameterCount() > 2 ?
	        		parseToJPAWhereExpression(methodExpression.getParameters().get(2),tableAlias) : null;
	        		
	        switch (methodExpression.getMethod()) {
	        	case SUBSTRING:
	        		third = third != null ? ", " + third : "";
	        		return String.format("SUBSTRING(%s, %s + 1 %s)", first,second,third);
	        	case SUBSTRINGOF:
	        		first = first.substring(1,first.length()-1);
	        		if(methodFlag==1) {
	        			methodFlag = 0;
	        			return String.format("(CASE WHEN %s LIKE '%%%s%%' THEN TRUE ELSE FALSE END)", second, first);
	        		}
	        		else {
	        			return String.format("(CASE WHEN %s LIKE '%%%s%%' THEN TRUE ELSE FALSE END) = true", second, first);
	        		}
	        	case TOLOWER:
	                return String.format("LOWER(%s)", first);
	        	default:
			    	  throw new ODataNotImplementedException();
	        }
	    
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
		
		if((selectedFields==null)||(selectedFields.size()==0)) {
			return tableAlias;
		}
		
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
			
			if(edmSimpleType == EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance()
	            || edmSimpleType == EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance()) {
				literal = literal.substring(literal.indexOf('\''), literal.indexOf('}'));
			}
							
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
	 * @throws ODataJPARuntimeException 
	 */
	private static String evaluateComparingExpression(String value, EdmSimpleType edmSimpleType) throws ODataJPARuntimeException {
		
		if (edmSimpleType == EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()
	            || edmSimpleType == EdmSimpleTypeKind.Guid.getEdmSimpleTypeInstance())
		{
			value = "\'"+value+"\'";	//$NON-NLS-1$	//$NON-NLS-2$
		}else if(edmSimpleType == EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance()
	            || edmSimpleType == EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance()	)
		{
				try {
				Calendar datetime = (Calendar) edmSimpleType.valueOfString(value, EdmLiteralKind.DEFAULT, null, edmSimpleType.getDefaultType());
				
				String year = String.format("%04d", datetime.get(Calendar.YEAR));						
				String month = String.format("%02d", datetime.get(Calendar.MONTH)+1);
				String day = String.format("%02d", datetime.get(Calendar.DAY_OF_MONTH));
				String hour = String.format("%02d", datetime.get(Calendar.HOUR_OF_DAY));
				String min = String.format("%02d", datetime.get(Calendar.MINUTE));
				String sec = String.format("%02d", datetime.get(Calendar.SECOND));
				
				value = JPQLStatement.DELIMITER.LEFT_BRACE+JPQLStatement.KEYWORD.TIMESTAMP+JPQLStatement.DELIMITER.SPACE+"\'"+year+JPQLStatement.DELIMITER.HYPHEN+month+JPQLStatement.DELIMITER.HYPHEN+day+JPQLStatement.DELIMITER.SPACE+hour+JPQLStatement.DELIMITER.COLON+min+JPQLStatement.DELIMITER.COLON+sec+JPQLStatement.KEYWORD.OFFSET+"\'"+JPQLStatement.DELIMITER.RIGHT_BRACE;
				
				} catch (EdmSimpleTypeException e) {
					throw ODataJPARuntimeException.throwException(
							ODataJPARuntimeException.GENERAL.addContent(e
									.getMessage()), e);
				}			
				
		}else if(edmSimpleType == EdmSimpleTypeKind.Time.getEdmSimpleTypeInstance()){
				try {
				Calendar time = (Calendar) edmSimpleType.valueOfString(value, EdmLiteralKind.DEFAULT, null, edmSimpleType.getDefaultType());
								
				String hourValue = String.format("%02d", time.get(Calendar.HOUR_OF_DAY));
				String minValue = String.format("%02d", time.get(Calendar.MINUTE));
				String secValue = String.format("%02d", time.get(Calendar.SECOND));
				
				value = "\'"+hourValue+JPQLStatement.DELIMITER.COLON+minValue+JPQLStatement.DELIMITER.COLON+secValue+"\'";	
				} catch (EdmSimpleTypeException e) {
					throw ODataJPARuntimeException.throwException(
							ODataJPARuntimeException.GENERAL.addContent(e
									.getMessage()), e);
				}						
			
		}else if(edmSimpleType == EdmSimpleTypeKind.Int64.getEdmSimpleTypeInstance()){
			value = value+JPQLStatement.DELIMITER.LONG;	//$NON-NLS-1$
		}
		return value;
	}

	public static HashMap<String, String> parseKeyPropertiesToJPAOrderByExpression(List<EdmProperty> edmPropertylist, String tableAlias) throws ODataJPARuntimeException {
		HashMap<String, String> orderByMap = new HashMap<String, String>();
		String propertyName = null;		
		for(EdmProperty edmProperty : edmPropertylist){
			try {
				EdmMapping mapping = edmProperty.getMapping();
				if(mapping != null && mapping.getInternalName() != null){
					propertyName = mapping.getInternalName();// For embedded/complex keys
				}else{
					propertyName = edmProperty.getName();
				}
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
