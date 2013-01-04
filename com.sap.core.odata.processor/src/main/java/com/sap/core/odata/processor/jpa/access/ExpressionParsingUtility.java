package com.sap.core.odata.processor.jpa.access;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotImplementedException;
import com.sap.core.odata.api.uri.expression.BinaryExpression;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.LiteralExpression;
import com.sap.core.odata.api.uri.expression.MethodExpression;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.api.uri.expression.OrderExpression;
import com.sap.core.odata.api.uri.expression.OrderType;
import com.sap.core.odata.api.uri.expression.PropertyExpression;
import com.sap.core.odata.api.uri.expression.UnaryExpression;
import com.sap.core.odata.processor.jpa.jpql.api.JPQLStatement;

public class ExpressionParsingUtility {
	
	public static final String SPACE = " ";
	
	public static String parseWhereExpression(final CommonExpression whereExpression) throws ODataException {
	    switch (whereExpression.getKind()) {
	    case UNARY:
	      final UnaryExpression unaryExpression = (UnaryExpression) whereExpression;
	      final String operand = parseWhereExpression(unaryExpression.getOperand());

	      switch (unaryExpression.getOperator()) {
	      case NOT:
	        //return Boolean.toString(!Boolean.parseBoolean(operand));
	    	  return SPACE + JPQLStatement.Operator.NOT + SPACE + operand;
	      case MINUS:
	        if (operand.startsWith("-"))
	          return operand.substring(1);
	        else
	          return "-" + operand;
	      default:
	        throw new ODataNotImplementedException();
	      }
	      
	    case FILTER:
	    	final FilterExpression filterExpression = (FilterExpression)whereExpression;
	    	return parseWhereExpression(filterExpression.getExpression());
	    case BINARY:
	      final BinaryExpression binaryExpression = (BinaryExpression) whereExpression;
	      //final EdmSimpleType binaryType = (EdmSimpleType) binaryExpression.getEdmType();
	      final String left = parseWhereExpression(binaryExpression.getLeftOperand());
	      final String right = parseWhereExpression(binaryExpression.getRightOperand());

	      switch (binaryExpression.getOperator()) {
		      case ADD:
		        return Double.toString(Double.valueOf(left) + Double.valueOf(right));
		      case SUB:
		        return Double.toString(Double.valueOf(left) - Double.valueOf(right));
		      case MUL:
		        return Double.toString(Double.valueOf(left) * Double.valueOf(right));
		      case DIV:
		        return Double.toString(Double.valueOf(left) / Double.valueOf(right));
		      case MODULO:
		        return Double.toString(Double.valueOf(left) % Double.valueOf(right));
		      case AND:
		        //return Boolean.toString(left.equals("true") && right.equals("true"));
		    	  return left + SPACE + JPQLStatement.Operator.AND + SPACE + right; 
		      case OR:
		        //return Boolean.toString(left.equals("true") || right.equals("true"));
		    	  return left + SPACE + JPQLStatement.Operator.OR + SPACE + right;
		      case EQ:
		        //return Boolean.toString(left.equals(right));
		    	  return left + SPACE + JPQLStatement.Operator.EQ+ SPACE + right;
		      case NE:
		        //return Boolean.toString(!left.equals(right));
		    	  return left + SPACE + JPQLStatement.Operator.NE + SPACE + right;
		      case LT:
		        /*if (binaryType == EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()
		            || binaryType == EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance()
		            || binaryType == EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance()
		            || binaryType == EdmSimpleTypeKind.Guid.getEdmSimpleTypeInstance()
		            || binaryType == EdmSimpleTypeKind.Time.getEdmSimpleTypeInstance())
		          return Boolean.toString(left.compareTo(right) < 0);
		        else
		          return Boolean.toString(Double.valueOf(left) < Double.valueOf(right));*/
		    	  return left + SPACE + JPQLStatement.Operator.LT + SPACE + right;
		      case LE:
		        /*if (binaryType == EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()
		            || binaryType == EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance()
		            || binaryType == EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance()
		            || binaryType == EdmSimpleTypeKind.Guid.getEdmSimpleTypeInstance()
		            || binaryType == EdmSimpleTypeKind.Time.getEdmSimpleTypeInstance())
		          return Boolean.toString(left.compareTo(right) <= 0);
		        else
		          return Boolean.toString(Double.valueOf(left) <= Double.valueOf(right));*/
		    	  return left + SPACE + JPQLStatement.Operator.LE + SPACE + right;
		      case GT:
		        /*if (binaryType == EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()
		            || binaryType == EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance()
		            || binaryType == EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance()
		            || binaryType == EdmSimpleTypeKind.Guid.getEdmSimpleTypeInstance()
		            || binaryType == EdmSimpleTypeKind.Time.getEdmSimpleTypeInstance())
		          return Boolean.toString(left.compareTo(right) > 0);
		        else
		          return Boolean.toString(Double.valueOf(left) > Double.valueOf(right));*/
		    	  return left + SPACE + JPQLStatement.Operator.GT + SPACE + right;
		      case GE:
		        /*if (binaryType == EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()
		            || binaryType == EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance()
		            || binaryType == EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance()
		            || binaryType == EdmSimpleTypeKind.Guid.getEdmSimpleTypeInstance()
		            || binaryType == EdmSimpleTypeKind.Time.getEdmSimpleTypeInstance())
		          return Boolean.toString(left.compareTo(right) >= 0);
		        else
		          return Boolean.toString(Double.valueOf(left) >= Double.valueOf(right));*/
		    	  return left + SPACE + JPQLStatement.Operator.GE + SPACE + SPACE + right;
		      case PROPERTY_ACCESS:
		        throw new ODataNotImplementedException();
		      default:
		        throw new ODataNotImplementedException();
	      }

	    case PROPERTY:
//	      final EdmProperty property = ((PropertyExpression) whereExpression).getEdmProperty();
//	      if (property == null)
//	        return "";
	      //final EdmSimpleType type = (EdmSimpleType) property.getType();
	      //return type.valueToString(getPropertyValue(data, property), EdmLiteralKind.DEFAULT, property.getFacets());
	    	return ((PropertyExpression) whereExpression).getPropertyName();//TODO - check

//	    case MEMBER:
//	      final MemberExpression memberExpression = (MemberExpression) expression;
//	      final PropertyExpression memberPath = (PropertyExpression) memberExpression.getPath();
//	      final EdmProperty memberProperty = memberPath.getEdmProperty();
//	      final EdmSimpleType memberType = (EdmSimpleType) memberPath.getEdmType();
//	      List<EdmProperty> propertyPath = new ArrayList<EdmProperty>();
//	      CommonExpression currentExpression = memberExpression;
//	      while (currentExpression != null && currentExpression.getKind() == ExpressionKind.MEMBER) {
//	        final MemberExpression currentMember = (MemberExpression) currentExpression;
//	        propertyPath.add(0, ((PropertyExpression) currentMember.getProperty()).getEdmProperty());
//	        currentExpression = currentMember.getPath();
//	      }
//	      return memberType.valueToString(getPropertyValue(data, propertyPath), EdmLiteralKind.DEFAULT, memberProperty.getFacets());

	    case LITERAL:
	    	final LiteralExpression literal = (LiteralExpression) whereExpression;
		      final EdmSimpleType literalType = (EdmSimpleType) literal.getEdmType();
		      return literalType.valueToString(literalType.valueOfString(literal.getUriLiteral(), EdmLiteralKind.URI, null), EdmLiteralKind.DEFAULT, null);
//		      return literalType.getName(); //TODO

	    case METHOD:
	      final MethodExpression methodExpression = (MethodExpression) whereExpression;
	      final String first = parseWhereExpression(methodExpression.getParameters().get(0));
	      final String second = methodExpression.getParameterCount() > 1 ?
	    		  parseWhereExpression(methodExpression.getParameters().get(1)) : null;
	      final String third = methodExpression.getParameterCount() > 2 ?
	    		  parseWhereExpression(methodExpression.getParameters().get(2)) : null;

	      switch (methodExpression.getMethod()) {
		      case ENDSWITH:
		        return Boolean.toString(first.endsWith(second));
		      case INDEXOF:
		        return Integer.toString(first.indexOf(second));
		      case STARTSWITH:
		        return Boolean.toString(first.startsWith(second));
		      case TOLOWER:
		        return first.toLowerCase(Locale.ROOT);
		      case TOUPPER:
		        return first.toUpperCase(Locale.ROOT);
		      case TRIM:
		        return first.trim();
		      case SUBSTRING:
		        final int offset = first.indexOf(second);
		        return first.substring(offset, offset + Integer.parseInt(third));
		      case SUBSTRINGOF:
		        return Boolean.toString(first.contains(second));
		      case CONCAT:
		        return first + second;
		      case LENGTH:
		        return Integer.toString(first.length());
		      case YEAR:
		        return String.valueOf(Integer.parseInt(first.substring(0, 4)));
		      case MONTH:
		        return String.valueOf(Integer.parseInt(first.substring(5, 7)));
		      case DAY:
		        return String.valueOf(Integer.parseInt(first.substring(8, 10)));
		      case HOUR:
		        return String.valueOf(Integer.parseInt(first.substring(11, 13)));
		      case MINUTE:
		        return String.valueOf(Integer.parseInt(first.substring(14, 16)));
		      case SECOND:
		        return String.valueOf(Integer.parseInt(first.substring(17, 19)));
		      case ROUND:
		        return Long.toString(Math.round(Double.valueOf(first)));
		      case FLOOR:
		        return Long.toString(Math.round(Math.floor(Double.valueOf(first))));
		      case CEILING:
		        return Long.toString(Math.round(Math.ceil(Double.valueOf(first))));
		      default:
		        throw new ODataNotImplementedException();
	      }

	    default:
	      throw new ODataNotImplementedException();
	    }
	  }
	
	public static HashMap<String, String> parseOrderByExpression(OrderByExpression orderByExpression){
		HashMap<String, String> orderByMap = new HashMap<String, String>();
		if(orderByExpression != null && orderByExpression.getOrders() != null ){
			List<OrderExpression> orderBys= orderByExpression.getOrders();
			String orderByField = null;
			String orderByDirection = null;
			for(OrderExpression orderBy : orderBys){
				
				try {
					orderByField = orderBy.getExpression().getEdmType().getName();
					orderByDirection = (orderBy.getSortOrder() == OrderType.asc)? "" : "DESC";
					orderByMap.put(orderByField, orderByDirection);
				} catch (EdmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}			
		} 
		return orderByMap;		
	}

}
