package com.sap.core.odata.core.uri.expression;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.FilterParserException;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.api.uri.expression.OrderByParser;
import com.sap.core.odata.api.uri.expression.OrderByParserException;
import com.sap.core.odata.api.uri.expression.SortOrder;

public class OrderByParserImpl extends FilterParserImpl implements OrderByParser
{
  /*instance attributes*/

  public OrderByParserImpl(Edm edm, EdmEntityType edmType)
  {
    super(edm, edmType);
  }

  @Override
  public OrderByExpression parseOrderByString(String orderByExpression) throws OrderByParserException, FilterParserInternalError
  {
    OrderByExpressionImpl orderCollection = new OrderByExpressionImpl();

    try
    {
      tokenList = new Tokenizer(orderByExpression).tokenize(); //throws TokenizerMessage
    } catch (TokenizerException tokenizerException)
    {
      //throw FilterParserExceptionImpl.createERROR_IN_TOKENIZER(tokenizerException);
      //TODO change name
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    boolean weiter = false;

    while (weiter == false)
    {
      CommonExpression node = null;
      try
      {
        CommonExpression nodeLeft = readElement(null);
        node = readElements(nodeLeft, 0);
      } catch (FilterParserException expressionException)
      {
        //TODO change name
        FilterExpression fe = new FilterExpressionImpl(orderByExpression, null);
        //throw expressionException.setFilterTree(fe);
      }

      OrderExpressionImpl orderNode = new OrderExpressionImpl(node);

      //read the sort order
      Token token = tokenList.lookToken();
      if (token == null)
      {
        orderNode.setSortOrder(SortOrder.asc);
      }
      else if ((token.getKind() == TokenKind.LITERAL) && (token.getUriLiteral().equals("asc")))
      {
        orderNode.setSortOrder(SortOrder.asc);
        tokenList.next();
        token = tokenList.lookToken();
      }
      else if ((token.getKind() == TokenKind.LITERAL) && (token.getUriLiteral().equals("desc")))
      {
        orderNode.setSortOrder(SortOrder.desc);
        tokenList.next();
        token = tokenList.lookToken();
      }
      else if (token.getKind() == TokenKind.COMMA)
      {
        orderNode.setSortOrder(SortOrder.asc);
        tokenList.next();
        token = tokenList.lookToken();
      }
      else
      {
        //TODO add syntax error
      }

      orderCollection.addOrder(orderNode);

      //ls_token may be a ',' or  empty.
      if (token == null)
      {
        weiter = true;
        break;
      }
      else if (token.getKind() == TokenKind.COMMA)
      {
        tokenList.next();
        token = tokenList.lookToken();
        /*
         * error xmlns="http://schemas.microsoft.com/ado/2007/08/dataservices/metadata">
        <code/>
        <message xml:lang="en-US">Expression expected at position 10.</message>
        </error>
         */
      }
    }
    return orderCollection;
  }
}
