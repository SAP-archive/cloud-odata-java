package org.odata4j.producer.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.odata4j.core.ImmutableList;
import org.odata4j.core.OEntity;
import org.odata4j.core.OProperty;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.edm.EdmProperty;
import org.odata4j.producer.jdbc.JdbcModel.JdbcColumn;
import org.odata4j.producer.jdbc.JdbcModel.JdbcTable;
import org.odata4j.producer.jdbc.SqlStatement.SqlParameter;

public class GenerateSqlInsert {

  public SqlStatement generate(JdbcMetadataMapping mapping, EdmEntitySet entitySet, OEntity entity) {
    JdbcTable table = mapping.getMappedTable(entitySet);
    StringBuilder sql = new StringBuilder("INSERT INTO " + table.tableName + "(");
    StringBuilder columns = new StringBuilder();
    StringBuilder values = new StringBuilder();
    List<SqlParameter> params = new ArrayList<SqlParameter>();
    for (OProperty<?> prop : entity.getProperties()) {
      if (columns.length() > 0)
        columns.append(", ");
      if (values.length() > 0)
        values.append(", ");
      EdmProperty edmProp = entitySet.getType().findProperty(prop.getName());
      JdbcColumn column = mapping.getMappedColumn(edmProp);
      columns.append(column.columnName);
      values.append("?");
      params.add(new SqlParameter(prop.getValue(), null));
    }
    sql.append(columns);
    sql.append(") VALUES (");
    sql.append(values);
    sql.append(")");

    return new SqlStatement(sql.toString(), ImmutableList.copyOf(params));
  }

}
