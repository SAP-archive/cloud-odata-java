package com.sap.core.odata.core.uri.expression;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExpressionParserException;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.api.uri.expression.OrderByParser;
import com.sap.core.odata.api.uri.expression.SortOrder;

public class OrderByParserImpl extends FilterParserImpl implements OrderByParser
{
  public OrderByParserImpl(Edm edm, EdmEntityType edmType)
  {
    super(edm, edmType);
  }

  @Override
  public OrderByExpression parseOrderByString(String orderByExpression) throws ExpressionParserException, ExpressionParserInternalError
  {
    OrderByExpressionImpl orderCollection = new OrderByExpressionImpl();
    curExpression = orderByExpression;

    try
    {
      tokenList = new Tokenizer(orderByExpression).tokenize(); //throws TokenizerMessage
    } catch (TokenizerException tokenizerException)
    {
      throw FilterParserExceptionImpl.createERROR_IN_TOKENIZER(tokenizerException);
    }

    boolean weiter = false;

    while (weiter == false)
    {
      CommonExpression node = null;
      try
      {
        CommonExpression nodeLeft = readElement(null);
        node = readElements(nodeLeft, 0);
      } catch (ExpressionParserException expressionException)
      {
        expressionException.setFilterTree(orderCollection);
        throw expressionException;
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
        // Tested with TestParserExceptions.TestOPMparseOrderByString CASE 1
        throw FilterParserExceptionImpl.createINVALID_SORT_ORDER(token, curExpression);
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
        Token oldToken = token;
        tokenList.next();
        token = tokenList.lookToken();

        if (token == null)
        {
          // Tested with TestParserExceptions.TestOPMparseOrderByString CASE 2
          throw FilterParserExceptionImpl.createEXPRESSION_EXPECTED_AFTER_POS(oldToken,curExpression);
        }
      }

    }
    return orderCollection;
  }
}
