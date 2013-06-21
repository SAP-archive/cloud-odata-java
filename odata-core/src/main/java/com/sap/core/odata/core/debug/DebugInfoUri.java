package com.sap.core.odata.core.debug;

import java.io.IOException;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.uri.UriInfo;
import com.sap.core.odata.api.uri.expression.ExceptionVisitExpression;
import com.sap.core.odata.api.uri.expression.ExpressionParserException;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.core.ep.util.JsonStreamWriter;
import com.sap.core.odata.core.uri.ExpandSelectTreeCreator;
import com.sap.core.odata.core.uri.ExpandSelectTreeNodeImpl;
import com.sap.core.odata.core.uri.expression.JsonVisitor;

/**
 * @author SAP AG
 */
public class DebugInfoUri implements DebugInfo {

  private final UriInfo uriInfo;
  private final ExpressionParserException exception;

  public DebugInfoUri(final UriInfo uriInfo, final Exception exception) {
    this.uriInfo = uriInfo;

    Throwable candidate = exception;
    while (candidate != null && !(candidate instanceof ExpressionParserException))
      candidate = candidate.getCause();
    this.exception = (ExpressionParserException) candidate;
  }

  @Override
  public String getName() {
    return "URI";
  }

  @Override
  public void appendJson(JsonStreamWriter jsonStreamWriter) throws IOException {
    jsonStreamWriter.beginObject();
    if (exception != null) {
      jsonStreamWriter.name("error");
      if (exception.getFilterTree() == null) {
        jsonStreamWriter.unquotedValue(null);
      } else {
        jsonStreamWriter.beginObject();
        jsonStreamWriter.namedStringValue("filter", exception.getFilterTree().getUriLiteral());
        jsonStreamWriter.endObject();
      }
      jsonStreamWriter.separator();
    }

    if (uriInfo == null) {
      jsonStreamWriter.name("uriInfo");
      jsonStreamWriter.unquotedValue(null);
    } else {
      final FilterExpression filter = uriInfo.getFilter();
      if (filter != null) {
        jsonStreamWriter.name("filter");
        String filterString;
        try {
          filterString = (String) filter.accept(new JsonVisitor());
        } catch (final ExceptionVisitExpression e) {
          filterString = null;
        } catch (final ODataApplicationException e) {
          filterString = null;
        }
        jsonStreamWriter.unquotedValue(filterString);
        jsonStreamWriter.separator();
      }

      final OrderByExpression orderBy = uriInfo.getOrderBy();
      if (orderBy != null) {
        jsonStreamWriter.name("orderby");
        String orderByString;
        try {
          orderByString = (String) orderBy.accept(new JsonVisitor());
        } catch (final ExceptionVisitExpression e) {
          orderByString = null;
        } catch (final ODataApplicationException e) {
          orderByString = null;
        }
        jsonStreamWriter.unquotedValue(orderByString);
        jsonStreamWriter.separator();
      }

      jsonStreamWriter.name("expand/select");
      try {
        ExpandSelectTreeCreator expandSelectCreator = new ExpandSelectTreeCreator(uriInfo.getSelect(), uriInfo.getExpand());
        final ExpandSelectTreeNodeImpl expandSelectTree = expandSelectCreator.create();
        jsonStreamWriter.unquotedValue(expandSelectTree == null ? null : expandSelectTree.toJsonString());
      } catch (final EdmException e) {
        jsonStreamWriter.unquotedValue(null);
      }
    }

    jsonStreamWriter.endObject();
  }
}
