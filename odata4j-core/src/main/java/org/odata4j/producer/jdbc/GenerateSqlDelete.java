package org.odata4j.producer.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.odata4j.core.ImmutableList;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.expression.BoolCommonExpression;
import org.odata4j.producer.jdbc.JdbcModel.JdbcTable;
import org.odata4j.producer.jdbc.SqlStatement.SqlParameter;

public class GenerateSqlDelete {

  public SqlStatement generate(JdbcMetadataMapping mapping, EdmEntitySet entitySet, BoolCommonExpression filter) {
    JdbcTable table = mapping.getMappedTable(entitySet);
    StringBuilder sql = new StringBuilder("DELETE FROM " + table.tableName);
    List<SqlParameter> params = new ArrayList<SqlParameter>();
    if (filter != null) {
      GenerateWhereClause whereClauseGen = newWhereClauseGenerator(entitySet, mapping);
      filter.visit(whereClauseGen);
      whereClauseGen.append(sql, params);
    }
    return new SqlStatement(sql.toString(), ImmutableList.copyOf(params));
  }

  public GenerateWhereClause newWhereClauseGenerator(EdmEntitySet entitySet, JdbcMetadataMapping mapping) {
    return new GenerateWhereClause(entitySet, mapping);
  }

}
