/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
    while (candidate != null && !(candidate instanceof ExpressionParserException)) {
      candidate = candidate.getCause();
    }
    this.exception = (ExpressionParserException) candidate;
  }

  @Override
  public String getName() {
    return "URI";
  }

  @Override
  public void appendJson(final JsonStreamWriter jsonStreamWriter) throws IOException {
    jsonStreamWriter.beginObject();

    if (exception != null) {
      jsonStreamWriter.name("error")
          .beginObject();
      if (exception.getFilterTree() != null) {
        jsonStreamWriter.namedStringValue("filter", exception.getFilterTree().getUriLiteral());
      }
      jsonStreamWriter.endObject();
    }

    if (uriInfo != null) {
      if (exception != null
          && (uriInfo.getFilter() != null || uriInfo.getOrderBy() != null
              || !uriInfo.getExpand().isEmpty() || !uriInfo.getSelect().isEmpty())) {
        jsonStreamWriter.separator();
      }

      final FilterExpression filter = uriInfo.getFilter();
      if (filter != null) {
        String filterString;
        try {
          filterString = (String) filter.accept(new JsonVisitor());
        } catch (final ExceptionVisitExpression e) {
          filterString = null;
        } catch (final ODataApplicationException e) {
          filterString = null;
        }
        jsonStreamWriter.name("filter").unquotedValue(filterString);
        if (uriInfo.getOrderBy() != null
            || !uriInfo.getExpand().isEmpty() || !uriInfo.getSelect().isEmpty()) {
          jsonStreamWriter.separator();
        }
      }

      final OrderByExpression orderBy = uriInfo.getOrderBy();
      if (orderBy != null) {
        String orderByString;
        try {
          orderByString = (String) orderBy.accept(new JsonVisitor());
        } catch (final ExceptionVisitExpression e) {
          orderByString = null;
        } catch (final ODataApplicationException e) {
          orderByString = null;
        }
        jsonStreamWriter.name("orderby").unquotedValue(orderByString);
        if (!uriInfo.getExpand().isEmpty() || !uriInfo.getSelect().isEmpty()) {
          jsonStreamWriter.separator();
        }
      }

      if (!uriInfo.getExpand().isEmpty() || !uriInfo.getSelect().isEmpty()) {
        String expandSelectString;
        try {
          ExpandSelectTreeCreator expandSelectCreator = new ExpandSelectTreeCreator(uriInfo.getSelect(), uriInfo.getExpand());
          final ExpandSelectTreeNodeImpl expandSelectTree = expandSelectCreator.create();
          expandSelectString = expandSelectTree.toJsonString();
        } catch (final EdmException e) {
          expandSelectString = null;
        }
        jsonStreamWriter.name("expand/select").unquotedValue(expandSelectString);
      }
    }

    jsonStreamWriter.endObject();
  }
}
